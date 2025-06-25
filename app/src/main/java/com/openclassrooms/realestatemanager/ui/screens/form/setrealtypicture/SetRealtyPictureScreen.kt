package com.openclassrooms.realestatemanager.ui.screens.form.setrealtypicture

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter
import com.openclassrooms.realestatemanager.ui.composable.ThemeText
import com.openclassrooms.realestatemanager.ui.composable.ThemeTextStyle
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.RealtyPicture
import com.openclassrooms.realestatemanager.ui.composable.ThemeButton
import com.openclassrooms.realestatemanager.ui.composable.ThemeDialog
import com.openclassrooms.realestatemanager.ui.composable.ThemeOutlinedTextField
import com.openclassrooms.realestatemanager.ui.composable.ThemeTopBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun SetRealtyPictureScreen(
    viewModel: SetRealtyPictureViewModel = koinViewModel(),
    onNext: () -> Unit,
    onBack: () -> Unit
) {

    val context = LocalContext.current

    val photos = remember { mutableStateListOf<RealtyPicture>() }
    var enabled by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    val selectedImage = remember { mutableStateOf<Bitmap?>(null) }
    val selectedUri = remember { mutableStateOf<String?>(null) }
    var cameraUri by remember { mutableStateOf<Uri?>(null) }
    var showFabMenu by remember { mutableStateOf(false) }

    val updatedRealty = viewModel.getUpdatedRealty()


    LaunchedEffect(Unit) {
        if (updatedRealty != null) {
            for (picture in updatedRealty.pictures) {
               val bitmap = viewModel.getBitmapFromUri(context, Uri.parse(picture.uriString))
                val newRealtyPicture = RealtyPicture(bitmap, picture.description, picture.uriString)
                photos.add(newRealtyPicture)
            }
        }else {
            photos.addAll(viewModel.getRealtyPictures() ?: emptyList())
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract =  ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        uri?.let {
            val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
            try {
                context.contentResolver.takePersistableUriPermission(it, flags)
            } catch (e: SecurityException) {
                Log.e("Picker", "Cannot persist permission for uri: $it", e)
            }

            selectedImage.value = viewModel.getBitmapFromUri(context, it)
            selectedUri.value = uri.toString()
            showDialog = true
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success: Boolean ->
        if (success) {
            cameraUri?.let { uri ->
                val flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                try {
                    context.contentResolver.takePersistableUriPermission(uri, flags)
                } catch (e: SecurityException) {
                    Log.e("PhotoGalleryScreen", "Failed to persist URI permission", e)
                }

                selectedImage.value = viewModel.getBitmapFromUri(context, uri)
                selectedUri.value = cameraUri.toString()
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
            cameraUri = viewModel.createImageUri(context)
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
            ThemeTopBar(title = stringResource(R.string.realty_pictures), onBackClick = { onBack() })
        },
        bottomBar = {
            ThemeButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                text = if(updatedRealty == null) stringResource(R.string.next) else stringResource(R.string.update),
                enabled = !photos.isEmpty(),
                onClick = {
                    viewModel.setRealtyPictures(
                        realtyPictures = photos,
                        updatedRealty = updatedRealty) { onNext() }

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
                    ThemeText(
                        text = stringResource(R.string.no_photo),
                        style = ThemeTextStyle.NORMAL,
                        textAlign = TextAlign.Center
                    )
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
                                    painter = rememberAsyncImagePainter(realtyPicture.bitmap),
                                    contentDescription = null,
                                    contentScale = ContentScale.Fit,
                                    modifier = Modifier
                                        .size(128.dp)
                                        .padding(4.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                )
                                ThemeText(
                                    text = realtyPicture.description,
                                    style = ThemeTextStyle.NORMAL
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
                    cameraUri = viewModel.createImageUri(context)
                    cameraUri?.let {
                        cameraLauncher.launch(it)
                    }
                } else {
                    cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
                showFabMenu = false
            },
            onSelectGallery = {
                galleryLauncher.launch(arrayOf("image/*"))
                showFabMenu = false
            }
        )
    }



    if (showDialog && selectedImage.value != null) {
        ImageDescriptionDialog(
            image = rememberAsyncImagePainter(selectedImage.value),
            onDismissRequest = { showDialog = false },
            onAddClick = { description ->
                val realtyPicture =
                    RealtyPicture(selectedImage.value,
                        description,
                        selectedUri.value ?: "")
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
                Image(
                    painter = image,
                    contentDescription = "Selected Image",
                    modifier = Modifier
                        .size(250.dp)
                        .align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

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

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = { onDismissRequest() }) {
                        ThemeText(
                            text = stringResource(R.string.cancel),
                            style = ThemeTextStyle.NORMAL
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = { onAddClick(description) }
                    ) {
                        ThemeText(
                            text = stringResource(R.string.add),
                            style = ThemeTextStyle.NORMAL
                        )
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

    ThemeDialog(
        title = stringResource(R.string.insert_a_picture),
        description = stringResource(R.string.choose_option),
        primaryButtonTitle = stringResource(R.string.choose_from_gallery),
        onPrimaryButtonClick = { onSelectGallery() },
        secondaryButtonTitle = stringResource(R.string.take_a_picture),
        onSecondaryButtonClick = { onSelectCamera() },
        onDismissRequest = { onDismissRequest() }
    )

}
