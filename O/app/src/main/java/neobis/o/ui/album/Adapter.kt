package neobis.o.ui.album

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import kotlinx.android.synthetic.main.item_album.view.*
import neobis.o.R
import neobis.o.model.Album

/**
 * Created by Alier on 18.07.2018.
 */

class Adapter(private var list: List<Album>, var listener: Listener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var generator = ColorGenerator.MATERIAL

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_album, parent, false)
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
            itemView.imageView.setImageDrawable(drawable)
            itemView.titleView.text = list[position].title

            itemView.tag = position
            itemView.setOnClickListener { v ->
                val index = v.tag as Int
                listener.onItemAtClicked(list.get(index))
            }
        }
    }

    fun setAlbumList(otherList: List<Album>) {
        list = otherList;
        notifyDataSetChanged()
    }

    interface Listener {
        fun onItemAtClicked(album: Album)
    }
}