package com.isoftinc.film.model

class HomeModel(
    val code:Int,
    val result : HomeResultModel,
    val message : String

)

class HomeResultModel(
    val banner : ArrayList<BannerModel>,
     val sub_category_video:ArrayList<SubCategoryVideoModel>,
    val continue_watch : ArrayList<VideoModel>?

)

class BannerModel(
    val url : String,
    val _id:String,
    val type:String,
    val video:String,
    val category:String,
    val sub_category:String,
    val series_show : String
)



class SubCategoryVideoModel(
    val _id:String,
    val name:String,
    val video:ArrayList<VideoModel>,
    val on_time : String
)

class VideoModel(
    val name:String,
    val description:String,
    val director:String,
    val starring:String,
    val type:String,
    val thumbnail:String,
    val _id:String,
    val video:ArrayList<VideoCategoryModel>?,
    val episode_id:String

)

class VideoCategoryModel(
    val poster:String,
    val thumbnail:String,
    val _id:String,
    val trailer:ArrayList<TrailerModel>,
    val main:ArrayList<TrailerModel>

)

class TrailerModel(
     val type:String,
     val url:String,
     val _id:String,
     var isSelected:Boolean = false
)





