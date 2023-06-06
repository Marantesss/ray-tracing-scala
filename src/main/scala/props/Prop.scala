package com.marantesss.raytracingscala
package props

import utils.{Ray, Vec3}

enum HitResult:
  case NoHit
  case Hit(
    /**
     * 3D coords where the ray hit the prop
     */
    point: Vec3,

    /**
     * prop surface perpendicular vector starting in point
     */
    normal: Vec3,

    /**
     * distance from ray origin and point
     */
    t: Double,

    /**
     * was the prop hit from the inside (backFacing) or the outside (frontFacing)
     */
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
    case HitResult.NoHit => NoHit
    case HitResult.Hit(p, n, t, _) =>
      val frontFacing = ray.direction.dot(n) < 0
      val normal = if frontFacing then n else -n
      Hit(p, normal, t, frontFacing)

  /**
   * TODO refactor this enum to avoid hacks like these
   * @return
   */
  def distanceToOrigin: Double = this match
    case HitResult.NoHit => Double.MinValue
    case HitResult.Hit(_p, _n, t, _) => t



trait Prop {
  def hit(ray: Ray, tMin: Double, tMax: Double): HitResult
}
