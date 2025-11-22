package com.ud.parcial2componentes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import com.ud.parcial2componentes.ui.screens.PlanListScreen
import com.ud.parcial2componentes.ui.theme.Parcial2ComponentesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            Parcial2ComponentesTheme {
                PlanListScreen(
                    onPlanClick = { id ->

                        println("Plan seleccionado: $id")
                    }
                )
            }
        }
    }
}
