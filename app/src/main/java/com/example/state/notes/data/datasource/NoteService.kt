package com.example.state.notes.data.datasource

import com.example.state.notes.data.model.CreateNoteRequest
import com.example.state.notes.data.model.CreateNoteResponse
import com.example.state.notes.data.model.NoteDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NoteService {

    @GET("api/notes/{user_id}")
    suspend fun getNotesByUserId(@Path("user_id") userId: Int): Response<List<NoteDTO>>

    @POST("api/notes/{user_id}")
    suspend fun createNote(
        @Path("user_id") userId: Int,
        @Body request: CreateNoteRequest
    ): Response<CreateNoteResponse>

}