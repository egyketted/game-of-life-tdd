class GameOfLife {
    private var livingCells: MutableSet<Cell> = mutableSetOf()

    fun startGame(livingCells: Collection<Cell>) {
        this.livingCells = mutableSetOf()
        this.livingCells.addAll(livingCells)
    }

    fun getLivingCells() = livingCells.toMutableList()

    fun nextGeneration() {
        val nextGenerationCells: MutableList<Cell> = mutableListOf()
        livingCells.forEach{if (countLivingNeighbours(it) == 2) nextGenerationCells.add(it)}
        livingCells.flatMap(this::getNeighbours)
            .toSet()
            .forEach{if (countLivingNeighbours(it) == 3) nextGenerationCells.add(it)}


        livingCells = nextGenerationCells.toMutableSet()
    }

    private fun countLivingNeighbours(cell: Cell): Int =
        Direction.values().count { livingCells.contains(it.getNeighbourInDirection(cell)) }

    private fun getNeighbours(cell: Cell): Set<Cell> =
        Direction.values().map { it.getNeighbourInDirection(cell) }.toSet()
}

data class Cell(val x: Int, val y: Int)

enum class Direction(private val x: Int, private val y:Int) {
    UP(-1, 0),
    DOWN(1, 0),
    LEFT(0, -1),
    RIGHT(0, 1),
    UP_LEFT(-1, -1),
    UP_RIGHT(-1, 1),
    DOWN_LEFT(1, -1),
    DOWN_RIGHT(1, 1);

    fun getNeighbourInDirection(cell: Cell): Cell = Cell(cell.x + x, cell.y + y)
}