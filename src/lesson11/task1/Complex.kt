@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import kotlin.math.pow
import java.lang.IllegalArgumentException

/**
 * Класс "комплексное число".
 *
 * Общая сложность задания -- лёгкая, общая ценность в баллах -- 8.
 * Объект класса -- комплексное число вида x+yi.
 * Про принципы работы с комплексными числами см. статью Википедии "Комплексное число".
 *
 * Аргументы конструктора -- вещественная и мнимая часть числа.
 */
class Complex(val re: Double, val im: Double) {

    /**
     * Конструктор из вещественного числа
     */
    constructor(x: Double) : this(x, 0.0)

    /**
     * Конструктор из строки вида x+yi
     */
    constructor(s: String) : this(toComplex(s).first, toComplex(s).second)

    companion object {
        fun toComplex(s: String): Pair<Double, Double> {
            val result =
                Regex("""(-?\d+(\.\d+)?)([+-]\d+(\.\d+)?)i""").matchEntire(s)
                    ?: throw IllegalArgumentException()
            return result.groupValues[1].toDouble() to result.groupValues[3].toDouble()
        }
    }

    /**
     * Сложение.
     */
    operator fun plus(other: Complex): Complex = Complex(this.re + other.re, this.im + other.im)

    /**
     * Смена знака (у обеих частей числа)
     */
    operator fun unaryMinus(): Complex = Complex(-re, -im)

    /**
     * Вычитание
     */
    operator fun minus(other: Complex): Complex = Complex(
        re * other.re - im * other.im,
        re * other.im + other.re * im
    )


    /**
     * Умножение
     */
    operator fun times(other: Complex): Complex = Complex(
        re * other.re - im * other.im,
        im * other.re + re * other.im
    )

    /**
     * Деление
     */
    operator fun div(other: Complex): Complex = Complex(
        (re * other.re + im * other.im) / (other.im.pow(2) + other.re.pow(2)),
        (im * other.re - re * other.im) / (other.im.pow(2) + other.re.pow(2))
    )


    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean =
        other is Complex && im == other.im && re == other.re

    /**
     * Преобразование в строку
     */
    override fun toString(): String = if (im >= 0) "$re+${im}i"
    else "$re${im}i"

    override fun hashCode(): Int = re.hashCode() * 31 + im.hashCode()
}
