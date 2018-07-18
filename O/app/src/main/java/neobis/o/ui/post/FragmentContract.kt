package neobis.o.ui.post

import neobis.alier.bilimkana.util.IProgressBar
import neobis.alier.bilimkana.util.IResult
import neobis.o.model.Comment
import neobis.o.model.Post
import neobis.o.model.Weather
import neobis.o.model.WeatherInfo

/**
 * Created by Alier on 18.07.2018.
 */
interface FragmentContract {

    interface View : IProgressBar, IResult<MutableList<Post>>{
        fun onWeatherResponse(result: WeatherInfo)
        fun onSelectComments(commentlist: ArrayList<Comment>)
    }

    interface Presenter {
        fun loadInformation()
        fun loadComments(list: MutableList<Any>?)
        fun getWeatherForCity(id: Int)
    }
}