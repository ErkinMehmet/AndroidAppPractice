package com.np.testroom.enums
import android.os.Parcel
import android.os.Parcelable

enum class ScenarioCategory : Parcelable {
    AUTO,
    PROP;

    // Writing the enum instance to a Parcel
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(ordinal)  // Write the ordinal value of the enum instance (AUTO = 0, PROP = 1)
    }

    // Describes the contents (not necessary for this case, so return 0)
    override fun describeContents(): Int = 0

    // Companion object to implement Parcelable.Creator for ScenarioCategory
    companion object CREATOR : Parcelable.Creator<ScenarioCategory> {
        override fun createFromParcel(parcel: Parcel): ScenarioCategory {
            // Read the ordinal value and convert it back to the corresponding enum
            return values()[parcel.readInt()]
        }

        override fun newArray(size: Int): Array<ScenarioCategory?> {
            return arrayOfNulls(size)
        }
    }
}