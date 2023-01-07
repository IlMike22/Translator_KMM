package com.mind.market.translator_kmm.android.voice_to_text.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mind.market.translator_kmm.voice_to_text.domain.IVoiceToTextParser
import com.mind.market.translator_kmm.voice_to_text.presentation.VoiceToTextEvent
import com.mind.market.translator_kmm.voice_to_text.presentation.VoiceToTextViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AndroidVoiceToTextViewModel @Inject constructor(
    private val parser: IVoiceToTextParser
): ViewModel() {
    private val viewModel by lazy {
        VoiceToTextViewModel(
            parser = parser,
            scope = viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event:VoiceToTextEvent) {
        viewModel.onEvent(event)
    }
}