package com.mind.market.translator_kmm.translate.data.translate

import com.mind.market.translator_kmm.NetworkConstants
import com.mind.market.translator_kmm.core.domain.language.Language
import com.mind.market.translator_kmm.translate.domain.translate.ITranslateClient
import com.mind.market.translator_kmm.translate.domain.translate.TranslateError
import com.mind.market.translator_kmm.translate.domain.translate.TranslateException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.errors.*

class KtorTranslateClient(
    private val httpClient: HttpClient
) : ITranslateClient {
    override suspend fun translate(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): String {
        val result = try {
            httpClient.post {
                url(NetworkConstants.BASE_URL + "/translate")
                contentType(ContentType.Application.Json)
                setBody(
                    TranslateDto(
                        textToTranslate = fromText,
                        sourceLanguageCode = fromLanguage.langCode,
                        targetLanguageCode = toLanguage.langCode
                    )
                )
            }

        } catch (exception: IOException) {
            throw TranslateException(TranslateError.SERVICE_UNAVAILABLE)
        }

        when (result.status.value) {
            in 200..299 -> Unit
            500 -> throw TranslateException(TranslateError.SERVER_ERROR)
            in 400..499 -> throw TranslateException(TranslateError.CLIENT_ERROR)
            else -> throw TranslateException(TranslateError.UNKNOWN)
        }
        return try {
            result.body<TranslatedDto>().translatedText
        } catch (exception: Exception) {
            throw TranslateException(TranslateError.SERVER_ERROR)
        }
    }
}