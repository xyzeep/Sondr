package com.softwarica.sondr.utils

import android.content.Context
import android.media.MediaExtractor
import android.media.MediaFormat
import android.net.Uri
import java.nio.ByteBuffer
import kotlin.math.abs

fun extractAmplitudesFromUri(context: Context, uri: Uri): IntArray {
    val extractor = MediaExtractor()
    extractor.setDataSource(context, uri, null)

    // Find first audio track
    var audioTrackIndex = -1
    for (i in 0 until extractor.trackCount) {
        val format = extractor.getTrackFormat(i)
        val mime = format.getString(MediaFormat.KEY_MIME)
        if (mime?.startsWith("audio/") == true) {
            audioTrackIndex = i
            break
        }
    }

    if (audioTrackIndex == -1) return IntArray(0)

    extractor.selectTrack(audioTrackIndex)

    val buffer = ByteBuffer.allocate(1024)
    val amplitudes = mutableListOf<Int>()

    while (true) {
        val sampleSize = extractor.readSampleData(buffer, 0)
        if (sampleSize < 0) break

        // Simple peak detection for waveform preview (just use first byte of each chunk)
        for (i in 0 until sampleSize step 2) {
            val sample = abs(buffer.get(i).toInt())
            amplitudes.add(sample)
        }

        extractor.advance()
    }

    extractor.release()
    return amplitudes.toIntArray()
}

fun downsampleAmplitudes(samples: IntArray, desiredSize: Int): IntArray {
    if (samples.size <= desiredSize) return samples

    val blockSize = samples.size.toDouble() / desiredSize
    val downsampled = IntArray(desiredSize)

    for (i in 0 until desiredSize) {
        val start = (i * blockSize).toInt()
        val end = ((i + 1) * blockSize).toInt().coerceAtMost(samples.size)
        val block = samples.sliceArray(start until end)
        downsampled[i] = block.average().toInt()
    }
    return downsampled
}

fun filterNoise(samples: IntArray, threshold: Int = 500): IntArray {
    return samples.map { if (it < threshold) 0 else it }.toIntArray()
}
