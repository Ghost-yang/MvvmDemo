package com.example.mvvmdemo.utils

import com.orhanobut.logger.Logger

/**
 * @description
 * @author Raymond
 * @date 2023/5/19
 *
 */
object VersionUtils {
    fun compareVersion(version1: String, version2: String): Int {
        version1.split(".").zipLongest(version2.split("."), "0")
            .onEach {
                with(it) {
                    if (first != second) {
                        return first.compareTo(second)
                    }
                }
            }.run {
                return 0
            }
    }

    /**
     * 优化with 嵌套
     *
     * @param version1
     * @param version2
     * @return
     */
    fun compareVersion2(version1: String, version2: String): Int {
        version1.split(".").zipLongest(version2.split("."), "0")
            .onEachWithReceiver {
                if (first != second) {
                    return first.compareTo(second)
                }
            }.run {
                return 0
            }
    }

    /**
     * "3x-3+2x+5x" ->3x -2 +2x +5x
     *
     * @param list
     */
    fun splitByOperator(list: String) =
        list.split(Regex("(?=[+-])")).filter {
            println("it-->${it}")
            it.isNotEmpty()
        }
}

private fun Iterable<String>.zipLongest(
    other: Iterable<String>,
    default: String
): List<Pair<Int, Int>> {
    val first = iterator()
    val second = other.iterator()
    val list = ArrayList<Pair<Int, Int>>(
        minOf(
            collectionSizeOrDefault(10),
            other.collectionSizeOrDefault(10)
        )
    )
    while (first.hasNext() || second.hasNext()) {
        val v1 = (first.nextOrNull() ?: default).toInt()
        val v2 = (second.nextOrNull() ?: default).toInt()
        list.add(Pair(v1, v2))
    }
    return list
}

private fun <T> Iterable<T>.collectionSizeOrDefault(default: Int): Int =
    if (this is Collection<*>) this.size else default


private fun <T> Iterator<T>.nextOrNull(): T? = if (hasNext()) next() else null

inline fun <T, C : Iterable<T>> C.onEachWithReceiver(action: T.() -> Unit): C {
    return apply {
        for (element in this) {
            action(element)
        }
    }
}


fun main() {
    val str = "3x-3+2x+5x"
    VersionUtils.splitByOperator(str)
}