package com.example.smartcoworking.ui.screens.mapa.components

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.lerp

/**
 * Funções utilitárias reutilizáveis para renderização no mapa de coworking.
 *
 * Este arquivo centraliza lógica de desenho comum para evitar duplicação
 * e facilitar manutenção. Contém efeitos visuais para polimento do design.
 */

// ============================================================================
// GRADIENTES E CORES
// ============================================================================

/**
 * Cria um gradiente radial para efeito de profundidade e brilho (sheen).
 *
 * O gradiente vai de uma versão mais clara da cor (no centro superior) para
 * uma versão mais escura (nas bordas), criando um efeito de volume e iluminação.
 *
 * @param baseColor Cor base para criar o gradiente
 * @return Brush com gradiente radial
 */
fun DrawScope.createPolishedGradient(
    baseColor: Color
): Brush {
    val highlightColor = lerp(baseColor, Color.White, 0.2f)
    val shadowColor = lerp(baseColor, Color.Black, 0.2f)

    return Brush.radialGradient(
        colors = listOf(highlightColor, baseColor, shadowColor),
        center = Offset(size.width * 0.5f, size.height * 0.25f), // Ponto de brilho no topo
        radius = size.width * 1.1f // Raio grande para suavidade
    )
}

// ============================================================================
// BORDAS (EFEITO 3D / BEVEL)
// ============================================================================

/**
 * Cria um pincel de gradiente para a borda, simulando um efeito de chanfro (bevel).
 * A borda terá um brilho na parte superior e uma sombra na parte inferior.
 */
private fun createBevelBrush(): Brush = Brush.verticalGradient(
    colors = listOf(
        Color.White.copy(alpha = 0.5f), // Brilho superior
        Color.Transparent,
        Color.Black.copy(alpha = 0.25f) // Sombra inferior
    )
)

/**
 * Desenha uma borda com efeito de chanfro (bevel) para retângulo arredondado.
 *
 * @param size Tamanho do retângulo
 * @param borderWidth Espessura da borda
 * @param cornerRadius Raio dos cantos arredondados
 */
fun DrawScope.drawBeveledBorder(
    size: Size,
    borderWidth: Float,
    cornerRadius: Float
) {
    drawRoundRect(
        brush = createBevelBrush(),
        topLeft = Offset(borderWidth / 2, borderWidth / 2),
        size = Size(size.width - borderWidth, size.height - borderWidth),
        cornerRadius = CornerRadius(cornerRadius - (borderWidth / 2)),
        style = Stroke(width = borderWidth)
    )
}

/**
 * Desenha uma borda com efeito de chanfro (bevel) para círculo.
 *
 * @param radius Raio do círculo
 * @param center Centro do círculo
 * @param borderWidth Espessura da borda
 */
fun DrawScope.drawBeveledBorder(
    radius: Float,
    center: Offset,
    borderWidth: Float
) {
    drawCircle(
        brush = createBevelBrush(),
        radius = radius - (borderWidth / 2),
        center = center,
        style = Stroke(width = borderWidth)
    )
}