import java.awt.Dimension
import kotlin.math.max

class Plane(//добавляем параметры(без размеров панели)
     xMin:Double,
     yMin:Double,
     xMax:Double,
     yMax:Double
) {// панель

    var pixelSize: Dimension=Dimension(1,1)
        set(size){
            field=Dimension(max(1, size.width), max(1, size.height))
        }

   private var xSize: Int
        get() =pixelSize.width
        set(w) {pixelSize.width=w}

    private var ySize: Int
        get() =pixelSize.height
        set(h) {pixelSize.height=h}

   var width:Int
   get()=xSize-1
    set(value){
        xSize=max(1,value)
    }
    var height:Int
        get()=ySize-1
        set(value){
            ySize=max(1,value)
        }

    var xMin:Double=0.0
        private set
    var yMin:Double=0.0
        private set
    var xMax:Double=0.0
        private set
    var yMax:Double=0.0
        private set

    var xSegment:Pair<Double,Double>
        get()=Pair(xMin,xMax)
        set(value) {
            val xk = if (value.first==value.second) 0.1 else 0.0
            if (value.first<=value.second){
                this.xMin=value.first-xk
                this.xMax=value.second+xk
            }
            else {
                this.xMax=value.first
                this.xMin=value.second
            }
        }
    var ySegment:Pair<Double,Double>
        get()=Pair(yMin,yMax)
        set(value) {
            val yk = if (value.first==value.second) 0.1 else 0.0
            if (value.first<=value.second){
                this.yMin=value.first-yk
                this.yMax=value.second+yk
            }
            else {
                this.yMax=value.first
                this.yMin=value.second
            }
        }

    init {
        xSegment=Pair(xMin,xMax)
        ySegment=Pair(yMin,yMax)
    }


    /**
     *плотность экранных точек по осям
     */
    val xDen:Double
        get() = width/(xMax-xMin)

    val yDen:Double
        get() = height/(yMax-yMin)

    /**
     * преобразование абсциссы из декартовой системы координат в экранную
     */

   fun xCrt2Scr(x: Double): Int{
        var r=(xDen*(x-xMin)).toInt()
        if (r<-width) r=-width
        if (r>2*width) r=2*width
        return r
    }

    fun yCrt2Scr(y: Double): Int{
     var r=(yDen*(yMax-y)).toInt()
        if (r<-height) r=-height
        if (r>2*height) r=2*height
        return r
    }


        fun  xScr2Crt(x: Int): Double{
        var _x=x
        if (_x<-width) _x=-width
        if(_x>2*width) _x=2*width
        return _x/xDen+xMin
    }

    fun yScr2Crt(y: Int): Double{
        var _y=y
        if (_y<-height) _y-width
        if(_y>2*width) _y=2*width
        return yMax-_y/yDen
    }

}

