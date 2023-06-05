package com.marantesss.raytracingscala

extension (d: Double)
  def *(c: Color): Color = c * d
  def *(v: Vec3): Vec3 = v * d