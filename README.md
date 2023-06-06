# Ray Tracing Scala

This repository hosts my take on  [_Ray Tracing in One Weekend Book Series_](https://raytracing.github.io/) using
[Scala](https://www.scala-lang.org/) and functional programming.

The goal behind this project is really just:
1. Further develop my Scala and Functional Programming skills
2. Try out testing in Scala testing using [ScalaTest](https://www.scalatest.org/)
3. Have some fun with basic Computer Graphics

This document is divided in 3 sections (one for each book of the series). In each section you'll find the generated
image after each new feature implementation as well as a brief explanation of my implementation.

## Book 1: [_Ray Tracing in One Weekend_](https://raytracing.github.io/books/RayTracingInOneWeekend.html)

### 1.2. Outputing an image

Everytime you need to create a file and write some content to it, you always end up Googling how to do it.
In an effort to save you some time, take a look at [How to write to a File in Scala](https://www.educba.com/scala-write-to-file/)
and the [PPM file format specification](https://netpbm.sourceforge.net/doc/ppm.html)

#### The `.ppm` file format

In a nutshell the PPM file specification consists of a grid of colors.
The colors can be represented in RGB or any other format, but let's keep in simple with RGB (0-255)
Take a look at the following example from [Wikipedia](https://en.wikipedia.org/wiki/Netpbm):

```ppm
P3           
# "P3" means this is a RGB color image in ASCII 
# "3 2" is the width and height of the image in pixels
# "255" is the maximum value for each color
# This, up through the "255" line below are the header.
# Everything after that is the image data: RGB triplets.
# In order: red, green, blue, yellow, white, and black.
3 2         
255       
255   0   0   0 255   0   0   0 255
255 255   0 255 255 255   0   0   0
```

which will show up as:

![](media/ppm-output-example.png)

#### Defining the `Color` and `Image` case class

We'll be using Scala's [`case class`](), [`object`]() and [`enum`](), just because they seem to be better suited for [FP data modeling](https://docs.scala-lang.org/scala3/book/domain-modeling-fp.html)

Defining a `Color` is pretty straightforward, and you can use a [companion object](https://docs.scala-lang.org/scala3/book/domain-modeling-fp.html#companion-object)
to declare static like functions and constants.

```scala
case class Color (red: Int, green: Int, blue: Int)

object Color:
  val BYTE_SIZE = 255
  def white: Color = Color(255, 255, 255)
  def black: Color = Color(0, 0, 0)
end Color
```

we can then override the `toString` method to make the code easier to read:

```scala
override def toString: String = s"$red $green $blue"
```

We'll need a data structure to model an image, something like this should do the trick:

```scala
case class Image(width: Int, height: Int, content: Seq[Seq[Color]] = Seq.empty)
```

```scala
def rowToString(row: Seq[Color]): String = row.foldLeft("")((accRow, byte) => s"$accRow$byte ").trim

def write(writer: PrintWriter): Unit =
// header
writer.write(s"P3\n$width $height\n${Color.BYTE_SIZE}\n")
// content
content.foreach(writer.write(s"${rowToString(_)}\n")
```

##### The rainbow effect

```scala
def fillRainbow(): Image = copy(
content = Seq.tabulate[Color](this.height, this.width)((h: Int, w: Int) =>
  val r = w.toDouble / (this.width - 1)
  val g = (this.height - 1 - h).toDouble / (this.width - 1)
  val b = 0.25

  Color.fromRatio(r, g, b)
)
)
```

### 1.3. The `Vec3` class

This part is very similar to the original approach in C++, just take a look at the `Vec3.scala` file, and you'll
quickly grasp this data structure.

However, unlike the original approach **`Vec3` is not used for colors, just 3-dimensional vectors and coordinates**.

### 1.4. Rays, camera and Background

Declaring a ray function ($P(t) = A + tb$), where:

- $A$ is the ray's origin
- $b$ is the ray's direction

is as easy as:

```scala
case class Ray(origin: Vec3, direction: Vec3):
  def at(t: Double): Vec3 = origin + t * direction
```

TBD

![](media/book-1/2.1-rainbow.ppm)

![](media/book-1/4.1-sky.ppm)

![](media/book-1/5.1-simple-red-sphere.ppm)

![](media/book-1/6.1-surface-normals.ppm)

![](media/book-1/6.2-surface-normals-ground.ppm)

## Book 2: [_Ray Tracing The Next Week_](https://raytracing.github.io/books/RayTracingTheNextWeek.html)

WIP

## Book 3: [_Ray Tracing The Rest of Your Life_](https://raytracing.github.io/books/RayTracingTheNextWeek.html)

WIP
