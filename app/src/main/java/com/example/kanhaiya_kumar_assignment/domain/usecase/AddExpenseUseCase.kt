package com.example.kanhaiya_kumar_assignment.domain.usecase

import com.example.kanhaiya_kumar_assignment.domain.model.Expense
import com.example.kanhaiya_kumar_assignment.domain.repository.ExpenseRepository
import javax.inject.Inject

class AddExpenseUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    suspend operator fun invoke(expense: Expense): Result<Long> {
        return try {
            if (expense.title.isBlank()) {
                Result.failure(Exception("Title cannot be empty"))
            } else if (expense.amount <= 0) {
                Result.failure(Exception("Amount must be greater than 0"))
            } else {
                val id = repository.insertExpense(expense)
                Result.success(id)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
