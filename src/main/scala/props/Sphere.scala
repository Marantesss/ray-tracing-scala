package com.marantesss.raytracingscala
package props

/**
 * Sphere equation: x² + y² + z² = r²
 *
 * @param center
 * @param radius
 */
case class Sphere(
  center: Vec3 = Vec3.zero,
  radius: Double = 1,
) extends Prop:
  def hit(ray: Ray, tMin: Double, tMax: Double): HitResult =
    val origin = ray.origin - center
    val a = ray.direction.dot(ray.direction)
    val b = 2.0 * origin.dot(ray.direction)
    val c = origin.dot(origin) - Math.pow(radius, 2)
    val discriminant = Math.pow(b, 2) - 4*a*c

    if discriminant < 0 then return HitResult.NoHit

    val t = Seq(
      (-b - Math.sqrt(discriminant)) / 2 * a,
      (-b + Math.sqrt(discriminant)) / 2 * a,
    ).find(x => !(x < tMin || x > tMax))

    if t.isEmpty then return HitResult.NoHit

    val point =  ray.at(t.get)
    HitResult.Hit(
      point,
      (point - center) / radius,
      t.get,
    )
