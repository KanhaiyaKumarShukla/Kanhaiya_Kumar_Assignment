package com.example.kanhaiya_kumar_assignment.domain.repository

import com.example.kanhaiya_kumar_assignment.domain.model.CategoryTotal
import com.example.kanhaiya_kumar_assignment.domain.model.DailyTotal
import com.example.kanhaiya_kumar_assignment.domain.model.Expense
import kotlinx.coroutines.flow.Flow

interface ExpenseRepository {
    fun getAllExpenses(): Flow<List<Expense>>
    fun getExpensesByDate(date: String): Flow<List<Expense>>
    fun getTodayExpenses(): Flow<List<Expense>>
    fun getTodayTotal(): Flow<Double>
    fun getCategoryTotalsLast7Days(): Flow<List<CategoryTotal>>
    fun getDailyTotalsLast7Days(): Flow<List<DailyTotal>>
    fun getExpenseById(id: Long): Flow<Expense?>
    suspend fun insertExpense(expense: Expense): Long
    suspend fun updateExpense(expense: Expense)
    suspend fun deleteExpense(expense: Expense)
    suspend fun deleteExpenseById(id: Long)
}
