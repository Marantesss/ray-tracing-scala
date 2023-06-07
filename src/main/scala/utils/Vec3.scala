package com.marantesss.raytracingscala
package utils

import scala.annotation.targetName

case class Vec3(
    x: Double,
    y: Double,
    z: Double,
):

  def lengthSquared: Double = x * x + y * y + z * z
  def length: Double        = Math.sqrt(lengthSquared)

  def unit: Vec3 = this / length

  def +(that: Vec3): Vec3      = Vec3(x + that.x, y + that.y, z + that.z)
  def -(that: Vec3): Vec3      = Vec3(x - that.x, y - that.y, z - that.z)
  def *(operand: Double): Vec3 = Vec3(x * operand, y * operand, z * operand)
  def /(operand: Double): Vec3 = this * (1 / operand)
  def unary_-                  = Vec3(-x, -y, -z)

  def dot(that: Vec3): Double = x * that.x + y * that.y + z * that.z
  def cross(that: Vec3): Vec3 = Vec3(
    y * that.z - z * that.y,
    z * that.x - x * that.z,
    x * that.y - y * that.x,
  )

object Vec3:
  def zero: Vec3 = Vec3(0, 0, 0)
  def unit: Vec3 = Vec3(1, 1, 1)
end Vec3
