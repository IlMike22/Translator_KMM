package com.mind.market.translator_kmm.translate.domain.translate

enum class TranslateError {
    SERVICE_UNAVAILABLE,
    CLIENT_ERROR,
    SERVER_ERROR,
    UNKNOWN
}

class TranslateException(val error: TranslateError) : Exception(
    "An error occurred when translating $error"
)