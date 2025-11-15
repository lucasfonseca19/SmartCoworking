package com.example.smartcoworking.ui.screens.mapa.components

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.example.smartcoworking.data.models.StatusEstacao

/**
 * Funções utilitárias reutilizáveis para renderização no mapa de coworking.
 *
 * Este arquivo centraliza lógica de desenho comum para evitar duplicação
 * e facilitar manutenção.
 */

// ============================================================================
// GRADIENTES E CORES
// ============================================================================

/**
 * Cria um gradiente vertical para efeito de profundidade.
 *
 * O gradiente vai de uma versão mais escura da cor (no topo) para a cor
 * original (na base), criando um efeito sutil de volume.
 *
 * @param baseColor Cor base para criar o gradiente
 * @param darkenFactor Fator de escurecimento (padrão: 0.9f = 10% mais escuro)
 * @return Brush com gradiente vertical
 */
fun DrawScope.createDepthGradient(
    baseColor: Color,
    darkenFactor: Float = DrawingConstants.GRADIENT_DARKEN_FACTOR
): Brush {
    val shadowColor = baseColor.copy(
        red = baseColor.red * darkenFactor,
        green = baseColor.green * darkenFactor,
        blue = baseColor.blue * darkenFactor
    )
    return Brush.verticalGradient(
        colors = listOf(shadowColor, baseColor),
        startY = 0f,
        endY = size.height
    )
}

// ============================================================================
// BORDAS
// ============================================================================

/**
 * Desenha uma borda para retângulo arredondado.
 *
 * @param size Tamanho do retângulo
 * @param borderWidth Espessura da borda
 * @param cornerRadius Raio dos cantos arredondados
 * @param color Cor da borda (padrão: preto semi-transparente)
 */
fun DrawScope.drawBorderRoundRect(
    size: Size,
    borderWidth: Float,
    cornerRadius: Float,
    color: Color = DrawingConstants.BORDER_COLOR
) {
    drawRoundRect(
        color = color,
        topLeft = Offset(borderWidth / 2, borderWidth / 2),
        size = Size(size.width - borderWidth, size.height - borderWidth),
        cornerRadius = CornerRadius(cornerRadius - (borderWidth / 2)),
        style = Stroke(width = borderWidth)
    )
}

/**
 * Desenha uma borda para círculo.
 *
 * @param radius Raio do círculo
 * @param center Centro do círculo
 * @param borderWidth Espessura da borda
 * @param color Cor da borda (padrão: preto semi-transparente)
 */
fun DrawScope.drawBorderCircle(
    radius: Float,
    center: Offset,
    borderWidth: Float,
    color: Color = DrawingConstants.BORDER_COLOR
) {
    drawCircle(
        color = color,
        radius = radius - (borderWidth / 2),
        center = center,
        style = Stroke(width = borderWidth)
    )
}

// ============================================================================
// PATHS
// ============================================================================

/**
 * Cria um Path de retângulo arredondado.
 *
 * Útil para aplicar padrões visuais (hachurado, pontilhado) dentro da forma.
 *
 * @param width Largura do retângulo (padrão: largura do canvas)
 * @param height Altura do retângulo (padrão: altura do canvas)
 * @param cornerRadius Raio dos cantos arredondados
 * @return Path do retângulo arredondado
 */
fun DrawScope.createRoundRectPath(
    width: Float = size.width,
    height: Float = size.height,
    cornerRadius: Float
): Path = Path().apply {
    addRoundRect(
        RoundRect(
            left = 0f,
            top = 0f,
            right = width,
            bottom = height,
            cornerRadius = CornerRadius(cornerRadius)
        )
    )
}

// ============================================================================
// PADRÕES VISUAIS (ACESSIBILIDADE)
// ============================================================================

/**
 * Aplica o padrão visual apropriado baseado no status da estação.
 *
 * Esta função centraliza a lógica de aplicação de padrões, evitando
 * duplicação do bloco when em vários lugares.
 *
 * @param path Path da forma onde aplicar o padrão
 * @param status Status da estação (LIVRE/OCUPADO/RESERVADO)
 * @param color Cor do padrão (padrão: preto)
 * @param escala Fator de escala para ajustar espaçamento e tamanhos
 */
fun DrawScope.aplicarPadraoStatus(
    path: Path,
    status: StatusEstacao,
    color: Color = Color.Black,
    escala: Float = 1f
) {
    when (status) {
        StatusEstacao.OCUPADO -> desenharPadraoHachurado(
            path = path,
            color = color,
            spacing = DrawingConstants.PATTERN_HATCHING_SPACING * escala,
            strokeWidth = DrawingConstants.PATTERN_HATCHING_STROKE * escala
        )
        StatusEstacao.RESERVADO -> desenharPadraoPontilhado(
            path = path,
            color = color,
            spacing = DrawingConstants.PATTERN_DOTTED_SPACING * escala,
            dotRadius = DrawingConstants.PATTERN_DOTTED_RADIUS * escala
        )
        StatusEstacao.LIVRE -> { /* Livre não tem padrão */ }
    }
}
