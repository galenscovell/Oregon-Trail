package galenscovell.hinterstar.util

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import galenscovell.hinterstar.generation.sector._
import galenscovell.hinterstar.processing.Event
import galenscovell.hinterstar.ui.components.gamescreen.GameStage
import galenscovell.hinterstar.ui.screens.GameScreen

import scala.collection.mutable.ArrayBuffer


/**
  * SystemRepo houses System data and methods that are called throughout application.
  * Currently, System data is needed in some areas that aren't created with it.
  * Ideally, this will be able to be eradicated through redesign in architecture.
  */
object SystemRepo {
  val systemsInRange: ArrayBuffer[System] = ArrayBuffer()
  val shapeRenderer: ShapeRenderer = new ShapeRenderer()
  val playerRange: Int = 12

  var gameScreen: GameScreen = _
  var systems: ArrayBuffer[System] = _
  var currentSystem, currentSelection: System = _


// Called from GameScreen //
  /**
    * Sets the gameScreen for SystemRepo.
    * WHY IT'S HERE: Repository has to have gameScreen set but object has no constructor.
    * WORKAROUND IDEAS: Potential for moving all of the GameScreen operations into GameScreen itself?
    */
  def setup(game: GameScreen): Unit = {
    gameScreen = game
  }

  /**
    * Finds all of the Systems in range of the Player's current System.
    * WHY IT'S HERE: SectorView has to display Systems in range of player but it has no access to System data.
    * WORKAROUND IDEAS:
    */
  def setTargetsInRange(): Unit = {
    systemsInRange.clear()

    // println("C", currentSystem.getSystemMarker.sx, currentSystem.getSystemMarker.sy)
    for (system <- systems) {
      val squareDist: Double = Math.pow(currentSystem.getSystemMarker.sx - system.getSystemMarker.sx, 2) + Math.pow(currentSystem.getSystemMarker.sy - system.getSystemMarker.sy, 2)
      if (squareDist <= Math.pow(playerRange, 2)) {
        systemsInRange += system
        // println("U", system.getSystemMarker.sx, system.getSystemMarker.sy)
      }
    }
  }

  /**
    * Use ShapeRenderer to render circles and pathing on navmap.
    * WHY IT'S HERE: SectorView has to display these shapes but has no access to System data.
    * WORKAROUND IDEAS:
    */
  def drawShapes(): Unit = {
    val radius: Float = playerRange * Constants.SYSTEMMARKER_SIZE
    val centerX: Float = currentSystem.getSystemMarker.sx * Constants.SYSTEMMARKER_SIZE + Constants.SYSTEM_MARKER_CENTER_X
    val centerY: Float = Gdx.graphics.getHeight - (currentSystem.getSystemMarker.sy * Constants.SYSTEMMARKER_SIZE) + Constants.SYSTEM_MARKER_CENTER_Y

    shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
    shapeRenderer.setColor(0.95f, 0.61f, 0.07f, 0.6f)
    shapeRenderer.circle(centerX, centerY, radius)
    shapeRenderer.circle(centerX, centerY, 30)

    if (systemsInRange != null && systemsInRange.nonEmpty) {
      shapeRenderer.setColor(0.93f, 0.94f, 0.95f, 0.6f)

      for (system <- systemsInRange) {
        shapeRenderer.line(
          centerX,
          centerY,
          system.getSystemMarker.sx * Constants.SYSTEMMARKER_SIZE + Constants.SYSTEM_MARKER_CENTER_X,
          Gdx.graphics.getHeight - (system.getSystemMarker.sy * Constants.SYSTEMMARKER_SIZE) + Constants.SYSTEM_MARKER_CENTER_Y
        )
      }
    }
    if (currentSelection != null) {
      shapeRenderer.setColor(0.18f, 0.8f, 0.44f, 0.6f)
      val selectionX: Float = currentSelection.getSystemMarker.sx * Constants.SYSTEMMARKER_SIZE + Constants.SYSTEM_MARKER_CENTER_X
      val selectionY: Float = Gdx.graphics.getHeight - (currentSelection.getSystemMarker.sy * Constants.SYSTEMMARKER_SIZE) + Constants.SYSTEM_MARKER_CENTER_Y
      shapeRenderer.circle(selectionX, selectionY, 30)
    }

    shapeRenderer.end()
  }



// Called from GameStage //
  /**
    * Gets the Event for the current System.
    * WHY IT'S HERE: GameStage must display the Event for each System, but has no access to System data.
    * WORKAROUND IDEAS: GameStage has access to GameScreen and vice versa
    */
  def parseNextEvent: Event = {
    currentSystem.getEvent
  }



// Called from SectorView //
  /**
    * If currently selected System is within range of Player, travel to it (set is as CurrentSystem and enter()).
    * WHY IT'S HERE: Travel is called from SectorView, but it has no access to System data.
    * WORKAROUND IDEAS: SectorView has access to GameStage, which has access to GameScreen
    */
  def travelToSelection: Boolean = {
    if (currentSelection != null && systemsInRange.contains(currentSelection)) {
      currentSystem.getSystemMarker.becomeExplored()
      currentSystem = currentSelection
      currentSelection.getSystemMarker.becomeCurrent()
      currentSelection.enter()
      setSelection(null)
      true
    } else {
      false
    }
  }



// Called from SystemMarker //
  /**
    * When a non-EMPTY SystemMarker is selected, set its System as the currentSelection,
    *     then calculates 4 * euclidean distance to it and updateDistanceLabel().
    * WHY IT'S HERE: GameStage has no access to System data.
    * WORKAROUND IDEAS: GameStage has access to GameScreen
    */
  def setSelection(selection: System): Unit = {
    var distance: Double = 0.0

    if (selection != null) {
      currentSelection = selection
      distance = 4 * Math.sqrt(
        Math.pow(currentSystem.getSystemMarker.sx - selection.getSystemMarker.sx, 2) +
          Math.pow(currentSystem.getSystemMarker.sy - selection.getSystemMarker.sy, 2)
      )
    }

    gameScreen.getGameStage.asInstanceOf[GameStage].updateDistanceLabel(f"Distance: $distance%1.1f AU")
  }



// Called from SectorGenerator //
  /**
    * Establishes System data for SystemRepo.
    * Sets the furthest to the left System as the Player's currentSystem.
    * Sets this first System as the Tutorial System.
    * WHY IT'S HERE: SystemRepo needs System data for everything else.
    * WORKAROUND IDEAS: SectorGenerator can be established within GameScreen, created anew for each SectorView init in GameStage
    */
  def populateSystems(systemsToSet: ArrayBuffer[System]): Unit = {
    systems = systemsToSet
    for (system <- systems) {
      if (currentSystem == null || system.x < currentSystem.x) {
        currentSystem = system
      }
    }
    currentSystem.getSystemMarker.becomeCurrent()
    currentSystem.setAsTutorial()
  }
}
