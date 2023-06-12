package com.marantesss.raytracingscala
package utils

import org.scalatest.funspec.AnyFunSpec

class Vec3Test extends AnyFunSpec:

  describe("Methods") {
    it("should calculate length") {
      assertResult(1)(Vec3(1, 0, 0).length)
      assertResult(Math.sqrt(1 * 1 + 2 * 2 + 3 * 3))(Vec3(1, 2, 3).length)
    }

    it("should calculate unit vector with same direction") {
      assertResult(Vec3(1, 0, 0))(Vec3(100, 0, 0).unit)
      assertResult(Vec3(0, 1, 0))(Vec3(0, 100, 0).unit)
      assertResult(Vec3(0, 0, 1))(Vec3(0, 0, 100).unit)

      assertResult(Vec3(0.5773502691896257, 0.5773502691896257, 0.5773502691896257))(
        Vec3(23, 23, 23).unit,
      )
    }
  }

  describe("Operators") {
    it("should sum 2 Vec3") {
      assertResult(Vec3(1, 1, 7))(Vec3(1, 2, 3) + Vec3(0, -1, 4))
    }

    it("should subtract 2 Vec3") {
      assertResult(Vec3(1, 3, -1))(Vec3(1, 2, 3) - Vec3(0, -1, 4))
    }

    it("should multiply a Vec3 with a constant") {
      assertResult(Vec3(3, 6, 9))(Vec3(1, 2, 3) * 3)
    }

    it("should multiply a constant with a Vec 3") {
      assertResult(Vec3(3, 6, 9))(3 * Vec3(1, 2, 3))
    }

    it("should create a vector with opposite direction") {
      assertResult(Vec3(-3, -6, -9))(-Vec3(3, 6, 9))
    }

    it("should dot product 2 vectors") {
      assertResult(12)(Vec3(1, 2, 3).dot(Vec3(4, -5, 6)))
      assertResult(0)(Vec3(6, -1, 3).dot(Vec3(4, 18, -2)))
    }

    it("should reflect one vector with another") {
      assertResult(Vec3(1, 1, 3))(Vec3(1, -1, 3).reflect(Vec3(0, 1, 0)))
      assertResult(Vec3(1, 1, 0))(Vec3(1, -1, 0).reflect(Vec3(0, 1, 0)))
    }

    it("should refract one vector with another") {
      val refracted = Vec3(1, -1, 0).unit
      val result    = Vec3(1, -1, 0).unit.refract(Vec3(0, 1, 0).unit, 1d)
      assertResult(f"${refracted.x}%1.12f")(f"${result.x}%1.12f")
      assertResult(f"${refracted.y}%1.12f")(f"${result.y}%1.12f")
      assertResult(f"${refracted.z}%1.12f")(f"${result.z}%1.12f")
    }
  }

  describe("Constants") {
    it("should create origin vector") {
      assertResult(Vec3(0, 0, 0))(Vec3.zero)
    }

    it("should create unit vector") {
      assertResult(Vec3(1, 1, 1))(Vec3.unit)
    }
  }

end Vec3Test
