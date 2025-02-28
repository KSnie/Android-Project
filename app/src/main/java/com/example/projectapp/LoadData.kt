package com.example.projectapp

data class Transaction(
    val date: String,
    val title: String,
    val type: String,
    val amount: String,
    val tax: String
)

val transactions = listOf(
    Transaction("07 April", "Shopping", "Income", "$75.00", "Tax $75.00"),
    Transaction("07 April", "Bonus", "Income", "$150.00", "Tax $150.00"),
    Transaction("07 April", "Salary", "Income", "$1,500.00", "Tax $1,500.00"),
    Transaction("07 April", "Rent", "Outcome", "-$1,200.00", "Tax -$1,200.00"),
    Transaction("08 April", "Groceries", "Outcome", "-$150.00", "Tax -$150.00"),
    Transaction("07 April", "Transport", "Outcome", "-$50.00", "Tax -$50.00")
)

val transaction_outcome = listOf(
    Transaction("07 April", "Rent", "Outcome", "-$1,200.00", "Tax -$1,200.00"),
    Transaction("08 April", "Groceries", "Outcome", "-$150.00", "Tax -$150.00"),
    Transaction("08 April", "Groceries", "Outcome", "-$150.00", "Tax -$150.00"),
    Transaction("08 April", "Groceries", "Outcome", "-$150.00", "Tax -$150.00"),
    Transaction("08 April", "Groceries", "Outcome", "-$150.00", "Tax -$150.00"),
    Transaction("08 April", "Groceries", "Outcome", "-$150.00", "Tax -$150.00"),
    Transaction("08 April", "Transport", "Outcome", "-$50.00", "Tax -$50.00"),
    Transaction("10 April", "Groceries", "Outcome", "-$150.00", "Tax -$150.00"),
    Transaction("11 April", "Groceries", "Outcome", "-$150.00", "Tax -$150.00"),
    Transaction("12 April", "Groceries", "Outcome", "-$150.00", "Tax -$150.00"),
    Transaction("13 April", "Groceries", "Outcome", "-$150.00", "Tax -$150.00"),
)

val transaction_Income = listOf(
    Transaction("07 April", "Shopping", "Income", "$75.00", "Tax $75.00"),
    Transaction("07 April", "Bonus", "Income", "$150.00", "Tax $150.00"),
    Transaction("07 April", "Salary", "Income", "$1,500.00", "Tax $1,500.00"),
)