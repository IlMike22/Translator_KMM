package com.mind.market.translator_kmm.core.presentation

import com.mind.market.translator_kmm.core.domain.language.Language

expect class UiLanguage {
    val language:Language

    companion object {
        fun byCode(languageCode:String):UiLanguage
        val allLanguages:List<UiLanguage>
    }
}