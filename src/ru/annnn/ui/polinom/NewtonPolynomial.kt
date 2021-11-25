package ru.annnn.ui.polinom

class NewtonPolynomial( private val points: MutableMap<Double,Double>): Polynomial(){

        var index : MutableMap<Double,Double> = mutableMapOf()
        var l = Polynomial(1.0) //сохраняем полином чтобы при добавлении точки заново не пересчитывать все значения
        init{
            for(i in 0 until points.size){
                index[points.keys.elementAt(i)] = points.values.elementAt(i) //записываем точки, которые передали чтобы потом с ними работать
            }
            var pol = Polynomial(points.values.elementAt(0)) // наш полином 0 степени

            for(i in 1 until index.size){ //по формуле ищем суммы
                l = l * Polynomial(-index.keys.elementAt(i-1),1.0)
                pol = pol +  l *  DividedDifferent(i) //сам наш полином
            }
            coeff = pol.coeff
        }

        fun DividedDifferent(k:Int) : Double
        {
            var f = 0.0
            for(i in 0..k){
                var z = 1.0
                for(j in 0..k){
                    if(j!=i){
                        z *=(index.keys.elementAt(i)-index.keys.elementAt(j))
                    }
                }
                f += index.values.elementAt(i)/z
            }
            return f
        }

        fun add(point :MutableMap<Double,Double>){ //передаем точки
            var pol = Polynomial()
            pol.coeff = this.coeff
            for(i in 0 until point.size){
                index[point.keys.elementAt(i)] = point.values.elementAt(i)
            }
            l = l * Polynomial(-index.keys.elementAt(index.size-2),1.0)
            pol = pol +  l * DividedDifferent(index.size-1)
            this.coeff = pol.coeff
        }

}

