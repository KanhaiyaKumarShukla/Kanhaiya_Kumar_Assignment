package com.example.kanhaiya_kumar_assignment.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.kanhaiya_kumar_assignment.presentation.screens.add_expense.AddExpenseScreen
import com.example.kanhaiya_kumar_assignment.presentation.screens.expense_list.ExpenseListScreen
import com.example.kanhaiya_kumar_assignment.presentation.screens.reports.ReportsScreen
import com.example.kanhaiya_kumar_assignment.presentation.screens.expense_detail.ExpenseDetailScreen

@Composable
fun ExpenseNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.ExpenseList.route
    ) {
        composable(Screen.ExpenseList.route) {
            ExpenseListScreen(
                onNavigateToAddExpense = {
                    navController.navigate(Screen.AddExpense.route)
                },
                onNavigateToReports = {
                    navController.navigate(Screen.Reports.route)
                },
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.ExpenseDetail.createRoute(id))
                }
            )
        }
        
        composable(Screen.AddExpense.route) {
            AddExpenseScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Reports.route) {
            ReportsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Screen.ExpenseDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            ExpenseDetailScreen(
                expenseId = id,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

sealed class Screen(val route: String) {
    object ExpenseList : Screen("expense_list")
    object AddExpense : Screen("add_expense")
    object Reports : Screen("reports")
    object ExpenseDetail : Screen("expense_detail/{id}") {
        fun createRoute(id: Long) = "expense_detail/$id"
    }
}
