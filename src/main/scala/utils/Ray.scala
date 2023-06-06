package com.marantesss.raytracingscala
package utils

import utils.Vec3

case class Ray(
  origin: Vec3,
  direction: Vec3,
):
  def at(t: Double): Vec3 = origin + t * direction
