import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GameOfLifeTest {

    private var gameOfLife: GameOfLife = GameOfLife()

    @BeforeEach
    fun setup() {
        gameOfLife = GameOfLife()
    }

    @Test
    fun startGameShouldInitializeTheGameFieldWithProvidedCells() {
        //Given
        val expectedStartingCells:List<Cell> = listOf(Cell(1, 2), Cell(2, 2), Cell(3, 2))

        //When
        gameOfLife.startGame(expectedStartingCells)

        //Then
        Assertions.assertEquals(expectedStartingCells, gameOfLife.getLivingCells())
    }

    @Test
    fun listOfLivingCellsShouldNotBeModifiableFromTheOutside() {
        //Given
        val expectedStartingCells:List<Cell> = listOf(Cell(1, 2), Cell(2, 2), Cell(3, 2))
        gameOfLife.startGame(expectedStartingCells)

        //When
        var livingCellsModified = gameOfLife.getLivingCells()
        livingCellsModified.removeAt(0)

        //Then
        Assertions.assertEquals(expectedStartingCells, gameOfLife.getLivingCells())
    }

    @Test
    fun nextGenerationShouldKillSingleLivingCellWithNoNeighbours() {
        //Given
        val singleLivingCell: List<Cell> = listOf(Cell(1,1))
        gameOfLife.startGame(singleLivingCell)

        //When
        gameOfLife.nextGeneration()

        //Then
        Assertions.assertEquals(0, gameOfLife.getLivingCells().size)
    }

    @Test
    fun nextGenerationShouldKillCellsWithSingleLivingNeighbour() {
        //Given
        val threeLivingCellsInALine: List<Cell> = listOf(Cell(1,1), Cell(1,2), Cell(1,3))
        val expectedToDie1: Cell = Cell(1,1)
        val expectedToDie2: Cell = Cell(1,3)
        gameOfLife.startGame(threeLivingCellsInALine)

        //When
        gameOfLife.nextGeneration()

        //Then
        Assertions.assertFalse(gameOfLife.getLivingCells().contains(expectedToDie1))
        Assertions.assertFalse(gameOfLife.getLivingCells().contains(expectedToDie2))
    }

    @Test
    fun nextGenerationShouldNotKillCellsWithTwoLivingNeighbours() {
        //Given
        val threeLivingCellsInALine: List<Cell> = listOf(Cell(1,1), Cell(1,2), Cell(1,3))
        val expectedToSurvive: Cell = Cell(1,2)
        gameOfLife.startGame(threeLivingCellsInALine)

        //When
        gameOfLife.nextGeneration()

        //Then
        Assertions.assertTrue(gameOfLife.getLivingCells().contains(expectedToSurvive))
    }

    @Test
    fun nextGenerationShouldReviveDeadCellsWithThreeNeighbours() {
        //Given
        val threeLivingCellsInALine: List<Cell> = listOf(Cell(1,1), Cell(1,2), Cell(1,3))
        val expectedToSurvive: Cell = Cell(1, 2)
        val expectedToBeRevived1: Cell = Cell(0,2)
        val expectedToBeRevived2: Cell = Cell(2,2)
        gameOfLife.startGame(threeLivingCellsInALine)

        //When
        gameOfLife.nextGeneration()

        //Then
        Assertions.assertTrue(gameOfLife.getLivingCells().contains(expectedToBeRevived1))
        Assertions.assertTrue(gameOfLife.getLivingCells().contains(expectedToBeRevived2))

        Assertions.assertTrue(gameOfLife.getLivingCells().contains(expectedToSurvive))
    }

    @Test
    fun nextGenerationShouldKillCellsWithMoreThanThreeNeighbours() {
        //Given
        val middleCell = Cell(1,1)
        val fiveCells: List<Cell> = listOf(middleCell,
            Direction.UP.getNeighbourInDirection(middleCell),
            Direction.DOWN.getNeighbourInDirection(middleCell),
            Direction.LEFT.getNeighbourInDirection(middleCell),
            Direction.RIGHT.getNeighbourInDirection(middleCell))
        val cellWithNeighboursInAllDirections: MutableList<Cell> = Direction.values().map {it.getNeighbourInDirection(middleCell)}.toMutableList()
        cellWithNeighboursInAllDirections.add(middleCell)

        //When
        gameOfLife.startGame(fiveCells)
        gameOfLife.nextGeneration()
        val resultWithFiveCells = gameOfLife.getLivingCells()

        gameOfLife.startGame(cellWithNeighboursInAllDirections)
        gameOfLife.nextGeneration()
        val resultWithNeighboursInAllDirections = gameOfLife.getLivingCells()

        //Then
        Assertions.assertFalse(resultWithFiveCells.contains(middleCell))
        Assertions.assertFalse(resultWithNeighboursInAllDirections.contains(middleCell))
    }

    @Test
    fun nextGenerationShouldNotDuplicateCells() {
        //Given
        val threeLivingCellsInALine: List<Cell> = listOf(Cell(1,1), Cell(1,2), Cell(1,3))
        val expectedToSurvive: Cell = Cell(1, 2)
        gameOfLife.startGame(threeLivingCellsInALine)

        //When
        gameOfLife.nextGeneration()
        gameOfLife.nextGeneration()
        gameOfLife.nextGeneration()
        gameOfLife.nextGeneration()

        //Then
        Assertions.assertTrue(gameOfLife.getLivingCells().count { it == expectedToSurvive } == 1)
    }
}