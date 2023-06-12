package com.marantesss.raytracingscala

import com.marantesss.raytracingscala.material.{Dielectric, Lambertian, Metal}
import com.marantesss.raytracingscala.props.Sphere
import com.marantesss.raytracingscala.utils.{Color, Ray, Vec3}

import java.io.{File, PrintWriter}
import scala.util.Random

@main
def main(): Unit =
  val t0 = System.nanoTime()
  // Image
  val aspectRatio = 3d / 2d
  val width       = 1200
  val height      = (width / aspectRatio).toInt

  // camera
  val lookFrom      = Vec3(13, 2, 3)
  val lookAt        = Vec3(0, 0, 0)
  val vup           = Vec3(0, 1, 0)
  val aperture      = 0.1
  val focusDistance = 10d

  val viewport = Viewport(
    lookFrom,
    lookAt,
    vup,
    20d,
    aspectRatio,
    aperture,
    focusDistance,
  )

  val scene = Scene.randomScene

  val content = Renderer(viewport, scene).renderContent(width, height)

  // Render
  Image(width, height)
    .fillContent(content)
    .write(
      PrintWriter(File("batata.ppm")),
    )

  val t1 = System.nanoTime()
  println("Elapsed time: " + (t1 - t0) + "ns")
