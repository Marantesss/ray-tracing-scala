package com.marantesss.raytracingscala
package props

import utils.{Ray, Vec3}

enum HitResult:
  case NoHit
  case Hit(
    point: Vec3,
    normal: Vec3,
    t: Double,
    frontFacing: Boolean = true,
  )

  /**
   * 1. A hit will always have 2 normals in opposite directions. We want the normal to always point outwards.
   * 2. Moreover, a Hit can happen from the inside or the outside of the Prop.
   *
   * Given these 2 variants, we should set frontFacing and normal accordingly
   * @param ray
   * @return
   */
  def setFaceNormal(ray: Ray): HitResult = this match
    case NoHit => NoHit
    case Hit(p, n, t, _) =>
      val frontFacing = ray.direction.dot(n) < 0
      val normal = if frontFacing then n else -n
      Hit(p, normal, t, frontFacing)



trait Prop {
  def hit(ray: Ray, tMin: Double, tMax: Double): HitResult
}
