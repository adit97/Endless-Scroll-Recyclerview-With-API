package com.test.rvtest.view.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.test.rvtest.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView.Main, MainAdapter.OnLoadMoreListener {
    private val presenter = MainPresenter(this)
    private val dataList = mutableListOf<Any?>()
    private val linearLayoutManager = LinearLayoutManager(this)
    private val dataAdapter = MainAdapter(dataList, linearLayoutManager, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

        initLoadItem()
    }

    override fun onLoadMore() {
        presenter.getItems(true)
    }

    private fun initLoadItem() {
        presenter.getItems(false)
    }

    private fun initView() {

        rv.run {
            dataAdapter.setRecyclerView(this)

            layoutManager = linearLayoutManager
            adapter = dataAdapter
        }

        swipe.setOnRefreshListener {
            swipe.isRefreshing = false

            dataList.clear()
            presenter.CURRENT_PAGE = 0

            initLoadItem()
        }
    }

    override fun showResult(data: List<Any?>?) {
        if (data != null) {
            dataList.addAll(data)
            dataAdapter.notifyDataSetChanged()
        }
    }

    override fun showInitialLoading() {
        mainprogressbar.visibility = View.VISIBLE
    }

    override fun hideInitialLoading() {
        mainprogressbar.visibility = View.GONE
    }

    override fun onLoadMoreStart() {
        dataAdapter.setLoadMoreProgress(true)
    }

    override fun onLoadMoreComplete() {
        dataAdapter.setLoadMoreProgress(false)
    }

    override fun onLoadMoreEnd() {
        dataAdapter.removeScrollListener()
    }
}
