package com.example.covidmap

data class CovidCentersInfo(
    val totalCount: Int,
    val data: ArrayList<CovidCenterData>
)