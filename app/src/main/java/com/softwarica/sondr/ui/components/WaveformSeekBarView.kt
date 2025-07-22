package com.softwarica.sondr.ui.components

import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.masoudss.lib.WaveformSeekBar
import com.masoudss.lib.utils.WaveGravity
import androidx.compose.runtime.Composable
import kotlin.random.Random


@Composable
fun WaveformSeekBarView(
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            WaveformSeekBar(ctx).apply {
                waveWidth = 12f
                waveGap = 10f
                waveMinHeight = 1f
                waveCornerRadius = 16f
                waveGravity = WaveGravity.CENTER
                wavePaddingTop = 10
                wavePaddingBottom = 100
                wavePaddingLeft = 16
                waveBackgroundColor = Color(0xFF476A95).copy(alpha = 0.8f).toArgb()
                waveProgressColor = Color(0xFF98C6E6).toArgb()
                markerWidth = 10f
                markerColor = Color.Red.toArgb()
                markerTextSize = 12f
                markerTextColor = Color.White.toArgb()
                markerTextPadding = 4f
            }
        },

        update = { waveformSeekBar ->
            val fakeWaveform = generateRandomWaveform(50, 2, 8)
            waveformSeekBar.setSampleFrom(fakeWaveform)
        }
    )
}

fun generateRandomWaveform(size: Int, minHeight: Int = 2, maxHeight: Int = 1): IntArray {
    val samples = IntArray(size)
    var currentHeight = Random.nextInt(minHeight, maxHeight)

    for (i in 0 until size) {

        // change the height slightly compared to the previous one
        val variation = Random.nextInt(-15, 16)  // Small change
        currentHeight = (currentHeight + variation).coerceIn(minHeight, maxHeight)
        samples[i] = currentHeight
    }

    return samples
}
