package com.example.pasteleriamilsabores.ui.registro

import android.Manifest
import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.pasteleriamilsabores.navigation.AppScreens
import com.example.pasteleriamilsabores.viewModel.RegistroViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(navController: NavController, registroViewModel: RegistroViewModel) {
    val uiState by registroViewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { registroViewModel.onProfileImageChange(it) }
    }
    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) registroViewModel.onProfileImageChange(uiState.tempImageUri)
    }
    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            val tempUri = context.createImageUri()
            registroViewModel.setTempImageUri(tempUri)
            cameraLauncher.launch(tempUri)
        }
    }

    if (showDialog) {
        ImageSourceDialog(
            onDismiss = { showDialog = false },
            onCameraClick = { showDialog = false; permissionLauncher.launch(Manifest.permission.CAMERA) },
            onGalleryClick = { showDialog = false; galleryLauncher.launch("image/*") }
        )
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Formulario de Registro") }) }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileImage(imageUri = uiState.profileImageUri, onClick = { showDialog = true })
            Spacer(modifier = Modifier.height(24.dp))

            ValidatedFormField(
                value = uiState.nombre,
                onValueChange = registroViewModel::onNombreChange,
                label = "Nombre Completo",
                errorMessage = uiState.errorNombre
            )
            ValidatedFormField(
                value = uiState.email,
                onValueChange = registroViewModel::onEmailChange,
                label = "Correo Electrónico",
                errorMessage = uiState.errorEmail,
                keyboardType = KeyboardType.Email
            )
            ValidatedFormField(
                value = uiState.contrasena,
                onValueChange = registroViewModel::onContrasenaChange,
                label = "Contraseña",
                errorMessage = uiState.errorContrasena,
                keyboardType = KeyboardType.Password,
                isPassword = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            val buttonColor by animateColorAsState(
                if (uiState.esBotonHabilitado) MaterialTheme.colorScheme.primary else Color.Gray,
                label = "Button Color"
            )
            Button(
                onClick = { navController.navigate(AppScreens.ResumenScreen.route) },
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.esBotonHabilitado,
                colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
            ) { Text("Registrarse") }
        }
    }
}

@Composable
private fun ValidatedFormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String?,
    keyboardType: KeyboardType = KeyboardType.Text,
    isPassword: Boolean = false
) {
    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            isError = errorMessage != null,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
        )
        AnimatedVisibility(visible = errorMessage != null) {
            Text(text = errorMessage ?: "", color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(start = 16.dp))
        }
    }
}

@Composable
fun ProfileImage(imageUri: Uri?, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (imageUri != null) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUri),
                contentDescription = "Foto de perfil",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Añadir foto de perfil",
                modifier = Modifier.size(60.dp),
                tint = Color.Gray
            )
        }
    }
}

@Composable
fun ImageSourceDialog(onDismiss: () -> Unit, onCameraClick: () -> Unit, onGalleryClick: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Elegir fuente") },
        confirmButton = { TextButton(onClick = onCameraClick) { Text("Cámara") } },
        dismissButton = { TextButton(onClick = onGalleryClick) { Text("Galería") } }
    )
}

fun Context.createImageUri(): Uri {
    val file = File(cacheDir, "camera_photo_${System.currentTimeMillis()}.jpg")
    return FileProvider.getUriForFile(this, "${packageName}.provider", file)
}
