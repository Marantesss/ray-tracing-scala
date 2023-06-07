package com.marantesss.raytracingscala

import utils.{Ray, Vec3}

/** Aka Camera
  */
case class Viewport(
    origin: Vec3 = Vec3.zero,
    height: Double = 2d,
    width: Double = 2d * (16d / 9d),
    focalLength: Double = 1d,
):
  val aspectRatio: Double      = width / height
  private val horizontal: Vec3 = Vec3(width, 0, 0)
  private val vertical: Vec3   = Vec3(0, height, 0)
  private val lowerLeftCorner: Vec3 =
    origin - horizontal / 2 - vertical / 2 - Vec3(0, 0, focalLength)

  def getRay(u: Double, v: Double): Ray =
    Ray(origin, lowerLeftCorner + u * horizontal + v * vertical - origin)
