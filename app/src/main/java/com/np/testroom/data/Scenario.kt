package com.np.testroom.data

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
    val term: Int,
    @ColumnInfo(name = "extra_monthly_payment") val extraMonthlyPayment: Double? = null,
    @ColumnInfo(name = "user_id") val userId: Long,
    val category: ScenarioCategory? = null,
    @ColumnInfo(name = "start_date") val startDate: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at") val updatedAt: Long = System.currentTimeMillis()
)
