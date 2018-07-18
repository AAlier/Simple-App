package neobis.o.ui.album

import neobis.o.R
import neobis.o.StartApplication
import neobis.o.model.Album
import neobis.o.utils.ConnectionsManager
import neobis.o.utils.Constant
import neobis.o.utils.FileLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * Created by Alier on 18.07.2018.
 */
class FragmentPresenter(var view: FragmentContract.View?) : FragmentContract.Presenter {

    override fun loadAlbums() {
        if (!ConnectionsManager.isNetworkOnline()) {
            view?.onError(StartApplication.INSTANCE.getString(R.string.error_network))
            return
        }
        view?.showProgress()
        StartApplication.service.loadAlbums().enqueue(object : Callback<List<Album>> {
            override fun onResponse(call: Call<List<Album>>?, response: Response<List<Album>>?) {
                if (response!!.isSuccessful && response.body() != null) {
                    val list = response.body() ?: ArrayList()
                    view?.onSuccess(list)
                } else {
                    view?.onError(StartApplication.INSTANCE.getString(R.string.error_response))
                }
                view?.hideProgress()
            }

            override fun onFailure(call: Call<List<Album>>?, t: Throwable?) {
                FileLog.e(t)
                view?.onError(StartApplication.INSTANCE.getString(R.string.error_response))
                view?.hideProgress()
            }
        })
    }
}