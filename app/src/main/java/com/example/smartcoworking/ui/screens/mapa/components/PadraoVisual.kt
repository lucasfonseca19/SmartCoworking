package com.example.smartcoworking.ui.screens.mapa.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import kotlin.math.ceil

/**
 * Desenha um padrão hachurado diagonal dentro de uma forma delimitada por um Path.
 * Usa clipPath para garantir que as linhas fiquem perfeitamente contidas na forma.
 *
 * OTIMIZAÇÃO: Usa uma única Path com todas as linhas em vez de múltiplas chamadas drawLine()
 * Reduz de ~100 operações de desenho para 1 operação (99% redução)
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
    // Cria uma única Path contendo todas as linhas
    val linePath = Path().apply {
        val diagonal = size.width + size.height
        val numLines = ceil(diagonal / spacing).toInt()

        // Adiciona todas as linhas diagonais à Path
        for (i in 0..numLines) {
            val offset = i * spacing - size.height
            moveTo(offset, 0f)
            lineTo(offset + size.height, size.height)
        }
    }

    // Desenha todas as linhas com uma única operação
    clipPath(path) {
        drawPath(
            path = linePath,
            color = color.copy(alpha = opacity),
            style = Stroke(width = strokeWidth)
        )
    }
}

/**
 * Desenha um padrão pontilhado uniformemente distribuído dentro de uma forma.
 * Usa clipPath para garantir que os pontos fiquem perfeitamente contidos na forma.
 *
 * OTIMIZAÇÃO: Usa uma única Path com todos os círculos em vez de múltiplas chamadas drawCircle()
 * Reduz de ~169 operações de desenho para 1 operação (99.4% redução)
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
    // Cria uma única Path contendo todos os pontos
    val dotPath = Path().apply {
        val numCols = ceil(size.width / spacing).toInt()
        val numRows = ceil(size.height / spacing).toInt()

        // Adiciona todos os círculos à Path de uma vez
        for (row in 0..numRows) {
            for (col in 0..numCols) {
                val x = col * spacing + spacing / 2
                val y = row * spacing + spacing / 2

                addOval(
                    androidx.compose.ui.geometry.Rect(
                        left = x - dotRadius,
                        top = y - dotRadius,
                        right = x + dotRadius,
                        bottom = y + dotRadius
                    )
                )
            }
        }
    }

    // Desenha todos os pontos com uma única operação
    clipPath(path) {
        drawPath(
            path = dotPath,
            color = color.copy(alpha = opacity)
        )
    }
}

/**
 * Desenha um padrão hachurado cruzado (horizontal + vertical) dentro de uma forma.
 * Este padrão é ideal para representar áreas não reserváveis (ex: área de descanso).
 * Usa clipPath para garantir que as linhas fiquem perfeitamente contidas na forma.
 *
 * OTIMIZAÇÃO: Usa uma única Path com todas as linhas em vez de múltiplas chamadas drawLine()
 * Reduz de ~50+ operações de desenho para 1 operação (98% redução)
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
    // Cria uma única Path contendo todas as linhas (horizontais e verticais)
    val gridPath = Path().apply {
        // Adiciona linhas horizontais
        val numHorizontalLines = ceil(size.height / spacing).toInt()
        for (i in 0..numHorizontalLines) {
            val y = i * spacing
            moveTo(0f, y)
            lineTo(size.width, y)
        }

        // Adiciona linhas verticais
        val numVerticalLines = ceil(size.width / spacing).toInt()
        for (i in 0..numVerticalLines) {
            val x = i * spacing
            moveTo(x, 0f)
            lineTo(x, size.height)
        }
    }

    // Desenha todas as linhas com uma única operação
    clipPath(path) {
        drawPath(
            path = gridPath,
            color = color.copy(alpha = opacity),
            style = Stroke(width = strokeWidth)
        )
    }
}
