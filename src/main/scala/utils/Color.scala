package com.marantesss.raytracingscala
package utils

/** Byte is [-128, ..., 127]
  */
case class Color(
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

  def /(operand: Double): Color = Color(
    (red / operand).toInt,
    (green / operand).toInt,
    (blue / operand).toInt,
  )

  def +(that: Color): Color = Color(
    red + that.red,
    green + that.green,
    blue + that.blue,
  )

  def pow(operand: Double): Color = Color(
    Math.pow(red, operand).toInt,
    Math.pow(green, operand).toInt,
    Math.pow(blue, operand).toInt,
  )

  /** Linear interpolation
    */
  def lerpStart(t: Double, endColor: Color): Color = (1.0 - t) * this + t * endColor

object Color:
  val BYTE_SIZE = 255
  def fromRatio(x: Double, y: Double, z: Double): Color = Color(
    (x * BYTE_SIZE).toInt,
    (y * BYTE_SIZE).toInt,
    (z * BYTE_SIZE).toInt,
  )
  def white: Color   = Color(255, 255, 255)
  def black: Color   = Color(0, 0, 0)
  def skyBlue: Color = Color.fromRatio(0.5, 0.7, 1.0)
  def red: Color     = Color.fromRatio(1, 0, 0)
end Color
