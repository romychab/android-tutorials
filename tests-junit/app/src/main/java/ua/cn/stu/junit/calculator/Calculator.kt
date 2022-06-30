package ua.cn.stu.junit.calculator

class Calculator {

    fun init() {
        // some init logic may be placed here
    }

    fun destroy() {
        // some release logic may be placed here
    }

    fun sum(a: Double, b: Double): Double = a + b

    fun subtract(a: Double, b: Double): Double = a - b

    fun multiply(a: Double, b: Double): Double = a * b

    fun divide(a: Double, b: Double): Double {
        if (b == 0.0) throw IllegalStateException("Division by zero")
        return a / b
    }

}