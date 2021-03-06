package galenscovell.hinterstar.ui.components.gamescreen.hud

import com.badlogic.gdx.scenes.scene2d.ui._
import com.badlogic.gdx.utils.Align
import galenscovell.hinterstar.processing.Event
import galenscovell.hinterstar.ui.components.gamescreen.stages.InterfaceStage
import galenscovell.hinterstar.util._


class EventPanel(interfaceStage: InterfaceStage, eventContainer: Event) extends Table {
  construct()



  def construct(): Unit = {
    this.setFillParent(true)
    val mainTable: Table = new Table
    mainTable.setBackground(Resources.npDarkGray)

    val titleTable: Table = createTitleTable(eventContainer.name)

    val centerTable: Table = new Table
    val descriptionTable: Table = createDescriptionTable(eventContainer.description)
    val optionTable: Table = createOptionTable

    centerTable.add(descriptionTable).height(100).expand.fill.top
    centerTable.row
    centerTable.add(optionTable).width(680).height(250).expand.fill

    mainTable.add(titleTable).height(50).expand.fill.top
    mainTable.row
    mainTable.add(centerTable).height(350).expand.fill.top

    this.add(mainTable).width(680).height(400).expand.fill.center
  }

  private def createTitleTable(eventTitle: String): Table = {
    val titleTable: Table = new Table
    titleTable.setBackground(Resources.npDarkBlue)
    val titleLabel: Label = new Label(eventTitle, Resources.labelXLargeStyle)
    titleLabel.setAlignment(Align.center, Align.center)
    titleTable.add(titleLabel)
    titleTable
  }

  private def createDescriptionTable(eventDescription: String): Table = {
    val descriptionTable: Table = new Table
    val descriptionLabel: Label = new Label(eventDescription, Resources.labelSmallStyle)
    descriptionLabel.setWrap(true)
    descriptionLabel.setAlignment(Align.center, Align.left)
    descriptionTable.add(descriptionLabel).width(680)
    descriptionTable
  }

  private def createOptionTable: Table = {
    val optionTable: Table = new Table
    optionTable.setBackground(Resources.npDarkBlue)

    for (choice <- eventContainer.choices) {
      val choiceText: String = choice.get("choice-text") match {
        case Some(a) => s"$a"
        case None    => "ERROR: Choice text not found"
      }
      val choiceButton: TextButton = new TextButton(choiceText, Resources.buttonEventStyle)
      choiceButton.getLabel.setAlignment(Align.left, Align.left)
      choiceButton.getLabel.setWrap(true)
      choiceButton.getLabelCell.padLeft(20).padRight(20)
      optionTable.add(choiceButton).width(680).height(50).expand.fill.top.padTop(2).padBottom(2)
      optionTable.row
    }

    optionTable
  }
}
