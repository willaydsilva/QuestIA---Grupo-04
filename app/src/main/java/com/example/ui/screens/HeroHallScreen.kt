package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.CharacterProfile
import com.example.ui.components.CharacterStatsGrid
import com.example.ui.components.DarkFantasyCard
import com.example.ui.components.HeroProfileCard
import com.example.ui.components.MissionCard
import com.example.ui.components.ReinoHeader
import com.example.ui.theme.ArcaneMana
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.QuestiaCardBorder
import com.example.ui.theme.QuestiaDarkBg
import com.example.ui.theme.QuestiaDeepNavy
import com.example.ui.theme.QuestiaSurfaceVariant
import com.example.ui.theme.TextGold
import com.example.ui.theme.TextLight
import com.example.ui.theme.TextMuted

@Composable
fun HeroHallScreen(
  character: CharacterProfile,
  onFaceChallengeClick: () -> Unit,
  onOpenWisdomClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Scaffold(
    modifier = modifier
      .fillMaxSize()
      .testTag("hero_hall_screen"),
    containerColor = QuestiaDarkBg
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
        .statusBarsPadding()
        .navigationBarsPadding()
    ) {
      Column(
        modifier = Modifier
          .fillMaxSize()
          .verticalScroll(rememberScrollState())
          .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        // Cabeçalho: 'REINO DE QUESTIA'
        ReinoHeader()

        Spacer(modifier = Modifier.height(12.dp))

        // Perfil de usuário para 'Random - Explorador do Desconhecido' no Nível 1
        HeroProfileCard(profile = character)

        Spacer(modifier = Modifier.height(14.dp))

        // Grid com os status do personagem: Vida 100/100, Energia 80/80 e Ouro 100
        CharacterStatsGrid(profile = character)

        Spacer(modifier = Modifier.height(18.dp))

        // No centro da tela: Card de destaque 'MISSÃO DO DIA: O Enigma da Equação' com botão 'ENFRENTAR DESAFIO'
        MissionCard(
          onFaceChallengeClick = onFaceChallengeClick,
          questCompleted = character.questCompleted
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Lore Anti-Brain Rot Banner with Wisdom Axes trigger
        DarkFantasyCard(
          modifier = Modifier.fillMaxWidth(),
          borderColor = QuestiaCardBorder.copy(alpha = 0.6f),
          backgroundColor = QuestiaSurfaceVariant.copy(alpha = 0.6f)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.weight(1f)
            ) {
              Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = null,
                tint = GoldPrimary,
                modifier = Modifier.size(20.dp)
              )
              Spacer(modifier = Modifier.width(10.dp))
              Column {
                Text(
                  text = "EIXOS DA SABEDORIA SOCRÁTICA",
                  color = GoldBright,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  letterSpacing = 0.5.sp
                )
                Text(
                  text = "Acerto • Argumentação • Autonomia • Pesquisa • Correção",
                  color = TextMuted,
                  fontSize = 10.sp
                )
              }
            }

            TextButton(
              onClick = onOpenWisdomClick,
              modifier = Modifier.testTag("view_axes_button")
            ) {
              Text(
                text = "VER",
                color = ArcaneMana,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp
              )
            }
          }
        }

        Spacer(modifier = Modifier.height(20.dp))
      }
    }
  }
}
