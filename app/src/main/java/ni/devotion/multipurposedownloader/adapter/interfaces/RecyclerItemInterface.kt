package ni.devotion.multipurposedownloader.adapter.interfaces

import ni.devotion.multipurposedownloader.models.Information

interface RecyclerItemInterface{
    fun onItemClicked(information: Information)
    fun onLoadMore()
}