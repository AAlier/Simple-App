package neobis.o.ui.post_comments

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import kotlinx.android.synthetic.main.item_comment.view.*
import neobis.o.R
import neobis.o.model.Comment

/**
 * Created by Alier on 18.07.2018.
 */
class CommentAdapter(private var list: List<Comment>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var generator = ColorGenerator.MATERIAL

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ViewHolder)?.bind(position)
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {

            // Get the first letter of list item
            val letter = list[position].name?.get(0).toString()
            // Create a new TextDrawable for our image's background
            val drawable = TextDrawable.builder().buildRound(letter, generator.randomColor)
            itemView.imageView.setImageDrawable(drawable)
            itemView.nameView?.text = list[position].name
            itemView.emailView?.text = list[position].email
            itemView.bodyView?.text = list[position].body
        }
    }

    fun setPostCommentList(otherList: List<Comment>) {
        list = otherList;
        notifyDataSetChanged()
    }
}