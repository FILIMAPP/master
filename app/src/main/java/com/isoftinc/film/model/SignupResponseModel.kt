package com.isoftinc.film.model

class SignupResponseModel(
   val  code: Int,
   val result: SignupResultModel
)

class SignupResultModel(
    val first_name:String,
    val last_name :String,
    val username:String,
    val stripe_customer_id:String,
    val email:String,
    val password:String,
    val gender:String,
    val profile_img:String,
    val type:String,
    val phone_number:String,
    val otp:String,
    val is_email_verify:String,
    val active : Boolean,
    val delete:Boolean,
    val send_notification_status:Boolean,
    val send_email_status:Boolean,
    val social_id:String,
    val login_type:String,
    val _id:String
)







