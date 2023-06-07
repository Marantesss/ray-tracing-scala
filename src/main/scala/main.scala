package com.marantesss.raytracingscala

import com.marantesss.raytracingscala.props.Sphere
import com.marantesss.raytracingscala.utils.{Color, Ray, Vec3}

import java.io.{File, PrintWriter}

@main
def main(): Unit = {
  // Image
  val aspectRatio = 16.0 / 9.0
  val width       = 400
  val height      = (400 / aspectRatio).toInt

  val viewport = Viewport(
    origin = Vec3.zero,
    height = 2.0,
    width = 2.0 * aspectRatio,
    focalLength = 1.0,
  )

  val scene = Scene(
    Seq(
      Sphere(Vec3(0, -100.5, -1), 100),
      Sphere(Vec3(0, 0, -1), 0.5),
    ),
  )

  // Render
  renderImage(viewport, scene, height, width)
    .write(
      PrintWriter(File("batata.ppm")),
    )
}

def renderImage(
    viewport: Viewport,
    scene: Scene,
    imageHeight: Int,
    imageWidth: Int,
): Image =
  Image(imageWidth, imageHeight)
    .fillContent(
      Seq.tabulate[Color](imageHeight, imageWidth)((h, w) =>
        val u = w.toDouble / (imageWidth - 1)
        val v = (imageHeight - 1 - h).toDouble / (imageHeight - 1)
        scene.rayColor(
          Ray(
            viewport.origin,
            viewport.calculatePoint(u, v),
          ),
        ),
      ),
    )
