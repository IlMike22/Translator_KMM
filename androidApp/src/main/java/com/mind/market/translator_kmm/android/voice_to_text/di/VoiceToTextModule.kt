package com.mind.market.translator_kmm.android.voice_to_text.di

import android.app.Application
import com.mind.market.translator_kmm.android.voice_to_text.data.AndroidVoiceToTextParser
import com.mind.market.translator_kmm.voice_to_text.domain.IVoiceToTextParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object VoiceToTextModule {
    @Provides
    @ViewModelScoped
    fun provideVoiceToTextParser(app: Application): IVoiceToTextParser {
        return AndroidVoiceToTextParser(app)
    }
}