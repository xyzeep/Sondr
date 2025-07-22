package com.softwarica.sondr.ui.components

import android.content.Context
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import com.masoudss.lib.WaveformSeekBar
import com.masoudss.lib.utils.WaveGravity


@Composable
fun WaveformSeekBarView(
    modifier: Modifier = Modifier,
    context: Context,
    audioUriString: String?, // path or URI string to your audio
) {
    AndroidView(
        modifier = modifier,
        factory = { ctx ->
            WaveformSeekBar(ctx).apply {
                waveWidth = 18f
                waveGap = 16f
                waveMinHeight = 5f
                waveCornerRadius = 16f
                waveGravity = WaveGravity.CENTER
                wavePaddingTop = 100
                wavePaddingBottom = 0
                wavePaddingLeft = 16
                wavePaddingRight = 16
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

            audioUriString?.let { path ->
                try {
                    // This will load samples from file or URI string
                    waveformSeekBar.setSampleFrom(path)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    )
}

