package com.example.smartcoworking.data.models

// ============================================================================
// ENUMS - TIPOS E ESTADOS
// ============================================================================

/**
 * Tipo de estação de trabalho no coworking
 */
enum class TipoEstacao {
    /** Mesa individual para uma pessoa */
    MESA,

    /** Sala de reunião para grupos */
    SALA_REUNIAO,


}

/**
 * Status de disponibilidade da estação
 */
enum class StatusEstacao {
    /** Disponível para reserva - Verde sólido */
    LIVRE,

    /** Atualmente em uso - Vermelho com hachuras */
    OCUPADO,

    /** Reservado para uso futuro - Amarelo com pontos */
    RESERVADO
}

/**
 * Forma geométrica da estação no mapa
 */
enum class FormaEstacao {
    /** Quadrado - Mesas individuais */
    QUADRADO,

    /** Círculo - Mesas colaborativas */
    CIRCULO,

    /** Retângulo - Salas e cabines */
    RETANGULO
}

/**
 * Nível de ruído ambiente
 */
enum class NivelRuido {
    /** Abaixo de 40 dB - Ambiente silencioso */
    SILENCIOSO,

    /** 40-60 dB - Conversas normais */
    MODERADO,

    /** Acima de 60 dB - Ambiente barulhento */
    ALTO
}

/**
 * Qualidade do ar ambiente
 */
enum class QualidadeAr {
    /** CO2 < 800 ppm - Ar fresco */
    BOA,

    /** CO2 800-1200 ppm - Ar aceitável */
    REGULAR,

    /** CO2 > 1200 ppm - Ar necessita renovação */
    RUIM
}

// ============================================================================
// DATA CLASSES - ESTRUTURAS DE DADOS
// ============================================================================

/**
 * Posição de um elemento no canvas do mapa
 *
 * @param x Coordenada horizontal (0-1200px)
 * @param y Coordenada vertical (0-800px)
 */
data class PosicaoCanvas(
    val x: Float,
    val y: Float
)

/**
 * Dimensões de um elemento no canvas
 *
 * @param largura Largura em pixels
 * @param altura Altura em pixels
 */
data class DimensoesCanvas(
    val largura: Float,
    val altura: Float
)

/**
 * Leitura dos sensores IoT de uma estação
 *
 * @param temperatura Temperatura ambiente em graus Celsius
 * @param nivelRuido Nível de ruído classificado
 * @param qualidadeAr Qualidade do ar classificada
 * @param timestamp Momento da leitura em formato ISO 8601
 */
data class LeituraSensor(
    val temperatura: Float,
    val nivelRuido: NivelRuido,
    val qualidadeAr: QualidadeAr,
    val timestamp: String
)

/**
 * Estação de trabalho completa com todas as informações
 *
 * Representa uma mesa, sala ou cabine no espaço de coworking,
 * incluindo suas características físicas, status atual e dados dos sensores.
 *
 * @param id Identificador único (ex: "EST-001")
 * @param numero Número exibido no mapa (1, 2, 3...)
 * @param nome Nome descritivo (ex: "Mesa 10A", "Sala Reunião 1")
 * @param tipo Tipo da estação (mesa, sala, cabine)
 * @param capacidade Número de pessoas que a estação comporta
 * @param status Status atual de disponibilidade
 * @param leituraSensor Dados dos sensores IoT
 * @param posicao Coordenadas no canvas do mapa
 * @param dimensoes Tamanho da estação no canvas
 * @param forma Forma geométrica para renderização
 */
data class EstacaoDeTrabalho(
    // Identificação
    val id: String,
    val numero: Int,
    val nome: String,
    val tipo: TipoEstacao,

    // Características
    val capacidade: Int,
    val status: StatusEstacao,

    // Sensores IoT
    val leituraSensor: LeituraSensor,

    // Renderização no Canvas
    val posicao: PosicaoCanvas,
    val dimensoes: DimensoesCanvas,
    val forma: FormaEstacao,

    // Detalhes adicionais
    val comodidades: List<String> = emptyList()
)

/**
 * Área especial não reservável (ex: área de descanso, lounge)
 *
 * @param id Identificador único (ex: "AREA-001")
 * @param label Texto exibido na área
 * @param posicao Coordenadas no canvas do mapa
 * @param dimensoes Tamanho da área no canvas
 */
data class AreaEspecial(
    val id: String,
    val label: String,
    val posicao: PosicaoCanvas,
    val dimensoes: DimensoesCanvas
)
