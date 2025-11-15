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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartcoworking.data.models.StatusEstacao
import com.example.smartcoworking.ui.theme.MapColors
import com.example.smartcoworking.ui.theme.SmartCoworkingTheme
import com.example.smartcoworking.ui.screens.mapa.components.desenharNumeroEstacao

// ============================================================================
// COMPONENTES DE ESTAÇÕES RESERVÁVEIS
// ============================================================================
// Componentes que representam estações que podem ser reservadas pelos usuários.
// Cada forma possui seu uso específico conforme o roadmap do projeto.
// ============================================================================

/**
 * ## Estação Quadrada - Mesa Individual
 *
 * Desenha uma mesa individual pequena com forma quadrada.
 * Utilizada para representar estações de trabalho para uma única pessoa.
 *
 * ### Características Visuais:
 * - Gradiente vertical sutil para profundidade
 * - Borda fina para delimitação
 * - Número de identificação centralizado
 * - Padrão visual baseado no status (hachuras ou pontos)
 *
 * @param modifier Modificadores Compose para controle de layout
 * @param status Status atual (LIVRE/OCUPADO/RESERVADO)
 * @param numero Número de identificação da estação
 * @param size Tamanho do lado do quadrado em dp (padrão: 50dp)
 * @param cornerRadius Raio dos cantos arredondados (padrão: 8dp)
 * @param borderWidth Espessura da borda (padrão: 1dp)
 */
@Composable
fun DesenharEstacaoQuadrada(
    modifier: Modifier = Modifier,
    status: StatusEstacao,
    numero: Int,
    size: Float = 50f,
    cornerRadius: Dp = 8.dp,
    borderWidth: Dp = 1.dp
) {
    val textMeasurer = rememberTextMeasurer()
    val color = MapColors.getStatusColor(status)

    Canvas(modifier = modifier.size(size.dp)) {
        val cornerRadiusPx = cornerRadius.toPx()
        val borderWidthPx = borderWidth.toPx()

        // PASSO 1: Criar gradiente para sombreamento interno
        val gradientBrush = createDepthGradient(color)

        // PASSO 2: Desenhar fundo com gradiente
        drawRoundRect(
            brush = gradientBrush,
            topLeft = Offset.Zero,
            size = this.size,
            cornerRadius = CornerRadius(cornerRadiusPx)
        )

        // PASSO 3: Desenhar borda para profundidade
        drawBorderRoundRect(
            size = this.size,
            borderWidth = borderWidthPx,
            cornerRadius = cornerRadiusPx
        )

        // PASSO 4: Aplicar padrão visual (acessibilidade)
        val shapePath = createRoundRectPath(cornerRadius = cornerRadiusPx)
        aplicarPadraoStatus(shapePath, status)

        // PASSO 5: Desenhar número da estação
        desenharNumeroEstacao(
            numero = numero,
            center = center,
            textColor = Color.White,
            textMeasurer = textMeasurer,
            fontSizeSp = (size / DrawingConstants.FONT_SIZE_DIVISOR)
        )
    }
}

/**
 * ## Estação Circular - Mesa Colaborativa
 *
 * Desenha uma mesa colaborativa redonda.
 * Utilizada para representar áreas de trabalho compartilhado entre múltiplas pessoas.
 *
 * ### Características Visuais:
 * - Forma circular perfeita
 * - Gradiente vertical para volume
 * - Padrões visuais para acessibilidade
 *
 * @param modifier Modificadores Compose para controle de layout
 * @param status Status atual (LIVRE/OCUPADO/RESERVADO)
 * @param numero Número de identificação da estação
 * @param size Diâmetro do círculo em dp (padrão: 80dp)
 * @param borderWidth Espessura da borda (padrão: 1dp)
 */
@Composable
fun DesenharEstacaoCircular(
    modifier: Modifier = Modifier,
    status: StatusEstacao,
    numero: Int,
    size: Float = 80f,
    borderWidth: Dp = 1.dp
) {
    val textMeasurer = rememberTextMeasurer()
    val color = MapColors.getStatusColor(status)

    Canvas(modifier = modifier.size(size.dp)) {
        val borderWidthPx = borderWidth.toPx()
        val radius = this.size.width / 2

        // PASSO 1: Criar gradiente
        val gradientBrush = createDepthGradient(color)

        // PASSO 2: Desenhar círculo com gradiente
        drawCircle(
            brush = gradientBrush,
            radius = radius,
            center = center
        )

        // PASSO 3: Desenhar borda
        drawBorderCircle(
            radius = radius,
            center = center,
            borderWidth = borderWidthPx
        )

        // PASSO 4: Aplicar padrão visual
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
        aplicarPadraoStatus(shapePath, status)

        // PASSO 5: Desenhar número
        desenharNumeroEstacao(
            numero = numero,
            center = center,
            textColor = Color.White,
            textMeasurer = textMeasurer,
            fontSizeSp = (size / DrawingConstants.FONT_SIZE_DIVISOR)
        )
    }
}

/**
 * ## Estação Retangular - Sala de Reunião / Cabine Privada
 *
 * Desenha uma sala de reunião ou cabine privativa com forma retangular.
 * Utilizada para espaços maiores que podem acomodar grupos ou trabalho individual privativo.
 *
 * ### Características Visuais:
 * - Proporções customizáveis (largura x altura)
 * - Cantos arredondados para suavidade
 * - Mesmos padrões visuais das outras formas
 *
 * @param modifier Modificadores Compose para controle de layout
 * @param status Status atual (LIVRE/OCUPADO/RESERVADO)
 * @param numero Número de identificação da estação
 * @param width Largura do retângulo em dp (padrão: 120dp)
 * @param height Altura do retângulo em dp (padrão: 70dp)
 * @param cornerRadius Raio dos cantos arredondados (padrão: 8dp)
 * @param borderWidth Espessura da borda (padrão: 1dp)
 */
@Composable
fun DesenharEstacaoRetangular(
    modifier: Modifier = Modifier,
    status: StatusEstacao,
    numero: Int,
    width: Float = 120f,
    height: Float = 70f,
    cornerRadius: Dp = 8.dp,
    borderWidth: Dp = 1.dp
) {
    val textMeasurer = rememberTextMeasurer()
    val color = MapColors.getStatusColor(status)

    Canvas(modifier = modifier.size(width.dp, height.dp)) {
        val cornerRadiusPx = cornerRadius.toPx()
        val borderWidthPx = borderWidth.toPx()

        // PASSO 1: Criar gradiente
        val gradientBrush = createDepthGradient(color)

        // PASSO 2: Desenhar fundo
        drawRoundRect(
            brush = gradientBrush,
            topLeft = Offset.Zero,
            size = this.size,
            cornerRadius = CornerRadius(cornerRadiusPx)
        )

        // PASSO 3: Desenhar borda
        drawBorderRoundRect(
            size = this.size,
            borderWidth = borderWidthPx,
            cornerRadius = cornerRadiusPx
        )

        // PASSO 4: Aplicar padrão visual
        val shapePath = createRoundRectPath(cornerRadius = cornerRadiusPx)
        aplicarPadraoStatus(shapePath, status)

        // PASSO 5: Desenhar número
        desenharNumeroEstacao(
            numero = numero,
            center = center,
            textColor = Color.White,
            textMeasurer = textMeasurer,
            fontSizeSp = (this.size.height / DrawingConstants.FONT_SIZE_DIVISOR)
        )
    }
}

// ============================================================================
// COMPONENTES DE ÁREAS ESPECIAIS (NÃO RESERVÁVEIS)
// ============================================================================
// Elementos decorativos ou áreas que não podem ser reservadas pelos usuários,
// como lounges, áreas de descanso, recepção, etc.
// ============================================================================

/**
 * ## Área Não Reservável - Área de Descanso/Lounge
 *
 * Desenha uma área especial que não pode ser reservada pelos usuários.
 * Utiliza padrão hachurado cruzado para indicar visualmente que não está disponível.
 *
 * ### Uso Típico:
 * - Áreas de descanso
 * - Lounges
 * - Recepção
 * - Áreas comuns
 *
 * ### Características Visuais:
 * - Fundo cinza claro
 * - Padrão hachurado cruzado sutil (opacidade 0.1)
 * - Label de texto customizável
 *
 * @param modifier Modificadores Compose para controle de layout
 * @param label Texto a ser exibido (ex: "Área de Descanso")
 * @param width Largura da área em dp (padrão: 180dp)
 * @param height Altura da área em dp (padrão: 100dp)
 * @param cornerRadius Raio dos cantos arredondados (padrão: 8dp)
 */
@Composable
fun DesenharAreaNaoReservavel(
    modifier: Modifier = Modifier,
    label: String = "Área de Descanso",
    width: Float = 180f,
    height: Float = 100f,
    cornerRadius: Dp = 8.dp
) {
    val textMeasurer = rememberTextMeasurer()
    val backgroundColor = Color(0xFFE0E0E0) // Cinza claro

    Canvas(modifier = modifier.size(width.dp, height.dp)) {
        val cornerRadiusPx = cornerRadius.toPx()

        // PASSO 1: Desenhar fundo cinza
        drawRoundRect(
            color = backgroundColor,
            topLeft = Offset.Zero,
            size = this.size,
            cornerRadius = CornerRadius(cornerRadiusPx)
        )

        // PASSO 2: Aplicar padrão hachurado cruzado (indica "não reservável")
        val shapePath = Path().apply {
            addRoundRect(
                androidx.compose.ui.geometry.RoundRect(
                    left = 0f,
                    top = 0f,
                    right = this@Canvas.size.width,
                    bottom = this@Canvas.size.height,
                    cornerRadius = CornerRadius(cornerRadiusPx)
                )
            )
        }

        desenharPadraoHachuradoCruzado(
            path = shapePath,
            color = Color.Gray
        )

        // PASSO 3: Desenhar borda
        drawRoundRect(
            color = Color.Gray.copy(alpha = 0.4f),
            topLeft = Offset(1f, 1f),
            size = Size(this.size.width - 2f, this.size.height - 2f),
            cornerRadius = CornerRadius(cornerRadiusPx - 1f),
            style = Stroke(width = 2f)
        )

        // PASSO 4: Desenhar label
        val fontSize = 12.sp
        val textLayoutResult = textMeasurer.measure(
            text = label,
            style = TextStyle(
                fontSize = fontSize,
                color = Color.DarkGray
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

// ============================================================================
// PREVIEWS - VISUALIZAÇÃO NO ANDROID STUDIO
// ============================================================================
// Previews para validar a aparência dos componentes durante o desenvolvimento.
// Cada preview mostra os três status possíveis (LIVRE, OCUPADO, RESERVADO).
// ============================================================================

@Preview(
    name = "Estações Quadradas - Todos os Status",
    showBackground = true,
    widthDp = 320,
    heightDp = 120
)
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
            DesenharEstacaoQuadrada(status = StatusEstacao.LIVRE, numero = 1, size = 80f)
            DesenharEstacaoQuadrada(status = StatusEstacao.OCUPADO, numero = 2, size = 80f)
            DesenharEstacaoQuadrada(status = StatusEstacao.RESERVADO, numero = 3, size = 80f)
        }
    }
}

@Preview(
    name = "Estações Circulares - Todos os Status",
    showBackground = true,
    widthDp = 320,
    heightDp = 120
)
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
            DesenharEstacaoCircular(status = StatusEstacao.LIVRE, numero = 4, size = 80f)
            DesenharEstacaoCircular(status = StatusEstacao.OCUPADO, numero = 5, size = 80f)
            DesenharEstacaoCircular(status = StatusEstacao.RESERVADO, numero = 6, size = 80f)
        }
    }
}

@Preview(
    name = "Estações Retangulares - Todos os Status",
    showBackground = true,
    widthDp = 450,
    heightDp = 120
)
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
            DesenharEstacaoRetangular(status = StatusEstacao.LIVRE, numero = 7)
            DesenharEstacaoRetangular(status = StatusEstacao.OCUPADO, numero = 8)
            DesenharEstacaoRetangular(status = StatusEstacao.RESERVADO, numero = 9)
            DesenharEstacaoRetangular(
                status = StatusEstacao.LIVRE,
                numero = 12,
                width = 70f,
                height = 120f
            )
        }
    }
}

@Preview(
    name = "Área Não Reservável - Demonstração",
    showBackground = true,
    widthDp = 220,
    heightDp = 140
)
@Composable
fun AreaNaoReservavelPreview() {
    SmartCoworkingTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            DesenharAreaNaoReservavel(label = "Área de Descanso")
        }
    }
}