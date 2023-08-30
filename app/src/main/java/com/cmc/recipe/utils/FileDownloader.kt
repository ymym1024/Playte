package com.cmc.recipe.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import com.cmc.recipe.BuildConfig
import okhttp3.*
import java.io.File
import java.io.IOException

class FileDownloaderAndOpener(private val context: Context, private val url: String) {
    private val client = OkHttpClient()
    private val mainHandler = Handler(Looper.getMainLooper())
    private val downloadDir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
    val fileName = "${BuildConfig.FILE_NAME}"

    fun downloadAndOpenFile() {
        val request = Request.Builder()
            .url(url)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val localSavePath = File(downloadDir, fileName) // 파일명 설정

                    response.body?.let { responseBody ->
                        localSavePath.outputStream().use { output ->
                            responseBody.byteStream().use { input ->
                                input.copyTo(output)
                            }
                        }
                        mainHandler.post {
                            showToast("파일이 성공적으로 다운로드 되었습니다.")
                            openFile(localSavePath)
                        }
                    }
                } else {
                    showToast("파일 다운로드에 실패했습니다.")
                }
            }
        })
    }

    private fun openFile(file: File) {
        val fileUri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(fileUri, "application/pdf")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            showToast("파일을 열 수 있는 앱이 설치되어 있지 않습니다.")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
