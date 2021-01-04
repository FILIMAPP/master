package com.isoftinc.film.model

class SeriesModelListResponse (
    val code: Int,
    val result : SeriesListsModel
)

class SeriesListsModel(
    val series_list : ArrayList<SeriesListsModels>
)

class SeriesListsModels (
    val name : String,
    val description  : String,
    val director : String,
    val starring : String,
    val thumbnail : String,
    val trailer : String,
    val _id : String,
    val season : ArrayList<SessionModelss>
)

class SessionModelss(
    val _id:String,

)


