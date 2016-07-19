package galenscovell.hinterstar.ui.components.startscreen

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.{Image, Label, Table, TextButton}
import com.badlogic.gdx.scenes.scene2d.utils.{ClickListener, TextureRegionDrawable}
import com.badlogic.gdx.scenes.scene2d.{Action, InputEvent}
import com.badlogic.gdx.utils.{Align, Scaling}
import galenscovell.hinterstar.things.parts.Part
import galenscovell.hinterstar.things.ships.{Ship, ShipParser}
import galenscovell.hinterstar.util._

import scala.collection.mutable.{ArrayBuffer, Map}


class ShipSelectPanel extends Table {
  private val partTypes: List[String] = List(
    "Combat", "Defense", "Mobility", "Power", "Storage"
  )
  private val allShips: Array[Ship] = new ShipParser().parseAll.toArray
  private var currentShipIndex: Int = 0

  private val shipDisplay: Table = new Table
  private val shipImage: Image = new Image
  private val shipDetail: Table = new Table

  shipImage.setScaling(Scaling.fillY)
  shipDetail.setBackground(Resources.npTest1)
  shipDetail.setColor(Constants.NORMAL_UI_COLOR)

  construct()


  def getShip: Ship = {
    allShips(currentShipIndex)
  }

  private def construct(): Unit = {
    val topTable: Table = createTopTable
    val bottomTable: Table = new Table

    bottomTable.add(shipDetail).expand.fill

    add(topTable).width(780).height(190).center
    row
    add(bottomTable).width(780).height(190).padTop(10).padBottom(10).center
  }

  private def createTopTable: Table = {
    val topTable: Table = new Table

    val scrollLeftButton: TextButton = new TextButton("<", Resources.blueButtonStyle)
    scrollLeftButton.addListener(new ClickListener() {
      override def clicked(event: InputEvent, x: Float, y: Float): Unit = {
        currentShipIndex -= 1
        if (currentShipIndex < 0) {
          currentShipIndex = allShips.length - 1
        }
        updateShipDisplay(false)
        updateShipDetails()
      }
    })
    val scrollRightButton: TextButton = new TextButton(">", Resources.blueButtonStyle)
    scrollRightButton.addListener(new ClickListener() {
      override def clicked(event: InputEvent, x: Float, y: Float): Unit = {
        currentShipIndex += 1
        if (currentShipIndex > allShips.length - 1) {
          currentShipIndex = 0
        }
        updateShipDisplay(true)
        updateShipDetails()
      }
    })

    topTable.add(scrollLeftButton).width(60).height(140).expand.fill.left
    topTable.add(shipDisplay).expand.fill
    topTable.add(scrollRightButton).width(60).height(140).expand.fill.right

    topTable
  }

  def updateShipDisplay(transitionRight: Boolean): Unit = {
    var amount: Int = 100
    var origin: Int = 0
    if (!transitionRight) {
      origin = amount * 2
      amount = -amount
    }

    shipDisplay.clearActions()
    shipDisplay.addAction(Actions.sequence(
      Actions.parallel(
        Actions.fadeOut(0.2f, Interpolation.sine),
        Actions.moveBy(amount, 0, 0.2f, Interpolation.sine)
      ),
      updateShipDisplayAction,
      Actions.moveTo(origin, 0),
      Actions.parallel(
        Actions.fadeIn(0.2f, Interpolation.sine),
        Actions.moveBy(amount, 0, 0.2f, Interpolation.sine)
      ),
      Actions.forever(
        Actions.sequence(
          Actions.moveBy(0, 8, 4.0f),
          Actions.moveBy(0, -8, 4.0f)
        )
      )
    ))
  }

  def updateShipDetails(): Unit = {
    shipDetail.clear()

    val shipDetailTop: Table = new Table
    shipDetailTop.setBackground(Resources.npTest1)
    val shipName: String = allShips(currentShipIndex).getName
    val shipDesc: String = allShips(currentShipIndex).getDescription
    val shipNameLabel: Label = new Label(shipName, Resources.labelMenuStyle)
    shipNameLabel.setAlignment(Align.center, Align.center)
    val shipDescLabel: Label = new Label(shipDesc, Resources.labelMediumStyle)
    shipDescLabel.setAlignment(Align.top, Align.center)
    shipDescLabel.setWrap(true)

    shipDetailTop.add(shipNameLabel).expand.fill.height(20).pad(5)
    shipDetailTop.row
    shipDetailTop.add(shipDescLabel).expand.fill.height(60).pad(5)

    val shipDetailBottom: Table = new Table
    val startingPartsTable: Table = new Table
    val shipStartingParts: Map[String, ArrayBuffer[Part]] = allShips(currentShipIndex).getParts
    for (pt: String <- partTypes) {
      val parts: Array[Part] = shipStartingParts.get(pt).get.toArray
      for (p: Part <- parts) {
        val partTable: Table = new Table
        partTable.setBackground(Resources.npTest4)

        val topBarTable: Table = new Table
        topBarTable.setBackground(Resources.npTest3)
        val iconTable = new Table
        iconTable.setBackground(Resources.npTest0)
        val typeLabel: Label = new Label(pt, Resources.labelDetailStyle)
        typeLabel.setAlignment(Align.center)

        topBarTable.add(iconTable).expand.fill.width(20).height(20)
        topBarTable.add(typeLabel).expand.fill.width(80).height(20)

        val partImage: Table = new Table

        val bottomPartTable: Table = new Table
        bottomPartTable.setBackground(Resources.npTest1)
        val partLabel: Label = new Label(p.getName, Resources.labelDetailStyle)
        partLabel.setAlignment(Align.center)

        bottomPartTable.add(partLabel).expand.fill

        partTable.add(topBarTable).expand.fill.height(20)
        partTable.row
        partTable.add(partImage).expand.fill.height(50)
        partTable.row
        partTable.add(bottomPartTable).expand.fill.height(20)

        startingPartsTable.add(partTable).width(100).pad(5)
      }
    }
    shipDetailBottom.add(startingPartsTable).expand.fill.pad(4)

    shipDetail.add(shipDetailTop).expand.fill.height(95).top.pad(5)
    shipDetail.row
    shipDetail.add(shipDetailBottom).expand.fill.height(95).top.pad(5)

    shipDetail.addAction(Actions.sequence(
      Actions.color(Constants.FLASH_UI_COLOR, 0.25f, Interpolation.sine),
      Actions.color(Constants.NORMAL_UI_COLOR, 0.25f, Interpolation.sine)
    ))
  }



  /**
    * Custom Scene2D Actions
    */
  private[startscreen] var updateShipDisplayAction: Action = new Action() {
    def act(delta: Float): Boolean = {
      val shipName: String = allShips(currentShipIndex).getName
      shipImage.setDrawable(new TextureRegionDrawable(Resources.shipAtlas.findRegion(shipName)))

      if (!shipImage.hasParent) {
        shipDisplay.add(shipImage).height(130)
      }
      true
    }
  }
}
