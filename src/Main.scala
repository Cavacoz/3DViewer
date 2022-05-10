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
  val octreeEditor = OctreeEditor()

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
    worldRoot.getChildren.add(wiredBox)
    val plc:octreeEditor.Placement = ((wiredBox.getTranslateX - wiredBox.getWidth / 2,wiredBox.getTranslateY - wiredBox.getWidth / 2, wiredBox.getTranslateZ - wiredBox.getWidth / 2), wiredBox.getWidth)
    val octree: Octree[octreeEditor.Placement] = OcNode(plc, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)

    val oct2:Octree[octreeEditor.Placement] = octreeEditor.octreeDevelope(wiredBox,list3D, 8.0, worldRoot, octree)
    println(oct2)

    //Mouse left click interaction
    scene.setOnMouseClicked(event => {
      camVolume.setTranslateX(camVolume.getTranslateX + 2)

      octreeEditor.updateViewColors(camVolume, oct2)

      octreeEditor.scaleOctree(2.0, oct2)

      worldRoot.getChildren.removeAll()

    })
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

