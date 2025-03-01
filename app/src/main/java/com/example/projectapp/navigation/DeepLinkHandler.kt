package com.example.projectapp.navigation

import android.content.Intent
import android.net.Uri

data class DeepLinkData(
    val destination: String,
    val title: String? = null,
    val amount: String? = null,
    val type: String? = null
)

object DeepLinkHandler {

    fun handleIntent(intent: Intent?): DeepLinkData? {
        val uri = intent?.data ?: return null

        return when(uri.host) {
            "input" -> parseInputDeepLink(uri)
            else -> null
        }
    }

    private fun parseInputDeepLink(uri: Uri): DeepLinkData {
        // Extract data from query parameters
        val title = uri.getQueryParameter("title")
        val amount = uri.getQueryParameter("amount")
        val type = uri.getQueryParameter("type")

        return DeepLinkData(
            destination = "input",
            title = title,
            amount = amount,
            type = type
        )
    }

    // Create a deep link Uri for testing/sharing
    fun createInputDeepLink(title: String? = null, amount: String? = null, type: String? = null): Uri {
        val builder = Uri.Builder()
            .scheme("projectapp")
            .authority("input")

        title?.let { builder.appendQueryParameter("title", it) }
        amount?.let { builder.appendQueryParameter("amount", it) }
        type?.let { builder.appendQueryParameter("type", it) }

        return builder.build()
    }
}