package neobis.o.ui.post_comments

import neobis.o.R
import neobis.o.StartApplication
import neobis.o.model.Comment
import neobis.o.utils.ConnectionsManager
import neobis.o.utils.FileLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Alier on 18.07.2018.
 */
class PostCommentPresenter(var view: PostCommentContract.View?) : PostCommentContract.Presenter {

    override fun loadComments(postId: Int?) {
        if(postId == null) {
            FileLog.e("Post id not found")
            return
        }
        if (!ConnectionsManager.isNetworkOnline()) {
            view?.onError(StartApplication.INSTANCE.getString(R.string.error_network))
            return
        }
        view?.showProgress()
        StartApplication.service.loadPostComments(postId).enqueue(object : Callback<List<Comment>> {
            override fun onResponse(call: Call<List<Comment>>?, response: Response<List<Comment>>?) {
                if (response!!.isSuccessful && response.body() != null) {
                    view?.onSuccess(response.body() ?: ArrayList())
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
}