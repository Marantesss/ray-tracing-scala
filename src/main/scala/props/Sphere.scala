package com.marantesss.raytracingscala
package props

import utils.{HitResult, Ray, Vec3}

import material.Material

/** Sphere equation: x² + y² + z² = r²
  *
  * @param center
  * @param radius
  */
case class Sphere(
    center: Vec3 = Vec3.zero,
    radius: Double = 1,
    material: Material,
) extends Prop(material):

  /** We can simplify: $$ {-b \pm \sqrt{b² - 4ac} \over 2a} $$ when $b = h / 2$ to: $$ {-h \pm
    * \sqrt{h² - ac} \over a} $$
    *
    * @param ray
    *   ray to test the hit
    * @param tMin
    *   consider only hits that have a larger t that tMin
    * @param tMax
    *   consider only hits that have a lower t that tMax
    * @return
    *   HitResult
    */
  def hit(ray: Ray, tMin: Double, tMax: Double): Option[HitResult] =
    val origin = ray.origin - center
    // same as ray.direction.dot(ray.direction)
    val a = ray.direction.lengthSquared
    // we can just use b / 2
    val halfB        = origin.dot(ray.direction)
    val c            = origin.lengthSquared - Math.pow(radius, 2)
    val discriminant = Math.pow(halfB, 2) - a * c

    if discriminant < 0 then return None

    val sqrtDiscriminant = Math.sqrt(discriminant)
    val t = Seq(
      (-halfB - sqrtDiscriminant) / a,
      (-halfB + sqrtDiscriminant) / a,
    ).find(x => !(x < tMin || x > tMax))

    if t.isEmpty then return None

    val point = ray.at(t.get)
    Option(
      HitResult(
        point = point,
        normal = (point - center) / radius,
        material = material,
        t = t.get,
      )
        .setFaceNormal(ray),
    )
