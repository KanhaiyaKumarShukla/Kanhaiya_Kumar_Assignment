package com.example.kanhaiya_kumar_assignment.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kanhaiya_kumar_assignment.domain.model.CategoryTotal
import com.example.kanhaiya_kumar_assignment.domain.model.ExpenseCategory
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CategoryChart(
    categoryTotals: List<CategoryTotal>,
    modifier: Modifier = Modifier
) {
    if (categoryTotals.isEmpty()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No data available",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }
    
    val totalAmount = categoryTotals.sumOf { it.total }
    val colors = listOf(
        Color(0xFF6366F1), // Indigo
        Color(0xFF8B5CF6), // Violet
        Color(0xFFEC4899), // Pink
        Color(0xFFF59E0B)  // Amber
    )
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Pie Chart
        Box(
            modifier = Modifier
                .size(150.dp)
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                drawPieChart(
                    categoryTotals = categoryTotals,
                    totalAmount = totalAmount,
                    colors = colors,
                    size = size
                )
            }
        }
        
        // Legend
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categoryTotals.forEachIndexed { index, categoryTotal ->
                val percentage = (categoryTotal.total / totalAmount * 100).toInt()
                LegendItem(
                    color = colors[index % colors.size],
                    category = categoryTotal.category,
                    amount = categoryTotal.total,
                    percentage = percentage
                )
            }
        }
    }
}

@Composable
private fun LegendItem(
    color: Color,
    category: ExpenseCategory,
    amount: Double,
    percentage: Int
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .drawWithContent {
                    drawRect(color = color)
                }
        )
        
        Column {
            Text(
                text = category.displayName,
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "₹${String.format("%.0f", amount)} ($percentage%)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun DrawScope.drawPieChart(
    categoryTotals: List<CategoryTotal>,
    totalAmount: Double,
    colors: List<Color>,
    size: Size
) {
    val center = Offset(size.width / 2, size.height / 2)
    val radius = minOf(size.width, size.height) / 2 * 0.8f
    
    var startAngle = -90f
    
    categoryTotals.forEachIndexed { index, categoryTotal ->
        val sweepAngle = (categoryTotal.total / totalAmount * 360).toFloat()
        
        drawArc(
            color = colors[index % colors.size],
            startAngle = startAngle,
            sweepAngle = sweepAngle,
            useCenter = true,
            topLeft = Offset(center.x - radius, center.y - radius),
            size = Size(radius * 2, radius * 2)
        )
        
        startAngle += sweepAngle
    }
}
