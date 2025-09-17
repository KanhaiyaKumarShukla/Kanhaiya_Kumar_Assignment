package com.example.kanhaiya_kumar_assignment.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kanhaiya_kumar_assignment.domain.model.Expense
import com.example.kanhaiya_kumar_assignment.domain.model.ExpenseCategory
import java.time.format.DateTimeFormatter

@Composable
fun AnimatedExpenseCard(
    expense: Expense,
    modifier: Modifier = Modifier,
    isVisible: Boolean = true,
    onClick: () -> Unit = {}
) {
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.8f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "card_scale"
    )
    
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it },
            animationSpec = tween(300)
        ) + fadeIn(animationSpec = tween(300)),
        exit = slideOutVertically(
            targetOffsetY = { -it },
            animationSpec = tween(300)
        ) + fadeOut(animationSpec = tween(300))
    ) {
        Card(
            modifier = modifier.scale(scale),
            onClick = onClick,
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Animated Category Icon
                val iconScale by animateFloatAsState(
                    targetValue = 1f,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessMedium
                    ),
                    label = "icon_scale"
                )
                
                Icon(
                    imageVector = getCategoryIcon(expense.category),
                    contentDescription = expense.category.displayName,
                    modifier = Modifier
                        .size(40.dp)
                        .scale(iconScale),
                    tint = getCategoryColor(expense.category)
                )
                
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
                
                // Animated Amount
                AnimatedContent(
                    targetState = expense.amount,
                    transitionSpec = {
                        slideInVertically { it } + fadeIn() togetherWith 
                        slideOutVertically { -it } + fadeOut()
                    },
                    label = "amount_animation"
                ) { amount ->
                    Text(
                        text = "₹${String.format("%.2f", amount)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
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
