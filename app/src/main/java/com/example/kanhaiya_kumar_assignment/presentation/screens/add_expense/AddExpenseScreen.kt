package com.example.kanhaiya_kumar_assignment.presentation.screens.add_expense

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import coil.compose.AsyncImage
import com.example.kanhaiya_kumar_assignment.domain.model.ExpenseCategory
import com.example.kanhaiya_kumar_assignment.presentation.components.CategoryDropdown
import com.example.kanhaiya_kumar_assignment.presentation.components.LoadingButton
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.time.Instant
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AddExpenseScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddExpenseViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val savedPath = saveReceiptToInternalStorage(context, it)
            viewModel.onReceiptImagePathChanged(savedPath)
        }
    }
    
    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onNavigateBack()
        }
    }
    
    uiState.errorMessage?.let { error ->
        LaunchedEffect(error) {
            // Show snackbar or handle error
            viewModel.clearError()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Expense", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Expense Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Title Input
                    OutlinedTextField(
                        value = uiState.title,
                        onValueChange = viewModel::onTitleChanged,
                        label = { Text("Title") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = uiState.titleError != null,
                        supportingText = uiState.titleError?.let { { Text(it) } },
                        leadingIcon = { Icon(Icons.Default.Edit, contentDescription = null) }
                    )

                    // Amount Input
                    OutlinedTextField(
                        value = uiState.amount,
                        onValueChange = viewModel::onAmountChanged,
                        label = { Text("Amount") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        isError = uiState.amountError != null,
                        supportingText = uiState.amountError?.let { { Text(it) } },
                        leadingIcon = { Icon(Icons.Default.CurrencyRupee, contentDescription = null) }
                    )

                    // Category Dropdown
                    CategoryDropdown(
                        selectedCategory = uiState.category,
                        onCategorySelected = viewModel::onCategoryChanged,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Notes Input
                    OutlinedTextField(
                        value = uiState.notes,
                        onValueChange = viewModel::onNotesChanged,
                        label = { Text("Notes (Optional)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3,
                        supportingText = { Text("${uiState.notes.length}/100") },
                        leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) }
                    )
                }
            }
            
            // Receipt Image Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Receipt",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = "Receipt",
                        modifier = Modifier.size(40.dp),
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        "Receipt Image (Optional)",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        textAlign = TextAlign.Center
                    )

                    // Preview if available
                    AnimatedContent(
                        targetState = uiState.receiptImagePath,
                        label = "receipt-preview"
                    ) { path ->
                        if (path.isNullOrBlank()) {
                            Text(
                                "No receipt added",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        } else {
                            AsyncImage(
                                model = File(path),
                                contentDescription = "Receipt Preview",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 160.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(MaterialTheme.colorScheme.surface),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = { pickImageLauncher.launch("image/*") }
                        ) {
                            Text("Add Receipt")
                        }
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            enabled = uiState.receiptImagePath != null,
                            onClick = { viewModel.onReceiptImagePathChanged(null) }
                        ) {
                            Text("Remove")
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Submit Button
            LoadingButton(
                onClick = viewModel::addExpense,
                modifier = Modifier.fillMaxWidth(),
                isLoading = uiState.isLoading,
                enabled = !uiState.isLoading
            ) {
                Text("Add Expense", fontWeight = FontWeight.Medium)
            }
        }
    }
}

private fun saveReceiptToInternalStorage(context: Context, uri: Uri): String? {
    return try {
        val receiptsDir = File(context.filesDir, "receipts").apply { if (!exists()) mkdirs() }
        val file = File(receiptsDir, "receipt_${Instant.now().toEpochMilli()}.jpg")
        context.contentResolver.openInputStream(uri)?.use { input: InputStream ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        file.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
