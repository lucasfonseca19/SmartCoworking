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
 *
 * OTIMIZAÇÃO: Usa cache para evitar recálculos pesados e distanceCache para sqrt()
 */
object EstacoesMockData {

    // Cache para evitar reprocessar dados mockados
    private var cachedEstacoes: List<EstacaoDeTrabalho>? = null
    private var cachedAreasEspeciais: List<AreaEspecial>? = null

    // Cache de distâncias para evitar cálculos repetidos de sqrt()
    private val distanceCache = mutableMapOf<Pair<PosicaoCanvas, PosicaoCanvas>, Float>()

    /**
     * Retorna todas as estações de trabalho mockadas (25 estações)
     * Posições baseadas no design do Figma (1500x900px)
     *
     * Distribuição:
     * - 5 individuais na parede esquerda (vertical)
     * - 4 mesas colaborativas no centro-esquerda
     * - 4 individuais no topo
     * - 4 individuais no fundo
     * - 5 individuais na coluna centro-direita (vertical)
     * - 3 salas de reunião na parede direita
     * - 1 área de descanso central (retornada separadamente)
     *
     * Status: ~60% livre, ~20% ocupado, ~20% reservado
     *
     * OTIMIZAÇÃO: Resultados são cacheados após primeira geração
     */
    fun obterEstacoes(): List<EstacaoDeTrabalho> {
        // Retorna cache se já foi gerado
        cachedEstacoes?.let { return it }

        // Gera dados pela primeira vez
        var numeroSequencial = 1
        val estacoes = mutableListOf<EstacaoDeTrabalho>()

        // ZONA 1: Parede Esquerda - 5 Estações Individuais
        PosicionamentoEstacoes.ParedeEsquerda.POSICOES_Y.forEach { y ->
            estacoes.add(
                criarEstacaoIndividual(
                    numero = numeroSequencial++,
                    x = PosicionamentoEstacoes.ParedeEsquerda.X,
                    y = y
                )
            )
        }

        // ZONA 2: Centro-Esquerda - 4 Mesas Colaborativas
        PosicionamentoEstacoes.MesasColaborativas.POSICOES.forEach { (x, y) ->
            estacoes.add(
                criarEstacaoColaborativa(
                    numero = numeroSequencial++,
                    x = x,
                    y = y
                )
            )
        }

        // ZONA 3: Topo - 4 Estações Individuais
        PosicionamentoEstacoes.Topo.POSICOES_X.forEach { x ->
            estacoes.add(
                criarEstacaoIndividual(
                    numero = numeroSequencial++,
                    x = x,
                    y = PosicionamentoEstacoes.Topo.Y
                )
            )
        }

        // ZONA 4: Fundo - 4 Estações Individuais
        PosicionamentoEstacoes.Fundo.POSICOES_X.forEach { x ->
            estacoes.add(
                criarEstacaoIndividual(
                    numero = numeroSequencial++,
                    x = x,
                    y = PosicionamentoEstacoes.Fundo.Y
                )
            )
        }

        // ZONA 5: Coluna Centro-Direita - 5 Estações Individuais
        PosicionamentoEstacoes.ColunaDireita.POSICOES_Y.forEach { y ->
            estacoes.add(
                criarEstacaoIndividual(
                    numero = numeroSequencial++,
                    x = PosicionamentoEstacoes.ColunaDireita.X,
                    y = y
                )
            )
        }

        // ZONA 6: Parede Direita - 3 Salas de Reunião
        PosicionamentoEstacoes.SalasReuniao.DADOS.forEach { (y, largura, altura) ->
            estacoes.add(
                criarSala(
                    numero = numeroSequencial++,
                    x = PosicionamentoEstacoes.SalasReuniao.X,
                    y = y,
                    largura = largura,
                    altura = altura
                )
            )
        }

        // Aplicar distribuição de status (~60% livre, 20% ocupado, 20% reservado)
        val resultado = aplicarDistribuicaoStatus(estacoes)

        // Cachear resultado
        cachedEstacoes = resultado
        return resultado
    }

    /**
     * Retorna áreas especiais não reserváveis
     * Usa constantes centralizadas de CanvasConfig
     *
     * OTIMIZAÇÃO: Resultados são cacheados
     */
    fun obterAreasEspeciais(): List<AreaEspecial> {
        cachedAreasEspeciais?.let { return it }

        val resultado = listOf(
            AreaEspecial(
                id = "AREA-001",
                label = "Área de Descanso",
                posicao = PosicaoCanvas(
                    x = PosicionamentoEstacoes.AreaDescanso.X,
                    y = PosicionamentoEstacoes.AreaDescanso.Y
                ),
                dimensoes = TamanhosEstacao.AREA_DESCANSO
            )
        )

        cachedAreasEspeciais = resultado
        return resultado
    }

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
        y: Float,
        largura: Float = TamanhosEstacao.SALA_SIZE.largura,
        altura: Float = TamanhosEstacao.SALA_SIZE.altura
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
            dimensoes = DimensoesCanvas(largura, altura),
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
     *
     * OTIMIZAÇÃO: Usa cache para evitar recálculos de sqrt()
     * Reduz 125+ chamadas de sqrt() para apenas as únicas necessárias
     */
    private fun calcularDistancia(p1: PosicaoCanvas, p2: PosicaoCanvas): Float {
        val key = p1 to p2
        return distanceCache.getOrPut(key) {
            val dx = p2.x - p1.x
            val dy = p2.y - p1.y
            sqrt(dx.pow(2) + dy.pow(2))
        }
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
