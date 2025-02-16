package com.josenavio.pixelartcanvas.ui.screens.pixel


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.josenavio.pixelartcanvas.core.utils.appDrawables

@Composable
fun PixelScreen(
    pixelState: PixelState,
    pixelEvent: (PixelEvent) -> Unit,
) {

    val noInsetPadding = WindowInsets(0, 0, 0, 0)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = noInsetPadding
    ) { paddingValues ->

        Box(
            modifier = Modifier.padding(paddingValues),
            contentAlignment = Alignment.BottomCenter
        ) {
            // Canvas
            PixelCanvas(pixelState, pixelEvent)
            // Bars
            Column(modifier = Modifier.navigationBarsPadding()) {
                // Colors
                ColorBar(pixelState, pixelEvent)
                // Bottom bar
                BottomBar(pixelState, pixelEvent)
            }
        }
    }
}

@Composable
private fun PixelCanvas(pixelState: PixelState, pixelEvent: (PixelEvent) -> Unit) {

    Box(
        modifier = Modifier
            .clipToBounds()
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTransformGestures { _, centroid, zoom, _ ->
                    // Scaling
                    pixelEvent(PixelEvent.OnScaleChanged(zoom))
                    // Panning
                    pixelEvent(
                        PixelEvent.OnTranslateChanged(
                            IntOffset(
                                centroid.x.toInt(),
                                centroid.y.toInt()
                            )
                        )
                    )
                }
            }
    ) {
        PixelGrid(pixelState, pixelEvent)
    }
}

@Composable
private fun PixelGrid(pixelState: PixelState, pixelEvent: (PixelEvent) -> Unit) {
    val aspectRatio = pixelState.width.toFloat() / pixelState.height.toFloat()

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .aspectRatio(aspectRatio)
            .offset { pixelState.offset }
            .scale(pixelState.scale)
            .clip(RoundedCornerShape(2.dp))
            .pointerInput(Unit) {
                detectTapGestures(onTap = { offset ->
                    val gridWidth = pixelState.width
                    val gridHeight = pixelState.height

                    val x = ((gridWidth * offset.x) / size.width).toInt()
                    val y = ((gridHeight * offset.y) / size.height).toInt()

                    pixelEvent(PixelEvent.OnPixelTapped(x, y))
                })
            }
            .pointerInput(pixelState.selectedTool) {
                if (pixelState.selectedTool != PixelCanvasTool.PAN) {

                    detectDragGestures { change, _ ->

                        val gridWidth = pixelState.width
                        val gridHeight = pixelState.height

                        val x = ((gridWidth * change.position.x) / size.width).toInt()
                        val y = ((gridHeight * change.position.y) / size.height).toInt()

                        pixelEvent(PixelEvent.OnPixelTapped(x, y))
                    }
                }
            }
            .drawBehind {

                // Background
                repeat(pixelState.width) { x ->
                    repeat(pixelState.height) { y ->
                        val checkeredColor = if ((x + y) % 2 == 0) Color.LightGray else Color.Gray
                        drawRect(
                            style = Fill,
                            color = checkeredColor,
                            topLeft = Offset(
                                x * size.width / pixelState.width,
                                y * size.height / pixelState.height
                            ),
                            size = Size(
                                size.width / pixelState.width,
                                size.height / pixelState.height
                            )
                        )
                    }
                }
            }
    ) {
        // Clip
        clipRect {
            // Transformations
            withTransform(
                transformBlock = { },
                drawBlock = {
                    // Pixels
                    repeat(pixelState.width) { x ->
                        repeat(pixelState.height) { y ->
                            pixelState.colors[PixelKey(x, y)]?.let {
                                drawRect(
                                    style = Fill,
                                    color = it,
                                    topLeft = Offset(
                                        x * size.width / pixelState.width,
                                        y * size.height / pixelState.height
                                    ),
                                    size = Size(
                                        size.width / pixelState.width,
                                        size.height / pixelState.height
                                    )
                                )
                            }
                        }
                    }
                    // Grid
                    if (pixelState.showGrid) {
                        repeat(pixelState.width) { x ->
                            repeat(pixelState.height) { y ->
                                drawRect(
                                    style = Stroke(1.dp.toPx() / pixelState.scale),
                                    color = Color.Cyan.copy(alpha = 0.45f),
                                    topLeft = Offset(
                                        x * size.width / pixelState.width,
                                        y * size.height / pixelState.height
                                    ),
                                    size = Size(
                                        size.width / pixelState.width,
                                        size.height / pixelState.height
                                    )
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun ColorBar(pixelState: PixelState, onEvent: (PixelEvent) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        PixelColorButton(
            color = Color.White,
            isSelected = pixelState.selectedColor == Color.White,
            onClick = { onEvent(PixelEvent.OnSelectColor(Color.White)) }
        )
        PixelColorButton(
            color = Color.Black,
            isSelected = pixelState.selectedColor == Color.Black,
            onClick = { onEvent(PixelEvent.OnSelectColor(Color.Black)) }
        )
        PixelColorButton(
            color = Color.Red,
            isSelected = pixelState.selectedColor == Color.Red,
            onClick = { onEvent(PixelEvent.OnSelectColor(Color.Red)) }
        )
        PixelColorButton(
            color = Color.Green,
            isSelected = pixelState.selectedColor == Color.Green,
            onClick = { onEvent(PixelEvent.OnSelectColor(Color.Green)) }
        )
        PixelColorButton(
            color = Color.Blue,
            isSelected = pixelState.selectedColor == Color.Blue,
            onClick = { onEvent(PixelEvent.OnSelectColor(Color.Blue)) }
        )
    }
}

@Composable
private fun BottomBar(pixelState: PixelState, onEvent: (PixelEvent) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        PixelIconButton(
            isSelected = pixelState.selectedTool == PixelCanvasTool.PAN,
            icon = appDrawables.icon_pan,
            onClick = { onEvent(PixelEvent.OnSelectTool(PixelCanvasTool.PAN)) }
        )
        PixelIconButton(
            isSelected = pixelState.selectedTool == PixelCanvasTool.DRAW,
            icon = appDrawables.icon_draw,
            onClick = { onEvent(PixelEvent.OnSelectTool(PixelCanvasTool.DRAW)) }
        )
        PixelIconButton(
            isSelected = pixelState.selectedTool == PixelCanvasTool.ERASE,
            icon = appDrawables.icon_erase,
            onClick = { onEvent(PixelEvent.OnSelectTool(PixelCanvasTool.ERASE)) }
        )
        PixelIconButton(
            icon = appDrawables.icon_center,
            onClick = {
                onEvent(PixelEvent.OnCenterCanvas)
            }
        )
        PixelIconButton(
            isSelected = pixelState.showGrid,
            icon = appDrawables.icon_grid,
            onClick = {
                onEvent(PixelEvent.OnEnableDisableGrid)
            }
        )
    }
}

@Composable
private fun PixelColorButton(color: Color, onClick: () -> Unit, isSelected: Boolean = false) {

    Button(
        onClick = onClick,
        modifier = Modifier.size(56.dp),
        colors = ButtonColors(
            containerColor = if (isSelected) color else color.darken(),
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        contentPadding = PaddingValues(2.dp),
        shape = CircleShape
    ) {
        Box(
            modifier = Modifier.size(56.dp)
        )
    }

}

private fun Color.darken(factor: Float = 0.65f): Color {
    return copy(
        red = red * (1 - factor),
        green = green * (1 - factor),
        blue = blue * (1 - factor)
    )
}

@Composable
private fun PixelIconButton(icon: Int, onClick: () -> Unit, isSelected: Boolean = false) {

    Button(
        onClick = onClick,
        modifier = Modifier.size(56.dp),
        colors = ButtonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        contentPadding = PaddingValues(8.dp),
        shape = CircleShape
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = "Add"
        )
    }

}
