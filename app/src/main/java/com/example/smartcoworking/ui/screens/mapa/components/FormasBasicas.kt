package com.example.smartcoworking.ui.screens.mapa.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartcoworking.ui.theme.MapColors
import com.example.smartcoworking.ui.theme.SmartCoworkingTheme

// Enum para representar o status da estação, como definido no roadmap.
enum class StatusEstacao {
    LIVRE, OCUPADO, RESERVADO
}

/**
 * Desenha um quadrado estilizado completo, representando uma mesa individual.
 * Inclui um sutil sombreamento interno para dar volume, e o número da estação.
 *
 * @param modifier Modificadores para controle de layout (size, offset, etc).
 * @param status O status atual da estação (LIVRE, OCUPADO, RESERVADO).
 * @param numero O número de identificação da estação a ser exibido.
 * @param size Tamanho do lado do quadrado em Dp.
 * @param cornerRadius Raio dos cantos arredondados em Dp.
 * @param borderWidth Largura da borda em Dp.
 */
@Composable
fun DesenharEstacaoQuadrada(
    modifier: Modifier = Modifier,
    status: StatusEstacao,
    numero: Int,
    size: Float = 50f,
    cornerRadius: Dp = 8.dp,
    borderWidth: Dp = 1.dp,
    rotationDegrees: Float = 0f // Novo parâmetro para rotação
) {
    val textMeasurer = rememberTextMeasurer()
    val color = when (status) {
        StatusEstacao.LIVRE -> MapColors.StatusLivre
        StatusEstacao.OCUPADO -> MapColors.StatusOcupado
        StatusEstacao.RESERVADO -> MapColors.StatusReservado
    }

    Canvas(modifier = modifier.size(size.dp)) {
        val cornerRadiusPx = cornerRadius.toPx()
        val borderWidthPx = borderWidth.toPx()

        // 1. Cria um gradiente sutil para o efeito de sombreamento interno
        val shadowColor = color.copy(
            red = color.red * 0.9f,
            green = color.green * 0.9f,
            blue = color.blue * 0.9f
        )
        val gradientBrush = Brush.verticalGradient(
            colors = listOf(shadowColor, color),
            startY = 0f,
            endY = this.size.height
        )

        // 2. Desenha o preenchimento com o gradiente
        drawRoundRect(
            brush = gradientBrush,
            topLeft = Offset.Zero,
            size = this.size,
            cornerRadius = CornerRadius(cornerRadiusPx)
        )

        // 3. Desenha a borda sutil para dar profundidade
        drawRoundRect(
            color = Color.Black.copy(alpha = 0.2f),
            topLeft = Offset(borderWidthPx / 2, borderWidthPx / 2),
            size = Size(this.size.width - borderWidthPx, this.size.height - borderWidthPx),
            cornerRadius = CornerRadius(cornerRadiusPx - (borderWidthPx / 2)),
            style = Stroke(width = borderWidthPx)
        )

        // 4. Desenha o número da estação no centro
        val fontSize = (size / 5.5).sp // Tamanho da fonte ajustado para ser menor
        val textLayoutResult = textMeasurer.measure(
            text = numero.toString(),
            style = TextStyle(
                fontSize = fontSize,
                color = Color.White
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
}


// ========================================
// PREVIEW - Validação no Android Studio
// ========================================

@Preview(name = "Estações Quadradas - Todos os Status", showBackground = true, widthDp = 320, heightDp = 120)
@Composable
fun EstacoesQuadradasPreview() {
    SmartCoworkingTheme {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        ) {
            DesenharEstacaoQuadrada(
                status = StatusEstacao.LIVRE,
                numero = 1,
                size = 80f
            )
            DesenharEstacaoQuadrada(
                status = StatusEstacao.OCUPADO,
                numero = 2,
                size = 80f
            )
            DesenharEstacaoQuadrada(
                status = StatusEstacao.RESERVADO,
                numero = 3,
                size = 80f
            )
        }
    }
}

/**
 * Desenha um círculo estilizado completo, representando uma mesa colaborativa.
 * Inclui um sutil sombreamento interno para dar volume, e o número da estação.
 *
 * @param modifier Modificadores para controle de layout (size, offset, etc).
 * @param status O status atual da estação (LIVRE, OCUPADO, RESERVADO).
 * @param numero O número de identificação da estação a ser exibido.
 * @param size Diâmetro do círculo em Dp.
 * @param borderWidth Largura da borda em Dp.
 */
@Composable
fun DesenharEstacaoCircular(
    modifier: Modifier = Modifier,
    status: StatusEstacao,
    numero: Int,
    size: Float = 80f,
    borderWidth: Dp = 1.dp,
    rotationDegrees: Float = 0f // Novo parâmetro para rotação
) {
    val textMeasurer = rememberTextMeasurer()
    val color = when (status) {
        StatusEstacao.LIVRE -> MapColors.StatusLivre
        StatusEstacao.OCUPADO -> MapColors.StatusOcupado
        StatusEstacao.RESERVADO -> MapColors.StatusReservado
    }

    Canvas(modifier = modifier.size(size.dp)) {
        val borderWidthPx = borderWidth.toPx()
        val radius = this.size.width / 2

        // 1. Cria um gradiente sutil para o efeito de sombreamento interno
        val shadowColor = color.copy(
            red = color.red * 0.9f,
            green = color.green * 0.9f,
            blue = color.blue * 0.9f
        )
        val gradientBrush = Brush.verticalGradient(
            colors = listOf(shadowColor, color),
            startY = 0f,
            endY = this.size.height
        )

        // 2. Desenha o preenchimento com o gradiente
        drawCircle(
            brush = gradientBrush,
            radius = radius,
            center = center
        )

        // 3. Desenha a borda sutil para dar profundidade
        drawCircle(
            color = Color.Black.copy(alpha = 0.2f),
            radius = radius - (borderWidthPx / 2),
            center = center,
            style = Stroke(width = borderWidthPx)
        )

        // 4. Desenha o número da estação no centro
        val fontSize = (size / 5.5).sp // Tamanho da fonte ajustado para ser menor
        val textLayoutResult = textMeasurer.measure(
            text = numero.toString(),
            style = TextStyle(
                fontSize = fontSize,
                color = Color.White
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
}

@Preview(name = "Estações Circulares - Todos os Status", showBackground = true, widthDp = 320, heightDp = 120)
@Composable
fun EstacoesCircularesPreview() {
    SmartCoworkingTheme {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        ) {
            DesenharEstacaoCircular(
                status = StatusEstacao.LIVRE,
                numero = 4,
                size = 80f
            )
            DesenharEstacaoCircular(
                status = StatusEstacao.OCUPADO,
                numero = 5,
                size = 80f
            )
            DesenharEstacaoCircular(
                status = StatusEstacao.RESERVADO,
                numero = 6,
                size = 80f
            )
        }
    }
}

/**
 * Desenha um retângulo estilizado completo, representando uma sala de reunião ou cabine.
 * Inclui um sutil sombreamento interno para dar volume, e o número da estação.
 *
 * @param modifier Modificadores para controle de layout (size, offset, etc).
 * @param status O status atual da estação (LIVRE, OCUPADO, RESERVADO).
 * @param numero O número de identificação da estação a ser exibido.
 * @param width Largura do retângulo em Dp.
 * @param height Altura do retângulo em Dp.
 * @param cornerRadius Raio dos cantos arredondados em Dp.
 * @param borderWidth Largura da borda em Dp.
 */
@Composable
fun DesenharEstacaoRetangular(
    modifier: Modifier = Modifier,
    status: StatusEstacao,
    numero: Int,
    width: Float = 120f,
    height: Float = 70f,
    cornerRadius: Dp = 8.dp,
    borderWidth: Dp = 1.dp,
    rotationDegrees: Float = 0f // Novo parâmetro para rotação
) {
    val textMeasurer = rememberTextMeasurer()
    val color = when (status) {
        StatusEstacao.LIVRE -> MapColors.StatusLivre
        StatusEstacao.OCUPADO -> MapColors.StatusOcupado
        StatusEstacao.RESERVADO -> MapColors.StatusReservado
    }

    Canvas(modifier = modifier.size(width.dp, height.dp)) {
        val cornerRadiusPx = cornerRadius.toPx()
        val borderWidthPx = borderWidth.toPx()

        // 1. Cria um gradiente sutil para o efeito de sombreamento interno
        val shadowColor = color.copy(
            red = color.red * 0.9f,
            green = color.green * 0.9f,
            blue = color.blue * 0.9f
        )
        val gradientBrush = Brush.verticalGradient(
            colors = listOf(shadowColor, color),
            startY = 0f,
            endY = this.size.height
        )

        // 2. Desenha o preenchimento com o gradiente
        drawRoundRect(
            brush = gradientBrush,
            topLeft = Offset.Zero,
            size = this.size,
            cornerRadius = CornerRadius(cornerRadiusPx)
        )

        // 3. Desenha a borda sutil para dar profundidade
        drawRoundRect(
            color = Color.Black.copy(alpha = 0.2f),
            topLeft = Offset(borderWidthPx / 2, borderWidthPx / 2),
            size = Size(this.size.width - borderWidthPx, this.size.height - borderWidthPx),
            cornerRadius = CornerRadius(cornerRadiusPx - (borderWidthPx / 2)),
            style = Stroke(width = borderWidthPx)
        )

        // 4. Desenha o número da estação no centro
        val fontSize = (this.size.height / 5.0).sp // Baseia o tamanho da fonte na altura, ajustado para ser menor
        val textLayoutResult = textMeasurer.measure(
            text = numero.toString(),
            style = TextStyle(
                fontSize = fontSize,
                color = Color.White
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
}

@Preview(name = "Estações Retangulares - Todos os Status", showBackground = true, widthDp = 450, heightDp = 120)
@Composable
fun EstacoesRetangularesPreview() {
    SmartCoworkingTheme {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
        ) {
            DesenharEstacaoRetangular(
                status = StatusEstacao.LIVRE,
                numero = 7
            )
            DesenharEstacaoRetangular(
                status = StatusEstacao.OCUPADO,
                numero = 8
            )
            DesenharEstacaoRetangular(
                status = StatusEstacao.RESERVADO,
                numero = 9
            )
            DesenharEstacaoRetangular(
                status = StatusEstacao.LIVRE,
                numero = 12,
                width = 70f,
                height = 120f,
                rotationDegrees = 90f
            )
        }
    }
}
