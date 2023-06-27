package com.example.covidmap.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CenterData")
data class CovidCenterData(
    val address: String,
    val centerName: String,
    val facilityName: String,
    val phoneNumber: String,
    val updatedAt: String,
//    val centerType: String,
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
