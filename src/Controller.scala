import OctreeEditor.{Placement, boxList}
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.SubScene
import javafx.scene.layout.StackPane

import java.awt.Label

class Controller {

  @FXML
  var subScene1: SubScene = _

  @FXML
  var loadOctree: Button = _

  @FXML
  var scaleLabel: Label = _

  @FXML
  var scaleUp: Button = _

  @FXML
  var scaleDown: Button = _

  @FXML
  var stackPane: StackPane = _

  //method automatically invoked after the @FXML fields have been injected
  @FXML
  def initialize(): Unit = {
    InitSubScene.subScene.widthProperty.bind(subScene1.widthProperty)
    InitSubScene.subScene.heightProperty.bind(subScene1.heightProperty)
    subScene1.setRoot(InitSubScene.root)
  }

  def onScaleUpClicked(): Unit = {
    val oct: Octree[Placement] = OctreeEditor.scaleOctree(2.0, InitSubScene.currentOctree)
    InitSubScene.currentOctree = oct
    println(oct)
  }

  def onScaleDownClicked(): Unit = {
    val oct: Octree[Placement] = OctreeEditor.scaleOctree(0.5, InitSubScene.currentOctree)
    InitSubScene.currentOctree = oct
    println(oct)
  }

  def onLoadOctreeClicked(): Unit = {
    val oct: Octree[Placement] = OctreeEditor.octreeDevelope(InitSubScene.wiredBox, InitSubScene.list3D, 8.0, InitSubScene.worldRoot, InitSubScene.currentOctree)
    InitSubScene.currentOctree = oct
    println(oct)
  }
}
