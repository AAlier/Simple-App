package neobis.o.ui.album

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.fragment_post.*
import neobis.o.R
import neobis.o.model.Album
import neobis.o.ui.album_photo.AlbumPhoto
import neobis.o.utils.Constant
import neobis.o.utils.FileLog

/**
 * Created by Alier on 18.07.2018.
 */
class FragmentAlbum : Fragment(), FragmentContract.View, Adapter.Listener {
    private var presenter: FragmentContract.Presenter? = null
    private var adapter: Adapter? = null
    private var list: List<Album> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_album, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        recyclerView.layoutManager = GridLayoutManager(context, 3)
        adapter = Adapter(ArrayList(), this)
        recyclerView.adapter = adapter

        presenter = FragmentPresenter(this)
        presenter?.loadAlbums()

        swipeRefresh.setOnRefreshListener {
            presenter?.loadAlbums()
        }
    }

    override fun onSuccess(result: List<Album>) {
        this.list = result
        adapter?.setAlbumList(result)
    }

    override fun onItemAtClicked(album: Album) {
        val intent = Intent(context, AlbumPhoto::class.java)
        intent.putExtra("album", album)
        startActivity(intent)
    }

    override fun onError(message: String?) {
        FileLog.showError(context, message)
    }

    override fun showProgress() {
        swipeRefresh?.isRefreshing = true
    }

    override fun hideProgress() {
        swipeRefresh?.isRefreshing = false
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.extract_elements, menu)
        menu?.getItem(1)?.isVisible = false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.nElement -> {
                adapter?.setAlbumList(Constant.selectTenElements(list))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}