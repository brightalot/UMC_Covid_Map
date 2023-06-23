package com.example.covidmap

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CovidCenterData")
data class CovidCenterData(
    val address: String,
    val centerName: String,
    val facilityName: String,
    val phoneNumber: String,
    val updatedAt: String,
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
