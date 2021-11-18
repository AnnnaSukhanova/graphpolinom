package ru.annnn.ui.painting

import java.awt.*
import Plane

class PointPainter(public val plane: Plane) : Painter {

    var pointColor: Color = Color.RED
    var point = mutableMapOf<Double,Double>()

    override fun paint(g: Graphics){
        with(g as Graphics2D) {
            color = pointColor
            var radius = 6
            stroke = BasicStroke(4F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
            val rh = mapOf(
                RenderingHints.KEY_ANTIALIASING to RenderingHints.VALUE_ANTIALIAS_ON,
                RenderingHints.KEY_INTERPOLATION to RenderingHints.VALUE_INTERPOLATION_BICUBIC,
                RenderingHints.KEY_RENDERING to RenderingHints.VALUE_RENDER_QUALITY,
                RenderingHints.KEY_DITHERING to RenderingHints.VALUE_DITHER_ENABLE
            )
            setRenderingHints(rh)
            with(plane) {
                for(i in 0..point.size-1){
                    fillOval(xCrt2Scr( point.keys.elementAt(i))-radius,plane.yCrt2Scr(point.values.elementAt(i))-radius,radius*2,radius*2)
                }
            }
        }
    }

}