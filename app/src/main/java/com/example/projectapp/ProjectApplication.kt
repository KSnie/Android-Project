package com.example.projectapp

import android.app.Application
import com.example.projectapp.data.database.AppDatabase
import com.example.projectapp.data.repository.TransactionRepository

class ProjectApplication : Application() {
    // Using by lazy so the database and repository are only created when needed
    // rather than when the application starts
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { TransactionRepository(database.transactionDao()) }
}