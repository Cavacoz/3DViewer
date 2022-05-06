import javafx.scene.paint.{Color, PhongMaterial}
import javafx.scene.shape.{Box, Cylinder, DrawMode, Shape3D}
import javafx.scene.{Group, Node}

import scala.io.Source

case class ObjectLoader() {

  type Point = (Double, Double, Double)
  type Size = Double
  type Placement = (Point, Size)
  type Section = (Placement, List[Node])

  def loadFromTextFile(file: String, root: Group, list: List[Shape3D]): List[Shape3D] = {

    var list3D = List[Shape3D]()
    val materialColour = new PhongMaterial()
    val bufferedSource = Source.fromFile(file)

    for (line <- bufferedSource.getLines){
      val linha = line.split(" ")
      val colour = linha(1).split(",")

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

        list3D = list :+ cylinder

        root.getChildren.add(cylinder)
      }
    }
    bufferedSource.close
    list3D
  }
}
