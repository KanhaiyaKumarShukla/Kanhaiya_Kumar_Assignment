package com.example.kanhaiya_kumar_assignment.domain.usecase

import com.example.kanhaiya_kumar_assignment.domain.model.Expense
import com.example.kanhaiya_kumar_assignment.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpensesUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    fun getAllExpenses(): Flow<List<Expense>> = repository.getAllExpenses()
    
    fun getTodayExpenses(): Flow<List<Expense>> = repository.getTodayExpenses()
    
    fun getExpensesByDate(date: String): Flow<List<Expense>> = repository.getExpensesByDate(date)
    
    fun getTodayTotal(): Flow<Double> = repository.getTodayTotal()
}
