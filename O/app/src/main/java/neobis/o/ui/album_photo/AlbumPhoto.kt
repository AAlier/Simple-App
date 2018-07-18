package neobis.o.ui.album_photo

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_album_photo.*
import neobis.alier.bilimkana.ui.BaseActivity
import neobis.o.R
import neobis.o.model.Album
import neobis.o.model.Photos

/**
 * Created by Alier on 18.07.2018.
 */
class AlbumPhoto : BaseActivity(), AlbumPhotoContract.View, PhotoAdapter.Listener {
    private var presenter: AlbumPhotoContract.Presenter? = null
    private var adapter: PhotoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album_photo)
        val album = intent.getParcelableExtra<Album>("album") ?: return
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        adapter = PhotoAdapter(ArrayList(), this)
        recyclerView.adapter = adapter

        presenter = AlbumPhotoPresenter(this)
        presenter?.loadPhotosOfAlbum(album.id)

        swipeRefresh.setOnRefreshListener {
            presenter?.loadPhotosOfAlbum(album.id)
        }
    }

    override fun onSuccess(result: List<Photos>) {
        adapter?.setAlbumPhotoList(result)
    }

    override fun onItemClickedAt(photo: Photos) {
        val intent = Intent(this, FullScreenPhoto::class.java)
        intent.putExtra("url", photo.url)
        startActivity(intent)
    }

    override fun showProgress() {
        super.showProgress()
        swipeRefresh.isRefreshing = true
    }

    override fun hideProgress() {
        super.hideProgress()
        swipeRefresh.isRefreshing = false
    }
}