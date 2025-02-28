package com.example.projectapp.model

data class Transaction(
    val date: String,
    val title: String,
    val type: String, // "Income" or "Outcome"
    val amount: String,
    val tax: String
)

// Sample data for the app
val transactions = listOf(
    Transaction("01 March", "Salary", "Income", "$3,500.00", "Tax $350.00"),
    Transaction("03 March", "Grocery Shopping", "Outcome", "-$120.50", "Tax $8.44"),
    Transaction("05 March", "Freelance Work", "Income", "$500.00", "Tax $50.00"),
    Transaction("10 March", "Restaurant", "Outcome", "-$85.30", "Tax $5.97"),
    Transaction("15 March", "Bonus", "Income", "$1,200.00", "Tax $120.00"),
    Transaction("18 March", "Phone Bill", "Outcome", "-$45.99", "Tax $3.22"),
    Transaction("20 March", "Gas", "Outcome", "-$40.00", "Tax $2.80"),
    Transaction("25 March", "Rent", "Outcome", "-$1,200.00", "Tax $0.00"),
    Transaction("28 March", "Part-time Job", "Income", "$250.00", "Tax $25.00")
)

// Sample data for income screen
val transaction_Income = listOf(
    Transaction("01 March", "Salary", "Income", "$3,500.00", "Tax $350.00"),
    Transaction("05 March", "Freelance Work", "Income", "$500.00", "Tax $50.00"),
    Transaction("15 March", "Bonus", "Income", "$1,200.00", "Tax $120.00"),
    Transaction("28 March", "Part-time Job", "Income", "$250.00", "Tax $25.00"),
    Transaction("02 April", "Salary", "Income", "$3,500.00", "Tax $350.00"),
    Transaction("10 April", "Freelance Work", "Income", "$600.00", "Tax $60.00"),
    Transaction("20 April", "Stock Dividends", "Income", "$450.00", "Tax $45.00"),
    Transaction("28 April", "Side Project", "Income", "$350.00", "Tax $35.00")
)

// Sample data for outcome screen
val transaction_outcome = listOf(
    Transaction("03 March", "Grocery Shopping", "Outcome", "-$120.50", "Tax $8.44"),
    Transaction("10 March", "Restaurant", "Outcome", "-$85.30", "Tax $5.97"),
    Transaction("18 March", "Phone Bill", "Outcome", "-$45.99", "Tax $3.22"),
    Transaction("20 March", "Gas", "Outcome", "-$40.00", "Tax $2.80"),
    Transaction("25 March", "Rent", "Outcome", "-$1,200.00", "Tax $0.00"),
    Transaction("05 April", "Grocery Shopping", "Outcome", "-$135.75", "Tax $9.50"),
    Transaction("12 April", "Internet Bill", "Outcome", "-$65.00", "Tax $4.55"),
    Transaction("15 April", "Gym Membership", "Outcome", "-$50.00", "Tax $3.50"),
    Transaction("22 April", "Electricity Bill", "Outcome", "-$95.25", "Tax $6.67"),
    Transaction("28 April", "Rent", "Outcome", "-$1,200.00", "Tax $0.00")
)