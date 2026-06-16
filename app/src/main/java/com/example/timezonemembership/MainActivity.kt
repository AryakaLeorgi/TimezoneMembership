package com.example.timezonemembership

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.timezonemembership.ui.navigation.NavGraph
import com.example.timezonemembership.ui.theme.TimezoneMembershipTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TimezoneMembershipTheme {
                NavGraph()
            }
        }
    }
}