package ua.cn.stu.tests.testutils

import org.junit.Assert

/**
 * Catch the exception thrown by the [block] and check the exception type [T].
 * This method fails with [AssertionError] if:
 * - exception type is not a type or subtype of [T]
 * - exception is not thrown by the [block]
 */
inline fun <reified T : Throwable> catch(block: () -> Unit): T {
    try {
        block()
    } catch (e: Throwable) {
        if (e is T) {
            return e
        } else {
            Assert.fail("Invalid exception type. " +
                    "Expected: ${T::class.java.simpleName}, " +
                    "Actual: ${e.javaClass.simpleName}")
        }
    }
    throw AssertionError("No expected exception")
}

fun wellDone() {
    // indicates test passed successfully
}

fun arranged() {
    // indicates Arrange section is empty and thus it's already done.
    // Look for the method annotated with @Before annotation if you see
    // this call.
}