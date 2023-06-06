package com.marantesss.raytracingscala

import utils.{Color, Vec3}

extension (d: Double)
  def *(c: Color): Color = c * d
  def *(v: Vec3): Vec3 = v * d