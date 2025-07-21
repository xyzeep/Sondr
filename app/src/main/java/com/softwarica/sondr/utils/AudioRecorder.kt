package com.softwarica.sondr.utils

import android.content.Context
import android.media.MediaRecorder
import android.net.Uri
import java.io.File

class AudioRecorder(private val context: Context) {
    private var recorder: MediaRecorder? = null
    private var outputFile: File? = null

    fun startRecording(): Uri {
        outputFile = File.createTempFile("temp_audio", ".m4a", context.cacheDir)
        recorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile!!.absolutePath)
            prepare()
            start()
        }
        return Uri.fromFile(outputFile)
    }

    fun stopRecording() {
        recorder?.apply {
            stop()
            release()
        }
        recorder = null
    }

    fun getOutputUri(): Uri? = outputFile?.let { Uri.fromFile(it) }
}
