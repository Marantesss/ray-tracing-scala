package com.marantesss.raytracingscala

import utils.{Color, Ray, Vec3}

import com.marantesss.raytracingscala.props.{HitResult, Prop, Sphere}
import com.marantesss.raytracingscala.props.HitResult.{Hit, NoHit}

case class Scene(
  props: Seq[Prop]
):
  /**
   * TODO fetch closest prop to camera instead of calculating hits for every prop
   * refactor this into a more functional style syntax
   */
  def propHits(ray: Ray, tMin: Double, tMax: Double): HitResult =
    var hitResult = NoHit
    var closestSoFar = tMax
    props.foreach(p =>
      p.hit(ray, tMin, closestSoFar) match
        case NoHit => None
        case Hit(p, n, t, f) =>
          println(s"normal: $n")
          closestSoFar = t
          hitResult = Hit(p, n, t, f)
    )
    hitResult

  def rayColor(ray: Ray): Color = this.propHits(ray, 0, Double.MaxValue) match
    case NoHit => Color.white.lerpStart(0.5 * (ray.direction.unit.y + 1.0), Color.skyBlue)
    case Hit(_p, n, t, _f) => 0.5 * (Color.fromRatio(1, 1, 1) + Color.fromRatio(n.x, n.y, n.z))
