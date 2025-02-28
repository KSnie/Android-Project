package com.example.projectapp.data.repository

import com.example.projectapp.data.dao.TransactionDao
import com.example.projectapp.model.Transaction
import com.example.projectapp.model.TransactionEntity
import com.example.projectapp.model.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TransactionRepository(private val transactionDao: TransactionDao) {

    val allTransactions: Flow<List<Transaction>> = transactionDao.getAllTransactions().map { entities ->
        entities.map { it.toTransaction() }
    }

    val incomeTransactions: Flow<List<Transaction>> = transactionDao.getIncomeTransactions().map { entities ->
        entities.map { it.toTransaction() }
    }

    val outcomeTransactions: Flow<List<Transaction>> = transactionDao.getOutcomeTransactions().map { entities ->
        entities.map { it.toTransaction() }
    }

    val totalIncome: Flow<Double?> = transactionDao.getTotalIncome()

    val totalOutcome: Flow<Double?> = transactionDao.getTotalOutcome()

    suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction.toEntity())
    }

    suspend fun updateTransaction(transaction: TransactionEntity) {
        transactionDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transaction: TransactionEntity) {
        transactionDao.deleteTransaction(transaction)
    }
}