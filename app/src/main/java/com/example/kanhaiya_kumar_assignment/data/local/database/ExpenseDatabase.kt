package com.example.kanhaiya_kumar_assignment.data.local.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.example.kanhaiya_kumar_assignment.data.local.dao.ExpenseDao
import com.example.kanhaiya_kumar_assignment.data.local.entity.ExpenseEntity

@Database(
    entities = [ExpenseEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ExpenseDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao
    
    companion object {
        const val DATABASE_NAME = "expense_database"
    }
}
