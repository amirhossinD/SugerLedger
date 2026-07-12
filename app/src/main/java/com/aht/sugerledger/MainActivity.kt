package com.aht.sugerledger

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.aht.sugerledger.navigation.SugarLedgerNavHost
import com.aht.sugerledger.ui.theme.SugerLedgerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SugerLedgerTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    SugarLedgerNavHost()
                }
            }
        }
    }
}
