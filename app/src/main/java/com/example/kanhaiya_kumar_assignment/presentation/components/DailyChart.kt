package com.example.kanhaiya_kumar_assignment.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kanhaiya_kumar_assignment.domain.model.DailyTotal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun DailyChart(
    dailyTotals: List<DailyTotal>,
    modifier: Modifier = Modifier
) {
    if (dailyTotals.isEmpty()) {
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
    
    val maxAmount = dailyTotals.maxOfOrNull { it.total } ?: 0.0
    val primaryColor = MaterialTheme.colorScheme.primary
    
    Column(
        modifier = modifier
    ) {
        // Chart
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
        ) {
            drawLineChart(
                dailyTotals = dailyTotals,
                maxAmount = maxAmount,
                color = primaryColor,
                size = size
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // X-axis labels
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            dailyTotals.takeLast(7).forEach { dailyTotal ->
                val date = try {
                    LocalDate.parse(dailyTotal.date)
                } catch (e: Exception) {
                    LocalDate.now()
                }
                
                Text(
                    text = date.format(DateTimeFormatter.ofPattern("MM/dd")),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun DrawScope.drawLineChart(
    dailyTotals: List<DailyTotal>,
    maxAmount: Double,
    color: Color,
    size: androidx.compose.ui.geometry.Size
) {
    if (dailyTotals.isEmpty()) return
    
    val padding = 20f
    val chartWidth = size.width - (padding * 2)
    val chartHeight = size.height - (padding * 2)
    
    val points = dailyTotals.mapIndexed { index, dailyTotal ->
        val x = padding + (index * chartWidth / (dailyTotals.size - 1).coerceAtLeast(1))
        val y = padding + chartHeight - (dailyTotal.total / maxAmount * chartHeight).toFloat()
        Offset(x, y)
    }
    
    // Draw line
    if (points.size > 1) {
        val path = Path()
        path.moveTo(points.first().x, points.first().y)
        
        for (i in 1 until points.size) {
            path.lineTo(points[i].x, points[i].y)
        }
        
        drawPath(
            path = path,
            color = color,
            style = Stroke(width = 3.dp.toPx())
        )
    }
    
    // Draw points
    points.forEach { point ->
        drawCircle(
            color = color,
            radius = 4.dp.toPx(),
            center = point
        )
    }
}
