package com.example.kanhaiya_kumar_assignment.data.local.dao

import androidx.room.*
import com.example.kanhaiya_kumar_assignment.data.local.entity.ExpenseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {
    
    @Query("SELECT * FROM expenses ORDER BY dateTime DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>
    
    @Query("SELECT * FROM expenses WHERE DATE(dateTime) = DATE(:date) ORDER BY dateTime DESC")
    fun getExpensesByDate(date: String): Flow<List<ExpenseEntity>>
    
    @Query("SELECT * FROM expenses WHERE DATE(dateTime) = DATE('now', 'localtime') ORDER BY dateTime DESC")
    fun getTodayExpenses(): Flow<List<ExpenseEntity>>
    
    @Query("SELECT SUM(amount) FROM expenses WHERE DATE(dateTime) = DATE('now', 'localtime')")
    fun getTodayTotal(): Flow<Double?>
    
    @Query("""
        SELECT category, SUM(amount) as total, COUNT(*) as count 
        FROM expenses 
        WHERE DATE(dateTime) >= DATE('now', '-7 days', 'localtime') 
        GROUP BY category
    """)
    fun getCategoryTotalsLast7Days(): Flow<List<CategoryTotalResult>>
    
    @Query("""
        SELECT DATE(dateTime) as date, SUM(amount) as total, COUNT(*) as count 
        FROM expenses 
        WHERE DATE(dateTime) >= DATE('now', '-7 days', 'localtime') 
        GROUP BY DATE(dateTime)
        ORDER BY DATE(dateTime) DESC
    """)
    fun getDailyTotalsLast7Days(): Flow<List<DailyTotalResult>>
    
    @Query("SELECT * FROM expenses WHERE id = :id LIMIT 1")
    fun getExpenseById(id: Long): Flow<ExpenseEntity?>
    
    @Insert
    suspend fun insertExpense(expense: ExpenseEntity): Long
    
    @Update
    suspend fun updateExpense(expense: ExpenseEntity)
    
    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)
    
    @Query("DELETE FROM expenses WHERE id = :id")
    suspend fun deleteExpenseById(id: Long)
}

data class CategoryTotalResult(
    val category: String,
    val total: Double,
    val count: Int
)

data class DailyTotalResult(
    val date: String,
    val total: Double,
    val count: Int
)
