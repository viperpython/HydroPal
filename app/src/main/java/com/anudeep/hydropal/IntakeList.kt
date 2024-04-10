package com.anudeep.hydropal

import androidx.room.Room
import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime

object IntakeList {
    private lateinit var db: AppDatabase
    var waterIntakeList = mutableListOf<WaterIntake>()

    fun initializeDatabase(application: Application) {
        db = Room.databaseBuilder(
            application,
            AppDatabase::class.java, "WaterIntake"
        ).build()
    }

    fun loadWaterIntakeList(): MutableList<WaterIntake> {
        waterIntakeList = db.waterIntakeDao().getAll().toMutableList()
        return waterIntakeList
    }

    fun saveWaterIntake(waterIntake: WaterIntake) {
        db.waterIntakeDao().insertAll(waterIntake)
        waterIntakeList.add(waterIntake)
    }
    fun deleteWaterIntake() {
        db.waterIntakeDao().delete()
        loadWaterIntakeList()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun addWaterIntake(amount: Int) {
        val waterIntake = WaterIntake(amount = amount, timestamp = LocalDateTime.now().toEpochSecond(java.time.ZoneOffset.UTC))
        saveWaterIntake(waterIntake)
    }
}