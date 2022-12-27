package com.mind.market.translator_kmm.core.presentation

import com.mind.market.translator_kmm.core.domain.language.Language

actual class UiLanguage(
    actual val language: Language,
    val imageName: String
) {
    actual companion object {
        actual fun byCode(languageCode: String): UiLanguage {
            return allLanguages.find { uiLanguage ->
                uiLanguage.language.langCode == languageCode
            } ?: throw IllegalArgumentException("Invalid or unsupported language code")
        }

        actual val allLanguages: List<UiLanguage>
            get() = Language.values().map { language ->
                UiLanguage(
                    language = language,
                    imageName = language.langName.lowercase()
                )
            }
    }
}