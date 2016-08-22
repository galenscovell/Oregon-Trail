package galenscovell.hinterstar.ui.components.gamescreen.stages

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Interpolation
import com.badlogic.gdx.scenes.scene2d._
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.FitViewport
import galenscovell.hinterstar.processing.CombatHandler
import galenscovell.hinterstar.things.entities.{Npc, Player}
import galenscovell.hinterstar.ui.screens.GameScreen
import galenscovell.hinterstar.util._


class ActionStage(game: GameScreen, viewport: FitViewport, spriteBatch: SpriteBatch) extends Stage(viewport, spriteBatch) {
  private val gameScreen: GameScreen = game
  private val player: Player = new Player(this)
  private var npc: Npc = new Npc(this)
  private val combatHandler: CombatHandler = new CombatHandler(player.getShip)

  private val leftTable: Table = new Table
  private val rightTable: Table = new Table

  construct()


  private def construct(): Unit = {
    val mainTable: Table = new Table
    mainTable.setFillParent(true)

    val actionTable: Table = new Table

    leftTable.add(player).expand.fill.left
    rightTable.add(npc).expand.fill.right

    actionTable.add(leftTable).left.padLeft(10)
    actionTable.add(rightTable).expand.right.padRight(10)

    mainTable.add(actionTable).width(Constants.EXACT_X * 1.5f).expand.fill.padLeft(Constants.EXACT_X / 2)

    this.addActor(mainTable)
  }

  def updatePlayerAnimation(): Unit = {
    player.clearActions()
    player.addAction(Actions.sequence(
      Actions.moveBy(80, 0, 1.75f, Interpolation.exp5In),
      Actions.moveBy(0, 4, 1.25f, Interpolation.linear),
      Actions.moveBy(0, -8, 1.75f, Interpolation.linear),
      Actions.moveBy(0, 4, 1.25f, Interpolation.linear),
      Actions.moveBy(-80, 0, 2.0f, Interpolation.exp5In),
      Actions.forever(
        Actions.sequence(
          Actions.moveBy(0, 8, 4.0f),
          Actions.moveBy(0, -8, 4.0f)
        ))
    ))
  }

  def toggleSubsystemOverlay(): Unit = {
    if (player.overlayPresent()) {
      player.disableOverlay()
    } else {
      player.enableOverlay()
    }
  }

  def disableSubsystemOverlay(): Unit = {
    if (player.overlayPresent()) {
      player.disableOverlay()
    }
  }

  def getGameScreen: GameScreen = {
    gameScreen
  }


  /*******************
    *     Combat     *
    *******************/
  def combatUpdate(): Unit = {
    combatHandler.update(player.getShip.updateActiveWeapons(), npc.getShip.updateActiveWeapons())
  }

  def combatRender(delta: Float): Unit = {
    combatHandler.render(delta)
  }
}
