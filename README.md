# Ray Tracing Scala

This repository hosts my take on  [_Ray Tracing in One Weekend Book Series_](https://raytracing.github.io/) using
[Scala](https://www.scala-lang.org/) and functional programming.

The goal behind this project is really just:
1. Further develop my Scala and Functional Programming skills
2. Try out Scala tools, such as:
   1. [ScalaTest](https://www.scalatest.org/) for testing
   2. [Scalafmt](https://scalameta.org/scalafmt/) for linting and formatting
3. Have some fun with basic Computer Graphics

This document is divided in 3 sections (one for each book of the series). In each section you'll find the generated
image after each new feature implementation as well as a brief explanation of my implementation.

> ⚠️ Code snippets on this README are simplified for readibility reasons ⚠️

## Book 1: [_Ray Tracing in One Weekend_](https://raytracing.github.io/books/RayTracingInOneWeekend.html)

### 1. Outputing an image

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

We'll be using Scala's [`case class`](https://docs.scala-lang.org/scala3/book/domain-modeling-tools.html#case-classes), [`object`](https://docs.scala-lang.org/scala3/book/domain-modeling-tools.html#objects) and [`enum`](https://docs.scala-lang.org/scala3/book/domain-modeling-tools.html#Enums_Scala_3_only), just because they seem to be better suited for [FP data modeling](https://docs.scala-lang.org/scala3/book/domain-modeling-fp.html)

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

We can then declare some methods to write the `Image`'s content to a file:

```scala
def rowToString(row: Seq[Color]): String = row
  .foldLeft("")((accRow, byte) => s"$accRow$byte ")
  .trim

def write(writer: PrintWriter): Unit =
  // header
  writer.write(s"P3\n$width $height\n${Color.BYTE_SIZE}\n")
  // content
  content.foreach(writer.write(s"${rowToString(_)}\n")
```

##### The rainbow effect

Just for fun, let's create a rainbow effect given the Image's height and width.
This is an excellent case to use [`Seq.tabulate`](https://www.scala-lang.org/api/3.3.0/scala/collection/immutable/Seq$.html#tabulate-e34).

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

![](media/book-1/2.1-rainbow.png)

### 2. The `Vec3` class

This part is very similar to the original approach in C++, just take a look at the `Vec3.scala` file, and you'll
quickly grasp this data structure.

However, unlike the original approach **`Vec3` is not used for colors, just 3-dimensional vectors and coordinates**.

### 3. Rays, camera and Background

Declaring a ray function ($P(t) = A + tb$), where:

- $A$ is the ray's origin
- $b$ is the ray's direction

is as easy as:

```scala
case class Ray(origin: Vec3, direction: Vec3):
  def at(t: Double): Vec3 = origin + t * direction
```

Now we just need to calculate the linear interpolation between white and blue given the ray's direction y-axis value
(which will be a value between -1 and 1). Take a look at `Color.lerpStart` to better understand the implementation details,
but in a nutshell it's just an implementation of the following formula:

$$ color = (1 − t) * startColor + t * endColor $$

where:
- $t$ is a value between 0 and 1

```scala
def rayColor(ray: Ray): Color =
  Color.white.lerpStart(0.5 * (ray.direction.unit.y + 1.0), Color.skyBlue)
```

![](media/book-1/4.1-sky.png)

### 4. Adding a Sphere

Let's create a `Sphere` case class to encapsulate its logic in one place. If you're not like me and remember your geometry
classes, you know that a sphere's equation is something like:

$$
(x - c_x)^2 + (z - c_z)^2 + (z - c_z)^2 = r^2
$$

where:
- $r$ = sphere radius
- $(c_x, c_y, c_z)$ = is the 3-dimensional coordinate in the center of the Sphere

This means that if:
- $(x - c_x)^2 + (z - c_z)^2 + (z - c_z)^2 > r^2$ then $(x, y, z)$ is **outside** the sphere;
- $(x - c_x)^2 + (z - c_z)^2 + (z - c_z)^2 < r^2$ then $(x, y, z)$ is **inside** the sphere;

With this in mind, I recommend you go over the book's explanation on [Ray-Sphere Intersection](https://raytracing.github.io/books/RayTracingInOneWeekend.html#addingasphere/ray-sphereintersection)
and to better understand the math behind the actual implementation.

```scala
case class Sphere(center: Vec3 = Vec3.zero, radius: Double = 1):
   def hit(ray: Ray): Boolean =
      val origin       = ray.origin - center
      val a            = ray.direction.dot(ray.direction)
      val b            = 2.0 * origin.dot(ray.direction)
      val c            = origin.dot(origin) - Math.pow(radius, 2)
      val discriminant = Math.pow(b, 2) - 4 * a * c
      
      !(discriminant < 0)
```

Now we need to update the `rayColor` function to render a red colored pixel when a Sphere is hit. Let's test this out
by hardcoding a Sphere in the middle of the Scene:

```scala
def rayColor(ray: Ray): Color =
  if (Sphere(Vec3(0, 0, -1), 0.5).hit(ray)) then
    Color.red
  else
    Color.white.lerpStart(0.5 * (ray.direction.unit.y + 1.0), Color.skyBlue)
```

And we'll get an Image which looks like this:

![](media/book-1/5.1-simple-red-sphere.png)

### 5. Surface Normals and Multiple Objects

Take a look at [Shading with Surface Normals](https://raytracing.github.io/books/RayTracingInOneWeekend.html#surfacenormalsandmultipleobjects/shadingwithsurfacenormals)
section for a theoretical background on how shading is done.

What we have to understand is that not only do we want to know if a ray hit the Sphere, but we also need to grab:
1. $t$: how far away from the ray's origin was the hit
2. $p$: the hit coordinates
3. $n$: the surface normal
4. frontFacing: was the Sphere hit from the inside or the outside

Let's create a data structure to model our hit results. As I see it, we have 2 options:
1. use an [`enum`](https://docs.scala-lang.org/scala3/book/domain-modeling-tools.html#Enums_Scala_3_only) which has
2 entries `Hit` and `NoHit`, where the first one will contain the values we need.
2. Or use a simple case class, which alongside the [`Option`](https://dotty.epfl.ch/api/scala/Option.html) class can have
`Some` or `None` value for hit or no hit respectively.

In the end, I've decided to implement option 2.

```scala
case class HitResult(
  point: Vec3,
  normal: Vec3,
  t: Double,
  frontFacing: Boolean = true,
)
```

we can now refactor our `Sphere.hit` method to this include this data structure and well as [simplify it](https://raytracing.github.io/books/RayTracingInOneWeekend.html#surfacenormalsandmultipleobjects/simplifyingtheray-sphereintersectioncode):

```scala
  def hit(ray: Ray, tMin: Double, tMax: Double): Option[HitResult] =
    val origin = ray.origin - center
    // same as ray.direction.dot(ray.direction)
    val a = ray.direction.lengthSquared
    // we can just use b / 2
    val halfB        = origin.dot(ray.direction)
    val c            = origin.lengthSquared - Math.pow(radius, 2)
    val discriminant = Math.pow(halfB, 2) - a * c

    if discriminant < 0 then return None

    val sqrtDiscriminant = Math.sqrt(discriminant)
    val t = Seq(
      (-halfB - sqrtDiscriminant) / a,
      (-halfB + sqrtDiscriminant) / a,
    ).find(x => !(x < tMin || x > tMax))

    if t.isEmpty then return None

    val point = ray.at(t.get)
    Option(
       HitResult(
        point = point,
        normal = (point - center) / radius,
       t = t.get,
      )
        .setFaceNormal(ray)
   )
```

and update our `rayColor` function to:

```scala
def rayColor(ray: Ray): Color = Sphere(Vec3(0, 0, -1), 0.5).hit(ray, 0, Double.MaxValue) match
  case None => Color.white.lerpStart(0.5 * (ray.direction.unit.y + 1.0), Color.skyBlue)
  case Some(hit) => 0.5 * (Color.fromRatio(1, 1, 1) + Color.fromRatio(hit.normal.x, hit.normal.y, hit.normal.z))
```

![](media/book-1/6.1-surface-normals.png)

#### Some Refactoring

Now we can create a `Scene` case class which will take care of storing our Props (such as our Spheres) as well as
casting and tracing rays.

```scala
case class Scene(props: Seq[Prop]):
  def propHits(ray: Ray, tMin: Double, tMax: Double): Option[HitResult] = 
    props
      .map(_.hit(ray, tMin, tMax)) // calculate hits
      .collect({ case Some(h) => h }) // filter by defined values
      .sortWith(_.t < _.t) // order by closest prop to origin/camera
      .headOption          // get closest option if available

  def rayColor(ray: Ray): Color = this.propHits(ray, 0, Double.MaxValue) match
    case None => Color.white.lerpStart(0.5 * (ray.direction.unit.y + 1.0), Color.skyBlue)
    case Some(hit) => 0.5 * (Color.fromRatio(1, 1, 1) + Color.fromRatio(hit.normal.x, hit.normal.y, hit.normal.z))
```

- `propHits`: this method takes a ray and the min and max distance the origin to render. It hits all props, orders by
the closest prop hit and then fetches the first `Hit`. If such `HitResult` does not exist, it returns `NoHit`;
- `rayColor`: calls `propHits` for a single ray and returns the correct pixel color;

Now, you can create a scene with multiple Spheres, and the Image will be rendered correctly, i.e. with the closest Sphere
visible.

![](media/book-1/6.2-surface-normals-ground.png)

### 6. Antialiasing

Take a look at the [book's section on Antialiasing](https://raytracing.github.io/books/RayTracingInOneWeekend.html#antialiasing)
to better understand how it works, but essentially we need to calculate the color of random viewport ray hits inside the
pixel we're working on, and then merge these values together to get a blending color effect.

#### Creating a Renderer Case Class

Let's move the render logic from our `main.scala` file into a dedicated file. Instead of outputting an Image, we just
want it to output a Color matrix of a specified `width` and `height`. Breaking down the render logic for each color pixel:
1. Create a sequence of `SAMPLES_PER_PIXEL` random values between 0 and 1
2. Map those random values into the calculated with `u` and `v`
3. Reduce that sequence into a unique Color which contains the sum of all RGB color values
4. Divide that color by `SAMPLES_PER_PIXEL` which calculates the "average" color in the pixel

While we're at it, let's place the `rayColor` method here as well:

```scala
case class Renderer(viewport: Viewport, scene: Scene):
  private val SAMPLES_PER_PIXEL = 100

  def renderContent(width: Int, height: Int): Seq[Seq[Color]] =
    Seq.tabulate[Color](height, width)((h, w) =>
      Seq
        .fill(SAMPLES_PER_PIXEL)(Random.nextDouble())       // step 1
        .map { random =>                                    // step 2
          val u = (w.toDouble + random) / (width - 1)
          val v = (height - 1 - h + random) / (height - 1)
          rayColor(viewport.getRay(u, v))
        }
        .reduce(_ + _)                                      // step 3
        /                                                   // step 4
          SAMPLES_PER_PIXEL,
    )

  def rayColor(ray: Ray): Color = scene.propHits(ray, 0, Double.MaxValue) match
    case None => Color.white.lerpStart(0.5 * (ray.direction.unit.y + 1.0), Color.skyBlue)
    case Some(hit) => 0.5 * (Color.fromRatio(1, 1, 1) + Color.fromRatio(hit.normal.x, hit.normal.y, hit.normal.z))
```

We'll then update our main function to:

```scala
@main
def main(): Unit = {
  val aspectRatio = 16.0 / 9.0
  val width       = 400
  val height      = (400 / aspectRatio).toInt

  val viewport = Viewport()

  val scene = Scene(
    Seq(
      Sphere(Vec3(0, -100.5, -1), 100),
      Sphere(Vec3(0, 0, -1), 0.5),
    ),
  )

  val content = Renderer(viewport, scene).renderContent(width, height)
  Image(width, height)
    .fillContent(content)
    .write(
      PrintWriter(File("output.ppm")),
    )
}
```

![](media/book-1/7-antialiasing.png)

### 7. Diffuse Materials

Picking random points in a unit sphere:

```scala
object Vec3:
  // ...
  def random: Vec3 = Vec3(Random.nextDouble(), Random.nextDouble(), Random.nextDouble())
  def random(min: Double, max: Double): Vec3 = Vec3(
    Utility.randomDoubleBetween(min, max),
    Utility.randomDoubleBetween(min, max),
    Utility.randomDoubleBetween(min, max),
  )

  @tailrec
  def randomInUnitSphere: Vec3 =
    val hypothesis = Vec3.random(-1, 1)
    if hypothesis.lengthSquared < 1 then hypothesis else Vec3.randomInUnitSphere
end Vec3
```

And updating the `rayColor` method:

```scala
case class Renderer(/* ... */):

  private val MAX_RAY_BOUNCE = 50

  def rayColor(ray: Ray, depth: Int = MAX_RAY_BOUNCE): Color =
    if (depth <= 0) then return Color.black

    scene.propHits(ray, 0, Double.MaxValue) match
      case None => Color.white.lerpStart(0.5 * (ray.direction.unit.y + 1.0), Color.skyBlue)
      case Some(hit) =>
        val target = hit.point + hit.normal + Vec3.randomInUnitSphere
        0.5 * rayColor(Ray(hit.point, target - hit.point), depth - 1)
```

Which will render:

![](media/book-1/8.1-diffuse.png)

As we can see (or not), the scene is a little dark. As the [book explains](https://raytracing.github.io/books/RayTracingInOneWeekend.html#diffusematerials/usinggammacorrectionforaccuratecolorintensity), we need to do some gamma correction:

```scala
case class Renderer(/* ... */):

  private val GAMMA = 2d

  def renderContent(width: Int, height: Int): Seq[Seq[Color]] =
    val totalPixels = width * height
    Seq.tabulate[Color](height, width)((h, w) =>
      val progress = (h * width + (w + 1)) / totalPixels.toDouble * 100d
      Seq
        .fill(SAMPLES_PER_PIXEL)(Random.nextDouble())
        .map { random =>
          val u = (w.toDouble + random) / (width - 1)
          val v = (height - 1 - h + random) / (height - 1)
          rayColor(viewport.getRay(u, v))
        }
        .reduce(_ + _)
        .pow(1 / GAMMA), // <--- add this here
    )
```

and also add the `pow` method to our `Color` case class:

```scala
case class Color(/* ... */):
  // ...
  def pow(operand: Double): Color = Color(
    Math.pow(red, operand).toInt,
    Math.pow(green, operand).toInt,
    Math.pow(blue, operand).toInt,
  )
```

![](media/book-1/8.2-gamma-correction.png)

Fixing shadow acne is as easy as:

```scala
case class Renderer(/* ... */):
  /** To fix shadow acne this value cannot be 0
    */
  private val T_MIN = 0.001

  // ...
  def rayColor(ray: Ray, depth: Int = MAX_RAY_BOUNCE): Color =
    // ...
    scene.propHits(ray, T_MIN, Double.MaxValue) match // <--- replace 0 with T_MIN
      // ...
```

![](media/book-1/8.3-shadown-acne-fix.png)

To have a true Lambertian distribution and an Alternative diffuse formaluation, we simply need to add options 2 and 3 to
the `Vec3` object. And of course, update the `rayColor` method and use any of the 3 options to calculate the `target`.

```scala
object Vec3:
  // ...
  /** Option 1: Good enough Lambertian Reflection
   */
  @tailrec
  def randomInUnitSphere: Vec3 =
    val hypothesis = Vec3.random(-1, 1)
    if hypothesis.lengthSquared < 1 then hypothesis else Vec3.randomInUnitSphere
  
  /** Option 2: True Lambertian Reflection
   */
  def randomInUnitSphereUnit: Vec3 = randomInUnitSphere.unit
  
  /** Option 3: An alternative diffuse formulation
   */
  def randomInUnitSphereHemisphere(normal: Vec3): Vec3 =
    val n = Vec3.randomInUnitSphere
    if n.dot(normal) > 0d then n else -n
end Vec3
```

![](media/book-1/8.4-lambertian-spheres.png)

![](media/book-1/8.5-alternative-diffuse-formulation.png)

### 8. Metal

Let's create a [`trait`](https://docs.scala-lang.org/tour/traits.html) for `Material`, which will be extended by a `Lambertian` and `Metal` case class.
1. `Lambertian` is our current implementation
2. `Metal` will require a new implementation for reflective materials

```scala
trait Material(color: Color):
  def scatter(ray: Ray, hit: HitResult): Option[ScatterResult]
end Material
```

We'll also need a `ScatterResult` data structure which will contain the scattered ray and its attenuation.

```scala
case class ScatterResult(scattered: Ray, attenuation: Color)
```

Now, we need to add a `Material` type attribute to the `HitResult` case class, as well as out `Prop` trait.

So, our current implementation of `Lambertian` material will look something like:

```scala
case class Lambertian(albedo: Color) extends Material(albedo):
  def scatter(ray: Ray, hit: HitResult): Option[ScatterResult] =
    val scatterDirection      = hit.normal + Vec3.randomInUnitSphereUnit
    val finalScatterDirection = if scatterDirection.isNearZero then hit.normal else scatterDirection
    Option(ScatterResult(Ray(hit.point, finalScatterDirection), albedo))
end Lambertian
```

And our `Metal` material with fuzziness will be something like:

```scala
case class Metal(albedo: Color, fuzz: Double = 1) extends Material(albedo):
  private val fuzziness = Utility.clamp(fuzz, 0d, 1d)
  def scatter(ray: Ray, hit: HitResult): Option[ScatterResult] =
    val scatterReflection = ray.direction.reflect(hit.normal).unit
    val scattered         = Ray(hit.point, scatterReflection + fuzziness * Vec3.randomInUnitSphere)
    if scattered.direction.dot(hit.normal) > 0 then Option(ScatterResult(scattered, albedo))
    else None
end Metal
```

![](media/book-1/9.1-metal-spheres.png)

![](media/book-1/9.2-metal-spheres-fuzziness.png)

### 9. Dielectrics

Creating the `refract` function:

```scala
def refract(normal: Vec3, refractionRatio: Double): Vec3 =
  val cosTheta      = Math.min((-this).dot(normal), 1d)
  val perpendicular = refractionRatio * (this + cosTheta * normal)
  val parallel      = -Math.sqrt(Math.abs(1d - perpendicular.lengthSquared)) * normal
  perpendicular + parallel
```

and the Dialetric material:

```scala
case class Dielectric(refractionIndex: Double) extends Material(Color.white):
  def scatter(ray: Ray, hit: HitResult): Option[ScatterResult] =
    val refractionRatio = if hit.frontFacing then (1d / refractionIndex) else refractionIndex
    val unitDirection   = ray.direction.unit

    val refracted = unitDirection.refract(hit.normal, refractionRatio)

    Option(ScatterResult(Ray(hit.point, refracted), Color.white))
end Dielectric
```

and then updating the scene with this new material:

![](media/book-1/10.1-glass-sphere-that-always-refracts.png)

Determining if the Ray can refract:

```scala
case class Dielectric(refractionIndex: Double) extends Material(Color.white):
  def scatter(ray: Ray, hit: HitResult): Option[ScatterResult] =
    val refractionRatio = if hit.frontFacing then (1d / refractionIndex) else refractionIndex
    val unitDirection   = ray.direction.unit

    val cosTheta   = Math.min((-unitDirection).dot(hit.normal), 1d)
    val sinTheta   = Math.sqrt(1d - cosTheta * cosTheta);
    val canRefract = refractionRatio * sinTheta <= 1.0

    val refracted =
      if canRefract then unitDirection.refract(hit.normal, refractionRatio)
      else unitDirection.reflect(hit.normal)

    Option(ScatterResult(Ray(hit.point, refracted), Color.white))
end Dielectric
```

![](media/book-1/10.2-glass-sphere-that-sometimes-refracts.png)

Then adding [Schlick's approximation](https://en.wikipedia.org/wiki/Schlick%27s_approximation):

```scala
  private def reflectance(cos: Double, refIdx: Double): Double =
    val r0 = Math.pow((1 - refIdx) / (1 + refIdx), 2)
    r0 + (1 - r0) * Math.pow((1 - cos), 5);
```

Finally [modeling a hallow glass sphere](https://raytracing.github.io/books/RayTracingInOneWeekend.html#dielectrics/modelingahollowglasssphere):

![](media/book-1/10.3-hollow-glass-sphere.png)

### 10. Positionable Camera

We simply need to add some new properties and calculations to the `Viewport` case class:

```scala
case class Viewport(
    lookFrom: Vec3,
    lookAt: Vec3,
    vup: Vec3,
    /** Vertical field-of-view in degrees
      */
    verticalFOV: Double = 90d,
    aspectRatio: Double = (16d / 9d),
):
  private val height: Double = Math.tan(verticalFOV.toRadians / 2d) * 2d
  private val width: Double  = height * aspectRatio

  private val w = (lookFrom - lookAt).unit
  private val u = vup.cross(w).unit
  private val v = w.cross(u)

  private val focalLength: Double = 1d

  val origin: Vec3     = lookFrom
  val horizontal: Vec3 = width * u
  val vertical: Vec3   = height * v
  val lowerLeftCorner: Vec3 =
    origin - horizontal / 2 - vertical / 2 - w

  def getRay(s: Double, t: Double): Ray =
    Ray(origin, lowerLeftCorner + s * horizontal + t * vertical - origin)
```

![](media/book-1/11.1-wide-angle-view.png)

![](media/book-1/11.2-distant-view.png)

![](media/book-1/11.3-zooming-in.png)

### 11. Defocus Blur

## Book 2: [_Ray Tracing The Next Week_](https://raytracing.github.io/books/RayTracingTheNextWeek.html)

WIP

## Book 3: [_Ray Tracing The Rest of Your Life_](https://raytracing.github.io/books/RayTracingTheNextWeek.html)

WIP
