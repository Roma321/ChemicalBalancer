import math.Fraction
import math.Gauss

class Equation {
    var left: List<Substance>
    var right: List<Substance>


    lateinit var coefVector: List<Int>
    lateinit var commonSolution: String

    private fun extractElements(list: List<Substance>): Set<String> {
        return list.map { it.items.keys }.flatten().toSet()
    }

    constructor(str: String) {
        val arr = str.split("=")
        left = arr[0].split("+").map { Substance(it.trim()) }
        right = arr[1].split("+").map { Substance(it.trim()) }
    }

    constructor(left: List<Substance>, right: List<Substance>) {
        this.left = left
        this.right = right
    }


    fun shuffle() {
        left = left.shuffled()
        right = right.shuffled()
    }


    fun balance() {
        val extractedLeft = extractElements(left)
        val extractedRight = extractElements(right)
        if (extractedLeft != extractedRight)
            throw Exception(
                "Невозможно сбалансировать. В левой части присутствуют элементы, которых нет в правой (${
                    extractedLeft.minus(
                        extractedRight
                    )
                }) или в правой части присутствуют элементы, которых нет в левой (${extractedRight.minus(extractedLeft)})"
            )

        val matrix: Array<Array<Fraction>> = buildMatrix()
        //println(str(matrix))
        //val gauss = Gauss(matrix, extractElements(left).toList())//TODO здесь не уверен про порядок
        val gauss = Gauss(matrix, (left + right).map { it.toString() })
        gauss.solveMatrix()
        gauss.setCoefLeastInteger()
        gauss.setMainDiagonalPositive()
        val pair = gauss.getAllRoots()
        val (common, minimal) = pair
        //println(common)
        coefVector = minimal
        commonSolution = common
    }

    private fun buildMatrix(): Array<Array<Fraction>> {
        val a: MutableList<Array<Fraction>> = mutableListOf()
        for (element in extractElements(left)) {//Для каждого элемента
            val string = mutableListOf<Fraction>()
            for (leftElement in left) { // Выписываем его количество
                string.add(Fraction(leftElement.items.getOrElse(element) { 0 }))
            }
            for (rightElement in right) { // Выписываем его количество
                string.add(Fraction(-rightElement.items.getOrElse(element) { 0 }))
            }
            a.add(string.toTypedArray())
        }
        return a.toTypedArray()
    }

    override fun toString(): String {
        val sb = StringBuilder()
        val leftSize = left.size
        val substanceList = left + right
        for ((i, substance) in substanceList.withIndex()) {
            sb.append("${if (coefVector[i] != 1) coefVector[i] else ""}$substance")
            if (i == substanceList.size - 1) {
                continue
            } else if (i == leftSize - 1) {
                sb.append(" = ")
            } else {
                sb.append(" + ")
            }
        }
        return sb.toString()
    }
}
