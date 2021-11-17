package ru.annnn.ui.polinom

import kotlin.math.abs
import kotlin.math.max
import kotlin.math.pow


open class Polynomial(coef: DoubleArray) : Comparable<Polynomial> {

    protected var coef: DoubleArray = coef.clone() //коэффициенты полинома

    val coeffitients: DoubleArray
        get() = coef.clone()

    val power: Int //степень полинома
        get() = coef.size - 1

    init {
        correctPower()
    }

    constructor() : this(doubleArrayOf(0.0)) //конструктор для создания полинома 0 степени

    private fun correctPower() { //удаление 0 коеф при старших степенях

        var b = true
        coef = coef.reversed().filterIndexed { i, v ->
            if (v.compareTo(0.0) != 0) b = false
            !b || (i == power)
        }.reversed().toDoubleArray()

    }

    operator fun plus(other: Polynomial) = //сложение 2 полиномов
        Polynomial(DoubleArray(max(power, other.power) + 1) {
            (if (it < coef.size) coef[it] else 0.0) +
                    (if (it < other.coef.size) other.coef[it] else 0.0)
        }
        )

    operator fun times(other: Polynomial): Polynomial { //умножение
        //Создание массива коэффициентов нового полинома
        val t = DoubleArray(power + other.power + 1) { 0.0 }
        //Для каждого коэффициента первого полинома и
        coef.forEachIndexed { ti, tc ->
            //коэффициента второго полинома
            other.coef.forEachIndexed { oi, oc ->
                t[ti + oi] += tc * oc
            }
        }
        // Создание нового полинома по рассчитанным коэффициентам
        return Polynomial(t)
    }

    operator fun times(k: Double) = //произведение полинома на число
        Polynomial(DoubleArray(power + 1) { coef[it] * k })


    operator fun minus(other: Polynomial) = //разность полиномов
        this + other * -1.0


    operator fun plusAssign(other: Polynomial) {
        coef = DoubleArray(max(power, other.power) + 1) {
            (if (it < coef.size) coef[it] else 0.0) +
                    (if (it < other.coef.size) other.coef[it] else 0.0)
        }
    }


    operator fun div(k: Double): Polynomial? { //деление полинома на число
        return times(1 / k)
    }

    override fun toString(): String {
        val out = StringBuilder()
        coef.reversed().forEachIndexed { i, v ->
            val j = power - i //Reversed indexes
            if (v != 0.0 || power == 0) {
                if (j == power) out.append(if (v >= 0.0) "" else "-")
                else out.append(if (v >= 0) "+ " else "- ")
                if (abs(v) != 1.0 || j == 0) out.append(if ((v.toLong() - v) != 0.0) abs(v) else abs(v.toLong()))
                if (j != 0) out.append("x")
                if (j > 1) out.append("^($j)")
                out.append(" ")
            }
        }
        return out.toString()
    }

    operator fun invoke(x: Double): Double {
        return coef.mapIndexed() { i, v -> v * x.pow(i) }.sum()

    }

    override fun compareTo(other: Polynomial): Int { //сравнение 2 полиномов
        when {
            coef.size > other.coef.size -> {
                return 1
            }
            coef.size < other.coef.size -> {
                return -1
            }
            else -> {
                for (i in coef.indices) {
                    if (coef[i] > other.coef[i]) return 1
                    if (coef[i] < other.coef[i]) return -1
                }
            }
        }
        return 0
    }

    fun derivative(): Polynomial { //производная
        val cfs = DoubleArray(coef.size-1)
        coef.forEachIndexed{i, x ->
            if (i != 0) cfs[i - 1] = x * i
        }
        return Polynomial(cfs)
    }

}