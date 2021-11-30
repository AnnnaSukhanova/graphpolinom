package ru.annnn.ui.painting
import Plane
import java.awt.*

class CartesianPainter(private val plane: Plane) : Painter {

     var xColor: Color= Color.BLUE
     var yColor: Color= Color.RED
     var mainFont: Font= Font ("Cambria", Font.BOLD, 14)
    override fun paint(g:Graphics){
        paintAxes(g)
        paintTix(g)
        paintNumbers(g)
    }

  private fun paintAxes(g:Graphics) {
       with(plane){
           (g as Graphics2D).apply{
           stroke = BasicStroke(3F)

           if (xMin<=0 && xMax>=0)
           {
               color= yColor
               drawLine(xCrt2Scr(0.0),0,  xCrt2Scr(0.0), height)
           }
           if (yMin<=0 && yMax>=0)//если у=0 попадает в промежуток, то рисуем нормально
           {
               color= xColor
               drawLine(0, yCrt2Scr(0.0), width, yCrt2Scr(0.0))
           }
       } }
   }

    private fun paintTix(g:Graphics) {
        with(plane) {
            (g as Graphics2D).apply {

                g.color = Color.BLACK
                stroke = BasicStroke(1.5F)
                val dt = 4 // Половина длины черточки
                var x0 = xCrt2Scr(0.0) //точка начала координат
                var y0 = yCrt2Scr(0.0) //точка начала координат
                var y1= yCrt2Scr(0.0)
                var x1 = xCrt2Scr(0.0)

            if (x0 < 0){ // переносит разметку оси у вправо и влево
                x0 = 0
                x1=width}
            if (x0 > width){
                x0 = width
                x1=0}

            if (y0 < 0) {//переносит разметку оси х вверх и вниз
                y0 = 0
                y1=height}
            if (y0 > height) {
                y0 = height
                y1=0 }


         val kX = when { //когда мы меняем диапазон значений то присваиваем параметру i соответствующее значение
               xMax - xMin < 1 -> { 50.0 }
               xMax - xMin < 6 -> { 20.0 }
               xMax - xMin < 11 -> { 10.0 }
               xMax - xMin < 25 -> { 5.0 }
               xMax - xMin < 50-> { 2.5 }
               xMax - xMin < 120 && width < 1000 -> { 1.25  }
                else -> { 0.625} }
                for (i in (xMin*kX).toInt() .. (xMax*kX).toInt()) {//штрихи на оси х
                    val gap = if (i % 10 == 0) 10 else if (i % 5 == 0) 6 else 2 //длина штриха для каждой 10, 5 и прочих черточек
                    val x = xCrt2Scr(i / kX) //частота штриха
                    if ((yMin > 0) or (yMax < 0)) {
                        drawLine(x, y0 - dt - gap, x, y0 + dt + gap)
                        drawLine(x, y1 - dt - gap, x, y1 + dt + gap)
                    } else   drawLine(x, y0 - dt - gap, x, y0 + dt + gap)
                }

               val kY = when {
                   yMax - yMin < 1 -> { 50.0 }
                   yMax - yMin < 6 -> { 20.0 }
                   yMax - yMin < 11 -> { 10.0 }
                   yMax - yMin < 25 -> { 5.0 }
                   yMax - yMin < 50 -> { 2.5 }
                   yMax - yMin < 120 && height < 1000 -> { 1.25 }
                   else -> { 0.625 } }
                for (i in (yMin*kY).toInt() .. (yMax*kY).toInt()){//штрихи на у //не включая = антил
                    val gap = if (i % 10 == 0) 10 else if (i % 5 == 0) 6 else 2
                    val y = yCrt2Scr(i / kY)
                    if ((xMin > 0) or (xMax < 0)) {
                        drawLine(x0 - dt - gap, y, x0 + dt + gap, y)
                        drawLine(x1 - dt - gap, y, x1 + dt + gap, y)
                    } else drawLine(x0 - dt - gap, y, x0 + dt + gap, y)
                }
            }
        }
    }


   private fun paintNumbers(g:Graphics) {
        with(plane) {
            (g as Graphics2D).apply {
                stroke = BasicStroke(1F)
                color= Color.darkGray
                font = Font("Cambria", Font.BOLD, 10)

                var x0 = xCrt2Scr(0.0)
                var y0 = yCrt2Scr(0.0)
                var x1 = xCrt2Scr(0.0)
                var y1 = yCrt2Scr(0.0)
                var dtx=-12
                var dty=2

                if (x0 < 0){ //переносит числа вправо-влево
                    x0 = 0
                    x1=width }
                if (x0 > width){
                    x0 = width
                    x1=0}

                if (y0 < 0) {//переносит числа вверх и вниз
                    y0 = 0
                    y1=height}
                if (y0 > height) {
                    y0 = height
                    y1=0 }

                val kX = when {
                    xMax - xMin < 2 && width > 1000 -> { 50.0 }
                    xMax - xMin < 6 && width > 1000 -> { 20.0 }
                    xMax - xMin < 11 && width > 1000 -> { 10.0 }
                    xMax - xMin < 25 && width > 1000  -> { 5.0 }
                    xMax - xMin < 50 && width > 1000  -> { 2.5 }
                    xMax - xMin < 160 && width > 1000  -> { 1.25 }
                    xMax - xMin < 1 && width < 1000 -> { 50.0 }
                    xMax - xMin < 3 && width < 1000 -> { 20.0 }
                    xMax - xMin < 11 && width < 1000 -> { 10.0 }
                    xMax - xMin < 19 && width < 1000  -> { 5.0 }
                    xMax - xMin < 35 && width < 1000 -> { 2.5 }
                    xMax - xMin < 50 && width < 1000 -> { 1.25 }
                    xMax - xMin < 130 && width < 1000 -> { 0.625 }
                    xMax - xMin >= 130 && width < 1000 -> { 0.3125 }
                    else -> { 0.625}
                }
                for (i in ((xMin+0.05) * kX).toInt() until (xMax * kX).toInt()) {
                    if (i % 5 != 0 || i == 0) continue
                    val x = xCrt2Scr(i / kX)
                    val (tW,tH)= with(fontMetrics.getStringBounds((i / kX).toString(), g)) {
                        Pair(width.toInt(),height.toInt()) }
                    if ((y0 >= height - tH)|| (y0 <= 0 + tH))  dtx=  17
                    if (yMin > 0) {
                        drawString((i / kX).toString(), x - tW / 2, y0  - dtx) //цифры на нижней
                        drawString((i / kX).toString(), x - tW / 2, y1  + dtx*3/2) }//на верхней, с коэф чтобы красиво отображались
                     else if (yMax < 0){
                        drawString((i / kX).toString(), x - tW / 2, y0  + dtx*3/2) //цифры на нижней
                        drawString((i / kX).toString(), x - tW / 2, y1  - dtx) }
                    else if  (yMax == 0.0 )  drawString((i / kX).toString(), x - tW / 2, y0  + dtx*3/2)
                    else if  (yMin == 0.0 ) drawString((i / kX).toString(), x - tW / 2, y0  - dtx)
                     else   drawString((i / kX).toString(), x - tW / 2, y0 + tH - dtx )
                }

                val kY = when {
                    yMax - yMin < 1 && height > 1000 -> { 50.0 }
                    yMax - yMin < 3 && height > 1000-> { 20.0 }
                    yMax - yMin < 25 && height > 1000-> { 10.0 }
                    yMax - yMin < 40 && height >1000-> { 5.0 }
                    yMax - yMin < 50 && height > 1000-> { 2.5 }
                    yMax - yMin < 1 && height < 1000 -> { 50.0 }
                    yMax - yMin < 3 && height <  1000-> { 20.0 }
                    yMax - yMin < 11 && height <  1000-> { 10.0 }
                    yMax - yMin < 25 && height < 1000-> { 5.0 }
                    yMax - yMin < 50 && height <  1000-> { 2.5 }
                    yMax - yMin > 130 && height <  1000-> { 0.625 }
                    else -> { 1.25 }
                }
                for (i in ((yMin+0.05) * kY).toInt() until (yMax * kY).toInt()) {
                    if (i % 5 != 0 || i == 0) continue
                    val y = yCrt2Scr(i / kY)
                    val (tW,tH)= with(fontMetrics.getStringBounds((i / kY).toString(), g)) {
                        Pair(width.toInt(),height.toInt()) }
                    if ((x0 >= width - tW) || (x0 <= 0 + tW)) dty=  17
                    if (xMin > 0) {
                        drawString((i / kY).toString(), x0 + dty, y + tH / 2)
                        drawString((i / kY).toString(), x1 - dty*5/2, y + tH / 2) }
                    else if  (xMax < 0) {
                        drawString((i / kY).toString(), x0 -  dty*5/2, y + tH / 2)
                        drawString((i / kY).toString(), x1 + dty , y + tH / 2) }
                    else if  (xMax == 0.0 )  drawString((i / kY).toString(), x0 - dty*5/2, y + tH / 2)
                    else drawString((i / kY).toString(), x0 + tW - dty, y + tH / 2)
                }
            }
        }
   }
}