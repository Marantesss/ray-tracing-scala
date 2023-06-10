package com.marantesss.raytracingscala

import utils.{Color, Ray, Vec3, HitResult}
import props.Prop

case class Scene(
    props: Seq[Prop] = Seq.empty,
):

  def addProp(prop: Prop): Scene = copy(
    props = this.props :+ prop,
  )

  def addProps(props: Seq[Prop]): Scene = copy(
    props = this.props ++ props,
  )

  def propHits(ray: Ray, tMin: Double, tMax: Double): Option[HitResult] =
    props
      .map(_.hit(ray, tMin, tMax)) // calculate hits
      .collect({ case Some(h) => h }) // filter by defined values
      .sortWith(_.t < _.t) // order by closest prop to origin/camera
      .headOption          // get closest option if available
