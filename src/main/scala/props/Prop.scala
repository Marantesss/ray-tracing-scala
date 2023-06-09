package com.marantesss.raytracingscala
package props

import utils.{HitResult, Ray, Vec3}
import material.Material

trait Prop(
    material: Material,
):
  def hit(ray: Ray, tMin: Double, tMax: Double): Option[HitResult]
end Prop
