package ru.annnn.ui.painting
import Plane
import java.awt.*
import ru.annnn.ui.polinom.Polynomial

open class FunctionPainter(
    private val plane: Plane,
    ) : Painter {

    private var funColor = Color.BLUE
    fun setColor(newColor: Color) {funColor = newColor}

    private val polynomials = mutableListOf<Polynomial>()

    override fun paint(g:Graphics) {
        with(g as Graphics2D) {
            color = funColor
            stroke = BasicStroke(4F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
            val rh = mapOf(
                RenderingHints.KEY_ANTIALIASING to RenderingHints.VALUE_ANTIALIAS_ON,
                RenderingHints.KEY_INTERPOLATION to RenderingHints.VALUE_INTERPOLATION_BICUBIC,
                RenderingHints.KEY_RENDERING to RenderingHints.VALUE_RENDER_QUALITY,
                RenderingHints.KEY_DITHERING to RenderingHints.VALUE_DITHER_ENABLE
            )
            setRenderingHints(rh)
            with(plane) {
                polynomials.forEach { polynomial ->
                    for (i in (0..width)) {
                        val x0 = xScr2Crt(i)
                        val x1 = xScr2Crt(i + 1)
                        drawLine(
                            i, yCrt2Scr(polynomial.invoke(x0)),
                            i + 1, yCrt2Scr(polynomial.invoke(x1))
                        )
                    }
                }
            }
        }
    }
    fun addPolynomial(polynomial: Polynomial){
        polynomials.add(polynomial)
    }

    fun removePolynomial(polynomial: Polynomial){
        polynomials.remove(polynomial)
    }

    fun removeAll(){
        polynomials.clear()
    }


}