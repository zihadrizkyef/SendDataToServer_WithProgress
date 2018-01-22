package com.android.gotosehat.registrasi_new

import android.os.Handler
import android.os.Looper
import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import java.io.IOException

/**
 * بِسْمِ اللهِ الرَّحْمٰنِ الرَّحِيْمِ
 * Created by zihadrizkyef on 15/09/17.
 */
class RequestBodyWithProgress(private val contentType: MediaType, private val mString: String, private val mListener: UploadCallbacks) : RequestBody() {

    override fun contentType(): MediaType? {
        return contentType
    }

    @Throws(IOException::class)
    override fun contentLength(): Long {
        return mString.length.toLong()
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val stringLength = mString.length
        val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
        val sis = mString.byteInputStream()
        var uploaded: Long = 0

        sis.use {
            val handler = Handler(Looper.getMainLooper())
            var read = it.read(buffer)
            while (read != -1) {
                handler.post(ProgressUpdater(uploaded, stringLength.toLong()))

                uploaded += read.toLong()
                sink.write(buffer, 0, read)
                read = it.read(buffer)
            }
        }
    }

    interface UploadCallbacks {
        fun onProgressUpdate(percentage: Int)
    }

    private inner class ProgressUpdater(uploaded: Long, total: Long) : Runnable {
        private var mUploaded: Long = 0
        private var mTotal: Long = 1

        init {
            mUploaded = uploaded
            mTotal = total
        }

        override fun run() {
            mListener.onProgressUpdate((100 * mUploaded / mTotal).toInt())
        }
    }

    companion object {
        private const val DEFAULT_BUFFER_SIZE = 2048
    }
}