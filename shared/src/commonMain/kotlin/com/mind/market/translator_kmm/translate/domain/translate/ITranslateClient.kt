package com.mind.market.translator_kmm.translate.domain.translate

import com.mind.market.translator_kmm.core.domain.language.Language

interface ITranslateClient {
    suspend fun translate(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): String
}