import java.lang.Exception

class Substance(private val asString: String) {
    val items = parse(asString)
    override fun toString(): String {
        return asString
    }

    private fun incInMap(map: MutableMap<String, Int>, key: String, increment: Int) {
        if (map[key] == null) {
            map[key] = increment
        } else {
            map[key] = map[key]!!.plus(increment)
        }
    }

    private fun parse(str: String): Map<String, Int> {
        if (!checkParentheses(str)) throw Exception("Неправильная расстановка скобок")
        val map = mutableMapOf<String, Int>()
        var curChain = ""
        var i = 0
        while (i < str.length) {
            val c = str[i]
            if (i == str.length - 1) {
                if (c.isDigit()) {
                    incInMap(map, curChain, c.digitToInt())
                } else if (c.isUpperCase() and curChain.isEmpty()) {
                    incInMap(map, c.toString(), 1)
                } else if (c.isUpperCase() and curChain.isNotEmpty()) {
                    incInMap(map, curChain, 1)
                    incInMap(map, c.toString(), 1)
                } else if (c.isLowerCase() and curChain.isEmpty()) {
                    throw Exception("Строчная буква после цифры/в начале формулы")
                } else if (c.isLowerCase()) {
                    curChain += c
                    incInMap(map, curChain, 1)
                }
            } else {
                if (c.isUpperCase() and curChain.isEmpty()) {
                    curChain = c.toString()
                } else if (c.isUpperCase() and curChain.isNotEmpty()) {
                    incInMap(map, curChain, 1)
                    curChain = c.toString()
                } else if (c.isLowerCase() and curChain.isEmpty()) {
                    throw Exception("Строчная буква после цифры/в начале формулы")
                } else if (c.isLowerCase()) {
                    curChain += c
                } else if (c.isDigit() and curChain.isEmpty()) {
                    throw Exception("Индекс, не относящийся ни к чему")
                } else if (c.isDigit()) {
                    val (inc, lastIndex) = processDigitFrom(str, i)
                    incInMap(map, curChain, inc)
                    i = lastIndex
                    curChain = ""
                } else if (c == '(') {
                    if (curChain.isNotEmpty())
                        incInMap(map, curChain, 1)
                    curChain = ""
                    val (thatMap, closingIndex) = processGroupFrom(str, i)
                    if (!str[closingIndex + 1].isDigit()) throw Exception("Не тем заканчивается скобка")
                    val (inc, lastIndex) = processDigitFrom(str, closingIndex + 1)
                    for (el in thatMap) {
                        incInMap(map, el.key, el.value * inc)
                    }

                    i = lastIndex

                }
            }
            i++
        }
        return map
    }

    private fun processGroupFrom(str: String, i: Int): Pair<Map<String, Int>, Int> {
        var deep = 1
        var saveJ = -1
        for (j in i + 1 until str.length) {
            if (str[j] == '(')
                deep += 1
            if (str[j] == ')') {
                deep -= 1
                if (deep == 0) {
                    saveJ = j
                }
            }
        }
        return Pair(parse(str.substring(i + 1, saveJ)), saveJ)
    }

    private fun processDigitFrom(str: String, i: Int): Pair<Int, Int> {
        var curChain = ""
        for (j in i until str.length) {
            if (str[j].isDigit())
                curChain += str[j]
            else {
                return Pair(curChain.toInt(), j - 1)
            }
        }
        return Pair(curChain.toInt(), str.length - 1)
    }


    private fun checkParentheses(str: String): Boolean {
        var deep = 0
        for (c in str) {
            if (c == '(')
                deep += 1
            if (c == ')')
                deep -= 1
            if (deep < 0)
                return false
        }
        if (deep != 0)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Substance

        if (asString != other.asString) return false

        return true
    }

    override fun hashCode(): Int {
        return asString.hashCode()
    }
}