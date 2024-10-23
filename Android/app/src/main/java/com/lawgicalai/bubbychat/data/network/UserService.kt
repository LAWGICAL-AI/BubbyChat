package com.lawgicalai.bubbychat.data.network

import android.util.Log
import com.lawgicalai.bubbychat.data.model.CommonResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

private const val TAG = "UserService"

class UserService @Inject constructor(
    private val client: HttpClient,
) {
    suspend fun login(requestBody: String): CommonResponse<String> {
        Log.d(TAG, "login: $requestBody")
        return client.post("users/login") {
            setBody(requestBody)
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun signup(requestBody: String): CommonResponse<Long> {
        return client.post("users/sign-up") {
            setBody(requestBody)
            contentType(ContentType.Application.Json)
        }.body()
    }

    suspend fun getUserInfo(): CommonResponse<String> {
        return client.get("users/my-page").body()
    }

    suspend fun getTest() {
        client.get("https://www.thecocktaildb.com/api/json/v1/1/filter.php?c=Ordinary_Drink")
    }
}