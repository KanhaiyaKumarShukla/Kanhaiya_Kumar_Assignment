package com.example.kanhaiya_kumar_assignment.presentation.screens.expense_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kanhaiya_kumar_assignment.domain.model.Expense
import com.example.kanhaiya_kumar_assignment.domain.usecase.GetExpenseByIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseDetailViewModel @Inject constructor(
    private val getExpenseById: GetExpenseByIdUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpenseDetailUiState())
    val uiState: StateFlow<ExpenseDetailUiState> = _uiState.asStateFlow()

    fun load(expenseId: Long) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            getExpenseById(expenseId)
                .onEach { expense ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        expense = expense,
                        errorMessage = if (expense == null) "Expense not found" else null
                    )
                }
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Failed to load expense"
                    )
                }
                .collect {}
        }
    }
}

data class ExpenseDetailUiState(
    val isLoading: Boolean = false,
    val expense: Expense? = null,
    val errorMessage: String? = null
)
