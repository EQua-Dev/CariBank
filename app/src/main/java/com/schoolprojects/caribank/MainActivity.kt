package com.schoolprojects.caribank

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.schoolprojects.caribank.holder.HolderScreen
import com.schoolprojects.caribank.ui.theme.CariBankTheme
import com.schoolprojects.corrreps.providers.LocalNavHost
import com.schoolprojects.corrreps.utils.LocalScreenSize
import com.schoolprojects.corrreps.utils.getScreenSize
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val defaultStatusBarColor = MaterialTheme.colorScheme.background.toArgb()
            var statusBarColor by remember { mutableStateOf(defaultStatusBarColor) }
            window.statusBarColor = statusBarColor

            /** Our navigation controller */
            val navController = rememberNavController()

            /** Getting screen size */
            val size = LocalContext.current.getScreenSize()

            CariBankTheme {
                // A surface container using the 'background' color from the theme
                CompositionLocalProvider(
                    LocalScreenSize provides size,
                    LocalNavHost provides navController
                ) {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        HolderScreen(
                            onStatusBarColorChange = {
                                //** Updating the color of the status bar *//*
                                //** Updating the color of the status bar *//*
                                //** Updating the color of the status bar *//*

                                //** Updating the color of the status bar *//*
                                statusBarColor = it.toArgb()
                            }
                        )
                    }
                }
            }
        }
    }
}