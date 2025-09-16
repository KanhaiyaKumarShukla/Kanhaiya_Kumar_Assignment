package com.example.kanhaiya_kumar_assignment.presentation.screens.add_expense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kanhaiya_kumar_assignment.domain.model.Expense
import com.example.kanhaiya_kumar_assignment.domain.model.ExpenseCategory
import com.example.kanhaiya_kumar_assignment.domain.usecase.AddExpenseUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val addExpenseUseCase: AddExpenseUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState: StateFlow<AddExpenseUiState> = _uiState.asStateFlow()
    
    fun onTitleChanged(title: String) {
        _uiState.value = _uiState.value.copy(
            title = title,
            titleError = null
        )
    }
    
    fun onAmountChanged(amount: String) {
        _uiState.value = _uiState.value.copy(
            amount = amount,
            amountError = null
        )
    }
    
    fun onCategoryChanged(category: ExpenseCategory) {
        _uiState.value = _uiState.value.copy(category = category)
    }
    
    fun onNotesChanged(notes: String) {
        if (notes.length <= 100) {
            _uiState.value = _uiState.value.copy(notes = notes)
        }
    }
    
    fun onReceiptImagePathChanged(path: String?) {
        _uiState.value = _uiState.value.copy(receiptImagePath = path)
    }
    
    fun addExpense() {
        val currentState = _uiState.value
        
        // Validate inputs
        val titleError = if (currentState.title.isBlank()) "Title is required" else null
        val amountError = when {
            currentState.amount.isBlank() -> "Amount is required"
            currentState.amount.toDoubleOrNull() == null -> "Invalid amount"
            currentState.amount.toDoubleOrNull()!! <= 0 -> "Amount must be greater than 0"
            else -> null
        }
        
        if (titleError != null || amountError != null) {
            _uiState.value = currentState.copy(
                titleError = titleError,
                amountError = amountError
            )
            return
        }
        
        _uiState.value = currentState.copy(isLoading = true)
        
        val expense = Expense(
            title = currentState.title.trim(),
            amount = currentState.amount.toDouble(),
            category = currentState.category,
            notes = currentState.notes.trim(),
            receiptImagePath = currentState.receiptImagePath,
            dateTime = LocalDateTime.now()
        )
        
        viewModelScope.launch {
            addExpenseUseCase(expense)
                .onSuccess {
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
                .onFailure { error ->
                    _uiState.value = currentState.copy(
                        isLoading = false,
                        errorMessage = error.message ?: "Failed to add expense"
                    )
                }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun resetForm() {
        _uiState.value = AddExpenseUiState()
    }
}

data class AddExpenseUiState(
    val title: String = "",
    val amount: String = "",
    val category: ExpenseCategory = ExpenseCategory.FOOD,
    val notes: String = "",
    val receiptImagePath: String? = null,
    val titleError: String? = null,
    val amountError: String? = null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)
