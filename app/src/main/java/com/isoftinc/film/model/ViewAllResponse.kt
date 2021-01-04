package com.isoftinc.film.model

class ViewAllResponse(
    val code :Int,
    val result : ViewAllResult

)

class ViewAllResult(
    val list: ArrayList<VideoListModel> ,
    val movie_list : ArrayList<VideoListModel>

)

class VideoListModel(
    val name:String,
    val description:String,
    val director:String,
    val starring:String,
    val type:String,
    val thumbnail:String,
    val _id:String,
    val video:ArrayList<VideoCategoryListModel>

)
class VideoCategoryListModel(
    val poster:String,
    val thumbnail:String,
    val _id:String,
    val trailer:ArrayList<TrailerModel>,
    val main:ArrayList<TrailerModel>,
    val language:LanguageModel

)

class  LanguageModel(
    val name:String,
    val _id :String
)

