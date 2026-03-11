package edu.feup.spendly.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import edu.feup.spendly.data.connectivity.ConnectivityObserver
import edu.feup.spendly.data.connectivity.NetworkConnectivityObserver
import edu.feup.spendly.data.local.SpendlyDatabase
import edu.feup.spendly.data.local.dao.ExpenseDao
import edu.feup.spendly.data.remote.api.ExpenseApi
import edu.feup.spendly.data.repository.ExpenseRepositoryImpl
import edu.feup.spendly.data.repository.UserPreferencesRepository
import edu.feup.spendly.domain.repository.ExpenseRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SpendlyDatabase {
        return Room.databaseBuilder(
            context,
            SpendlyDatabase::class.java,
            SpendlyDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideExpenseDao(db: SpendlyDatabase): ExpenseDao = db.expenseDao

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideExpenseApi(okHttpClient: OkHttpClient): ExpenseApi {
        val json = Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }
        val contentType = "application/json".toMediaType()
        
        return Retrofit.Builder()
            .baseUrl("https://firestore.googleapis.com/v1/projects/YOUR_PROJECT_ID/databases/(default)/documents/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
            .create(ExpenseApi::class.java)
    }

    @Provides
    @Singleton
    fun provideExpenseRepository(
        dao: ExpenseDao,
        api: ExpenseApi
    ): ExpenseRepository {
        return ExpenseRepositoryImpl(dao, api)
    }

    @Provides
    @Singleton
    fun provideConnectivityObserver(@ApplicationContext context: Context): ConnectivityObserver {
        return NetworkConnectivityObserver(context)
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(@ApplicationContext context: Context): UserPreferencesRepository {
        return UserPreferencesRepository(context)
    }
}
