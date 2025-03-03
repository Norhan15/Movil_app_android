package com.example.state.notes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.state.core.hardware.TextToSpeach
import com.example.state.notes.data.model.CreateNoteRequest
import com.example.state.notes.data.model.NoteDTO
import com.example.state.notes.data.repository.NoteRepository
import kotlinx.coroutines.launch

class NoteViewModel(private val tts: TextToSpeach) : ViewModel() {

    private val repository = NoteRepository()

    private val _notes = MutableLiveData<List<NoteDTO>>(emptyList())
    val notes: LiveData<List<NoteDTO>> = _notes

    private val _error = MutableLiveData<String?>(null)
    val error: LiveData<String?> = _error

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showDialog = MutableLiveData<Boolean>(false)
    val showDialog: LiveData<Boolean> = _showDialog

    private val _title = MutableLiveData<String>("")
    val title: LiveData<String> = _title

    private val _content = MutableLiveData<String>("")
    val content: LiveData<String> = _content

    private val _navigationCommand = MutableLiveData<String?>(null)
    val navigationCommand: LiveData<String?> = _navigationCommand

    init {
        getAllNotes()
    }

    fun getAllNotes() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getNotes()
            result.onSuccess { noteList ->
                _notes.value = noteList
                _error.value = null
            }.onFailure { exception ->
                _notes.value = emptyList()
                _error.value = exception.message
            }
            _isLoading.value = false
        }
    }

    fun createNote() {
        val titleValue = title.value.orEmpty()
        val contentValue = content.value.orEmpty()

        if (titleValue.isBlank() || contentValue.isBlank()) {
            _error.value = "Por favor, llena todos los campos"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            val request = CreateNoteRequest(titleValue, contentValue)
            val result = repository.createNote(request)
            result.onSuccess { createResponse ->
                if (createResponse.success) {
                    getAllNotes()
                    _title.value = ""
                    _content.value = ""
                    _showDialog.value = false
                } else {
                    _error.value = createResponse.message
                }
            }.onFailure { exception ->
                _error.value = exception.message
            }
            _isLoading.value = false
        }
    }

    fun onNoteSelected(note: NoteDTO) {
        val textToSpeak = "Título: ${note.title}. Contenido: ${note.content}"
        tts.speak(textToSpeak)
    }

    fun onTitleChange(newTitle: String) {
        _title.value = newTitle
    }

    fun onContentChange(newContent: String) {
        _content.value = newContent
    }

    fun openDialog() {
        _showDialog.value = true
    }

    fun closeDialog() {
        _showDialog.value = false
    }

    fun onNavigationHandled() {
        _navigationCommand.value = null
    }

    fun navigateSomewhere(destination: String) {
        _navigationCommand.value = destination
    }
}
