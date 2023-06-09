package com.marantesss.raytracingscala
package utils

import material.Material

case class HitResult(
    /** 3D coords where the ray hit the prop
      */
    point: Vec3,

    /** prop surface perpendicular vector starting in point
      */
    normal: Vec3,

    /** Material hit
      */
    material: Material,

    /** distance from ray origin and point
      */
    t: Double,

    /** was the prop hit from the inside (backFacing) or the outside (frontFacing)
      */
    frontFacing: Boolean = true,
):

  /**   - A hit will always have 2 normals in opposite directions. We want the normal to always
    *     point outwards.
    *   - Moreover, a Hit can happen from the inside or the outside of the Prop.
    *
    * Given these 2 variants, we should set frontFacing and normal accordingly
    *
    * @param ray
    * @return
    */
  def setFaceNormal(ray: Ray): HitResult =
    val frontFacing = ray.direction.dot(normal) < 0
    val newNormal   = if frontFacing then normal else -normal
    copy(normal = newNormal)

end HitResult
