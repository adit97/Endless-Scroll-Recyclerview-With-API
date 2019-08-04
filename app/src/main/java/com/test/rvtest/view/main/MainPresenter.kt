package com.test.rvtest.view.main

import android.annotation.SuppressLint
import android.os.AsyncTask
import com.test.rvtest.App
import com.test.rvtest.model.ResultResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainPresenter(private val view: MainView.Main) : MainView.Presenter {

    companion object {
        const val LIMIT = 15
        const val MAX_PAGE = 3
    }

    var CURRENT_PAGE = 0

    override fun getItems(isLoadMore: Boolean) {
        CURRENT_PAGE += 1
        ResultAsync(isLoadMore).execute()
    }

    override fun resetPagination() {
        CURRENT_PAGE = 0
    }

    @SuppressLint("StaticFieldLeak")
    inner class ResultAsync(
        private val isLoadMore: Boolean
    ) : AsyncTask<Unit, Unit, MutableList<Any?>>() {

        override fun doInBackground(vararg params: Unit?): MutableList<Any?> {

            val itemList : MutableList<Any?> = mutableListOf()

            if (CURRENT_PAGE <= MAX_PAGE) {
                App.api.search(
                    CURRENT_PAGE,
                    LIMIT,
                    "desc",
                    "activity",
                    "android",
                    "stackoverflow").enqueue(object: Callback<ResultResponse>{
                    override fun onFailure(call: Call<ResultResponse>, t: Throwable) {

                    }

                    override fun onResponse(call: Call<ResultResponse>, response: Response<ResultResponse>) {
                        val body = response.body()
                        if(body != null){
                            itemList.addAll(body.items)
                        }
                    }
                })

                try {
                    Thread.sleep(5000)
                } catch (e: Exception) {

                }
            } else {
                itemList.add(null)
            }


            return itemList
        }

        override fun onPreExecute() {
            super.onPreExecute()

            if (isLoadMore)
                view.onLoadMoreStart()
            else
                view.showInitialLoading()
        }

        override fun onPostExecute(result: MutableList<Any?>?) {
            super.onPostExecute(result)

            if (isLoadMore) {
                view.onLoadMoreComplete()

                if (result == null)
                    view.onLoadMoreEnd()
            } else {
                view.hideInitialLoading()
            }

            if (result != null)
                view.showResult(result)
        }
    }
}