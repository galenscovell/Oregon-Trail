package galenscovell.hinterstar.util

import com.badlogic.gdx.graphics.Color


/**
  * Stores values used throughout application numerous times.
  */
object Constants {

  // Lighting masks
  val BIT_LIGHT: Short = 1
  val BIT_WALL: Short = 2
  val BIT_GROUP: Short = 5

  // Exact pixel dimensions
  val EXACT_X: Int = 800
  val EXACT_Y: Int = 480

  // Ship interior dimensions
  val TILE_SIZE: Int = 16

  // Custom screen dimension units
  val SCREEN_X: Int = 200
  val SCREEN_Y: Int = 120

  // Map dimensions
  val MAP_WIDTH: Int = 44
  val MAP_HEIGHT: Int = 23
  val SYSTEMMARKER_SIZE: Int = 16
  val SYSTEM_MARKER_X: Int = SYSTEMMARKER_SIZE
  val SYSTEM_MARKER_Y: Int = -SYSTEMMARKER_SIZE * 7
  val SYSTEM_MARKER_CENTER_X: Int = (SYSTEMMARKER_SIZE / 2) + SYSTEMMARKER_SIZE * 3
  val SYSTEM_MARKER_CENTER_Y: Int = -SYSTEMMARKER_SIZE * 4

  // UI Colors
  val NORMAL_UI_COLOR: Color = new Color(0.5f, 0.5f, 0.5f, 1.0f)
  val FLASH_UI_COLOR: Color = new Color(1.0f, 1.0f, 1.0f, 1.0f)
}
