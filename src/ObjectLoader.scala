import javafx.scene.paint.{Color, PhongMaterial}
import javafx.scene.shape.{Cylinder, Shape3D}
import javafx.scene.{Group, Node}

import scala.io.Source

object ObjectLoader {

  def loadFromTextFile(file: String, root: Group): List[Shape3D] = {

    var list3D: List[Shape3D] = List()
    val materialColour = new PhongMaterial()
    val bufferedSource = Source.fromFile(file)

    for (line <- bufferedSource.getLines) {
      val linha = line.split(" ")
      val colour = linha(1).split(",")

      if (linha(0).equals("Cylinder")) {
        val cylinder = new Cylinder(0.5, 1, 10)
        cylinder.setTranslateX(linha(2).toInt)
        cylinder.setTranslateY(linha(3).toInt)
        cylinder.setTranslateZ(linha(4).toInt)
        cylinder.setScaleX(linha(5).toDouble)
        cylinder.setScaleY(linha(6).toDouble)
        cylinder.setScaleZ(linha(7).toDouble)

        materialColour.setDiffuseColor(Color.rgb(colour(0).toInt, colour(1).toInt, colour(2).toInt))
        cylinder.setMaterial(materialColour)

        list3D = addInsideList(list3D, cylinder)

        root.getChildren.add(cylinder)
      }
    }
    bufferedSource.close
    list3D
  }

  def addInsideList(list: List[Shape3D], objeto: Shape3D): List[Shape3D] = {
    list match {
      case Nil => list :+ objeto
      case x :: xs => x :: addInsideList(xs, objeto)
    }
  }

}
