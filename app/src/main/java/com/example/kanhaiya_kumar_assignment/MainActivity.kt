package com.example.kanhaiya_kumar_assignment

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.kanhaiya_kumar_assignment.presentation.navigation.ExpenseNavigation
import com.example.kanhaiya_kumar_assignment.presentation.theme.ThemeViewModel
import com.example.kanhaiya_kumar_assignment.ui.theme.Kanhaiya_Kumar_AssignmentTheme
import dagger.hilt.android.AndroidEntryPoint
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.runtime.SideEffect

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val themeViewModel: ThemeViewModel = hiltViewModel()
            val isDarkTheme by themeViewModel.isDarkTheme.collectAsState()
            
            Kanhaiya_Kumar_AssignmentTheme(darkTheme = isDarkTheme) {
                val systemUiController = rememberSystemUiController()
                val colorScheme = MaterialTheme.colorScheme
                SideEffect {
                    // Status bar should follow the app surface color
                    systemUiController.setStatusBarColor(
                        color = colorScheme.surface,
                        darkIcons = !isDarkTheme
                    )
                    // Navigation bar should be dark in dark theme and match surface in light theme
                    systemUiController.setNavigationBarColor(
                        color = colorScheme.surface,
                        darkIcons = !isDarkTheme,
                        navigationBarContrastEnforced = false
                    )
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    ExpenseNavigation(navController = navController)
                }
            }
        }
    }
}