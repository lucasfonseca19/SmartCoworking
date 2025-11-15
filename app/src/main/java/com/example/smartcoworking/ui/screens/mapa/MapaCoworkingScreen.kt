package com.example.smartcoworking.ui.screens.mapa

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartcoworking.data.CanvasConfig
import com.example.smartcoworking.data.models.EstacaoDeTrabalho
import com.example.smartcoworking.ui.theme.SmartCoworkingTheme

/**
 * Tela principal do Mapa de Coworking
 *
 * Exibe o canvas interativo com todas as estações de trabalho e permite
 * navegação para detalhes ao clicar em uma estação.
 *
 * @param viewModel ViewModel que gerencia o estado das estações
 * @param onEstacaoClick Callback para quando uma estação é clicada (navegação para detalhes)
 * @param onVoltar Callback para voltar à tela anterior (opcional, para navegação futura)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapaCoworkingScreen(
    viewModel: MapaViewModel = viewModel(),
    onEstacaoClick: (EstacaoDeTrabalho) -> Unit = {},
    onVoltar: (() -> Unit)? = null
) {
    // Coletar estados do ViewModel
    val estacoes by viewModel.estacoes.collectAsState()
    val areasEspeciais by viewModel.areasEspeciais.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    // Estado local para controlar exibição da legenda (Fase 6)
    var mostrarLegenda by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Mapa de Estações")
                        Text(
                            text = "${estacoes.size} estações disponíveis",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarLegenda = true },
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = "Mostrar Legenda"
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                // Estado de carregamento
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Nenhuma estação disponível (edge case)
                estacoes.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Nenhuma estação disponível",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.recarregar() }) {
                            Text("Recarregar")
                        }
                    }
                }

                // Renderizar mapa com scroll horizontal
                else -> {
                    BoxWithConstraints {
                        val hScroll = rememberScrollState()
                        val aspect = CanvasConfig.LARGURA / CanvasConfig.ALTURA
                        val mapHeight = this.maxHeight
                        val mapWidth = mapHeight * aspect

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .horizontalScroll(hScroll)
                        ) {
                            MapaCoworkingCanvas(
                                estacoes = estacoes,
                                areasEspeciais = areasEspeciais,
                                modifier = Modifier.size(mapWidth, mapHeight),
                                onEstacaoClick = onEstacaoClick
                            )
                        }
                    }
                }
            }

            // TODO Fase 6: Implementar LegendaDialog
            // if (mostrarLegenda) {
            //     LegendaDialog(onDismiss = { mostrarLegenda = false })
            // }
        }
    }
}

// ============================================================================
// PREVIEWS
// ============================================================================

@Preview(
    name = "Mapa Coworking Screen - Completo",
    showBackground = true,
    widthDp = 400,
    heightDp = 700
)
@Composable
fun MapaCoworkingScreenPreview() {
    SmartCoworkingTheme {
        MapaCoworkingScreen(
            onEstacaoClick = { estacao ->
                // Preview não navega, apenas simula o clique
                println("Estação clicada: ${estacao.nome}")
            }
        )
    }
}

@Preview(
    name = "Mapa Coworking Screen - Dark Mode",
    showBackground = true,
    widthDp = 400,
    heightDp = 700
)
@Composable
fun MapaCoworkingScreenDarkPreview() {
    SmartCoworkingTheme(darkTheme = true) {
        MapaCoworkingScreen(
            onEstacaoClick = { estacao ->
                println("Estação clicada: ${estacao.nome}")
            }
        )
    }
}

@Preview(
    name = "Mapa Coworking Screen - Landscape",
    showBackground = true,
    widthDp = 800,
    heightDp = 400
)
@Composable
fun MapaCoworkingScreenLandscapePreview() {
    SmartCoworkingTheme {
        MapaCoworkingScreen(
            onEstacaoClick = { estacao ->
                println("Estação clicada: ${estacao.nome}")
            }
        )
    }
}