import java.io.File

fun main() {
    val input = File("./input/day20.txt").readText()
    println("Result: " + part1(input))
}

private fun part1(input : String): Long {
    val imageInput = input.split("\r\n\r\n")
    val images = imageInput.map { i -> Image(i) }
    for(image in images) {
        for(otherImage in images) {
            println("Checking ${image.id} against ${otherImage.id}")
            if(image.id == otherImage.id) continue
            checkMatches(image, otherImage)
            image.rotate()
            checkMatches(image, otherImage)
            image.flipHorizontal()
            checkMatches(image, otherImage)
            image.rotate()
            checkMatches(image, otherImage)
            image.flipVertical()
            checkMatches(image, otherImage)
            image.rotate()
            println(image.matches)
        }
    }
    val corners = images.filter { i -> i.matches.size < 3 };
    println(corners.map { i -> "${i.id} ${i.matches}" })
    return corners.map {c -> c.id}.reduce { cur, acc -> cur * acc}

}

private fun checkMatches(image: Image, otherImage: Image) {
    checkEdges("l1", image.l1, image, otherImage)
    checkEdges("l2", image.l2, image, otherImage)
    checkEdges("s1", image.s1, image, otherImage)
    checkEdges("s2", image.s2, image, otherImage)

}

fun checkEdges(id: String, edge: String, image: Image, otherImage: Image) {
    when (edge) {
        otherImage.l1 -> image.matches[id] = otherImage.id.toString() + ";l1"
        otherImage.l2 -> image.matches[id] = otherImage.id.toString() + ";l2"
        otherImage.s1 -> image.matches[id] = otherImage.id.toString() + ";s1"
        otherImage.s2 -> image.matches[id] = otherImage.id.toString() + ";s2"
    }
}

class Image(private val input : String) {

    var id = 0L
    var l1 = ""
    var l2 = ""
    var s1 = ""
    var s2 = ""
    var rotated = false
    var matches = mutableMapOf<String,String>()

    init {
        val rows = input.split("\r\n")
        id = rows[0].replace("Tile ", "").replace(":", "").toLong()
        l1 = rows.subList(1, rows.size).fold("") { acc, cur -> acc + cur[0]}
        l2 = rows.subList(1, rows.size).fold("") { acc, cur -> acc + cur.last()}
        s1 = rows[1]
        s2 = rows.last()
    }

    fun rotate() {
        if(rotated) {
            s1 = s1.reversed()
            s2 = s2.reversed()
            rotated = false
        } else {
            l1 = l1.reversed()
            l2 = l2.reversed()
            rotated = true
        }
    }

    fun flipVertical() {
        l1 = l1.reversed()
        l2 = l2.reversed()
    }

    fun flipHorizontal() {
        s1 = s1.reversed()
        s2 = s2.reversed()
    }

    fun edges(): MutableList<String> {
        return mutableListOf(l1, l2, s1, s2)
    }


}