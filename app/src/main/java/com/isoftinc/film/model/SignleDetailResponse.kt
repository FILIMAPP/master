package com.isoftinc.film.model

class SignleDetailResponse(
    val code: Int,
    val result : SingleDetailResult
)

class SingleDetailResult(
    val detail : DetailModel,
    val common_videos : ArrayList<CommonVideoModel>
)

class DetailModel(
    val name:String,
    val description :String,
    val director:String,
    val starring:String,
    val type:String,
    val _id:String,
    val video:ArrayList<VideoCategoryListModel>,
    val current_session : CurrentSessionModel?

)

class CommonVideoModel(
    val _id: String,
    val name:String,
    val description :String,
    val director:String,
    val starring:String,
    val type:String,
    val video:ArrayList<VideoCategoryModel>,
    val trailer_duration:String,
    val main_video_duration:String
)

class CurrentSessionModel(
    val _id: String,
    val   section:String,
    val on_time : String,
    val delete:Boolean,
    val user:String,
    val video : String

)









