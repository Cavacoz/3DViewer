import OctreeEditor.{blueMaterial, redMaterial}
import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.geometry.{Insets, Pos}
import javafx.scene.{Group, Parent, PerspectiveCamera, Scene, SceneAntialiasing, SubScene}
import javafx.scene.layout.StackPane
import javafx.scene.paint.{Color}
import javafx.scene.shape.{Box, Cylinder, DrawMode, Line}
import javafx.scene.transform.Rotate
import javafx.stage.Stage

class InitSubScene extends Application {

  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("PPM Project")
    val fxmlLoader = new FXMLLoader(getClass.getResource("Controller.fxml"))
    val mainViewRoot: Parent = fxmlLoader.load()
    val scene = new Scene(mainViewRoot)
    scene.setCamera(new PerspectiveCamera(false))
    primaryStage.setScene(scene)
    primaryStage.show()
  }
}

object InitSubScene {

  //3D objects
  val lineX = new Line(0, 0, 200, 0)
  lineX.setStroke(Color.GREEN)

  val lineY = new Line(0, 0, 0, 200)
  lineY.setStroke(Color.YELLOW)

  val lineZ = new Line(0, 0, 200, 0)
  lineZ.setStroke(Color.LIGHTSALMON)
  lineZ.getTransforms.add(new Rotate(-90, 0, 0, 0, Rotate.Y_AXIS))

  val camVolume = new Cylinder(10, 50, 10)
  camVolume.setTranslateX(1)
  camVolume.getTransforms.add(new Rotate(45, 0, 0, 0, Rotate.X_AXIS))
  camVolume.setMaterial(blueMaterial)
  camVolume.setDrawMode(DrawMode.LINE)

  val wiredBox = new Box(32, 32, 32)
  wiredBox.setTranslateX(16)
  wiredBox.setTranslateY(16)
  wiredBox.setTranslateZ(16)
  wiredBox.setMaterial(redMaterial)
  wiredBox.setDrawMode(DrawMode.LINE)

  val worldRoot: Group = new Group(camVolume, wiredBox, lineX, lineY, lineZ)

  val plc: OctreeEditor.Placement = ((wiredBox.getTranslateX - wiredBox.getWidth / 2, wiredBox.getTranslateY - wiredBox.getWidth / 2, wiredBox.getTranslateZ - wiredBox.getWidth / 2), wiredBox.getWidth)
  var currentOctree: Octree[OctreeEditor.Placement] = OcNode(plc, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)

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

  val subScene = new SubScene(worldRoot, 800, 600, true, SceneAntialiasing.BALANCED)
  subScene.setFill(Color.DARKSLATEGRAY)
  subScene.setCamera(camera)

  val cameraView = new CameraView(subScene)
  cameraView.setFirstPersonNavigationEabled(true)
  cameraView.setFitWidth(350)
  cameraView.setFitHeight(225)
  cameraView.getRx.setAngle(-45)
  cameraView.getT.setZ(-100)
  cameraView.getT.setY(-500)
  cameraView.getCamera.setTranslateZ(-50)
  cameraView.startViewing()

  StackPane.setAlignment(cameraView, Pos.BOTTOM_RIGHT)
  StackPane.setMargin(cameraView, new Insets(5))

  val root = new StackPane(subScene, cameraView)

  root.setOnMouseClicked((event) => {
    OctreeEditor.updateViewColors(camVolume, currentOctree)
    camVolume.setTranslateX(camVolume.getTranslateX + 2)
  })
}

