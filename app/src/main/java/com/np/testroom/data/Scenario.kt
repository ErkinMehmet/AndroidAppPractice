package com.np.testroom.data
import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.np.testroom.enums.ScenarioCategory
@Entity(
    tableName = "scenarios",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE // When a user is deleted, the associated scenarios will also be deleted
        )
    ]
)
data class Scenario(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val loan: Double,
    @ColumnInfo(name = "interest_rate") val interestRate: Double,
    val period: Int, // For example: number of months
    val term: String,
    @ColumnInfo(name = "extra_monthly_payment") val extraMonthlyPayment: Double? = null,
    @ColumnInfo(name = "user_id") val userId: Long,
    val category: ScenarioCategory? = null,
    @ColumnInfo(name = "start_date") val startDate: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis()
): Parcelable {

    constructor(parcel: Parcel) : this(
        id = parcel.readLong(),
        title = parcel.readString() ?: "",
        loan = parcel.readDouble(),
        interestRate = parcel.readDouble(),
        period = parcel.readInt(),
        term = parcel.readString() ?: "",
        extraMonthlyPayment = parcel.readValue(Double::class.java.classLoader) as? Double,
        userId = parcel.readLong(),
        category = parcel.readParcelable(ScenarioCategory::class.java.classLoader),
        startDate = parcel.readLong(),
        createdAt = parcel.readLong(),
        updatedAt = parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(title)
        parcel.writeDouble(loan)
        parcel.writeDouble(interestRate)
        parcel.writeInt(period)
        parcel.writeString(term)
        parcel.writeValue(extraMonthlyPayment)
        parcel.writeLong(userId)
        parcel.writeParcelable(category, flags)
        parcel.writeLong(startDate)
        parcel.writeLong(createdAt)
        parcel.writeLong(updatedAt)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Scenario> {
        override fun createFromParcel(parcel: Parcel): Scenario {
            return Scenario(parcel)
        }

        override fun newArray(size: Int): Array<Scenario?> {
            return arrayOfNulls(size)
        }
    }
}
