package galenscovell.hinterstar.ui.components.gamescreen.hud

import com.badlogic.gdx.scenes.scene2d._
import com.badlogic.gdx.scenes.scene2d.ui._
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import galenscovell.hinterstar.generation.sector._
import galenscovell.hinterstar.ui.components.gamescreen.stages.InterfaceStage
import galenscovell.hinterstar.util._


class TravelPanel(interfaceStage: InterfaceStage) extends Table {
  private var distanceLabel: Label = _

  construct()



  private def construct(): Unit = {
    this.setFillParent(true)

    val topTable: Table = createTopTable
    val mapTable: Table = createMapTable
    val bottomTable: Table = createBottomTable

    this.add(topTable).width(Constants.EXACT_X).height(48)
    this.row
    this.add(mapTable).width(Constants.EXACT_X).height(Constants.EXACT_Y - 96)
    this.row
    this.add(bottomTable).width(Constants.EXACT_X).height(48)
  }

  private def createTopTable: Table = {
    val table: Table = new Table
    table.setBackground(Resources.npDarkBlue)

    val closeButton: TextButton = new TextButton("Close", Resources.blueButtonStyle)
    closeButton.addListener(new ClickListener() {
      override def clicked(event: InputEvent, x: Float, y: Float) {
        interfaceStage.closeTravelPanel()
      }
    })
    table.add(closeButton).expand.width(96).height(40).right.padRight(10)

    table
  }

  private def createMapTable: Table = {
    val mapTable: Table = new Table
    generateMap(mapTable)
    mapTable.setBackground(Resources.npDarkGray)
    mapTable
  }

  private def generateMap(container: Table): Unit =  {
    // TODO: Each new map has randomized SystemMarker layout (depending on difficulty)
    val sectorGenerator: SectorGenerator = new SectorGenerator(16, 2)
    val systemMarkers: Array[Array[SystemMarker]] = sectorGenerator.getSystemMarkers

    // container.setDebug(true)
    for (row: Array[SystemMarker] <- systemMarkers) {
      for (systemMarker: SystemMarker <- row) {
        container.add(systemMarker).width(Constants.SYSTEMMARKER_SIZE).height(Constants.SYSTEMMARKER_SIZE)
      }
      container.row
    }
  }

  private def createBottomTable: Table = {
    val table: Table = new Table
    table.setBackground(Resources.npDarkBlue)

    val travelButton: TextButton = new TextButton("Warp", Resources.blueButtonStyle)
    travelButton.addListener(new ClickListener() {
      override def clicked(event: InputEvent, x: Float, y: Float) {
        travelToSystem()
      }
    })
    distanceLabel = new Label("Distance: 0 AU", Resources.labelLargeStyle)
    table.add(distanceLabel).expand.fill.left.padLeft(10)
    table.add(travelButton).width(96).height(40).expand.fill.right.padRight(10)

    table
  }

  private def travelToSystem(): Unit = {
    if (SystemOperations.travelToSelection) {
      interfaceStage.closeTravelPanel()
      interfaceStage.disableTravelButton()
      interfaceStage.getGameScreen.beginTravel()
      interfaceStage.hideUI()
    }
  }
  
  def updateDistanceLabel(d: String): Unit = {
    distanceLabel.setText(d)
  }
}
