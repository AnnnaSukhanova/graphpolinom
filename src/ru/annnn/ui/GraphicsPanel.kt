package ru.annnn.ui
import ru.annnn.ui.painting.CartesianPainter
import ru.annnn.ui.painting.FunctionPainter
import ru.annnn.ui.painting.Painter
import java.awt.Graphics
import javax.swing.JPanel

class GraphicsPanel(private val painters: List<Painter>
) : JPanel() {

    override fun paint(g: Graphics?) {
        super.paint(g)
        g?.let {
            painters.forEach { p -> p.paint(it) }
        }
    }
}
