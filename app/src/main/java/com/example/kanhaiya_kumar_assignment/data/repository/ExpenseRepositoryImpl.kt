package com.example.kanhaiya_kumar_assignment.data.repository

import com.example.kanhaiya_kumar_assignment.data.local.dao.ExpenseDao
import com.example.kanhaiya_kumar_assignment.data.local.entity.toEntity
import com.example.kanhaiya_kumar_assignment.data.local.entity.toDomainModel
import com.example.kanhaiya_kumar_assignment.domain.model.CategoryTotal
import com.example.kanhaiya_kumar_assignment.domain.model.DailyTotal
import com.example.kanhaiya_kumar_assignment.domain.model.Expense
import com.example.kanhaiya_kumar_assignment.domain.model.ExpenseCategory
import com.example.kanhaiya_kumar_assignment.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpenseRepositoryImpl @Inject constructor(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {
    
    override fun getAllExpenses(): Flow<List<Expense>> {
        return expenseDao.getAllExpenses().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getExpensesByDate(date: String): Flow<List<Expense>> {
        return expenseDao.getExpensesByDate(date).map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getTodayExpenses(): Flow<List<Expense>> {
        return expenseDao.getTodayExpenses().map { entities ->
            entities.map { it.toDomainModel() }
        }
    }
    
    override fun getTodayTotal(): Flow<Double> {
        return expenseDao.getTodayTotal().map { it ?: 0.0 }
    }
    
    override fun getCategoryTotalsLast7Days(): Flow<List<CategoryTotal>> {
        return expenseDao.getCategoryTotalsLast7Days().map { results ->
            results.map { result ->
                CategoryTotal(
                    category = ExpenseCategory.valueOf(result.category),
                    total = result.total,
                    count = result.count
                )
            }
        }
    }
    
    override fun getDailyTotalsLast7Days(): Flow<List<DailyTotal>> {
        return expenseDao.getDailyTotalsLast7Days().map { results ->
            results.map { result ->
                DailyTotal(
                    date = result.date,
                    total = result.total,
                    count = result.count
                )
            }
        }
    }
    
    override fun getExpenseById(id: Long): Flow<Expense?> {
        return expenseDao.getExpenseById(id).map { entity -> entity?.toDomainModel() }
    }
    
    override suspend fun insertExpense(expense: Expense): Long {
        return expenseDao.insertExpense(expense.toEntity())
    }
    
    override suspend fun updateExpense(expense: Expense) {
        expenseDao.updateExpense(expense.toEntity())
    }
    
    override suspend fun deleteExpense(expense: Expense) {
        expenseDao.deleteExpense(expense.toEntity())
    }
    
    override suspend fun deleteExpenseById(id: Long) {
        expenseDao.deleteExpenseById(id)
    }
}
