package com.example.state.notes.data.repository


import com.example.state.core.network.RetrofitHelper
import com.example.state.notes.data.model.CreateNoteRequest
import com.example.state.notes.data.model.CreateNoteResponse
import com.example.state.notes.data.model.NoteDTO

class NoteRepository {
    private val noteService = RetrofitHelper.createService(com.example.state.notes.data.datasource.NoteService::class.java)

    suspend fun getNotesByUserId(userId: Int): Result<List<NoteDTO>> {
        return try {
            val response = noteService.getNotesByUserId(userId)
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createNote(title: String, content: String, userId: Int): Result<CreateNoteResponse> {
        // Validación local
        if (userId <= 0) {
            return Result.failure(IllegalArgumentException("Invalid user ID"))
        }

        return try {
            val response = noteService.createNote(
                userId,
                CreateNoteRequest(title, content)
            )

            when {
                response.isSuccessful -> Result.success(response.body()!!)
                response.code() == 404 -> Result.failure(Exception("User not found"))
                response.code() == 400 -> Result.failure(Exception("Invalid user ID"))
                else -> {
                    val errorMsg = response.errorBody()?.string() ?: "Unknown error"
                    Result.failure(Exception("Server error: $errorMsg"))
                }
            }
        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}"))
        }
    }

}