package com.example.orwma_proj_zad

import com.google.type.Date
import com.google.type.DateTime

data class Match(
    val id: String = "",
    val sport_title: String? = null,
    val commence_time: String? = null,
    val home_team: String? = null,
    val away_team: String? = null,
    val scores: ArrayList<Score>? = null
)

data class Score(
    val name: String? = null,
    val score: Int? = 0
)
