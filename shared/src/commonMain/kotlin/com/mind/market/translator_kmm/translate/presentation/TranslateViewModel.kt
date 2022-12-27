package com.mind.market.translator_kmm.translate.presentation

import com.mind.market.translator_kmm.core.domain.util.Resource
import com.mind.market.translator_kmm.core.domain.util.toCommonStateFlow
import com.mind.market.translator_kmm.core.presentation.UiLanguage
import com.mind.market.translator_kmm.translate.domain.history.IHistoryDataSource
import com.mind.market.translator_kmm.translate.domain.translate.Translate
import com.mind.market.translator_kmm.translate.domain.translate.TranslateException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TranslateViewModel(
    private val translateUseCase: Translate,
    private val historyDataSource: IHistoryDataSource,
    private val coroutineScope: CoroutineScope?
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(TranslateState())
    val state = combine(
        _state,
        historyDataSource.getHistory()
    ) { state, history ->
        if (state.history != history) {
            state.copy(
                history = history.mapNotNull { historyItem ->
                    UiHistoryItem(
                        id = historyItem.id ?: return@mapNotNull null,
                        fromText = historyItem.fromText,
                        toText = historyItem.toText,
                        fromLanguage = UiLanguage.byCode(historyItem.fromLanguageCode),
                        toLanguage = UiLanguage.byCode(historyItem.toLanguageCode)
                    )
                }
            )
        } else state
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        TranslateState()
    ).toCommonStateFlow()

    private var translateJob: Job? = null

    fun onEvent(event: TranslateEvent) {
        when (event) {
            TranslateEvent.Translate -> translate(state.value)
            TranslateEvent.EditTranslation -> {
                if (state.value.toText != null) {
                    _state.update { state ->
                        state.copy(
                            isTranslating = false,
                            toText = null
                        )
                    }
                }
            }
            TranslateEvent.CloseTranslation -> {
                _state.update { state ->
                    state.copy(
                        isTranslating = false,
                        fromText = "",
                        toText = null
                    )
                }
            }
            TranslateEvent.OnErrorSeen -> {
                _state.update { state ->
                    state.copy(error = null)
                }
            }
            TranslateEvent.OpenFromLanguageDropdown -> {
                _state.update { state ->
                    state.copy(isChoosingFromLanguage = true)
                }
            }
            TranslateEvent.OpenToLanguageDropdown -> {
                _state.update { state ->
                    state.copy(isChoosingToLanguage = true)
                }
            }
            TranslateEvent.StopChoosingLanguage -> {
                _state.update { state ->
                    state.copy(
                        isChoosingToLanguage = false,
                        isChoosingFromLanguage = false
                    )
                }
            }
            is TranslateEvent.ChangeTranslationText -> {
                _state.update { state ->
                    state.copy(fromText = event.text)
                }
            }
            is TranslateEvent.ChooseFromLanguage -> {
                _state.update { state ->
                    state.copy(
                        isChoosingFromLanguage = false,
                        fromLanguage = event.language
                    )
                }
            }
            is TranslateEvent.ChooseToLanguage -> {
                val newState = _state.updateAndGet { state ->
                    state.copy(
                        isChoosingToLanguage = false,
                        toLanguage = event.language
                    )
                }

                translate(newState)
            }
            is TranslateEvent.SelectHistoryItem -> {
                translateJob?.cancel()

                _state.update { state ->
                    state.copy(
                        fromLanguage = event.item.fromLanguage,
                        toLanguage = event.item.toLanguage,
                        toText = event.item.toText,
                        fromText = event.item.fromText,
                        isTranslating = false
                    )
                }
            }
            is TranslateEvent.SubmitVoiceResult -> {
                _state.update { state ->
                    state.copy(
                        fromText = event.result ?: state.fromText,
                        isTranslating = if (event.result != null) false else state.isTranslating,
                        toText = if (event.result != null) null else state.toText
                    )
                }
            }
            TranslateEvent.SwapLanguages -> {
                _state.update { state ->
                    state.copy(
                        fromLanguage = state.toLanguage,
                        toLanguage = state.fromLanguage,
                        fromText = state.toText ?: "",
                        toText = if (state.toText != null) state.fromText else null
                    )
                }
            }
            else -> Unit
        }
    }

    private fun translate(state: TranslateState) {
        if (state.isTranslating || state.fromText.isBlank()) {
            return
        }

        translateJob = viewModelScope.launch {
            _state.update { state ->
                state.copy(isTranslating = true)
            }
            val result = translateUseCase.execute(
                fromLanguage = state.fromLanguage.language,
                toLanguage = state.toLanguage.language,
                fromText = state.fromText
            )

            when (result) {
                is Resource.Success -> {
                    _state.update { state ->
                        state.copy(
                            isTranslating = false,
                            toText = result.data
                        )
                    }
                }
                is Resource.Error -> {
                    _state.update { state ->
                        state.copy(
                            isTranslating = false,
                            error = (result.throwable as? TranslateException)?.error
                        )
                    }
                }
            }
        }
    }
}