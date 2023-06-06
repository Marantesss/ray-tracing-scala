package com.marantesss.raytracingscala

import utils.Vec3

case class Viewport(
 origin: Vec3,
 height: Double,
 width: Double,
 focalLength: Double,
):
  val horizontal: Vec3 = Vec3(width, 0, 0)
  val vertical: Vec3 = Vec3(0, height, 0)
  val lowerLeftCorner: Vec3 = origin - horizontal / 2 - vertical / 2 - Vec3(0, 0, focalLength)
  
  def calculatePoint(u: Double, v: Double): Vec3 = lowerLeftCorner + u * horizontal + v * vertical - origin
