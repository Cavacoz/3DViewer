import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.paint.PhongMaterial
import javafx.scene.shape._
import javafx.scene.transform.Rotate
import javafx.scene.{Group, Node}
import javafx.stage.Stage
import javafx.geometry.Pos
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.{PerspectiveCamera, Scene, SceneAntialiasing, SubScene}
import scala.collection.mutable.ListBuffer


class Main extends Application {

  //val file = "C:\\Users\\My PC\\Desktop\\Grupo30_RuiCavaco_MiguelReis_InesComba\\conf3D.txt"
  val file = "/Users/miguelreis/Desktop/Universidade/2ºsemestre-2ºano/PPM/Projeto/Base_Project2Share/conf3D.txt"
  val octIns = ObjectLoader()
  var list3D = List[Shape3D]()
  val boxList = ListBuffer[Box]()

  //Auxiliary types
  type Point = (Double, Double, Double)
  type Size = Double
  type Placement = (Point, Size) //1st point: origin, 2nd point: size
  //Shape3D is an abstract class that extends javafx.scene.Node
  //Box and Cylinder are subclasses of Shape3D
  type Section = (Placement, List[Node])  //example: ( ((0.0,0.0,0.0), 2.0), List(new Cylinder(0.5, 1, 10)))

  //Materials to be applied to the 3D objects
  val redMaterial = new PhongMaterial()
  redMaterial.setDiffuseColor(Color.rgb(255,0,0))

  val greenMaterial = new PhongMaterial()
  greenMaterial.setDiffuseColor(Color.rgb(0,255,0))

  val blueMaterial = new PhongMaterial()
  blueMaterial.setDiffuseColor(Color.rgb(0,0,255))

  /*
    Additional information about JavaFX basic concepts (e.g. Stage, Scene) will be provided in week7
   */
  override def start(stage: Stage): Unit = {

    //Get and print program arguments (args: Array[String])
    val params = getParameters
    println("Program arguments:" + params.getRaw)

    //3D objects
    val lineX = new Line(0, 0, 200, 0)
    lineX.setStroke(Color.GREEN)

    val lineY = new Line(0, 0, 0, 200)
    lineY.setStroke(Color.YELLOW)

    val lineZ = new Line(0, 0, 200, 0)
    lineZ.setStroke(Color.LIGHTSALMON)
    lineZ.getTransforms().add(new Rotate(-90, 0, 0, 0, Rotate.Y_AXIS))

    //Volume de visualização
    val camVolume = new Cylinder(10, 50, 10)
    camVolume.setTranslateX(1)
    camVolume.getTransforms().add(new Rotate(45, 0, 0, 0, Rotate.X_AXIS))
    camVolume.setMaterial(blueMaterial)
    camVolume.setDrawMode(DrawMode.LINE)

    val wiredBox = new Box(32, 32, 32)
    wiredBox.setTranslateX(16)
    wiredBox.setTranslateY(16)
    wiredBox.setTranslateZ(16)
    wiredBox.setMaterial(redMaterial)
    wiredBox.setDrawMode(DrawMode.LINE)

    // 3D objects (group of nodes - javafx.scene.Node) that will be provide to the subScene
     val worldRoot:Group = new Group(lineX, lineY, lineZ, camVolume)

    //Tarefa 1 - Carregamento dos Objetos 3D do ficheiro de texto.
    list3D = octIns.loadFromTextFile(file, worldRoot, list3D)

    // Camera
    val camera = new PerspectiveCamera(true)

    val cameraTransform = new CameraTransformer
    cameraTransform.setTranslate(0, 0, 0)
    cameraTransform.getChildren.add(camera)
    camera.setNearClip(0.1)
    camera.setFarClip(10000.0)

    camera.setTranslateZ(-500)
    camera.setFieldOfView(20)
    cameraTransform.ry.setAngle(-45.0)
    cameraTransform.rx.setAngle(-45.0)
    worldRoot.getChildren.add(cameraTransform)

    // SubScene - composed by the nodes present in the worldRoot
    val subScene = new SubScene(worldRoot, 800, 600, true, SceneAntialiasing.BALANCED)
    subScene.setFill(Color.DARKSLATEGRAY)
    subScene.setCamera(camera)

    // CameraView - an additional perspective of the environment
    val cameraView = new CameraView(subScene)
    cameraView.setFirstPersonNavigationEabled(true)
    cameraView.setFitWidth(350)
    cameraView.setFitHeight(225)
    cameraView.getRx.setAngle(-45)
    cameraView.getT.setZ(-100)
    cameraView.getT.setY(-500)
    cameraView.getCamera.setTranslateZ(-50)
    cameraView.startViewing

    // Position of the CameraView: Right-bottom corner
    StackPane.setAlignment(cameraView, Pos.BOTTOM_RIGHT)
    StackPane.setMargin(cameraView, new Insets(5))

    // Scene - defines what is rendered (in this case the subScene and the cameraView)
    val root = new StackPane(subScene, cameraView)
    subScene.widthProperty.bind(root.widthProperty)
    subScene.heightProperty.bind(root.heightProperty)

    val scene = new Scene(root, 810, 610, true, SceneAntialiasing.BALANCED)

    //setup and start the Stage
    stage.setTitle("PPM Project 21/22")
    stage.setScene(scene)
    stage.show

    //Criação da OcTree
    val oct2:Octree[Placement] = octreeDevelope2(wiredBox, list3D.head.asInstanceOf[Node], 8.0, worldRoot)

    //Mouse left click interaction
    scene.setOnMouseClicked(event => {
      camVolume.setTranslateX(camVolume.getTranslateX + 2)

      updateViewColors(camVolume, oct2)

      scaleOctree(2.0, oct2)

      worldRoot.getChildren.removeAll()

    })
  }

  //Tarefa 2
  def octreeDevelope2(PreBox:Box, cilindro:Node, OctreeDimensions:Double, root:Group): Octree[Placement] = {

    val tamanho: Int = PreBox.getWidth.toInt / 2
    val origemx: Int = PreBox.getTranslateX.toInt
    val origemy: Int = PreBox.getTranslateY.toInt
    val origemz: Int = PreBox.getTranslateZ.toInt

    val Box1 = new Box(tamanho, tamanho, tamanho)
    Box1.setTranslateX(origemx - tamanho / 2)
    Box1.setTranslateY(origemy - tamanho / 2)
    Box1.setTranslateZ(tamanho / 2)
    Box1.setMaterial(redMaterial)
    Box1.setDrawMode(DrawMode.LINE)

    val Box2 = new Box(tamanho, tamanho, tamanho)
    Box2.setTranslateX(origemx - tamanho / 2 + tamanho)
    Box2.setTranslateY(origemy - tamanho / 2 + tamanho)
    Box2.setTranslateZ(origemz - tamanho / 2)
    Box2.setMaterial(redMaterial)
    Box2.setDrawMode(DrawMode.LINE)

    val Box3 = new Box(tamanho, tamanho, tamanho)
    Box3.setTranslateX(origemx - tamanho / 2 + tamanho)
    Box3.setTranslateY(origemy - tamanho / 2)
    Box3.setTranslateZ(origemz - tamanho / 2)
    Box3.setMaterial(redMaterial)
    Box3.setDrawMode(DrawMode.LINE)

    val Box4 = new Box(tamanho, tamanho, tamanho)
    Box4.setTranslateX(origemx - tamanho / 2)
    Box4.setTranslateY(origemy - tamanho / 2 + tamanho)
    Box4.setTranslateZ(origemz - tamanho / 2)
    Box4.setMaterial(redMaterial)
    Box4.setDrawMode(DrawMode.LINE)

    val Box5 = new Box(tamanho, tamanho, tamanho)
    Box5.setTranslateX(origemx - tamanho / 2)
    Box5.setTranslateY(origemy - tamanho / 2)
    Box5.setTranslateZ(origemz - tamanho / 2 + tamanho)
    Box5.setMaterial(redMaterial)
    Box5.setDrawMode(DrawMode.LINE)

    val Box6 = new Box(tamanho, tamanho, tamanho)
    Box6.setTranslateX(origemx - tamanho / 2 + tamanho)
    Box6.setTranslateY(origemy - tamanho / 2 + tamanho)
    Box6.setTranslateZ(origemz - tamanho / 2 + tamanho)
    Box6.setMaterial(redMaterial)
    Box6.setDrawMode(DrawMode.LINE)

    val Box7 = new Box(tamanho, tamanho, tamanho)
    Box7.setTranslateX(origemx - tamanho / 2 + tamanho)
    Box7.setTranslateY(origemy - tamanho / 2)
    Box7.setTranslateZ(origemz - tamanho / 2 + tamanho)
    Box7.setMaterial(redMaterial)
    Box7.setDrawMode(DrawMode.LINE)

    val Box8 = new Box(tamanho, tamanho, tamanho)
    Box8.setTranslateX(origemx - tamanho / 2)
    Box8.setTranslateY(origemy - tamanho / 2 + tamanho)
    Box8.setTranslateZ(origemz - tamanho / 2 + tamanho)
    Box8.setMaterial(redMaterial)
    Box8.setDrawMode(DrawMode.LINE)

    if (Box1.asInstanceOf[Shape3D].getBoundsInParent.contains(cilindro.getBoundsInParent)) {
      root.getChildren.add(Box1)
      boxList += Box1
      val plc: Placement = ((Box1.getTranslateX - Box1.getWidth / 2.toDouble, Box1.getTranslateY - Box1.getWidth / 2.toDouble, Box1.getTranslateZ - Box1.getWidth / 2.toDouble), Box1.getWidth)
      OcNode(plc, octreeDevelope2(Box1, cilindro, OctreeDimensions, root),OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)

    } else if (Box2.asInstanceOf[Shape3D].getBoundsInParent.contains(cilindro.getBoundsInParent)) {
      root.getChildren.add(Box2)
      boxList += Box2
      val plc: Placement = ((Box2.getTranslateX - Box2.getWidth / 2.toDouble, Box2.getTranslateY - Box2.getWidth / 2.toDouble, Box2.getTranslateZ - Box2.getWidth / 2.toDouble), Box2.getWidth)
      OcNode(plc, OcEmpty, octreeDevelope2(Box2, cilindro, OctreeDimensions, root), OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty)

    } else if (Box3.asInstanceOf[Shape3D].getBoundsInParent.contains(cilindro.getBoundsInParent)) {
      root.getChildren.add(Box3)
      boxList += Box3
      val plc: Placement = ((Box3.getTranslateX - Box3.getWidth / 2.toDouble, Box3.getTranslateY - Box3.getWidth / 2.toDouble, Box3.getTranslateZ - Box3.getWidth / 2.toDouble), Box3.getWidth)
      OcNode(plc, OcEmpty,OcEmpty, octreeDevelope2(Box3, cilindro, OctreeDimensions, root),OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty)

    } else if (Box4.asInstanceOf[Shape3D].getBoundsInParent.contains(cilindro.getBoundsInParent)) {
      root.getChildren.add(Box4)
      boxList += Box4
      val plc: Placement = ((Box4.getTranslateX - Box4.getWidth / 2.toDouble, Box4.getTranslateY - Box4.getWidth / 2.toDouble, Box4.getTranslateZ - Box4.getWidth / 2.toDouble), Box4.getWidth)
      OcNode(plc, OcEmpty, OcEmpty, OcEmpty, octreeDevelope2(Box4, cilindro, OctreeDimensions, root), OcEmpty, OcEmpty, OcEmpty, OcEmpty)

    }else if (Box5.asInstanceOf[Shape3D].getBoundsInParent.contains(cilindro.getBoundsInParent)) {
      root.getChildren.add(Box5)
      boxList += Box5
      val plc: Placement = ((Box5.getTranslateX - Box5.getWidth / 2.toDouble, Box5.getTranslateY - Box5.getWidth / 2.toDouble, Box5.getTranslateZ - Box5.getWidth / 2.toDouble), Box5.getWidth)
      OcNode(plc, OcEmpty,OcEmpty,OcEmpty,OcEmpty, octreeDevelope2(Box5, cilindro, OctreeDimensions, root), OcEmpty,OcEmpty,OcEmpty)

    } else if (Box6.asInstanceOf[Shape3D].getBoundsInParent.contains(cilindro.getBoundsInParent)) {
      root.getChildren.add(Box6)
      boxList += Box6
      val plc: Placement = ((Box6.getTranslateX - Box6.getWidth / 2.toDouble, Box6.getTranslateY - Box6.getWidth / 2.toDouble, Box6.getTranslateZ - Box6.getWidth / 2.toDouble), Box6.getWidth)
      OcNode(plc, OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty, octreeDevelope2(Box6, cilindro, OctreeDimensions, root), OcEmpty,OcEmpty)

    } else if (Box7.asInstanceOf[Shape3D].getBoundsInParent.contains(cilindro.getBoundsInParent)) {
      root.getChildren.add(Box7)
      boxList += Box7
      val plc: Placement = ((Box7.getTranslateX - Box7.getWidth / 2.toDouble, Box7.getTranslateY - Box7.getWidth / 2.toDouble, Box7.getTranslateZ - Box7.getWidth / 2.toDouble), Box7.getWidth)
      OcNode(plc, OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty, octreeDevelope2(Box7, cilindro, OctreeDimensions, root),OcEmpty)

    } else if (Box8.asInstanceOf[Shape3D].getBoundsInParent.contains(cilindro.getBoundsInParent)) {
      root.getChildren.add(Box8)
      boxList += Box8
      val plc: Placement = ((Box8.getTranslateX - Box8.getWidth / 2.toDouble, Box8.getTranslateY - Box8.getWidth / 2.toDouble, Box8.getTranslateZ - Box8.getWidth / 2.toDouble), Box8.getWidth)
      OcNode(plc, OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty, octreeDevelope2(Box8, cilindro, OctreeDimensions, root))

    } else if (cilindro.asInstanceOf[Shape3D].getBoundsInParent.intersects(Box1.getBoundsInParent)) {
      val plc: Placement = ((Box1.getTranslateX - Box1.getWidth / 2.toDouble, Box1.getTranslateY - Box1.getWidth / 2.toDouble, Box1.getTranslateZ - Box1.getWidth / 2.toDouble), Box1.getWidth)
      val sec: Section = (plc, List(cilindro))
      OcLeaf(sec)

    } else {
      OcEmpty
    }
  }

  //Tarefa 3
  def updateViewColors(viewVolume: Shape3D, octree: Octree[Placement] ): Unit ={

    octree match {

      case OcEmpty =>

      case OcLeaf(section) =>

        changeColor(section.asInstanceOf[Section]._2.toArray, viewVolume)
        changeColor(boxList.toArray, viewVolume)

      case OcNode(placement, up_00, up_01, up_10, up_11, down_00, down_01, down_10, down_11) =>

        updateViewColors(viewVolume, up_00)
        updateViewColors(viewVolume, up_01)
        updateViewColors(viewVolume, up_10)
        updateViewColors(viewVolume, up_11)
        updateViewColors(viewVolume, down_00)
        updateViewColors(viewVolume, down_01)
        updateViewColors(viewVolume, down_10)
        updateViewColors(viewVolume, down_11)
    }
  }

  def changeColor(listObj: Array[AnyRef], viewVolume: Shape3D): Unit = {
    listObj.map(x => {
      if(viewVolume.getBoundsInParent.contains(x.asInstanceOf[Shape3D].getBoundsInParent) || x.asInstanceOf[Shape3D].getBoundsInParent.intersects(viewVolume.getBoundsInParent))
      x.asInstanceOf[Shape3D].setMaterial(greenMaterial)
      else
      x.asInstanceOf[Shape3D].setMaterial(redMaterial)
    })
  }

  //Tarefa 4
  def scaleOctree(fact:Double, oct:Octree[Placement]): Octree[Placement] = {

        oct match {

          case OcEmpty => OcEmpty

          case OcLeaf(section) =>

            updateShapeSize(section.asInstanceOf[Section]._1._2, section.asInstanceOf[Section]._2.toArray, fact)

            val sec1: Section = (((section.asInstanceOf[Section]._1._1._1 * fact, section.asInstanceOf[Section]._1._1._2 * fact, section.asInstanceOf[Section]._1._1._3 * fact), section.asInstanceOf[Section]._1._2 * fact),
              section.asInstanceOf[Section]._2)

            OcLeaf(sec1)

          case OcNode(placement,up_00,up_01,up_10,up_11,down_00,down_01,down_10,down_11) =>

            updateShapeSize(placement._2, boxList.toArray, fact)

            OcNode(((placement._1._1 * fact, placement._1._2 * fact, placement._1._3 * fact), placement._2 * fact),
              scaleOctree(fact, up_00 ), scaleOctree(fact,up_01),
              scaleOctree(fact,up_10), scaleOctree(fact,up_11),
              scaleOctree(fact,down_00), scaleOctree(fact,down_01),
              scaleOctree(fact,down_10), scaleOctree(fact,down_11))
        }
  }

  def updateShapeSize(size: Double, listObj: Array[Node], fact: Double): Unit ={
    listObj.map(x => if (x.isInstanceOf[Box] && x.asInstanceOf[Box].getWidth.equals(size)) {
      translateObj(x.asInstanceOf[Shape3D], fact)
    } else if(x.isInstanceOf[Cylinder]){
      translateObj(x.asInstanceOf[Shape3D], fact)
    })
  }

  def translateObj(x: Shape3D, fact: Double): Unit ={
    x.setScaleX(x.getScaleX * fact)
    x.setScaleY(x.getScaleY * fact)
    x.setScaleZ(x.getScaleZ * fact)
    x.setTranslateX(x.getTranslateX * fact)
    x.setTranslateY(x.getTranslateY * fact)
    x.setTranslateZ(x.getTranslateZ * fact)
  }


  override def init(): Unit = {
    println("init")
  }

  override def stop(): Unit = {
    println("stopped")
  }
}

object FxApp {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[Main], args: _*)
  }
}

