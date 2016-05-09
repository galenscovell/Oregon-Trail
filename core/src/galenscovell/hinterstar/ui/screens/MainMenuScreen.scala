package galenscovell.hinterstar.ui.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.scenes.scene2d._
import com.badlogic.gdx.scenes.scene2d.actions.Actions
import com.badlogic.gdx.scenes.scene2d.ui._
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.viewport.FitViewport
import galenscovell.hinterstar.Hinterstar
import galenscovell.hinterstar.util._


class MainMenuScreen(gameRoot: Hinterstar) extends AbstractScreen(gameRoot) {


  protected override def create(): Unit = {
    this.stage = new Stage(new FitViewport(Constants.EXACT_X, Constants.EXACT_Y), root.spriteBatch)
    val mainTable: Table = new Table
    mainTable.setFillParent(true)

    val titleTable: Table = new Table
    val titleLabel: Label = new Label("Game Name", ResourceManager.labelTitleStyle)
    titleLabel.setAlignment(Align.center, Align.center)
    titleTable.add(titleLabel).width(600).height(80)

    val buttonTable: Table = new Table
    val newGameButton: TextButton = new TextButton("New", ResourceManager.buttonMenuStyle)
    newGameButton.getLabel.setAlignment(Align.bottom, Align.center)
    newGameButton.addListener(new ClickListener() {
      override def clicked(event: InputEvent, x: Float, y: Float) {
        root.newGame()
        stage.getRoot.addAction(Actions.sequence(
          Actions.fadeOut(0.5f),
          toStartScreenAction)
        )
      }
    })
    val continueGameButton: TextButton = new TextButton("Load", ResourceManager.buttonMenuStyle)
    continueGameButton.getLabel.setAlignment(Align.bottom, Align.center)
    continueGameButton.addListener(new ClickListener() {
      override def clicked(event: InputEvent, x: Float, y: Float) {
      }
    })
    val settingButton: TextButton = new TextButton("Settings", ResourceManager.buttonMenuStyle)
    settingButton.getLabel.setAlignment(Align.bottom, Align.center)
    settingButton.addListener(new ClickListener() {
      override def clicked(event: InputEvent, x: Float, y: Float) {
      }
    })
    val quitButton: TextButton = new TextButton("Quit", ResourceManager.buttonMenuStyle)
    quitButton.getLabel.setAlignment(Align.bottom, Align.center)
    quitButton.addListener(new ClickListener() {
      override def clicked(event: InputEvent, x: Float, y: Float) {
        stage.getRoot.addAction(Actions.sequence(
          Actions.fadeOut(0.5f),
          quitGameAction)
        )
      }
    })

    buttonTable.add(newGameButton).width(180).height(360).padRight(20)
    buttonTable.add(continueGameButton).width(180).height(360).padRight(20)
    buttonTable.add(settingButton).width(180).height(360).padRight(20)
    buttonTable.add(quitButton).width(180).height(360)

    mainTable.add(titleTable).width(760).height(80).center.pad(8)
    mainTable.row
    mainTable.add(buttonTable).width(760).height(360).center.pad(8)

    stage.addActor(mainTable)
    mainTable.addAction(Actions.sequence(
      Actions.fadeOut(0),
      Actions.fadeIn(0.5f))
    )
  }



  /**
    * Custom Scene2D Actions
    */
  private[screens] var toStartScreenAction: Action = new Action() {
    def act(delta: Float): Boolean = {
      root.setScreen(root.startScreen)
      true
    }
  }
  private[screens] var toGameScreenAction: Action = new Action() {
    def act(delta: Float): Boolean = {
      root.setScreen(root.gameScreen)
      true
    }
  }
  private[screens] var quitGameAction: Action = new Action() {
    def act(delta: Float): Boolean = {
      Gdx.app.exit()
      true
    }
  }
}
