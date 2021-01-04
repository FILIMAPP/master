package com.isoftinc.film.model

class UpdateSessionRequest(
   val user_id : String,
   val video_id : String,
   val on_time : String,
   val language_id : String,
   val parent_id : String
)

