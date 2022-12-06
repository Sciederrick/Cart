package com.derrick.cart.data.pagingdatasource
//
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//import com.derrick.cart.data.local.daos.ChecklistDao
//import com.derrick.cart.data.local.entities.Checklist
//import com.derrick.cart.data.repository.CartRepository
//import kotlinx.coroutines.delay
//import kotlin.math.max
//
//private const val STARTING_KEY = 0
////private const val LOAD_DELAY_MILLIS = 3_000L
//
//class ChecklistPagingSource(private val checklistDao: ChecklistDao): PagingSource<Int, Checklist>() {
//    override fun getRefreshKey(state: PagingState<Int, Checklist>): Int? {
//        val anchorPosition = state.anchorPosition ?: return null
//        val checklist = state.closestItemToPosition(anchorPosition) ?: return null
//        return ensureValidKey(key = checklist.id.toInt() - (state.config.pageSize / 2))
//
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Checklist> {
//        val start = params.key ?: STARTING_KEY
//
//        val range = start.until(start + params.loadSize)
//
////        if (start != STARTING_KEY) delay(LOAD_DELAY_MILLIS)
//
//
//        return LoadResult.Page(
////            data = range.map { number ->
////                Checklist(
////                    // Generate consecutive increasing numbers as the article id
////                    id = number.toLong(),
////                    title = "Article $number",
////                    tags = "one, two, three",
////                    size = number.toLong()
////                )
////            },
////            data = checklistDao.getByIds(range.toList()),
//            data = checklistDao.getAll,
//            // Make sure we don't try to load items behind the STARTING_KEY
//            prevKey = when (start) {
//                STARTING_KEY -> null
//                else -> ensureValidKey(key = range.first - params.loadSize)
//            },
//            nextKey = range.last + 1
//        )
//
//    }
//
//    /**
//     * Makes sure the paging key is never less than [STARTING_KEY]
//     */
//    private fun ensureValidKey(key: Int) = max(STARTING_KEY, key)
//
//}