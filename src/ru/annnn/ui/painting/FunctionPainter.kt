package ru.annnn.ui.painting
import Plane
import ru.annnn.ui.polinom.NewtonPolynomial
import java.awt.*
import ru.annnn.ui.polinom.Polynomial

open class FunctionPainter(
    private val plane: Plane,
    ) : Painter {

    private var funColor = Color.BLUE
    private var difColor = Color.GREEN
    private var pointColor = Color.RED
    fun setFunColor(newColor: Color) {funColor = newColor}
    fun setDifColor(newColor: Color) {difColor = newColor}
    fun setPointColor(newColor: Color) {pointColor = newColor}


    val epsilon = 0.001
    lateinit var pol: NewtonPolynomial
    lateinit var m:MutableMap<Int,Int>
    lateinit var diffpol:Polynomial
    var IsDiff = false


    fun add(x:Int,y:Int){
        if(::pol.isInitialized == false){
            pol = NewtonPolynomial(mutableMapOf(plane.xScr2Crt(x) to plane.yScr2Crt(y)))
            m = mutableMapOf(x to y)
        }
        else{
            var dontClick = false
            for(i in 0..m.size-1){
                if((x<m.keys.elementAt(i)+20) && (x>m.keys.elementAt(i)-20) && (y<m.values.elementAt(i)+20) && (y>m.values.elementAt(i)-20) || ((x<m.keys.elementAt(i)+20) && (x>m.keys.elementAt(i)-20))){
                    dontClick = true
                    break
                }
            }
            if(dontClick){}
            else{
                pol.add(mutableMapOf(plane.xScr2Crt(x) to plane.yScr2Crt(y)))
                m.put(x,y)
            }
        }
    }

    fun deletePoint(x:Int,y:Int){
        var m2:MutableMap<Double,Double> = mutableMapOf()
        for(i in 0..m.size-1){
            if((x<m.keys.elementAt(i)+20) && (x>m.keys.elementAt(i)-20) && (y<m.values.elementAt(i)+20) && (y>m.values.elementAt(i)-20)){
                m.remove(m.keys.elementAt(i))
                break
            }
        }
        for (i in 0..m.size-1){
            m2.put(plane.xScr2Crt(m.keys.elementAt(i)),plane.yScr2Crt(m.values.elementAt(i)))
        }
        var pol1 = NewtonPolynomial(m2)
        pol = pol1
    }

    fun Diff(){
        diffpol = Polynomial()
        diffpol.coeff = pol.diff()
    }

    override fun paint(g: Graphics){
        with(g as Graphics2D) {
            stroke = BasicStroke(4F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
            val rh = mapOf(
                RenderingHints.KEY_ANTIALIASING to RenderingHints.VALUE_ANTIALIAS_ON,
                RenderingHints.KEY_INTERPOLATION to RenderingHints.VALUE_INTERPOLATION_BICUBIC,
                RenderingHints.KEY_RENDERING to RenderingHints.VALUE_RENDER_QUALITY,
                RenderingHints.KEY_DITHERING to RenderingHints.VALUE_DITHER_ENABLE
            )
            setRenderingHints(rh)
            if (pol.coeff.size == 0){

            }
            else {
                with(plane) {
                    for (i in 0 until width) {
                        drawLine(
                            i,
                            yCrt2Scr(pol.invoke(xScr2Crt(i))),
                            i + 1,
                            yCrt2Scr(pol.invoke(xScr2Crt(i + 1))),
                        )
                        color = funColor
                    }

                    for(i in 0..pol.ind.size-1){
                        color = pointColor
                        fillOval(plane.xCrt2Scr( pol.ind.keys.elementAt(i))-7,plane.yCrt2Scr(pol.ind.values.elementAt(i))-7,14,14)
                    }

                    if(IsDiff){
                        for (i in 0 until width) {
                            drawLine(
                                i,
                                yCrt2Scr(diffpol.invoke(xScr2Crt(i))),
                                i + 1,
                                yCrt2Scr(diffpol.invoke(xScr2Crt(i + 1))),
                            )
                            color = difColor
                        }
                    }
                }
            }
        }
    }

}
