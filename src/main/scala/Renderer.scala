package com.marantesss.raytracingscala

import utils.{Color, Ray, Vec3}

import com.marantesss.raytracingscala.props.HitResult.{Hit, NoHit}

import scala.util.Random

case class Renderer(
    viewport: Viewport,
    scene: Scene,
):
  /** To fix shadow acne this value cannot be 0
    */
  private val T_MIN = 0.001

  /** Color samples per pixel render for antialiasing
    */
  private val SAMPLES_PER_PIXEL = 100

  /** Max recursive calls of rayColor
    */
  private val MAX_RAY_BOUNCE = 50

  /** Cenas
    */
  private val GAMMA = 2d

  def renderContent(
      width: Int,
      height: Int,
  ): Seq[Seq[Color]] =
    println("Rendering Content...")
    val totalPixels = width * height
    Seq.tabulate[Color](height, width)((h, w) =>
      val progress = (h * width + (w + 1)) / totalPixels.toDouble * 100d
      println(s"\t${f"$progress%1.3f"}% complete. Rendering pixel ($h, $w)")
      Seq
        .fill(SAMPLES_PER_PIXEL)(Random.nextDouble())
        .map { random =>
          val u = (w.toDouble + random) / (width - 1)
          val v = (height - 1 - h + random) / (height - 1)
          rayColor(viewport.getRay(u, v))
        }
        .reduce(_ + _)
        .pow(1 / GAMMA),
    )

  def rayColor(ray: Ray, depth: Int = MAX_RAY_BOUNCE): Color =
    // println(s"\t\trendering ray hit: ${MAX_RAY_BOUNCE - depth + 1}")
    // If we've exceeded the ray bounce limit, no more light is gathered.
    if (depth <= 0) then return Color.fromRatio(0, 0, 0);

    scene.propHits(ray, T_MIN, Double.MaxValue) match
      case NoHit =>
        Color.white.lerpStart(0.5 * (ray.direction.unit.y + 1.0), Color.skyBlue)
      case Hit(p, n, t, _f) =>
        val bounceDirection = (p + n + Vec3.randomInUnitSphere) - p
        0.5 * rayColor(Ray(p, bounceDirection), depth - 1)

end Renderer
