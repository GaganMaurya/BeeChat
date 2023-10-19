package com.example.cryptoo

import com.example.cryptoo.constants.Companion.contenttype
import com.example.cryptoo.constants.Companion.serverkey
import com.example.cryptoo.dataclass.pushNotification
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationApi {

    @Headers("Authorization: key=$serverkey","Content-Type:$contenttype")
    @POST("fcm/send")
    suspend fun postnotification(
        @Body notification : pushNotification
    ): Response<ResponseBody>
}