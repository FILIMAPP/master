package com.isoftinc.film.model

class SeriesModelResponse (
    val code: Int,
    val result : SeriesListModel
)

class SeriesListModel(
    val series_list : ArrayList<SeriesListModels>
)

class SeriesListModels (
    val name : String,
    val description  : String,
    val director : String,
    val starring : String,
    val thumbnail : String,
    val trailer : String,
    val _id : String,
    val cover : String,
    val season : ArrayList<SessionModel>
)

class SessionModel(
    val _id:String,
    val episodes :  ArrayList<VideoModel>
)


