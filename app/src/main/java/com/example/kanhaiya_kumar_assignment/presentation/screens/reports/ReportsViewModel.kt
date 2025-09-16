package com.example.kanhaiya_kumar_assignment.presentation.screens.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kanhaiya_kumar_assignment.domain.model.CategoryTotal
import com.example.kanhaiya_kumar_assignment.domain.model.DailyTotal
import com.example.kanhaiya_kumar_assignment.domain.usecase.GetReportsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val getReportsUseCase: GetReportsUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ReportsUiState())
    val uiState: StateFlow<ReportsUiState> = _uiState.asStateFlow()
    
    val categoryTotals = getReportsUseCase.getCategoryTotalsLast7Days()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    val dailyTotals = getReportsUseCase.getDailyTotalsLast7Days()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    
    fun simulateExport(format: ExportFormat) {
        _uiState.value = _uiState.value.copy(
            isExporting = true,
            exportFormat = format
        )
        
        // Simulate export delay
        viewModelScope.launch {
            kotlinx.coroutines.delay(2000)
            _uiState.value = _uiState.value.copy(
                isExporting = false,
                exportSuccess = true,
                exportFormat = null
            )
            
            // Reset success state after showing message
            kotlinx.coroutines.delay(3000)
            _uiState.value = _uiState.value.copy(exportSuccess = false)
        }
    }
}

data class ReportsUiState(
    val isExporting: Boolean = false,
    val exportSuccess: Boolean = false,
    val exportFormat: ExportFormat? = null
)

enum class ExportFormat(val displayName: String) {
    PDF("PDF"),
    CSV("CSV")
}
