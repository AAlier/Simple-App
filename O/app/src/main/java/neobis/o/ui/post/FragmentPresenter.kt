package neobis.o.ui.post

import neobis.o.R
import neobis.o.StartApplication
import neobis.o.model.Comment
import neobis.o.model.Post
import neobis.o.model.WeatherInfo
import neobis.o.utils.ConnectionsManager
import neobis.o.utils.FileLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Alier on 18.07.2018.
 */
class FragmentPresenter(val view: FragmentContract.View?) : FragmentContract.Presenter {
    override fun loadInformation() {
        if (!ConnectionsManager.isNetworkOnline()) {
            view?.onError(StartApplication.INSTANCE.getString(R.string.error_network))
            return
        }
        view?.showProgress()
        StartApplication.service.loadPosts().enqueue(object : Callback<MutableList<Post>> {
            override fun onResponse(call: Call<MutableList<Post>>?, response: Response<MutableList<Post>>?) {
                if (response!!.isSuccessful && response.body() != null) {
                    val list = response.body() ?: ArrayList()
                    view?.onSuccess(list)
                } else {
                    view?.onError(StartApplication.INSTANCE.getString(R.string.error_response))
                }
                view?.hideProgress()
            }

            override fun onFailure(call: Call<MutableList<Post>>?, t: Throwable?) {
                FileLog.e(t)
                view?.onError(StartApplication.INSTANCE.getString(R.string.error_response))
                view?.hideProgress()
            }
        })
    }

    override fun loadComments(list: MutableList<Any>?) {
        if (!ConnectionsManager.isNetworkOnline()) {
            view?.onError(StartApplication.INSTANCE.getString(R.string.error_network))
            return
        }
        view?.showProgress()
        StartApplication.service.loadComments().enqueue(object : Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>?, response: Response<List<Comment>>?) {
                if (response!!.isSuccessful && response.body() != null) {
                    selectPostComments(list, response.body()!!)
                } else {
                    view?.onError(StartApplication.INSTANCE.getString(R.string.error_response))
                }
                view?.hideProgress()
            }

            override fun onFailure(call: Call<List<Comment>>?, t: Throwable?) {
                FileLog.e(t)
                view?.onError(StartApplication.INSTANCE.getString(R.string.error_response))
                view?.hideProgress()
            }
        })
    }

    private fun selectPostComments(list: MutableList<Any>?, listOfComments: List<Comment>) {
        if(list == null)
            return
        val commentlist = ArrayList<Comment>()
        for (comment in listOfComments) {
            for (post in list) {
                if (post is WeatherInfo)
                    continue
                else {
                    post as Post
                    if (post.id == comment.postId)
                        commentlist.add(comment)
                }
            }
        }
        view?.onSelectComments(commentlist)
    }

    override fun getWeatherForCity(id: Int) {
        StartApplication.weatherService.getWeatherForCity(id, "6c4073d27817692d5ee59160e39a468f")
                .enqueue(object : Callback<WeatherInfo> {
                    override fun onResponse(call: Call<WeatherInfo>?, response: Response<WeatherInfo>?) {
                        if (response!!.isSuccessful && response.body() != null) {
                            view?.onWeatherResponse(response.body()!!)
                        } else {
                            FileLog.e("Error Loading weather info")
                            // view?.onError(StartApplication.INSTANCE.getString(R.string.error_weather))
                        }
                    }

                    override fun onFailure(call: Call<WeatherInfo>?, t: Throwable?) {
                        FileLog.e(t)
                        // view?.onError(StartApplication.INSTANCE.getString(R.string.error_weather))
                    }

                })
    }
}