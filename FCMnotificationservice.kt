package com.example.cryptoo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_HIGH
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_ONE_SHOT
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random
private const val channelId = "my channel"
class FCMnotificationservice : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val model = modelclass()

        val intent = Intent(this , chatactivity::class.java)
        usermodelasintent(intent ,model)

        val notifiactionMa = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notifiId = Random.nextInt()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createnotificationchannel(notifiactionMa)
        }


        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pend = PendingIntent.getActivity(
            this,
            0,
            intent,
            FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )
       val notication = NotificationCompat.Builder(this , channelId )
           .setContentTitle(message.data["title"])
           .setContentText(message.data["message"])
           .setSmallIcon(R.drawable.applogo)
           .setAutoCancel(true)
           .setContentIntent(pend)
           .build()

        notifiactionMa.notify(notifiId , notication)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createnotificationchannel(nm :  NotificationManager) {
        val cname = "channelName"
        val channel = NotificationChannel(channelId , cname , IMPORTANCE_HIGH).apply {
            description = "My channel description"
            enableLights(true)
            lightColor =  Color.GREEN
        }
        nm.createNotificationChannel(channel)
    }

    private fun usermodelasintent(intent: Intent, m: modelclass?) {
          intent.putExtra("name" , m?.name.toString())
          intent.putExtra("phone" , m?.number.toString())
          intent.putExtra("uid" , m?.uid.toString())
    }
}