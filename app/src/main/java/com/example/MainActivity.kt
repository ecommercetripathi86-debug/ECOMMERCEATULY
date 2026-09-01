package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.data.model.EduModule
import com.example.ui.components.EduTributeHeader
import com.example.ui.screens.AiGuruScreen
import com.example.ui.screens.EngineTesterScreen
import com.example.ui.screens.TriviaBattleScreen
import com.example.ui.screens.VoiceGratitudeScreen
import com.example.ui.theme.EduTributeTheme
import com.example.ui.viewmodel.EduTributeViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: EduTributeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EduTributeTheme {
                EduTributeApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun EduTributeApp(viewModel: EduTributeViewModel) {
    val currentModule by viewModel.currentModule.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            EduTributeHeader(
                currentModule = currentModule,
                onSelectModule = { viewModel.selectModule(it) }
            )
        },
        contentWindowInsets = WindowInsets.systemBars
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            AnimatedContent(
                targetState = currentModule,
                transitionSpec = { fadeIn() togetherWith fadeOut() },
                label = "ModuleTransition"
            ) { targetModule ->
                when (targetModule) {
                    EduModule.AI_GURU -> AiGuruScreen(viewModel = viewModel)
                    EduModule.VOICE_OF_GRATITUDE -> VoiceGratitudeScreen(viewModel = viewModel)
                    EduModule.TRIVIA_BATTLE -> TriviaBattleScreen(viewModel = viewModel)
                    EduModule.API_ENGINE -> EngineTesterScreen(viewModel = viewModel)
                }
            }
        }
    }
}
