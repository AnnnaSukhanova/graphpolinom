package ru.annnn.ui.painting

import Plane
import java.awt.*

class ParametricFunPainter(var x: (Double)->Double, var y: (Double)->Double,val plane: Plane) : Painter
{
    var funColor: Color = Color.DARK_GRAY


    override fun paint(g: Graphics){
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
                var i = tMin
                val dt = (tMax-tMin)/1000 //вычисляем на сколько отрезочков разделим прямую, дальше строим линию аналогично
                while (i<= tMax) {
                    drawLine(
                        xCrt2Scr(x(i)),
                        yCrt2Scr(y(i)),
                        xCrt2Scr(x(i+dt)),
                        yCrt2Scr(y(i+dt)),
                    )
                    i = i.plus(dt)
                }
            }

        }
    }
}