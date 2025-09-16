package com.example.kanhaiya_kumar_assignment.domain.usecase

import com.example.kanhaiya_kumar_assignment.domain.model.Expense
import com.example.kanhaiya_kumar_assignment.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExpenseByIdUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    operator fun invoke(id: Long): Flow<Expense?> = repository.getExpenseById(id)
}
