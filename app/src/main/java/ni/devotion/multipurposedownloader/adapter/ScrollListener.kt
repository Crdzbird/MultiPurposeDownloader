package ni.devotion.multipurposedownloader.adapter

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import ni.devotion.multipurposedownloader.adapter.interfaces.RecyclerItemInterface
import java.util.*


class ScrollListener(private val layoutManager: RecyclerView.LayoutManager, private val recyclerItemInterface: RecyclerItemInterface) : RecyclerView.OnScrollListener() {
    private var visibleThreshold = 10
    private var lastVisibleItem: Int = 0
    private var totalItemCount:Int = 0

    init {
        when(layoutManager){
            is GridLayoutManager -> { visibleThreshold *= layoutManager.spanCount }
            is StaggeredGridLayoutManager -> { visibleThreshold *= layoutManager.spanCount }
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy <= 0) return
        totalItemCount = layoutManager.itemCount
        when (layoutManager) {
            is StaggeredGridLayoutManager -> {
                val lastVisibleItemPositions = layoutManager.findLastVisibleItemPositions(null)
                lastVisibleItem = getLastVisibleItem(lastVisibleItemPositions)
            }
            is GridLayoutManager -> { lastVisibleItem = layoutManager.findLastVisibleItemPosition() }
            is LinearLayoutManager -> { lastVisibleItem = layoutManager.findLastVisibleItemPosition() }
        }
        if (totalItemCount <= lastVisibleItem + 1) recyclerItemInterface.onLoadMore()
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