package com.marantesss.raytracingscala
package material

import utils.{Color, HitResult, Ray, ScatterResult}

import scala.util.Random

case class Dielectric(
    refractionIndex: Double,
) extends Material(Color.white):
  def scatter(ray: Ray, hit: HitResult): Option[ScatterResult] =
    val refractionRatio = if hit.frontFacing then (1d / refractionIndex) else refractionIndex
    val unitDirection   = ray.direction.unit

    val cosTheta   = Math.min((-unitDirection).dot(hit.normal), 1d)
    val sinTheta   = Math.sqrt(1d - cosTheta * cosTheta);
    val canRefract = refractionRatio * sinTheta <= 1.0

    val refracted =
      if canRefract || reflectance(cosTheta, refractionRatio) > Random.nextDouble() then
        unitDirection.refract(hit.normal, refractionRatio)
      else unitDirection.reflect(hit.normal)

    Option(ScatterResult(Ray(hit.point, refracted), Color.white))

  private def reflectance(cos: Double, refIdx: Double): Double =
    val r0 = Math.pow((1 - refIdx) / (1 + refIdx), 2)
    r0 + (1 - r0) * Math.pow((1 - cos), 5);

end Dielectric
