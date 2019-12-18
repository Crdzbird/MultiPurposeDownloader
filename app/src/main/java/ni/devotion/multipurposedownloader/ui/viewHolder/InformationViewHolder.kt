package ni.devotion.multipurposedownloader.ui.viewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.information_content.*
import ni.devotion.multidataparser.selector.MultiDataParser
import ni.devotion.multipurposedownloader.R
import ni.devotion.multipurposedownloader.adapter.interfaces.RecyclerItemInterface
import ni.devotion.multipurposedownloader.models.Information
import ni.devotion.multipurposedownloader.util.ImageSizer

class InformationViewHolder constructor(override val containerView: View) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    fun bind(information: Information, recyclerItemInterface: RecyclerItemInterface) {
        profile_image?.let { MultiDataParser().obtainImage(containerView.context).load(it, information.user.profile_image.small) }
        username.text = information.user.name
        createdAt.text = information.created_at
        informationImage?.let { MultiDataParser().obtainImage(containerView.context).load(it, information.urls.small) }
        total_likes.text = containerView.context.getString(R.string.likes, information.likes)
        informationImage.layoutParams.height = ImageSizer().getRandomIntInRange(250, 180)
    }

    companion object {
        fun create(parent: ViewGroup): InformationViewHolder {
            return InformationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.information_layout, parent, false))
        }
    }
}