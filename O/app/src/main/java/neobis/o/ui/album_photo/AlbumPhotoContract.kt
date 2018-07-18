package neobis.o.ui.album_photo

import neobis.alier.bilimkana.util.IProgressBar
import neobis.alier.bilimkana.util.IResult
import neobis.o.model.Photos

/**
 * Created by Alier on 18.07.2018.
 */
interface AlbumPhotoContract {
    interface View : IProgressBar, IResult<List<Photos>>

    interface Presenter{
        fun loadPhotosOfAlbum(id: Int?)
    }
}