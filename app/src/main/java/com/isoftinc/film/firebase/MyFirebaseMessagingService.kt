package com.isoftinc.film.firebase


import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.isoftinc.film.R
import com.isoftinc.film.activity.MainActivity
import com.isoftinc.film.util.PreferenceStore
import org.json.JSONObject
import java.net.URL


class MyFirebaseMessagingService : FirebaseMessagingService() {
    var message = ""
  //  val androidUtilities = AndroidUtilities()
  var preferenceStore: PreferenceStore? = null
    override fun onNewToken(mToken: String) {
        super.onNewToken(mToken)
        Log.e("TOKEN", mToken)
       preferenceStore = PreferenceStore(applicationContext)
        preferenceStore!!.setToken(mToken)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) { var json:JSONObject? = null
// Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Log.d("Notification++++++: " ,remoteMessage.notification!!.body)
            message = remoteMessage.notification!!.body!!

        }

        // Check if message contains a data payload.6
        if (remoteMessage.data.isNotEmpty()) {
            Log.d( "Notification+++++++: " , remoteMessage.data.toString())

            try {
                 json =  JSONObject(remoteMessage.data.toString())
                Log.d( "json: " , json.toString())
            } catch (e:Exception ) {
                Log.e(TAG, "Exception: " + e.message)
            }
        }




        if(json != null) {
            handleNotification(json.toString())
        }

    }


    @TargetApi(26)
    private fun createChannel(
        notificationManager: NotificationManager,
        title: String,
        desc: String
    ) {
        val importance = NotificationManager.IMPORTANCE_HIGH

        val mChannel = NotificationChannel(title, title, importance)
        mChannel.description = desc
        mChannel.enableLights(true)
        mChannel.setShowBadge(true)
        notificationManager.createNotificationChannel(mChannel)
    }


    private fun handleNotification( body: String) {

        try {

            val jsonObject = JSONObject(body)
            val title = getString(R.string.app_name)
            var message = "Check out Cool Movies"
            var data:JSONObject? = null
            var info : JSONObject? = null
            var image = ""
            if(jsonObject.has("info")){
                info = jsonObject.getJSONObject("info")
            }


            if(info!!.has("body"))
                message = info!!.getString("body")

            if(info!!.has("image"))
                image = info!!.getString("image")

            if(image.isNullOrEmpty()){
                val notifyIntent = Intent(this, MainActivity::class.java)
                notifyIntent.flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                notifyIntent.putExtra("pushNotification", body)
                val notifyPendingIntent = PendingIntent.getActivity(
                    this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
                )
                val mNotifyManager =
                    this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createChannel(mNotifyManager, title, body)
                    // .setSmallIcon(R.mipmap.ic_launcher)
                    val mBuilder = NotificationCompat.Builder(this, title)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(
                            NotificationManager.IMPORTANCE_HIGH
                        )
                        .setContentIntent(notifyPendingIntent)
                        .setAutoCancel(true)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBuilder.setSmallIcon(R.drawable.noti_icon)
                        mBuilder.color = resources.getColor(R.color.yellow)
                    } else {
                        mBuilder.setSmallIcon(R.drawable.ic_launcher)
                    }
                    mNotifyManager.notify(0, mBuilder.build())
                } else {

                    val mBuilder = NotificationCompat.Builder(this,title)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setPriority(NotificationManager.IMPORTANCE_HIGH)
                        .setContentIntent(notifyPendingIntent)
                        .setAutoCancel(true)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        mBuilder.setSmallIcon(R.drawable.noti_icon)
                        mBuilder.color = resources.getColor(R.color.yellow)
                    } else {
                        mBuilder.setSmallIcon(R.drawable.ic_launcher)
                    }
                    mNotifyManager.notify(0, mBuilder.build())
                }
            }else{
                MyDownloader(image, this, title,message,body).execute()
            }



        }catch (ex:Exception){

        }




    }



    class MyDownloader(val imageUrl : String, val context: Context?, val title: String, val message:String, val body: String) : AsyncTask<String?, Void?, Bitmap?>() {
        override fun onPreExecute() {
            // Show progress dialog
            super.onPreExecute()
        }

        override fun onPostExecute(bitmap: Bitmap?) {
            //Populate Ui
            super.onPostExecute(bitmap)
            val notifyIntent = Intent(context, MainActivity::class.java)
            notifyIntent.flags = FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            val notifyPendingIntent = PendingIntent.getActivity(
                context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT
            )
            val mNotifyManager =
                context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Assign big picture notification
            // Assign big picture notification
            val bpStyle = NotificationCompat.BigPictureStyle()
            bpStyle.bigPicture(bitmap)
                .build()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               MyFirebaseMessagingService().createChannel(mNotifyManager, title, body)
                // .setSmallIcon(R.mipmap.ic_launcher)
                val mBuilder = NotificationCompat.Builder(context, title)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(
                        NotificationManager.IMPORTANCE_HIGH
                    )
                    .setContentIntent(notifyPendingIntent)
                    .setStyle(bpStyle)
                    .setAutoCancel(true)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                    mBuilder.color = context.resources.getColor(R.color.yellow)
                } else {
                    mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                }
                mNotifyManager.notify(0, mBuilder.build())
            } else {

                val mBuilder = NotificationCompat.Builder(context,title)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setContentIntent(notifyPendingIntent)
                    .setAutoCancel(true)
                    .setStyle(bpStyle)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                    mBuilder.color = context.resources.getColor(R.color.yellow)
                } else {
                    mBuilder.setSmallIcon(R.mipmap.ic_launcher)
                }
                mNotifyManager.notify(0, mBuilder.build())
            }


        }


        override fun doInBackground(vararg params: String?): Bitmap? {
            val url = URL(imageUrl)
            val image =
                BitmapFactory.decodeStream(url.openConnection().getInputStream())
            return  image
        }
    }







}
