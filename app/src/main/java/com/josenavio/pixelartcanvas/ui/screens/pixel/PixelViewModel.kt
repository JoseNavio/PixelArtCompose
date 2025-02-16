package com.josenavio.pixelartcanvas.ui.screens.pixel

import androidx.compose.runtime.toMutableStateMap
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntOffset
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


sealed interface PixelEvent {
    data object OnCenterCanvas : PixelEvent
    data object OnEnableDisableGrid : PixelEvent
    data class OnSelectTool(val tool: PixelCanvasTool) : PixelEvent
    data class OnSelectColor(val color: Color) : PixelEvent
    data class OnPixelTapped(val x: Int, val y: Int) : PixelEvent
    data class OnScaleChanged(val scale: Float) : PixelEvent
    data class OnTranslateChanged(val offset: IntOffset) : PixelEvent
}

enum class PixelCanvasTool {
    PAN,
    DRAW,
    ERASE,
}


data class PixelKey(val x: Int, val y: Int)

data class PixelState(
    val showGrid: Boolean = false,
    val scale: Float = 1f,
    val offset: IntOffset = IntOffset.Zero,
    val width: Int = 32,
    val height: Int = 32,
    var selectedTool: PixelCanvasTool = PixelCanvasTool.DRAW,
    val selectedColor: Color = Color.Red,
    val colors: MutableMap<PixelKey, Color> =
        List(width) { x ->
            List(height) { y ->
                // Key-value pair
                PixelKey(x, y) to Color.Transparent
            }
        }
            .flatten()
            .toMutableStateMap(),
)

class PixelViewModel : ViewModel() {

    // todo First we create project in database, then view model...

    private val _state = MutableStateFlow(PixelState())
    val state = _state.asStateFlow()

    fun onEvent(event: PixelEvent) {

        when (event) {

            is PixelEvent.OnCenterCanvas -> {

                _state.update {
                    it.copy(
                        scale = 1f,
                        offset = IntOffset.Zero
                    )
                }
            }

            is PixelEvent.OnPixelTapped -> {

                val newColors = _state.value.colors

                if (_state.value.selectedTool == PixelCanvasTool.DRAW) {
                    newColors.also { map ->
                        map[PixelKey(event.x, event.y)] = _state.value.selectedColor
                    }
                } else if (_state.value.selectedTool == PixelCanvasTool.ERASE) {
                    newColors.also { map ->
                        map[PixelKey(event.x, event.y)] = Color.Transparent
                    }
                }
                // Update the state
                _state.update { state ->
                    state.copy(
                        colors = newColors
                    )
                }

            }

            is PixelEvent.OnScaleChanged -> {

                val newScale = _state.value.scale * event.scale

                _state.update {
                    it.copy(scale = newScale)
                }
            }

            is PixelEvent.OnTranslateChanged -> {

//                if (_state.value.selectedTool == PixelCanvasTool.PAN) {
                    val newOffset = _state.value.offset + event.offset
                    _state.update {
                        it.copy(offset = newOffset)
                    }
//                }

            }

            PixelEvent.OnEnableDisableGrid -> {
                _state.update {
                    it.copy(
                        showGrid = !_state.value.showGrid
                    )
                }
            }

            is PixelEvent.OnSelectColor -> {
                _state.update {
                    it.copy(
                        selectedColor = event.color
                    )
                }
            }

            is PixelEvent.OnSelectTool -> {
                _state.update {
                    it.copy(
                        selectedTool = event.tool
                    )
                }
            }
        }
    }
}