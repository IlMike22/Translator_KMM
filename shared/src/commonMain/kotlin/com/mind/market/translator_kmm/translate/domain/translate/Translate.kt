package com.mind.market.translator_kmm.translate.domain.translate

import com.mind.market.translator_kmm.core.domain.language.Language
import com.mind.market.translator_kmm.core.domain.util.Resource
import com.mind.market.translator_kmm.translate.domain.history.HistoryItem
//import com.mind.market.translator_kmm.translate.domain.history.IHistoryDataSource

class Translate(
//    private val historyDataSource: IHistoryDataSource,
    private val translateClient: ITranslateClient
) {
    suspend fun execute(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): Resource<String> {
        return try {
            val translatedText = translateClient.translate(fromLanguage, fromText, toLanguage)
//            historyDataSource.insertHistoryItem(
//                HistoryItem(
//                    id = null,
//                    fromLanguageCode = fromLanguage.langCode,
//                    fromText = fromText,
//                    toLanguageCode = toLanguage.langCode,
//                    toText = translatedText
//                )
//            )
            Resource.Success(translatedText)
        } catch (exception: TranslateException) {
            Resource.Error(exception)
        }
    }
}