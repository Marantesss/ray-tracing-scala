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
    verticalFOV: Double,
    aspectRatio: Double,
    aperture: Double,
    focusDistance: Double,
):
  private val height: Double = Math.tan(verticalFOV.toRadians / 2d) * 2d
  private val width: Double  = height * aspectRatio

  private val w = (lookFrom - lookAt).unit
  private val u = vup.cross(w).unit
  private val v = w.cross(u)

  private val lensRadius: Double = aperture / 2d

  private val origin: Vec3     = lookFrom
  private val horizontal: Vec3 = focusDistance * width * u
  private val vertical: Vec3   = focusDistance * height * v
  private val lowerLeftCorner: Vec3 =
    origin - horizontal / 2 - vertical / 2 - focusDistance * w

  def getRay(s: Double, t: Double): Ray =
    val rd     = lensRadius * Vec3.randomInUnitDisk
    val offset = u * rd.x + v * rd.y

    Ray(
      origin + offset,
      lowerLeftCorner + s * horizontal + t * vertical - origin - offset,
    )
