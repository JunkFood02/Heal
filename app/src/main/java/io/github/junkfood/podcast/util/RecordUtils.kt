package io.github.junkfood.podcast.util

import android.os.Parcel
import android.os.Parcelable

class RecordUtils( ) : Parcelable{
    constructor(parcel: Parcel) : this() {
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        TODO("Not yet implemented")
    }

    companion object CREATOR : Parcelable.Creator<RecordUtils> {
        override fun createFromParcel(parcel: Parcel): RecordUtils {
            return RecordUtils(parcel)
        }

        override fun newArray(size: Int): Array<RecordUtils?> {
            return arrayOfNulls(size)
        }
    }

}