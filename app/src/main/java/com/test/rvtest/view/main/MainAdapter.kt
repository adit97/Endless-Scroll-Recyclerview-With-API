package com.test.rvtest.view.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.rvtest.R
import com.test.rvtest.model.Item
import kotlinx.android.synthetic.main.rv_item.view.*
import kotlinx.android.synthetic.main.rv_load.view.*

class MainAdapter(
        private var itemList: MutableList<Any?>,
        private var linearLayoutManager: LinearLayoutManager,
        private val onLoadMoreListener: OnLoadMoreListener?
) : RecyclerView.Adapter<MainAdapter.Holder>() {

    companion object {
        const val VIEW_ITEM = 1
        const val VIEW_LOADMORE = 0
    }

    var isMoreLoading = false
    var visibleThreshold = 15
    var lasttVisibleItem = 0
    var totalItemCount = 0

    private lateinit var recyclerView: RecyclerView

    private lateinit var scrollListener: RecyclerView.OnScrollListener


    fun setLoadMoreProgress(isLoadMore: Boolean) {
        isMoreLoading = isLoadMore

        if (isLoadMore) {
            itemList.add(itemCount, null)
            notifyItemInserted(itemCount - 1)
        } else {
            if (itemCount > 0) {
                itemList.removeAt(itemCount - 1)
                notifyItemRemoved(itemCount - 1)
            }
        }
    }

    fun removeScrollListener() {
        recyclerView.removeOnScrollListener(scrollListener)
    }

    fun setRecyclerView(recyclerView: RecyclerView) {
        this.recyclerView = recyclerView

        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                totalItemCount = linearLayoutManager.itemCount
                lasttVisibleItem = linearLayoutManager.findLastVisibleItemPosition()

                if (dy > 0) {
                    if (!isMoreLoading && totalItemCount <= (lasttVisibleItem + visibleThreshold)) {
                        onLoadMoreListener?.onLoadMore()

                        isMoreLoading = true
                    }
                }
            }
        }

        recyclerView.addOnScrollListener(scrollListener)
    }

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val inflater = LayoutInflater.from(parent.context)

        return if (viewType == VIEW_ITEM) {
            Holder(inflater.inflate(R.layout.rv_item, parent, false))
        } else {
            Holder(inflater.inflate(R.layout.rv_load, parent, false))
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = itemList[position]

        holder.bind(item)
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position] is Item) VIEW_ITEM else VIEW_LOADMORE
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(item: Any?) = itemView.apply {
            if (item is Item) {
                title.text = item.title
            } else {
                if (isMoreLoading) {
                    pgbar.visibility = View.VISIBLE
                } else {
                    pgbar.visibility = View.GONE
                }
            }

        }
    }
}