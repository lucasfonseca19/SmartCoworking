package com.example.smartcoworking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartcoworking.ui.screens.login.LoginScreen
import com.example.smartcoworking.ui.screens.mapa.MapaCoworkingScreen
import com.example.smartcoworking.ui.theme.SmartCoworkingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCoworkingTheme {
                val navController = rememberNavController()
                
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate("map") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                    composable("map") {
                        MapaCoworkingScreen(
                            onProfileClick = {
                                navController.navigate("profile")
                            }
                        )
                    }
                    composable("profile") {
                        com.example.smartcoworking.ui.screens.profile.ProfileScreen(
                            onLogout = {
                                navController.navigate("login") {
                                    popUpTo("map") { inclusive = true }
                                }
                            },
                            onVoltar = {
                                navController.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}