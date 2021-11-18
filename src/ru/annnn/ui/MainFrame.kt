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
import ru.annnn.ui.painting.FunctionPainter

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
        val cartesianPainter= CartesianPainter(mainPlane)
        var functionPainter = FunctionPainter(mainPlane) // Объект для отрисоки полиномов
        val derFunctionPainter = FunctionPainter(mainPlane)
        val painters = mutableListOf<Painter>(cartesianPainter)
        mainPanel=GraphicsPanel(painters).apply {
            background=Color.WHITE
        }

        checkboxPoint= JCheckBox()
        checkboxGraphics= JCheckBox()
        checkboxDerivative= JCheckBox()

        mainPanel.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                super.mouseClicked(e)
                if (e?.button == MouseEvent.BUTTON1) { // Если нажата левая кнопка мыши
                    if(painters.size == 1){
                        functionPainter = FunctionPainter(mainPlane)
                    }
                    painters.addAll(mutableListOf(functionPainter))
                    functionPainter.add(e.point.x, e.point.y) // Добавляем узел к полиному Ньютон
                }
                if (e?.button == MouseEvent.BUTTON3) { // Если нажата правая кнопка мыши
                    if(functionPainter.m.size == 1){
                        painters.removeAt(painters.size-1)
                    }
                    else{
                        functionPainter.deletePoint(e.point.x,e.point.y)
                    }
                }
                mainPanel.repaint() // перерисовываем панель
            }
        })

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
            background= Color.RED
        }
        colorpanelGraphic= JPanel().apply {
            background= Color.BLUE
        }
        colorpanelDerivative= JPanel().apply {
            background= Color.BLUE
        }

        colorpanelPoint.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                e?.let {
                    val colorpanelPoint = it.source as JPanel
                    val color1 = JColorChooser.showDialog(null, "Выберите цвет", colorpanelPoint.background)
                    colorpanelPoint.background=color1
                    functionPainter.setPointColor(color1)
                    mainPanel.repaint()
                }
            }
        })

        colorpanelGraphic.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                e?.let {
                    val colorpanelGraphic= it.source as JPanel
                    val color2 = JColorChooser.showDialog(null, "Выберите цвет", colorpanelGraphic.background)
                    colorpanelGraphic.background=color2
                    functionPainter.setFunColor(color2)
                    mainPanel.repaint()
                }
            }
        })

        colorpanelDerivative.addMouseListener(object : MouseAdapter(){
            override fun mouseClicked(e: MouseEvent?) {
                e?.let {
                    val colorpanelDerivative= it.source as JPanel
                    val color3 = JColorChooser.showDialog(null, "Выберите цвет", colorpanelDerivative.background)
                    colorpanelDerivative.background=color3
                    derFunctionPainter.setDifColor(color3)
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