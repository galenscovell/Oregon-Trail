package galenscovell.hinterstar.generation.sector

import galenscovell.hinterstar.util._

import scala.collection.mutable.ArrayBuffer


/**
  * MapGenerator constructs SystemMarker grid and places non-overlapping Systems.
  */
class SectorGenerator(maxSystems: Int, padSize: Int) {
  private val systemMarkers: Array[Array[SystemMarker]] = Array.ofDim[SystemMarker](Constants.MAP_HEIGHT, Constants.MAP_WIDTH)
  private val systems: ArrayBuffer[System] = ArrayBuffer()

  build()
  placeSystems()
  SystemRepo.populateSystems(systems)


  /**
    * Return 2D Array of SystemMarkers.
    */
  def getSystemMarkers: Array[Array[SystemMarker]] = {
    systemMarkers
  }

  /**
    * Construct SystemMarker[MAP_HEIGHT][MAP_WIDTH] of all empty SystemMarkers.
    */
  private def build(): Unit = {
    for (x <- 0 until Constants.MAP_WIDTH) {
      for (y <- 0 until Constants.MAP_HEIGHT) {
        systemMarkers(y)(x) = new SystemMarker(x, y)
      }
    }
  }

  /**
    * Place random Systems, ensuring that they are distanced apart.
    * TODO: Ensure that there is at least one full path from start to finish.
    */
  private def placeSystems(): Unit = {
    var attempts: Int = 240

    while (attempts > 0 && systems.length < maxSystems) {
      val x: Int = getRandom(1, Constants.MAP_WIDTH - padSize - 1)
      val y: Int = getRandom(1, Constants.MAP_HEIGHT - padSize - 1)
      if (!(x == 0 || x == Constants.MAP_WIDTH || y == 0 || y == Constants.MAP_HEIGHT)) {
        val system: System = new System(x, y, padSize)
        if (!doesCollide(system)) {
          val centerX: Int = (system.size / 2) + system.x
          val centerY: Int = (system.size / 2) + system.y
          val systemMarker: SystemMarker = systemMarkers(centerY)(centerX)
          system.setSystemMarker(systemMarker)
          systemMarker.setSystem(system)
          systems += system
        }
      }
      attempts -= 1
    }
  }

  /**
    * Return if target System overlaps an already placed System.
    */
  private def doesCollide(system: System): Boolean = {
    for (i <- systems.indices) {
      val check: System = systems(i)
      if (!((system.x + system.size < check.x - 2) ||
            (system.x - 2 > check.x + check.size) ||
            (system.y + system.size < check.y - 2) ||
            (system.y - 2 > check.y + check.size))) {
        return true
      }
    }
    false
  }

  /**
    * Return a random integer between low and high.
    */
  private def getRandom(low: Int, high: Int): Int = {
    (Math.random * (high - low)).toInt + low
  }


  def debugPrint(): Unit = {
    println()
    for (row: Array[SystemMarker] <- systemMarkers) {
      for (sm: SystemMarker <- row) {
        print(sm.debugDraw())
      }
      println()
    }
    println()
  }
}

