package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.AppScreen
import com.example.ui.QuestiaViewModel
import com.example.ui.components.WisdomAxesDialog
import com.example.ui.screens.CopilotChatScreen
import com.example.ui.screens.QuestIaMainScreen
import com.example.ui.screens.SoloEnemTrialScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  private val viewModel: QuestiaViewModel by viewModels {
    object : ViewModelProvider.Factory {
      override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return QuestiaViewModel(application) as T
      }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        QuestiaApp(viewModel = viewModel)
      }
    }
  }

  override fun onPause() {
    super.onPause()
    viewModel.saveOnAppExit()
  }

  override fun onStop() {
    super.onStop()
    viewModel.saveOnAppExit()
  }
}

@Composable
fun QuestiaApp(viewModel: QuestiaViewModel) {
  val state by viewModel.uiState.collectAsStateWithLifecycle()

  // Handle system back button when in secondary screen
  BackHandler(enabled = state.currentScreen != AppScreen.QUESTIA_MAIN) {
    viewModel.navigateToMain()
  }

  Crossfade(
    targetState = state.currentScreen,
    label = "screen_transition",
    modifier = Modifier.fillMaxSize()
  ) { screen ->
    when (screen) {
      AppScreen.QUESTIA_MAIN -> {
        QuestIaMainScreen(
          uiState = state,
          onUserNameChanged = { viewModel.onUserNameChanged(it) },
          onCompleteOnboarding = { viewModel.completeOnboarding() },
          onTabSelected = { viewModel.setActiveTab(it) },
          onStartQuest = { viewModel.startQuest(it) },
          onOpenAiChat = { viewModel.openAiChat(it) },
          onJoinGuilda = { viewModel.joinGuilda(it) },
          onLeaveGuilda = { viewModel.leaveGuilda() },
          onCreateGuilda = { name, school, motto, city, emblem ->
            viewModel.createGuilda(name, school, motto, city, emblem)
          },
          onGuildaSearchChanged = { viewModel.onGuildaSearchChanged(it) },
          onUpdateAvatarModel3d = { viewModel.updateAvatarModel3d(it) },
          onCompleteQuest = { viewModel.completeQuestDirectly(it) },
          onOpenSoloTrial = { viewModel.openSoloTrial(it) }
        )
      }
      AppScreen.COPILOT_CHAT -> {
        CopilotChatScreen(
          state = state,
          onBackClick = { viewModel.navigateToMain() },
          onSendMessage = { viewModel.sendMessage() },
          onInputChanged = { viewModel.onInputTextChanged(it) },
          onQuickPrompt = { viewModel.sendQuickPrompt(it) },
          onOpenWisdomClick = { viewModel.toggleWisdomSheet(true) },
          onDismissVictory = { viewModel.dismissVictoryDialog() }
        )
      }
      AppScreen.SOLO_ENEM_TRIAL -> {
        SoloEnemTrialScreen(
          state = state,
          onBackClick = { viewModel.navigateToMain() },
          onOptionSelected = { viewModel.onSelectSoloOption(it) },
          onSubmitAnswer = { viewModel.submitSoloAnswer() },
          onDismissIncorrect = { viewModel.dismissSoloIncorrectDialog() },
          onClaimVictory = { viewModel.claimSoloVictory() }
        )
      }
    }
  }

  // Wisdom axes dialog
  if (state.showWisdomSheet) {
    WisdomAxesDialog(
      wisdom = state.character.wisdomAxes,
      onDismiss = { viewModel.toggleWisdomSheet(false) }
    )
  }
}
