package com.example.smartcoworking

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.smartcoworking.ui.theme.SmartCoworkingTheme
import androidx.compose.ui.unit.dp
import com.example.smartcoworking.data.CanvasConfig
import com.example.smartcoworking.data.EstacoesMockData
import com.example.smartcoworking.ui.screens.mapa.MapaCoworkingCanvas

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SmartCoworkingTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BoxWithConstraints(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        val hScroll = rememberScrollState()
                        val aspect = CanvasConfig.LARGURA / CanvasConfig.ALTURA
                        val topSpace = 72.dp
                        val bottomSpace = 56.dp
                        val mapHeight = maxHeight - topSpace - bottomSpace
                        val mapWidth = mapHeight * aspect

                        Column(modifier = Modifier.fillMaxSize()) {
                            Spacer(modifier = Modifier.height(topSpace))
                            Box(
                                modifier = Modifier
                                    .horizontalScroll(hScroll)
                                    .padding(0.dp)
                            ) {
                                MapaCoworkingCanvas(
                                    estacoes = EstacoesMockData.obterEstacoes(),
                                    areasEspeciais = EstacoesMockData.obterAreasEspeciais(),
                                    modifier = Modifier.size(mapWidth, mapHeight)
                                )
                            }
                            Spacer(modifier = Modifier.height(bottomSpace))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SmartCoworkingTheme {
        Greeting("Android")
    }
}
