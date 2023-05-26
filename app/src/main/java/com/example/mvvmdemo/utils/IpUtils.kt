package com.example.mvvmdemo.utils

import android.content.Context
import android.net.wifi.WifiManager
import com.example.mvvmdemo.app.App
import java.net.InetAddress
import java.net.NetworkInterface
import java.net.SocketException
import java.util.*

/**
 * @description
 * @author Raymond
 * @date 2023/5/26
 *
 */
object IpUtils {

    fun getIp(): String? {
        val wifiManager =
            App.instance.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        if (wifiManager.isWifiEnabled) {
            val info = wifiManager.connectionInfo
            val ipAddress = info.ipAddress
            return intToIp(ipAddress)
        } else {
            return getIpAddress()
        }
    }

    fun intToIp(ipAddress: Int) = (ipAddress and 0xFF).toString() + "." +
            (ipAddress shr 8 and 0xFF) + "." +
            (ipAddress shr 16 and 0xFF) + "." +
            (ipAddress shr 24 and 0xFF)

    /**
     * 获取本机IPv4地址
     *
     * @return 本机IPv4地址；null：无网络连接
     */
    private fun getIpAddress(): String? {
        return try {
            var networkInterface: NetworkInterface
            var inetAddress: InetAddress
            val en: Enumeration<NetworkInterface> = NetworkInterface.getNetworkInterfaces()
            while (en.hasMoreElements()) {
                networkInterface = en.nextElement()
                val enumIpAddr: Enumeration<InetAddress> = networkInterface.getInetAddresses()
                while (enumIpAddr.hasMoreElements()) {
                    inetAddress = enumIpAddr.nextElement()
                    if (!inetAddress.isLoopbackAddress && !inetAddress.isLinkLocalAddress) {
                        return inetAddress.hostAddress
                    }
                }
            }
            null
        } catch (ex: SocketException) {
            ex.printStackTrace()
            null
        }
    }
}