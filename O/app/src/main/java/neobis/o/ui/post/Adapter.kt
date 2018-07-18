package neobis.o.ui.post

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.amulyakhare.textdrawable.TextDrawable
import com.amulyakhare.textdrawable.util.ColorGenerator
import kotlinx.android.synthetic.main.item_post.view.*
import neobis.o.R
import neobis.o.model.Post
import neobis.o.model.WeatherInfo
import java.lang.RuntimeException
import java.util.*


/**
 * Created by Alier on 18.07.2018.
 */
class Adapter(var list: MutableList<Any>, private var listener: Listener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var generator = ColorGenerator.MATERIAL
    private val POST = 0
    private val WEATHER = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_post, parent, false)
        return when (viewType) {
            POST -> {
                ViewHolder(view)
            }
            WEATHER -> {
                WeatherViewHolder(view)
            }
            else -> {
                throw RuntimeException("Incorrect View Holder")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder)
            holder.bind(list.get(position) as Post)
        else if (holder is WeatherViewHolder)
            holder.bind(list.get(position) as WeatherInfo)
    }

    override fun getItemViewType(position: Int): Int {
        if (list.get(position) is Post) {
            return POST
        } else if (list.get(position) is WeatherInfo) {
            return WEATHER
        }
        return super.getItemViewType(position)
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(post: Post) {
            itemView.mainView.setBackgroundColor(0)
            // Get the first letter of list item
            val letter = post.title?.get(0).toString()
            // Create a new TextDrawable for our image's background
            val drawable = TextDrawable.builder().buildRound(letter, generator.randomColor)
            itemView.imageView.setImageDrawable(drawable)
            itemView.titleView.text = post.title
            itemView.descriptionView.text = post.body

            itemView.tag = post
            itemView.setOnClickListener { v ->
                val mPost = v.tag as Post
                listener.onPostClicked(mPost)
            }
        }
    }

    inner class WeatherViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        fun bind(weather: WeatherInfo) {
            itemView.mainView.setBackgroundColor(Color.parseColor("#A7C2FA"))
            itemView.imageView.setImageResource(R.mipmap.weather)
            itemView.titleView.text = weather.name
            itemView.descriptionView.text = weather.main?.temp.toString()

            itemView.setOnClickListener { v ->
                listener.onWeatherClicked()
            }
        }
    }

    inner class OtherViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView)

    fun setPostList(otherList: MutableList<Any>) {
        list = otherList;
        notifyDataSetChanged()
    }

    interface Listener {
        fun onPostClicked(post: Post)
        fun onWeatherClicked()
    }

    fun addAtRandom(result: WeatherInfo) {
        val index = getRandomNumberInRange(0, itemCount)
        list.add(index, result)
        notifyItemInserted(index)
    }

    private fun getRandomNumberInRange(min: Int, max: Int): Int {

        if (min >= max) {
            throw IllegalArgumentException("max must be greater than min")
        }

        val r = Random()
        return r.nextInt(max - min + 1) + min
    }
}