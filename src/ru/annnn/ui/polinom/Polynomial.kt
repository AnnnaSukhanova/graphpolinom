package ru.annnn.ui.polinom

import java.lang.StringBuilder
import kotlin.math.abs
import kotlin.math.max

open class Polynomial(coeff: Collection<Double>) {

    infix fun Double.eq(other: Double) =
        abs(this - other) < max(Math.ulp(this), Math.ulp(other)) * 2
    infix fun Double.neq(other: Double) =
        abs(this - other) > max(Math.ulp(this), Math.ulp(other)) * 2


    private val _coeff: MutableList<Double> = mutableListOf()

    var coeff: List<Double>
        get() = _coeff.toList()
        set(value) {
            _coeff.clear()
            _coeff.addAll(value)
        }

    val degree: Int
        get() = _coeff.size - 1

    init {
        this._coeff.addAll(coeff)
        removeZeros()
    }
    constructor() : this(listOf(0.0))

    constructor(c: Array<Double>) : this(c.toList())


    constructor(vararg c: Double) : this(c.toList())


    private fun removeZeros() {
        var found = false
        _coeff.indices.reversed().forEach {
            if (_coeff[it] neq 0.0) found = true
            else if (!found) _coeff.removeAt(it)
        }
        if (_coeff.size == 0) _coeff.add(0.0)
    }
    fun diff():MutableList<Double>{
        var DiffCoef:MutableList<Double> = mutableListOf()
        for (i in 0..coeff.size-2){
            DiffCoef.add(_coeff[i]*(coeff.size-1-i))
        }
        return DiffCoef
    }
    override fun toString() =
        coeff.indices.reversed().joinToString(""){ ind ->
            val monStr = StringBuilder()
            val acoeff = abs(coeff[ind])
            if (coeff[ind] neq 0.0){
                if (ind < coeff.size - 1 && coeff[ind] > 0){
                    monStr.append("+")
                } else if (coeff[ind] < 0) monStr.append("-")
                if (acoeff neq 1.0)
                    if (abs(acoeff - acoeff.toInt().toDouble()) eq 0.0)
                        monStr.append(acoeff.toInt())
                    else monStr.append(acoeff)
                if (ind > 0) {
                    monStr.append("x")
                    if (ind > 1) monStr.append("^$ind")
                }
            } else {
                if (coeff.size == 1) monStr.append("0")
            }
            monStr
        }

    operator fun invoke(x: Double): Double{
        var p = 1.0
        return _coeff.reduce { res, d -> p *= x; res + d * p }
    }
    operator fun plus(other: Polynomial) =
        Polynomial(Array<Double>(max(degree, other.degree)+1)
        {
            (if (it <= degree) _coeff[it] else 0.0) +
                    if (it <= other.degree) other._coeff[it] else 0.0
        })

    operator fun plus(value: Double)  =
        Polynomial(
            Array<Double>(degree+1){ _coeff[it] + if (it == 0) value else 0.0}
        )

    operator fun plusAssign(other: Polynomial){
        for(i in 0..degree){
            _coeff[i] += if (i <= other.degree) other._coeff[i] else 0.0
        }
        for (i in degree+1..other.degree){
            _coeff.add(other._coeff[i])
        }
        removeZeros()
    }

    operator fun plusAssign(value: Double){
        _coeff[0] += value
    }

    operator fun minus(other: Polynomial) = this + other * -1.0

    operator fun minus(value: Double) = this + value * -1.0

    operator fun unaryMinus() = Polynomial(Array<Double>(degree){_coeff[it]*(-1.0)})


    operator fun minusAssign(other: Polynomial){
        this.plusAssign(-other)
    }

    operator fun minusAssign(value: Double){
        this.plusAssign(-value)
    }

    operator fun times(value: Double) =
        Polynomial(Array<Double>(degree + 1){_coeff[it] * value})


    operator fun timesAssign(value: Double){
        for(i in 0..degree){
            _coeff[i] *= value
        }
        removeZeros()
    }

    operator fun times(other: Polynomial) : Polynomial{
        val asl = Array<Double>(degree + other.degree +1){0.0}
        for (i in 0..degree){
            for (j in 0..other.degree){
                asl[i+j] += _coeff[i] * other._coeff[j]
            }
        }
        return  Polynomial(asl)
    }

    operator fun timesAssign(other: Polynomial){
        val asl = DoubleArray(degree + other.degree +1){0.0}
        for (i in 0..degree){
            for (j in 0..other.degree){
                asl[i+j] += _coeff[i] * other._coeff[j]
            }
        }
        _coeff.clear()
        _coeff.addAll(asl.toMutableList())
        removeZeros()
    }


    operator fun div(value: Double) =
        Polynomial(List(degree + 1){_coeff[it] / value})

    operator fun divAssign(value: Double){
        for(i in 0..degree){
            _coeff[i] /= value
        }
        removeZeros()
    }

    override operator fun equals (other: Any?) =
        (other is Polynomial) && (_coeff == other._coeff)   // умное  приведение типа

    override fun hashCode() = _coeff.hashCode()

}