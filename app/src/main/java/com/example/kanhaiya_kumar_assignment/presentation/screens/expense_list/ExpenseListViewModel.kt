package com.example.kanhaiya_kumar_assignment.presentation.screens.expense_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kanhaiya_kumar_assignment.domain.model.Expense
import com.example.kanhaiya_kumar_assignment.domain.model.ExpenseCategory
import com.example.kanhaiya_kumar_assignment.domain.usecase.GetExpensesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val getExpensesUseCase: GetExpensesUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ExpenseListUiState())
    val uiState: StateFlow<ExpenseListUiState> = _uiState.asStateFlow()
    
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    private val _groupBy = MutableStateFlow(GroupBy.TIME)
    private val _filterCategory = MutableStateFlow<ExpenseCategory?>(null)
    
    val todayTotal = getExpensesUseCase.getTodayTotal()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )
    
    init {
        observeExpenses()
    }
    
    private fun observeExpenses() {
        viewModelScope.launch {
            combine(
                _selectedDate,
                _groupBy,
                _filterCategory
            ) { date, groupBy, filterCategory ->
                Triple(date, groupBy, filterCategory)
            }.collectLatest { (date, groupBy, filterCategory) ->
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                val expensesFlow = if (date == LocalDate.now()) {
                    getExpensesUseCase.getTodayExpenses()
                } else {
                    getExpensesUseCase.getExpensesByDate(date.toString())
                }
                
                expensesFlow.collect { expenses ->
                    val filteredExpenses = if (filterCategory != null) {
                        expenses.filter { it.category == filterCategory }
                    } else {
                        expenses
                    }
                    
                    val groupedExpenses = when (groupBy) {
                        GroupBy.CATEGORY -> groupByCategory(filteredExpenses)
                        GroupBy.TIME -> groupByTime(filteredExpenses)
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        expenses = filteredExpenses,
                        groupedExpenses = groupedExpenses,
                        selectedDate = date,
                        groupBy = groupBy,
                        filterCategory = filterCategory,
                        isLoading = false,
                        totalAmount = filteredExpenses.sumOf { it.amount },
                        totalCount = filteredExpenses.size
                    )
                }
            }
        }
    }
    
    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }
    
    fun onGroupByChanged(groupBy: GroupBy) {
        _groupBy.value = groupBy
    }
    
    fun onFilterCategoryChanged(category: ExpenseCategory?) {
        _filterCategory.value = category
    }
    
    private fun groupByCategory(expenses: List<Expense>): Map<String, List<Expense>> {
        return expenses.groupBy { it.category.displayName }
    }
    
    private fun groupByTime(expenses: List<Expense>): Map<String, List<Expense>> {
        return expenses.groupBy { 
            it.dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
        }
    }
}

data class ExpenseListUiState(
    val expenses: List<Expense> = emptyList(),
    val groupedExpenses: Map<String, List<Expense>> = emptyMap(),
    val selectedDate: LocalDate = LocalDate.now(),
    val groupBy: GroupBy = GroupBy.TIME,
    val filterCategory: ExpenseCategory? = null,
    val isLoading: Boolean = false,
    val totalAmount: Double = 0.0,
    val totalCount: Int = 0
)

enum class GroupBy(val displayName: String) {
    CATEGORY("Category"),
    TIME("Time")
}
