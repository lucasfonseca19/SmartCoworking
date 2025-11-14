package com.example.smartcoworking.data

import com.example.smartcoworking.data.models.*
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

// ============================================================================
// FACTORY DE DADOS MOCKADOS
// ============================================================================

/**
 * Factory para criar dados mockados de estações de trabalho e áreas especiais.
 *
 * Gera 25 estações distribuídas pelo layout do coworking, com sensores
 * calculados baseados na posição espacial (proximidade de janelas, área de descanso).
 */
object EstacoesMockData {

    /**
     * Retorna todas as estações de trabalho mockadas (26 estações)
     *
     * Distribuição:
     * - 5 individuais na parede esquerda
     * - 5 colaborativas no centro-esquerda
     * - 5 individuais na parede inferior (deslocadas para direita - porta)
     * - 6 individuais na coluna central (à direita da área de descanso)
     * - 4 salas na parede direita
     * - 1 área de descanso central (retornada separadamente)
     *
     * Status: ~60% livre, ~20% ocupado, ~20% reservado
     */
    fun obterEstacoes(): List<EstacaoDeTrabalho> {
        var numeroSequencial = 1
        val estacoes = mutableListOf<EstacaoDeTrabalho>()

        // ZONA 1: Parede Esquerda - 5 Estações Individuais
        val yPosicoesParedeEsquerda = listOf(80f, 190f, 300f, 410f, 520f)
        yPosicoesParedeEsquerda.forEach { y ->
            estacoes.add(
                criarEstacaoIndividual(
                    numero = numeroSequencial++,
                    x = ZonasX.PAREDE_ESQUERDA,
                    y = y
                )
            )
        }

        // ZONA 2: Centro-Esquerda - 5 Mesas Colaborativas (arranjo colmeia)
        estacoes.add(
            criarEstacaoColaborativa(
                numero = numeroSequencial++,
                x = ZonasX.COLABORATIVAS_COL1,
                y = 130f
            )
        )
        estacoes.add(
            criarEstacaoColaborativa(
                numero = numeroSequencial++,
                x = ZonasX.COLABORATIVAS_COL1,
                y = 280f
            )
        )
        estacoes.add(
            criarEstacaoColaborativa(
                numero = numeroSequencial++,
                x = ZonasX.COLABORATIVAS_COL1,
                y = 430f
            )
        )
        estacoes.add(
            criarEstacaoColaborativa(
                numero = numeroSequencial++,
                x = ZonasX.COLABORATIVAS_COL2,
                y = 205f
            )
        )
        estacoes.add(
            criarEstacaoColaborativa(
                numero = numeroSequencial++,
                x = ZonasX.COLABORATIVAS_COL2,
                y = 355f
            )
        )

        // ZONA 3: Parede Inferior - 5 Estações Individuais (deslocadas para direita - porta à esquerda)
        val xPosicoesParedeInferior = listOf(200f, 310f, 420f, 530f, 640f)
        xPosicoesParedeInferior.forEach { x ->
            estacoes.add(
                criarEstacaoIndividual(
                    numero = numeroSequencial++,
                    x = x,
                    y = ZonasY.PAREDE_INFERIOR
                )
            )
        }

        // ZONA 4: Coluna Central - 6 Estações Individuais (sem gap)
        val yPosicoesColunaCentral = listOf(80f, 190f, 300f, 410f, 520f, 630f)
        yPosicoesColunaCentral.forEach { y ->
            estacoes.add(
                criarEstacaoIndividual(
                    numero = numeroSequencial++,
                    x = ZonasX.COLUNA_CENTRAL,
                    y = y
                )
            )
        }

        // ZONA 5: Parede Direita - 4 Salas de Reunião (ajustadas para novo tamanho)
        val yPosicoesSalas = listOf(80f, 270f, 460f, 650f)
        yPosicoesSalas.forEach { y ->
            estacoes.add(
                criarSala(
                    numero = numeroSequencial++,
                    x = ZonasX.PAREDE_DIREITA,
                    y = y
                )
            )
        }

        // Aplicar distribuição de status (~60% livre, 20% ocupado, 20% reservado)
        return aplicarDistribuicaoStatus(estacoes)
    }

    /**
     * Retorna áreas especiais não reserváveis
     */
    fun obterAreasEspeciais(): List<AreaEspecial> = listOf(
        AreaEspecial(
            id = "AREA-001",
            label = "Área de Descanso",
            posicao = PosicaoCanvas(
                x = ZonasX.AREA_DESCANSO_X,
                y = ZonasY.AREA_DESCANSO_Y
            ),
            dimensoes = TamanhosEstacao.AREA_DESCANSO
        )
    )

    // ========================================================================
    // FUNÇÕES AUXILIARES - CRIAÇÃO DE ESTAÇÕES
    // ========================================================================

    /**
     * Cria uma estação individual (mesa quadrada)
     */
    private fun criarEstacaoIndividual(
        numero: Int,
        x: Float,
        y: Float
    ): EstacaoDeTrabalho {
        val posicao = PosicaoCanvas(x, y)

        return EstacaoDeTrabalho(
            id = "EST-${numero.toString().padStart(3, '0')}",
            numero = numero,
            nome = "Mesa ${numero}A",
            tipo = TipoEstacao.MESA,
            capacidade = 1,
            status = StatusEstacao.LIVRE, // Será redistribuído depois
            leituraSensor = gerarLeituraSensor(posicao),
            posicao = posicao,
            dimensoes = DimensoesCanvas(
                TamanhosEstacao.INDIVIDUAL_SIZE,
                TamanhosEstacao.INDIVIDUAL_SIZE
            ),
            forma = FormaEstacao.QUADRADO
        )
    }

    /**
     * Cria uma estação colaborativa (mesa circular)
     */
    private fun criarEstacaoColaborativa(
        numero: Int,
        x: Float,
        y: Float
    ): EstacaoDeTrabalho {
        val posicao = PosicaoCanvas(x, y)

        return EstacaoDeTrabalho(
            id = "EST-${numero.toString().padStart(3, '0')}",
            numero = numero,
            nome = "Mesa Colaborativa ${numero}",
            tipo = TipoEstacao.MESA,
            capacidade = 4,
            status = StatusEstacao.LIVRE, // Será redistribuído depois
            leituraSensor = gerarLeituraSensor(posicao),
            posicao = posicao,
            dimensoes = DimensoesCanvas(
                TamanhosEstacao.COLABORATIVA_DIAMETER,
                TamanhosEstacao.COLABORATIVA_DIAMETER
            ),
            forma = FormaEstacao.CIRCULO
        )
    }

    /**
     * Cria uma sala de reunião (retangular)
     */
    private fun criarSala(
        numero: Int,
        x: Float,
        y: Float
    ): EstacaoDeTrabalho {
        val posicao = PosicaoCanvas(x, y)

        return EstacaoDeTrabalho(
            id = "SALA-${numero.toString().padStart(3, '0')}",
            numero = numero,
            nome = "Sala de Reunião ${numero}",
            tipo = TipoEstacao.SALA_REUNIAO,
            capacidade = 6,
            status = StatusEstacao.LIVRE, // Será redistribuído depois
            leituraSensor = gerarLeituraSensor(posicao),
            posicao = posicao,
            dimensoes = TamanhosEstacao.SALA_SIZE,
            forma = FormaEstacao.RETANGULO
        )
    }

    // ========================================================================
    // FUNÇÕES AUXILIARES - SENSORES
    // ========================================================================

    /**
     * Gera leitura de sensores baseada na posição espacial da estação
     *
     * Lógica:
     * - Temperatura: maior perto das janelas (sol), menor no centro
     * - Ruído: maior perto da área de descanso, menor nas extremidades
     * - Qualidade do ar: variação leve aleatória
     */
    private fun gerarLeituraSensor(posicao: PosicaoCanvas): LeituraSensor {
        return LeituraSensor(
            temperatura = calcularTemperatura(posicao),
            nivelRuido = calcularNivelRuido(posicao),
            qualidadeAr = calcularQualidadeAr(),
            timestamp = "2025-11-14T10:00:00Z" // Mock fixo
        )
    }

    /**
     * Calcula temperatura baseada na distância das janelas
     *
     * Lógica:
     * - Temperatura base: 22°C (centro)
     * - Perto das janelas: até +2°C (sol)
     * - Longe das janelas: até -1°C
     */
    private fun calcularTemperatura(posicao: PosicaoCanvas): Float {
        val temperaturaBase = ReferenciasEspaciais.Temperatura.BASE

        // Calcula distância para a janela mais próxima
        val distanciaJanelaMaisProxima = ReferenciasEspaciais.JANELAS.minOf { janela ->
            calcularDistancia(posicao, janela)
        }

        // Calcula variação baseada na distância
        val raioInfluencia = ReferenciasEspaciais.Temperatura.RAIO_INFLUENCIA_JANELA
        val variacaoMaxima = ReferenciasEspaciais.Temperatura.VARIACAO_JANELA

        val fatorProximidade = 1f - (distanciaJanelaMaisProxima / raioInfluencia).coerceIn(0f, 1f)
        val variacao = fatorProximidade * variacaoMaxima

        // Adiciona pequena aleatoriedade (±0.5°C)
        val ruido = Random.nextFloat() * 1f - 0.5f

        return (temperaturaBase + variacao + ruido).coerceIn(20f, 25f)
    }

    /**
     * Calcula nível de ruído baseado na distância da área de descanso
     *
     * Lógica:
     * - Perto da área de descanso: MODERADO ou ALTO
     * - Longe da área de descanso: SILENCIOSO ou MODERADO
     * - Extremidades da sala: SILENCIOSO
     */
    private fun calcularNivelRuido(posicao: PosicaoCanvas): NivelRuido {
        val distanciaAreaDescanso = calcularDistancia(
            posicao,
            ReferenciasEspaciais.CENTRO_AREA_DESCANSO
        )

        val raioInfluencia = ReferenciasEspaciais.Ruido.RAIO_INFLUENCIA_DESCANSO

        return when {
            distanciaAreaDescanso < raioInfluencia * 0.5f -> {
                // Muito perto da área de descanso
                if (Random.nextFloat() < 0.6f) NivelRuido.MODERADO else NivelRuido.ALTO
            }
            distanciaAreaDescanso < raioInfluencia -> {
                // Próximo da área de descanso
                if (Random.nextFloat() < 0.7f) NivelRuido.MODERADO else NivelRuido.SILENCIOSO
            }
            else -> {
                // Longe da área de descanso
                if (Random.nextFloat() < 0.8f) NivelRuido.SILENCIOSO else NivelRuido.MODERADO
            }
        }
    }

    /**
     * Calcula qualidade do ar (variação leve aleatória)
     *
     * Lógica:
     * - 70% BOA
     * - 25% REGULAR
     * - 5% RUIM
     */
    private fun calcularQualidadeAr(): QualidadeAr {
        val random = Random.nextFloat()
        return when {
            random < 0.70f -> QualidadeAr.BOA
            random < 0.95f -> QualidadeAr.REGULAR
            else -> QualidadeAr.RUIM
        }
    }

    /**
     * Calcula distância euclidiana entre duas posições
     */
    private fun calcularDistancia(p1: PosicaoCanvas, p2: PosicaoCanvas): Float {
        val dx = p2.x - p1.x
        val dy = p2.y - p1.y
        return sqrt(dx.pow(2) + dy.pow(2))
    }

    // ========================================================================
    // DISTRIBUIÇÃO DE STATUS
    // ========================================================================

    /**
     * Aplica distribuição de status nas estações (~60% livre, 20% ocupado, 20% reservado)
     */
    private fun aplicarDistribuicaoStatus(estacoes: List<EstacaoDeTrabalho>): List<EstacaoDeTrabalho> {
        val totalEstacoes = estacoes.size

        val numLivres = (totalEstacoes * DistribuicaoStatus.PERCENTUAL_LIVRE).toInt()
        val numOcupados = (totalEstacoes * DistribuicaoStatus.PERCENTUAL_OCUPADO).toInt()
        val numReservados = totalEstacoes - numLivres - numOcupados // Resto vai para reservado

        // Embaralha a lista para distribuir status aleatoriamente
        val estacoesEmbaralhadas = estacoes.shuffled()

        return estacoesEmbaralhadas.mapIndexed { index, estacao ->
            val novoStatus = when {
                index < numLivres -> StatusEstacao.LIVRE
                index < numLivres + numOcupados -> StatusEstacao.OCUPADO
                else -> StatusEstacao.RESERVADO
            }

            estacao.copy(status = novoStatus)
        }
    }
}
