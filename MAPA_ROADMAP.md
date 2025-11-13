# 🗺️ Roadmap de Desenvolvimento Incremental do Mapa Interativo

## 📋 Visão Geral

Este documento guia o desenvolvimento do componente de mapa interativo do Smart Coworking de forma **incremental e controlada**, construindo desde componentes atômicos até o sistema completo.

### Estratégia de Desenvolvimento

```
Componentes Atômicos → Componentes Compostos → Canvas Completo → Interatividade → Tempo Real
     (Formas)              (Estações)              (Mapa)         (Cliques)      (Simulação)
```

### Usando o Android Studio Preview

O Preview permite visualizar componentes sem executar o app. Use as seguintes anotações:

```kotlin
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark

// Preview básico
@Preview(showBackground = true)
@Composable
fun MeuComponentePreview() {
    SmartCoworkingTheme {
        MeuComponente()
    }
}

// Preview com múltiplos estados
@Preview(name = "Light Mode", showBackground = true)
@Preview(name = "Dark Mode", uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MeuComponentePreview() {
    SmartCoworkingTheme {
        MeuComponente()
    }
}

// Preview com dimensões específicas
@Preview(name = "Componente Grande", widthDp = 400, heightDp = 300)
@Composable
fun MeuComponenteGrandePreview() {
    SmartCoworkingTheme {
        MeuComponente()
    }
}
```

**Documentação Oficial:** https://developer.android.com/develop/ui/compose/tooling/previews?hl=pt-br

---

## 🎯 FASE 1: Componentes Atômicos (Fundação)

### Objetivo
Criar os blocos de construção mais básicos: formas geométricas simples com cores sólidas.

### 1.1 Definir Cores do Sistema de Status

**Arquivo:** `app/src/main/java/com/example/smartcoworking/ui/theme/MapColors.kt`

```kotlin
package com.example.smartcoworking.ui.theme

import androidx.compose.ui.graphics.Color

object MapColors {
    // Status das Estações
    val StatusLivre = Color(0xFF4CAF50)      // Verde sólido
    val StatusOcupado = Color(0xFFF44336)    // Vermelho
    val StatusReservado = Color(0xFFFFC107)  // Amarelo

    // Elementos Decorativos
    val AreaComum = Color(0xFFE0E0E0)        // Cinza claro
    val Borda = Color(0xFF757575)            // Cinza médio
    val Texto = Color(0xFF212121)            // Quase preto
    val TextoClaro = Color(0xFFFFFFFF)       // Branco
}
```

**Preview:**
```kotlin
@Preview(showBackground = true, widthDp = 300, heightDp = 100)
@Composable
fun MapColorsPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(MapColors.StatusLivre)
            ) {
                Text("Livre", modifier = Modifier.align(Alignment.Center))
            }
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(MapColors.StatusOcupado)
            ) {
                Text("Ocupado", modifier = Modifier.align(Alignment.Center), color = Color.White)
            }
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(MapColors.StatusReservado)
            ) {
                Text("Reservado", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
```

**Critérios de Aceitação:**
- ✅ Cores seguem especificação Material Design
- ✅ Contraste adequado com texto preto/branco
- ✅ Preview mostra as 3 cores principais

---

### 1.2 Componente: Quadrado (Mesa Individual)

**Arquivo:** `app/src/main/java/com/example/smartcoworking/ui/mapa/components/FormasBasicas.kt`

```kotlin
package com.example.smartcoworking.ui.mapa.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartcoworking.ui.theme.MapColors
import com.example.smartcoworking.ui.theme.SmartCoworkingTheme

/**
 * Desenha um quadrado representando uma mesa individual
 *
 * @param color Cor de preenchimento do quadrado
 * @param size Tamanho do lado do quadrado (padrão: 50f)
 * @param topLeft Posição superior esquerda (x, y)
 */
@Composable
fun DesenharQuadrado(
    color: Color,
    size: Float = 50f,
    topLeft: Offset = Offset.Zero,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size.dp)) {
        drawRect(
            color = color,
            topLeft = topLeft,
            size = Size(size, size)
        )
    }
}
```

**Preview:**
```kotlin
@Preview(name = "Quadrado Livre", showBackground = true)
@Composable
fun QuadradoLivrePreview() {
    SmartCoworkingTheme {
        DesenharQuadrado(
            color = MapColors.StatusLivre,
            size = 50f
        )
    }
}

@Preview(name = "Quadrado Ocupado", showBackground = true)
@Composable
fun QuadradoOcupadoPreview() {
    SmartCoworkingTheme {
        DesenharQuadrado(
            color = MapColors.StatusOcupado,
            size = 50f
        )
    }
}

@Preview(name = "Quadrado Reservado", showBackground = true)
@Composable
fun QuadradoReservadoPreview() {
    SmartCoworkingTheme {
        DesenharQuadrado(
            color = MapColors.StatusReservado,
            size = 50f
        )
    }
}

@Preview(name = "Variações de Tamanho", showBackground = true, widthDp = 300)
@Composable
fun QuadradosVariadosPreview() {
    SmartCoworkingTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DesenharQuadrado(color = MapColors.StatusLivre, size = 30f)
            DesenharQuadrado(color = MapColors.StatusLivre, size = 50f)
            DesenharQuadrado(color = MapColors.StatusLivre, size = 70f)
        }
    }
}
```

**Critérios de Aceitação:**
- ✅ Quadrado renderiza corretamente no Preview
- ✅ Cor é aplicada corretamente
- ✅ Tamanho pode ser customizado
- ✅ Posição pode ser customizada

---

### 1.3 Componente: Círculo (Mesa Colaborativa)

**Adicionar ao arquivo:** `app/src/main/java/com/example/smartcoworking/ui/mapa/components/FormasBasicas.kt`

```kotlin
/**
 * Desenha um círculo representando uma mesa colaborativa
 *
 * @param color Cor de preenchimento do círculo
 * @param radius Raio do círculo (padrão: 35f)
 * @param center Posição central (x, y)
 */
@Composable
fun DesenharCirculo(
    color: Color,
    radius: Float = 35f,
    center: Offset = Offset(radius, radius),
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size((radius * 2).dp)) {
        drawCircle(
            color = color,
            radius = radius,
            center = center
        )
    }
}
```

**Preview:**
```kotlin
@Preview(name = "Círculo Livre", showBackground = true)
@Composable
fun CirculoLivrePreview() {
    SmartCoworkingTheme {
        DesenharCirculo(
            color = MapColors.StatusLivre,
            radius = 35f
        )
    }
}

@Preview(name = "Círculos Todos Status", showBackground = true, widthDp = 300)
@Composable
fun CirculosTodosStatusPreview() {
    SmartCoworkingTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            DesenharCirculo(color = MapColors.StatusLivre, radius = 35f)
            DesenharCirculo(color = MapColors.StatusOcupado, radius = 35f)
            DesenharCirculo(color = MapColors.StatusReservado, radius = 35f)
        }
    }
}
```

**Critérios de Aceitação:**
- ✅ Círculo renderiza corretamente no Preview
- ✅ Raio pode ser customizado
- ✅ Cor é aplicada corretamente
- ✅ Centro pode ser posicionado

---

### 1.4 Componente: Retângulo (Sala/Cabine)

**Adicionar ao arquivo:** `app/src/main/java/com/example/smartcoworking/ui/mapa/components/FormasBasicas.kt`

```kotlin
/**
 * Desenha um retângulo representando sala de reunião ou cabine
 *
 * @param color Cor de preenchimento do retângulo
 * @param width Largura do retângulo (padrão: 100f)
 * @param height Altura do retângulo (padrão: 60f)
 * @param topLeft Posição superior esquerda (x, y)
 */
@Composable
fun DesenharRetangulo(
    color: Color,
    width: Float = 100f,
    height: Float = 60f,
    topLeft: Offset = Offset.Zero,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(width.dp, height.dp)) {
        drawRect(
            color = color,
            topLeft = topLeft,
            size = Size(width, height)
        )
    }
}
```

**Preview:**
```kotlin
@Preview(name = "Retângulos Variados", showBackground = true, widthDp = 300, heightDp = 200)
@Composable
fun RetangulosPreview() {
    SmartCoworkingTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sala de reunião grande
            DesenharRetangulo(
                color = MapColors.StatusLivre,
                width = 100f,
                height = 60f
            )
            // Cabine individual
            DesenharRetangulo(
                color = MapColors.StatusOcupado,
                width = 80f,
                height = 50f
            )
        }
    }
}
```

**Critérios de Aceitação:**
- ✅ Retângulo renderiza corretamente
- ✅ Largura e altura podem ser customizadas
- ✅ Diferentes proporções funcionam bem

---

### 1.5 Componente: Padrões Visuais (Acessibilidade)

**Arquivo:** `app/src/main/java/com/example/smartcoworking/ui/mapa/components/PadroesVisuais.kt`

```kotlin
package com.example.smartcoworking.ui.mapa.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartcoworking.ui.theme.MapColors
import com.example.smartcoworking.ui.theme.SmartCoworkingTheme

/**
 * Desenha padrão hachurado diagonal para status "Ocupado"
 */
fun DrawScope.desenharPadraoHachurado(
    bounds: Size,
    color: Color = MapColors.StatusOcupado,
    espacamento: Float = 10f
) {
    val linhas = (bounds.width / espacamento).toInt()

    for (i in 0..linhas) {
        val x = i * espacamento
        drawLine(
            color = color.copy(alpha = 0.3f),
            start = Offset(x, 0f),
            end = Offset(x + bounds.height, bounds.height),
            strokeWidth = 2f
        )
    }
}

/**
 * Desenha padrão pontilhado para status "Reservado"
 */
fun DrawScope.desenharPadraoPontilhado(
    bounds: Size,
    color: Color = MapColors.StatusReservado,
    espacamento: Float = 15f
) {
    val colsCount = (bounds.width / espacamento).toInt()
    val rowsCount = (bounds.height / espacamento).toInt()

    for (row in 0..rowsCount) {
        for (col in 0..colsCount) {
            drawCircle(
                color = color.copy(alpha = 0.4f),
                radius = 2f,
                center = Offset(col * espacamento, row * espacamento)
            )
        }
    }
}
```

**Preview:**
```kotlin
@Preview(name = "Padrão Hachurado", showBackground = true)
@Composable
fun PadraoHachuradoPreview() {
    SmartCoworkingTheme {
        Canvas(modifier = Modifier.size(100.dp)) {
            // Fundo
            drawRect(color = MapColors.StatusOcupado)
            // Padrão
            desenharPadraoHachurado(size)
        }
    }
}

@Preview(name = "Padrão Pontilhado", showBackground = true)
@Composable
fun PadraoPontilhadoPreview() {
    SmartCoworkingTheme {
        Canvas(modifier = Modifier.size(100.dp)) {
            // Fundo
            drawRect(color = MapColors.StatusReservado)
            // Padrão
            desenharPadraoPontilhado(size)
        }
    }
}

@Preview(name = "Comparação Padrões", showBackground = true, widthDp = 300)
@Composable
fun ComparacaoPadroesPreview() {
    SmartCoworkingTheme {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Livre (sem padrão)
            Canvas(modifier = Modifier.size(80.dp)) {
                drawRect(color = MapColors.StatusLivre)
            }
            // Ocupado (hachurado)
            Canvas(modifier = Modifier.size(80.dp)) {
                drawRect(color = MapColors.StatusOcupado)
                desenharPadraoHachurado(size)
            }
            // Reservado (pontilhado)
            Canvas(modifier = Modifier.size(80.dp)) {
                drawRect(color = MapColors.StatusReservado)
                desenharPadraoPontilhado(size)
            }
        }
    }
}
```

**Critérios de Aceitação:**
- ✅ Padrão hachurado visível sobre vermelho
- ✅ Padrão pontilhado visível sobre amarelo
- ✅ Padrões melhoram acessibilidade (distinguível sem cor)

---

### 1.6 Componente: Label de Número

**Arquivo:** `app/src/main/java/com/example/smartcoworking/ui/mapa/components/EstacaoLabel.kt`

```kotlin
package com.example.smartcoworking.ui.mapa.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartcoworking.ui.theme.SmartCoworkingTheme

/**
 * Desenha o número da estação centralizado
 */
fun DrawScope.desenharNumeroEstacao(
    numero: Int,
    center: Offset,
    textColor: Color = Color.White,
    textMeasurer: androidx.compose.ui.text.TextMeasurer
) {
    val text = numero.toString()
    val textLayoutResult = textMeasurer.measure(
        text = text,
        style = TextStyle(
            fontSize = 16.sp,
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
```

**Preview:**
```kotlin
@Preview(name = "Labels em Formas", showBackground = true, widthDp = 300)
@Composable
fun LabelsPreview() {
    SmartCoworkingTheme {
        val textMeasurer = rememberTextMeasurer()

        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Quadrado com número
            Canvas(modifier = Modifier.size(50.dp)) {
                drawRect(color = Color(0xFF4CAF50))
                desenharNumeroEstacao(
                    numero = 1,
                    center = Offset(size.width / 2, size.height / 2),
                    textColor = Color.White,
                    textMeasurer = textMeasurer
                )
            }

            // Círculo com número
            Canvas(modifier = Modifier.size(70.dp)) {
                drawCircle(
                    color = Color(0xFFF44336),
                    radius = 35f,
                    center = Offset(size.width / 2, size.height / 2)
                )
                desenharNumeroEstacao(
                    numero = 5,
                    center = Offset(size.width / 2, size.height / 2),
                    textColor = Color.White,
                    textMeasurer = textMeasurer
                )
            }
        }
    }
}
```

**Critérios de Aceitação:**
- ✅ Número centralizado na forma
- ✅ Texto legível em todas as cores de fundo
- ✅ Tamanho da fonte apropriado

---

## 🔧 FASE 2: Componentes de Estação (Composição)

### Objetivo
Combinar formas + cores + padrões + labels em componentes de estação completos.

### 2.1 Modelo de Dados: EstacaoDeTrabalho

**Arquivo:** `app/src/main/java/com/example/smartcoworking/data/models/EstacaoDeTrabalho.kt`

```kotlin
package com.example.smartcoworking.data.models

data class EstacaoDeTrabalho(
    val id: String,
    val numero: Int,
    val nome: String,
    val tipo: TipoEstacao,
    val capacidade: Int,
    val status: StatusEstacao,
    val leituraSensor: LeituraSensor,

    // Propriedades para renderização
    val posicaoX: Float,
    val posicaoY: Float,
    val largura: Float,
    val altura: Float,
    val forma: FormaEstacao
)

enum class TipoEstacao {
    MESA, SALA_REUNIAO, CABINE_PRIVADA
}

enum class StatusEstacao {
    LIVRE, OCUPADO, RESERVADO
}

enum class FormaEstacao {
    QUADRADO,
    CIRCULO,
    RETANGULO
}

data class LeituraSensor(
    val temperatura: Float,
    val nivelRuido: NivelRuido,
    val qualidadeAr: QualidadeAr,
    val timestamp: String
)

enum class NivelRuido {
    SILENCIOSO, MODERADO, ALTO
}

enum class QualidadeAr {
    BOA, REGULAR, RUIM
}
```

**Critérios de Aceitação:**
- ✅ Todos os enums definidos
- ✅ Estrutura de dados completa
- ✅ Preparado para serialização futura

---

### 2.2 Componente: EstacaoCanvas (Componente Unificado)

**Arquivo:** `app/src/main/java/com/example/smartcoworking/ui/mapa/components/EstacaoCanvas.kt`

```kotlin
package com.example.smartcoworking.ui.mapa.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartcoworking.data.models.*
import com.example.smartcoworking.ui.theme.MapColors
import com.example.smartcoworking.ui.theme.SmartCoworkingTheme

/**
 * Renderiza uma estação completa no canvas
 * Combina: forma + cor + padrão + número
 */
@Composable
fun RenderizarEstacao(
    estacao: EstacaoDeTrabalho,
    modifier: Modifier = Modifier
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier.size(estacao.largura.dp, estacao.altura.dp)) {
        val corStatus = when (estacao.status) {
            StatusEstacao.LIVRE -> MapColors.StatusLivre
            StatusEstacao.OCUPADO -> MapColors.StatusOcupado
            StatusEstacao.RESERVADO -> MapColors.StatusReservado
        }

        // Desenhar forma base
        when (estacao.forma) {
            FormaEstacao.QUADRADO -> {
                drawRect(
                    color = corStatus,
                    topLeft = Offset.Zero,
                    size = Size(estacao.largura, estacao.altura)
                )
            }
            FormaEstacao.CIRCULO -> {
                drawCircle(
                    color = corStatus,
                    radius = estacao.largura / 2,
                    center = Offset(size.width / 2, size.height / 2)
                )
            }
            FormaEstacao.RETANGULO -> {
                drawRect(
                    color = corStatus,
                    topLeft = Offset.Zero,
                    size = Size(estacao.largura, estacao.altura)
                )
            }
        }

        // Aplicar padrão visual se necessário
        when (estacao.status) {
            StatusEstacao.OCUPADO -> desenharPadraoHachurado(size, corStatus)
            StatusEstacao.RESERVADO -> desenharPadraoPontilhado(size, corStatus)
            else -> {} // Livre não tem padrão
        }

        // Desenhar número
        desenharNumeroEstacao(
            numero = estacao.numero,
            center = Offset(size.width / 2, size.height / 2),
            textColor = Color.White,
            textMeasurer = textMeasurer
        )
    }
}
```

**Preview:**
```kotlin
@Preview(name = "Mesa Livre", showBackground = true)
@Composable
fun MesaLivrePreview() {
    SmartCoworkingTheme {
        RenderizarEstacao(
            estacao = EstacaoDeTrabalho(
                id = "1",
                numero = 1,
                nome = "Mesa 1A",
                tipo = TipoEstacao.MESA,
                capacidade = 1,
                status = StatusEstacao.LIVRE,
                leituraSensor = LeituraSensor(
                    temperatura = 22.5f,
                    nivelRuido = NivelRuido.SILENCIOSO,
                    qualidadeAr = QualidadeAr.BOA,
                    timestamp = "2025-11-13T10:00:00Z"
                ),
                posicaoX = 0f,
                posicaoY = 0f,
                largura = 50f,
                altura = 50f,
                forma = FormaEstacao.QUADRADO
            )
        )
    }
}

@Preview(name = "Mesa Colaborativa Ocupada", showBackground = true)
@Composable
fun MesaColaborativaOcupadaPreview() {
    SmartCoworkingTheme {
        RenderizarEstacao(
            estacao = EstacaoDeTrabalho(
                id = "5",
                numero = 5,
                nome = "Mesa Colaborativa",
                tipo = TipoEstacao.MESA,
                capacidade = 4,
                status = StatusEstacao.OCUPADO,
                leituraSensor = LeituraSensor(
                    temperatura = 23.0f,
                    nivelRuido = NivelRuido.MODERADO,
                    qualidadeAr = QualidadeAr.BOA,
                    timestamp = "2025-11-13T10:00:00Z"
                ),
                posicaoX = 0f,
                posicaoY = 0f,
                largura = 70f,
                altura = 70f,
                forma = FormaEstacao.CIRCULO
            )
        )
    }
}

@Preview(name = "Sala Reunião Reservada", showBackground = true)
@Composable
fun SalaReuniaoReservadaPreview() {
    SmartCoworkingTheme {
        RenderizarEstacao(
            estacao = EstacaoDeTrabalho(
                id = "10",
                numero = 10,
                nome = "Sala de Reunião A",
                tipo = TipoEstacao.SALA_REUNIAO,
                capacidade = 8,
                status = StatusEstacao.RESERVADO,
                leituraSensor = LeituraSensor(
                    temperatura = 21.0f,
                    nivelRuido = NivelRuido.SILENCIOSO,
                    qualidadeAr = QualidadeAr.BOA,
                    timestamp = "2025-11-13T10:00:00Z"
                ),
                posicaoX = 0f,
                posicaoY = 0f,
                largura = 100f,
                altura = 60f,
                forma = FormaEstacao.RETANGULO
            )
        )
    }
}

@Preview(name = "Galeria de Estações", showBackground = true, widthDp = 350, heightDp = 200)
@Composable
fun GaleriaEstacoesPreview() {
    SmartCoworkingTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // Mesa livre
                RenderizarEstacao(
                    estacao = EstacaoDeTrabalho(
                        id = "1", numero = 1, nome = "Mesa 1A",
                        tipo = TipoEstacao.MESA, capacidade = 1,
                        status = StatusEstacao.LIVRE,
                        leituraSensor = LeituraSensor(22.5f, NivelRuido.SILENCIOSO, QualidadeAr.BOA, ""),
                        posicaoX = 0f, posicaoY = 0f, largura = 50f, altura = 50f,
                        forma = FormaEstacao.QUADRADO
                    )
                )
                // Mesa ocupada
                RenderizarEstacao(
                    estacao = EstacaoDeTrabalho(
                        id = "2", numero = 2, nome = "Mesa 2A",
                        tipo = TipoEstacao.MESA, capacidade = 1,
                        status = StatusEstacao.OCUPADO,
                        leituraSensor = LeituraSensor(23.0f, NivelRuido.MODERADO, QualidadeAr.BOA, ""),
                        posicaoX = 0f, posicaoY = 0f, largura = 50f, altura = 50f,
                        forma = FormaEstacao.QUADRADO
                    )
                )
                // Mesa reservada
                RenderizarEstacao(
                    estacao = EstacaoDeTrabalho(
                        id = "3", numero = 3, nome = "Mesa 3A",
                        tipo = TipoEstacao.MESA, capacidade = 1,
                        status = StatusEstacao.RESERVADO,
                        leituraSensor = LeituraSensor(22.0f, NivelRuido.SILENCIOSO, QualidadeAr.BOA, ""),
                        posicaoX = 0f, posicaoY = 0f, largura = 50f, altura = 50f,
                        forma = FormaEstacao.QUADRADO
                    )
                )
            }
        }
    }
}
```

**Critérios de Aceitação:**
- ✅ Estação renderiza forma correta baseada em `forma`
- ✅ Cor correta baseada em `status`
- ✅ Padrão visual aplicado para Ocupado/Reservado
- ✅ Número visível e centralizado
- ✅ Preview funciona para todos os tipos

---

### 2.3 Dados Mockados: EstacoesMockData

**Arquivo:** `app/src/main/java/com/example/smartcoworking/data/mock/EstacoesMockData.kt`

```kotlin
package com.example.smartcoworking.data.mock

import com.example.smartcoworking.data.models.*

object EstacoesMockData {

    /**
     * Lista de 15 estações de trabalho com posições hardcoded
     * Layout simulando um coworking real de 1000x1000 unidades
     */
    fun obterEstacoes(): List<EstacaoDeTrabalho> = listOf(
        // Fileira superior - Mesas individuais (1-5)
        EstacaoDeTrabalho(
            id = "1", numero = 1, nome = "Mesa 1A",
            tipo = TipoEstacao.MESA, capacidade = 1,
            status = StatusEstacao.LIVRE,
            leituraSensor = criarLeituraSensor(22.5f, NivelRuido.SILENCIOSO, QualidadeAr.BOA),
            posicaoX = 100f, posicaoY = 100f,
            largura = 50f, altura = 50f,
            forma = FormaEstacao.QUADRADO
        ),
        EstacaoDeTrabalho(
            id = "2", numero = 2, nome = "Mesa 2A",
            tipo = TipoEstacao.MESA, capacidade = 1,
            status = StatusEstacao.OCUPADO,
            leituraSensor = criarLeituraSensor(23.0f, NivelRuido.MODERADO, QualidadeAr.BOA),
            posicaoX = 200f, posicaoY = 100f,
            largura = 50f, altura = 50f,
            forma = FormaEstacao.QUADRADO
        ),
        EstacaoDeTrabalho(
            id = "3", numero = 3, nome = "Mesa 3A",
            tipo = TipoEstacao.MESA, capacidade = 1,
            status = StatusEstacao.RESERVADO,
            leituraSensor = criarLeituraSensor(22.0f, NivelRuido.SILENCIOSO, QualidadeAr.BOA),
            posicaoX = 300f, posicaoY = 100f,
            largura = 50f, altura = 50f,
            forma = FormaEstacao.QUADRADO
        ),
        EstacaoDeTrabalho(
            id = "4", numero = 4, nome = "Mesa 4A",
            tipo = TipoEstacao.MESA, capacidade = 1,
            status = StatusEstacao.LIVRE,
            leituraSensor = criarLeituraSensor(21.5f, NivelRuido.SILENCIOSO, QualidadeAr.BOA),
            posicaoX = 400f, posicaoY = 100f,
            largura = 50f, altura = 50f,
            forma = FormaEstacao.QUADRADO
        ),
        EstacaoDeTrabalho(
            id = "5", numero = 5, nome = "Mesa 5A",
            tipo = TipoEstacao.MESA, capacidade = 1,
            status = StatusEstacao.LIVRE,
            leituraSensor = criarLeituraSensor(22.5f, NivelRuido.SILENCIOSO, QualidadeAr.BOA),
            posicaoX = 500f, posicaoY = 100f,
            largura = 50f, altura = 50f,
            forma = FormaEstacao.QUADRADO
        ),

        // Mesas colaborativas - Círculos (6-8)
        EstacaoDeTrabalho(
            id = "6", numero = 6, nome = "Mesa Colaborativa A",
            tipo = TipoEstacao.MESA, capacidade = 4,
            status = StatusEstacao.OCUPADO,
            leituraSensor = criarLeituraSensor(24.0f, NivelRuido.MODERADO, QualidadeAr.REGULAR),
            posicaoX = 150f, posicaoY = 300f,
            largura = 70f, altura = 70f,
            forma = FormaEstacao.CIRCULO
        ),
        EstacaoDeTrabalho(
            id = "7", numero = 7, nome = "Mesa Colaborativa B",
            tipo = TipoEstacao.MESA, capacidade = 4,
            status = StatusEstacao.LIVRE,
            leituraSensor = criarLeituraSensor(22.5f, NivelRuido.SILENCIOSO, QualidadeAr.BOA),
            posicaoX = 350f, posicaoY = 300f,
            largura = 70f, altura = 70f,
            forma = FormaEstacao.CIRCULO
        ),
        EstacaoDeTrabalho(
            id = "8", numero = 8, nome = "Mesa Colaborativa C",
            tipo = TipoEstacao.MESA, capacidade = 4,
            status = StatusEstacao.RESERVADO,
            leituraSensor = criarLeituraSensor(23.0f, NivelRuido.SILENCIOSO, QualidadeAr.BOA),
            posicaoX = 550f, posicaoY = 300f,
            largura = 70f, altura = 70f,
            forma = FormaEstacao.CIRCULO
        ),

        // Cabines privadas (9-11)
        EstacaoDeTrabalho(
            id = "9", numero = 9, nome = "Cabine Privada 1",
            tipo = TipoEstacao.CABINE_PRIVADA, capacidade = 1,
            status = StatusEstacao.LIVRE,
            leituraSensor = criarLeituraSensor(21.0f, NivelRuido.SILENCIOSO, QualidadeAr.BOA),
            posicaoX = 100f, posicaoY = 500f,
            largura = 80f, altura = 50f,
            forma = FormaEstacao.RETANGULO
        ),
        EstacaoDeTrabalho(
            id = "10", numero = 10, nome = "Cabine Privada 2",
            tipo = TipoEstacao.CABINE_PRIVADA, capacidade = 1,
            status = StatusEstacao.OCUPADO,
            leituraSensor = criarLeituraSensor(22.0f, NivelRuido.SILENCIOSO, QualidadeAr.BOA),
            posicaoX = 200f, posicaoY = 500f,
            largura = 80f, altura = 50f,
            forma = FormaEstacao.RETANGULO
        ),
        EstacaoDeTrabalho(
            id = "11", numero = 11, nome = "Cabine Privada 3",
            tipo = TipoEstacao.CABINE_PRIVADA, capacidade = 1,
            status = StatusEstacao.LIVRE,
            leituraSensor = criarLeituraSensor(21.5f, NivelRuido.SILENCIOSO, QualidadeAr.BOA),
            posicaoX = 300f, posicaoY = 500f,
            largura = 80f, altura = 50f,
            forma = FormaEstacao.RETANGULO
        ),

        // Salas de reunião grandes (12-15)
        EstacaoDeTrabalho(
            id = "12", numero = 12, nome = "Sala de Reunião A",
            tipo = TipoEstacao.SALA_REUNIAO, capacidade = 8,
            status = StatusEstacao.RESERVADO,
            leituraSensor = criarLeituraSensor(21.0f, NivelRuido.SILENCIOSO, QualidadeAr.BOA),
            posicaoX = 100f, posicaoY = 700f,
            largura = 100f, altura = 60f,
            forma = FormaEstacao.RETANGULO
        ),
        EstacaoDeTrabalho(
            id = "13", numero = 13, nome = "Sala de Reunião B",
            tipo = TipoEstacao.SALA_REUNIAO, capacidade = 8,
            status = StatusEstacao.LIVRE,
            leituraSensor = criarLeituraSensor(20.5f, NivelRuido.SILENCIOSO, QualidadeAr.BOA),
            posicaoX = 250f, posicaoY = 700f,
            largura = 100f, altura = 60f,
            forma = FormaEstacao.RETANGULO
        ),
        EstacaoDeTrabalho(
            id = "14", numero = 14, nome = "Sala de Reunião C",
            tipo = TipoEstacao.SALA_REUNIAO, capacidade = 6,
            status = StatusEstacao.LIVRE,
            leituraSensor = criarLeituraSensor(21.5f, NivelRuido.SILENCIOSO, QualidadeAr.BOA),
            posicaoX = 400f, posicaoY = 700f,
            largura = 100f, altura = 60f,
            forma = FormaEstacao.RETANGULO
        ),
        EstacaoDeTrabalho(
            id = "15", numero = 15, nome = "Sala de Reunião D",
            tipo = TipoEstacao.SALA_REUNIAO, capacidade = 10,
            status = StatusEstacao.OCUPADO,
            leituraSensor = criarLeituraSensor(23.5f, NivelRuido.ALTO, QualidadeAr.REGULAR),
            posicaoX = 550f, posicaoY = 700f,
            largura = 100f, altura = 60f,
            forma = FormaEstacao.RETANGULO
        )
    )

    private fun criarLeituraSensor(
        temperatura: Float,
        nivelRuido: NivelRuido,
        qualidadeAr: QualidadeAr
    ) = LeituraSensor(
        temperatura = temperatura,
        nivelRuido = nivelRuido,
        qualidadeAr = qualidadeAr,
        timestamp = "2025-11-13T10:00:00Z"
    )
}
```

**Critérios de Aceitação:**
- ✅ 15 estações definidas
- ✅ Posições não sobrepostas
- ✅ Variedade de formas, tipos e status
- ✅ Coordenadas dentro do canvas 1000x1000

---

## 🗺️ FASE 3: Canvas Completo do Mapa

### Objetivo
Renderizar todas as estações em um único canvas com elementos decorativos.

### 3.1 Componente: MapaCoworkingCanvas

**Arquivo:** `app/src/main/java/com/example/smartcoworking/ui/mapa/MapaCoworkingCanvas.kt`

```kotlin
package com.example.smartcoworking.ui.mapa

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartcoworking.data.models.*
import com.example.smartcoworking.data.mock.EstacoesMockData
import com.example.smartcoworking.ui.mapa.components.*
import com.example.smartcoworking.ui.theme.MapColors
import com.example.smartcoworking.ui.theme.SmartCoworkingTheme

/**
 * Canvas principal do mapa de coworking
 * Renderiza todas as estações e elementos decorativos
 */
@Composable
fun MapaCoworkingCanvas(
    estacoes: List<EstacaoDeTrabalho>,
    modifier: Modifier = Modifier,
    onEstacaoClick: ((EstacaoDeTrabalho) -> Unit)? = null
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(modifier = modifier.fillMaxSize()) {
        val canvasWidth = size.width
        val canvasHeight = size.height

        // Escala para adaptar coordenadas 1000x1000 ao tamanho real
        val scaleX = canvasWidth / 1000f
        val scaleY = canvasHeight / 1000f

        // Desenhar fundo
        drawRect(
            color = Color(0xFFF5F5F5),
            size = size
        )

        // Desenhar área comum central (decorativo)
        drawRect(
            color = MapColors.AreaComum,
            topLeft = Offset(250f * scaleX, 400f * scaleY),
            size = Size(200f * scaleX, 80f * scaleY)
        )
        desenharPadraoHachurado(
            bounds = Size(200f * scaleX, 80f * scaleY),
            color = MapColors.Borda,
            espacamento = 15f * scaleX
        )

        // Labels "JANELA" (decorativo)
        val labelJanelaStyle = TextStyle(
            fontSize = (12 * scaleX).sp,
            color = MapColors.Texto.copy(alpha = 0.5f)
        )

        // Janela esquerda
        val janelaEsqText = textMeasurer.measure("JANELA", labelJanelaStyle)
        drawText(
            textLayoutResult = janelaEsqText,
            topLeft = Offset(20f * scaleX, 300f * scaleY)
        )

        // Janela direita
        val janelaDirText = textMeasurer.measure("JANELA", labelJanelaStyle)
        drawText(
            textLayoutResult = janelaDirText,
            topLeft = Offset(720f * scaleX, 300f * scaleY)
        )

        // Renderizar todas as estações
        estacoes.forEach { estacao ->
            val corStatus = when (estacao.status) {
                StatusEstacao.LIVRE -> MapColors.StatusLivre
                StatusEstacao.OCUPADO -> MapColors.StatusOcupado
                StatusEstacao.RESERVADO -> MapColors.StatusReservado
            }

            val topLeft = Offset(
                estacao.posicaoX * scaleX,
                estacao.posicaoY * scaleY
            )
            val scaledWidth = estacao.largura * scaleX
            val scaledHeight = estacao.altura * scaleY

            // Desenhar forma base
            when (estacao.forma) {
                FormaEstacao.QUADRADO -> {
                    drawRect(
                        color = corStatus,
                        topLeft = topLeft,
                        size = Size(scaledWidth, scaledHeight)
                    )
                    if (estacao.status == StatusEstacao.OCUPADO) {
                        // Aplicar hachurado
                        val offset = topLeft
                        for (i in 0..(scaledWidth / 10).toInt()) {
                            drawLine(
                                color = corStatus.copy(alpha = 0.3f),
                                start = Offset(offset.x + i * 10, offset.y),
                                end = Offset(offset.x + i * 10 + scaledHeight, offset.y + scaledHeight),
                                strokeWidth = 2f
                            )
                        }
                    } else if (estacao.status == StatusEstacao.RESERVADO) {
                        // Aplicar pontilhado
                        val offset = topLeft
                        for (row in 0..(scaledHeight / 15).toInt()) {
                            for (col in 0..(scaledWidth / 15).toInt()) {
                                drawCircle(
                                    color = corStatus.copy(alpha = 0.4f),
                                    radius = 2f,
                                    center = Offset(offset.x + col * 15, offset.y + row * 15)
                                )
                            }
                        }
                    }
                }
                FormaEstacao.CIRCULO -> {
                    val center = Offset(
                        topLeft.x + scaledWidth / 2,
                        topLeft.y + scaledHeight / 2
                    )
                    drawCircle(
                        color = corStatus,
                        radius = scaledWidth / 2,
                        center = center
                    )
                    if (estacao.status == StatusEstacao.OCUPADO) {
                        // Hachurado em círculo (simplificado)
                        for (i in 0..(scaledWidth / 10).toInt()) {
                            drawLine(
                                color = corStatus.copy(alpha = 0.3f),
                                start = Offset(center.x - scaledWidth/2 + i * 10, center.y - scaledHeight/2),
                                end = Offset(center.x - scaledWidth/2 + i * 10 + scaledHeight, center.y + scaledHeight/2),
                                strokeWidth = 2f
                            )
                        }
                    } else if (estacao.status == StatusEstacao.RESERVADO) {
                        // Pontilhado em círculo
                        for (row in 0..(scaledHeight / 15).toInt()) {
                            for (col in 0..(scaledWidth / 15).toInt()) {
                                drawCircle(
                                    color = corStatus.copy(alpha = 0.4f),
                                    radius = 2f,
                                    center = Offset(
                                        center.x - scaledWidth/2 + col * 15,
                                        center.y - scaledHeight/2 + row * 15
                                    )
                                )
                            }
                        }
                    }
                }
                FormaEstacao.RETANGULO -> {
                    drawRect(
                        color = corStatus,
                        topLeft = topLeft,
                        size = Size(scaledWidth, scaledHeight)
                    )
                    if (estacao.status == StatusEstacao.OCUPADO) {
                        val offset = topLeft
                        for (i in 0..(scaledWidth / 10).toInt()) {
                            drawLine(
                                color = corStatus.copy(alpha = 0.3f),
                                start = Offset(offset.x + i * 10, offset.y),
                                end = Offset(offset.x + i * 10 + scaledHeight, offset.y + scaledHeight),
                                strokeWidth = 2f
                            )
                        }
                    } else if (estacao.status == StatusEstacao.RESERVADO) {
                        val offset = topLeft
                        for (row in 0..(scaledHeight / 15).toInt()) {
                            for (col in 0..(scaledWidth / 15).toInt()) {
                                drawCircle(
                                    color = corStatus.copy(alpha = 0.4f),
                                    radius = 2f,
                                    center = Offset(offset.x + col * 15, offset.y + row * 15)
                                )
                            }
                        }
                    }
                }
            }

            // Desenhar número
            val numeroText = textMeasurer.measure(
                text = estacao.numero.toString(),
                style = TextStyle(
                    fontSize = (16 * ((scaleX + scaleY) / 2)).sp,
                    color = Color.White
                )
            )
            val center = when (estacao.forma) {
                FormaEstacao.CIRCULO -> Offset(
                    topLeft.x + scaledWidth / 2,
                    topLeft.y + scaledHeight / 2
                )
                else -> Offset(
                    topLeft.x + scaledWidth / 2,
                    topLeft.y + scaledHeight / 2
                )
            }
            drawText(
                textLayoutResult = numeroText,
                topLeft = Offset(
                    center.x - numeroText.size.width / 2,
                    center.y - numeroText.size.height / 2
                )
            )
        }
    }
}
```

**Preview:**
```kotlin
@Preview(name = "Mapa Completo", showBackground = true, widthDp = 400, heightDp = 400)
@Composable
fun MapaCompletoPreview() {
    SmartCoworkingTheme {
        MapaCoworkingCanvas(
            estacoes = EstacoesMockData.obterEstacoes()
        )
    }
}

@Preview(name = "Mapa Pequeno", showBackground = true, widthDp = 250, heightDp = 250)
@Composable
fun MapaPequenoPreview() {
    SmartCoworkingTheme {
        MapaCoworkingCanvas(
            estacoes = EstacoesMockData.obterEstacoes().take(8)
        )
    }
}
```

**Critérios de Aceitação:**
- ✅ Todas as estações renderizadas
- ✅ Escala responsiva (adapta ao tamanho do canvas)
- ✅ Elementos decorativos visíveis
- ✅ Padrões visuais aplicados corretamente
- ✅ Números legíveis em todas as estações

---

## 🖱️ FASE 4: Interatividade e Detecção de Cliques

### Objetivo
Adicionar detecção de cliques nas estações para navegação.

### 4.1 Adicionar Detecção de Cliques

**Atualizar:** `app/src/main/java/com/example/smartcoworking/ui/mapa/MapaCoworkingCanvas.kt`

```kotlin
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun MapaCoworkingCanvas(
    estacoes: List<EstacaoDeTrabalho>,
    modifier: Modifier = Modifier,
    onEstacaoClick: ((EstacaoDeTrabalho) -> Unit)? = null
) {
    val textMeasurer = rememberTextMeasurer()

    Canvas(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(estacoes) {
                detectTapGestures { offset ->
                    // Detectar qual estação foi clicada
                    val scaleX = size.width / 1000f
                    val scaleY = size.height / 1000f

                    val estacaoClicada = estacoes.find { estacao ->
                        val left = estacao.posicaoX * scaleX
                        val top = estacao.posicaoY * scaleY
                        val right = left + (estacao.largura * scaleX)
                        val bottom = top + (estacao.altura * scaleY)

                        when (estacao.forma) {
                            FormaEstacao.QUADRADO, FormaEstacao.RETANGULO -> {
                                offset.x in left..right && offset.y in top..bottom
                            }
                            FormaEstacao.CIRCULO -> {
                                val centerX = left + (estacao.largura * scaleX) / 2
                                val centerY = top + (estacao.altura * scaleY) / 2
                                val radius = (estacao.largura * scaleX) / 2
                                val dx = offset.x - centerX
                                val dy = offset.y - centerY
                                (dx * dx + dy * dy) <= (radius * radius)
                            }
                        }
                    }

                    estacaoClicada?.let { onEstacaoClick?.invoke(it) }
                }
            }
    ) {
        // ... código de renderização existente ...
    }
}
```

**Preview com Simulação de Clique:**
```kotlin
@Preview(name = "Mapa Interativo", showBackground = true, widthDp = 400, heightDp = 400)
@Composable
fun MapaInterativoPreview() {
    var estacaoSelecionada by remember { mutableStateOf<EstacaoDeTrabalho?>(null) }

    SmartCoworkingTheme {
        Column {
            MapaCoworkingCanvas(
                estacoes = EstacoesMockData.obterEstacoes(),
                modifier = Modifier.weight(1f),
                onEstacaoClick = { estacao ->
                    estacaoSelecionada = estacao
                }
            )

            // Mostrar informações da estação clicada
            estacaoSelecionada?.let { estacao ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Estação Selecionada:", style = MaterialTheme.typography.titleMedium)
                        Text("${estacao.nome} (${estacao.numero})")
                        Text("Status: ${estacao.status}")
                        Text("Temperatura: ${estacao.leituraSensor.temperatura}°C")
                    }
                }
            }
        }
    }
}
```

**Critérios de Aceitação:**
- ✅ Cliques em quadrados detectados corretamente
- ✅ Cliques em círculos detectados corretamente
- ✅ Cliques em retângulos detectados corretamente
- ✅ Callback `onEstacaoClick` é invocado
- ✅ Cliques fora das estações são ignorados

---

## ⏱️ FASE 5: Simulação em Tempo Real

### Objetivo
Implementar atualização periódica dos status e sensores das estações.

### 5.1 ViewModel com Estado do Mapa

**Arquivo:** `app/src/main/java/com/example/smartcoworking/ui/mapa/MapaViewModel.kt`

```kotlin
package com.example.smartcoworking.ui.mapa

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smartcoworking.data.models.*
import com.example.smartcoworking.data.mock.EstacoesMockData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class MapaViewModel : ViewModel() {

    private val _estacoes = MutableStateFlow<List<EstacaoDeTrabalho>>(emptyList())
    val estacoes: StateFlow<List<EstacaoDeTrabalho>> = _estacoes.asStateFlow()

    init {
        carregarEstacoes()
        iniciarSimulacaoTempoReal()
    }

    private fun carregarEstacoes() {
        _estacoes.value = EstacoesMockData.obterEstacoes()
    }

    /**
     * Simula mudanças em tempo real:
     * - Atualiza status de 1-2 estações a cada 10 segundos
     * - Varia temperatura ±2°C
     * - Muda nível de ruído baseado em ocupação
     */
    private fun iniciarSimulacaoTempoReal() {
        viewModelScope.launch {
            while (true) {
                delay(10000) // 10 segundos

                val estacoesAtualizadas = _estacoes.value.toMutableList()

                // Selecionar 1-2 estações aleatórias para atualizar
                val numAtualizacoes = Random.nextInt(1, 3)
                repeat(numAtualizacoes) {
                    val indiceAleatorio = Random.nextInt(estacoesAtualizadas.size)
                    val estacao = estacoesAtualizadas[indiceAleatorio]

                    // Alternar status aleatoriamente
                    val novoStatus = when (Random.nextInt(3)) {
                        0 -> StatusEstacao.LIVRE
                        1 -> StatusEstacao.OCUPADO
                        else -> StatusEstacao.RESERVADO
                    }

                    // Variar temperatura
                    val novaTemperatura = estacao.leituraSensor.temperatura + Random.nextFloat() * 4 - 2

                    // Nível de ruído baseado em ocupação
                    val novoRuido = when (novoStatus) {
                        StatusEstacao.LIVRE -> NivelRuido.SILENCIOSO
                        StatusEstacao.OCUPADO -> if (Random.nextBoolean()) NivelRuido.MODERADO else NivelRuido.ALTO
                        StatusEstacao.RESERVADO -> NivelRuido.SILENCIOSO
                    }

                    // Atualizar estação
                    estacoesAtualizadas[indiceAleatorio] = estacao.copy(
                        status = novoStatus,
                        leituraSensor = estacao.leituraSensor.copy(
                            temperatura = novaTemperatura.coerceIn(18f, 28f),
                            nivelRuido = novoRuido,
                            qualidadeAr = if (Random.nextFloat() > 0.8) QualidadeAr.REGULAR else QualidadeAr.BOA,
                            timestamp = System.currentTimeMillis().toString()
                        )
                    )
                }

                _estacoes.value = estacoesAtualizadas
            }
        }
    }
}
```

**Critérios de Aceitação:**
- ✅ ViewModel inicializa com dados mockados
- ✅ Atualização periódica a cada 10 segundos
- ✅ Status muda realisticamente
- ✅ Sensores variam de forma coerente
- ✅ StateFlow emite atualizações

---

### 5.2 Tela do Mapa com ViewModel

**Arquivo:** `app/src/main/java/com/example/smartcoworking/ui/mapa/MapaCoworkingScreen.kt`

```kotlin
package com.example.smartcoworking.ui.mapa

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.smartcoworking.data.models.EstacaoDeTrabalho
import com.example.smartcoworking.ui.theme.SmartCoworkingTheme

@Composable
fun MapaCoworkingScreen(
    viewModel: MapaViewModel = viewModel(),
    onEstacaoClick: (EstacaoDeTrabalho) -> Unit
) {
    val estacoes by viewModel.estacoes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mapa de Estações") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Mostrar legenda */ }
            ) {
                Text("?")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (estacoes.isEmpty()) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                MapaCoworkingCanvas(
                    estacoes = estacoes,
                    onEstacaoClick = onEstacaoClick
                )
            }
        }
    }
}
```

**Preview:**
```kotlin
@Preview(name = "Tela Mapa Completa", showBackground = true)
@Composable
fun MapaCoworkingScreenPreview() {
    SmartCoworkingTheme {
        MapaCoworkingScreen(
            onEstacaoClick = { /* Preview não navega */ }
        )
    }
}
```

**Critérios de Aceitação:**
- ✅ Integração com ViewModel funciona
- ✅ Estado reativo (UI atualiza automaticamente)
- ✅ Loading state enquanto carrega dados
- ✅ TopAppBar e FAB presentes

---

## ✨ FASE 6: Polimento e Acessórios

### Objetivo
Adicionar legenda, animações e toques finais.

### 6.1 Dialog da Legenda

**Arquivo:** `app/src/main/java/com/example/smartcoworking/ui/mapa/components/LegendaDialog.kt`

```kotlin
package com.example.smartcoworking.ui.mapa.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartcoworking.ui.theme.MapColors
import com.example.smartcoworking.ui.theme.SmartCoworkingTheme

@Composable
fun LegendaDialog(
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Legenda do Mapa")
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Status das Estações:", style = MaterialTheme.typography.titleSmall)

                // Livre
                ItemLegenda(
                    cor = MapColors.StatusLivre,
                    titulo = "Livre",
                    descricao = "Disponível para reserva",
                    comPadrao = false
                )

                // Ocupado
                ItemLegenda(
                    cor = MapColors.StatusOcupado,
                    titulo = "Ocupado",
                    descricao = "Atualmente em uso",
                    comPadrao = true,
                    tipoPatrao = "hachurado"
                )

                // Reservado
                ItemLegenda(
                    cor = MapColors.StatusReservado,
                    titulo = "Reservado",
                    descricao = "Reservado para horário futuro",
                    comPadrao = true,
                    tipoPatrao = "pontilhado"
                )

                Divider(modifier = Modifier.padding(vertical = 8.dp))

                Text("Tipos de Estação:", style = MaterialTheme.typography.titleSmall)

                // Formas
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Canvas(modifier = Modifier.size(30.dp)) {
                            drawRect(
                                color = Color.Gray,
                                size = Size(size.width, size.height)
                            )
                        }
                        Text("Mesa", style = MaterialTheme.typography.bodySmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Canvas(modifier = Modifier.size(30.dp)) {
                            drawCircle(
                                color = Color.Gray,
                                radius = size.width / 2,
                                center = Offset(size.width / 2, size.height / 2)
                            )
                        }
                        Text("Mesa Colab.", style = MaterialTheme.typography.bodySmall)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Canvas(modifier = Modifier.size(width = 40.dp, height = 25.dp)) {
                            drawRect(
                                color = Color.Gray,
                                size = Size(size.width, size.height)
                            )
                        }
                        Text("Sala", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Entendi")
            }
        }
    )
}

@Composable
private fun ItemLegenda(
    cor: Color,
    titulo: String,
    descricao: String,
    comPadrao: Boolean,
    tipoPatrao: String = ""
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Canvas(modifier = Modifier.size(40.dp)) {
            drawRect(color = cor, size = size)
            if (comPadrao) {
                if (tipoPatrao == "hachurado") {
                    desenharPadraoHachurado(size, cor)
                } else if (tipoPatrao == "pontilhado") {
                    desenharPadraoPontilhado(size, cor)
                }
            }
        }
        Column {
            Text(titulo, style = MaterialTheme.typography.bodyMedium)
            Text(descricao, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
        }
    }
}
```

**Preview:**
```kotlin
@Preview(showBackground = true)
@Composable
fun LegendaDialogPreview() {
    SmartCoworkingTheme {
        LegendaDialog(onDismiss = {})
    }
}
```

**Integrar no MapaCoworkingScreen:**
```kotlin
@Composable
fun MapaCoworkingScreen(
    viewModel: MapaViewModel = viewModel(),
    onEstacaoClick: (EstacaoDeTrabalho) -> Unit
) {
    val estacoes by viewModel.estacoes.collectAsState()
    var mostrarLegenda by remember { mutableStateOf(false) }

    Scaffold(
        // ... código existente ...
        floatingActionButton = {
            FloatingActionButton(
                onClick = { mostrarLegenda = true }
            ) {
                Text("?")
            }
        }
    ) { paddingValues ->
        // ... código existente ...

        if (mostrarLegenda) {
            LegendaDialog(onDismiss = { mostrarLegenda = false })
        }
    }
}
```

**Critérios de Aceitação:**
- ✅ Dialog mostra todos os status
- ✅ Padrões visuais exibidos
- ✅ Tipos de formas explicados
- ✅ FAB abre o dialog

---

## 📋 Checklist Final de Implementação

### Fase 1: Componentes Atômicos
- [ ] MapColors.kt criado e testado
- [ ] FormasBasicas.kt com quadrado, círculo e retângulo
- [ ] PadroesVisuais.kt com hachurado e pontilhado
- [ ] EstacaoLabel.kt com números centralizados
- [ ] Todos os Previews funcionando

### Fase 2: Componentes de Estação
- [ ] Modelos de dados criados (EstacaoDeTrabalho, enums)
- [ ] EstacaoCanvas.kt renderiza estação completa
- [ ] EstacoesMockData.kt com 15 estações
- [ ] Previews de todos os tipos de estação

### Fase 3: Canvas Completo
- [ ] MapaCoworkingCanvas.kt renderiza todas estações
- [ ] Sistema de escala responsivo implementado
- [ ] Elementos decorativos adicionados
- [ ] Preview do mapa completo funciona

### Fase 4: Interatividade
- [ ] Detecção de cliques implementada
- [ ] Hit test para quadrados funciona
- [ ] Hit test para círculos funciona
- [ ] Hit test para retângulos funciona
- [ ] Callback onEstacaoClick funcional

### Fase 5: Tempo Real
- [ ] MapaViewModel criado
- [ ] Simulação periódica implementada
- [ ] StateFlow emitindo atualizações
- [ ] MapaCoworkingScreen integrado
- [ ] UI reage a mudanças de estado

### Fase 6: Polimento
- [ ] LegendaDialog criado e funcional
- [ ] FAB abre legenda
- [ ] TopAppBar estilizado
- [ ] Loading states implementados
- [ ] Acessibilidade verificada

---

## 🎓 Recursos e Documentação

### Jetpack Compose Canvas
- [Documentação Oficial - Canvas](https://developer.android.com/jetpack/compose/graphics/draw/overview)
- [Guia de Desenho - DrawScope](https://developer.android.com/reference/kotlin/androidx/compose/ui/graphics/drawscope/DrawScope)
- [Gestures - detectTapGestures](https://developer.android.com/jetpack/compose/touch-input/pointer-input/tap-and-press)

### Preview e Tooling
- [Previews no Compose](https://developer.android.com/develop/ui/compose/tooling/previews?hl=pt-br)
- [Preview Annotations](https://developer.android.com/reference/kotlin/androidx/compose/ui/tooling/preview/Preview)

### State Management
- [State e Jetpack Compose](https://developer.android.com/jetpack/compose/state)
- [StateFlow e collectAsState](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow)

### Exemplos de Código
- [Compose Samples - GitHub](https://github.com/android/compose-samples)

---

## 🚀 Próximos Passos Após o Mapa

Uma vez que o mapa estiver completo, você poderá:

1. **Criar Tela de Detalhes da Estação**
   - Exibir informações completas
   - Mostrar dados de sensores em tempo real
   - Botão para reservar

2. **Implementar Navegação**
   - Adicionar Jetpack Navigation Compose
   - Configurar rotas (Login → Mapa → Detalhes → Reserva)

3. **Criar Tela de Login**
   - Autenticação com hash de senha
   - Validação de inputs

4. **Criar Tela de Reserva**
   - Formulário com validações
   - Date/Time pickers
   - Verificação de conflitos

5. **Implementar Testes**
   - Testes unitários para ViewModels
   - Testes de UI com Compose Testing

---

**Boa sorte com o desenvolvimento! Este roadmap foi projetado para ser seguido passo a passo, com cada fase construindo sobre a anterior. Use os Previews extensivamente para validar cada componente antes de prosseguir.**
