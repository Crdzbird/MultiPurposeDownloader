package ni.devotion.multipurposedownloader.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.faltenreich.skeletonlayout.Skeleton
import com.faltenreich.skeletonlayout.applySkeleton
import kotlinx.android.synthetic.main.activity_main.*
import ni.devotion.multidataparser.selector.MultiDataParser
import ni.devotion.multipurposedownloader.R
import ni.devotion.multipurposedownloader.adapter.InformationAdapter
import ni.devotion.multipurposedownloader.adapter.ScrollListener
import ni.devotion.multipurposedownloader.adapter.interfaces.RecyclerItemInterface
import ni.devotion.multipurposedownloader.ui.viewModel.InformationViewModel
import ni.devotion.multipurposedownloader.util.GridItemDecoration
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), RecyclerItemInterface {

    private val informationViewModel: InformationViewModel by viewModel()
    private lateinit var skeleton: Skeleton
    lateinit var scrollListener: ScrollListener
    private val informationAdapter: InformationAdapter by lazy { InformationAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecycler()
        setRVScrollListener()
        setupSkeleton()
        initializeObserverInformation()
        informationViewModel.requestInformation()
    }

    private fun initializeObserverInformation(){
        informationViewModel.uiState.observe(this, Observer {
            val dataState = it ?: return@Observer
            if (!dataState.showProgress){
                scrollListener.isLoading = true
                displayHideSkeleton(hide = true)
            }else{
                scrollListener.isLoading = false
            }
            if (dataState.result != null && !dataState.result.consumed){
                dataState.result.consume()?.let { list_information ->
                    if(list_information.isEmpty()){
//                        rv_information.visibility = View.GONE
//                        playAnimation(R.raw.empty)
                    } else {
//                        animationContainer.cancelAnimation()
//                        animationContainer.visibility = View.GONE
//                        rv_information.visibility = View.VISIBLE
                        informationAdapter.submitList(list_information)
                    }
                }
            }
            if (dataState.error != null && !dataState.error.consumed){
                dataState.error.consume()?.let { error ->
                    rv_information.visibility = View.GONE
                    //playAnimation(R.raw.not_found)
                    Toast.makeText(applicationContext, resources.getString(error), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun setupRecycler(){
        rv_information.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            addItemDecoration(GridItemDecoration(4, 2))
            adapter = informationAdapter
            setHasFixedSize(true)
        }
    }

    private fun setRVScrollListener() {
        scrollListener = ScrollListener(rv_information.layoutManager as StaggeredGridLayoutManager, this)
        rv_information.addOnScrollListener(scrollListener)
    }

    private fun setupSkeleton(){
        skeleton = rv_information.applySkeleton(R.layout.information_layout, 10)
        skeleton.showShimmer = true
        skeleton.shimmerDurationInMillis = 900
        skeleton.maskCornerRadius = 0f
    }

    private fun displayHideSkeleton(hide: Boolean = false){
        if(hide) {
            if (skeleton.isSkeleton()) skeleton.showOriginal()
        } else skeleton.showSkeleton()
    }


    override fun onItemClicked(position: Int) {
    }

    override fun onLoadMore() {
        println("ESTA CARGANDO MAAAAAS")
        informationViewModel.requestInformation()
    }
}
