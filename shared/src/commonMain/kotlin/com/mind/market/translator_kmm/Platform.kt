package com.mind.market.translator_kmm

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform