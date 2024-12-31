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
    val term: String, // Loan term description (e.g., "30 years")
    @ColumnInfo(name = "extra_monthly_payment") val extraMonthlyPayment: Double? = null,
    @ColumnInfo(name = "user_id") val userId: Long,
    val category: ScenarioCategory? = null // Optional category for the loan scenario
)
