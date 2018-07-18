package neobis.o.ui.album_photo

import neobis.o.R
import neobis.o.StartApplication
import neobis.o.model.Comment
import neobis.o.model.Photos
import neobis.o.utils.ConnectionsManager
import neobis.o.utils.FileLog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by Alier on 18.07.2018.
 */
class AlbumPhotoPresenter(var view: AlbumPhotoContract.View?): AlbumPhotoContract.Presenter {

    override fun loadPhotosOfAlbum(id: Int?) {
        if(id == null) {
            FileLog.e("Album id is not found")
            return
        }
        if (!ConnectionsManager.isNetworkOnline()) {
            view?.onError(StartApplication.INSTANCE.getString(R.string.error_network))
            return
        }
        view?.showProgress()
        StartApplication.service.loadAlbumPhotos(id).enqueue(object : Callback<List<Photos>> {
            override fun onResponse(call: Call<List<Photos>>?, response: Response<List<Photos>>?) {
                if (response!!.isSuccessful && response.body() != null) {
                    view?.onSuccess(response.body() ?: ArrayList())
                } else {
                    view?.onError(StartApplication.INSTANCE.getString(R.string.error_response))
                }
                view?.hideProgress()
            }

            override fun onFailure(call: Call<List<Photos>>?, t: Throwable?) {
                FileLog.e(t)
                view?.onError(StartApplication.INSTANCE.getString(R.string.error_response))
                view?.hideProgress()
            }
        })
    }
}
