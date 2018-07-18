package neobis.o.ui.album

import neobis.alier.bilimkana.util.IProgressBar
import neobis.alier.bilimkana.util.IResult
import neobis.o.model.Album

/**
 * Created by Alier on 18.07.2018.
 */
interface FragmentContract {

    interface View : IProgressBar, IResult<List<Album>>

    interface Presenter {
        fun loadAlbums()
    }
}