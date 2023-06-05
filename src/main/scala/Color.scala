package com.marantesss.raytracingscala

/**
 * Byte is [-128, ..., 127]
 */
case class Color (
  red: Int,
  green: Int,
  blue: Int,
):
  override def toString: String = s"$red $green $blue"

  def *(operand: Double): Color = Color(
    (red * operand).toInt,
    (green * operand).toInt,
    (blue * operand).toInt,
  )

  def +(that: Color): Color = Color(
    red + that.red,
    green + that.green,
    blue + that.blue,
  )

  /**
   * Linear interpolation
   */
  def lerpStart(t: Double, endColor: Color): Color = (1.0 - t) * this + t * endColor

object Color:
  val BYTE_SIZE = 255
  def fromRatio(x: Double, y: Double, z: Double): Color = Color(
    (x * BYTE_SIZE).toInt,
    (y * BYTE_SIZE).toInt,
    (z * BYTE_SIZE).toInt,
  )
  def white: Color = Color(255, 255, 255)
  def skyBlue: Color = Color.fromRatio(0.5, 0.7, 1.0)
end Color

