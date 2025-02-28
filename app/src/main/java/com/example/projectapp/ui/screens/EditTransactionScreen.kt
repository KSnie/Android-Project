package com.example.projectapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projectapp.ProjectApplication
import com.example.projectapp.model.Transaction
import com.example.projectapp.ui.viewmodel.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTransactionScreen(
    transaction: Transaction,
    isEnglish: Boolean,
    onBack: () -> Unit,
    onSave: (Transaction) -> Unit = {}
) {
    // Get the repository from the Application class
    val context = LocalContext.current
    val application = context.applicationContext as ProjectApplication

    // Initialize ViewModel with factory
    val viewModel: TransactionViewModel = viewModel(
        factory = TransactionViewModel.TransactionViewModelFactory(
            application.repository
        )
    )

    // Initialize state variables with the transaction data
    var name by remember { mutableStateOf(transaction.title) }

    // Extract numeric value from the amount string (removing currency symbol, negative sign, etc.)
    val initialValue = transaction.amount
        .replace("-$", "")
        .replace("$", "")
        .replace(",", "")

    var value by remember { mutableStateOf("$$initialValue") }
    var selectedType by remember {
        mutableStateOf(
            if (isEnglish)
                transaction.type
            else
                if (transaction.type == "Income") "รายรับ" else "รายจ่าย"
        )
    }
    var expanded by remember { mutableStateOf(false) }

    // Date selection state
    val initialDate = parseDate(transaction.date)
    var selectedDateMillis by remember { mutableStateOf(initialDate.timeInMillis) }
    var selectedDateText by remember {
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
        mutableStateOf(dateFormat.format(Date(selectedDateMillis)))
    }
    var showDatePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDateMillis
    )

    val transactionTypes = if (isEnglish)
        listOf("Income", "Outcome")
    else
        listOf("รายรับ", "รายจ่าย")

    // Date picker dialog
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { dateMillis ->
                        selectedDateMillis = dateMillis
                        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH)
                        selectedDateText = dateFormat.format(Date(dateMillis))
                    }
                    showDatePicker = false
                }) {
                    Text(if (isEnglish) "Confirm" else "ยืนยัน")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(if (isEnglish) "Cancel" else "ยกเลิก")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(onClick = onBack) {
            Text(if (isEnglish) "Back" else "กลับ")
        }

        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = if (isEnglish) "Edit Transaction" else "แก้ไขรายการ",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )

        Column {
            Text(
                text = if (isEnglish) "Name" else "ชื่อ",
                fontSize = 14.sp,
                color = Color.Gray
            )
            TextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }

        Column {
            Text(
                text = if (isEnglish) "Value" else "จำนวนเงิน",
                fontSize = 14.sp,
                color = Color.Gray
            )
            TextField(
                value = value,
                onValueChange = {
                    val cleaned = it.replace("[^\\d.]".toRegex(), "")
                    value = if (cleaned.isNotEmpty())
                        "$${"%,.2f".format(cleaned.toDoubleOrNull() ?: 0.0)}"
                    else ""
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                singleLine = true,
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }

        // Date selection field
        Column {
            Text(
                text = if (isEnglish) "Date" else "วันที่",
                fontSize = 14.sp,
                color = Color.Gray
            )
            TextField(
                value = selectedDateText,
                onValueChange = { },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                shape = RoundedCornerShape(16.dp),
                textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                trailingIcon = {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = "Select date",
                        modifier = Modifier.clickable { showDatePicker = true }
                    )
                },
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                )
            )
        }

        Column {
            Text(
                text = if (isEnglish) "Type" else "ประเภท",
                fontSize = 14.sp,
                color = Color.Gray
            )
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                TextField(
                    value = selectedType,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    transactionTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(type) },
                            onClick = {
                                selectedType = type
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (name.isNotEmpty() && value.isNotEmpty()) {
                    // Get the type in English format for storage
                    val type = if (isEnglish) {
                        if (selectedType == "Income") "Income" else "Outcome"
                    } else {
                        if (selectedType == "รายรับ") "Income" else "Outcome"
                    }

                    // Format date for display
                    val dateFormat = SimpleDateFormat("dd MMMM", Locale.ENGLISH)
                    val formattedDate = dateFormat.format(Date(selectedDateMillis))

                    // Format amount
                    val formattedValue = value.replace("[^\\d.]".toRegex(), "")
                    val amount = if (type == "Outcome") "-$$formattedValue" else "$$formattedValue"

                    // Format tax (for simplicity using a percentage of the amount)
                    val tax = if (isEnglish) "Tax $$formattedValue" else "ภาษี $$formattedValue"

                    // Create updated transaction with original ID
                    val updatedTransaction = Transaction(
                        id = transaction.id, // Keep the original ID
                        date = formattedDate,
                        title = name,
                        type = type,
                        amount = amount,
                        tax = tax
                    )

                    // Update transaction in repository
                    viewModel.updateTransaction(updatedTransaction, transaction)

                    // Go back
                    onBack()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            enabled = name.isNotEmpty() && value.isNotEmpty()
        ) {
            Text(
                text = if (isEnglish) "Save Changes" else "บันทึกการเปลี่ยนแปลง",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// Helper function to parse date from string
private fun parseDate(dateString: String): Calendar {
    val calendar = Calendar.getInstance()
    try {
        // Try to parse the date string (e.g., "01 March")
        val dateFormat = SimpleDateFormat("dd MMMM", Locale.ENGLISH)
        val date = dateFormat.parse(dateString)
        if (date != null) {
            calendar.time = date
            // Set the year to current year if not present in the date string
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            calendar.set(Calendar.YEAR, currentYear)
        }
    } catch (e: Exception) {
        // If parsing fails, return the current date
        e.printStackTrace()
    }
    return calendar
}