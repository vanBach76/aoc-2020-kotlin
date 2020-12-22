import kotlin.math.abs

fun main() {

    part1()
}

fun part1() {
    var input = "#####..#\n" +
            "#..###.#\n" +
            "###.....\n" +
            ".#.#.#..\n" +
            "##.#..#.\n" +
            "######..\n" +
            ".##..###\n" +
            "###.####"
    val split = input.split("\n")

    val initialState = getInitialState(15, 15, 6, 6, split)

    println(initialState)

    var state = initialState
    for(round in 1..6) {
        state = getNewState(state)
        val active = state.filter { c -> c[4] == 1 }
        println("Nr active cubes: ${active.size}")
    }


}

private fun getNewState(currentState: MutableList<MutableList<Int>>): MutableList<MutableList<Int>> {
    val newState = mutableListOf<MutableList<Int>>()
    var count = 0;
    for (cube in currentState) {
        val nrActive = countActiveNeighbors(currentState, cube)
        val cubeCopy = cube.toMutableList()
        if (cubeCopy[4] == 1) {
            if (nrActive != 2 && nrActive != 3) cubeCopy[4] = 0
        } else {
            if (nrActive == 3) cubeCopy[4] = 1
        }
        newState.add(cubeCopy)
        count++
        if(count % 10000 == 0) println("Cubes checked: $count")
    }
    return newState
}

private fun countActiveNeighbors(cubes: MutableList<MutableList<Int>>, cube: MutableList<Int>): Int {
    var count = 0
    for (otherCube in cubes) {
        if(otherCube[4] == 0 || otherCube == cube) continue
        val isNeighbor = abs(cube[0] - otherCube[0]) < 2
                && abs(cube[1] - otherCube[1]) < 2
                && abs(cube[2] - otherCube[2]) < 2
                && abs(cube[3] - otherCube[3]) < 2
        if (isNeighbor) count++
    }
    return count
}

private fun getInitialState(yMax: Int, xMax: Int, zMax: Int, wMax: Int, input: List<String>): MutableList<MutableList<Int>> {
    val initialState = mutableListOf<MutableList<Int>>()
    for(w in -wMax..wMax) {
        for(z in -zMax..zMax) {
            for (y in -yMax..yMax) {
                for (x in -xMax..xMax) {
                    var state = 0
                    if((y > -1 && y < input.size) && (x > -1 && x < input[0].length) && z == 0 && w == 0) {
                        if (input[y][x] == '#') state = 1
                    }
                    initialState.add(mutableListOf(x, y, z, w, state))
                }
            }
        }
    }
    return initialState
}