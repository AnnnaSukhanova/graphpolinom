package ru.annnn.ui
import ru.annnn.ui.painting.CartesianPainter
import ru.annnn.ui.painting.Painter
import java.awt.Color
import java.awt.Dimension
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import Plane
import ru.annnn.ui.polinom.NewtonPolynomial
import ru.annnn.ui.polinom.Polynomial
import ru.annnn.ui.painting.FunctionPainter
import ru.annnn.ui.painting.PointPainter
import java.awt.event.ItemListener


class MainFrame  : JFrame() {

    private val minDim = Dimension(600, 700)

    private val mainPanel: GraphicsPanel
    private val controlPanel: JPanel
    private val xMinM: SpinnerNumberModel
    private val yMinM: SpinnerNumberModel
    private val xMaxM: SpinnerNumberModel
    private val yMaxM: SpinnerNumberModel
    private val xMin: JSpinner
    private val yMin:JSpinner
    private val xMax: JSpinner
    private val yMax:JSpinner
    private val colorpanelPoint: JPanel
    private val colorpanelGraphic: JPanel
    private val colorpanelDerivative: JPanel
    private val textXMin: JLabel
    private  val textXMax: JLabel
    private val textYMin: JLabel
    private val textYMax: JLabel
    private val textPoint: JLabel
    private val textGraphic: JLabel
    private val textDerivative: JLabel
    private val checkboxPoint: JCheckBox
    private val checkboxGraphics: JCheckBox
    private val checkboxDerivative: JCheckBox


    init {
        defaultCloseOperation = EXIT_ON_CLOSE // Чтобы приложение завершало работу при закрытии
        minimumSize = minDim

        xMinM= SpinnerNumberModel(-5.0,-100.0,4.9,0.1)
        xMin= JSpinner(xMinM)
        yMinM= SpinnerNumberModel(-5.0,-100.0,4.9,0.1)
        yMin= JSpinner(yMinM)
        xMaxM= SpinnerNumberModel(5.0,-4.9,100.0,0.1)
        xMax= JSpinner(xMaxM)
        yMaxM=SpinnerNumberModel(5.0,-4.9,100.0,0.1)
        yMax= JSpinner(yMaxM)

        val mainPlane= Plane(xMinM.value as Double,yMinM.value as Double,xMaxM.value as Double,yMaxM.value as Double)
        var k = 0
        val cartesianPainter = CartesianPainter(mainPlane)
        var functionPainter = FunctionPainter(mainPlane)
        var derFunctionPainter = FunctionPainter(mainPlane)
        var pointPainter= PointPainter(mainPlane)
        val painters = mutableListOf<Painter>(cartesianPainter)
        derFunctionPainter.funColor = Color.GREEN
        lateinit var Polynom:NewtonPolynomial
        mainPanel=GraphicsPanel(painters).apply {
            background=Color.WHITE
        }

        checkboxPoint= JCheckBox()
        checkboxPoint.isSelected = true
        checkboxGraphics= JCheckBox()
        checkboxGraphics.isSelected = true
        checkboxDerivative= JCheckBox()
        checkboxDerivative.isSelected = true

        mainPanel.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?)
            {
                if(e?.point != null){
                    if(e.button == MouseEvent.BUTTON1) {
                        if(k == 0){ //если еще ничего не было нарисовано
                            Polynom = NewtonPolynomial(mutableMapOf(functionPainter.plane.xScr2Crt(e.point.x) to functionPainter.plane.yScr2Crt(e.point.y)))
                            pointPainter.point[functionPainter.plane.xScr2Crt(e.point.x)] = functionPainter.plane.yScr2Crt(e.point.y)
                            functionPainter.function = Polynom::invoke
                            var poll:Polynomial = Polynomial(Polynom.diff())
                            derFunctionPainter.function = poll::invoke //считаем полином в точке
//                            painters.addAll(mutableListOf(pointPainter))
                            painters.addAll(mutableListOf(derFunctionPainter))
                            painters.addAll(mutableListOf(functionPainter))
                            painters.addAll(mutableListOf(pointPainter))
                            k = 1
                        }
                        else{
                            var p = true
                            for(i in 0 until pointPainter.point.size){ //проверим куда нажали
                                if((e.point.x < functionPainter.plane.xCrt2Scr(pointPainter.point.keys.elementAt(i))+12 && e.point.x > functionPainter.plane.xCrt2Scr(pointPainter.point.keys.elementAt(i))-12)) {
                                    p = false
                                    break
                                }
                            }
                            if(p){ //если тыкаем не по уже нарисованной точке
                                Polynom.add(mutableMapOf(functionPainter.plane.xScr2Crt(e.point.x) to functionPainter.plane.yScr2Crt(e.point.y)))
                                pointPainter.point[functionPainter.plane.xScr2Crt(e.point.x)] = functionPainter.plane.yScr2Crt(e.point.y)
                                var poll:Polynomial = Polynomial(Polynom.diff()) //чтобы производная тоже строилась
                                derFunctionPainter.function = poll::invoke //считаем полином в точке
                            }
                        }
                        mainPanel.repaint()
                    }
                    if(e.button == MouseEvent.BUTTON3){
                        if(painters.size != 1){ //если есть что то кроме координат
                            for(i in 0 until pointPainter.point.size){ //проходимся по всем точкам
                                if((functionPainter.plane.xScr2Crt(e.point.x)+0.1 >pointPainter.point.keys.elementAt(i) || functionPainter.plane.xScr2Crt(e.point.x)-0.1 < pointPainter.point.keys.elementAt(i))
                                    || (functionPainter.plane.yScr2Crt(e.point.y)+0.1 >pointPainter.point.values.elementAt(i) || functionPainter.plane.yScr2Crt(e.point.y)-0.1 < pointPainter.point.values.elementAt(i))){
                                    if(pointPainter.point.size == 1){
                                        painters.remove(pointPainter)
                                        painters.remove(functionPainter)
                                        painters.remove(derFunctionPainter)
                                    }
                                    pointPainter.point.remove(pointPainter.point.keys.elementAt(i))
                                    break
                                }
                            }
                            if(pointPainter.point.isEmpty()){
                                Polynom.index.clear()
                                k = 0
                            }
                            if(pointPainter.point.isNotEmpty()){
                                var pol1 = NewtonPolynomial(pointPainter.point)
                                Polynom = pol1
                                var poll:Polynomial = Polynomial(Polynom.diff())
                                derFunctionPainter.function = poll::invoke
                                functionPainter.function = Polynom::invoke
                            }
                        }
                        mainPanel.repaint()
                    }
                }
            }
        })

        fun deletePoints(){
            if(k == 1) painters.remove(pointPainter)
            mainPanel.repaint()
        }
        fun showPoints(){
            if(k == 1) painters.addAll(mutableListOf(pointPainter))
            mainPanel.repaint()
        }
        checkboxPoint.addItemListener { if (!checkboxPoint.isSelected) deletePoints() else showPoints() }

        fun deletePolynom(){
            if(k == 1) painters.remove(functionPainter)
            mainPanel.repaint()
        }
        fun showPolynom(){
            if(k == 1) {
                painters.addAll(mutableListOf(functionPainter))
            }
            mainPanel.repaint()
        }
        fun showPP(){
            if(k == 1) {
                painters.addAll(mutableListOf(functionPainter))
                painters.remove(pointPainter)
                painters.addAll(mutableListOf(pointPainter))
            }
            mainPanel.repaint()
        }
        checkboxGraphics.addItemListener {
            if (checkboxGraphics.isSelected&&checkboxPoint.isSelected) showPP()
            else if (checkboxGraphics.isSelected) showPolynom()
            else deletePolynom() }

        fun deleteDerivative(){
            if(k == 1) painters.remove(derFunctionPainter)
            mainPanel.repaint()
        }
        fun showDerivative(){
            if(k == 1) painters.addAll(mutableListOf(derFunctionPainter))
            mainPanel.repaint()
        }
        checkboxDerivative.addItemListener { if (!checkboxDerivative.isSelected) deleteDerivative() else showDerivative() }




        mainPlane.pixelSize=mainPanel.size
        mainPanel.addComponentListener(object:ComponentAdapter(){
            override fun componentResized(e: ComponentEvent?) {
                mainPlane.pixelSize=mainPanel.size
                mainPanel.repaint()
            }
        })

        controlPanel= JPanel().apply {

        }

        //запишем лямбда выражение, оно если одно, то после круглых скобок, если в круглых ничего нет, опускаем их
        xMin.addChangeListener{
            xMaxM.minimum=(xMinM.value as Double + 0.1)
            mainPlane.xSegment=Pair(xMin.value as Double, xMax.value as Double)
            mainPanel.repaint()
        }
        //подчеркивание вместо параметра, если не будем его потом использовать, можно вообще его не писать
        xMax.addChangeListener{
            xMinM.maximum=(xMaxM.value as Double - 0.1)
            mainPlane.xSegment=Pair(xMin.value as Double, xMax.value as Double)
            mainPanel.repaint()
        }
        yMin.addChangeListener{
            yMaxM.minimum=(yMinM.value as Double + 0.1)
            mainPlane.ySegment=Pair(yMin.value as Double, yMax.value as Double)
            mainPanel.repaint()
        }
        yMax.addChangeListener{
            yMinM.maximum=(yMaxM.value as Double - 0.1)
            mainPlane.ySegment=Pair(yMin.value as Double, yMax.value as Double)
            mainPanel.repaint()
        }

        textXMin= JLabel().apply {
            text="Xmin"
        }
        textXMax=JLabel().apply {
            text="Xmax"
        }
        textYMin= JLabel().apply {
            text="Ymin"
        }
        textYMax=JLabel().apply {
            text="Ymax"
        }

        textPoint= JLabel().apply {
            text="Отобразить точки"
        }
        textGraphic= JLabel().apply {
            text="Отобразить график функции"
        }
        textDerivative= JLabel().apply {
            text="Отобразить производную"
        }

        colorpanelPoint= JPanel().apply {
            background= Color.ORANGE
        }
        colorpanelGraphic= JPanel().apply {
            background= Color.DARK_GRAY
        }
        colorpanelDerivative= JPanel().apply {
            background= Color.GREEN
        }


        colorpanelPoint.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                e?.let {
                    val color1 = JColorChooser.showDialog(null, "Выберите цвет", colorpanelPoint.background)
                    colorpanelPoint.background=color1
                    pointPainter.pointColor=color1
                    mainPanel.repaint()
                }
            }
        })

        colorpanelGraphic.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                e?.let {
                    val color2 = JColorChooser.showDialog(null, "Выберите цвет", colorpanelGraphic.background)
                    colorpanelGraphic.background=color2
                    functionPainter.funColor=color2
                    mainPanel.repaint()
                }
            }
        })

        colorpanelDerivative.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                e?.let {
                    val color3 = JColorChooser.showDialog(null, "Выберите цвет", colorpanelDerivative.background)
                    colorpanelDerivative.background=color3
                    derFunctionPainter.funColor=color3
                    mainPanel.repaint()
                }
            }
        })

        layout = GroupLayout(contentPane).apply {
            setHorizontalGroup(createSequentialGroup()
                .addGap(4)
                .addGroup(
                    createParallelGroup()
                        .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE,  GroupLayout.DEFAULT_SIZE,  GroupLayout.DEFAULT_SIZE)
                        .addComponent(controlPanel, GroupLayout.DEFAULT_SIZE,  GroupLayout.DEFAULT_SIZE,  GroupLayout.DEFAULT_SIZE)
                )
                .addGap(4)
            )
            setVerticalGroup(createSequentialGroup()
                .addGap(4)
                .addComponent(mainPanel, GroupLayout.DEFAULT_SIZE,  GroupLayout.DEFAULT_SIZE,  GroupLayout.DEFAULT_SIZE)
                .addGap(4)
                .addComponent(controlPanel, GroupLayout.DEFAULT_SIZE,  GroupLayout.DEFAULT_SIZE,  GroupLayout.DEFAULT_SIZE)
                .addGap(4)
            )
        }

        controlPanel.layout=GroupLayout(controlPanel).apply{ //делаем контрольную панель
            setHorizontalGroup(createSequentialGroup()
                .addGap(8)
                .addGroup(
                    createParallelGroup()
                        .addComponent(textXMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textYMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8)
                .addGroup(
                    createParallelGroup()
                        .addComponent(xMin, 100,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(yMin, 100,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )

                .addGap(30)
                .addGroup(
                    createParallelGroup()
                        .addComponent(textXMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textYMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8)
                .addGroup(
                    createParallelGroup()
                        .addComponent(xMax, 100,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(yMax, 100,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(30)
                .addGroup(
                    createParallelGroup()
                        .addComponent(checkboxPoint, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkboxGraphics, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkboxDerivative, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(4)
                .addGroup(
                    createParallelGroup()
                        .addComponent(textPoint, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textGraphic, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textDerivative, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
            )

                .addGap(10)
                .addGroup(
                    createParallelGroup()
                        .addComponent(colorpanelPoint, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(colorpanelGraphic, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(colorpanelDerivative, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )

            )
            setVerticalGroup(createSequentialGroup()
                .addGap(8)
                .addGroup(
                    createParallelGroup()
                        .addComponent(textXMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(xMin,GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textXMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(xMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkboxPoint,GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textPoint, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(colorpanelPoint, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8)
                .addGroup(
                    createParallelGroup()
                        .addComponent(textYMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(yMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textYMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(yMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkboxGraphics,GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textGraphic, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(colorpanelGraphic, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8)
                .addGroup(
                    createParallelGroup()
                        .addComponent(checkboxDerivative,GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textDerivative, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(colorpanelDerivative, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8)
            )

        }
        pack() //задание и пересчет размеров
            mainPlane.width = mainPanel.width
            mainPlane.height = mainPanel.height
    }


}