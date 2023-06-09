package com.marantesss.raytracingscala
package material

import utils.{Color, HitResult, Ray, ScatterResult, Vec3}

case class Lambertian(
    albedo: Color,
) extends Material(albedo):

  def scatter(ray: Ray, hit: HitResult): Option[ScatterResult] =
    val scatterDirection      = hit.normal + Vec3.randomInUnitSphereUnit
    val finalScatterDirection = if scatterDirection.isNearZero then hit.normal else scatterDirection
    Option(ScatterResult(Ray(hit.point, finalScatterDirection), albedo))

end Lambertian
