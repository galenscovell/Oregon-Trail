package galenscovell.hinterstar.ui.components.startscreen

import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui._
import com.badlogic.gdx.scenes.scene2d.utils.{ClickListener, TextureRegionDrawable}
import com.badlogic.gdx.scenes.scene2d.{Action, InputEvent}
import com.badlogic.gdx.utils.{Align, Scaling}
import galenscovell.hinterstar.things.parts.Weapon
import galenscovell.hinterstar.things.ships.{Ship, ShipParser}
import galenscovell.hinterstar.util._


class ShipSelectPanel extends Table {
  private val allShips: Array[Ship] = new ShipParser().parseAll.toArray
  private var currentShipIndex: Int = 0

  private val shipDisplay: Table = new Table
  private val shipImage: Image = new Image
  private val shipDetail: Table = new Table

  shipImage.setScaling(Scaling.fillY)
  shipDetail.setBackground(Resources.npDarkBlue)
  shipDetail.setColor(Constants.NORMAL_UI_COLOR)

  construct()



  def getShip: Ship = {
    allShips(currentShipIndex)
  }

  private def construct(): Unit = {
    val topTable: Table = createTopTable
    val bottomTable: Table = new Table

    bottomTable.add(shipDetail)

    this.add(topTable).width(520).height(140).center
    this.row
    this.add(bottomTable).width(520).height(260).center
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

    topTable.add(scrollLeftButton).width(60).height(130).expand.fill.left
    topTable.add(shipDisplay).expand.fill.center
    topTable.add(scrollRightButton).width(60).height(130).expand.fill.right

    topTable
  }

  def updateShipDisplay(transitionRight: Boolean): Unit = {
    var amount: Int = 60
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
          Actions.moveBy(0, 6, 5.0f),
          Actions.moveBy(0, -6, 5.0f)
        )
      )
    ))
  }

  def updateShipDetails(): Unit = {
    shipDetail.clear()

    // Ship Detail Display
    val shipDetailTop: Table = new Table
    shipDetailTop.setBackground(Resources.npDarkBlue)

    val shipNameLabel: Label = new Label(allShips(currentShipIndex).getName, Resources.labelLargeStyle)
    shipNameLabel.setAlignment(Align.center, Align.center)
    val shipDescLabel: Label = new Label(allShips(currentShipIndex).getDescription, Resources.labelMediumStyle)
    shipDescLabel.setAlignment(Align.top, Align.center)
    shipDescLabel.setWrap(true)

    shipDetailTop.add(shipNameLabel).expand.fill.height(20).pad(5)
    shipDetailTop.row
    shipDetailTop.add(shipDescLabel).expand.fill.height(60).pad(5)

    val shipDetailBottom: Table = new Table

    // Ship Weapon Display
    // Max # starting weapons for any given ship is 4
    val shipWeaponTable: Table = new Table
    for (weapon: Weapon <- allShips(currentShipIndex).getWeapons) {
      val weaponTable: Table = new Table
      weaponTable.setBackground(Resources.npDarkGray)

      val topBarTable: Table = new Table
      topBarTable.setBackground(Resources.npBlue)
      val iconTable = new Table
      iconTable.setBackground(Resources.npGreen)
      val damageLabel: Label = new Label(weapon.getDamage.toString, Resources.labelSmallStyle)
      damageLabel.setAlignment(Align.center)

      topBarTable.add(iconTable).expand.fill.width(20).height(20)
      topBarTable.add(damageLabel).expand.fill.width(80).height(20)

      val weaponImage: Table = new Table

      val bottomWeaponTable: Table = new Table
      bottomWeaponTable.setBackground(Resources.npDarkBlue)
      val weaponLabel: Label = new Label(weapon.getName, Resources.labelSmallStyle)
      weaponLabel.setAlignment(Align.center)

      bottomWeaponTable.add(weaponLabel).expand.fill

      weaponTable.add(topBarTable).expand.fill.height(20)
      weaponTable.row
      weaponTable.add(weaponImage).expand.fill.height(50)
      weaponTable.row
      weaponTable.add(bottomWeaponTable).expand.fill.height(20)

      shipWeaponTable.add(weaponTable).width(100).pad(5)
    }

    // Ship Subsystem Display
    // Max # subsystems for any given ship is 8
    val shipSubsystemTable: Table = new Table
    val shipSubsystems: Array[String] = allShips(currentShipIndex).getSubsystemNames

    // Row 1
    for (i <- 0 until 4) {
      val subsystemTable: Table = new Table

      if (shipSubsystems.length >= i + 1) {
        val subsystem: String = shipSubsystems(i)

        subsystemTable.setBackground(Resources.npDarkGray)
        val subsystemLabel: Label = new Label(subsystem, Resources.labelTinyStyle)
        subsystemLabel.setAlignment(Align.center)
        subsystemTable.add(subsystemLabel).expand.fill.pad(2)
      }
      shipSubsystemTable.add(subsystemTable).width(120).pad(2)
    }

    shipSubsystemTable.row

    // Row 2
    for (i <- 4 until 8) {
      val subsystemTable: Table = new Table

      if (shipSubsystems.length >= i + 1) {
        val subsystem: String = shipSubsystems(i)

        subsystemTable.setBackground(Resources.npDarkGray)
        val subsystemLabel: Label = new Label(subsystem, Resources.labelTinyStyle)
        subsystemLabel.setAlignment(Align.center)
        subsystemTable.add(subsystemLabel).expand.fill.pad(2)
      }
      shipSubsystemTable.add(subsystemTable).width(120).pad(2)
    }

    shipDetailBottom.add(shipWeaponTable).expand.fill.height(95).pad(2)
    shipDetailBottom.row
    shipDetailBottom.add(shipSubsystemTable).expand.fill.height(30).pad(2)

    shipDetail.add(shipDetailTop).expand.fill.width(516).height(100).top.pad(2)
    shipDetail.row
    shipDetail.add(shipDetailBottom).expand.fill.width(516).height(156).top.pad(2)

    shipDetail.addAction(Actions.sequence(
      Actions.color(Constants.FLASH_UI_COLOR, 0.25f, Interpolation.sine),
      Actions.color(Constants.NORMAL_UI_COLOR, 0.25f, Interpolation.sine)
    ))
  }



  /***************************
    * Custom Scene2D Actions *
    ***************************/
  private[startscreen] var updateShipDisplayAction: Action = new Action() {
    def act(delta: Float): Boolean = {
      val shipName: String = allShips(currentShipIndex).getName
      shipImage.setDrawable(new TextureRegionDrawable(Resources.atlas.findRegion(shipName)))

      if (!shipImage.hasParent) {
        shipDisplay.add(shipImage).height(130)
      }
      true
    }
  }
}
