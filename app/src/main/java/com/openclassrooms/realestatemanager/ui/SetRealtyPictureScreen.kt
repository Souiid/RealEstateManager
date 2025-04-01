package com.openclassrooms.realestatemanager.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.RealtyPicture
import com.openclassrooms.realestatemanager.ui.composable.ThemeButton
import com.openclassrooms.realestatemanager.ui.composable.ThemeOutlinedTextField
import com.openclassrooms.realestatemanager.ui.composable.ThemeTopBar

@Composable
fun SetRealtyPictureScreen(viewModel: SetRealtyPictureViewModel, onNext: () -> Unit, onBack: () -> Unit) {

    val context = LocalContext.current

    val photos = remember { mutableStateListOf<RealtyPicture>() }
    var enabled by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var selectedImage = remember { mutableStateOf<Bitmap?>(null) }
    var cameraUri by remember { mutableStateOf<Uri?>(null) }
    var showFabMenu by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        photos.addAll(viewModel.getRealtyPictures() ?: emptyList())
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImage.value = getBitmapFromUri(context, it)
            showDialog = true
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            cameraUri?.let {
                selectedImage.value = getBitmapFromUri(context, it)
                showDialog = true
            }
        } else {
            Log.d("PhotoGalleryScreen", "Camera capture failed")
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            cameraUri = createImageUri(context)
            cameraUri?.let { cameraLauncher.launch(it) }
        } else {
            Log.e("PhotoGalleryScreen", "Camera permission denied")
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding(),
        topBar = {
            ThemeTopBar(title = "Realty pictures", onBackClick = { onBack() })
        },
        bottomBar = {
            ThemeButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                text = "Next",
                enabled = !photos.isEmpty(),
                onClick = {
                    viewModel.setRealtyPictures(photos)
                    onNext()
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                showFabMenu = true
            }) {
                Icon(Icons.Default.Add, contentDescription = "Add Photo")
            }
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (photos.isEmpty()) {
                    Text(text = "No photos selected, Add a photo by clicking button +")
                    enabled = false
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 128.dp),
                        contentPadding = PaddingValues(8.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        items(photos.size) { index ->
                            val realtyPicture = photos[index]
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.clickable {
                                    photos.remove(realtyPicture)
                                }
                            ) {
                                Image(
                                    painter = rememberImagePainter(realtyPicture.bitmap),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .size(128.dp)
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                Text(
                                    text = realtyPicture.description,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }
                        }
                    }
                    enabled = true
                }
            }
        }
    )

    if (showFabMenu) {
        CameraGalleryDialog(
            onDismissRequest = { showFabMenu = false },
            onSelectCamera = {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    cameraUri = createImageUri(context)
                    cameraLauncher.launch(cameraUri!!)
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
                showFabMenu = false
            },
            onSelectGallery = {
                galleryLauncher.launch("image/*")
                showFabMenu = false
            }
        )
    }



    if (showDialog && selectedImage.value != null) {
        ImageDescriptionDialog(
            image = rememberAsyncImagePainter(selectedImage.value),
            onDismissRequest = { showDialog = false },
            onAddClick = { description ->
                val realtyPicture = RealtyPicture(selectedImage.value, description)
                photos.add(realtyPicture)
                showDialog = false
            }
        )
    }
}


@Composable
fun ImageDescriptionDialog(
    image: Painter,
    onDismissRequest: () -> Unit,
    onAddClick: (String) -> Unit
) {
    var description by remember { mutableStateOf("") }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .width(IntrinsicSize.Min)
            ) {
                // Image
                Image(
                    painter = image,
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // TextField for description
                ThemeOutlinedTextField(
                    value = description,
                    onValueChanged = { description = it },
                    labelID = R.string.description,
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text,
                    singleLine = false,
                    height = 100.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Buttons
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { onDismissRequest() }) {
                        Text("Annuler")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onAddClick(description) }
                    ) {
                        Text("Ajouter")
                    }
                }
            }
        }
    }
}

@Composable
fun CameraGalleryDialog(
    onDismissRequest: () -> Unit,
    onSelectCamera: () -> Unit,
    onSelectGallery: () -> Unit
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.width(IntrinsicSize.Min)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .width(IntrinsicSize.Min)
            ) {
                Text(text = "Choisissez une option")
                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { onSelectCamera() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Prendre une photo")
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(onClick = { onSelectGallery() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Choisir depuis la galerie")
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(onClick = { onDismissRequest() }, modifier = Modifier.fillMaxWidth()) {
                    Text("Annuler")
                }
            }
        }
    }
}

fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun createImageUri(context: Context): Uri? {
    val contentResolver = context.contentResolver
    val contentValues = android.content.ContentValues().apply {
        put(
            android.provider.MediaStore.MediaColumns.DISPLAY_NAME,
            "captured_image_${System.currentTimeMillis()}.jpg"
        )
        put(android.provider.MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
    }

    val uri = contentResolver.insert(
        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        contentValues
    )
    if (uri == null) {
        Log.e("createImageUri", "Failed to create image URI")
    } else {
        Log.d("createImageUri", "Generated Uri: $uri")
    }

    return uri
}
