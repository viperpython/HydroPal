package com.anudeep.hydropal

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WaterIntakeDao {
    @Query("SELECT * FROM WaterIntake")
    fun getAll(): List<WaterIntake>

    @Insert
    fun insertAll(vararg waterIntakes: WaterIntake)
    @Query("DELETE FROM WaterIntake WHERE Timestamp = (select max(Timestamp) from WaterIntake)")
    fun delete()
}