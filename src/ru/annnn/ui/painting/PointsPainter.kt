package ru.annnn.ui.painting
import Plane
import java.awt.Color
import java.awt.Graphics
import java.awt.Graphics2D
import java.lang.Math.abs

data class Point(var x: Double, var y: Double)

class PointsPainter(private val plane: Plane , private var radius: Int) : Painter {

    private var points = mutableListOf<Point>() //список точек
    var pointcolor: Color = Color.RED
    fun setColor(newColor: Color) {pointcolor = newColor}
    
    override fun paint(g: Graphics) { //рисуем все точки из списка
        with(g as Graphics2D){
            color = pointcolor
            points.forEach { point ->
                fillOval(plane.xCrt2Scr(point.x) - radius,
                    plane.yCrt2Scr(point.y) - radius, radius * 2, radius * 2) }
        }
    }

    fun addPoint(xScr: Int, yScr: Int) { //добавить точку
        points.add(Point(plane.xScr2Crt(xScr), plane.yScr2Crt(yScr)))
    }
    
    fun getPoints(): MutableList<Point> {return points.toMutableList()}

    fun deletePoint(xScr: Int, yScr: Int) {

        val x = plane.xScr2Crt(xScr)
        val y = plane.yScr2Crt(yScr)
        var crtRad = plane.xScr2Crt(radius) - plane.xScr2Crt(0) // Радиус в декартовых координат
        var pointDeleter: Point? = null
        points.forEach { point ->
            if (abs(x - point.x) < crtRad && (abs(y - point.y) < crtRad)) {//если при клике попадаем в диапазон радиуса то удаляем точку
                pointDeleter = point
                return@forEach
            }
        }
        if (pointDeleter != null) points.remove(pointDeleter!!) //передаем сюда значение точки + мы знаем что оно не null так что можно использовать !!
    }
}