package com.thejv04.noteapp.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.thejv04.noteapp.data.NoteViewModel
import com.thejv04.noteapp.model.Note
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.text.style.TextDecoration
import com.thejv04.noteapp.data.ChecklistConverter
import com.thejv04.noteapp.model.ChecklistItem
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(
    viewModel: NoteViewModel,
    onNoteClick: (Note) -> Unit,
    onAddNote: (Boolean) -> Unit,
    onSettings: () -> Unit,
    language: String
) {
    val notes by viewModel.allNotes.collectAsState()
    val isSpanish = language == "es"
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isSpanish) "Mis Notas" else "My Notes") },
                actions = {
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Preferencias")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { showBottomSheet = true }) {
                Icon(Icons.Default.Add, contentDescription = "Agregar")
            }
        }
    ) { padding ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(8.dp),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(notes) { note ->
                NoteCard(
                    note = note,
                    onClick = { onNoteClick(note) },
                    onDelete = { viewModel.deleteNote(note) }
                )
            }
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = sheetState
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .padding(bottom = 32.dp)
                ) {
                    Text(
                        text = if (isSpanish) "Crear nueva" else "Create new",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    ListItem(
                        headlineContent = {
                            Text(if (isSpanish) "Nota" else "Note")
                        },
                        supportingContent = {
                            Text(if (isSpanish) "Texto libre" else "Free text")
                        },
                        leadingContent = {
                            Icon(Icons.Default.Create, contentDescription = null)
                        },
                        modifier = Modifier.clickable {
                            showBottomSheet = false
                            onAddNote(false)
                        }
                    )
                    HorizontalDivider()
                    ListItem(
                        headlineContent = {
                            Text(if (isSpanish) "Lista" else "List")
                        },
                        supportingContent = {
                            Text(if (isSpanish) "Lista de tareas" else "To-do list")
                        },
                        leadingContent = {
                            Icon(Icons.Default.CheckCircle, contentDescription = null)
                        },
                        modifier = Modifier.clickable {
                            showBottomSheet = false
                            onAddNote(true)
                        }
                    )
                }
            }
        }
    }
}
@Composable
fun NoteCard(
    note: Note,
    onClick: () -> Unit,
    onDelete: () -> Unit
) {
    val converter = remember { ChecklistConverter() }
    val checklistItems = remember(note.checklistItems) {
        converter.toChecklistItems(note.checklistItems)
    }
    val firstImage = remember(note.imageUris) {
        if (note.imageUris.isBlank()) null
        else note.imageUris.split(",").firstOrNull()
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column {
            // Imagen de portada
            if (firstImage != null) {
                AsyncImage(
                    model = firstImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                )
            }
            Column(modifier = Modifier.padding(12.dp)) {
                if (note.title.isNotBlank()) {
                    Text(
                        text = note.title,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                if (note.isChecklist) {
                    checklistItems.take(5).forEach { item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Checkbox(
                                checked = item.isChecked,
                                onCheckedChange = null,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = item.text,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    textDecoration = if (item.isChecked)
                                        TextDecoration.LineThrough else TextDecoration.None,
                                    color = if (item.isChecked)
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                    }
                    if (checklistItems.size > 5) {
                        Text(
                            text = "+ ${checklistItems.size - 5} más",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    Text(
                        text = note.content,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 6
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDelete) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                }
            }
        }
    }
}