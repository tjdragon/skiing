package skiing

// http://geeks.redmart.com/2015/01/07/skiing-in-singapore-a-coding-diversion/
// By TJ
object SkiingInSG {
  val datum = "./src/main/scala/mountain1000x1000.txt"
  val linesIterator = scala.io.Source.fromFile(datum).getLines
  val dim = linesIterator.next.split("\\s+")
  val mountainDim = (dim(0).toInt, dim(1).toInt)
  val X = mountainDim._1
  val Y = mountainDim._2
  println(s"Mountain Dim $mountainDim")
  val mountain = Array.ofDim[Int](mountainDim._1, mountainDim._2)

  def populate {
    var i = 0; var j = 0;
    for (line <- linesIterator) {
      for (e <- line.split("\\s+")) {
        mountain(i)(j) = e.toInt
        j = j + 1
      }
      j = 0
      i = i + 1
    }
  }

  val t0 = System.currentTimeMillis()
  populate
  val t1 = System.currentTimeMillis() - t0
  println("           (popu)   " + t1 + " millis")

  type Coord = (Int, Int)
  type Path = List[Coord]

  def elem(e: Coord) = mountain(e._1)(e._2)
  def elems(path: List[Coord]) = path.map(e => elem(e))

  def validNeighbours(coord: Coord): List[Coord] = {
    val from = elem(coord)
    List((coord._1, coord._2 + 1),
         (coord._1, coord._2 - 1),
         (coord._1 + 1, coord._2),
         (coord._1 - 1, coord._2))
         .filter(e => redmartCondition(from, e))
  }

  def redmartCondition(from: Int, e: Coord): Boolean = e._1 >= 0 && e._2 >= 0 && e._1 < X && e._2 < Y && from > elem(e)

  def cioCondition(from: Int, e: Coord): Boolean = ???

  def paths(from: Coord): List[Path] = {
    def pathsRec(current: Coord, currentPath: Path): List[Path] = {
      val vn = validNeighbours(current)
      if (vn.isEmpty) List(currentPath.reverse)
      else vn.filter(e => !(currentPath contains e)).flatMap(c => pathsRec(c, c :: currentPath))
    }
    pathsRec(from, List(from))
  }

  def allPaths() : List[Path] = {
    (for {
      i <- 0 until X
      j <- 0 until Y
    } yield paths((i, j))).flatten.toList
  }

  def bestSkiSlope(): Path = {
    val t0 = System.currentTimeMillis()
    val slopes = allPaths()
    val t1 = System.currentTimeMillis() - t0
    println("           (allp)   " + t1)
    val maxNodes = slopes.maxBy(p => p.size).size
    val best = slopes.filter(p => p.size == maxNodes).maxBy(p => elem(p.head) - elem(p.last))
    best
  }

  def main(args: Array[String]): Unit = {
    val t0 = java.lang.System.currentTimeMillis
    println("Solving skiing in Singapore ...")

    val bestSlope = bestSkiSlope()
    val bestSlopeNbs = elems(bestSlope)
    val drop = bestSlopeNbs.head - bestSlopeNbs.last
    println("Best Slope (coord)  " + bestSlope)
    println("           (elems)  " + bestSlopeNbs)
    println("           (length) " + bestSlope.size)
    println("           (drop)   " + drop)
    val email = s"${bestSlope.size}$drop@redmart.com"
    println("           (email)  " + email)
    val tf = (java.lang.System.currentTimeMillis - t0)
    println("           (time)   " + tf + " millis")
  }
}
