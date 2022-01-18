package ru.annnn.ui
import java.awt.Color
import java.awt.Dimension
import java.awt.event.ComponentAdapter
import java.awt.event.ComponentEvent
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.*
import Plane
import ru.annnn.ui.painting.*
import ru.annnn.ui.painting.Painter
import kotlin.math.*


class MainFrameEX  : JFrame() {

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
    private val tMin:JSpinner
    private val tMax: JSpinner
    private val tMinM: SpinnerNumberModel
    private val tMaxM: SpinnerNumberModel
    private val colorpanelExplicit: JPanel
    private val colorpanelParametric: JPanel
    private val textXMin: JLabel
    private  val textXMax: JLabel
    private val textYMin: JLabel
    private val textYMax: JLabel
    private val textTMin: JLabel
    private val textTMax: JLabel
    private val textExplicit: JLabel
    private val textParametric: JLabel
    private val checkboxExplicit: JCheckBox
    private val checkboxParametric: JCheckBox



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
        tMinM= SpinnerNumberModel(-15.0,-100.0,14.9,0.1)
        tMin= JSpinner(tMinM)
        tMaxM= SpinnerNumberModel(15.0,-14.9,100.0,0.1)
        tMax= JSpinner(tMaxM)

        val mainPlane= Plane(xMinM.value as Double,yMinM.value as Double,xMaxM.value as Double,yMaxM.value as Double)
        val cartesianPainter = CartesianPainter(mainPlane)
        val painters = mutableListOf<Painter>(cartesianPainter)
        mainPanel=GraphicsPanel(painters).apply {
            background=Color.WHITE
        }

        checkboxExplicit= JCheckBox()
        checkboxExplicit.isSelected = false //задаем пустые чекбоксы по умолчанию
        checkboxParametric= JCheckBox()
        checkboxParametric.isSelected = false

        mainPlane.pixelSize=mainPanel.size
        mainPanel.addComponentListener(object:ComponentAdapter(){
            override fun componentResized(e: ComponentEvent?) {
                mainPlane.pixelSize=mainPanel.size
                mainPanel.repaint()
            }
        })

        val explicit = { x:Double ->  sqrt(9-x.pow(2))} //функция из билета явная
        val expFun = FunctionPainter(mainPlane)//рисовальщик функции

        val x = {t:Double -> t+3* cos(t)} //параметрически задаем х и у
        val y = {t:Double -> sin(t)}
        val parFun = ParametricFunPainter(x,y,mainPlane) //рисовалка для параметрической функции

        checkboxExplicit.addItemListener {
            if (checkboxExplicit.isSelected) { //если чекбокс с галочкой то рисуем график
                expFun.function=explicit
                painters.add(expFun)
                mainPanel.repaint()
            }
            else { //если без то стираем
                painters.remove(expFun)
                mainPanel.repaint()
            }
        }
        checkboxParametric.addItemListener {//все аналогично
            if (checkboxParametric.isSelected) {
                painters.add(parFun)
                mainPanel.repaint()
            }
            else {
                painters.remove(parFun)
                mainPanel.repaint()
            }
        }

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
        tMin.addChangeListener{
            tMaxM.minimum = (tMin.value as Double + 0.1)
            mainPlane.tMax = tMax.value as Double
            mainPlane.tMin = tMin.value as Double
            mainPanel.repaint()
        }
        tMax.addChangeListener{
            tMinM.maximum = (tMax.value as Double - 0.1)
            mainPlane.tMax = tMax.value as Double
            mainPlane.tMin = tMin.value as Double
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
        textTMin= JLabel().apply {
            text="Tmin"
        }
        textTMax=JLabel().apply {
            text="Tmax"
        }

        textExplicit= JLabel().apply {
            text="Отобразить явный график"
        }
        textParametric= JLabel().apply {
            text="Отобразить параметрический"
        }

        colorpanelExplicit= JPanel().apply {
            background= Color.ORANGE
        }
        colorpanelParametric= JPanel().apply {
            background= Color.DARK_GRAY
        }

        colorpanelExplicit.addMouseListener(object : MouseAdapter(){ //просто задача цвета
            override fun mouseClicked(e: MouseEvent?) {
                e?.let {
                    val color1 = JColorChooser.showDialog(null, "Выберите цвет", colorpanelExplicit.background)
                    colorpanelExplicit.background=color1
                    expFun.funColor=color1
                    mainPanel.repaint()
                }
            }
        })

        colorpanelParametric.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                e?.let {
                    val color2 = JColorChooser.showDialog(null, "Выберите цвет", colorpanelParametric.background)
                    colorpanelParametric.background=color2
                    parFun.funColor=color2
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
                        .addComponent(textXMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8)
                .addGroup(
                    createParallelGroup()
                        .addComponent(xMin, 100,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(xMax, 100,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )

                .addGap(30)
                .addGroup(
                    createParallelGroup()
                        .addComponent(textYMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textYMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8)
                .addGroup(
                    createParallelGroup()
                        .addComponent(yMin, 100,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(yMax, 100,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(30)
                .addGap(8)
                .addGroup(
                    createParallelGroup()
                        .addComponent(textTMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textTMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8)
                .addGroup(
                    createParallelGroup()
                        .addComponent(tMin, 100,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(tMax, 100,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(30)
                .addGroup(
                    createParallelGroup()
                        .addComponent(checkboxExplicit, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkboxParametric, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(4)
                .addGroup(
                    createParallelGroup()
                        .addComponent(textExplicit, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textParametric, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )

                .addGap(10)
                .addGroup(
                    createParallelGroup()
                        .addComponent(colorpanelExplicit, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(colorpanelParametric, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )

            )
            setVerticalGroup(createSequentialGroup()
                .addGap(8)
                .addGroup(
                    createParallelGroup()
                        .addComponent(textXMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(xMin,GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textYMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(yMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textTMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(tMin, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkboxExplicit,GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textExplicit, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(colorpanelExplicit, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8)
                .addGroup(
                    createParallelGroup()
                        .addComponent(textXMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(xMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textYMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(yMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textTMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(tMax, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(checkboxParametric,GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(textParametric, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                        .addComponent(colorpanelParametric, GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE,  GroupLayout.PREFERRED_SIZE)
                )
                .addGap(8)
            )

        }
        pack() //задание и пересчет размеров
        mainPlane.width = mainPanel.width
        mainPlane.height = mainPanel.height
    }


}