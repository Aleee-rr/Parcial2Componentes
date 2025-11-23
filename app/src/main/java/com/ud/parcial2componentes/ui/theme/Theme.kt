package com.ud.parcial2componentes.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// =======================
// Colores personalizados
// =======================
val PrimaryPurple = Color(0xFF9B7EE8)      // Morado principal, usado para botones, encabezados
val SecondaryPurple = Color(0xFFB39DDB)    // Morado claro, para elementos secundarios
val BackgroundLight = Color(0xFFF8F7FF)    // Fondo de pantalla en tema claro
val CardBackground = Color(0xFFFFFFFF)     // Color de fondo de cards
val AccentGreen = Color(0xFF4CAF50)        // Verde para estados positivos o confirmaciones
val AccentBlue = Color(0xFF2196F3)         // Azul para información o acciones destacadas

// =======================
// Esquemas de color
// =======================

// Tema claro
private val LightColorScheme = lightColorScheme(
    primary = PrimaryPurple,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE8DEF8),
    onPrimaryContainer = Color(0xFF21005E),

    secondary = SecondaryPurple,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8DEF8),
    onSecondaryContainer = Color(0xFF1D192B),

    tertiary = Color(0xFF7D5260),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFD8E4),
    onTertiaryContainer = Color(0xFF31111D),

    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),

    background = BackgroundLight,
    onBackground = Color(0xFF1C1B1F),

    surface = CardBackground,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F),

    outline = Color(0xFF79747E),
)

// Tema oscuro
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFD0BCFF),
    onPrimary = Color(0xFF381E72),
    primaryContainer = Color(0xFF4F378B),
    onPrimaryContainer = Color(0xFFEADDFF),

    secondary = Color(0xFFCCC2DC),
    onSecondary = Color(0xFF332D41),
    secondaryContainer = Color(0xFF4A4458),
    onSecondaryContainer = Color(0xFFE8DEF8),

    tertiary = Color(0xFFEFB8C8),
    onTertiary = Color(0xFF492532),
    tertiaryContainer = Color(0xFF633B48),
    onTertiaryContainer = Color(0xFFFFD8E4),

    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),

    background = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),

    surface = Color(0xFF1C1B1F),
    onSurface = Color(0xFFE6E1E5),
    surfaceVariant = Color(0xFF49454F),
    onSurfaceVariant = Color(0xFFCAC4D0),

    outline = Color(0xFF938F99),
)

// =======================
// Composable de tema
// =======================
/**
 * Tema principal de la aplicación.
 * Detecta si el sistema está en modo oscuro y aplica el esquema correspondiente.
 *
 * @param darkTheme Determina si se usa tema oscuro (por defecto se detecta del sistema)
 * @param content Composable que será envuelto en el tema
 */
@Composable
fun Parcial2ComponentesTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
