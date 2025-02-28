package com.example.projectapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,
    val title: String,
    val type: String, // "Income" or "Outcome"
    val amount: String,
    val tax: String
) {
    // Convert Entity to UI model
    fun toTransaction(): Transaction {
        return Transaction(
            id = this.id,
            date = this.date,
            title = this.title,
            type = this.type,
            amount = this.amount,
            tax = this.tax
        )
    }
}

// Extension function to convert UI model to Entity
fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = this.id,
        date = this.date,
        title = this.title,
        type = this.type,
        amount = this.amount,
        tax = this.tax
    )
}