package com.marantesss.raytracingscala

import utils.{Color, Ray, Vec3}

import com.marantesss.raytracingscala.props.{HitResult, Prop, Sphere}
import com.marantesss.raytracingscala.props.HitResult.{Hit, NoHit}

case class Scene(
    props: Seq[Prop],
):
  def propHits(ray: Ray, tMin: Double, tMax: Double): HitResult =
    props
      .map(_.hit(ray, tMin, tMax))                       // calculate hits
      .sortWith(_.distanceToOrigin < _.distanceToOrigin) // order by closest prop to origin/camera
      .find {                                            // fetch the first (closest) hit
        case Hit(_p, _n, _t, _f) => true
        case _                   => false
      }
      .getOrElse(NoHit) // if not found then NoHit
