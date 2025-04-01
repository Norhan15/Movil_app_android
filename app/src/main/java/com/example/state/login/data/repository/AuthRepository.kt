package com.example.state.login.data.repository

import com.example.state.core.network.RetrofitHelper
import com.example.state.login.data.datasource.AuthService

class AuthRepository {
    private val authService = RetrofitHelper.createService(AuthService::class.java)

    suspend fun login(username: String, password: String): Result<Pair<String, Int>> {
        return try {
            val response = authService.login(
                mapOf(
                    "username" to username,
                    "password" to password
                )
            )
            if (response.isSuccessful) {
                val loginBody = response.body()
                if (loginBody != null && loginBody.success) {
                    // Return both token and userId
                    val userId = loginBody.userId ?: -1
                    Result.success(Pair(loginBody.message, userId))
                } else {
                    Result.failure(Exception(loginBody?.message ?: "Error desconocido"))
                }
            } else {
                Result.failure(Exception("Error en la autenticación"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
