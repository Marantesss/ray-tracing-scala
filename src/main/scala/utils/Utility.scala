package com.marantesss.raytracingscala
package utils

import scala.util.Random

object Utility:

  def randomDoubleBetween(min: Double, max: Double): Double =
    min + (max - min) * Random.nextDouble()

  def clamp(num: Double, min: Double, max: Double): Double =
    Math.min(Math.max(num, min), max)

end Utility
