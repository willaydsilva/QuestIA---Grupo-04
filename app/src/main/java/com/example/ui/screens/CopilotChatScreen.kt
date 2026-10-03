package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AxisFeedback
import com.example.model.ChatMessage
import com.example.model.MessageSender
import com.example.ui.QuestiaUiState
import com.example.ui.components.DarkFantasyCard
import com.example.ui.components.GoldenRpgButton
import com.example.ui.components.MathMarkdownFormatter
import com.example.ui.components.RichChatMessageText
import com.example.ui.theme.AmberGold
import com.example.ui.theme.ArcaneMana
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldGlow
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.QuestiaCardBorder
import com.example.ui.theme.QuestiaDarkBg
import com.example.ui.theme.QuestiaDeepNavy
import com.example.ui.theme.QuestiaSurface
import com.example.ui.theme.QuestiaSurfaceVariant
import com.example.ui.theme.RubyHealth
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextLight
import com.example.ui.theme.TextMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CopilotChatScreen(
  state: QuestiaUiState,
  onBackClick: () -> Unit,
  onSendMessage: () -> Unit,
  onInputChanged: (String) -> Unit,
  onQuickPrompt: (String) -> Unit,
  onOpenWisdomClick: () -> Unit,
  onDismissVictory: () -> Unit,
  modifier: Modifier = Modifier
) {
  val listState = rememberLazyListState()

  // Auto-scroll to bottom on new message
  LaunchedEffect(state.messages.size, state.isTyping) {
    if (state.messages.isNotEmpty()) {
      listState.animateScrollToItem(state.messages.size)
    }
  }

  Scaffold(
    modifier = modifier
      .fillMaxSize()
      .testTag("copilot_chat_screen"),
    containerColor = QuestiaDarkBg,
    topBar = {
      TopAppBar(
        modifier = Modifier.statusBarsPadding(),
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = QuestiaDeepNavy,
          navigationIconContentColor = GoldBright,
          titleContentColor = TextLight,
          actionIconContentColor = GoldPrimary
        ),
        navigationIcon = {
          IconButton(
            onClick = onBackClick,
            modifier = Modifier.testTag("back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Voltar ao Salão do Herói"
            )
          }
        },
        title = {
          Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Text(
                text = "Copiloto da Luz",
                style = MaterialTheme.typography.titleMedium.copy(
                  fontWeight = FontWeight.Black,
                  fontSize = 17.sp,
                  letterSpacing = 1.sp
                ),
                color = GoldBright
              )
              Spacer(modifier = Modifier.width(6.dp))
              Box(
                modifier = Modifier
                  .size(8.dp)
                  .clip(CircleShape)
                  .background(if (state.isLiveGeminiActive) Color(0xFF22C55E) else Color(0xFFFFA502))
              )
            }
            Text(
              text = if (state.isLiveGeminiActive) "✦ Gemini 3.5 Flash Online" else "Tutor Socrático • Foco Anti-Brain Rot",
              style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
              color = if (state.isLiveGeminiActive) Color(0xFF4ADE80) else ArcaneMana
            )
          }
        },
        actions = {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = GoldDark.copy(alpha = 0.6f),
            border = BorderStroke(1.dp, GoldPrimary),
            modifier = Modifier
              .padding(end = 12.dp)
              .clickable { onOpenWisdomClick() }
              .testTag("eixos_chip")
          ) {
            Row(
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = null,
                tint = GoldBright,
                modifier = Modifier.size(15.dp)
              )
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "EIXOS",
                color = GoldBright,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      )
    },
    bottomBar = {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .background(QuestiaDeepNavy)
          .navigationBarsPadding()
          .imePadding()
          .padding(horizontal = 12.dp, vertical = 8.dp)
      ) {
        // Quick Socratic Suggestions Chips
        QuickSuggestionsRow(
          heroName = state.currentHero.name,
          onQuickPrompt = onQuickPrompt,
          enabled = !state.isTyping
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Message Input Field
        Row(
          modifier = Modifier.fillMaxWidth(),
          verticalAlignment = Alignment.CenterVertically
        ) {
          OutlinedTextField(
            value = state.inputText,
            onValueChange = onInputChanged,
            placeholder = {
              Text(
                text = "Responda ao Copiloto da Luz...",
                color = TextMuted,
                fontSize = 14.sp
              )
            },
            modifier = Modifier
              .weight(1f)
              .testTag("chat_input_field"),
            colors = OutlinedTextFieldDefaults.colors(
              focusedContainerColor = QuestiaSurface,
              unfocusedContainerColor = QuestiaSurface,
              focusedBorderColor = GoldPrimary,
              unfocusedBorderColor = QuestiaCardBorder.copy(alpha = 0.6f),
              focusedTextColor = TextLight,
              unfocusedTextColor = TextLight,
              cursorColor = GoldBright
            ),
            shape = RoundedCornerShape(14.dp),
            maxLines = 3
          )

          Spacer(modifier = Modifier.width(8.dp))

          Surface(
            shape = CircleShape,
            color = if (state.inputText.isNotBlank() && !state.isTyping) GoldPrimary else QuestiaSurfaceVariant,
            border = BorderStroke(1.dp, if (state.inputText.isNotBlank()) GoldGlow else QuestiaCardBorder.copy(alpha = 0.4f)),
            modifier = Modifier
              .size(50.dp)
              .clip(CircleShape)
              .clickable(
                enabled = state.inputText.isNotBlank() && !state.isTyping,
                onClick = onSendMessage
              )
              .testTag("send_button")
          ) {
            Box(contentAlignment = Alignment.Center) {
              if (state.isTyping) {
                CircularProgressIndicator(
                  modifier = Modifier.size(20.dp),
                  strokeWidth = 2.dp,
                  color = GoldBright
                )
              } else {
                Icon(
                  imageVector = Icons.AutoMirrored.Filled.Send,
                  contentDescription = "Enviar",
                  tint = if (state.inputText.isNotBlank()) TextDark else TextMuted,
                  modifier = Modifier.size(20.dp)
                )
              }
            }
          }
        }
      }
    }
  ) { paddingValues ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .background(
          brush = Brush.verticalGradient(
            colors = listOf(
              QuestiaDeepNavy,
              QuestiaDarkBg,
              Color(0xFF04060A)
            )
          )
        )
        .padding(paddingValues)
    ) {
      LazyColumn(
        state = listState,
        modifier = Modifier
          .fillMaxSize()
          .padding(horizontal = 14.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
      ) {
        item {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (state.isLiveGeminiActive) Color(0xFF0D2818) else Color(0xFF131A2D),
            border = BorderStroke(1.dp, if (state.isLiveGeminiActive) Color(0xFF22C55E).copy(alpha = 0.5f) else GoldDark)
          ) {
            Row(
              modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 7.dp),
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = if (state.isLiveGeminiActive) "🟢 Conexão IA Ativa: Google Gemini 3.5 Flash em tempo real" else "⚡ Conexão Socrática Ativa (Adicione GEMINI_API_KEY no painel Secrets do AI Studio para IA na nuvem)",
                color = if (state.isLiveGeminiActive) Color(0xFF4ADE80) else GoldGlow,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }

        items(state.messages, key = { it.id }) { message ->
          when (message.sender) {
            MessageSender.SYSTEM -> SystemMessageBanner(message = message)
            MessageSender.COPILOT -> CopilotMessageBubble(
              message = message,
              onOpenWisdomClick = onOpenWisdomClick
            )
            MessageSender.USER -> UserMessageBubble(message = message)
          }
        }

        if (state.isTyping) {
          item {
            TypingIndicator()
          }
        }
      }
    }
  }

  // Victory Dialog
  if (state.showVictoryDialog) {
    AlertDialog(
      onDismissRequest = onDismissVictory,
      containerColor = QuestiaDeepNavy,
      shape = RoundedCornerShape(16.dp),
      icon = {
        Icon(
          imageVector = Icons.Default.LockOpen,
          contentDescription = null,
          tint = GoldBright,
          modifier = Modifier.size(36.dp)
        )
      },
      title = {
        Text(
          text = "MÁQUINA DA ILUSÃO VENCIDA!",
          color = GoldBright,
          fontSize = 18.sp,
          fontWeight = FontWeight.Black,
          textAlign = TextAlign.Center
        )
      },
      text = {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
          Text(
            text = "Você decifrou o Enigma da Equação usando o raciocínio autêntico e dispersou as brumas do Brain Rot!",
            color = TextLight,
            fontSize = 13.sp,
            textAlign = TextAlign.Center
          )
          Spacer(modifier = Modifier.height(12.dp))
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = QuestiaSurface,
            border = BorderStroke(1.dp, GoldPrimary)
          ) {
            Column(
              modifier = Modifier.padding(12.dp),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Text(
                text = "RECOMPENSAS CONCEDIDAS",
                color = TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
              Spacer(modifier = Modifier.height(4.dp))
              Text(
                text = "+250 XP • +50 Ouro • Foco 100%",
                color = GoldBright,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black
              )
              Spacer(modifier = Modifier.height(2.dp))
              Text(
                text = "Parabéns, você avançou para o Nível 2!",
                color = ArcaneMana,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
              )
            }
          }
        }
      },
      confirmButton = {
        GoldenRpgButton(
          text = "RETORNAR AO SALÃO",
          onClick = {
            onDismissVictory()
            onBackClick()
          },
          modifier = Modifier.fillMaxWidth()
        )
      }
    )
  }
}

@Composable
private fun SystemMessageBanner(message: ChatMessage) {
  Box(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 8.dp),
    contentAlignment = Alignment.Center
  ) {
    Surface(
      shape = RoundedCornerShape(10.dp),
      color = Color(0xFF0E1729),
      border = BorderStroke(1.dp, QuestiaCardBorder.copy(alpha = 0.5f))
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
      ) {
        Icon(
          imageVector = Icons.Default.Shield,
          contentDescription = null,
          tint = GoldPrimary,
          modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        RichChatMessageText(
          text = message.text,
          color = GoldGlow,
          boldColor = Color.White,
          codeColor = GoldBright,
          fontSize = 12.sp,
          lineHeight = 16.sp,
          textAlign = TextAlign.Center,
          modifier = Modifier.weight(1f)
        )
      }
    }
  }
}

@Composable
private fun CopilotMessageBubble(
  message: ChatMessage,
  onOpenWisdomClick: () -> Unit
) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Start
  ) {
    // Copilot Icon Badge
    Box(
      modifier = Modifier
        .size(38.dp)
        .border(1.5.dp, GoldPrimary, CircleShape)
        .padding(2.dp)
        .clip(CircleShape)
        .background(QuestiaDeepNavy),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.AutoAwesome,
        contentDescription = "Copiloto da Luz",
        tint = GoldBright,
        modifier = Modifier.size(20.dp)
      )
    }

    Spacer(modifier = Modifier.width(10.dp))

    Column(modifier = Modifier.widthIn(max = 300.dp)) {
      Surface(
        shape = RoundedCornerShape(
          topStart = 4.dp,
          topEnd = 16.dp,
          bottomEnd = 16.dp,
          bottomStart = 16.dp
        ),
        color = QuestiaSurface,
        border = BorderStroke(1.2.dp, GoldPrimary.copy(alpha = 0.7f)),
        tonalElevation = 4.dp
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          // Sender Header
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "Copiloto da Luz",
              color = GoldBright,
              fontSize = 12.sp,
              fontWeight = FontWeight.Black
            )
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = QuestiaDeepNavy,
              border = BorderStroke(0.5.dp, QuestiaCardBorder)
            ) {
              Text(
                text = "TUTOR SOCRÁTICO",
                color = ArcaneMana,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          // Message Body (With rich markdown bold and math Unicode equations)
          RichChatMessageText(
            text = message.text,
            color = TextLight,
            boldColor = Color.White,
            codeColor = GoldBright,
            fontSize = 14.sp,
            lineHeight = 20.sp
          )

          // Equation Highlight plaque if any
          if (message.equationHighlight != null) {
            val cleanHighlight = MathMarkdownFormatter.cleanMathAndEquations(message.equationHighlight)
              .replace("`", "")
              .trim()
            Spacer(modifier = Modifier.height(10.dp))
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = QuestiaDeepNavy,
              border = BorderStroke(1.dp, GoldGlow.copy(alpha = 0.5f))
            ) {
              Text(
                text = cleanHighlight,
                color = GoldBright,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
              )
            }
          }

          // Axis Feedback Pills
          if (!message.axisFeedback.isNullOrEmpty()) {
            Spacer(modifier = Modifier.height(10.dp))
            Column(
              modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .background(QuestiaDeepNavy)
                .padding(8.dp)
            ) {
              Text(
                text = "⚔️ AVALIAÇÃO DOS EIXOS",
                color = GoldPrimary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
              )
              Spacer(modifier = Modifier.height(4.dp))
              message.axisFeedback.forEach { fb ->
                Row(
                  modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp),
                  verticalAlignment = Alignment.CenterVertically,
                  horizontalArrangement = Arrangement.SpaceBetween
                ) {
                  Text(
                    text = "• ${fb.axisName}: ${fb.reason}",
                    color = TextMuted,
                    fontSize = 10.sp,
                    modifier = Modifier.weight(1f)
                  )
                  Text(
                    text = "+${fb.deltaPoints}",
                    color = GoldBright,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                  )
                }
              }
            }
          }
        }
      }
    }
  }
}

@Composable
private fun UserMessageBubble(message: ChatMessage) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.End
  ) {
    Surface(
      shape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 4.dp,
        bottomEnd = 16.dp,
        bottomStart = 16.dp
      ),
      color = Color(0xFF1E263D),
      border = BorderStroke(1.dp, ArcaneMana.copy(alpha = 0.6f)),
      modifier = Modifier.widthIn(max = 280.dp)
    ) {
      Column(modifier = Modifier.padding(12.dp)) {
        Text(
          text = "Você (Aprendiz)",
          color = ArcaneMana,
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        RichChatMessageText(
          text = message.text,
          color = TextLight,
          boldColor = Color.White,
          codeColor = ArcaneMana,
          fontSize = 14.sp,
          lineHeight = 19.sp
        )
      }
    }
  }
}

@Composable
private fun TypingIndicator() {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.Start,
    verticalAlignment = Alignment.CenterVertically
  ) {
    Box(
      modifier = Modifier
        .size(34.dp)
        .border(1.dp, GoldPrimary, CircleShape)
        .clip(CircleShape)
        .background(QuestiaDeepNavy),
      contentAlignment = Alignment.Center
    ) {
      Icon(
        imageVector = Icons.Default.AutoAwesome,
        contentDescription = null,
        tint = GoldBright,
        modifier = Modifier.size(16.dp)
      )
    }
    Spacer(modifier = Modifier.width(10.dp))
    Surface(
      shape = RoundedCornerShape(12.dp),
      color = QuestiaSurface,
      border = BorderStroke(1.dp, QuestiaCardBorder.copy(alpha = 0.5f))
    ) {
      Row(
        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        CircularProgressIndicator(
          modifier = Modifier.size(14.dp),
          strokeWidth = 2.dp,
          color = GoldBright
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
          text = "O Copiloto da Luz está ponderando uma pista socrática...",
          color = TextMuted,
          fontSize = 11.sp
        )
      }
    }
  }
}

@Composable
private fun QuickSuggestionsRow(
  heroName: String,
  onQuickPrompt: (String) -> Unit,
  enabled: Boolean
) {
  val suggestions = listOf(
    "A = 1, B = 4, C = -11",
    "Delta = 60",
    "Como identificar figuras de linguagem?",
    "Dicas de interpretação em inglês",
    "Revolução Industrial e cidadania",
    "Dicas contra o Brain Rot e distração",
    "Como o herói $heroName pode me ajudar?",
    "Por que o x² tem coeficiente 1?",
    "Não lembro a fórmula de Delta"
  )

  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    modifier = Modifier.fillMaxWidth()
  ) {
    items(suggestions) { suggestion ->
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = QuestiaSurfaceVariant,
        border = BorderStroke(1.dp, QuestiaCardBorder.copy(alpha = 0.6f)),
        modifier = Modifier.clickable(enabled = enabled) {
          onQuickPrompt(suggestion)
        }
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Icon(
            imageVector = Icons.Default.Lightbulb,
            contentDescription = null,
            tint = AmberGold,
            modifier = Modifier.size(12.dp)
          )
          Spacer(modifier = Modifier.width(5.dp))
          Text(
            text = suggestion,
            color = TextLight,
            fontSize = 11.sp
          )
        }
      }
    }
  }
}
