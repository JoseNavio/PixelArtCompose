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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
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
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 32.dp)
                    .statusBarsPadding(),
                contentAlignment = Alignment.CenterEnd
            ) {
                // Top bar
                ScrollableColumn(
                    items = pixelTopButtons,
                    pixelEvent = pixelEvent
                )
            }
            // Canvas
            PixelCanvas(pixelState, pixelEvent)
            // Bottom bars
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                // Bottom bar
                BottomBar(pixelState, pixelEvent)
                // Colors
                ColorBar(pixelState, pixelEvent)
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


//
//@Composable
//private fun PixelGrid(pixelState: PixelState, pixelEvent: (PixelEvent) -> Unit) {
//    val aspectRatio = pixelState.width.toFloat() / pixelState.height.toFloat()
//
//    Canvas(
//        modifier = Modifier
//            .fillMaxSize()
//            .aspectRatio(aspectRatio)
//            .offset { pixelState.offset }
//            .scale(pixelState.scale)
//            .clip(RoundedCornerShape(2.dp))
//            .pointerInput(Unit) {
//                detectTapGestures(onTap = { offset ->
//                    val gridWidth = pixelState.width
//                    val gridHeight = pixelState.height
//
//                    val x = ((gridWidth * offset.x) / size.width).toInt()
//                    val y = ((gridHeight * offset.y) / size.height).toInt()
//
//                    pixelEvent(PixelEvent.OnPixelTapped(x, y))
//                })
//            }
//            .pointerInput(pixelState.selectedTool) {
//                if (pixelState.selectedTool != PixelCanvasTool.PAN) {
//
//                    detectDragGestures { change, _ ->
//
//                        val gridWidth = pixelState.width
//                        val gridHeight = pixelState.height
//
//                        val x = ((gridWidth * change.position.x) / size.width).toInt()
//                        val y = ((gridHeight * change.position.y) / size.height).toInt()
//
//                        pixelEvent(PixelEvent.OnPixelTapped(x, y))
//                    }
//                }
//            }
//            .drawBehind {
//
//                // Background
//                repeat(pixelState.width) { x ->
//                    repeat(pixelState.height) { y ->
//                        val checkeredColor = if ((x + y) % 2 == 0) Color.LightGray else Color.Gray
//                        drawRect(
//                            style = Fill,
//                            color = checkeredColor,
//                            topLeft = Offset(
//                                x * size.width / pixelState.width,
//                                y * size.height / pixelState.height
//                            ),
//                            size = Size(
//                                size.width / pixelState.width + 1,
//                                size.height / pixelState.height + 1
//                            )
//                        )
//                    }
//                }
//            }
//    ) {
//        // Clip
//        clipRect {
//            // Transformations
//            withTransform(
//                transformBlock = { },
//                drawBlock = {
//                    // Pixels
//                    repeat(pixelState.width) { x ->
//                        repeat(pixelState.height) { y ->
//                            pixelState.colors[PixelKey(x, y)]?.let {
//                                drawRect(
//                                    style = Fill,
//                                    color = it,
//                                    topLeft = Offset(
//                                        x * size.width / pixelState.width,
//                                        y * size.height / pixelState.height
//                                    ),
//                                    size = Size(
//                                        size.width / pixelState.width + 1,
//                                        size.height / pixelState.height + 1
//                                    )
//                                )
//                            }
//                        }
//                    }
//                    // Grid
//                    if (pixelState.showGrid) {
//                        repeat(pixelState.width) { x ->
//                            repeat(pixelState.height) { y ->
//                                drawRect(
//                                    style = Stroke(1.dp.toPx() / pixelState.scale),
//                                    color = Color.Cyan.copy(alpha = 0.45f),
//                                    topLeft = Offset(
//                                        x * size.width / pixelState.width,
//                                        y * size.height / pixelState.height
//                                    ),
//                                    size = Size(
//                                        size.width / pixelState.width + 1,
//                                        size.height / pixelState.height + 1
//                                    )
//                                )
//                            }
//                        }
//                    }
//                }
//            )
//        }
//    }
//}


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
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomEnd = 0.dp,
                    bottomStart = 0.dp
                )
            )
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 32.dp, vertical = 16.dp)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        PixelColorButton(
            color = PixelColor.LIGHT_GREEN,
            isSelected = pixelState.selectedColor == PixelColor.LIGHT_GREEN,
            onClick = { onEvent(PixelEvent.OnSelectColor(PixelColor.LIGHT_GREEN)) }
        )

        PixelColorButton(
            color = PixelColor.MINT,
            isSelected = pixelState.selectedColor == PixelColor.MINT,
            onClick = { onEvent(PixelEvent.OnSelectColor(PixelColor.MINT)) }
        )

        PixelColorButton(
            color = PixelColor.DARK_GREEN,
            isSelected = pixelState.selectedColor == PixelColor.DARK_GREEN,
            onClick = { onEvent(PixelEvent.OnSelectColor(PixelColor.DARK_GREEN)) }
        )

        PixelColorButton(
            color = PixelColor.BLACK,
            isSelected = pixelState.selectedColor == PixelColor.BLACK,
            onClick = { onEvent(PixelEvent.OnSelectColor(PixelColor.BLACK)) }
        )

    }
}

@Composable
private fun BottomBar(pixelState: PixelState, onEvent: (PixelEvent) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        // Draw
        PixelIconButton(
            isSelected = pixelState.selectedTool == PixelCanvasTool.DRAW,
            icon = appDrawables.icon_draw,
            onClick = { onEvent(PixelEvent.OnSelectTool(PixelCanvasTool.DRAW)) }
        )
        // Bucket
        PixelIconButton(
            isSelected = pixelState.selectedTool == PixelCanvasTool.BUCKET,
            icon = appDrawables.icon_bucket,
            onClick = { onEvent(PixelEvent.OnSelectTool(PixelCanvasTool.BUCKET)) }
        )
        // Erase
        PixelIconButton(
            isSelected = pixelState.selectedTool == PixelCanvasTool.ERASE,
            icon = appDrawables.icon_erase,
            onClick = { onEvent(PixelEvent.OnSelectTool(PixelCanvasTool.ERASE)) }
        )
        ScrollableColumn(pixelEvent = onEvent)
    }
}

data class PixelButton(
    val icon: Int,
    val event: PixelEvent? = null,
)

val pixelTopButtons = listOf(
    PixelButton(
        icon = appDrawables.icon_eye,
    ),
    PixelButton(
        icon = appDrawables.icon_grid,
    ),
)

val pixelBottomButtons = listOf(
    PixelButton(
        icon = appDrawables.icon_center,
        event = PixelEvent.OnCenterCanvas
    ),
    PixelButton(
        icon = appDrawables.icon_pan,
    ),
    PixelButton(
        icon = appDrawables.icon_magnifier,
    ),
    PixelButton(
        icon = appDrawables.icon_lock,
    ),
)

@Composable
private fun ScrollableColumn(
    items: List<PixelButton> = pixelBottomButtons,
    pixelEvent: (PixelEvent) -> Unit,
) {

    Column(modifier = Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if (false) {
            // Scrollable
            LazyColumn(
                reverseLayout = false,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items) { item ->
                    PixelIconButton(
                        modifier = Modifier.size(48.dp),
                        icon = item.icon,
                        onClick = {
                            item.event?.let { event ->
                                pixelEvent(event)
                            }
                        }
                    )
                }
            }
        }
        // Selected // todo Add selected...
        PixelIconButton(
            modifier = Modifier
                .padding(top = 16.dp)
                .size(56.dp),
            icon = items.first().icon,
            onClick = { items.first().event?.let { event -> pixelEvent(event) } }
        )
    }

}

@Composable
private fun PixelColorButton(color: Color, onClick: () -> Unit, isSelected: Boolean = false) {

    Button(
        onClick = onClick,
        modifier = if (isSelected) Modifier.size(56.dp) else Modifier
            .padding(4.dp)
            .size(48.dp),
        colors = ButtonColors(
            containerColor = if (isSelected) color else color.darken(),
            contentColor = MaterialTheme.colorScheme.onBackground,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp
        ),
        contentPadding = PaddingValues(2.dp),
        shape = CircleShape
    ) { }

}

private fun Color.darken(factor: Float = 0.25f): Color {
    return copy(
        red = red * (1 - factor),
        green = green * (1 - factor),
        blue = blue * (1 - factor)
    )
}

@Composable
private fun PixelIconButton(
    icon: Int,
    onClick: () -> Unit,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier.size(56.dp),
) {

    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
            contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp
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
