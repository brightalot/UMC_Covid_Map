package com.example.covidmap.Data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CovidCenterData::class], version = 1)
abstract class CovidCenterDB: RoomDatabase() {
    abstract fun covidCenterDAO(): CovidCenterDAO
}