package neobis.o.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Alier on 18.07.2018.
 */
data class Photos(var albumId: Int,
                  var id: Int,
                  var title: String? = null,
                  var url: String? = null,
                  var thumbnailUrl: String? = null): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString(),
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(albumId)
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(url)
        parcel.writeString(thumbnailUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Photos> {
        override fun createFromParcel(parcel: Parcel): Photos {
            return Photos(parcel)
        }

        override fun newArray(size: Int): Array<Photos?> {
            return arrayOfNulls(size)
        }
    }
}