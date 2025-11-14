package com.example.smartcoworking.data

import com.example.smartcoworking.data.models.DimensoesCanvas
import com.example.smartcoworking.data.models.PosicaoCanvas

// ============================================================================
// CONFIGURAÇÕES DO CANVAS
// ============================================================================

/**
 * Configurações globais do canvas do mapa de coworking
 *
 * Define as dimensões fixas do canvas (1500x900px) sem responsividade.
 * Scroll horizontal e zoom serão implementados futuramente.
 */
object CanvasConfig {
    /** Largura total do canvas em pixels */
    const val LARGURA = 1500f

    /** Altura total do canvas em pixels */
    const val ALTURA = 900f

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
 * Valores baseados no design do Figma (1500x900px)
 *
 * AJUSTE AQUI para alterar os tamanhos de todas as estações globalmente
 */
object TamanhosEstacao {
    /** Tamanho do lado das mesas individuais quadradas (90x90 no Figma) */
    const val INDIVIDUAL_SIZE = 90f

    /** Diâmetro das mesas colaborativas circulares (150x150 no Figma) */
    const val COLABORATIVA_DIAMETER = 150f

    /** Dimensões padrão das salas retangulares (largura x altura) */
    val SALA_SIZE = DimensoesCanvas(204f, 300f)

    /** Dimensões da área de descanso central (720,150 - 210x570 no Figma) */
    val AREA_DESCANSO = DimensoesCanvas(300f, 700f)
}

// ============================================================================
// POSICIONAMENTO DAS ESTAÇÕES
// ============================================================================

/**
 * Posicionamento de todas as estações no mapa
 * Valores baseados no design do Figma (1500x900px)
 *
 * AJUSTE AQUI para alterar as posições de todas as estações globalmente
 */
object PosicionamentoEstacoes {

    // ZONA 1: Parede Esquerda (5 mesas individuais verticais)
    object ParedeEsquerda {
        const val X = 30f
        val POSICOES_Y = listOf(180f, 300f, 420f, 540f, 660f)
    }

    // ZONA 2: Mesas Colaborativas (4 círculos)
    object MesasColaborativas {
        val POSICOES = listOf(
            Pair(240f, 180f),
            Pair(450f, 300f),
            Pair(240f, 425f),
            Pair(450f, 540f)
        )
    }

    // ZONA 3: Topo (4 mesas individuais horizontais)
    object Topo {
        const val Y = 30f
        val POSICOES_X = listOf(180f, 300f, 420f, 540f)
    }

    // ZONA 4: Fundo (4 mesas individuais horizontais)
    object Fundo {
        const val Y = 780f
        val POSICOES_X = listOf(180f, 300f, 420f, 540f)
    }

    // ZONA 5: Coluna Centro-Direita (5 mesas individuais verticais)
    object ColunaDireita {
        const val X = 1050f
        val POSICOES_Y = listOf(150f, 270f, 390f, 510f, 630f)
    }

    // ZONA 6: Salas de Reunião (3 retângulos)
    object SalasReuniao {
        const val X = 1260f
        // Triple(Y, Largura, Altura)
        val DADOS = listOf(
            Triple(63f, 204f, 229f),
            Triple(305f, 204f, 230f),
            Triple(547f, 204f, 230f)
        )
    }

    // Área Especial: Área de Descanso
    object AreaDescanso {
        const val X = 720f
        const val Y = 80f
        // Dimensões vêm de TamanhosEstacao.AREA_DESCANSO
    }
}

// ============================================================================
// REFERÊNCIAS ESPACIAIS PARA SENSORES
// ============================================================================

/**
 * Posições de referência para cálculo dos dados dos sensores.
 * Janelas nas laterais afetam temperatura, área de descanso afeta ruído.
 * Valores baseados no layout do Figma (1500x900px)
 */
object ReferenciasEspaciais {
    /**
     * Posições das janelas (laterais do prédio)
     * Estações próximas tendem a ter temperatura maior devido ao sol
     */
    val JANELAS = listOf(
        PosicaoCanvas(0f, 450f),      // Janela esquerda (meio da altura)
        PosicaoCanvas(1500f, 450f)    // Janela direita (meio da altura)
    )

    /**
     * Centro da área de descanso (calculado dinamicamente)
     * Estações próximas tendem a ter maior nível de ruído
     */
    val CENTRO_AREA_DESCANSO = PosicaoCanvas(
        PosicionamentoEstacoes.AreaDescanso.X + TamanhosEstacao.AREA_DESCANSO.largura / 2,
        PosicionamentoEstacoes.AreaDescanso.Y + TamanhosEstacao.AREA_DESCANSO.altura / 2
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
