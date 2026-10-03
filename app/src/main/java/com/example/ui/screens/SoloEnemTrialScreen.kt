package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.QuestiaUiState
import com.example.ui.components.MathMarkdownFormatter
import com.example.ui.components.RichChatMessageText

private val DarkBackground = Color(0xFF0A0E17)
private val CardBackground = Color(0xFF121826)
private val SurfaceBorder = Color(0xFF1E2838)
private val TextMuted = Color(0xFF8B9CB3)
private val GoldAccent = Color(0xFFFFD700)
private val PurpleSanctuary = Color(0xFFA855F7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoloEnemTrialScreen(
  state: QuestiaUiState,
  onBackClick: () -> Unit,
  onOptionSelected: (String) -> Unit,
  onSubmitAnswer: () -> Unit,
  onDismissIncorrect: () -> Unit,
  onClaimVictory: () -> Unit
) {
  val challenge = state.activeSoloChallenge
  val studentName = state.getDisplayName()
  val subjectColor = try {
    Color(android.graphics.Color.parseColor(challenge.subject.colorHex))
  } catch (_: Exception) {
    GoldAccent
  }

  val scrollState = rememberScrollState()

  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(DarkBackground)
  ) {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .navigationBarsPadding()
    ) {
      // Top Bar
      TopAppBar(
        title = {
          Column {
            Text(
              text = "SANTUÁRIO DO RITO FINAL",
              fontSize = 14.sp,
              fontWeight = FontWeight.Black,
              color = GoldAccent,
              letterSpacing = 1.sp
            )
            Text(
              text = "PROVA DE AUTONOMIA • ${challenge.subject.displayName.uppercase()}",
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = subjectColor,
              letterSpacing = 0.5.sp
            )
          }
        },
        navigationIcon = {
          IconButton(
            onClick = onBackClick,
            modifier = Modifier.testTag("solo_trial_back_button")
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Filled.ArrowBack,
              contentDescription = "Voltar",
              tint = Color.White
            )
          }
        },
        colors = TopAppBarDefaults.topAppBarColors(
          containerColor = CardBackground
        ),
        actions = {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = PurpleSanctuary.copy(alpha = 0.15f),
            border = BorderStroke(1.dp, PurpleSanctuary),
            modifier = Modifier.padding(end = 12.dp)
          ) {
            Text(
              text = "MODO SOLO: SEM IA 🛡️",
              fontSize = 9.sp,
              fontWeight = FontWeight.Black,
              color = Color(0xFFE9D5FF),
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      )

      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(scrollState)
          .padding(horizontal = 16.dp, vertical = 12.dp)
      ) {
        // Enredo Épico: O Copiloto Confia no Aluno e Silencia a IA
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = CardBackground),
          modifier = Modifier
            .fillMaxWidth()
            .border(
              1.5.dp,
              Brush.horizontalGradient(listOf(PurpleSanctuary, GoldAccent)),
              RoundedCornerShape(16.dp)
            )
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
              Box(
                modifier = Modifier
                  .size(46.dp)
                  .clip(CircleShape)
                  .background(PurpleSanctuary.copy(alpha = 0.2f))
                  .border(1.5.dp, GoldAccent, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Text(text = "🧙‍♂️", fontSize = 24.sp)
              }

              Spacer(modifier = Modifier.width(12.dp))

              Column {
                Text(
                  text = "COPILOTO DA LUZ",
                  fontSize = 13.sp,
                  fontWeight = FontWeight.Black,
                  color = GoldAccent,
                  letterSpacing = 1.sp
                )
                Text(
                  text = "Voz Selada pelo Rito de Passagem",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = Color(0xFFC084FC)
                )
              }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color(0xFF18182B),
              border = BorderStroke(1.dp, Color(0xFF332A4F)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(12.dp)) {
                Text(
                  text = "“Nobre $studentName, você alcançou 100% da Trilha de ${challenge.subject.displayName}!\n\nNeste santuário sagrado, minha voz está temporariamente em silêncio: nenhuma Inteligência Artificial pode te entregar respostas, fórmulas ou atalhos nesta fase final.\n\nEu não posso intervir... mas meu coração de mentor transborda de orgulho e confiança em você. E não tema: o erro faz parte do caminho. Se errar, aprendemos juntos e você tenta de novo!\n\nMostre ao mundo e a si mesmo a força do seu próprio raciocínio autônomo!”",
                  fontSize = 12.sp,
                  color = Color(0xFFE2E8F0),
                  lineHeight = 18.sp
                )
              }
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Badge da Prova Oficial do ENEM
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF2E1A47),
            border = BorderStroke(1.dp, Color(0xFFC084FC))
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
              Text(text = "🏛️", fontSize = 13.sp)
              Spacer(modifier = Modifier.width(6.dp))
              Text(
                text = "ENEM ${challenge.enemYear} • QUESTÃO ${challenge.questionNumber}",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFFF3E8FF)
              )
            }
          }

          Text(
            text = "+${challenge.xpReward} XP MESTRE",
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = GoldAccent
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Card do Enunciado do ENEM
        Card(
          shape = RoundedCornerShape(16.dp),
          colors = CardDefaults.cardColors(containerColor = CardBackground),
          modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, subjectColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
        ) {
          Column(modifier = Modifier.padding(16.dp)) {
            Text(
              text = challenge.title,
              fontSize = 17.sp,
              fontWeight = FontWeight.Black,
              color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Texto contextualizado da questão do ENEM
            Surface(
              shape = RoundedCornerShape(10.dp),
              color = Color(0xFF0F1420),
              border = BorderStroke(1.dp, SurfaceBorder),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = challenge.contextText,
                fontSize = 13.sp,
                color = Color(0xFFCBD5E1),
                lineHeight = 20.sp,
                modifier = Modifier.padding(12.dp)
              )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
              text = challenge.questionStatement,
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White,
              lineHeight = 20.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "SELECIONE SUA RESPOSTA AUTÔNOMA:",
          fontSize = 11.sp,
          fontWeight = FontWeight.Black,
          color = subjectColor,
          letterSpacing = 1.sp,
          modifier = Modifier.padding(bottom = 8.dp)
        )

        // Alternativas A, B, C, D, E
        challenge.options.forEach { option ->
          val isSelected = state.selectedSoloOption == option.letter
          val cardBorder = if (isSelected) {
            BorderStroke(2.dp, GoldAccent)
          } else {
            BorderStroke(1.dp, SurfaceBorder)
          }
          val cardBackground = if (isSelected) {
            Color(0xFF231E35)
          } else {
            CardBackground
          }

          Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = cardBackground),
            border = cardBorder,
            modifier = Modifier
              .fillMaxWidth()
              .padding(bottom = 10.dp)
              .clip(RoundedCornerShape(12.dp))
              .clickable { onOptionSelected(option.letter) }
              .testTag("solo_option_${option.letter}")
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(12.dp)
            ) {
              Box(
                modifier = Modifier
                  .size(36.dp)
                  .clip(CircleShape)
                  .background(if (isSelected) GoldAccent else Color(0xFF1E2838))
                  .border(1.dp, if (isSelected) Color.White else TextMuted, CircleShape),
                contentAlignment = Alignment.Center
              ) {
                Text(
                  text = option.letter,
                  fontSize = 15.sp,
                  fontWeight = FontWeight.Black,
                  color = if (isSelected) Color.Black else Color.White
                )
              }

              Spacer(modifier = Modifier.width(12.dp))

              Text(
                text = option.text,
                fontSize = 14.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) Color.White else Color(0xFFE2E8F0),
                lineHeight = 19.sp,
                modifier = Modifier.weight(1f)
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão de Confirmação da Resposta
        Button(
          onClick = onSubmitAnswer,
          enabled = state.selectedSoloOption != null,
          modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .testTag("confirm_solo_answer_button"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = GoldAccent,
            contentColor = Color.Black,
            disabledContainerColor = Color(0xFF2A2E3D),
            disabledContentColor = Color(0xFF6B7280)
          )
        ) {
          Text(
            text = "CONFIRMAR RESPOSTA SOLO (SEM IA) ⚔️",
            fontWeight = FontWeight.Black,
            fontSize = 13.sp,
            letterSpacing = 1.sp
          )
        }

        Spacer(modifier = Modifier.height(40.dp))
      }
    }

    // Modal de Erro (Acolhedor, sem punição)
    if (state.showSoloIncorrectDialog) {
      AlertDialog(
        onDismissRequest = onDismissIncorrect,
        containerColor = Color(0xFF1A1326),
        shape = RoundedCornerShape(20.dp),
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "🌱", fontSize = 24.sp)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "O Erro é Parte do Mestre",
              fontSize = 17.sp,
              fontWeight = FontWeight.Black,
              color = Color(0xFFFDE047)
            )
          }
        },
        text = {
          Column {
            Text(
              text = "Você marcou a alternativa ${state.selectedSoloOption}.",
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
              text = "O Copiloto da Luz sussurra um abraço de confiança:\n\n“Não se desespere e não tenha medo de errar, nobre aprendiz! Errar uma questão do ENEM faz parte do forjamento de um pensador autônomo.\n\nRespire fundo, releia o enunciado com calma, observe os dados fundamentais e tente outra vez!”",
              fontSize = 13.sp,
              color = Color(0xFFE2E8F0),
              lineHeight = 18.sp
            )
          }
        },
        confirmButton = {
          Button(
            onClick = onDismissIncorrect,
            colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = Color.Black),
            shape = RoundedCornerShape(10.dp)
          ) {
            Text(text = "TENTAR NOVAMENTE 🔄", fontWeight = FontWeight.Black, fontSize = 12.sp)
          }
        }
      )
    }

    // Modal de Vitória Triunfante
    if (state.showSoloVictoryDialog) {
      AlertDialog(
        onDismissRequest = onClaimVictory,
        containerColor = Color(0xFF13221A),
        shape = RoundedCornerShape(20.dp),
        title = {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "🏆", fontSize = 28.sp)
            Spacer(modifier = Modifier.width(10.dp))
            Column {
              Text(
                text = "RITO DE AUTONOMIA CONCLUÍDO!",
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFF4ADE80)
              )
              Text(
                text = "ENEM 2025 SUPERADO SEM IA",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent
              )
            }
          }
        },
        text = {
          Column {
            Text(
              text = "O Copiloto da Luz vibra com a sua glória:\n\n“Extraordinário! Você provou a si mesmo que domina a matéria e superou a névoa do Brain Rot com o seu próprio cérebro!”",
              fontSize = 13.sp,
              color = Color.White,
              lineHeight = 19.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
              shape = RoundedCornerShape(10.dp),
              color = Color(0xFF0C1912),
              border = BorderStroke(1.dp, Color(0xFF166534)),
              modifier = Modifier.fillMaxWidth()
            ) {
              Column(modifier = Modifier.padding(10.dp)) {
                Text(
                  text = "RESOLUÇÃO OFICIAL COMENTADA:",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Black,
                  color = Color(0xFF86EFAC),
                  letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                  text = challenge.explanation,
                  fontSize = 12.sp,
                  color = Color(0xFFE2E8F0),
                  lineHeight = 16.sp
                )
              }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceEvenly
            ) {
              Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF0F2E1E),
                border = BorderStroke(1.dp, Color(0xFF22C55E))
              ) {
                Text(
                  text = "+${challenge.xpReward} XP",
                  fontWeight = FontWeight.Black,
                  fontSize = 12.sp,
                  color = Color(0xFF4ADE80),
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
              }

              Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF2C2411),
                border = BorderStroke(1.dp, GoldAccent)
              ) {
                Text(
                  text = "+${challenge.goldReward} OURO",
                  fontWeight = FontWeight.Black,
                  fontSize = 12.sp,
                  color = GoldAccent,
                  modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
              }
            }
          }
        },
        confirmButton = {
          Button(
            onClick = onClaimVictory,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E), contentColor = Color.Black),
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "REIVINDICAR GLÓRIA & VOLTAR ÀS TRILHAS 🌟",
              fontWeight = FontWeight.Black,
              fontSize = 12.sp
            )
          }
        }
      )
    }
  }
}
