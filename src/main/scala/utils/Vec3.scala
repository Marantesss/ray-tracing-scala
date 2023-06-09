package com.marantesss.raytracingscala
package utils

import scala.annotation.{tailrec, targetName}
import scala.compiletime.ops.float.Max
import scala.util.Random

case class Vec3(
    x: Double,
    y: Double,
    z: Double,
):

  def lengthSquared: Double = x * x + y * y + z * z
  def length: Double        = Math.sqrt(lengthSquared)
  def unit: Vec3            = this / length
  def isNearZero: Boolean   = x.abs < 1e-8 && y.abs < 1e-8 && z.abs < 1e-8

  def +(that: Vec3): Vec3      = Vec3(x + that.x, y + that.y, z + that.z)
  def -(that: Vec3): Vec3      = Vec3(x - that.x, y - that.y, z - that.z)
  def *(operand: Double): Vec3 = Vec3(x * operand, y * operand, z * operand)
  def /(operand: Double): Vec3 = this * (1 / operand)
  def unary_-                  = Vec3(-x, -y, -z)
  def dot(that: Vec3): Double  = x * that.x + y * that.y + z * that.z
  def cross(that: Vec3): Vec3 = Vec3(
    y * that.z - z * that.y,
    z * that.x - x * that.z,
    x * that.y - y * that.x,
  )
  def reflect(that: Vec3): Vec3 = this - 2 * this.dot(that) * that

end Vec3

object Vec3:
  def zero: Vec3   = Vec3(0, 0, 0)
  def unit: Vec3   = Vec3(1, 1, 1)
  def random: Vec3 = Vec3(Random.nextDouble(), Random.nextDouble(), Random.nextDouble())
  def random(min: Double, max: Double): Vec3 = Vec3(
    Utility.randomDoubleBetween(min, max),
    Utility.randomDoubleBetween(min, max),
    Utility.randomDoubleBetween(min, max),
  )

  /** Option 1: Good enough Lambertian Reflection
    */
  @tailrec
  def randomInUnitSphere: Vec3 =
    val hypothesis = Vec3.random(-1, 1)
    if hypothesis.lengthSquared < 1 then hypothesis else Vec3.randomInUnitSphere

  /** Option 2: True Lambertian Reflection
    */
  def randomInUnitSphereUnit: Vec3 = randomInUnitSphere.unit

  /** Option 3: An alternative diffuse formulation
    */
  def randomInUnitSphereHemisphere(normal: Vec3): Vec3 =
    val n = Vec3.randomInUnitSphere
    if n.dot(normal) > 0d then n else -n

end Vec3
