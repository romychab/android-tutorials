package ua.cn.stu.tests.domain

import org.junit.Assert.*
import org.junit.Test
import ua.cn.stu.tests.testutils.catch
import ua.cn.stu.tests.testutils.wellDone

class ResultTest {

    @Test
    fun getValueOrNullReturnsNullForNonSuccessResults() {
        val emptyResult = Empty<String>()
        val pendingResult = Pending<String>()
        val errorResult = Error<String>(Exception())

        val emptyValue = emptyResult.getValueOrNull()
        val pendingValue = pendingResult.getValueOrNull()
        val errorValue = errorResult.getValueOrNull()

        assertNull(emptyValue)
        assertNull(pendingValue)
        assertNull(errorValue)
    }

    @Test
    fun getValueOrNullReturnsValueForSuccessResult() {
        val successResult = Success("test")

        val value = successResult.getValueOrNull()

        assertEquals("test", value)
    }

    @Test
    fun isFinishedForSuccessAndErrorReturnsTrue() {
        val errorResult = Error<String>(Exception())
        val successResult = Success("test")

        val isErrorFinished = errorResult.isFinished()
        val isSuccessFinished = successResult.isFinished()

        assertTrue(isErrorFinished)
        assertTrue(isSuccessFinished)
    }

    @Test
    fun isFinishedForEmptyAndPendingReturnsFalse() {
        val emptyResult = Empty<String>()
        val pendingResult = Pending<String>()

        val isEmptyFinished = emptyResult.isFinished()
        val isPendingFinished = pendingResult.isFinished()

        assertFalse(isEmptyFinished)
        assertFalse(isPendingFinished)
    }

    @Test
    fun testNonSuccessResultsMapping() {
        val exception = Exception()
        val emptyResult = Empty<String>()
        val pendingResult = Pending<String>()
        val errorResult = Error<String>(exception)

        val mappedEmptyResult = emptyResult.map<Int>()
        val mappedPendingResult = pendingResult.map<Int>()
        val mappedErrorResult = errorResult.map<Int>()

        assertTrue(mappedEmptyResult is Empty<Int>)
        assertTrue(mappedPendingResult is Pending<Int>)
        assertTrue(mappedErrorResult is Error<Int>)
        assertSame(exception, (mappedErrorResult as Error<Int>).error)
    }

    @Test
    fun mapWithoutMapperCantConvertSuccessResult() {
        val result = Success("test")

        catch<IllegalStateException> { result.map<Int>() }

        wellDone()
    }

    @Test
    fun mapWithMapperConvertsSuccessToSuccess() {
        val result = Success("123")

        val mappedResult = result.map { it.toInt() }

        assertTrue(mappedResult is Success<Int>)
        assertEquals(123, (mappedResult as Success<Int>).value)
    }

    @Test
    fun testEquals() {
        val exception = IllegalStateException()
        val pending1 = Pending<String>()
        val pending2 = Pending<String>()
        val empty1 = Empty<String>()
        val empty2 = Empty<String>()
        val error1 = Error<String>(exception)
        val error2 = Error<String>(exception)
        val success1 = Success("val")
        val success2 = Success("val")

        assertEquals(pending1, pending2)
        assertEquals(empty1, empty2)
        assertEquals(error1, error2)
        assertEquals(success1, success2)
    }

    @Test
    fun testNotEquals() {
        val pending = Pending<String>()
        val empty = Empty<String>()
        val error1 = Error<String>(IllegalStateException())
        val error2 = Error<String>(IllegalStateException())
        val success1 = Success("val1")
        val success2 = Success("val2")

        assertNotEquals(pending, empty)
        assertNotEquals(pending, error1)
        assertNotEquals(pending, success1)
        assertNotEquals(empty, error1)
        assertNotEquals(empty, success1)
        assertNotEquals(error1, error2)
        assertNotEquals(error1, success1)
        assertNotEquals(success1, success2)
    }

    @Test
    fun testHashCode() {
        val exception = IllegalStateException()
        val pending1 = Pending<String>()
        val pending2 = Pending<String>()
        val empty1 = Empty<String>()
        val empty2 = Empty<String>()
        val error1 = Error<String>(exception)
        val error2 = Error<String>(exception)
        val success1 = Success("val")
        val success2 = Success("val")

        assertEquals(pending1.hashCode(), pending2.hashCode())
        assertEquals(empty1.hashCode(), empty2.hashCode())
        assertEquals(error1.hashCode(), error2.hashCode())
        assertEquals(success1.hashCode(), success2.hashCode())
    }

}
