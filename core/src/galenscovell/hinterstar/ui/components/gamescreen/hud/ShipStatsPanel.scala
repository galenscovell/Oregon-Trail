package galenscovell.hinterstar.ui.components.gamescreen.hud

import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.ui._
import galenscovell.hinterstar.ui.components.gamescreen.stages.HudStage
import galenscovell.hinterstar.util._


class ShipStatsPanel(stage: HudStage) extends Table {
  private val hudStage: HudStage = stage
  private val shipStatsTable: Table = new Table
  private val statIcons: Array[Image] = Array(
    new Image(new Sprite(Resources.atlas.createSprite("shield-icon"))),
    new Image(new Sprite(Resources.atlas.createSprite("engine-icon"))),
    new Image(new Sprite(Resources.atlas.createSprite("weapon-icon")))
  )

  construct()
  refreshStats()


  private def construct(): Unit = {
    this.add(shipStatsTable).expand.left.padLeft(4)
  }

  def refreshStats(): Unit = {
    shipStatsTable.clear()

    val stats: Array[Float] = PlayerData.getShipStats

    for (i <- stats.indices) {
      val statTable: Table = new Table
      statTable.setBackground(Resources.npTest4)

      val statKeyTable: Table = new Table
      statKeyTable.add(statIcons(i)).expand.fill.width(24).height(24).center.pad(4)

      val statValueTable: Table = new Table
      val statValueLabel: Label = new Label(stats(i).toString, Resources.labelTinyStyle)
      statValueTable.add(statValueLabel).expand.center.pad(4)

      statTable.add(statKeyTable).expand.left
      statTable.add(statValueTable).expand.fill.center

      shipStatsTable.add(statTable).width(64).height(32).center.padRight(4)
    }
  }
}
