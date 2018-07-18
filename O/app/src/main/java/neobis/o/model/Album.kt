package neobis.o.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Alier on 18.07.2018.
 */
data class Album(var userId: Int,
                 var id: Int,
                 var title: String? = null): Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel?, flags: Int) {
        parcel?.writeInt(userId)
        parcel?.writeInt(id)
        parcel?.writeString(title)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Album> {
        override fun createFromParcel(parcel: Parcel): Album {
            return Album(parcel)
        }

        override fun newArray(size: Int): Array<Album?> {
            return arrayOfNulls(size)
        }
    }
}