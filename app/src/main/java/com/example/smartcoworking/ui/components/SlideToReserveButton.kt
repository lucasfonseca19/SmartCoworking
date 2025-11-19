package com.example.smartcoworking.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun SlideToReserveButton(
    isReserved: Boolean,
    onReserve: () -> Unit,
    modifier: Modifier = Modifier
) {
    val width = 280.dp
    val height = 56.dp
    val thumbSize = 48.dp
    val padding = 4.dp

    val density = LocalDensity.current
    val totalDragDistance = with(density) { (width - thumbSize - (padding * 2)).toPx() }

    var offsetX by remember { mutableFloatStateOf(0f) }
    val draggableState = rememberDraggableState { delta ->
        if (!isReserved) {
            offsetX = (offsetX + delta).coerceIn(0f, totalDragDistance)
        }
    }

    // Reset se soltar antes do fim
    LaunchedEffect(offsetX) {
        if (offsetX >= totalDragDistance * 0.95f && !isReserved) {
            onReserve()
        }
    }
    
    // Se já reservado, manter no final
    LaunchedEffect(isReserved) {
        if (isReserved) {
            offsetX = totalDragDistance
        } else {
            offsetX = 0f
        }
    }

    val progress = (offsetX / totalDragDistance).coerceIn(0f, 1f)
    
    val contentColor by animateColorAsState(
        targetValue = if (isReserved) Color.White else Color.Gray,
        label = "contentColor"
    )

    Box(
        modifier = modifier
            .width(width)
            .height(height)
            .clip(RoundedCornerShape(height / 2))
            .background(Color(0xFFE0E0E0)) // Fundo Cinza Padrão
            .draggable(
                state = draggableState,
                orientation = Orientation.Horizontal,
                onDragStopped = {
                    if (!isReserved) {
                        // Snap back se não completou
                        if (offsetX < totalDragDistance * 0.95f) {
                            offsetX = 0f
                        }
                    }
                }
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        // CAMADA DE PREENCHIMENTO VERDE (FILL)
        // A largura depende do offset + tamanho do thumb
        val fillWidth = with(density) { offsetX.toDp() + thumbSize + (padding * 2) }
        Box(
            modifier = Modifier
                .width(fillWidth)
                .height(height)
                .clip(RoundedCornerShape(topStart = height/2, bottomStart = height/2, topEnd = height/2, bottomEnd = height/2)) // Arredondar tudo para ficar bonito
                .background(Color(0xFF4CAF50))
        )

        // Texto de Fundo
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (isReserved) "RESERVADO" else "Deslize para reservar",
                style = MaterialTheme.typography.labelLarge.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = if (isReserved) Color.White else contentColor,
                modifier = Modifier.alpha(if (isReserved) 1f else (1f - progress))
            )
        }

        // Thumb (Botão Deslizante)
        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .padding(padding)
                .size(thumbSize)
                .clip(CircleShape)
                .background(Color.White)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.animation.AnimatedContent(
                targetState = isReserved,
                transitionSpec = {
                    if (targetState) {
                        (androidx.compose.animation.fadeIn() + androidx.compose.animation.scaleIn())
                            .togetherWith(androidx.compose.animation.fadeOut() + androidx.compose.animation.scaleOut())
                    } else {
                        (androidx.compose.animation.fadeIn() + androidx.compose.animation.scaleIn())
                            .togetherWith(androidx.compose.animation.fadeOut() + androidx.compose.animation.scaleOut())
                    }
                },
                label = "IconTransition"
            ) { reserved ->
                Icon(
                    imageVector = if (reserved) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = if (reserved) Color(0xFF4CAF50) else Color.Gray
                )
            }
        }
    }
}
