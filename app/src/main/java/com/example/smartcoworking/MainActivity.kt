package com.example.smartcoworking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.smartcoworking.ui.screens.mapa.MapaCoworkingScreen
import com.example.smartcoworking.ui.theme.SmartCoworkingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCoworkingTheme {
                // A tela MapaCoworkingScreen já contém seu próprio Scaffold e toda a lógica de UI.
                // Substituímos o layout manual anterior por esta chamada mais simples para
                // carregar a tela completa do mapa.
                MapaCoworkingScreen()
            }
        }
    }
}