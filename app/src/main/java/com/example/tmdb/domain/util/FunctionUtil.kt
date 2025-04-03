package com.example.tmdb.domain.util

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

object FunctionUtil {

    @Composable
    fun isEdgeToEdgeEnabled(view: View): Boolean {

        // Retrieve WindowInsets and check for navigation bar visibility
        val isEdgeToEdge = remember {
            val insets = ViewCompat.getRootWindowInsets(view)
            if (insets != null) {
                val isNavBarVisible =
                    insets.isVisible(WindowInsetsCompat.Type.systemBars())
                !isNavBarVisible // If nav bar isn't visible, edge-to-edge is enabled
            } else {
                false // Fallback for older Android versions or unsupported cases
            }
        }

        return isEdgeToEdge
    }
}