package math

class Fraction {
    var numerator = 0
    var denominator = 0

    constructor(numeratorValue: Int, denominatorValue: Int) {
        numerator = numeratorValue
        denominator = denominatorValue
    }

    constructor(numeratorValue: Int) {
        numerator = numeratorValue
        denominator = 1
    }

    constructor(str: String) {
        val arr = str.split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (arr.size == 2) {
            numerator = arr[0].toInt()
            denominator = arr[1].toInt()
            require(denominator != 0)
            if (denominator < 0) {
                denominator *= -1
                numerator *= -1
            }
        } else if (arr.size == 1) {
            numerator = arr[0].toInt()
            denominator = 1
        } else throw IllegalArgumentException()
    }

    operator fun plus(f2: Fraction): Fraction {
        if (numerator == 0) return Fraction(f2.numerator, f2.denominator)
        if (f2.numerator == 0) return Fraction(numerator, denominator)
        //новый знаменатель
        var newDenominator = nok(Math.abs(denominator), f2.denominator)
        //новый числитель: домножение на коэффициент, потом сумма
        var newNumerator = numerator * newDenominator / denominator + f2.numerator * newDenominator / f2.denominator
        if (newNumerator == 0) return Fraction(0, 1)
        if (newDenominator < 0) {
            newDenominator = -newDenominator
            newNumerator = -newNumerator
        }
        val divisor = nod(newDenominator, newNumerator)
        newDenominator /= divisor
        newNumerator /= divisor
        return Fraction(newNumerator, newDenominator)
    }

    operator fun minus(f2: Fraction): Fraction {
        return this.plus(f2.times(-1))
    }

    operator fun times(a: Int): Fraction {
        var newDenominator = denominator
        //новый числитель
        var newNumerator = numerator * a
        val divisor = nod(newDenominator, newNumerator)
        if (newNumerator == 0) return Fraction(0, 1)
        if (newDenominator < 0) {
            newDenominator = -newDenominator
            newNumerator = -newNumerator
        }
        newDenominator /= divisor
        newNumerator /= divisor
        return Fraction(newNumerator, newDenominator)
    }

    operator fun div(f2: Fraction): Fraction {
        //if (f2.numerator == 0) throw java.lang.ArithmeticException("/ by zero")
        if (numerator == 0) return Fraction(0, 1)
        val temp = this.times(Fraction(f2.denominator, f2.numerator))
        if (temp.denominator < 0) {
            temp.numerator = -temp.numerator
            temp.denominator = -temp.denominator
        }
        return temp
    }

    operator fun times(f2: Fraction): Fraction {
        //новый знаменатель
        var newDenominator = denominator * f2.denominator
        //новый числитель
        var newNumerator = numerator * f2.numerator
        if (newNumerator == 0) return Fraction(0, 1)
        if (newDenominator < 0 && newNumerator < 0) {
            newDenominator = -newDenominator
            newNumerator = -newNumerator
        }
        val divisor = nod(newDenominator, newNumerator)
        newDenominator /= divisor
        newNumerator /= divisor
        return Fraction(newNumerator, newDenominator)
    }



    override fun toString(): String {
        return if (denominator == 1) numerator.toString() else "$numerator/$denominator"
    }
}