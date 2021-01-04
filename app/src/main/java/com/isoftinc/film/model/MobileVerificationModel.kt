package com.isoftinc.film.model

class MobileVerificationModel(
    var code:Int,
    var message :String,
    var result : MobileVerificationResult

)

class MobileVerificationResult(
    val rzp_customer_id:String,
   val phone_number : String,
    val _id:String,
    val subscription : Any?,
    val username : String,
    val profile_img:String,
    val device : ArrayList<DeviceModel>
)

class SubscriptionModel(
    val end_date : String,
    val _id : String,
   val  user : String,
  val   plan : PlanModel?,
   val payment : String,

)
 class  PlanModel(
     val name : String,
     val price : String,
     val month : String,
     val one_month_price : String,
     val _id :String
 )



/*
{
    "code": 200,
    "message": "success",
    "result": {
    "username": "",
    "rzp_customer_id": "cust_FnAJ3QcrRGq5fN",
    "email": "",
    "password": "",
    "profile_img": "",
    "type": "U",
    "phone_number": "8447329391",
    "otp": "",
    "is_email_verify": false,
    "is_phone_number_verify": true,
    "active": true,
    "delete": false,
    "send_notification_status": true,
    "send_email_status": true,
    "social_id": "",
    "login_type": "N",
    "_id": "5f818c9ce3575b3587ba4f89",
    "createdAt": "2020-10-10T10:27:40.899Z",
    "updatedAt": "2020-10-10T10:29:21.973Z",
    "__v": 0
},
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyIjp7InVzZXJuYW1lIjoiIiwicnpwX2N1c3RvbWVyX2lkIjoiY3VzdF9GbkFKM1FjclJHcTVmTiIsImVtYWlsIjoiIiwicGFzc3dvcmQiOiIiLCJwcm9maWxlX2ltZyI6IiIsInR5cGUiOiJVIiwicGhvbmVfbnVtYmVyIjoiODQ0NzMyOTM5MSIsIm90cCI6IiIsImlzX2VtYWlsX3ZlcmlmeSI6ZmFsc2UsImlzX3Bob25lX251bWJlcl92ZXJpZnkiOnRydWUsImFjdGl2ZSI6dHJ1ZSwiZGVsZXRlIjpmYWxzZSwic2VuZF9ub3RpZmljYXRpb25fc3RhdHVzIjp0cnVlLCJzZW5kX2VtYWlsX3N0YXR1cyI6dHJ1ZSwic29jaWFsX2lkIjoiIiwibG9naW5fdHlwZSI6Ik4iLCJfaWQiOiI1ZjgxOGM5Y2UzNTc1YjM1ODdiYTRmODkiLCJjcmVhdGVkQXQiOiIyMDIwLTEwLTEwVDEwOjI3OjQwLjg5OVoiLCJ1cGRhdGVkQXQiOiIyMDIwLTEwLTEwVDEwOjI5OjIxLjk3M1oiLCJfX3YiOjB9LCJpYXQiOjE2MDIzMjU3NjF9.NbMU4NXcJ6IQXm_UfR8TFxot6WIa3bbTdtuox-SchuY"
}*/
