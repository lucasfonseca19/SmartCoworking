package com.example.smartcoworking.ui.screens.mapa

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartcoworking.data.CanvasConfig
import com.example.smartcoworking.data.models.EstacaoDeTrabalho
import com.example.smartcoworking.data.models.StatusEstacao
import com.example.smartcoworking.ui.screens.mapa.components.ReservationCard
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
    onVoltar: (() -> Unit)? = null,
    onProfileClick: () -> Unit = {}
) {
    // Coletar estados do ViewModel
    val estacoes by viewModel.estacoes.collectAsState()
    val areasEspeciais by viewModel.areasEspeciais.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val estacaoSelecionada by viewModel.estacaoSelecionada.collectAsState()

    // Calcular número de estações disponíveis (status LIVRE)
    val estacoesDisponiveis = remember(estacoes) {
        estacoes.count { it.status == StatusEstacao.LIVRE }
    }



    Scaffold { paddingValues ->
        // Usar Box para permitir sobreposição (Floating UI)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // CAMADA 1: MAPA (Fundo total)
            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (estacoes.isEmpty()) {
                // Empty State
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Nenhuma estação disponível")
                    Button(onClick = { viewModel.recarregar() }) { Text("Recarregar") }
                }
            } else {
                // Mapa com Scroll
                BoxWithConstraints(
                    modifier = Modifier.fillMaxSize()
                ) {
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
                            onEstacaoClick = { estacao ->
                                viewModel.selecionarEstacao(estacao)
                                onEstacaoClick(estacao)
                            }
                        )
                    }
                }
            }

            // CAMADA 2: UI FLUTUANTE (Sobreposta ao mapa)
            
            // Container para alinhar Pílula e Botão de Usuário
            // (Ocultar quando o card estiver visível para limpar a tela)
            AnimatedVisibility(
                visible = estacaoSelecionada == null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .fillMaxWidth()
                        .padding(top = 48.dp, start = 24.dp, end = 24.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 2.1 Header Flutuante (Pílula de Status)
                    Surface(
                        shape = MaterialTheme.shapes.extraLarge,
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 4.dp,
                        tonalElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "$estacoesDisponiveis Livres",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    // 2.2 Botão de Usuário (Alinhado visualmente com a pílula)
                    Surface(
                        onClick = onProfileClick,
                        shape = MaterialTheme.shapes.extraLarge,
                        color = MaterialTheme.colorScheme.surface,
                        shadowElevation = 4.dp,
                        tonalElevation = 2.dp,
                        modifier = Modifier.height(48.dp)
                    ) {
                        Box(
                            modifier = Modifier.padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Perfil do Usuário",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }

            // CAMADA 3: CARD DE RESERVA (Centralizado)
            AnimatedVisibility(
                visible = estacaoSelecionada != null,
                enter = scaleIn() + fadeIn(),
                exit = scaleOut() + fadeOut(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                estacaoSelecionada?.let { estacao ->
                    ReservationCard(
                        station = estacao,
                        onDismiss = { viewModel.selecionarEstacao(null) },
                        onReserve = {
                            viewModel.reservarEstacao(estacao)
                        }
                    )
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