package com.example.mvvmdemo.socket

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.composedemo.base.BaseSimpleVBActivity
import com.example.mvvmdemo.base.BaseSimpleVBFragment
import com.example.mvvmdemo.databinding.ActivitySocketSocketBinding
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.IOException
import java.net.Socket
import kotlin.concurrent.thread

/**
 * @description
 * @author Raymond
 * @date 2023/5/24
 *
 */
class SocketClientActivity : BaseSimpleVBActivity<ActivitySocketSocketBinding>() {
    private lateinit var socket: Socket

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.btnSend.setOnClickListener {
            create()
        }
    }

    fun create() {
        /*  lifecycleScope.launch {
              val content = withContext(Dispatchers.IO) {
                  try {
                      Logger.d("start connect socket--" + Thread.currentThread().name)
                      socket = Socket(serverIp, serverPort)
                      Logger.d("connect statu--" + socket.isConnected)
                      //发送数据
                      sendData("客户端消息")
                      //接受数据
                      val inputStream = socket.getInputStream()
                      val buffer = BufferedReader(inputStream.reader(), 1024)
                      buffer.readLine()
                  } catch (e: Exception) {
                      e.printStackTrace()
                  }
              }
              binding.tvMessage.text = content.toString()*/
        thread {
            try {
                Logger.d("start connect socket--" + Thread.currentThread().name)
                socket = Socket(serverIp, serverPort)
                Logger.d("connect statu--" + socket.isConnected)
                //发送数据
                sendData("客户端消息")
                //接受数据
                val inputStream = socket.getInputStream()
                val buffer = BufferedReader(inputStream.reader(), 1024)
                Logger.d("好的，我收到了你的回复：${buffer.readLine()}")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    fun sendData(data: String) {
        val outputStream = socket.getOutputStream()
        val buffer = BufferedWriter(outputStream.writer(), 1024)
        buffer.write(data)
        buffer.newLine()
        buffer.flush()
    }

    fun close() {
        socket.close()
    }

    override fun onDestroy() {
        super.onDestroy()
        close()
    }

    companion object {
        const val serverIp = "10.7.130.101"
        const val serverPort = 42519
    }


}