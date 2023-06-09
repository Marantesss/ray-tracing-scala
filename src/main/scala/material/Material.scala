package com.marantesss.raytracingscala
package material

import utils.{Color, HitResult, Ray, ScatterResult}

trait Material(color: Color):
  def scatter(ray: Ray, hit: HitResult): Option[ScatterResult]
end Material
