package com.example.communityeventssa.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class User(val id: Int? = null, val username: String, val email: String, val password: String)
data class Event(val id: Int? = null, val title: String, val description: String, val date: String, val location: String)
data class ApiResponse(val status: String, val message: String)

interface ApiService {
    @POST("register")
    fun registerUser(@Body user: User): Call<ApiResponse>

    @POST("login")
    fun loginUser(@Body credentials: Map<String, String>): Call<ApiResponse>

    @GET("events")
    fun getEvents(): Call<List<Event>>

    @POST("events")
    fun createEvent(@Body event: Event): Call<ApiResponse>
}