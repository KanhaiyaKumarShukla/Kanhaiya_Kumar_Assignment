package com.example.kanhaiya_kumar_assignment.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kanhaiya_kumar_assignment.domain.model.CategoryTotal
import com.example.kanhaiya_kumar_assignment.domain.model.DailyTotal

@Composable
fun ReportSummaryCard(
    categoryTotals: List<CategoryTotal>,
    dailyTotals: List<DailyTotal>,
    modifier: Modifier = Modifier
) {
    val totalAmount = categoryTotals.sumOf { it.total }
    val totalExpenses = categoryTotals.sumOf { it.count }
    val averageDaily = if (dailyTotals.isNotEmpty()) totalAmount / dailyTotals.size else 0.0
    
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Assessment,
                    contentDescription = "Report Summary",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Last 7 Days Summary",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SummaryItem(
                    title = "Total Spent",
                    value = "₹${String.format("%.2f", totalAmount)}",
                    modifier = Modifier.weight(1f)
                )
                
                SummaryItem(
                    title = "Total Expenses",
                    value = totalExpenses.toString(),
                    modifier = Modifier.weight(1f)
                )
                
                SummaryItem(
                    title = "Daily Average",
                    value = "₹${String.format("%.2f", averageDaily)}",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SummaryItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}
