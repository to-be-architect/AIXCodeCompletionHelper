package com.github.aixcode.utils


import com.github.aixcode.utils.TextDistanceUtil.getSimilarity

object TextDistanceUtil {
    /**
     *  Returns the Levenshtein distance between a and b, result 0~1
     */
    fun getLevenshteinDistance(X: String, Y: String): Int {
        val m = X.length
        val n = Y.length
        val T = Array(m + 1) { IntArray(n + 1) }
        for (i in 1..m) {
            T[i][0] = i
        }
        for (j in 1..n) {
            T[0][j] = j
        }
        var cost: Int
        for (i in 1..m) {
            for (j in 1..n) {
                cost = if (X[i - 1] == Y[j - 1]) 0 else 1
                T[i][j] = Integer.min(Integer.min(T[i - 1][j] + 1, T[i][j - 1] + 1),
                    T[i - 1][j - 1] + cost)
            }
        }
        return T[m][n]
    }

    fun getSimilarity(x: String, y: String): Double {
        val maxLength = java.lang.Double.max(x.length.toDouble(), y.length.toDouble())
        return if (maxLength > 0) {
            // 如果需要，可以选择忽略大小写
            (maxLength - getLevenshteinDistance(x, y)) / maxLength
        } else 1.0
    }
}

fun main() {
    println(getSimilarity("c", "cql")) // 0.3333333333333333
    println(getSimilarity("cq", "cql")) // 0.6666666666666666
    println(getSimilarity("cql", "cql")) // 1.0
}