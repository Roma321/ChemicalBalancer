package math

import kotlin.math.abs

fun nok(m: Int, n: Int): Int {
    for (i in maxOf(n, m) until n * m + 1) {
        if (i % m == 0 && i % n == 0) {
            if (i != 0) {
                return i
            }
        }
    }
    return -1
}

fun nod(n: Int, d: Int): Int {
    var n = n
    var d = d
    n = abs(n)
    d = abs(d)
    if (d == 0) return n
    if (n == 0) return d
    while (d != 0 && n != 0) {
        if (n % d > 0) {
            val temp = n
            n = d
            d = temp % d
        } else break
    }
    return if (d != 0 && n != 0) d else -1
}


fun nodOfList(list: List<Int>): Int {
    var res = list[0]
    for (i in list) {
        res = nod(res, i)
    }
    return res
}

fun nokOfList(list: List<Int>): Int {
    var res = 1
    for (item in list) {
        res = nok(res, item)
    }
    return res
}