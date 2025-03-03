package com.example.state.notes.data.datasource

import com.example.state.notes.data.model.CreateNoteRequest
import com.example.state.notes.data.model.CreateNoteResponse
import com.example.state.notes.data.model.NoteDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface NoteService {

    @GET("api/notes")
    suspend fun getNotes(): Response<List<NoteDTO>>

    @POST("api/notes/")
    suspend fun createNote(@Body request: CreateNoteRequest): Response<CreateNoteResponse>

}
