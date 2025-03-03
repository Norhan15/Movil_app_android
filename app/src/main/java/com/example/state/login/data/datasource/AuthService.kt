package com.example.state.login.data.datasource

import com.example.state.login.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("api/login/")
    suspend fun login(@Body credentials: Map<String, String>): Response<LoginResponse>
}
