package ru.annnn.ui.painting
import Plane
import java.awt.*

open class FunctionPainter(
    val plane: Plane,
    ) : Painter {

    var funColor: Color = Color.DARK_GRAY
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
                        drawLine(
                            i,
                            yCrt2Scr(function(xScr2Crt(i))),
                            i + 1,
                            yCrt2Scr(function(xScr2Crt(i + 1))),
                        )
                    }
                }
            }
        }
    }

}
