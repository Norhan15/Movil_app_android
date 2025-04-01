package com.example.state.notes.domain

import com.example.state.notes.data.model.NoteDTO
import com.example.state.notes.data.repository.NoteRepository

class GetNotesUseCase {
    private val repository = NoteRepository()
    suspend operator fun invoke(userId: Int): Result<List<NoteDTO>> = repository.getNotesByUserId(userId)
}