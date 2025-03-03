package com.example.state.notes.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.state.notes.data.model.NoteDTO
import com.example.state.ui.theme.Beige
import com.example.state.ui.theme.Teal
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NoteScreen(
    noteViewModel: NoteViewModel,
    onNavigate: (String) -> Unit
) {
    val notes by noteViewModel.notes.observeAsState(emptyList())
    val isLoading by noteViewModel.isLoading.observeAsState(false)
    val error by noteViewModel.error.observeAsState(null)
    val showDialog by noteViewModel.showDialog.observeAsState(false)
    val titleValue by noteViewModel.title.observeAsState("")
    val contentValue by noteViewModel.content.observeAsState("")
    val navigationCommand by noteViewModel.navigationCommand.observeAsState(null)

    // Manejo de navegación
    LaunchedEffect(navigationCommand) {
        navigationCommand?.let {
            onNavigate(it)
            noteViewModel.onNavigationHandled()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Teal)
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Mis Notas",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = Beige,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        // Botón para abrir el modal de creación de nota
        Button(
            onClick = { noteViewModel.openDialog() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Beige,
                contentColor = Teal
            )
        ) {
            Text(text = "Nueva Nota", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Error (si existe)
        if (!error.isNullOrEmpty()) {
            Text(
                text = error!!,
                color = Color.Red,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Loading (opcional)
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }

        // AHORA: Listado de notas en 2 columnas
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp)
        ) {
            items(notes) { note: NoteDTO ->
                NoteCard(
                    note = note,
                    onClick = { noteViewModel.onNoteSelected(note) }
                )
            }
        }
    }

    // Diálogo (modal) para crear nueva nota
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { noteViewModel.closeDialog() },
            title = {
                Text("Crear Nueva Nota")
            },
            text = {
                Column {
                    TextField(
                        value = titleValue,
                        onValueChange = { noteViewModel.onTitleChange(it) },
                        label = { Text("Título") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = contentValue,
                        onValueChange = { noteViewModel.onContentChange(it) },
                        label = { Text("Contenido") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = { noteViewModel.createNote() }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { noteViewModel.closeDialog() }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun NoteCard(note: NoteDTO, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                onClick()
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = note.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = note.content,
                fontSize = 14.sp
            )
        }
    }
}
