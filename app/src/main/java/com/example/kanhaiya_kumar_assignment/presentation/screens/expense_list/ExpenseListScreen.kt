package com.example.kanhaiya_kumar_assignment.presentation.screens.expense_list

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Inbox
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.kanhaiya_kumar_assignment.domain.model.ExpenseCategory
import com.example.kanhaiya_kumar_assignment.presentation.components.AnimatedFAB
import com.example.kanhaiya_kumar_assignment.presentation.components.AnimatedExpenseCard
import com.example.kanhaiya_kumar_assignment.presentation.components.ExpenseCard
import com.example.kanhaiya_kumar_assignment.presentation.components.ThemeToggleButton
import com.example.kanhaiya_kumar_assignment.presentation.components.TotalSpentCard
import com.example.kanhaiya_kumar_assignment.presentation.theme.ThemeViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpenseListScreen(
    onNavigateToAddExpense: () -> Unit,
    onNavigateToReports: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    viewModel: ExpenseListViewModel = hiltViewModel(),
    themeViewModel: ThemeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val todayTotal by viewModel.todayTotal.collectAsState()
    val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
    var showFilterDialog by remember { mutableStateOf(false) }
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = { Text("Expense Tracker") },
                scrollBehavior = scrollBehavior,
                actions = {
                    ThemeToggleButton(
                        isDarkTheme = isDarkTheme,
                        onToggle = themeViewModel::toggleTheme
                    )
                    IconButton(onClick = { showFilterDialog = true }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filter")
                    }
                    IconButton(onClick = onNavigateToReports) {
                        Icon(Icons.Default.Assessment, contentDescription = "Reports")
                    }
                }
            )
        },
        floatingActionButton = {
            AnimatedFAB(
                onClick = onNavigateToAddExpense,
                isVisible = !uiState.isLoading
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Today's Total Card
            TotalSpentCard(
                amount = todayTotal,
                modifier = Modifier.padding(16.dp)
            )
            
            // Date and Summary Info
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (uiState.selectedDate == LocalDate.now()) "Today" 
                                  else uiState.selectedDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Row {
                            FilterChip(
                                onClick = { viewModel.onGroupByChanged(GroupBy.TIME) },
                                label = { Text("Time") },
                                selected = uiState.groupBy == GroupBy.TIME
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            FilterChip(
                                onClick = { viewModel.onGroupByChanged(GroupBy.CATEGORY) },
                                label = { Text("Category") },
                                selected = uiState.groupBy == GroupBy.CATEGORY
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "${uiState.totalCount} expenses",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "₹${String.format("%.2f", uiState.totalAmount)}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Expenses List
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.expenses.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Inbox,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No expenses found",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Tap + to add your first expense",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    uiState.groupedExpenses.forEach { (group, expenses) ->
                        item {
                            Text(
                                text = group,
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }
                        
                        items(expenses) { expense ->
                            AnimatedExpenseCard(
                                expense = expense,
                                modifier = Modifier.fillMaxWidth(),
                                isVisible = true,
                                onClick = { onNavigateToDetail(expense.id) }
                            )
                        }
                    }
                    
                    item {
                        Spacer(modifier = Modifier.height(80.dp)) // FAB padding
                    }
                }
            }
        }
    }
    
    // Filter Dialog
    if (showFilterDialog) {
        FilterDialog(
            selectedCategory = uiState.filterCategory,
            onCategorySelected = { category ->
                viewModel.onFilterCategoryChanged(category)
                showFilterDialog = false
            },
            onDismiss = { showFilterDialog = false }
        )
    }
}

@Composable
private fun FilterDialog(
    selectedCategory: ExpenseCategory?,
    onCategorySelected: (ExpenseCategory?) -> Unit,
    onDismiss: () -> Unit
) {
    var tempSelected by remember { mutableStateOf(selectedCategory) }

    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Filled.FilterList, contentDescription = null) },
        title = { Text("Filter by Category") },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 320.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // All Categories option
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = tempSelected == null,
                            onClick = { tempSelected = null }
                        )
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = tempSelected == null,
                        onClick = { tempSelected = null }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("All Categories")
                }

                ExpenseCategory.values().forEach { category ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = tempSelected == category,
                                onClick = { tempSelected = category }
                            )
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = tempSelected == category,
                            onClick = { tempSelected = category }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(category.displayName)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onCategorySelected(tempSelected)
                onDismiss()
            }) {
                Text("Apply")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                tempSelected = null
                onCategorySelected(null)
                onDismiss()
            }) {
                Text("Clear")
            }
        }
    )
}
