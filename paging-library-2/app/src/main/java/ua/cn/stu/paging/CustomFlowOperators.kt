package ua.cn.stu.paging

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.scan
import java.util.*

/**
 * Emits the previous values (`null` if there is no previous values) along with the current one.
 * For example:
 * - origin flow:
 *   ```
 *   [
 *     "a",
 *     "b",
 *     "c",
 *     "d"
 *   ]
 *   ```
 * - resulting flow (count = 2):
 *   ```
 *   [
 *     (null, null)
 *     (null, "a"),
 *     ("a",  "b"),
 *     ("b",  "c"),
 *     ("c",  "d")
 *   ]
 *   ```
 */
@ExperimentalCoroutinesApi
fun <T> Flow<T>.simpleScan(count: Int): Flow<List<T?>> {
    val items = List<T?>(count) { null }
    return this.scan(items) { previous, value -> previous.drop(1) + value }
}