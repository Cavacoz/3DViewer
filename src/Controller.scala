import InitSubScene.{camVolume, currentOctree, wiredBox, worldRoot}
import OctreeEditor.{Placement, func}
import javafx.fxml.FXML
import javafx.scene.control.{Button, TextField}
import javafx.scene.SubScene
import javafx.scene.layout.StackPane

import java.awt.Label

class Controller {

  var loaded: Boolean = false

  @FXML
  var subScene1: SubScene = _

  @FXML
  var scaleLabel: Label = _

  @FXML
  var scaleUp: Button = _

  @FXML
  var scaleDown: Button = _

  @FXML
  var colorLabel: Label = _

  @FXML
  var removeGreen: Button = _

  @FXML
  var addSepia: Button = _

  @FXML
  var saveState: Button = _

  @FXML
  var loadOctree: Button = _

  @FXML
  var fileName: TextField = _

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
    val oct: Octree[Placement] = OctreeEditor.scaleOctree(2.0, currentOctree)
    currentOctree = oct
    OctreeEditor.updateViewColors(camVolume, currentOctree)
    println(oct)
  }

  def onScaleDownClicked(): Unit = {
    val oct: Octree[Placement] = OctreeEditor.scaleOctree(0.5, currentOctree)
    currentOctree = oct
    OctreeEditor.updateViewColors(camVolume, currentOctree)
    println(oct)
  }

  def saveOctreeState(): Unit = {
    ObjectLoader.saveOctreeState(currentOctree)
  }

  def onLoadOctreeClicked(): Unit = {
    if (loaded == false) {
      val file = "C:\\Users\\My PC\\Desktop\\Grupo30_RuiCavaco_MiguelReis_InesComba\\" + fileName.getText() + ".txt"
      val list3D = ObjectLoader.loadFromTextFile(file, worldRoot)
      val oct: Octree[Placement] = OctreeEditor.octreeDevelope(wiredBox, list3D, 8.0, worldRoot, currentOctree)
      InitSubScene.currentOctree = oct
      fileName.clear()
      println(oct)
      loaded = true
    }
  }

  def onRemoveGreenClicked(): Unit = {
    val oct: Octree[Placement] = OctreeEditor.mapColourEffect(func, currentOctree, 0)
    InitSubScene.currentOctree = oct
  }

  def onSepiaClicked(): Unit = {
    val oct: Octree[Placement] = OctreeEditor.mapColourEffect(func, currentOctree, 1)
    InitSubScene.currentOctree = oct
  }
}
