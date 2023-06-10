package com.marantesss.raytracingscala
package material

import utils.{Color, HitResult, Ray, ScatterResult, Utility, Vec3}

case class Metal(
    albedo: Color,
    fuzz: Double = 1,
) extends Material(albedo):
  private val fuzziness = Utility.clamp(fuzz, 0d, 1d)

  /** Only consider reflections on the same plane as hit normal
    */
  def scatter(ray: Ray, hit: HitResult): Option[ScatterResult] =
    val scatterReflection = ray.direction.reflect(hit.normal).unit
    val scattered         = Ray(hit.point, scatterReflection + fuzziness * Vec3.randomInUnitSphere)
    if scattered.direction.dot(hit.normal) > 0 then Option(ScatterResult(scattered, albedo))
    else None
end Metal
