package ua.cn.stu.junit.calculator

import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CalculatorTest {

    private val delta = 0.00001

    private lateinit var calculator: Calculator

    @Before
    fun setUp() {
        calculator = Calculator()
        calculator.init()
    }

    @After
    fun cleanUp() {
        calculator.destroy()
    }

    @Test(expected = IllegalStateException::class)
    fun `divide() method with b=0 should throw IllegalStateException`() {
        // arrange
        // -

        // act
        calculator.divide(10.0, 0.0)

        // assert
        // -
    }

    @Test
    fun subtractCalculatesSubtraction() {
        // arrange
        val calculator = Calculator()
        calculator.init()

        // act
        val result = calculator.subtract(3.0, 2.0)

        // assert
        Assert.assertEquals(1.0, result, delta)
    }

    @Test
    fun sumCalculatesSum() {
        // arrange
        // -

        // act
        val result = calculator.sum(2.0, 2.0)

        // assert
        Assert.assertEquals(4.0, result, delta)
    }


}