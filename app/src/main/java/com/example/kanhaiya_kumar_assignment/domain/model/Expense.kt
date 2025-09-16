package com.example.kanhaiya_kumar_assignment.domain.model

import java.time.LocalDateTime

data class Expense(
    val id: Long = 0,
    val title: String,
    val amount: Double,
    val category: ExpenseCategory,
    val notes: String = "",
    val receiptImagePath: String? = null,
    val dateTime: LocalDateTime = LocalDateTime.now()
)

enum class ExpenseCategory(val displayName: String) {
    STAFF("Staff"),
    TRAVEL("Travel"),
    FOOD("Food"),
    UTILITY("Utility")
}

data class CategoryTotal(
    val category: ExpenseCategory,
    val total: Double,
    val count: Int
)

data class DailyTotal(
    val date: String,
    val total: Double,
    val count: Int
)
