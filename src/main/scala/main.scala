package com.marantesss.raytracingscala

import com.marantesss.raytracingscala.props.HitResult.{Hit, NoHit}
import com.marantesss.raytracingscala.props.Sphere

import java.io.{File, PrintWriter}

@main
def main(): Unit = {
  // Image
  val aspectRatio = 16.0 / 9.0
  val width = 400
  val height = (400 / aspectRatio).toInt

  // Camera
  val viewportHeight = 2.0
  val viewportWidth = viewportHeight * aspectRatio
  val focalLength = 1.0

  val origin = Vec3(0, 0, 0)
  val horizontal = Vec3(viewportWidth, 0, 0)
  val vertical = Vec3(0, viewportHeight, 0)
  val lowerLeftCorner = origin - horizontal / 2 - vertical / 2 - Vec3(0, 0, focalLength)

  // Render
  val content = Seq.tabulate[Color](height, width)((h, w) =>
    val u = w.toDouble / (width - 1)
    val v = (height - 1 - h).toDouble / (height - 1)
    rayColor(
      Ray(
        origin,
        lowerLeftCorner + u * horizontal + v * vertical - origin
      )
    )
  )

  val i = Image(width, height)
    .fillContent(content)
    .write(
      PrintWriter(File("batata.ppm"))
    )
}

def rayColor(ray: Ray): Color = Sphere(Vec3(0, 0, -1), 0.5).hit(ray, 0, 1) match
  case NoHit => {
    val t = 0.5 * (ray.direction.unit.y + 1)
    Color.white.lerpStart(t, Color.skyBlue)
  }
  case Hit(_p, _n, t) => {
    val n = (ray.at(t) - Vec3(0, 0, -1)).unit
    0.5 * Color.fromRatio(n.x + 1, n.y + 1, n.z + 1)
  }



