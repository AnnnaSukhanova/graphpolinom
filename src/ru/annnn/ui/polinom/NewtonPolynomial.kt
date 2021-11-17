package ru.annnn.ui.polinom

class NewtonPolynomial() : Polynomial() {

    private var points: ArrayList<Pair<Double, Double>> = arrayListOf()//список узлов для полинома

    private var divDifferenceData: MutableMap<Pair<Int, Int>, Double> = mutableMapOf() //поле содержащее разделенные разности

    constructor(points: ArrayList<Pair<Double, Double>>) : this() { //конструктор создающий полином
        points.forEach { point -> addNode(point.first, point.second) }
    }

    private fun divDifference(first: Int, last: Int): Double { //находим разделенную разность
        return when {
            // Если такая разделенная разность уже вычислена, возвращаем ее
            divDifferenceData.containsKey(Pair(first, last)) -> divDifferenceData[Pair(first, last)] ?: error("")
            else -> {
                if (last == first) {
                    // Если начальный и конечный индексы совпадают, то искомое значение - это значение функции в точке соответствующей индексу
                    divDifferenceData[Pair(first, first)] = points[last].second // Сохраняем вычисленную разделенную разность
                    return points[last].second // Возвращаем f(x)
                } // Иначе вычисляем по рекурентной формуле
                val left = divDifference(first + 1, last)
                val right = divDifference(first, last - 1)
                val difference = (left - right) / (points[last].first - points[first].first)
                divDifferenceData[Pair(first, last)] = difference // Сохраняем вычисленную разделенную разность
                return difference
            }
        }
    }

    fun addNode(x: Double, y: Double) { //добавить узел для интерполирования
        val base = Polynomial(coef)
        var p = Polynomial(doubleArrayOf(1.0))
        points.add(Pair(x, y))
        for (i in 0 until points.size) if (i != 0) p *= Polynomial(doubleArrayOf(-points[i - 1].first, 1.0))
        base += p * divDifference(0, points.size - 1)
        coef = base.coeffitients
    }
}