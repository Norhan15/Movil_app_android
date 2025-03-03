package com.example.state.notes.data.repository

import com.example.state.core.network.RetrofitHelper
import com.example.state.notes.data.model.CreateNoteRequest
import com.example.state.notes.data.model.CreateNoteResponse
import com.example.state.notes.data.model.NoteDTO

class NoteRepository {
    private val noteService = RetrofitHelper.createService(com.example.state.notes.data.datasource.NoteService::class.java)

    suspend fun getNotes(): Result<List<NoteDTO>> {
        return try {
            val response = noteService.getNotes()
            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createNote(request: CreateNoteRequest): Result<CreateNoteResponse> {
        return try {
            val response = noteService.createNote(request)
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
