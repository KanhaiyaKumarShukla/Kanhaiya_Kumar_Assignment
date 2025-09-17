package com.example.kanhaiya_kumar_assignment.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kanhaiya_kumar_assignment.domain.model.Expense
import com.example.kanhaiya_kumar_assignment.domain.model.ExpenseCategory
import java.time.format.DateTimeFormatter

@Composable
fun ExpenseCard(
    expense: Expense,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon
            Surface(
                color = getCategoryColor(expense.category).copy(alpha = 0.18f),
                shape = MaterialTheme.shapes.large,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp
            ) {
                Box(
                    modifier = Modifier.size(44.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getCategoryIcon(expense.category),
                        contentDescription = expense.category.displayName,
                        tint = getCategoryColor(expense.category)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Expense Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = expense.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                
                Text(
                    text = expense.category.displayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (expense.notes.isNotBlank()) {
                    Text(
                        text = expense.notes,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
                
                Text(
                    text = expense.dateTime.format(DateTimeFormatter.ofPattern("MMM dd, HH:mm")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Amount
            Text(
                text = "₹${String.format("%.2f", expense.amount)}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun getCategoryIcon(category: ExpenseCategory): ImageVector {
    return when (category) {
        ExpenseCategory.STAFF -> Icons.Default.Person
        ExpenseCategory.TRAVEL -> Icons.Default.DirectionsCar
        ExpenseCategory.FOOD -> Icons.Default.Restaurant
        ExpenseCategory.UTILITY -> Icons.Default.ElectricBolt
    }
}

@Composable
private fun getCategoryColor(category: ExpenseCategory): androidx.compose.ui.graphics.Color {
    return when (category) {
        ExpenseCategory.STAFF -> MaterialTheme.colorScheme.primary
        ExpenseCategory.TRAVEL -> MaterialTheme.colorScheme.secondary
        ExpenseCategory.FOOD -> MaterialTheme.colorScheme.tertiary
        ExpenseCategory.UTILITY -> MaterialTheme.colorScheme.error
    }
}
