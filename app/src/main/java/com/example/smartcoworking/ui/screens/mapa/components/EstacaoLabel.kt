package com.example.smartcoworking.ui.screens.mapa.components

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.unit.sp

/**
 * Desenha o número de identificação de uma estação centralizado na posição fornecida.
 *
 * Esta função de extensão para DrawScope é usada por todos os tipos de estações
 * (quadradas, circulares, retangulares) para renderizar o número de forma consistente.
 *
 * O texto é automaticamente centralizado tanto horizontal quanto verticalmente
 * em relação ao ponto fornecido.
 *
 * @param numero Número da estação a ser exibido (ex: 1, 2, 3...)
 * @param center Posição central onde o texto deve ser renderizado
 * @param textColor Cor do texto (padrão: branco para contraste com fundos coloridos)
 * @param textMeasurer TextMeasurer para calcular dimensões do texto antes do desenho
 * @param fontSizeSp Tamanho da fonte em sp (padrão: 16f, mas geralmente calculado baseado no tamanho da estação)
 *
 * @see DrawingConstants.FONT_SIZE_DIVISOR
 * @see DrawingConstants.MIN_FONT_SIZE
 */
fun DrawScope.desenharNumeroEstacao(
    numero: Int,
    center: Offset,
    textColor: Color = Color.White,
    textMeasurer: androidx.compose.ui.text.TextMeasurer,
    fontSizeSp: Float = 16f
) {
    val textLayoutResult = textMeasurer.measure(
        text = numero.toString(),
        style = TextStyle(
            fontSize = fontSizeSp.sp,
            color = textColor
        )
    )

    drawText(
        textLayoutResult = textLayoutResult,
        topLeft = Offset(
            x = center.x - textLayoutResult.size.width / 2,
            y = center.y - textLayoutResult.size.height / 2
        )
    )
}