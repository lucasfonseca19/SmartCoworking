package com.example.smartcoworking.ui.screens.mapa

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartcoworking.data.CanvasConfig
import com.example.smartcoworking.data.EstacoesMockData
import com.example.smartcoworking.data.models.*
import com.example.smartcoworking.ui.screens.mapa.components.StatusEstacao
import com.example.smartcoworking.ui.screens.mapa.components.desenharPadraoHachurado
import com.example.smartcoworking.ui.screens.mapa.components.desenharPadraoHachuradoCruzado
import com.example.smartcoworking.ui.screens.mapa.components.desenharPadraoPontilhado
import com.example.smartcoworking.ui.theme.MapColors
import com.example.smartcoworking.ui.theme.SmartCoworkingTheme
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke

/**
 * Canvas principal do mapa de coworking.
 * Renderiza todas as estações e áreas especiais em escala.
 *
 * O canvas usa um sistema de coordenadas interno de 1200x800px (definido em CanvasConfig)
 * e escala proporcionalmente para o tamanho real disponível.
 *
 * @param estacoes Lista de estações de trabalho a serem renderizadas
 * @param areasEspeciais Lista de áreas não reserváveis (ex: área de descanso)
 * @param modifier Modificadores Compose para o canvas
 * @param onEstacaoClick Callback opcional para detecção de cliques (Fase 4)
 */
@Composable
fun MapaCoworkingCanvas(
    estacoes: List<EstacaoDeTrabalho>,
    areasEspeciais: List<AreaEspecial> = emptyList(),
    modifier: Modifier = Modifier,
    onEstacaoClick: ((EstacaoDeTrabalho) -> Unit)? = null
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier.fillMaxSize()) {
        // Calcular escala para adaptar coordenadas 1200x800 ao tamanho real
        val escalaX = size.width / CanvasConfig.LARGURA
        val escalaY = size.height / CanvasConfig.ALTURA

        // ====================================================================
        // PASSO 1: Desenhar fundo do canvas
        // ====================================================================
        drawRect(
            color = Color(0xFFFAFAFA), // Cinza muito claro
            size = size
        )

        // ====================================================================
        // PASSO 2: Desenhar áreas especiais (área de descanso, etc)
        // ====================================================================
        areasEspeciais.forEach { area ->
            val posicaoEscalada = Offset(
                x = area.posicao.x * escalaX,
                y = area.posicao.y * escalaY
            )
            val dimensoesEscaladas = Size(
                width = area.dimensoes.largura * escalaX,
                height = area.dimensoes.altura * escalaY
            )

            // Fundo cinza claro
            drawRoundRect(
                color = Color(0xFFE0E0E0),
                topLeft = posicaoEscalada,
                size = dimensoesEscaladas,
                cornerRadius = CornerRadius(8f * escalaX)
            )

            // Padrão hachurado cruzado
            val areaPath = Path().apply {
                addRoundRect(
                    androidx.compose.ui.geometry.RoundRect(
                        left = posicaoEscalada.x,
                        top = posicaoEscalada.y,
                        right = posicaoEscalada.x + dimensoesEscaladas.width,
                        bottom = posicaoEscalada.y + dimensoesEscaladas.height,
                        cornerRadius = CornerRadius(8f * escalaX)
                    )
                )
            }

            translate(posicaoEscalada.x, posicaoEscalada.y) {
                desenharPadraoHachuradoCruzado(
                    path = Path().apply {
                        addRoundRect(
                            androidx.compose.ui.geometry.RoundRect(
                                left = 0f,
                                top = 0f,
                                right = dimensoesEscaladas.width,
                                bottom = dimensoesEscaladas.height,
                                cornerRadius = CornerRadius(8f * escalaX)
                            )
                        )
                    },
                    color = Color.Gray,
                    spacing = 12f * escalaX,
                    opacity = 0.1f
                )
            }

            // Borda
            drawRoundRect(
                color = Color.Gray.copy(alpha = 0.4f),
                topLeft = posicaoEscalada + Offset(1f, 1f),
                size = Size(dimensoesEscaladas.width - 2f, dimensoesEscaladas.height - 2f),
                cornerRadius = CornerRadius(8f * escalaX - 1f),
                style = Stroke(width = 2f)
            )

            // Label da área
            val fontSize = (12f * ((escalaX + escalaY) / 2f)).sp
            val textLayoutResult = textMeasurer.measure(
                text = area.label,
                style = TextStyle(
                    fontSize = fontSize,
                    color = Color.DarkGray
                )
            )
            drawText(
                textLayoutResult = textLayoutResult,
                topLeft = Offset(
                    x = posicaoEscalada.x + dimensoesEscaladas.width / 2 - textLayoutResult.size.width / 2,
                    y = posicaoEscalada.y + dimensoesEscaladas.height / 2 - textLayoutResult.size.height / 2
                )
            )
        }

        // ====================================================================
        // PASSO 3: Desenhar labels decorativas (JANELA)
        // ====================================================================
        val labelJanelaStyle = TextStyle(
            fontSize = (14f * ((escalaX + escalaY) / 2f)).sp,
            color = Color.Gray.copy(alpha = 0.5f)
        )

        // Janela esquerda (vertical)
        val janelaEsqText = textMeasurer.measure("JANELA", labelJanelaStyle)
        drawText(
            textLayoutResult = janelaEsqText,
            topLeft = Offset(
                x = 10f * escalaX,
                y = (CanvasConfig.ALTURA / 2) * escalaY - janelaEsqText.size.height / 2
            )
        )

        // Janela direita (vertical)
        val janelaDirText = textMeasurer.measure("JANELA", labelJanelaStyle)
        drawText(
            textLayoutResult = janelaDirText,
            topLeft = Offset(
                x = (CanvasConfig.LARGURA - 80f) * escalaX,
                y = (CanvasConfig.ALTURA / 2) * escalaY - janelaDirText.size.height / 2
            )
        )

        // ====================================================================
        // PASSO 4: Renderizar todas as estações
        // ====================================================================
        estacoes.forEach { estacao ->
            val cor = when (estacao.status) {
                com.example.smartcoworking.data.models.StatusEstacao.LIVRE -> MapColors.StatusLivre
                com.example.smartcoworking.data.models.StatusEstacao.OCUPADO -> MapColors.StatusOcupado
                com.example.smartcoworking.data.models.StatusEstacao.RESERVADO -> MapColors.StatusReservado
            }

            val posicaoEscalada = Offset(
                x = estacao.posicao.x * escalaX,
                y = estacao.posicao.y * escalaY
            )
            val dimensoesEscaladas = Size(
                width = estacao.dimensoes.largura * escalaX,
                height = estacao.dimensoes.altura * escalaY
            )

            // Usar translate para posicionar o desenho
            translate(posicaoEscalada.x, posicaoEscalada.y) {
                // Criar gradiente para sombreamento
                val shadowColor = cor.copy(
                    red = cor.red * 0.9f,
                    green = cor.green * 0.9f,
                    blue = cor.blue * 0.9f
                )
                val gradientBrush = Brush.verticalGradient(
                    colors = listOf(shadowColor, cor),
                    startY = 0f,
                    endY = dimensoesEscaladas.height
                )

                // Desenhar forma baseada no tipo
                when (estacao.forma) {
                    FormaEstacao.QUADRADO -> {
                        // Fundo com gradiente
                        drawRoundRect(
                            brush = gradientBrush,
                            topLeft = Offset.Zero,
                            size = dimensoesEscaladas,
                            cornerRadius = CornerRadius(8f * escalaX)
                        )

                        // Borda
                        drawRoundRect(
                            color = Color.Black.copy(alpha = 0.2f),
                            topLeft = Offset(1f, 1f),
                            size = Size(dimensoesEscaladas.width - 2f, dimensoesEscaladas.height - 2f),
                            cornerRadius = CornerRadius(8f * escalaX - 1f),
                            style = Stroke(width = 1f)
                        )

                        // Aplicar padrão visual
                        val shapePath = Path().apply {
                            addRoundRect(
                                androidx.compose.ui.geometry.RoundRect(
                                    left = 0f,
                                    top = 0f,
                                    right = dimensoesEscaladas.width,
                                    bottom = dimensoesEscaladas.height,
                                    cornerRadius = CornerRadius(8f * escalaX)
                                )
                            )
                        }

                        when (estacao.status) {
                            com.example.smartcoworking.data.models.StatusEstacao.OCUPADO ->
                                desenharPadraoHachurado(shapePath, Color.Black, spacing = 10f * escalaX)
                            com.example.smartcoworking.data.models.StatusEstacao.RESERVADO ->
                                desenharPadraoPontilhado(shapePath, Color.Black, spacing = 8f * escalaX)
                            else -> { /* Livre não tem padrão */ }
                        }
                    }

                    FormaEstacao.CIRCULO -> {
                        val radius = dimensoesEscaladas.width / 2
                        val center = Offset(radius, radius)

                        // Círculo com gradiente
                        drawCircle(
                            brush = gradientBrush,
                            radius = radius,
                            center = center
                        )

                        // Borda
                        drawCircle(
                            color = Color.Black.copy(alpha = 0.2f),
                            radius = radius - 1f,
                            center = center,
                            style = Stroke(width = 1f)
                        )

                        // Aplicar padrão visual
                        val shapePath = Path().apply {
                            addOval(
                                androidx.compose.ui.geometry.Rect(
                                    left = center.x - radius,
                                    top = center.y - radius,
                                    right = center.x + radius,
                                    bottom = center.y + radius
                                )
                            )
                        }

                        when (estacao.status) {
                            com.example.smartcoworking.data.models.StatusEstacao.OCUPADO ->
                                desenharPadraoHachurado(shapePath, Color.Black, spacing = 10f * escalaX)
                            com.example.smartcoworking.data.models.StatusEstacao.RESERVADO ->
                                desenharPadraoPontilhado(shapePath, Color.Black, spacing = 8f * escalaX)
                            else -> { /* Livre não tem padrão */ }
                        }
                    }

                    FormaEstacao.RETANGULO -> {
                        // Fundo com gradiente
                        drawRoundRect(
                            brush = gradientBrush,
                            topLeft = Offset.Zero,
                            size = dimensoesEscaladas,
                            cornerRadius = CornerRadius(8f * escalaX)
                        )

                        // Borda
                        drawRoundRect(
                            color = Color.Black.copy(alpha = 0.2f),
                            topLeft = Offset(1f, 1f),
                            size = Size(dimensoesEscaladas.width - 2f, dimensoesEscaladas.height - 2f),
                            cornerRadius = CornerRadius(8f * escalaX - 1f),
                            style = Stroke(width = 1f)
                        )

                        // Aplicar padrão visual
                        val shapePath = Path().apply {
                            addRoundRect(
                                androidx.compose.ui.geometry.RoundRect(
                                    left = 0f,
                                    top = 0f,
                                    right = dimensoesEscaladas.width,
                                    bottom = dimensoesEscaladas.height,
                                    cornerRadius = CornerRadius(8f * escalaX)
                                )
                            )
                        }

                        when (estacao.status) {
                            com.example.smartcoworking.data.models.StatusEstacao.OCUPADO ->
                                desenharPadraoHachurado(shapePath, Color.Black, spacing = 10f * escalaX)
                            com.example.smartcoworking.data.models.StatusEstacao.RESERVADO ->
                                desenharPadraoPontilhado(shapePath, Color.Black, spacing = 8f * escalaX)
                            else -> { /* Livre não tem padrão */ }
                        }
                    }
                }

                // Desenhar número da estação
                val fontSize = ((dimensoesEscaladas.height / 5.5f)).sp
                val numeroText = textMeasurer.measure(
                    text = estacao.numero.toString(),
                    style = TextStyle(
                        fontSize = fontSize,
                        color = Color.White
                    )
                )

                val centerLocal = when (estacao.forma) {
                    FormaEstacao.CIRCULO -> Offset(
                        dimensoesEscaladas.width / 2,
                        dimensoesEscaladas.height / 2
                    )
                    else -> Offset(
                        dimensoesEscaladas.width / 2,
                        dimensoesEscaladas.height / 2
                    )
                }

                drawText(
                    textLayoutResult = numeroText,
                    topLeft = Offset(
                        x = centerLocal.x - numeroText.size.width / 2,
                        y = centerLocal.y - numeroText.size.height / 2
                    )
                )
            }
        }
    }
}

// ============================================================================
// PREVIEWS
// ============================================================================

@Preview(
    name = "Mapa Completo - Todas Estações",
    showBackground = true,
    widthDp = 600,
    heightDp = 400
)
@Composable
fun MapaCompletoPreview() {
    SmartCoworkingTheme {
        MapaCoworkingCanvas(
            estacoes = EstacoesMockData.obterEstacoes(),
            areasEspeciais = EstacoesMockData.obterAreasEspeciais()
        )
    }
}

@Preview(
    name = "Mapa Pequeno - Teste de Escala",
    showBackground = true,
    widthDp = 300,
    heightDp = 200
)
@Composable
fun MapaPequenoPreview() {
    SmartCoworkingTheme {
        MapaCoworkingCanvas(
            estacoes = EstacoesMockData.obterEstacoes(),
            areasEspeciais = EstacoesMockData.obterAreasEspeciais()
        )
    }
}

@Preview(
    name = "Mapa Grande - Teste de Escala",
    showBackground = true,
    widthDp = 800,
    heightDp = 533
)
@Composable
fun MapaGrandePreview() {
    SmartCoworkingTheme {
        MapaCoworkingCanvas(
            estacoes = EstacoesMockData.obterEstacoes(),
            areasEspeciais = EstacoesMockData.obterAreasEspeciais()
        )
    }
}
