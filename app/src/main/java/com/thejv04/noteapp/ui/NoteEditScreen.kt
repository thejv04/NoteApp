package com.thejv04.noteapp.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.thejv04.noteapp.data.ChecklistConverter
import com.thejv04.noteapp.data.NoteViewModel
import com.thejv04.noteapp.model.ChecklistItem
import com.thejv04.noteapp.model.Note
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Share

/**
 * Screen for creating and editing notes.
 * Supports both plain text notes and checklist notes.
 * Allows attaching images from the device gallery and sharing note content.
 *
 * @param viewModel The [NoteViewModel] used to insert and update notes.
 * @param noteId The id of the note to edit, or -1 to create a new note.
 * @param onBack Callback invoked when the user navigates back.
 * @param language The current language code ("es" or "en").
 * @param isChecklistDefault Whether the note should default to checklist mode.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteEditScreen(
    viewModel: NoteViewModel,
    noteId: Int?,
    onBack: () -> Unit,
    language: String,
    isChecklistDefault: Boolean = false
) {
    val isSpanish = language == "es"
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var existingNote by remember { mutableStateOf<Note?>(null) }
    var isChecklist by remember { mutableStateOf(isChecklistDefault) }
    var checklistItems by remember { mutableStateOf(listOf<ChecklistItem>()) }
    var imageUris by remember { mutableStateOf(listOf<String>()) }
    val converter = remember { ChecklistConverter() }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri> ->
        uris.forEach { uri ->
            context.contentResolver.takePersistableUriPermission(
                uri,
                android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION
            )
        }
        imageUris = imageUris + uris.map { it.toString() }
    }

    LaunchedEffect(noteId) {
        if (noteId != null && noteId != -1) {
            val note = viewModel.allNotes.value.find { it.id == noteId }
            note?.let {
                title = it.title
                content = it.content
                isChecklist = it.isChecklist
                checklistItems = converter.toChecklistItems(it.checklistItems)
                imageUris = if (it.imageUris.isBlank()) emptyList()
                else it.imageUris.split(",")
                existingNote = it
            }
        } else {
            isChecklist = isChecklistDefault
            if (isChecklistDefault) checklistItems = listOf(ChecklistItem())
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (existingNote == null)
                            if (isSpanish) "Nueva Nota" else "New Note"
                        else
                            if (isSpanish) "Editar Nota" else "Edit Note"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    /** Opens the image picker to attach images to the note. */
                    IconButton(onClick = { imagePicker.launch("image/*") }) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = "Agregar imagen")
                    }
                    /** Shares the note content via the Android share sheet. */
                    IconButton(onClick = {
                        val hasContent = if (isChecklist)
                            checklistItems.any { it.text.isNotBlank() }
                        else
                            content.isNotBlank()

                        if (hasContent || imageUris.isNotEmpty()) {
                            val itemsJson = converter.fromChecklistItems(checklistItems)
                            val urisString = imageUris.joinToString(",")
                            if (existingNote != null) {
                                viewModel.updateNote(
                                    existingNote!!.copy(
                                        title = title,
                                        content = content,
                                        isChecklist = isChecklist,
                                        checklistItems = itemsJson,
                                        imageUris = urisString,
                                        timestamp = System.currentTimeMillis()
                                    )
                                )
                            } else {
                                viewModel.insertNote(
                                    Note(
                                        title = title,
                                        content = content,
                                        isChecklist = isChecklist,
                                        checklistItems = itemsJson,
                                        imageUris = urisString
                                    )
                                )
                            }
                            onBack()
                        }
                    }) {
                        Icon(Icons.Default.Check, contentDescription = "Guardar")
                    }

                    IconButton(onClick = {
                        val shareText = buildString {
                            if (title.isNotBlank()) {
                                append(title)
                                append("\n\n")
                            }
                            if (isChecklist) {
                                checklistItems.forEach { item ->
                                    append(if (item.isChecked) "✅ " else "⬜ ")
                                    append(item.text)
                                    append("\n")
                                }
                            } else {
                                append(content)
                            }
                        }
                        val intent = android.content.Intent().apply {
                            action = android.content.Intent.ACTION_SEND
                            putExtra(android.content.Intent.EXTRA_TEXT, shareText)
                            type = "text/plain"
                        }
                        context.startActivity(
                            android.content.Intent.createChooser(
                                intent,
                                if (isSpanish) "Compartir nota" else "Share note"
                            )
                        )
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Compartir")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text(if (isSpanish) "Título" else "Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (imageUris.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                ) {
                    items(imageUris) { uri ->
                        Box {
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(150.dp)
                            )
                            IconButton(
                                onClick = { imageUris = imageUris - uri },
                                modifier = Modifier.align(Alignment.TopEnd)
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Eliminar imagen",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (isChecklist) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    itemsIndexed(checklistItems) { index, item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = item.isChecked,
                                onCheckedChange = { checked ->
                                    checklistItems = checklistItems.toMutableList().also {
                                        it[index] = item.copy(isChecked = checked)
                                    }
                                }
                            )
                            OutlinedTextField(
                                value = item.text,
                                onValueChange = { text ->
                                    checklistItems = checklistItems.toMutableList().also {
                                        it[index] = item.copy(text = text)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                textStyle = LocalTextStyle.current.copy(
                                    textDecoration = if (item.isChecked)
                                        TextDecoration.LineThrough else TextDecoration.None,
                                    color = if (item.isChecked)
                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                                    else
                                        MaterialTheme.colorScheme.onSurface
                                ),
                                placeholder = { Text(if (isSpanish) "Elemento..." else "Item...") },
                                singleLine = true
                            )
                            IconButton(onClick = {
                                checklistItems = checklistItems.toMutableList().also {
                                    it.removeAt(index)
                                }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                    }
                    item {
                        TextButton(
                            onClick = { checklistItems = checklistItems + ChecklistItem() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isSpanish) "Agregar elemento" else "Add item")
                        }
                    }
                }
            } else {
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text(if (isSpanish) "Contenido" else "Content") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    maxLines = Int.MAX_VALUE
                )
            }
        }
    }
}