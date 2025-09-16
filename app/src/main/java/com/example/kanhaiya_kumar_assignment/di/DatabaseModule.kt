package com.example.kanhaiya_kumar_assignment.di

import android.content.Context
import androidx.room.Room
import com.example.kanhaiya_kumar_assignment.data.local.dao.ExpenseDao
import com.example.kanhaiya_kumar_assignment.data.local.database.ExpenseDatabase
import com.example.kanhaiya_kumar_assignment.data.repository.ExpenseRepositoryImpl
import com.example.kanhaiya_kumar_assignment.domain.repository.ExpenseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideExpenseDatabase(@ApplicationContext context: Context): ExpenseDatabase {
        return Room.databaseBuilder(
            context,
            ExpenseDatabase::class.java,
            ExpenseDatabase.DATABASE_NAME
        ).build()
    }
    
    @Provides
    fun provideExpenseDao(database: ExpenseDatabase): ExpenseDao {
        return database.expenseDao()
    }
    
    @Provides
    @Singleton
    fun provideExpenseRepository(expenseDao: ExpenseDao): ExpenseRepository {
        return ExpenseRepositoryImpl(expenseDao)
    }
}
