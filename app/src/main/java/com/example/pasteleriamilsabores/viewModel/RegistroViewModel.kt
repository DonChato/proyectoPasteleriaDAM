package com.example.pasteleriamilsabores.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RegistroUiState(
    val nombre: String = "",
    val email: String = "",
    val contrasena: String = "",
    val profileImageUri: Uri? = null,
    val tempImageUri: Uri? = null,
    val errorNombre: String? = null,
    val errorEmail: String? = null,
    val errorContrasena: String? = null,
    val esBotonHabilitado: Boolean = false
)
class RegistroViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegistroUiState())
    val uiState: StateFlow<RegistroUiState> = _uiState.asStateFlow()

    fun onNombreChange(nombre: String) {
        val error = if (nombre.length < 3) "El nombre es muy corto" else null
        _uiState.update { it.copy(nombre = nombre, errorNombre = error) }
        validarFormulario()
    }

    fun onEmailChange(email: String) {
        val error = if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) "El email no es válido" else null
        _uiState.update { it.copy(email = email, errorEmail = error) }
        validarFormulario()
    }

    fun onContrasenaChange(contrasena: String) {
        val error = if (contrasena.length < 8) "La contraseña debe tener al menos 8 caracteres" else null
        _uiState.update { it.copy(contrasena = contrasena, errorContrasena = error) }
        validarFormulario()
    }

    fun onProfileImageChange(uri: Uri?) {
        _uiState.update { it.copy(profileImageUri = uri) }
    }

    fun setTempImageUri(uri: Uri?) {
        _uiState.update { it.copy(tempImageUri = uri) }
    }


    private fun validarFormulario() {
        _uiState.update { currentState ->
            val esValido = currentState.errorNombre == null &&
                    currentState.errorEmail == null &&
                    currentState.errorContrasena == null &&
                    currentState.nombre.isNotBlank() &&
                    currentState.email.isNotBlank() &&
                    currentState.contrasena.isNotBlank()
            currentState.copy(esBotonHabilitado = esValido)
        }
    }
}