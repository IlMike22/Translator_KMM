package com.mind.market.translator_kmm.voice_to_text.presentation

import com.mind.market.translator_kmm.core.domain.util.toCommonStateFlow
import com.mind.market.translator_kmm.voice_to_text.domain.IVoiceToTextParser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class VoiceToTextViewModel(
    private val parser: IVoiceToTextParser,
    private val scope: CoroutineScope? = null
) {
    private val viewModelScope = scope ?: CoroutineScope(Dispatchers.Main)
    private val _state = MutableStateFlow(VoiceToTextState())
    val state = _state.combine(parser.state) { state, voiceResult ->
        state.copy(
            spokenText = voiceResult.result,
            canRecord = voiceResult.isSpeaking,
            displayState = when {
                voiceResult.error != null -> DisplayState.ERROR
                voiceResult.result.isNotBlank() && voiceResult.isSpeaking.not() -> DisplayState.DISPLAYING_RESULTS
                voiceResult.isSpeaking -> DisplayState.SPEAKING
                else -> DisplayState.WAITING_TO_TALK
            }
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), VoiceToTextState())
        .toCommonStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                if (state.value.displayState == DisplayState.SPEAKING) {
                    _state.update {
                        it.copy(
                            powerRatios = it.powerRatios + parser.state.value.powerRatio
                        )
                    }
                    delay(50L)
                }
            }
        }
    }

    fun onEvent(event: VoiceToTextEvent) {
        when (event) {
            is VoiceToTextEvent.PermissionResult -> {
                _state.update { it.copy(canRecord = event.isGranted) }
            }
            VoiceToTextEvent.Reset -> {
                parser.reset()
                _state.value = VoiceToTextState()
            }
            is VoiceToTextEvent.ToggleRecording -> toggleRecording(event.languageCode)
            else -> Unit
        }
    }

    private fun toggleRecording(languageCode: String) {
        parser.cancel()
        if (state.value.displayState == DisplayState.SPEAKING) {
            parser.stopListening()
        } else {
            parser.startListening(languageCode)
        }
    }
}