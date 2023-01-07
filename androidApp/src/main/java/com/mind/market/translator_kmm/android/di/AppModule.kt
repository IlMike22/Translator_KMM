package com.mind.market.translator_kmm.android.di

import android.app.Application
//import com.mind.market.translator_kmm.translate.data.history.SqlDelightHistoryDataSource
//import com.mind.market.translator_kmm.translate.data.local.DatabaseDriverFactory
import com.mind.market.translator_kmm.translate.data.remote.HttpClientFactory
import com.mind.market.translator_kmm.translate.data.translate.KtorTranslateClient
//import com.mind.market.translator_kmm.translate.domain.history.IHistoryDataSource
import com.mind.market.translator_kmm.translate.domain.translate.ITranslateClient
import com.mind.market.translator_kmm.translate.domain.translate.Translate
//import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClientFactory().create()
    }

    @Provides
    @Singleton
    fun provideTranslateClient(httpClient: HttpClient): ITranslateClient {
        return KtorTranslateClient(httpClient)
    }

//    @Provides
//    @Singleton
//    fun provideDatabaseDriver(app: Application): SqlDriver {
//        return DatabaseDriverFactory(app).create()
//    }

//    @Provides
//    @Singleton
//    fun provideHistoryDataSource(driver: SqlDriver): IHistoryDataSource {
//        return SqlDelightHistoryDataSource(db = TranslateDatabase(driver))
//    }

    @Provides
    @Singleton
    fun provideTranslateUseCase(
        client: ITranslateClient,
//        dataSource: IHistoryDataSource
    ): Translate {
        return Translate(
//            historyDataSource = dataSource,
            translateClient = client
        )
    }
}