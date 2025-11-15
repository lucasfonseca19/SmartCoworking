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
import com.example.smartcoworking.ui.screens.mapa.components.*
import com.example.smartcoworking.ui.theme.MapColors
import com.example.smartcoworking.ui.theme.SmartCoworkingTheme
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.max
import com.example.smartcoworking.ui.screens.mapa.components.desenharNumeroEstacao

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
    areasEspeciais: List<AreaEspecial> = emptyList(),
    modifier: Modifier = Modifier,
    onEstacaoClick: ((EstacaoDeTrabalho) -> Unit)? = null
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier) {
        // Escala uniforme baseada na altura do container (preserva proporções 1500x900)
        val escala = size.height / CanvasConfig.ALTURA
        val escalaX = escala
        val escalaY = escala
        val conteudoEscala = 0.85f
        val centerBase = Offset(CanvasConfig.LARGURA / 2f, CanvasConfig.ALTURA / 2f)
        val compensateLeft = (CanvasConfig.LARGURA * (1f - conteudoEscala)) / 2f
        fun scaleAroundCenter(base: Offset): Offset {
            return Offset(
                x = centerBase.x + (base.x - centerBase.x) * conteudoEscala,
                y = centerBase.y + (base.y - centerBase.y) * conteudoEscala
            )
        }
        fun applyCompensations(o: Offset): Offset {
            return Offset(o.x - compensateLeft, o.y)
        }

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
            val posicaoBase = Offset(area.posicao.x, area.posicao.y)
            val posicaoEscalada = applyCompensations(scaleAroundCenter(posicaoBase)) * escala
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

            // Padrão hachurado cruzado
            translate(posicaoEscalada.x, posicaoEscalada.y) {
                desenharPadraoHachuradoCruzado(
                    path = Path().apply {
                        addRoundRect(
                            androidx.compose.ui.geometry.RoundRect(
                                left = 0f,
                                top = 0f,
                                right = dimensoesEscaladas.width,
                                bottom = dimensoesEscaladas.height,
                                cornerRadius = CornerRadius(8f * escala * conteudoEscala)
                            )
                        )
                    },
                    color = Color.Gray,
                    spacing = 12f * escala * conteudoEscala,
                    opacity = 0.1f,
                    strokeWidth = 1f * escala * conteudoEscala
                )
            }

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
            val posicaoEscalada = applyCompensations(scaleAroundCenter(posicaoBase)) * escala
            val dimensoesEscaladas = Size(
                width = estacao.dimensoes.largura * escala * conteudoEscala,
                height = estacao.dimensoes.altura * escala * conteudoEscala
            )

            // Usar translate para posicionar o desenho
            translate(posicaoEscalada.x, posicaoEscalada.y) {
                // Criar gradiente para sombreamento
                val gradientBrush = createDepthGradient(color)

                val borderWidth = DrawingConstants.DEFAULT_BORDER_WIDTH_DP * escala * conteudoEscala
                val cornerRadiusPx = DrawingConstants.DEFAULT_CORNER_RADIUS_DP * escala * conteudoEscala

                // Desenhar forma baseada no tipo
                when (estacao.forma) {
                    FormaEstacao.QUADRADO, FormaEstacao.RETANGULO -> {
                        // Fundo com gradiente
                        drawRoundRect(
                            brush = gradientBrush,
                            topLeft = Offset.Zero,
                            size = dimensoesEscaladas,
                            cornerRadius = CornerRadius(cornerRadiusPx)
                        )

                        // Borda
                        drawBorderRoundRect(
                            size = dimensoesEscaladas,
                            borderWidth = borderWidth,
                            cornerRadius = cornerRadiusPx
                        )

                        // Aplicar padrão visual
                        val shapePath = createRoundRectPath(
                            width = dimensoesEscaladas.width,
                            height = dimensoesEscaladas.height,
                            cornerRadius = cornerRadiusPx
                        )
                        aplicarPadraoStatus(
                            path = shapePath,
                            status = estacao.status,
                            escala = escala * conteudoEscala
                        )
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
                        drawBorderCircle(
                            radius = radius,
                            center = center,
                            borderWidth = borderWidth
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
                        aplicarPadraoStatus(
                            path = shapePath,
                            status = estacao.status,
                            escala = escala * conteudoEscala
                        )
                    }
                }

                val centerLocal = Offset(
                    dimensoesEscaladas.width / 2,
                    dimensoesEscaladas.height / 2
                )
                val fontSizeSp = max(
                    (dimensoesEscaladas.height / DrawingConstants.FONT_SIZE_DIVISOR),
                    DrawingConstants.MIN_FONT_SIZE
                )
                desenharNumeroEstacao(
                    numero = estacao.numero,
                    center = centerLocal,
                    textColor = Color.White,
                    textMeasurer = textMeasurer,
                    fontSizeSp = fontSizeSp
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
