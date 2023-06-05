package com.marantesss.raytracingscala
package props

enum HitResult:
  case NoHit
  case Hit(
    point: Vec3,
    normal: Vec3,
    t: Double
  )

trait Prop {
  def hit(ray: Ray, tMin: Double, tMax: Double): HitResult
}
