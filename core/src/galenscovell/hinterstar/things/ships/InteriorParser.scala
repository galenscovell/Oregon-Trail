package galenscovell.hinterstar.things.ships

import java.io._

import com.badlogic.gdx.Gdx
import galenscovell.hinterstar.generation.interior.Tile

import scala.collection.mutable.ArrayBuffer


/**
  * Contains a grid of Tiles representing the subsystem actors for a given ship.
  */
class InteriorParser(ship: Ship) {
  private val rootShip: Ship = ship
  private val targetShipName: String = {
    if (ship.isPlayerShip) {
      ship.getName
    } else {
      ship.getName + "-reversed"
    }
  }
  private val tiles: ArrayBuffer[Array[Tile]] = ArrayBuffer()
  private var width: Int = 0
  private var height: Int = 0
  var tileSize: Int = 0

  parse()
  // debugPrint()


  def getTiles: Array[Array[Tile]] = {
    tiles.toArray
  }

  private def parse(): Unit = {
    val reader: BufferedReader = Gdx.files.internal("data/ship_interiors.txt").reader(1024)

    var shipFound: Boolean = false
    var searching: Boolean = true
    var y = 0

    var line: String = reader.readLine()
    while (line != null && searching) {
      if (shipFound && line == "END") {
        searching = false
      } else if (shipFound) {
        val subsystemRow: Array[Tile] = Array.ofDim(width)

        for (x <- 0 until line.length) {
          line(x) match {
            case 'W' => subsystemRow(x) = new Tile(x, y, tileSize, height, "Weapon Control", true, rootShip.isPlayerShip)
            case 'E' => subsystemRow(x) = new Tile(x, y, tileSize, height, "Engine Room", false, rootShip.isPlayerShip)
            case 'H' => subsystemRow(x) = new Tile(x, y, tileSize, height, "Helm", false, rootShip.isPlayerShip)
            case 'S' => subsystemRow(x) = new Tile(x, y, tileSize, height, "Shield Control", false, rootShip.isPlayerShip)
            case 'M' => subsystemRow(x) = new Tile(x, y, tileSize, height, "Medbay", false, rootShip.isPlayerShip)
            case _ => subsystemRow(x) = new Tile(x, y, tileSize, height, "none", false, rootShip.isPlayerShip)
          }
        }

        tiles.append(subsystemRow)
        y += 1
      }

      if (line == targetShipName) {
        shipFound = true
        val dimLine: String = reader.readLine()
        val splitLine: Array[String] = dimLine.split(",")
        width = Integer.valueOf(splitLine(0))
        height = Integer.valueOf(splitLine(1))
        tileSize = Integer.valueOf(splitLine(2))
      }

      line = reader.readLine()
    }

    reader.close()
  }

  private def debugPrint(): Unit = {
    println("Parsed interior")
    for (row: Array[Tile] <- tiles) {
      println()
      for (tile: Tile <- row) {
        print(tile.debugDraw)
      }
    }
    println()
  }
}