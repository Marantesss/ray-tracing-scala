package com.marantesss.raytracingscala

import com.marantesss.raytracingscala.utils.Color

import java.io.PrintWriter

case class Image(
  width: Int,
  height: Int,
  content: Seq[Seq[Color]] = Seq.empty,
):

  def fillEmpty(): Image = copy(
    content = Seq.fill[Color](this.height, this.width)(Color.black)
  )

  def fillRainbow(): Image = copy(
    content = Seq.tabulate[Color](this.height, this.width)((h: Int, w: Int) =>
      val r = w.toDouble / (this.width - 1)
      val g = (this.height - 1 - h).toDouble / (this.width - 1)
      val b = 0.25

      Color.fromRatio(r, g, b)
    )
  )

  def fillContent(content: Seq[Seq[Color]]): Image = copy(content = content)

  def progress(percentage: Float): Unit =
    println(s"${(percentage * 100).toInt}%")
    Console.flush()

  /**
   * Takes too long and consumes too much memory
   */
  def contentToString: String = content.foldLeft("")((acc, row) => row.foldLeft(acc)((accRow, byte) => s"$accRow$byte ").trim)

  def rowToString(row: Seq[Color]): String = row.foldLeft("")((accRow, byte) => s"$accRow$byte ").trim

  def write(writer: PrintWriter): Unit =
    // header
    writer.write(s"P3\n$width $height\n${Color.BYTE_SIZE}\n")
    // content
    content.zipWithIndex.foreach { row =>
      writer.write(s"${rowToString(row(0))}\n")
      progress(row(1).toFloat / height)
    }
