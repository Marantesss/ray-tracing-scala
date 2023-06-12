package com.marantesss.raytracingscala

import com.marantesss.raytracingscala.material.{Dielectric, Lambertian, Metal}
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

  // World
  val materialGround = Lambertian(Color.fromRatio(0.8, 0.8, 0.0))
  val materialCenter = Lambertian(Color.fromRatio(0.1, 0.2, 0.5))
  val materialLeft   = Dielectric(1.5)
  val materialRight  = Metal(Color.fromRatio(0.8, 0.6, 0.2), 0d)

  val sphereGround = Sphere(Vec3(0.0, -100.5, -1.0), 100, materialGround)
  val sphereCenter = Sphere(Vec3(0.0, 0.0, -1.0), 0.5, materialCenter)
  val sphereLeft   = Sphere(Vec3(-1.0, 0.0, -1.0), 0.5, materialLeft)
  val sphereLeft1   = Sphere(Vec3(-1.0, 0.0, -1.0), -0.4, materialLeft)
  val sphereRight  = Sphere(Vec3(1.0, 0.0, -1.0), 0.5, materialRight)

  val scene = Scene()
    .addProp(sphereGround)
    .addProp(sphereCenter)
    .addProp(sphereLeft)
    .addProp(sphereLeft1)
    .addProp(sphereRight)

  val content = Renderer(viewport, scene).renderContent(width, height)

  // Render
  Image(width, height)
    .fillContent(content)
    .write(
      PrintWriter(File("batata.ppm")),
    )
}
