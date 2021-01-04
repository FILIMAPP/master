package com.isoftinc.film.model

class SessionDetailResponse (
  val code : Int,
  val result : ArrayList<SessionDetail>
)

class  SessionDetail(
    val _id : String,
    val episode: EpisodeModel,
    val current_session : CurrentSession?
)

class EpisodeModel(
    val name : String,
    val description : String,
    val director : String,
    val starring : String,
    val trailer_duration : String,
    val main_video_duration : String,
    val type : String,
    val _id : String,
    val video : ArrayList<VideoCategoryListModel>
)

class CurrentSession(
    val parent_id : String,
    val section : String,
    val on_time : String,
    val _id : String,
    val video: String

)



