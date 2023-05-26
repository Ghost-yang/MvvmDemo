package com.example.mvvmdemo.socket

import android.os.Bundle
import com.example.composedemo.base.BaseSimpleVBActivity
import com.example.mvvmdemo.databinding.ActivitySocketServerBinding
import com.orhanobut.logger.Logger
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.ServerSocket
import java.net.Socket
import kotlin.concurrent.thread

/**
 * @description
 * @author Raymond
 * @date 2023/5/24
 *
 */
class SocketServerActivity : BaseSimpleVBActivity<ActivitySocketServerBinding>() {
    lateinit var socket: Socket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createServer()
    }

    private fun createServer() {
        thread {
            try {
                val server = ServerSocket(serverPort)
                socket = server.accept()
                Logger.d("------accept thread" + Thread.currentThread().name)
                val inputStream = socket.getInputStream()
                val bufferInputStream = BufferedReader(inputStream.reader())
                val content = bufferInputStream.readLine()
                Logger.d("rec--->" + content)

                val outputStream = socket.getOutputStream()
                val bufferedWriter = BufferedWriter(outputStream.writer())
                bufferedWriter.write("我收到你的消息了")
                bufferedWriter.newLine()
                bufferedWriter.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.close()
    }

    companion object {
        const val serverPort = 42519
    }
}