package com.marantesss.raytracingscala

import com.marantesss.raytracingscala.props.Sphere
import com.marantesss.raytracingscala.utils.{Color, Ray, Vec3}

import java.io.{File, PrintWriter}
import scala.util.Random

@main
def main(): Unit = {
  // Image
  val aspectRatio = 16.0 / 9.0
  val width       = 400
  val height      = (400 / aspectRatio).toInt

  val viewport = Viewport()

  val scene = Scene(
    Seq(
      Sphere(Vec3(0, -100.5, -1), 100),
      Sphere(Vec3(0, 0, -1), 0.5),
    ),
  )

  val content = Renderer(viewport, scene).renderContent(width, height)

  // Render
  Image(width, height)
    .fillContent(content)
    .write(
      PrintWriter(File("batata.ppm")),
    )
}
