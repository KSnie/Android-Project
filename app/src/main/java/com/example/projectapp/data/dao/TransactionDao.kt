package com.example.projectapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.projectapp.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Delete
    suspend fun deleteTransaction(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE type = 'Income' ORDER BY date DESC")
    fun getIncomeTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE type = 'Outcome' ORDER BY date DESC")
    fun getOutcomeTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT SUM(CAST(REPLACE(REPLACE(amount, '$', ''), ',', '') AS REAL)) FROM transactions WHERE type = 'Income'")
    fun getTotalIncome(): Flow<Double?>

    @Query("SELECT SUM(CAST(REPLACE(REPLACE(REPLACE(amount, '-$', ''), '$', ''), ',', '') AS REAL)) FROM transactions WHERE type = 'Outcome'")
    fun getTotalOutcome(): Flow<Double?>
}