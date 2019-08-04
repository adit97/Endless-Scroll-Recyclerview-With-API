package com.test.rvtest.view.main

interface MainView {

    interface Main {
        fun showResult(data: List<Any?>?)
        fun showInitialLoading()
        fun hideInitialLoading()
        fun onLoadMoreStart()
        fun onLoadMoreComplete()
        fun onLoadMoreEnd()
    }

    interface Presenter {
        fun getItems(isLoadMore: Boolean)

        fun resetPagination()
    }
}