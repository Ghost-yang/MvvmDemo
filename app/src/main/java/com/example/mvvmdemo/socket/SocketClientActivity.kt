package com.example.mvvmdemo.socket

import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.example.composedemo.base.BaseSimpleVBActivity
import com.example.mvvmdemo.R
import com.example.mvvmdemo.databinding.ActivitySocketSocketBinding
import com.example.mvvmdemo.utils.IpUtils
import com.example.mvvmdemo.utils.click
import com.orhanobut.logger.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.Socket

/**
 * @description
 * @author Raymond
 * @date 2023/5/24
 *
 */
class SocketClientActivity : BaseSimpleVBActivity<ActivitySocketSocketBinding>() {
    private lateinit var socket: Socket

    private lateinit var udpScoket: DatagramSocket

    private var serverIp = "127.0.0.1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run {
            btnTcpSend.setOnClickListener {
                createTcp()
            }

            btnUdpSend.click {
                createUdp()
            }
            edtServerIp.addTextChangedListener {
                serverIp = it.toString()
            }
        }
    }

    private fun createTcp() {
        lifecycleScope.launch {
            val content = withContext(Dispatchers.IO) {
                var contentStr = ""
                try {
                    Logger.d("start connect socket--" + Thread.currentThread().name)
                    socket = Socket(serverIp, serverPort)
                    Logger.d("connect statu--" + socket.isConnected)
                    //发送数据
                    sendData("客户端消息")
                    //接受数据
                    val inputStream = socket.getInputStream()
                    val buffer = BufferedReader(inputStream.reader(), 1024)
                    contentStr = buffer.readLine()
                    // Logger.d("好的，我收到了你的回复：${buffer.readLine()}")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                contentStr
            }
            binding.tvMessage.text = getString(R.string.tv_server_back_message, content.toString())
            /* thread {
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
        }*/
        }
    }

    private fun createUdp() {
        lifecycleScope.launch {
            val recContent = withContext(Dispatchers.IO) {
                var rec = ""
                try {
                    Logger.d("start send udp--" + Thread.currentThread().name)
                    val data = "你好，UDP服务端"
                    val bytes = data.toByteArray()
                    val intAddress = InetAddress.getByName(serverIp)
                    val packet = DatagramPacket(bytes, bytes.size, intAddress, serverPort)
                    Logger.d("send packet--address${intAddress}--port-->${serverPort}")
                    udpScoket = DatagramSocket()
                    udpScoket.send(packet)

                    val recBytes = ByteArray(1024)
                    val recPacket = DatagramPacket(recBytes, recBytes.size)
                    udpScoket.receive(recPacket)
                    rec = recBytes.toString()
                    rec

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            binding.tvMessage.text =
                getString(R.string.tv_server_back_message, recContent.toString())
        }
    }


    private fun sendData(data: String) {
        val outputStream = socket.getOutputStream()
        val buffer = BufferedWriter(outputStream.writer(), 1024)
        buffer.write(data)
        buffer.newLine()
        buffer.flush()
    }

    private fun close() {
        if (this::socket.isInitialized) {
            socket.close()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        close()
    }

    companion object {
        const val serverPort = 42519
    }

}