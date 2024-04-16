package com.anudeep.hydropal

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity
data class WaterIntake(
    @PrimaryKey(autoGenerate = true)
    val id : Int=0,
    val amount: Int,
    val timestamp: Long
)
