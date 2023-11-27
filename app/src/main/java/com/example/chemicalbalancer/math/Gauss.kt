package math

class Gauss(val data: Array<Array<Fraction>>, val variableNames: List<String>) {

    fun solveMatrix() {
        for ((i, string) in data.withIndex()) {
            if (string[i].numerator == 0) {
                if (endGauss(i)) return
                fixRowsOrderDown(i)
//                println(str(data))
            }
            for (j in data.indices) {
                if (j != i) {
                    val mul = data[j][i] / data[i][i]
                    val subtractedString = data[i].map { it * mul }
                    data[j] =
                        data[j].mapIndexed { index, fraction -> fraction - subtractedString[index] }.toTypedArray()
//                    println("Из ${j+1}-й строки вычитаем ${i+1}-ю")
//                    println(mul)
//                    println(subtractedString)
//                    println(str(data))
                }
            }

        }
    }

    fun getAllRoots(): Pair<String, List<Int>> {

        val tempData = data.filter { str -> str.any { it.numerator != 0 } }.toTypedArray()
        val freeVariables = tempData[0].size - tempData.size
       // println(freeVariables)
        if (freeVariables == 0) throw Exception("Невозможно уравнять")
        //println(str(tempData))
        val tempDataFree = tempData.map { it.filterIndexed { index, _ -> index >= data[0].size - freeVariables } }
       // println(tempDataFree)
        val stringBuilder = StringBuilder()
        val tempIndicesMatrix: MutableList<List<Fraction>> = mutableListOf()
        for (i in tempData.indices) {
            val sb = StringBuilder()
            sb.append("${variableNames[i]} = ")
            val dependingCoef = tempData[i][i]
            val stringIndices = Array(data[0].size) { Fraction(0) }
            stringIndices[i] = Fraction(1)
//            if (dependingCoef.numerator == 0){
//
//            }
            for ((idx, variable) in tempDataFree[i].withIndex()) {
                val freeCoef = variable * (-1) / dependingCoef
                stringIndices[data[0].size - freeVariables + idx] = freeCoef
                sb.append("$freeCoef*${variableNames[data[0].size - freeVariables + idx]} + ")
            }
            tempIndicesMatrix.add(stringIndices.toList())
            stringBuilder.append("${sb.toString().substring(0, sb.length - 3)}\n")
        }
        //println(tempIndicesMatrix)
        return Pair(stringBuilder.toString(), buildVector(tempIndicesMatrix))
    }

    private fun buildVector(tempIndicesMatrix: MutableList<List<Fraction>>): List<Int> {
        //println(tempIndicesMatrix)
        val freeVariables = tempIndicesMatrix[0].size - tempIndicesMatrix.size
        val coefVector = Array(tempIndicesMatrix[0].size) { 0 }
        for (i in 0 until freeVariables) {
            var myNok = 1
            //Для каждой переменной ищем минимальный индекс,
            // который можно взять, чтобы коэффициенты остались целыми
            for (j in tempIndicesMatrix.indices) {//идём по строкам
                myNok = nok(myNok, tempIndicesMatrix[j][tempIndicesMatrix.size + i].denominator)

            }
            coefVector[tempIndicesMatrix.size + i] = myNok
        }
        //println(coefVector.toList())
        for (i in 0 until tempIndicesMatrix.size) {
            var sum = 0
            for (j in 0 until freeVariables) {
                sum += (tempIndicesMatrix[i][tempIndicesMatrix.size + j] * coefVector[tempIndicesMatrix.size + j]).numerator
            }
            coefVector[i] = sum
        }
        //println(coefVector.toList())
        return coefVector.map { it/ nodOfList(coefVector.toList()) }
    }

    private fun zeroMap(variableNames: List<String>): String {
        val map = mutableMapOf<String, Fraction>()
        for (name in variableNames) {
            map[name] = Fraction(0)
        }
        return map.toString()
    }

    private fun fixRowsOrderDown(i: Int): Boolean {
        var stringForChangeFound = false
//        println(str(data))
        for (j in i + 1 until data.size) {
            if (data[j][i].numerator != 0) {
//                println(data[i].toList())
//                println(data[j].toList())
                val t = data[j]
                data[j] = data[i]
                data[i] = t
                stringForChangeFound = true
                break
            }
        }
//        println(str(data))
        if (!stringForChangeFound)
            throw Exception("Не удалось выполнить прямой ход метода Гаусса")
        return stringForChangeFound
    }

    fun setCoefLeastInteger() {
        for ((i, string) in data.withIndex()) {
            if (!string.all { it.numerator == 0 }) {
                val mul = nokOfList(string.filter { it.numerator != 0 }.map { it.denominator })
                data[i] = string.map { it * mul }.toTypedArray()
                val div = nodOfList(data[i].filter { it.numerator != 0 }.map { it.numerator })
                data[i] = data[i].map { it / Fraction(div) }.toTypedArray()
            }
        }
    }

    fun setMainDiagonalPositive() {
        for ((i, _) in data.withIndex()) {
            if (data[i][i].numerator < 0) {
                data[i] = data[i].map { it * (-1) }.toTypedArray()
            }
        }
    }

    private fun endGauss(i: Int): Boolean {
        if (i == data.size - 1) return true
        for (j in i + 1 until data.size) {
            if (data[j].all { it.numerator == 0 })
                return true
        }
        return false
    }

    override fun toString(): String {
        return str(data)
    }


}

fun str(data: Array<Array<Fraction>>): String {
    val a = StringBuilder()
    for (item in data) {
        for (item2 in item)
            a.append("$item2 ")
        a.append("\n")
    }
    return a.toString()
}