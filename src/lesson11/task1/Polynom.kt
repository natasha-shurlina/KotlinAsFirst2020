@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import kotlin.math.pow

/**
 * Класс "полином с вещественными коэффициентами".
 *
 * Общая сложность задания -- средняя, общая ценность в баллах -- 16.
 * Объект класса -- полином от одной переменной (x) вида 7x^4+3x^3-6x^2+x-8.
 * Количество слагаемых неограничено.
 *
 * Полиномы можно складывать -- (x^2+3x+2) + (x^3-2x^2-x+4) = x^3-x^2+2x+6,
 * вычитать -- (x^3-2x^2-x+4) - (x^2+3x+2) = x^3-3x^2-4x+2,
 * умножать -- (x^2+3x+2) * (x^3-2x^2-x+4) = x^5+x^4-5x^3-3x^2+10x+8,
 * делить с остатком -- (x^3-2x^2-x+4) / (x^2+3x+2) = x-5, остаток 12x+16
 * вычислять значение при заданном x: при x=5 (x^2+3x+2) = 42.
 *
 * В конструктор полинома передаются его коэффициенты, начиная со старшего.
 * Нули в середине и в конце пропускаться не должны, например: x^3+2x+1 --> Polynom(1.0, 2.0, 0.0, 1.0)
 * Старшие коэффициенты, равные нулю, игнорировать, например Polynom(0.0, 0.0, 5.0, 3.0) соответствует 5x+3
 */
class Polynom(vararg coeffs: Double) {

    constructor(per: MutableList<Double>) : this(*per.toDoubleArray())

    private val listCoeffs = coeffs.toMutableList()
    private fun removingZeros(coffeesWithoutZeros: MutableList<Double>): MutableList<Double> {
        if (coffeesWithoutZeros.size == 1 && coffeesWithoutZeros[0] == 0.0) coffeesWithoutZeros
        else while (coffeesWithoutZeros.size != 0 && coffeesWithoutZeros[0] == 0.0)
            coffeesWithoutZeros.removeAt(0)

        return coffeesWithoutZeros
    }

    val size = removingZeros(listCoeffs).size
    val polinom = removingZeros(listCoeffs)


    /**
     * Геттер: вернуть значение коэффициента при x^i
     */
    fun coeff(i: Int): Double = polinom[size - i - 1]

    /**
     * Расчёт значения при заданном x
     */
    fun getValue(x: Double): Double {
        var result = 0.0
        for (i in 0 until size) {
            result += polinom[i] * x.pow(size - 1 - i)
        }
        return result
    }

    /**
     * Степень (максимальная степень x при ненулевом слагаемом, например 2 для x^2+x+1).
     *
     * Степень полинома с нулевыми коэффициентами считать равной 0.
     * Слагаемые с нулевыми коэффициентами игнорировать, т.е.
     * степень 0x^2+0x+2 также равна 0.
     */
    fun degree(): Int = size - 1

    /**
     * Сложение
     */
    operator fun plus(other: Polynom): Polynom {
        val result: MutableList<Double> = mutableListOf()
        val small: MutableList<Double>
        val big: MutableList<Double>
        if (size >= other.size) {
            big = polinom
            small = other.polinom
        } else {
            big = other.polinom
            small = polinom
        }

        var n = 0
        val difference = big.size - small.size
        for (i in 0 until big.size) {
            if (difference - i - 1 >= 0) result += big[i]
            else {
                result += small[n] + big[i]
                n++
            }
        }
        return Polynom(result)
    }

    /**
     * Смена знака (при всех слагаемых)
     */
    operator fun unaryMinus(): Polynom {
        val result: MutableList<Double> = mutableListOf()
        for (i in 0 until size)
            result += polinom[i] * (-1)
        return Polynom(result)
    }

    /**
     * Вычитание
     */
    operator fun minus(other: Polynom): Polynom {

        val result: MutableList<Double> = mutableListOf()
        val first: MutableList<Double> = polinom
        val second: MutableList<Double> = other.polinom

        if (first.size > second.size) {
            for (i in 0 until first.size - second.size) second.add(0, 0.0)
        } else for (i in 0 until second.size - first.size) first.add(0, 0.0)
        for (i in 0 until first.size) result += polinom[i] - other.polinom[i]
        return Polynom(result)
    }

    /**
     * Умножение
     */
    operator fun times(other: Polynom): Polynom {
        val resultMap = mutableMapOf<Int, Double>()
        val result: MutableList<Double> = mutableListOf()
        for ((power1, coefficient1) in polinom.withIndex()) {
            for ((power2, coefficient2) in other.polinom.withIndex()) {
                val power = size + other.polinom.size - power1 - power2 - 2
                val coefficient = coefficient1 * coefficient2
                resultMap[power] = (resultMap[power] ?: 0.0).plus(coefficient)
            }
        }
        for ((power, coefficient) in resultMap) {
            result += coefficient
        }
        return Polynom(result)
    }


    /**
     * Деление
     *
     * Про операции деления и взятия остатка см. статью Википедии
     * "Деление многочленов столбиком". Основные свойства:
     *
     * Если A / B = C и A % B = D, то A = B * C + D и степень D меньше степени B
     */
    operator fun div(other: Polynom): Polynom {

        return when {
            polinom.size < other.polinom.size -> Polynom(0.0)
            other.polinom.size == size -> return when {
                polinom[0] < other.polinom[0] -> Polynom(0.0)
                else -> Polynom(polinom[0] / other.polinom[0])
            }
            else -> {
                val result: MutableList<Double> = mutableListOf()
                val perem: MutableList<Double> = mutableListOf()
                perem += polinom[0]
                var count = size
                var ex = polinom
                while (count >= other.size) {
                    val res = perem[0] / other.polinom[0]
                    result += res
                    perem.clear()
                    for (i in 0 until other.size) {
                        perem += ex[i] - (other.polinom[i] * res)
                        if (perem[0] == 0.0) perem.removeAt(0)
                    }
                    ex = perem
                    count = perem.size
                }
                return Polynom(result)
            }
        }
    }

    /**
     * Взятие остатка
     */
    operator fun rem(other: Polynom): Polynom = TODO()

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean = other is Polynom && polinom == other.polinom


    /**
     * Получение хеш-кода
     */
    override fun hashCode(): Int = polinom.hashCode()
}
