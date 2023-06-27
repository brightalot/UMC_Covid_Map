package com.example.covidmap.Data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CovidCenterDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCovidCenter(covidCenterData: CovidCenterData)

    @Query("DELETE FROM CenterData")
    suspend fun deleteCovidCenter()
}