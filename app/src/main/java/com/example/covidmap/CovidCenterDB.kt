package com.example.covidmap

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CovidCenterData::class], version = 1, exportSchema = false)
abstract class CovidCenterDB: RoomDatabase() {
    abstract fun covidCenterDAO(): CovidCenterDAO
}