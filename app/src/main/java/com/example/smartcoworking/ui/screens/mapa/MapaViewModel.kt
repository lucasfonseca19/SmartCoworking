package com.example.smartcoworking.ui.screens.mapa

import androidx.lifecycle.ViewModel
import com.example.smartcoworking.data.EstacoesMockData
import com.example.smartcoworking.data.models.AreaEspecial
import com.example.smartcoworking.data.models.EstacaoDeTrabalho
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * ViewModel para o Mapa de Coworking
 *
 * Gerencia o estado das estações de trabalho e áreas especiais.
 * Na Fase 3, apenas carrega os dados mockados.
 * Na Fase 5, será implementada a simulação em tempo real.
 */
class MapaViewModel : ViewModel() {

    // Estado das estações de trabalho
    private val _estacoes = MutableStateFlow<List<EstacaoDeTrabalho>>(emptyList())
    val estacoes: StateFlow<List<EstacaoDeTrabalho>> = _estacoes.asStateFlow()

    // Estado das áreas especiais
    private val _areasEspeciais = MutableStateFlow<List<AreaEspecial>>(emptyList())
    val areasEspeciais: StateFlow<List<AreaEspecial>> = _areasEspeciais.asStateFlow()

    // Estado de carregamento
    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado da estação selecionada
    private val _estacaoSelecionada = MutableStateFlow<EstacaoDeTrabalho?>(null)
    val estacaoSelecionada: StateFlow<EstacaoDeTrabalho?> = _estacaoSelecionada.asStateFlow()

    init {
        carregarDados()
    }

    /**
     * Carrega dados mockados das estações e áreas especiais
     */
    private fun carregarDados() {
        _isLoading.value = true

        // Simular delay de carregamento (opcional, pode remover)
        // Na prática, os dados já estão disponíveis instantaneamente
        _estacoes.value = EstacoesMockData.obterEstacoes()
        _areasEspeciais.value = EstacoesMockData.obterAreasEspeciais()

        _isLoading.value = false
    }

    /**
     * Recarrega os dados (útil para refresh manual)
     */
    fun recarregar() {
        carregarDados()
    }

    /**
     * Seleciona uma estação específica
     */
    fun selecionarEstacao(estacao: EstacaoDeTrabalho?) {
        _estacaoSelecionada.value = estacao
    }



    /**
     * Reserva uma estação de trabalho
     */
    fun reservarEstacao(estacao: EstacaoDeTrabalho) {
        val novaLista = _estacoes.value.map {
            if (it.id == estacao.id) {
                it.copy(status = com.example.smartcoworking.data.models.StatusEstacao.RESERVADO)
            } else {
                it
            }
        }
        _estacoes.value = novaLista
        
        // Atualiza também a estação selecionada para refletir a mudança na UI
        if (_estacaoSelecionada.value?.id == estacao.id) {
            _estacaoSelecionada.value = _estacaoSelecionada.value?.copy(status = com.example.smartcoworking.data.models.StatusEstacao.RESERVADO)
        }
    }
}
