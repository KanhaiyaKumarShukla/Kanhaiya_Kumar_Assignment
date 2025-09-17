package com.example.kanhaiya_kumar_assignment.presentation.screens.reports

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kanhaiya_kumar_assignment.presentation.components.CategoryChart
import com.example.kanhaiya_kumar_assignment.presentation.components.DailyChart
import com.example.kanhaiya_kumar_assignment.presentation.components.ReportSummaryCard
import androidx.compose.ui.tooling.preview.Preview
import com.example.kanhaiya_kumar_assignment.domain.model.CategoryTotal
import com.example.kanhaiya_kumar_assignment.domain.model.DailyTotal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    onNavigateBack: () -> Unit,
    viewModel: ReportsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val categoryTotals by viewModel.categoryTotals.collectAsState()
    val dailyTotals by viewModel.dailyTotals.collectAsState()
    
    var showExportDialog by remember { mutableStateOf(false) }

    
    LaunchedEffect(uiState.exportSuccess) {
        if (uiState.exportSuccess) {
            // Could show a snackbar here
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Reports") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showExportDialog = true }) {
                        Icon(Icons.Default.FileDownload, contentDescription = "Export")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Report Summary
            ReportSummaryCard(
                categoryTotals = categoryTotals,
                dailyTotals = dailyTotals
            )
            
            // Category-wise Totals Chart
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Category-wise Spending (Last 7 Days)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (categoryTotals.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No data available",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        CategoryChart(
                            categoryTotals = categoryTotals,
                            modifier = Modifier.height(200.dp)
                        )
                    }
                }
            }
            
            // Daily Totals Chart
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Daily Spending Trend (Last 7 Days)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    if (dailyTotals.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No data available",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        DailyChart(
                            dailyTotals = dailyTotals,
                            modifier = Modifier.height(200.dp)
                        )
                    }
                }
            }
            
            // Export Status
            if (uiState.isExporting) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Exporting ${uiState.exportFormat?.displayName}...",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
            
            if (uiState.exportSuccess) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Share,
                            contentDescription = "Success",
                            tint = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Export completed! Ready to share.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
    
    // Export Dialog
    if (showExportDialog) {
        AlertDialog(
            onDismissRequest = { showExportDialog = false },
            title = { Text("Export Report") },
            text = {
                Column {
                    Text("Choose export format:")
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    ExportFormat.values().forEach { format ->
                        TextButton(
                            onClick = {
                                viewModel.simulateExport(format)
                                showExportDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(format.displayName)
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { showExportDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// Preview with simulated data
@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun ReportsScreenPreview() {
    // Simulated data
    val sampleCategoryTotals = listOf(
        CategoryTotal(category = com.example.kanhaiya_kumar_assignment.domain.model.ExpenseCategory.FOOD, total = 1240.0, count = 8),
        CategoryTotal(category = com.example.kanhaiya_kumar_assignment.domain.model.ExpenseCategory.TRAVEL, total = 3420.0, count = 3),
        CategoryTotal(category = com.example.kanhaiya_kumar_assignment.domain.model.ExpenseCategory.UTILITY, total = 890.0, count = 4),
        CategoryTotal(category = com.example.kanhaiya_kumar_assignment.domain.model.ExpenseCategory.STAFF, total = 2100.0, count = 2)
    )
    val sampleDailyTotals = listOf(
        DailyTotal(date = "2025-09-11", total = 550.0, count = 2),
        DailyTotal(date = "2025-09-12", total = 1200.0, count = 3),
        DailyTotal(date = "2025-09-13", total = 800.0, count = 2),
        DailyTotal(date = "2025-09-14", total = 1850.0, count = 4),
        DailyTotal(date = "2025-09-15", total = 600.0, count = 1),
        DailyTotal(date = "2025-09-16", total = 1420.0, count = 3),
        DailyTotal(date = "2025-09-17", total = 980.0, count = 2)
    )

    MaterialTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Reports (Preview)") }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Summary with sample data
                ReportSummaryCard(
                    categoryTotals = sampleCategoryTotals,
                    dailyTotals = sampleDailyTotals
                )

                // Category-wise Totals Chart
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Category-wise Spending (Last 7 Days)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CategoryChart(
                            categoryTotals = sampleCategoryTotals,
                            modifier = Modifier.height(200.dp)
                        )
                    }
                }

                // Daily Totals Chart
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Daily Spending Trend (Last 7 Days)",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        DailyChart(
                            dailyTotals = sampleDailyTotals,
                            modifier = Modifier.height(200.dp)
                        )
                    }
                }

                // Export Status (Preview only)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Exporting CSV... (preview)",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

