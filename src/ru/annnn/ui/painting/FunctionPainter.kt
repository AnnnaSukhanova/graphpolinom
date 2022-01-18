package ru.annnn.ui.painting
import Plane
import java.awt.*
import java.lang.Double.NaN

open class FunctionPainter(
    val plane: Plane,
    ) : Painter {

    var funColor: Color = Color.ORANGE
    lateinit var function: (Double)->Double

    override fun paint(g: Graphics){
        with(g as Graphics2D) {
            color = funColor
            stroke = BasicStroke(3F, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
            val rh = mapOf(
                RenderingHints.KEY_ANTIALIASING to RenderingHints.VALUE_ANTIALIAS_ON,
                RenderingHints.KEY_INTERPOLATION to RenderingHints.VALUE_INTERPOLATION_BICUBIC,
                RenderingHints.KEY_RENDERING to RenderingHints.VALUE_RENDER_QUALITY,
                RenderingHints.KEY_DITHERING to RenderingHints.VALUE_DITHER_ENABLE
            )
            setRenderingHints(rh)
            if(::function.isInitialized) {
                with(plane) {
                    for (i in 0 until width) {
                        var c=function(xScr2Crt(i))
                        var d = yCrt2Scr(function(xScr2Crt(i)))
                        if (c != NaN /*d!=0*/)
                        {
                        drawLine(
                            i,
                            yCrt2Scr(function(xScr2Crt(i))),
                            i + 1,
                            yCrt2Scr(function(xScr2Crt(i + 1))),
                        )
                        }
                }
            }}
        }
    }

}
