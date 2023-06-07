package com.marantesss.raytracingscala

import utils.Color

import scala.util.Random

case class Renderer(
    viewport: Viewport,
    scene: Scene,
):
  private val samplesPerPixel = 100

  def renderContent(
      width: Int,
      height: Int,
  ): Seq[Seq[Color]] =
    Seq.tabulate[Color](height, width)((h, w) =>
      Seq
        .fill(samplesPerPixel)(Random.nextDouble())
        .map { random =>
          val u = (w.toDouble + random) / (width - 1)
          val v = (height - 1 - h + random) / (height - 1)
          scene.rayColor(viewport.getRay(u, v))
        }
        .reduce(_ + _)
        /
          samplesPerPixel,
    )
end Renderer
