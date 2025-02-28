package com.example.projectapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.projectapp.data.repository.TransactionRepository
import com.example.projectapp.model.Transaction
import com.example.projectapp.model.TransactionEntity
import com.example.projectapp.model.toEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {

    // Expose UI state as StateFlow
    val allTransactions: StateFlow<List<Transaction>> = repository.allTransactions
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val incomeTransactions: StateFlow<List<Transaction>> = repository.incomeTransactions
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val outcomeTransactions: StateFlow<List<Transaction>> = repository.outcomeTransactions
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val totalIncome: StateFlow<Double?> = repository.totalIncome
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    val totalOutcome: StateFlow<Double?> = repository.totalOutcome
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0.0
        )

    /**
     * Insert a new transaction into the database
     */
    fun insertTransaction(title: String, amount: String, type: String, isEnglish: Boolean) {
        viewModelScope.launch {
            val formattedValue = amount.replace("[^\\d.]".toRegex(), "")
            val dateFormat = SimpleDateFormat("dd MMMM", Locale.ENGLISH)
            val currentDate = dateFormat.format(Date())

            val transaction = Transaction(
                date = currentDate,
                title = title,
                type = type,
                amount = if (type == "Outcome") "-$$formattedValue" else "$$formattedValue",
                tax = if (isEnglish) "Tax $$formattedValue" else "ภาษี $$formattedValue"
            )

            repository.insertTransaction(transaction)
        }
    }

    /**
     * Insert a new transaction with a specific date
     */
    fun insertTransaction(title: String, amount: String, type: String, date: Date, isEnglish: Boolean) {
        viewModelScope.launch {
            val formattedValue = amount.replace("[^\\d.]".toRegex(), "")
            val dateFormat = SimpleDateFormat("dd MMMM", Locale.ENGLISH)
            val formattedDate = dateFormat.format(date)

            val transaction = Transaction(
                date = formattedDate,
                title = title,
                type = type,
                amount = if (type == "Outcome") "-$$formattedValue" else "$$formattedValue",
                tax = if (isEnglish) "Tax $$formattedValue" else "ภาษี $$formattedValue"
            )

            repository.insertTransaction(transaction)
        }
    }

    /**
     * Delete a transaction
     */
    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            // Direct conversion to entity with the id
            val entity = transaction.toEntity()
            repository.deleteTransaction(entity)
        }
    }

    /**
     * Update an existing transaction
     */
    fun updateTransaction(updatedTransaction: Transaction, originalTransaction: Transaction) {
        viewModelScope.launch {
            // Create updated entity with the original ID
            val updatedEntity = TransactionEntity(
                id = originalTransaction.id,
                date = updatedTransaction.date,
                title = updatedTransaction.title,
                type = updatedTransaction.type,
                amount = updatedTransaction.amount,
                tax = updatedTransaction.tax
            )

            repository.updateTransaction(updatedEntity)
        }
    }

    /**
     * Factory class for creating TransactionViewModel instances
     */
    class TransactionViewModelFactory(private val repository: TransactionRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(TransactionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return TransactionViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}