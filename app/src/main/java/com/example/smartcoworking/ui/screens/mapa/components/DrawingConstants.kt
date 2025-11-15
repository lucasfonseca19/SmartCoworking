package com.example.smartcoworking.ui.screens.mapa.components

import androidx.compose.ui.graphics.Color

/**
 * Constantes para renderização de formas e padrões no mapa de coworking.
 *
 * Este objeto centraliza todos os "magic numbers" usados na renderização,
 * facilitando manutenção e ajustes globais.
 */
object DrawingConstants {
    // ========================================================================
    // GRADIENTE E SOMBREAMENTO
    // ========================================================================

    /**
     * Fator de escurecimento para criar gradiente de profundidade.
     * Valor de 0.9f significa que a cor no topo será 10% mais escura.
     */
    const val GRADIENT_DARKEN_FACTOR = 0.9f

    // ========================================================================
    // BORDAS
    // ========================================================================

    /**
     * Opacidade padrão da borda das estações.
     * Valor de 0.2f cria uma borda sutil que não compete com a cor principal.
     */
    const val BORDER_ALPHA = 0.2f

    /**
     * Cor padrão das bordas.
     */
    val BORDER_COLOR = Color.Black.copy(alpha = BORDER_ALPHA)

    // ========================================================================
    // ESCALA E LAYOUT
    // ========================================================================

    /**
     * Fator de escala do conteúdo dentro do canvas.
     * Valor de 0.85f deixa 15% de margem ao redor do conteúdo.
     */
    const val CONTENT_SCALE_FACTOR = 0.85f

    /**
     * Divisor para calcular tamanho da fonte baseado no tamanho da forma.
     * Valor de 5.5f garante que o número da estação caiba confortavelmente.
     */
    const val FONT_SIZE_DIVISOR = 5.5f

    /**
     * Tamanho mínimo de fonte para garantir legibilidade em formas pequenas.
     */
    const val MIN_FONT_SIZE = 12f

    // ========================================================================
    // PADRÕES VISUAIS (ACESSIBILIDADE)
    // ========================================================================

    /**
     * Espaçamento entre linhas do padrão hachurado (para status OCUPADO).
     * Valor em pixels antes de aplicar escala.
     */
    const val PATTERN_HATCHING_SPACING = 10f

    /**
     * Espessura das linhas do padrão hachurado.
     */
    const val PATTERN_HATCHING_STROKE = 1.5f

    /**
     * Espaçamento entre pontos do padrão pontilhado (para status RESERVADO).
     * Valor em pixels antes de aplicar escala.
     */
    const val PATTERN_DOTTED_SPACING = 8f

    /**
     * Raio dos pontos do padrão pontilhado.
     */
    const val PATTERN_DOTTED_RADIUS = 2f

    /**
     * Opacidade padrão dos padrões visuais.
     * Valor baixo (0.15f) garante que o padrão seja visível mas não dominante.
     */
    const val PATTERN_OPACITY = 0.15f

    // ========================================================================
    // DIMENSÕES PADRÃO
    // ========================================================================

    /**
     * Raio dos cantos arredondados em dp (para conversão para px).
     */
    const val DEFAULT_CORNER_RADIUS_DP = 8f

    /**
     * Espessura padrão da borda em dp.
     */
    const val DEFAULT_BORDER_WIDTH_DP = 1f
}
