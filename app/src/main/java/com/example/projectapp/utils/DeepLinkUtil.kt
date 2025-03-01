package com.example.projectapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat

/**
 * Helper class for creating and sharing deep links to the app
 */
object DeepLinkUtil {

    /**
     * Create a deep link to the InputScreen with optional prefilled data
     */
    fun createInputDeepLink(
        title: String? = null,
        amount: String? = null,
        type: String? = null
    ): Uri {
        val builder = Uri.Builder()
            .scheme("projectapp")
            .authority("input")

        title?.let { builder.appendQueryParameter("title", it) }
        amount?.let { builder.appendQueryParameter("amount", it) }
        type?.let { builder.appendQueryParameter("type", it) }

        return builder.build()
    }

    /**
     * Share a deep link via platform sharing menu
     */
    fun shareDeepLink(context: Context, uri: Uri, message: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "${message}\n${uri}")
        }

        val shareIntent = Intent.createChooser(intent, null)
        ContextCompat.startActivity(context, shareIntent, null)
    }

    /**
     * Create and share a direct input transaction deep link
     */
    fun shareInputTransaction(
        context: Context,
        title: String,
        amount: String,
        type: String,
        message: String
    ) {
        val deepLink = createInputDeepLink(title, amount, type)
        shareDeepLink(context, deepLink, message)
    }
}