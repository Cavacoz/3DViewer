import javafx.scene.paint.{Color, PhongMaterial}
import javafx.scene.{Group, Node}
import javafx.scene.shape.{Box, Cylinder, DrawMode, Shape3D}

import scala.collection.mutable.ListBuffer

case class OctreeEditor() {
  type Point = (Double, Double, Double)
  type Size = Double
  type Placement = (Point, Size)
  type Section = (Placement, List[Node])

  val boxList = ListBuffer[Box]()

  val redMaterial = new PhongMaterial()
  redMaterial.setDiffuseColor(Color.rgb(255,0,0))

  val greenMaterial = new PhongMaterial()
  greenMaterial.setDiffuseColor(Color.rgb(0,255,0))

  val blueMaterial = new PhongMaterial()
  blueMaterial.setDiffuseColor(Color.rgb(0,0,255))

  //Tarefa 2
 /*
  def octreeDevelope(PreBox:Box, ObjectList:List[Shape3D], OctreeDimensions:Double, root:Group): Octree[Placement] = {
        val tamanho: Int = PreBox.getWidth.toInt / 2
        val origemx: Int = PreBox.getTranslateX.toInt
        val origemy: Int = PreBox.getTranslateY.toInt
        val origemz: Int = PreBox.getTranslateZ.toInt

        //Criação das caixas que dividem o Cubo "Pai" em 8 "filhos"
        val Box1 = createBox(tamanho, origemx - tamanho / 2, origemy - tamanho / 2, tamanho / 2)
        val Box2 = createBox(tamanho, origemx - tamanho / 2 + tamanho, origemy - tamanho / 2 + tamanho, origemz - tamanho / 2)
        val Box3 = createBox(tamanho, origemx - tamanho / 2 + tamanho, origemy - tamanho / 2, origemz - tamanho / 2)
        val Box4 = createBox(tamanho, origemx - tamanho / 2, origemy - tamanho / 2 + tamanho, origemz - tamanho / 2)
        val Box5 = createBox(tamanho, origemx - tamanho / 2, origemy - tamanho / 2, origemz - tamanho / 2 + tamanho)
        val Box6 = createBox(tamanho, origemx - tamanho / 2 + tamanho, origemy - tamanho / 2 + tamanho, origemz - tamanho / 2 + tamanho)
        val Box7 = createBox(tamanho, origemx - tamanho / 2 + tamanho, origemy - tamanho / 2, origemz - tamanho / 2 + tamanho)
        val Box8 = createBox(tamanho, origemx - tamanho / 2, origemy - tamanho / 2 + tamanho, origemz - tamanho / 2 + tamanho)

        if (conectionSituation(Box1, ObjectList, 0,0, null) != null) {
          root.getChildren.add(Box1)
          boxList += Box1
          val plc: Placement = ((Box1.getTranslateX - Box1.getWidth / 2.toDouble, Box1.getTranslateY - Box1.getWidth / 2.toDouble, Box1.getTranslateZ - Box1.getWidth / 2.toDouble), Box1.getWidth)
          OcNode(plc, octreeDevelope(Box1, ObjectList, OctreeDimensions, root),OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)


        } else if (conectionSituation(Box2, ObjectList, 0,0, null) != null) {
          root.getChildren.add(Box2)
          boxList += Box2
          val plc: Placement = ((Box2.getTranslateX - Box2.getWidth / 2.toDouble, Box2.getTranslateY - Box2.getWidth / 2.toDouble, Box2.getTranslateZ - Box2.getWidth / 2.toDouble), Box2.getWidth)
          OcNode(plc, OcEmpty, octreeDevelope(Box2, ObjectList, OctreeDimensions, root), OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty)


        } else if (conectionSituation(Box3, ObjectList, 0,0, null) != null) {
          root.getChildren.add(Box3)
          boxList += Box3
          val plc: Placement = ((Box3.getTranslateX - Box3.getWidth / 2.toDouble, Box3.getTranslateY - Box3.getWidth / 2.toDouble, Box3.getTranslateZ - Box3.getWidth / 2.toDouble), Box3.getWidth)
          OcNode(plc, OcEmpty,OcEmpty, octreeDevelope(Box3, ObjectList, OctreeDimensions, root),OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty)


        } else if (conectionSituation(Box4, ObjectList, 0,0, null) != null) {
          root.getChildren.add(Box4)
          boxList += Box4
          val plc: Placement = ((Box4.getTranslateX - Box4.getWidth / 2.toDouble, Box4.getTranslateY - Box4.getWidth / 2.toDouble, Box4.getTranslateZ - Box4.getWidth / 2.toDouble), Box4.getWidth)
          OcNode(plc, OcEmpty, OcEmpty, OcEmpty, octreeDevelope(Box4, ObjectList, OctreeDimensions, root), OcEmpty, OcEmpty, OcEmpty, OcEmpty)


        }else if (conectionSituation(Box5, ObjectList, 0,0, null) != null) {
          root.getChildren.add(Box5)
          boxList += Box5
          val plc: Placement = ((Box5.getTranslateX - Box5.getWidth / 2.toDouble, Box5.getTranslateY - Box5.getWidth / 2.toDouble, Box5.getTranslateZ - Box5.getWidth / 2.toDouble), Box5.getWidth)
          OcNode(plc, OcEmpty,OcEmpty,OcEmpty,OcEmpty, octreeDevelope(Box5, ObjectList, OctreeDimensions, root), OcEmpty,OcEmpty,OcEmpty)

        } else if (conectionSituation(Box6, ObjectList, 0,0, null) != null) {
          root.getChildren.add(Box6)
          boxList += Box6
          val plc: Placement = ((Box6.getTranslateX - Box6.getWidth / 2.toDouble, Box6.getTranslateY - Box6.getWidth / 2.toDouble, Box6.getTranslateZ - Box6.getWidth / 2.toDouble), Box6.getWidth)
          OcNode(plc, OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty, octreeDevelope(Box6, ObjectList, OctreeDimensions, root), OcEmpty,OcEmpty)


        } else if (conectionSituation(Box7, ObjectList, 0,0, null) != null) {
          root.getChildren.add(Box7)
          boxList += Box7
          val plc: Placement = ((Box7.getTranslateX - Box7.getWidth / 2.toDouble, Box7.getTranslateY - Box7.getWidth / 2.toDouble, Box7.getTranslateZ - Box7.getWidth / 2.toDouble), Box7.getWidth)
          OcNode(plc, OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty, octreeDevelope(Box7, ObjectList, OctreeDimensions, root),OcEmpty)


        } else if (conectionSituation(Box8, ObjectList, 0,0, null) != null) {
          root.getChildren.add(Box8)
          boxList += Box8
          val plc: Placement = ((Box8.getTranslateX - Box8.getWidth / 2.toDouble, Box8.getTranslateY - Box8.getWidth / 2.toDouble, Box8.getTranslateZ - Box8.getWidth / 2.toDouble), Box8.getWidth)
          OcNode(plc, OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty,OcEmpty, octreeDevelope(Box8, ObjectList, OctreeDimensions, root))

        } else { //Verificar se alguma das box intersete um elemento present na lista "ObjectList" ou não.
          //val boxList: List[Box] = List(Box1,Box2,Box3,Box4,Box5,Box6,Box7,Box8)
          val result:Octree[Placement] = intersectsSituation(PreBox, ObjectList:List[Shape3D])
          result
        }
    }

  */





  //Tarefa2
  def octreeDevelope(preBox:Box, ObjectList:List[Shape3D], OctreeDimensions:Double, root:Group, octree:Octree[Placement]):Octree[Placement] = {
        val tamanho: Int = preBox.getWidth.toInt / 2
        val origemx: Int = preBox.getTranslateX.toInt
        val origemy: Int = preBox.getTranslateY.toInt
        val origemz: Int = preBox.getTranslateZ.toInt
        //Criação das caixas que dividem o Cubo "Pai" em 8 "filhos"
        val Box1 = createBox(tamanho, origemx - tamanho / 2, origemy - tamanho / 2, tamanho / 2)
        val Box2 = createBox(tamanho, origemx - tamanho / 2 + tamanho, origemy - tamanho / 2 + tamanho, origemz - tamanho / 2)
        val Box3 = createBox(tamanho, origemx - tamanho / 2 + tamanho, origemy - tamanho / 2, origemz - tamanho / 2)
        val Box4 = createBox(tamanho, origemx - tamanho / 2, origemy - tamanho / 2 + tamanho, origemz - tamanho / 2)
        val Box5 = createBox(tamanho, origemx - tamanho / 2, origemy - tamanho / 2, origemz - tamanho / 2 + tamanho)
        val Box6 = createBox(tamanho, origemx - tamanho / 2 + tamanho, origemy - tamanho / 2 + tamanho, origemz - tamanho / 2 + tamanho)
        val Box7 = createBox(tamanho, origemx - tamanho / 2 + tamanho, origemy - tamanho / 2, origemz - tamanho / 2 + tamanho)
        val Box8 = createBox(tamanho, origemx - tamanho / 2, origemy - tamanho / 2 + tamanho, origemz - tamanho / 2 + tamanho)

        val listWiredBox:List[Box] = List(Box1,Box2,Box3,Box4,Box5,Box6,Box7,Box8)
        createWiredList(listWiredBox,ObjectList, root) //Devolve uma lista de wiredBox que contenham Objetos, e adiciona as wiredBox à root
        val wiredBoxNumber:List[Int] = createNumberList(listWiredBox,ObjectList,1) //Devolve uma lista com o numero das box da lista acima.

        if (checkForConnections(listWiredBox, ObjectList, 0, 0) == 2) {
          return intersectsSituation(preBox, ObjectList)
        }

        if (checkForConnections(listWiredBox, ObjectList, 0, 0) == 1) {
          //chama a função recursivamente com a octree defenida
          return nodeDevelope(wiredBoxNumber, octree, listWiredBox, ObjectList, root, OctreeDimensions)
        }
        OcEmpty
  }

  def checkForConnections(wiredList:List[Box], objectlist: List[Shape3D], intersection: Int, contains:Int): Int = {
    wiredList match {
      case Nil =>
        if(intersection == 0 && contains!=0) {
          return 1
        }
        if (intersection !=0) {
          return 2
        }
        0
      case x::xs =>
        if(conectionSituation(x, objectlist, 0,0) == 2) {
          return checkForConnections(xs, objectlist, intersection +1, contains)
        }
        if(conectionSituation(x, objectlist, 0,0) == 1) {
          return checkForConnections(xs, objectlist, intersection , contains +1)
        }
        checkForConnections(xs, objectlist, intersection, contains)
    }
  }
  def createWiredList(wiredList:List[Box], objectlist: List[Shape3D], root:Group): List[Box] = {
    wiredList match {
      case Nil => Nil
      case x::xs =>
        if(conectionSituation(x, objectlist, 0,0) == 1) {
          root.getChildren.add(x)
          boxList += x
          return x::createWiredList(xs, objectlist, root)
        }
          createWiredList(xs, objectlist, root)
    }
  }
  def createNumberList(wiredList:List[Box], objectlist: List[Shape3D], iteration:Int): List[Int] = {
    wiredList match {
      case Nil => Nil
      case x::xs =>
        //println(conectionSituation(x, objectlist, 0,0)) //Imprime a conexão da caixa com um objeto presente na lista
        if(conectionSituation(x, objectlist, 0,0) == 1) {
          //println("Box" + iteration + "Contêm um objeto")
          return iteration::createNumberList(xs, objectlist, iteration+1)
        }
        createNumberList(xs, objectlist, iteration+1)
    }
  }
  def nodeDevelope(wiredBoxNumberList:List[Int], octree:Octree[Placement], wiredBoxList:List[Box], objectList:List[Shape3D], root:Group, octreeDimensions:Double): Octree[Placement] = {
    wiredBoxNumberList match {
      case Nil => octree
      case x::xs=> {
            val resultOctree:Octree[Placement] = nodeDevelope2(x,octree, wiredBoxList, objectList, root, octreeDimensions)
            nodeDevelope(xs,resultOctree, wiredBoxList, objectList, root, octreeDimensions)
      }
    }
  }
  def nodeDevelope2(boxNumber:Int, octree: Octree[Placement], wiredBoxList:List[Box], objectList:List[Shape3D], root:Group, octreeDimensions:Double): Octree[Placement] = {
    octree match {
      case OcEmpty => octree
      case OcNode(plc,node1,node2,node3,node4,node5,node6,node7,node8) => {
        val box:Box = nodeDevelope3(boxNumber,wiredBoxList, 1)
        val plcBox: Placement = ((box.getTranslateX - box.getWidth / 2.toDouble, box.getTranslateY - box.getWidth / 2.toDouble, box.getTranslateZ - box.getWidth / 2.toDouble), box.getWidth)
        val secondOctree:Octree[Placement] = OcNode(plcBox, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty, OcEmpty)
        if(boxNumber==1){
          OcNode(plc, octreeDevelope(box, objectList,octreeDimensions - 1, root, secondOctree),node2, node3, node4, node5, node6, node7, node8)
        } else if(boxNumber==2) {
          OcNode(plc, node1,octreeDevelope(box, objectList,octreeDimensions - 1, root, secondOctree), node3, node4, node5, node6, node7, node8)
        } else if(boxNumber==3) {
          OcNode(plc, node1,node2, octreeDevelope(box, objectList,octreeDimensions - 1, root, secondOctree), node4, node5, node6, node7, node8)
        } else if(boxNumber==4) {
          OcNode(plc, node1,node2, node3, octreeDevelope(box, objectList,octreeDimensions - 1, root, secondOctree), node5, node6, node7, node8)
        } else if(boxNumber==5) {
          OcNode(plc, node1,node2, node3, node4, octreeDevelope(box, objectList,octreeDimensions - 1, root, secondOctree), node6, node7, node8)
        } else if(boxNumber==6) {
          OcNode(plc, node1,node2, node3, node4, node5, octreeDevelope(box, objectList,octreeDimensions - 1, root, secondOctree), node7, node8)
        } else if(boxNumber==7) {
          OcNode(plc, node1,node2, node3, node4, node5, node6, octreeDevelope(box, objectList,octreeDimensions - 1, root, secondOctree), node8)
        } else if(boxNumber==8) {
          OcNode(plc, node1,node2, node3, node4, node5, node6, node7, octreeDevelope(box, objectList,octreeDimensions - 1, root, secondOctree))
        } else {
          OcEmpty
        }
      }
    }
  }
  def nodeDevelope3(i: Int, value: List[Box], iterationnumber:Int): Box = {
    value match {
      case Nil => null
      case x::xs => {
        if(iterationnumber==i) {
          x
        } else {
          nodeDevelope3(i, xs, iterationnumber + 1)
        }
      }
    }
  }

  /*
  def iterationteste(Octree: Octree[Placement], arrayBox: Array[Box], iteration:Int):Octree[Placement] = {
    arrayBox match {
      case null => Octree
      case x::xs => {
        if(x!=null)
      }
    }
  }
   */



  def intersectsSituation(PreBox: Box, ObjectList:List[Shape3D]): Octree[Placement] = {
    ObjectList match {
      case head::tail => {
        if(head.getBoundsInParent.intersects(PreBox.getBoundsInParent)) { //Caso a "boxIntersects" não seja nula, ou seja, caso tenha sido encontrada uma box que de facto se intersete com um dos objetos presentes na "ObjectList", então a função da return de uma OcLeaf
          //val boxIntersects:Box = intersects(BoxList, head)
          val plc: Placement = ((PreBox.getTranslateX - PreBox.getWidth / 2.toDouble, PreBox.getTranslateY - PreBox.getWidth / 2.toDouble, PreBox.getTranslateZ - PreBox.getWidth / 2.toDouble), PreBox.getWidth)
          val listOfObjects = listOFObjectsInLeaf(PreBox, ObjectList)
          val sec: Section = (plc, listOfObjects)
          return OcLeaf(sec)
        }
        intersectsSituation(PreBox, tail)
          //Caso a lista das box não esteja vazia, esta irá se chamar recursivamente até encontrar ou não uma box que intersete um dos objetos presentes na lista "ObjectList"
      }
    }
  }

  def listOFObjectsInLeaf(preBox: Box, ObjectList:List[Shape3D]): List[Shape3D] = {
    ObjectList match {
      case Nil => Nil
      case x::xs => {
        if (x.getBoundsInParent.intersects(preBox.getBoundsInParent)) {
          return x::listOFObjectsInLeaf(preBox, xs)
        }
        listOFObjectsInLeaf(preBox, xs)
      }
    }
  }



  //Função que verifica se determinado cubo interseta um objeto
  /*
  def intersects(boxList:List[Box], objeto:Shape3D): Box = {
    boxList match {
      case Nil => null
      case headBox::tail => {
        if (objeto.getBoundsInParent.intersects(headBox.getBoundsInParent)) {
          headBox
        } // Se a headBox intersetar o objeto, então retorna a propria headBox
        else
          intersects(tail, objeto) //Caso não intersete, continua a invocar a função recursivamente
      }
    }
  }
   */


  //Função que verifica se determinada caixa, contêm/interseta outro.
  def conectionSituation(box: Box, ObjectList:List[Shape3D], contains:Int, intersects:Int): Int = {
    ObjectList match {
      case Nil => {
        if (intersects == 0 && contains != 0) {
          return 1 //Caso a box não intersete nenhum objeto e contenha algum
        }
        if(intersects != 0 ){
          return 2 //Caso a box intersete um objeto
        }
        0 //Caso a box não intersete nem contenha nenhum objeto
      }
      case head :: tail => {
        if (box.asInstanceOf[Shape3D].getBoundsInParent.contains(head.getBoundsInParent)) {
          return conectionSituation(box, tail, contains + 1, intersects)
        }
        if (head.getBoundsInParent.intersects(box.getBoundsInParent)) {
          return conectionSituation(box, tail, contains, intersects + 1)
        }
        conectionSituation(box, tail, contains, intersects)
      }
    }
  }

  //Função para criar as Box
  def createBox(tamanho: Int, scaleX: Int, scaleY: Int, scaleZ: Int): Box = {
    val box = new Box(tamanho, tamanho, tamanho)
    box.setTranslateX(scaleX)
    box.setTranslateY(scaleY)
    box.setTranslateZ(scaleZ)
    box.setMaterial(redMaterial)
    box.setDrawMode(DrawMode.LINE)

    box
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

}
