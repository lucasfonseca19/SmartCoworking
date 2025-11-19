package com.example.smartcoworking.ui.screens.mapa

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
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

import androidx.compose.ui.unit.sp
import com.example.smartcoworking.data.CanvasConfig
import com.example.smartcoworking.data.EstacoesMockData
import com.example.smartcoworking.data.models.*
import com.example.smartcoworking.ui.screens.mapa.components.*
import com.example.smartcoworking.ui.theme.MapColors
import com.example.smartcoworking.ui.theme.SmartCoworkingTheme
import androidx.compose.ui.geometry.CornerRadius

import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import kotlin.math.max

/**
 * Canvas principal do mapa de coworking.
 * Renderiza todas as estações e áreas especiais em escala.
 *
 * Sistema de coordenadas interno definido em CanvasConfig (1500x900px) e
 * escalado de forma uniforme conforme o tamanho externo fornecido via `modifier`.
 *
 * @param estacoes Lista de estações de trabalho a serem renderizadas
 * @param areasEspeciais Lista de áreas não reserváveis (ex: área de descanso)
 * @param modifier Modificadores Compose para o canvas
 * @param onEstacaoClick Callback opcional para detecção de cliques (Fase 4)
 */
@Composable
fun MapaCoworkingCanvas(
    estacoes: List<EstacaoDeTrabalho>,
    modifier: Modifier = Modifier,
    areasEspeciais: List<AreaEspecial> = emptyList(),
    onEstacaoClick: ((EstacaoDeTrabalho) -> Unit)? = null
) {
    val textMeasurer = rememberTextMeasurer()

    // OTIMIZAÇÃO: Cache de TextLayoutResult para evitar 25+ measure() calls por frame
    val textLayoutCache = androidx.compose.runtime.remember {
        mutableMapOf<Pair<Int, Float>, androidx.compose.ui.text.TextLayoutResult>()
    }

    val backgroundColor = MaterialTheme.colorScheme.background

    BoxWithConstraints(modifier = modifier) {
        // Dimensões do Canvas em Pixels
        val canvasHeight = constraints.maxHeight.toFloat()


        // Parâmetros de Escala (Mesma lógica do DrawScope)
        val escala = canvasHeight / CanvasConfig.ALTURA
        val conteudoEscala = 0.80f
        val verticalOffset = CanvasConfig.ALTURA * 0.05f
        val centerBase = Offset(CanvasConfig.LARGURA / 2f, CanvasConfig.ALTURA / 2f)

        fun scaleAroundCenter(base: Offset): Offset {
            return Offset(
                x = centerBase.x + (base.x - centerBase.x) * conteudoEscala,
                y = centerBase.y + (base.y - centerBase.y) * conteudoEscala + verticalOffset
            )
        }

        // Função inversa: Tela (Pixels) -> Canvas Virtual (1500x900)
        fun screenToCanvas(screenOffset: Offset): Offset {
            // 1. Desfazer a escala global
            val unscaled = screenOffset / escala
            
            // 2. Desfazer o deslocamento vertical e escala de conteúdo
            // y = centerBase.y + (base.y - centerBase.y) * conteudoEscala + verticalOffset
            // (y - verticalOffset - centerBase.y) / conteudoEscala + centerBase.y = base.y
            
            val x = (unscaled.x - centerBase.x) / conteudoEscala + centerBase.x
            val y = (unscaled.y - verticalOffset - centerBase.y) / conteudoEscala + centerBase.y
            
            return Offset(x, y)
        }

        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = { tapOffset ->
                            val canvasPoint = screenToCanvas(tapOffset)
                            
                            // Verificar colisão com estações
                            val estacaoClicada = estacoes.find { estacao ->
                                canvasPoint.x >= estacao.posicao.x &&
                                canvasPoint.x <= estacao.posicao.x + estacao.dimensoes.largura &&
                                canvasPoint.y >= estacao.posicao.y &&
                                canvasPoint.y <= estacao.posicao.y + estacao.dimensoes.altura
                            }
                            
                            if (estacaoClicada != null) {
                                onEstacaoClick?.invoke(estacaoClicada)
                            }
                        }
                    )
                }
        ) {
            // ====================================================================
            // PASSO 1: Desenhar fundo do canvas
            // ====================================================================
            drawRect(
                color = backgroundColor,
                size = size
            )

            // ====================================================================
            // PASSO 2: Desenhar áreas especiais (área de descanso, etc)
            // ====================================================================
            areasEspeciais.forEach { area ->
                val posicaoBase = Offset(area.posicao.x, area.posicao.y)
                val posicaoEscalada = scaleAroundCenter(posicaoBase) * escala
                val dimensoesEscaladas = Size(
                    width = area.dimensoes.largura * escala * conteudoEscala,
                    height = area.dimensoes.altura * escala * conteudoEscala
                )

                // Fundo cinza claro
                drawRoundRect(
                    color = Color(0xFFE0E0E0),
                    topLeft = posicaoEscalada,
                    size = dimensoesEscaladas,
                    cornerRadius = CornerRadius(8f * escala)
                )

                // Borda
                val borderWidth = 2f * escala * conteudoEscala
                drawRoundRect(
                    color = Color.Gray.copy(alpha = 0.4f),
                    topLeft = posicaoEscalada + Offset(borderWidth / 2, borderWidth / 2),
                    size = Size(dimensoesEscaladas.width - borderWidth, dimensoesEscaladas.height - borderWidth),
                    cornerRadius = CornerRadius(8f * escala * conteudoEscala - (borderWidth / 2)),
                    style = Stroke(width = borderWidth)
                )

                // Label da área
                val fontSize = max(12f * escala * conteudoEscala, 12f).sp
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
            // PASSO 4: Renderizar todas as estações
            // ====================================================================
            estacoes.forEach { estacao ->
                val color = MapColors.getStatusColor(estacao.status)

                val posicaoBase = Offset(estacao.posicao.x, estacao.posicao.y)
                val posicaoEscalada = scaleAroundCenter(posicaoBase) * escala
                val dimensoesEscaladas = Size(
                    width = estacao.dimensoes.largura * escala * conteudoEscala,
                    height = estacao.dimensoes.altura * escala * conteudoEscala
                )

                // Usar translate para posicionar o desenho
                translate(posicaoEscalada.x, posicaoEscalada.y) {
                    // =================================================
                    // NOVOS EFEITOS VISUAIS
                    // =================================================
                    val shadowOffset = Offset(3f * escala, 3f * escala)
                    val shadowColor = Color.Black.copy(alpha = 0.25f)
                    val borderWidth = DrawingConstants.DEFAULT_BORDER_WIDTH_DP * escala * conteudoEscala
                    val cornerRadiusPx = DrawingConstants.DEFAULT_CORNER_RADIUS_DP * escala * conteudoEscala

                    // PASSO 1: Desenhar Sombra (Drop Shadow)
                    when (estacao.forma) {
                        FormaEstacao.QUADRADO, FormaEstacao.RETANGULO -> {
                            drawRoundRect(
                                color = shadowColor,
                                topLeft = shadowOffset,
                                size = dimensoesEscaladas,
                                cornerRadius = CornerRadius(cornerRadiusPx)
                            )
                        }
                        FormaEstacao.CIRCULO -> {
                            val radius = dimensoesEscaladas.width / 2
                            drawCircle(
                                color = shadowColor,
                                radius = radius,
                                center = Offset(radius, radius) + shadowOffset
                            )
                        }
                    }

                    // PASSO 2: Desenhar Fundo com Gradiente Polido
                    val gradientBrush = createPolishedGradient(color)
                    when (estacao.forma) {
                        FormaEstacao.QUADRADO, FormaEstacao.RETANGULO -> {
                            drawRoundRect(
                                brush = gradientBrush,
                                topLeft = Offset.Zero,
                                size = dimensoesEscaladas,
                                cornerRadius = CornerRadius(cornerRadiusPx)
                            )
                        }
                        FormaEstacao.CIRCULO -> {
                            val radius = dimensoesEscaladas.width / 2
                            drawCircle(
                                brush = gradientBrush,
                                radius = radius,
                                center = Offset(radius, radius)
                            )
                        }
                    }

                    // PASSO 3: Desenhar Borda Chanfrada (Beveled)
                    when (estacao.forma) {
                        FormaEstacao.QUADRADO, FormaEstacao.RETANGULO -> {
                            drawBeveledBorder(
                                size = dimensoesEscaladas,
                                borderWidth = borderWidth,
                                cornerRadius = cornerRadiusPx
                            )
                        }
                        FormaEstacao.CIRCULO -> {
                            val radius = dimensoesEscaladas.width / 2
                            drawBeveledBorder(
                                radius = radius,
                                center = Offset(radius, radius),
                                borderWidth = borderWidth
                            )
                        }
                    }

                    // PASSO 4: Desenhar Número da Estação
                    val centerLocal = Offset(
                        dimensoesEscaladas.width / 2,
                        dimensoesEscaladas.height / 2
                    )
                    val fontSizeSp = max(
                        (dimensoesEscaladas.height / DrawingConstants.FONT_SIZE_DIVISOR),
                        DrawingConstants.MIN_FONT_SIZE
                    )

                    val cacheKey = estacao.numero to fontSizeSp
                    val textLayoutResult = textLayoutCache.getOrPut(cacheKey) {
                        textMeasurer.measure(
                            text = estacao.numero.toString(),
                            style = TextStyle(
                                fontSize = fontSizeSp.sp,
                                color = Color.White
                            )
                        )
                    }

                    drawText(
                        textLayoutResult = textLayoutResult,
                        topLeft = Offset(
                            x = centerLocal.x - textLayoutResult.size.width / 2,
                            y = centerLocal.y - textLayoutResult.size.height / 2
                        )
                    )
                }
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
