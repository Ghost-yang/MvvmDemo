package com.example.mvvmdemo.socket

import android.os.Bundle
import com.example.composedemo.base.BaseSimpleVBActivity
import com.example.mvvmdemo.R
import com.example.mvvmdemo.databinding.ActivitySocketServerBinding
import com.example.mvvmdemo.utils.IpUtils
import com.example.mvvmdemo.utils.click
import com.orhanobut.logger.Logger
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.DataInputStream
import java.io.DataOutputStream
import java.net.DatagramPacket
import java.net.DatagramSocket
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

    lateinit var udpSocket: DatagramSocket
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.run {
            tvIp.text = getString(R.string.tv_server_ip, IpUtils.getIp())

            btnStartTcp.click {
                createTcpServer()
            }

            btnStartUdp.click {
                createUdpServer()
            }
        }

    }

    private fun createTcpServer() {
        thread {
            try {
                val server = ServerSocket(serverPort)
                socket = server.accept()
                Logger.d("------accept thread" + Thread.currentThread().name)
                val inputStream = socket.getInputStream()
                val bufferInputStream = BufferedReader(inputStream.reader())
                val content = bufferInputStream.readLine()
                Logger.d("rec--->$content")

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

    private fun createUdpServer() {
        thread {
            try {
                Logger.d("create udp server")
                val bytes = ByteArray(1024)
                //创建接受数据包
                val packet = DatagramPacket(bytes, bytes.size)
                udpSocket = DatagramSocket(serverPort)
                udpSocket.receive(packet)
                Logger.d(
                    "rec content-->${
                        String(
                            bytes,
                            0,
                            packet.length
                        )
                    }--ip${packet.address.hostAddress}--port${packet.port}"
                )
                //创建发送数据包
                /* val sendPacket = DatagramPacket(bytes, bytes.size*//*, packet.address, packet.port*//*)
                udpSocket.send(sendPacket)*/

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::socket.isInitialized) {
            socket.close()
        }
    }

    companion object {
        const val serverPort = 42519
    }
}