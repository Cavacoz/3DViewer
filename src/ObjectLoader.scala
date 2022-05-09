import javafx.application.Application
import javafx.geometry.Insets
import javafx.scene.paint.PhongMaterial
import javafx.scene.shape._
import javafx.scene.transform.{Rotate, Translate}
import javafx.scene.{Group, Node}
import javafx.stage.Stage
import javafx.geometry.Pos
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.{PerspectiveCamera, Scene, SceneAntialiasing, SubScene}

import scala.collection.mutable.ListBuffer
import scala.io.Source

case class ObjectLoader() {

  type Point = (Double, Double, Double)
  type Size = Double
  type Placement = (Point, Size)
  type Section = (Placement, List[Node])

  def loadFromTextFile(file: String, root: Group, list: List[Shape3D]): List[Shape3D] = {

    var list3D:List[Shape3D] = List()
    val materialColour = new PhongMaterial()
    println(file)
    val bufferedSource = Source.fromFile(file)

    for (line <- bufferedSource.getLines()){

      val linha: Array[String] = line.split(" ")
      val colour: Array[String] = linha(1).split(",")
      if(linha(0).equals("Cylinder")) {
        val cylinder = new Cylinder(0.5, 1, 10)
        cylinder.setTranslateX(linha(2).toInt)
        cylinder.setTranslateY(linha(3).toInt)
        cylinder.setTranslateZ(linha(4).toInt)
        cylinder.setScaleX(linha(5).toDouble)
        cylinder.setScaleY(linha(6).toDouble)
        cylinder.setScaleZ(linha(7).toDouble)

        materialColour.setDiffuseColor(Color.rgb(colour(0).toInt, colour(1).toInt, colour(2).toInt))
        cylinder.setMaterial(materialColour)

        //list3D = list :+ cylinder
        list3D = addInsideList(list3D, cylinder)
        root.getChildren.add(cylinder)
      }
    }
    bufferedSource.close
    println(list3D)
    list3D
  }

  def addInsideList(list: List[Shape3D], objeto: Shape3D): List[Shape3D] = {
    list match {
      case Nil =>  list:+objeto
      case x::xs => x::addInsideList(xs, objeto)
    }
  }
}
