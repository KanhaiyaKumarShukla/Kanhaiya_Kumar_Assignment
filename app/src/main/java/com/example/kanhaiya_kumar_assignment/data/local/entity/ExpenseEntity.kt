package com.example.kanhaiya_kumar_assignment.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kanhaiya_kumar_assignment.domain.model.ExpenseCategory
import java.time.LocalDateTime

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: String, // Store as string for Room compatibility
    val notes: String = "",
    val receiptImagePath: String? = null,
    val dateTime: String // Store as ISO string for Room compatibility
)

// Extension functions for mapping between domain and data models
fun ExpenseEntity.toDomainModel(): com.example.kanhaiya_kumar_assignment.domain.model.Expense {
    return com.example.kanhaiya_kumar_assignment.domain.model.Expense(
        id = id,
        title = title,
        amount = amount,
        category = ExpenseCategory.valueOf(category),
        notes = notes,
        receiptImagePath = receiptImagePath,
        dateTime = LocalDateTime.parse(dateTime)
    )
}

fun com.example.kanhaiya_kumar_assignment.domain.model.Expense.toEntity(): ExpenseEntity {
    return ExpenseEntity(
        id = id,
        title = title,
        amount = amount,
        category = category.name,
        notes = notes,
        receiptImagePath = receiptImagePath,
        dateTime = dateTime.toString()
    )
}
