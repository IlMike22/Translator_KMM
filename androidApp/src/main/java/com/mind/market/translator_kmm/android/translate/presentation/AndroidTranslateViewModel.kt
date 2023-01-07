package com.mind.market.translator_kmm.android.translate.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.mind.market.translator_kmm.translate.domain.history.IHistoryDataSource
import com.mind.market.translator_kmm.translate.domain.translate.Translate
import com.mind.market.translator_kmm.translate.presentation.TranslateEvent
import com.mind.market.translator_kmm.translate.presentation.TranslateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidTranslateViewModel @Inject constructor(
    private val translateUseCase: Translate,
//    private val historyDataSource: IHistoryDataSource
) : ViewModel() {
    private val viewModel by lazy {
        TranslateViewModel(
            translateUseCase = translateUseCase,
//            historyDataSource = historyDataSource,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: TranslateEvent) {
        viewModel.onEvent(event)
    }
}