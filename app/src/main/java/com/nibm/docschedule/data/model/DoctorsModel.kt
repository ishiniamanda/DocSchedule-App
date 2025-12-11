package com.nibm.docschedule.data.model

import android.os.Parcel
import android.os.Parcelable

data class DoctorsModel(
    val Address: String = "",
    val Biography: String = "",
    val Id: Int = 0,
    val Name: String = "",
    val Picture: String = "",
    val Special: String = "",
    val Expriense: Int = 0,
    val Location: String = "",
    val Mobile: String = "",
    val Patiens: String = "",
    val Rating: Double = 0.0,
    val Site: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readInt(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readInt(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readDouble(),
        parcel.readString().orEmpty()
    )



    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(Address)
        dest.writeString(Biography)
        dest.writeInt(Id)
        dest.writeString(Name)
        dest.writeString(Picture)
        dest.writeString(Special)
        dest.writeInt(Expriense)
        dest.writeString(Location)
        dest.writeString(Mobile)
        dest.writeString(Patiens)
        dest.writeDouble(Rating)
        dest.writeString(Site)
    }

    override fun describeContents(): Int {
        return 0
    }
    companion object CREATOR : Parcelable.Creator<DoctorsModel> {
        override fun createFromParcel(parcel: Parcel): DoctorsModel {
            return DoctorsModel(parcel)
        }

        override fun newArray(size: Int): Array<DoctorsModel?> {
            return arrayOfNulls(size)
        }
    }
}