package com.marantesss.raytracingscala

import utils.{Ray, Vec3}

/** Aka Camera
  */
case class Viewport(
    lookFrom: Vec3,
    lookAt: Vec3,
    vup: Vec3,
    /** Vertical field-of-view in degrees
      */
    verticalFOV: Double = 90d,
    aspectRatio: Double = (16d / 9d),
):
  private val height: Double = Math.tan(verticalFOV.toRadians / 2d) * 2d
  private val width: Double  = height * aspectRatio

  private val w = (lookFrom - lookAt).unit
  private val u = vup.cross(w).unit
  private val v = w.cross(u)

  private val focalLength: Double = 1d

  val origin: Vec3     = lookFrom
  val horizontal: Vec3 = width * u
  val vertical: Vec3   = height * v
  val lowerLeftCorner: Vec3 =
    origin - horizontal / 2 - vertical / 2 - w

  def getRay(s: Double, t: Double): Ray =
    Ray(origin, lowerLeftCorner + s * horizontal + t * vertical - origin)
