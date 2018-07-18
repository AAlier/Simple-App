package neobis.o.ui.album_photo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_photo.view.*
import neobis.o.R
import neobis.o.model.Photos

/**
 * Created by Alier on 18.07.2018.
 */
class PhotoAdapter(private var list: List<Photos>, var listener: Listener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var generator = ColorGenerator.MATERIAL

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_photo, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ViewHolder)?.bind(position)
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {

            // Get the first letter of list item
            val letter = list[position].title?.get(0).toString()
            // Create a new TextDrawable for our image's background
            val drawable = TextDrawable.builder().buildRound(letter, generator.randomColor)
            Glide.with(itemView.context).load(list[position].thumbnailUrl).error(drawable).into(itemView.imageView)
            itemView.titleView.text = list[position].title
            itemView.tag = position
            itemView.setOnClickListener { v ->
                val index = v.tag as Int
                listener.onItemClickedAt(list.get(index))
            }
        }
    }

    fun setAlbumPhotoList(otherList: List<Photos>) {
        list = otherList;
        notifyDataSetChanged()
    }

    interface Listener {
        fun  onItemClickedAt(photo: Photos)
    }
}