package ni.devotion.multipurposedownloader.adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ni.devotion.multipurposedownloader.adapter.interfaces.RecyclerItemInterface
import java.util.*


class ScrollListener(layoutManager: RecyclerView.LayoutManager, val recyclerItemInterface: RecyclerItemInterface) : RecyclerView.OnScrollListener() {

    private var visibleThreshold = 10
    var isLoading: Boolean = false
    private var lastVisibleItem: Int = 0
    private var totalItemCount:Int = 0
    private lateinit var mLayoutManager: RecyclerView.LayoutManager

    init {
        when(layoutManager){
            is GridLayoutManager -> {
                this.mLayoutManager = layoutManager
                visibleThreshold *= layoutManager.spanCount
            }
            is LinearLayoutManager -> {
                this.mLayoutManager = layoutManager
            }
            is StaggeredGridLayoutManager -> {
                this.mLayoutManager = layoutManager
                visibleThreshold *= layoutManager.spanCount
            }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy <= 0) return
        totalItemCount = mLayoutManager.itemCount
        when (mLayoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions = (Objects.requireNonNull(recyclerView.layoutManager) as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
                //val lastVisibleItemPositions = (mLayoutManager as StaggeredGridLayoutManager).findLastVisibleItemPositions(null)
                lastVisibleItem = getLastVisibleItem(lastVisibleItemPositions)
            }
            is GridLayoutManager -> {
                lastVisibleItem = (mLayoutManager as GridLayoutManager).findLastVisibleItemPosition()
            }
            is LinearLayoutManager -> {
                lastVisibleItem = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            }
        }
        if (!isLoading && totalItemCount <= lastVisibleItem + visibleThreshold) {
            println("ESTA CARGANDO MAS")
            recyclerItemInterface.onLoadMore()
            isLoading = true
        }

    }

    private fun getLastVisibleItem(lastVisibleItemPositions: IntArray): Int {
        var maxSize = 0
        for (i in lastVisibleItemPositions.indices) {
            if (i == 0) {
                maxSize = lastVisibleItemPositions[i]
            } else if (lastVisibleItemPositions[i] > maxSize) {
                maxSize = lastVisibleItemPositions[i]
            }
        }
        return maxSize
    }
}