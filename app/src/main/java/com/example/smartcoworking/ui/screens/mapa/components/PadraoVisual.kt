package com.example.smartcoworking.ui.screens.mapa.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import kotlin.math.ceil

/**
 * Desenha um padrão hachurado diagonal dentro de uma forma delimitada por um Path.
 * Usa clipPath para garantir que as linhas fiquem perfeitamente contidas na forma.
 *
 * @param path O Path que define a forma onde o padrão será desenhado (usado para clipping)
 * @param color A cor base do padrão
 * @param spacing Espaçamento entre as linhas diagonais em pixels
 * @param opacity Opacidade do padrão (0.0 a 1.0). Padrão: 0.15 para não poluir
 */
fun DrawScope.desenharPadraoHachurado(
    path: Path,
    color: Color,
    spacing: Float = 10f,
    opacity: Float = 0.15f,
    strokeWidth: Float = 1.5f
) {
    clipPath(path) {
        // Calcula quantas linhas precisamos para cobrir toda a área
        val diagonal = size.width + size.height
        val numLines = ceil(diagonal / spacing).toInt()

        // Desenha linhas diagonais (top-left para bottom-right)
        for (i in 0..numLines) {
            val offset = i * spacing - size.height
            drawLine(
                color = color.copy(alpha = opacity),
                start = Offset(offset, 0f),
                end = Offset(offset + size.height, size.height),
                strokeWidth = strokeWidth
            )
        }
    }
}

/**
 * Desenha um padrão pontilhado uniformemente distribuído dentro de uma forma.
 * Usa clipPath para garantir que os pontos fiquem perfeitamente contidos na forma.
 *
 * @param path O Path que define a forma onde o padrão será desenhado (usado para clipping)
 * @param color A cor base do padrão
 * @param dotRadius Raio de cada ponto em pixels
 * @param spacing Espaçamento entre os pontos em pixels
 * @param opacity Opacidade do padrão (0.0 a 1.0). Padrão: 0.2 para não poluir
 */
fun DrawScope.desenharPadraoPontilhado(
    path: Path,
    color: Color,
    dotRadius: Float = 2f,
    spacing: Float = 8f,
    opacity: Float = 0.2f
) {
    clipPath(path) {
        // Calcula quantos pontos precisamos em cada direção
        val numCols = ceil(size.width / spacing).toInt()
        val numRows = ceil(size.height / spacing).toInt()

        // Desenha grid de pontos
        for (row in 0..numRows) {
            for (col in 0..numCols) {
                val x = col * spacing + spacing / 2
                val y = row * spacing + spacing / 2

                drawCircle(
                    color = color.copy(alpha = opacity),
                    radius = dotRadius,
                    center = Offset(x, y)
                )
            }
        }
    }
}

/**
 * Desenha um padrão hachurado cruzado (horizontal + vertical) dentro de uma forma.
 * Este padrão é ideal para representar áreas não reserváveis (ex: área de descanso).
 * Usa clipPath para garantir que as linhas fiquem perfeitamente contidas na forma.
 *
 * @param path O Path que define a forma onde o padrão será desenhado (usado para clipping)
 * @param color A cor base do padrão
 * @param spacing Espaçamento entre as linhas em pixels
 * @param opacity Opacidade do padrão (0.0 a 1.0). Padrão: 0.1 para indicar "não disponível" sutilmente
 */
fun DrawScope.desenharPadraoHachuradoCruzado(
    path: Path,
    color: Color,
    spacing: Float = 12f,
    opacity: Float = 0.1f,
    strokeWidth: Float = 1f
) {
    clipPath(path) {
        // Desenha linhas horizontais
        val numHorizontalLines = ceil(size.height / spacing).toInt()
        for (i in 0..numHorizontalLines) {
            val y = i * spacing
            drawLine(
                color = color.copy(alpha = opacity),
                start = Offset(0f, y),
                end = Offset(size.width, y),
                strokeWidth = strokeWidth
            )
        }

        // Desenha linhas verticais
        val numVerticalLines = ceil(size.width / spacing).toInt()
        for (i in 0..numVerticalLines) {
            val x = i * spacing
            drawLine(
                color = color.copy(alpha = opacity),
                start = Offset(x, 0f),
                end = Offset(x, size.height),
                strokeWidth = strokeWidth
            )
        }
    }
}
