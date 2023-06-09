package com.marantesss.raytracingscala
package material

import utils.{Color, HitResult, Ray, ScatterResult}

case class Metal(
    albedo: Color,
) extends Material(albedo):
  /** Only consider reflections on the same plane as hit normal
    */
  def scatter(ray: Ray, hit: HitResult): Option[ScatterResult] =
    val scattered = Ray(hit.point, ray.direction.reflect(hit.normal).unit)
    if scattered.direction.dot(hit.normal) > 0 then Option(ScatterResult(scattered, albedo))
    else None
end Metal
