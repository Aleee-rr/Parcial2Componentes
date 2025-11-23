package com.ud.parcial2componentes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.ud.parcial2componentes.navigation.AppNavigation
import com.ud.parcial2componentes.ui.theme.Parcial2ComponentesTheme

/**
 * Activity principal de la aplicación.
 *
 * Responsabilidades:
 * 1. Configurar el tema visual de la aplicación usando [Parcial2ComponentesTheme].
 * 2. Configurar navegación mediante [AppNavigation].
 * 3. Habilitar el modo edge-to-edge para que la UI ocupe toda la pantalla.
 *
 * Esta clase actúa como punto de entrada de la aplicación.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Permite que la UI se extienda hasta los bordes de la pantalla
        enableEdgeToEdge()

        // Configura el contenido de Compose
        setContent {
            // Aplica el tema personalizado de la app
            Parcial2ComponentesTheme {
                // Surface actúa como contenedor principal y establece el color de fondo
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Inicializa la navegación de la aplicación
                    AppNavigation()
                }
            }
        }
    }
}
