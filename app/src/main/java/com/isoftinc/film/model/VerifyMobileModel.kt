package com.isoftinc.film.model

class VerifyMobileModel(
    val phone_number : String,
    val otp : String,
    val device : DeviceModel
)

class DeviceModel(
    val id:String,
    val type : String,
    val token:String

)

