package com.example.smartcoworking.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartcoworking.data.models.StatusEstacao

/**
 * Objeto que define as cores do sistema de mapa de estações
 * Seguindo especificação Material Design com contraste adequado
 */
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

    /**
     * Mapeia o status de uma estação para sua cor correspondente.
     *
     * Esta função centraliza a lógica de mapeamento status→cor,
     * eliminando duplicação de código em todos os componentes.
     *
     * @param status Status da estação (LIVRE/OCUPADO/RESERVADO)
     * @return Cor correspondente ao status
     */
    fun getStatusColor(status: StatusEstacao): Color = when (status) {
        StatusEstacao.LIVRE -> StatusLivre
        StatusEstacao.OCUPADO -> StatusOcupado
        StatusEstacao.RESERVADO -> StatusReservado
    }
}

/**
 * Preview das cores do mapa para validação no Android Studio
 * Mostra as três cores principais de status com seus respectivos labels
 */
@Preview(showBackground = true, widthDp = 300, heightDp = 100)
@Composable
fun MapColorsPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            // Status Livre
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(MapColors.StatusLivre)
            ) {
                Text(
                    "Livre",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Black
                )
            }

            // Status Ocupado
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(MapColors.StatusOcupado)
            ) {
                Text(
                    "Ocupado",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White
                )
            }

            // Status Reservado
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .background(MapColors.StatusReservado)
            ) {
                Text(
                    "Reservado",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.Black
                )
            }
        }
    }
}
