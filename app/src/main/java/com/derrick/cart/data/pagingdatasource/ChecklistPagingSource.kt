package com.derrick.cart.data.pagingdatasource

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.derrick.cart.data.local.entities.Checklist

class ChecklistPagingSource : PagingSource<Int, Checklist>() {
    override fun getRefreshKey(state: PagingState<Int, Checklist>): Int? {
        TODO("Not yet implemented")
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Checklist> {
        TODO("Not yet implemented")
    }
}