package com.mind.market.translator_kmm.voice_to_text.domain

import com.mind.market.translator_kmm.core.domain.util.CommonStateFlow

interface IVoiceToTextParser {
    val state: CommonStateFlow<VoiceToTextParserState>

    fun startListening(languageCode: String)
    fun stopListening()
    fun cancel()
    fun reset()
}