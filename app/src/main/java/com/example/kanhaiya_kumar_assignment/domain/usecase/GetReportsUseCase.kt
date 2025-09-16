package com.example.kanhaiya_kumar_assignment.domain.usecase

import com.example.kanhaiya_kumar_assignment.domain.model.CategoryTotal
import com.example.kanhaiya_kumar_assignment.domain.model.DailyTotal
import com.example.kanhaiya_kumar_assignment.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetReportsUseCase @Inject constructor(
    private val repository: ExpenseRepository
) {
    fun getCategoryTotalsLast7Days(): Flow<List<CategoryTotal>> = 
        repository.getCategoryTotalsLast7Days()
    
    fun getDailyTotalsLast7Days(): Flow<List<DailyTotal>> = 
        repository.getDailyTotalsLast7Days()
}
