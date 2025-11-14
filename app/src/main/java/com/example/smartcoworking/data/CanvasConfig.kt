package com.example.smartcoworking.data

import com.example.smartcoworking.data.models.DimensoesCanvas
import com.example.smartcoworking.data.models.PosicaoCanvas

// ============================================================================
// CONFIGURAÇÕES DO CANVAS
// ============================================================================

/**
 * Configurações globais do canvas do mapa de coworking
 *
 * Define as dimensões fixas do canvas (1200x800px) sem responsividade.
 * Scroll horizontal será implementado futuramente.
 */
object CanvasConfig {
    /** Largura total do canvas em pixels */
    const val LARGURA = 1200f

    /** Altura total do canvas em pixels */
    const val ALTURA = 800f

    /** Margem lateral (esquerda/direita) em pixels */
    const val MARGEM_LATERAL = 50f

    /** Margem superior em pixels */
    const val MARGEM_TOPO = 50f

    /** Margem inferior (para entrada) em pixels */
    const val MARGEM_FUNDO = 80f

    /** Espaçamento padrão para corredores entre zonas */
    const val CORREDOR = 100f
}

// ============================================================================
// TAMANHOS PADRÃO DAS ESTAÇÕES
// ============================================================================

/**
 * Dimensões padrão de cada tipo de estação
 */
object TamanhosEstacao {
    /** Tamanho do lado das mesas individuais quadradas */
    const val INDIVIDUAL_SIZE = 80f

    /** Diâmetro das mesas colaborativas circulares */
    const val COLABORATIVA_DIAMETER = 110f

    /** Dimensões das salas retangulares (largura x altura) */
    val SALA_SIZE = DimensoesCanvas(120f, 160f)

    /** Dimensões da área de descanso central - Grande retângulo vertical */
    val AREA_DESCANSO = DimensoesCanvas(280f, 550f)
}

// ============================================================================
// ZONAS DO LAYOUT
// ============================================================================

/**
 * Posições X fixas para cada zona vertical do layout
 */
object ZonasX {
    /** Parede esquerda - Estações individuais (quase grudadas na parede) */
    const val PAREDE_ESQUERDA = 20f

    /** Centro-esquerda - Mesas colaborativas (coluna 1) */
    const val COLABORATIVAS_COL1 = 200f

    /** Centro-esquerda - Mesas colaborativas (coluna 2) */
    const val COLABORATIVAS_COL2 = 330f

    /** Centro - Início da área de descanso */
    const val AREA_DESCANSO_X = 480f

    /** Centro-direita - Estações individuais coluna central (à direita da área de descanso) */
    const val COLUNA_CENTRAL = 800f

    /** Parede direita - Salas de reunião (mais próximas) */
    const val PAREDE_DIREITA = 1000f
}

/**
 * Posições Y comuns usadas no layout
 */
object ZonasY {
    /** Linha de estações na parte inferior (perto da entrada) */
    const val PAREDE_INFERIOR = 700f

    /** Centro vertical da área de descanso (centralizada na altura do canvas) */
    const val AREA_DESCANSO_Y = 125f
}

// ============================================================================
// REFERÊNCIAS ESPACIAIS PARA SENSORES
// ============================================================================

/**
 * Posições de referência para cálculo dos dados dos sensores.
 * Janelas nas laterais afetam temperatura, área de descanso afeta ruído.
 */
object ReferenciasEspaciais {
    /**
     * Posições das janelas (laterais do prédio)
     * Estações próximas tendem a ter temperatura maior devido ao sol
     */
    val JANELAS = listOf(
        PosicaoCanvas(0f, CanvasConfig.ALTURA / 2),      // Janela esquerda
        PosicaoCanvas(CanvasConfig.LARGURA, CanvasConfig.ALTURA / 2)  // Janela direita
    )

    /**
     * Centro da área de descanso
     * Estações próximas tendem a ter maior nível de ruído
     */
    val CENTRO_AREA_DESCANSO = PosicaoCanvas(
        ZonasX.AREA_DESCANSO_X + TamanhosEstacao.AREA_DESCANSO.largura / 2,
        ZonasY.AREA_DESCANSO_Y + TamanhosEstacao.AREA_DESCANSO.altura / 2
    )

    /**
     * Temperaturas de referência
     */
    object Temperatura {
        /** Temperatura base no centro do ambiente (longe das janelas) */
        const val BASE = 22f

        /** Variação máxima perto das janelas (±2°C) */
        const val VARIACAO_JANELA = 2f

        /** Distância de influência das janelas (pixels) */
        const val RAIO_INFLUENCIA_JANELA = 300f
    }

    /**
     * Configurações de ruído
     */
    object Ruido {
        /** Distância de influência da área de descanso (pixels) */
        const val RAIO_INFLUENCIA_DESCANSO = 250f
    }
}

// ============================================================================
// DISTRIBUIÇÃO DE STATUS
// ============================================================================

/**
 * Percentuais para distribuição inicial dos status das estações
 */
object DistribuicaoStatus {
    /** Percentual de estações livres (~60%) */
    const val PERCENTUAL_LIVRE = 0.6f

    /** Percentual de estações ocupadas (~20%) */
    const val PERCENTUAL_OCUPADO = 0.2f

    /** Percentual de estações reservadas (~20%) */
    const val PERCENTUAL_RESERVADO = 0.2f
}
