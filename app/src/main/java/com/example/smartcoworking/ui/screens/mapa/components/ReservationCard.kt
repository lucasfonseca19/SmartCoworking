package com.example.smartcoworking.ui.screens.mapa.components


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.smartcoworking.data.models.EstacaoDeTrabalho
import com.example.smartcoworking.data.models.LeituraSensor
import com.example.smartcoworking.data.models.NivelRuido
import com.example.smartcoworking.data.models.QualidadeAr
import com.example.smartcoworking.data.models.StatusEstacao
import com.example.smartcoworking.data.models.TipoEstacao
import com.example.smartcoworking.data.models.FormaEstacao
import com.example.smartcoworking.data.models.PosicaoCanvas
import com.example.smartcoworking.data.models.DimensoesCanvas
import com.example.smartcoworking.ui.theme.CoffeeDark
import com.example.smartcoworking.ui.theme.CreamBackground
import com.example.smartcoworking.ui.theme.StatusGreen
import com.example.smartcoworking.ui.theme.StatusRed
import com.example.smartcoworking.ui.theme.StatusYellow

@Composable
fun ReservationCard(
    station: EstacaoDeTrabalho,
    onDismiss: () -> Unit,
    onReserve: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .width(360.dp) // Increased width
            .padding(16.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
        colors = CardDefaults.cardColors(containerColor = CreamBackground)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header: Title and Close Button
            // Using Box with constrained width for title to avoid overlap
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.TopStart).size(24.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fechar",
                        tint = CoffeeDark
                    )
                }
                
                Column(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 32.dp), // Padding to prevent overlap with X
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = station.nome,
                        style = MaterialTheme.typography.headlineSmall, // Slightly smaller for long names
                        color = CoffeeDark,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    StatusPill(status = station.status)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Capacity Indicator
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Capacidade",
                    tint = CoffeeDark.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (station.capacidade == 1) "1 Pessoa" else "${station.capacidade} Pessoas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CoffeeDark.copy(alpha = 0.6f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // IoT Sensors Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SensorItem(
                    icon = Icons.Default.AcUnit,
                    value = "${station.leituraSensor.temperatura.toInt()}°C",
                    label = "Temp"
                )
                SensorItem(
                    icon = Icons.AutoMirrored.Filled.VolumeUp,
                    value = station.leituraSensor.nivelRuido.name.lowercase().capitalize(),
                    label = "Ruído"
                )
                SensorItem(
                    icon = Icons.Default.Air,
                    value = station.leituraSensor.qualidadeAr.name.lowercase().capitalize(),
                    label = "Ar"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Amenities Section
            if (station.comodidades.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Comodidades",
                        style = MaterialTheme.typography.titleSmall,
                        color = CoffeeDark.copy(alpha = 0.7f),
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    // Simple FlowRow implementation using nested Rows for compatibility
                    // (Or just a simple Row with wrap if few items, or vertical list)
                    // Since we don't have FlowRow in older Compose versions without Accompanist,
                    // let's use a simple vertical list of rows or just one row with scroll if needed.
                    // For now, let's assume few items and use a wrapped layout logic or just display first few.
                    // Actually, let's just use a Column of Rows for simplicity without external libs.
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                         station.comodidades.take(3).forEach { item ->
                            AmenityChip(text = item)
                        }
                    }
                    if (station.comodidades.size > 3) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                             station.comodidades.drop(3).forEach { item ->
                                AmenityChip(text = item)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(32.dp))
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Reserve Button
            val isReservable = station.status == StatusEstacao.LIVRE
            Button(
                onClick = onReserve,
                enabled = isReservable,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CoffeeDark,
                    contentColor = CreamBackground,
                    disabledContainerColor = CoffeeDark.copy(alpha = 0.5f),
                    disabledContentColor = CreamBackground.copy(alpha = 0.7f)
                )
            ) {
                Text(
                    text = if (isReservable) "Reservar Agora" else "Indisponível",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun AmenityChip(text: String) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(CoffeeDark.copy(alpha = 0.1f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = CoffeeDark,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun StatusPill(status: StatusEstacao) {
    val (color, text) = when (status) {
        StatusEstacao.LIVRE -> StatusGreen to "Livre"
        StatusEstacao.OCUPADO -> StatusRed to "Ocupado"
        StatusEstacao.RESERVADO -> StatusYellow to "Reservado"
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(color.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = color,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun SensorItem(icon: ImageVector, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(CoffeeDark.copy(alpha = 0.05f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = CoffeeDark,
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = CoffeeDark,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = CoffeeDark.copy(alpha = 0.6f)
        )
    }
}

// Extension to capitalize first letter (simple version)
fun String.capitalize(): String {
    return this.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
}

@Preview
@Composable
fun ReservationCardPreview() {
    val mockStation = EstacaoDeTrabalho(
        id = "1",
        numero = 4,
        nome = "Estação 04",
        tipo = TipoEstacao.MESA,
        capacidade = 1,
        status = StatusEstacao.LIVRE,
        leituraSensor = LeituraSensor(22.5f, NivelRuido.SILENCIOSO, QualidadeAr.BOA, "now"),
        posicao = PosicaoCanvas(0f, 0f),
        dimensoes = DimensoesCanvas(0f, 0f),
        forma = FormaEstacao.QUADRADO
    )
    
    ReservationCard(
        station = mockStation,
        onDismiss = {},
        onReserve = {}
    )
}
