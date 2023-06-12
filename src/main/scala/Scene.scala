package com.marantesss.raytracingscala

import utils.{Color, HitResult, Ray, Utility, Vec3}
import props.{Prop, Sphere}

import com.marantesss.raytracingscala.material.{Dielectric, Lambertian, Metal}

import scala.util.Random

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

end Scene

object Scene:
  def randomScene: Scene =
    val groundMaterial = Lambertian(Color.fromRatio(0.5, 0.5, 0.5))
    val ground         = Sphere(Vec3(0, -1_000, 0), 1_000, groundMaterial)

    val smallSpheres = Seq
      .tabulate[Sphere](20, 20)((a, b) =>
        val x            = a - 11
        val y            = b - 11
        val randomDouble = Random.nextDouble()
        val center       = Vec3(x + 0.9 * Random.nextDouble(), 0.2, y + 0.9 * Random.nextDouble())

        if randomDouble < 0.8 then
          // diffuse
          Sphere(center, 0.2, Lambertian(Color.random))
        else if randomDouble < 0.95 then
          // Metal
          Sphere(center, 0.2, Metal(Color.random(0.5, 1d), Utility.randomDoubleBetween(0, 0.5)))
        else
          // Glass
          Sphere(center, 0.2, Dielectric(1.5)),
      )
      .flatten
      .filter(s => (s.center - Vec3(4, 0.2, 2)).length > 0.9)

    val leftSphere   = Sphere(Vec3(-4, 1, 0), 1d, Lambertian(Color.fromRatio(0.4, 0.2, 0.1)))
    val middleSphere = Sphere(Vec3(0, 1, 0), 1d, Dielectric(1.5))
    val rightSphere  = Sphere(Vec3(4, 1, 0), 1d, Metal(Color.fromRatio(0.7, 0.6, 0.5), 0.0))

    Scene()
      .addProps(smallSpheres)
      .addProp(ground)
      .addProp(leftSphere)
      .addProp(middleSphere)
      .addProp(rightSphere)

end Scene
