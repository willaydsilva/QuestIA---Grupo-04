package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Avatar3dModel
import com.example.model.AvatarItem
import com.example.model.AvatarItemType
import com.example.model.Guilda
import com.example.model.Hero
import com.example.model.Quest
import com.example.model.QuestIaData
import com.example.model.QuestSubject
import com.example.model.Trail
import com.example.model.UserAvatarCustomization
import com.example.ui.QuestIaTab
import com.example.ui.QuestiaUiState
import com.example.ui.components.FounderDollCard
import com.example.ui.components.UserAvatarDoll

private val DarkBackground = Color(0xFF090C15)
private val CardBackground = Color(0xFF151B2B)
private val TextMuted = Color(0xFF8B9BB4)
private val PrimaryCyan = Color(0xFF00E5FF)
private val GoldAccent = Color(0xFFFFA502)

@Composable
fun QuestIaMainScreen(
  uiState: QuestiaUiState,
  onUserNameChanged: (String) -> Unit,
  onCompleteOnboarding: () -> Unit,
  onTabSelected: (QuestIaTab) -> Unit,
  onStartQuest: (Quest) -> Unit,
  onOpenAiChat: (String?) -> Unit,
  onJoinGuilda: (String) -> Unit,
  onLeaveGuilda: () -> Unit,
  onCreateGuilda: (name: String, schoolName: String, motto: String, city: String, emblem: String) -> Unit,
  onGuildaSearchChanged: (String) -> Unit,
  onUpdateAvatarModel3d: (String) -> Unit,
  onCompleteQuest: (String) -> Unit = {},
  onOpenSoloTrial: (QuestSubject) -> Unit = {}
) {
  if (!uiState.isRegistered) {
    OnboardingScreen(
      userName = uiState.userName,
      onUserNameChanged = onUserNameChanged,
      onConfirm = onCompleteOnboarding
    )
    return
  }

  Scaffold(
    containerColor = DarkBackground,
    bottomBar = {
      QuestIaBottomNavigation(
        activeTab = uiState.activeTab,
        onTabSelected = onTabSelected
      )
    }
  ) { innerPadding ->
    Box(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding)
        .background(DarkBackground)
    ) {
      when (uiState.activeTab) {
        QuestIaTab.HOME -> HomeView(
          uiState = uiState,
          onGoToQuests = { onTabSelected(QuestIaTab.QUESTS) },
          onGoToGuildas = { onTabSelected(QuestIaTab.GUILDAS) },
          onGoToHerois = { onTabSelected(QuestIaTab.HEROIS) },
          onGoToTrilhas = { onTabSelected(QuestIaTab.TRILHAS) },
          onOpenAiChat = onOpenAiChat
        )
        QuestIaTab.QUESTS -> QuestsView(
          quests = uiState.quests,
          onStartQuest = onStartQuest,
          onOpenAiChat = onOpenAiChat,
          onCompleteQuest = onCompleteQuest
        )
        QuestIaTab.GUILDAS -> GuildasView(
          userGuilda = uiState.userGuilda,
          guildasList = uiState.filteredGuildas,
          searchQuery = uiState.guildaSearchQuery,
          onSearchChanged = onGuildaSearchChanged,
          onJoinGuilda = onJoinGuilda,
          onLeaveGuilda = onLeaveGuilda,
          onCreateGuilda = onCreateGuilda
        )
        QuestIaTab.TRILHAS -> TrilhasView(
          trails = uiState.userTrails,
          onGoToSubjectQuest = { subject ->
            onTabSelected(QuestIaTab.QUESTS)
          },
          onOpenSoloTrial = onOpenSoloTrial
        )
        QuestIaTab.HEROIS -> HeroisView(
          customization = uiState.avatarCustomization,
          hero = uiState.currentHero,
          displayName = uiState.getDisplayName(),
          onUpdateModel3d = onUpdateAvatarModel3d
        )
      }
    }
  }
}

// ---------------------------------------------------------
// ONBOARDING SCREEN
// ---------------------------------------------------------

@Composable
private fun OnboardingScreen(
  userName: String,
  onUserNameChanged: (String) -> Unit,
  onConfirm: () -> Unit
) {
  Box(
    modifier = Modifier
      .fillMaxSize()
      .background(
        Brush.verticalGradient(
          colors = listOf(Color(0xFF060913), Color(0xFF0F172A))
        )
      )
      .statusBarsPadding()
      .navigationBarsPadding()
      .padding(24.dp),
    contentAlignment = Alignment.Center
  ) {
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .border(1.5.dp, PrimaryCyan, RoundedCornerShape(20.dp)),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
      Column(
        modifier = Modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Box(
          modifier = Modifier
            .size(76.dp)
            .clip(CircleShape)
            .background(PrimaryCyan.copy(alpha = 0.15f))
            .border(2.dp, PrimaryCyan, CircleShape),
          contentAlignment = Alignment.Center
        ) {
          Text(text = "⚔️", fontSize = 38.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
          text = "REINO DE QUESTIA",
          fontSize = 24.sp,
          fontWeight = FontWeight.Black,
          color = Color.White,
          letterSpacing = 2.sp
        )

        Text(
          text = "PLATAFORMA GAMIFICADA CONTRA O BRAIN ROT",
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = PrimaryCyan,
          letterSpacing = 1.sp,
          modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
        )

        Text(
          text = "Adentre o Reino de QuestIA. Fortaleça seu herói estudante, vença missões em Matemática, Português, Inglês e História, e dispute a honra da sua Guilda Escolar!",
          fontSize = 13.sp,
          color = TextMuted,
          textAlign = TextAlign.Center,
          lineHeight = 18.sp
        )

        // Notice card explaining 0% initial progress for all trails
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = Color(0xFF101928),
          border = BorderStroke(1.dp, PrimaryCyan.copy(alpha = 0.5f)),
          modifier = Modifier
            .fillMaxWidth()
            .padding(top = 14.dp, bottom = 4.dp)
        ) {
          Row(
            modifier = Modifier.padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "🎯", fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp))
            Text(
              text = "A Trilha de Matemática já inicia em 75% (faltando 1 missão para os 100% e o Rito de Autonomia do ENEM 2025)! Cada missão realizada avança +25%.",
              fontSize = 11.sp,
              color = Color(0xFFD6E4F0),
              lineHeight = 15.sp
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
          value = userName,
          onValueChange = onUserNameChanged,
          label = { Text("Nome do seu Herói", color = TextMuted) },
          placeholder = { Text("Ex: Arthur, Sofia...", color = Color(0xFF55657E)) },
          singleLine = true,
          modifier = Modifier
            .fillMaxWidth()
            .testTag("student_name_input"),
          shape = RoundedCornerShape(12.dp),
          colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = PrimaryCyan,
            unfocusedBorderColor = Color(0xFF263248),
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
          ),
          keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
          keyboardActions = KeyboardActions(onDone = {
            if (userName.isNotBlank()) onConfirm()
          })
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(
          onClick = onConfirm,
          enabled = userName.isNotBlank(),
          modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .testTag("btn_enter_realm"),
          shape = RoundedCornerShape(12.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryCyan,
            contentColor = Color(0xFF090C15),
            disabledContainerColor = Color(0xFF1E2838),
            disabledContentColor = Color(0xFF475569)
          )
        ) {
          Text(
            text = "ENTRAR NO REINO ✦",
            fontWeight = FontWeight.Black,
            fontSize = 13.sp,
            letterSpacing = 1.sp
          )
        }
      }
    }
  }
}

// ---------------------------------------------------------
// 1. HOME SCREEN
// ---------------------------------------------------------

@Composable
private fun HomeView(
  uiState: QuestiaUiState,
  onGoToQuests: () -> Unit,
  onGoToGuildas: () -> Unit,
  onGoToHerois: () -> Unit,
  onGoToTrilhas: () -> Unit,
  onOpenAiChat: (String?) -> Unit
) {
  val hero = uiState.currentHero
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    HeaderTitle(
      title = "REINO DE QUESTIA",
      subtitle = "ENSINO MÉDIO & LIGAS ESTUDANTIS"
    )

    // Hero 3D & Stats Card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .border(1.5.dp, PrimaryCyan, RoundedCornerShape(18.dp)),
      shape = RoundedCornerShape(18.dp),
      colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
      Column(modifier = Modifier.padding(18.dp)) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.fillMaxWidth()
        ) {
          // Realistic 3D Customized Character (Unobstructed in Home)
          UserAvatarDoll(
            customization = uiState.avatarCustomization,
            size = 100,
            showTitleBadge = false,
            modifier = Modifier.clickable(onClick = onGoToHerois)
          )

          Spacer(modifier = Modifier.width(14.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = uiState.getDisplayName(),
              fontSize = 20.sp,
              fontWeight = FontWeight.Black,
              color = Color.White
            )

            // Guilda Badge if member
            if (uiState.userGuilda != null) {
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = GoldAccent.copy(alpha = 0.15f),
                border = BorderStroke(1.dp, GoldAccent),
                modifier = Modifier
                  .padding(top = 6.dp)
                  .clickable(onClick = onGoToGuildas)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                  Text(text = uiState.userGuilda?.emblem ?: "🛡️", fontSize = 11.sp)
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = "${uiState.userGuilda?.name} • #${uiState.userGuilda?.rank}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = GoldAccent
                  )
                }
              }
            } else {
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFF263248),
                modifier = Modifier
                  .padding(top = 6.dp)
                  .clickable(onClick = onGoToGuildas)
              ) {
                Text(
                  text = "+ Entrar em uma Guilda",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Bold,
                  color = TextMuted,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
              }
            }
          }

          // Level badge
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = PrimaryCyan.copy(alpha = 0.15f),
            border = BorderStroke(1.dp, PrimaryCyan)
          ) {
            Text(
              text = "LVL ${hero.level}",
              fontSize = 12.sp,
              fontWeight = FontWeight.Black,
              color = PrimaryCyan,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Stats Row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          StatItem(label = "VIDA (HP)", value = "${hero.hp}", valueColor = Color(0xFFFF4757), modifier = Modifier.weight(1f))
          StatItem(label = "MANA (MP)", value = "${hero.mp}", valueColor = Color(0xFF2ED573), modifier = Modifier.weight(1f))
          StatItem(label = "OURO", value = "${hero.gold} G", valueColor = GoldAccent, modifier = Modifier.weight(1f))
          StatItem(label = "XP TOTAL", value = "${hero.xp}", valueColor = PrimaryCyan, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(18.dp))

        Button(
          onClick = onGoToQuests,
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("action_quest_button"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryCyan,
            contentColor = Color(0xFF090C15)
          )
        ) {
          Text(
            text = "EXPLORAR MISSÕES POR MATÉRIA ⚔️",
            fontWeight = FontWeight.Black,
            fontSize = 12.sp,
            letterSpacing = 1.sp
          )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
          onClick = { onOpenAiChat(null) },
          modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .testTag("action_chat_ai_button"),
          shape = RoundedCornerShape(10.dp),
          colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF162035),
            contentColor = PrimaryCyan
          ),
          border = BorderStroke(1.5.dp, PrimaryCyan)
        ) {
          Text(
            text = "CONVERSAR COM O MENTOR IA 🔮",
            fontWeight = FontWeight.Black,
            fontSize = 12.sp,
            letterSpacing = 1.sp
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // TRILHAS DE CONHECIMENTO (VISUALIZAÇÃO DE PROGRESSO DAS MATÉRIAS)
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, Color(0xFF223048), RoundedCornerShape(16.dp))
        .clickable(onClick = onGoToTrilhas),
      shape = RoundedCornerShape(16.dp),
      colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
      Column(modifier = Modifier.padding(16.dp)) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "📚", fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp))
            Column {
              Text(
                text = "TRILHAS DE CONHECIMENTO",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 0.5.sp
              )
              Text(
                text = "Matemática em 75% • Conclua 1 missão para a Prova Solo (100%)",
                fontSize = 10.sp,
                color = TextMuted
              )
            }
          }

          Text(
            text = "VER TODAS ➔",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryCyan
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        uiState.userTrails.forEach { trail ->
          val subjectColor = try {
            Color(android.graphics.Color.parseColor(trail.subject.colorHex))
          } catch (_: Exception) {
            PrimaryCyan
          }

          Column(modifier = Modifier.padding(vertical = 4.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween,
              verticalAlignment = Alignment.CenterVertically
            ) {
              Text(
                text = "${trail.icon} ${trail.subject.displayName}",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
              )
              Text(
                text = "${trail.progress}% (Nvl ${trail.level})",
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                color = subjectColor
              )
            }
            Spacer(modifier = Modifier.height(4.dp))
            LinearProgressIndicator(
              progress = { trail.progress / 100f },
              modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
              color = subjectColor,
              trackColor = Color(0xFF1E2838)
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Anti-Brain Rot Banner
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .border(1.dp, Color(0xFF22C55E).copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
      Row(
        modifier = Modifier.padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = "🛡️", fontSize = 28.sp, modifier = Modifier.padding(end = 12.dp))
        Column(modifier = Modifier.weight(1f)) {
          Text(
            text = "ESCUDO ANTI-BRAIN ROT ATIVO",
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = Color(0xFF22C55E),
            letterSpacing = 1.sp
          )
          Text(
            text = "Cada missão concluída com pensamento próprio destrói a distração superficial e alimenta seu herói.",
            fontSize = 11.sp,
            color = TextMuted,
            modifier = Modifier.padding(top = 2.dp)
          )
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))
  }
}

// ---------------------------------------------------------
// 2. QUESTS SCREEN (UMA MISSÃO DE CADA MATÉRIA)
// ---------------------------------------------------------

@Composable
private fun QuestsView(
  quests: List<Quest>,
  onStartQuest: (Quest) -> Unit,
  onOpenAiChat: (String?) -> Unit,
  onCompleteQuest: (String) -> Unit
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    HeaderTitle(
      title = "MISSÕES DO REINO",
      subtitle = "UMA JORNADA POR CADA MATÉRIA FUNDAMENTAL"
    )

    quests.forEach { quest ->
      val subjectColor = try {
        Color(android.graphics.Color.parseColor(quest.subject.colorHex))
      } catch (_: Exception) {
        PrimaryCyan
      }

      val tagBgColor = try {
        Color(android.graphics.Color.parseColor(quest.subject.tagBgHex))
      } catch (_: Exception) {
        Color(0xFF00363A)
      }

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 14.dp)
          .border(
            1.5.dp,
            if (quest.isCompleted) Color(0xFF22C55E) else subjectColor.copy(alpha = 0.5f),
            RoundedCornerShape(16.dp)
          ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = tagBgColor,
                border = BorderStroke(1.dp, subjectColor)
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Text(text = quest.subject.icon, fontSize = 12.sp)
                  Spacer(modifier = Modifier.width(4.dp))
                  Text(
                    text = quest.subject.displayName.uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = subjectColor,
                    letterSpacing = 1.sp
                  )
                }
              }

              if (quest.enemLabel != null) {
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = Color(0xFF2E1A47),
                  border = BorderStroke(1.dp, Color(0xFFC084FC))
                ) {
                  Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp)
                  ) {
                    Text(text = "🏛️", fontSize = 10.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                      text = quest.enemLabel!!,
                      fontSize = 10.sp,
                      fontWeight = FontWeight.Black,
                      color = Color(0xFFE9D5FF)
                    )
                  }
                }
              }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
              if (quest.isCompleted) {
                Surface(
                  shape = RoundedCornerShape(6.dp),
                  color = Color(0xFF0E381E),
                  border = BorderStroke(1.dp, Color(0xFF22C55E)),
                  modifier = Modifier.padding(end = 8.dp)
                ) {
                  Text(
                    text = "CONCLUÍDA ✓",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF4ADE80),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                  )
                }
              }

              Text(
                text = "+${quest.xp} XP",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = GoldAccent
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = quest.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
          )

          Text(
            text = quest.description,
            fontSize = 12.sp,
            color = TextMuted,
            lineHeight = 17.sp,
            modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = { onStartQuest(quest) },
              modifier = Modifier
                .weight(1f)
                .height(44.dp)
                .testTag("btn_quest_${quest.id}"),
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = if (quest.isCompleted) Color(0xFF1E293B) else subjectColor.copy(alpha = 0.2f),
                contentColor = if (quest.isCompleted) Color(0xFF4ADE80) else subjectColor
              ),
              border = BorderStroke(1.dp, if (quest.isCompleted) Color(0xFF22C55E) else subjectColor)
            ) {
              Text(
                text = if (quest.isCompleted) "REPETIR COM COPILOTO ✦" else "INICIAR COM COPILOTO ⚔️",
                fontWeight = FontWeight.Black,
                fontSize = 11.sp
              )
            }

            if (!quest.isCompleted) {
              Button(
                onClick = { onCompleteQuest(quest.id) },
                modifier = Modifier
                  .height(44.dp)
                  .testTag("btn_complete_quest_${quest.id}"),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                  containerColor = Color(0xFF122C1A),
                  contentColor = Color(0xFF4ADE80)
                ),
                border = BorderStroke(1.dp, Color(0xFF22C55E))
              ) {
                Text(
                  text = "CONCLUIR ✓",
                  fontWeight = FontWeight.Black,
                  fontSize = 11.sp
                )
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(30.dp))
  }
}

// ---------------------------------------------------------
// 3. GUILDAS SCREEN (RANKING DE ESCOLAS, BUSCA E SISTEMA DE ENTRADA)
// ---------------------------------------------------------

@Composable
private fun GuildasView(
  userGuilda: Guilda?,
  guildasList: List<Guilda>,
  searchQuery: String,
  onSearchChanged: (String) -> Unit,
  onJoinGuilda: (String) -> Unit,
  onLeaveGuilda: () -> Unit,
  onCreateGuilda: (String, String, String, String, String) -> Unit
) {
  var showCreateDialog by remember { mutableStateOf(false) }
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    HeaderTitle(
      title = "LIGAS & GUILDAS ESCOLARES",
      subtitle = "RANKING DE ESCOLAS & UNIÃO DOS ESTUDANTES"
    )

    // User's active guilda status
    if (userGuilda != null) {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 16.dp)
          .border(1.5.dp, userGuilda.accentColor, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            Box(
              modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(userGuilda.accentColor.copy(alpha = 0.2f))
                .border(1.5.dp, userGuilda.accentColor, CircleShape),
              contentAlignment = Alignment.Center
            ) {
              Text(text = userGuilda.emblem, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = "SUA GUILDA ATIVA",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = GoldAccent,
                letterSpacing = 1.sp
              )
              Text(
                text = userGuilda.name,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
              )
              Text(
                text = "${userGuilda.schoolName} • ${userGuilda.city}",
                fontSize = 11.sp,
                color = TextMuted
              )
            }

            Surface(
              shape = RoundedCornerShape(8.dp),
              color = GoldAccent.copy(alpha = 0.2f),
              border = BorderStroke(1.dp, GoldAccent)
            ) {
              Text(
                text = "#${userGuilda.rank} NO RANKING",
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = GoldAccent,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(10.dp))

          Text(
            text = "“${userGuilda.motto}”",
            fontSize = 11.sp,
            color = Color(0xFFCAD5E2),
            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
          )

          Spacer(modifier = Modifier.height(10.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "👥 ${userGuilda.memberCount} Alunos • ⚡ ${userGuilda.totalXp} XP Coletivo",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = PrimaryCyan
            )

            Button(
              onClick = onLeaveGuilda,
              modifier = Modifier.height(34.dp),
              shape = RoundedCornerShape(8.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF261218),
                contentColor = Color(0xFFFF4757)
              ),
              border = BorderStroke(1.dp, Color(0xFFFF4757).copy(alpha = 0.6f))
            ) {
              Text("Sair da Guilda", fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
          }
        }
      }
    } else {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 16.dp)
          .border(1.dp, Color(0xFF2A364F), RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Text(
            text = "🛡️ VOCÊ AINDA NÃO TEM GUILDA",
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            color = Color.White
          )
          Text(
            text = "Filie-se a uma escola no ranking abaixo ou crie a guilda da sua própria escola!",
            fontSize = 11.sp,
            color = TextMuted,
            modifier = Modifier.padding(top = 2.dp)
          )
        }
      }
    }

    // Actions & Search Row
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.spacedBy(8.dp),
      verticalAlignment = Alignment.CenterVertically
    ) {
      OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchChanged,
        placeholder = { Text("Buscar escola, código ou cidade...", color = Color(0xFF5A6A85), fontSize = 12.sp) },
        leadingIcon = {
          Icon(Icons.Default.Search, contentDescription = null, tint = TextMuted, modifier = Modifier.size(18.dp))
        },
        singleLine = true,
        modifier = Modifier
          .weight(1f)
          .testTag("guilda_search_input"),
        shape = RoundedCornerShape(10.dp),
        colors = OutlinedTextFieldDefaults.colors(
          focusedBorderColor = PrimaryCyan,
          unfocusedBorderColor = Color(0xFF222B3D),
          focusedTextColor = Color.White,
          unfocusedTextColor = Color.White
        )
      )

      Button(
        onClick = { showCreateDialog = true },
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
          containerColor = PrimaryCyan,
          contentColor = Color(0xFF090C15)
        ),
        modifier = Modifier
          .height(52.dp)
          .testTag("btn_create_guilda")
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
          Spacer(modifier = Modifier.width(4.dp))
          Text("Criar", fontSize = 12.sp, fontWeight = FontWeight.Black)
        }
      }
    }

    Spacer(modifier = Modifier.height(18.dp))

    Text(
      text = "CLASSIFICAÇÃO GERAL DAS ESCOLAS",
      fontSize = 12.sp,
      fontWeight = FontWeight.Black,
      color = Color.White,
      letterSpacing = 1.sp,
      modifier = Modifier.padding(bottom = 10.dp)
    )

    // Guildas ranking list
    guildasList.forEach { guilda ->
      val isUserGuilda = guilda.id == userGuilda?.id
      val rankBadge = when (guilda.rank) {
        1 -> "🥇 1º LUGAR" to Color(0xFFFFD700)
        2 -> "🥈 2º LUGAR" to Color(0xFFE2E8F0)
        3 -> "🥉 3º LUGAR" to Color(0xFFFFA502)
        else -> "#${guilda.rank} LUGAR" to TextMuted
      }

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp)
          .border(
            1.dp,
            if (isUserGuilda) PrimaryCyan else Color(0xFF222B3D),
            RoundedCornerShape(14.dp)
          ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
      ) {
        Column(modifier = Modifier.padding(14.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(text = guilda.emblem, fontSize = 32.sp)

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = guilda.name,
                fontSize = 15.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
              )
              Text(
                text = "${guilda.schoolName} (${guilda.city})",
                fontSize = 11.sp,
                color = TextMuted,
                modifier = Modifier.padding(top = 1.dp)
              )
            }

            Surface(
              shape = RoundedCornerShape(6.dp),
              color = rankBadge.second.copy(alpha = 0.15f),
              border = BorderStroke(1.dp, rankBadge.second)
            ) {
              Text(
                text = rankBadge.first,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                color = rankBadge.second,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(8.dp))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "⚡ ${guilda.totalXp} XP • 👥 ${guilda.memberCount} Alunos • Cód: ${guilda.code}",
              fontSize = 11.sp,
              color = Color(0xFFCAD5E2)
            )

            if (!isUserGuilda) {
              Button(
                onClick = { onJoinGuilda(guilda.id) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                  containerColor = PrimaryCyan.copy(alpha = 0.15f),
                  contentColor = PrimaryCyan
                ),
                border = BorderStroke(1.dp, PrimaryCyan),
                modifier = Modifier.height(34.dp)
              ) {
                Text("Entrar", fontSize = 10.sp, fontWeight = FontWeight.Black)
              }
            } else {
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFF0F2E1E),
                border = BorderStroke(1.dp, Color(0xFF22C55E))
              ) {
                Text(
                  text = "MEMBRO ✓",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Black,
                  color = Color(0xFF4ADE80),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(30.dp))
  }

  // Create Guilda Dialog
  if (showCreateDialog) {
    CreateGuildaDialog(
      onDismiss = { showCreateDialog = false },
      onConfirm = { name, school, motto, city, emblem ->
        onCreateGuilda(name, school, motto, city, emblem)
        showCreateDialog = false
      }
    )
  }
}

@Composable
private fun CreateGuildaDialog(
  onDismiss: () -> Unit,
  onConfirm: (name: String, schoolName: String, motto: String, city: String, emblem: String) -> Unit
) {
  var name by remember { mutableStateOf("") }
  var schoolName by remember { mutableStateOf("") }
  var city by remember { mutableStateOf("") }
  var motto by remember { mutableStateOf("") }
  var emblem by remember { mutableStateOf("🛡️") }

  val emblemOptions = listOf("🛡️", "🦅", "⚡", "☀️", "🌿", "🔥", "👑", "🚀", "🧠")

  AlertDialog(
    onDismissRequest = onDismiss,
    containerColor = CardBackground,
    title = {
      Text(
        text = "FUNDAR GUILDA ESCOLAR",
        fontSize = 16.sp,
        fontWeight = FontWeight.Black,
        color = Color.White
      )
    },
    text = {
      Column(modifier = Modifier.fillMaxWidth()) {
        Text(
          text = "Escolha o brasão:",
          fontSize = 11.sp,
          color = TextMuted,
          modifier = Modifier.padding(bottom = 6.dp)
        )
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          emblemOptions.take(5).forEach { icon ->
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(if (emblem == icon) PrimaryCyan.copy(alpha = 0.3f) else Color(0xFF0F172A))
                .border(1.dp, if (emblem == icon) PrimaryCyan else Color(0xFF263248), CircleShape)
                .clickable { emblem = icon },
              contentAlignment = Alignment.Center
            ) {
              Text(text = icon, fontSize = 18.sp)
            }
          }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
          value = name,
          onValueChange = { name = it },
          label = { Text("Nome da Guilda", fontSize = 11.sp) },
          placeholder = { Text("Ex: Guardiões de Oxford", fontSize = 11.sp) },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = PrimaryCyan,
            unfocusedBorderColor = Color(0xFF263248)
          )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = schoolName,
          onValueChange = { schoolName = it },
          label = { Text("Nome da Escola", fontSize = 11.sp) },
          placeholder = { Text("Ex: E.E. Santos Dumont", fontSize = 11.sp) },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = PrimaryCyan,
            unfocusedBorderColor = Color(0xFF263248)
          )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = city,
          onValueChange = { city = it },
          label = { Text("Cidade / Estado", fontSize = 11.sp) },
          placeholder = { Text("Ex: Rio de Janeiro - RJ", fontSize = 11.sp) },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = PrimaryCyan,
            unfocusedBorderColor = Color(0xFF263248)
          )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
          value = motto,
          onValueChange = { motto = it },
          label = { Text("Lema da Guilda", fontSize = 11.sp) },
          placeholder = { Text("Ex: O saber vence tudo", fontSize = 11.sp) },
          singleLine = true,
          modifier = Modifier.fillMaxWidth(),
          colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedBorderColor = PrimaryCyan,
            unfocusedBorderColor = Color(0xFF263248)
          )
        )
      }
    },
    confirmButton = {
      Button(
        onClick = { onConfirm(name, schoolName, motto, city, emblem) },
        enabled = name.isNotBlank() && schoolName.isNotBlank(),
        colors = ButtonDefaults.buttonColors(
          containerColor = PrimaryCyan,
          contentColor = Color(0xFF090C15)
        )
      ) {
        Text("Fundar Guilda", fontWeight = FontWeight.Bold)
      }
    },
    dismissButton = {
      Button(
        onClick = onDismiss,
        colors = ButtonDefaults.buttonColors(
          containerColor = Color.Transparent,
          contentColor = TextMuted
        )
      ) {
        Text("Cancelar")
      }
    }
  )
}

// ---------------------------------------------------------
// 4. TRILHAS SCREEN (PROGRESSÃO RELACIONADA ÀS QUESTS)
// ---------------------------------------------------------

@Composable
private fun TrilhasView(
  trails: List<Trail>,
  onGoToSubjectQuest: (QuestSubject) -> Unit,
  onOpenSoloTrial: (QuestSubject) -> Unit = {}
) {
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    HeaderTitle(
      title = "TRILHAS DE CONHECIMENTO",
      subtitle = "PROGRESSÃO CURRICULAR POR MATÉRIA"
    )

    // Trail Quest connection explanation card
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp)
        .border(1.dp, PrimaryCyan.copy(alpha = 0.4f), RoundedCornerShape(14.dp)),
      shape = RoundedCornerShape(14.dp),
      colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
      Row(
        modifier = Modifier.padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = "⚡", fontSize = 28.sp, modifier = Modifier.padding(end = 12.dp))
        Column {
          Text(
            text = "PROGRESSÃO & RITO DE AUTONOMIA",
            fontSize = 11.sp,
            fontWeight = FontWeight.Black,
            color = PrimaryCyan,
            letterSpacing = 1.sp
          )
          Text(
            text = "Ao atingir 100% na trilha, você desbloqueia a Prova Final do ENEM 2025 respondida 100% sozinho, sem nenhuma ajuda de IA!",
            fontSize = 11.sp,
            color = TextMuted,
            lineHeight = 15.sp,
            modifier = Modifier.padding(top = 2.dp)
          )
        }
      }
    }

    trails.forEach { trail ->
      val subjectColor = try {
        Color(android.graphics.Color.parseColor(trail.subject.colorHex))
      } catch (_: Exception) {
        PrimaryCyan
      }

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 14.dp)
          .border(
            if (trail.progress >= 100) 2.dp else 1.dp,
            if (trail.progress >= 100) GoldAccent else subjectColor.copy(alpha = 0.35f),
            RoundedCornerShape(16.dp)
          ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
      ) {
        Column(modifier = Modifier.padding(16.dp)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            Box(
              modifier = Modifier
                .size(46.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(subjectColor.copy(alpha = 0.15f))
                .border(1.dp, subjectColor, RoundedCornerShape(12.dp)),
              contentAlignment = Alignment.Center
            ) {
              Text(text = trail.icon, fontSize = 22.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
              Text(
                text = trail.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
              )
              Text(
                text = "NÍVEL DA TRILHA: ${trail.level} • ${trail.subject.displayName}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = subjectColor,
                letterSpacing = 0.5.sp,
                modifier = Modifier.padding(top = 2.dp)
              )
            }

            Text(
              text = "${trail.progress}%",
              fontSize = 14.sp,
              fontWeight = FontWeight.Black,
              color = if (trail.progress >= 100) GoldAccent else subjectColor
            )
          }

          Spacer(modifier = Modifier.height(12.dp))

          // Progress bar
          LinearProgressIndicator(
            progress = { trail.progress / 100f },
            modifier = Modifier
              .fillMaxWidth()
              .height(8.dp)
              .clip(RoundedCornerShape(4.dp)),
            color = if (trail.progress >= 100) GoldAccent else subjectColor,
            trackColor = Color(0xFF1E2838)
          )

          Spacer(modifier = Modifier.height(12.dp))

          if (trail.progress >= 100) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = Color(0xFF2E1A47),
              border = BorderStroke(1.dp, Color(0xFFC084FC)),
              modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp)
            ) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
              ) {
                Text(text = "👑", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                  Text(
                    text = "PORTAL DO MESTRE DESBLOQUEADO (100%)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFFFDE047)
                  )
                  Text(
                    text = "A voz do Copiloto está selada. Responda sozinho a questão do ENEM 2025 para provar sua autonomia!",
                    fontSize = 10.sp,
                    color = Color(0xFFE9D5FF),
                    lineHeight = 14.sp
                  )
                }
              }
            }

            Button(
              onClick = { onOpenSoloTrial(trail.subject) },
              modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("solo_trial_button_${trail.subject.name.lowercase()}"),
              shape = RoundedCornerShape(10.dp),
              colors = ButtonDefaults.buttonColors(
                containerColor = GoldAccent,
                contentColor = Color.Black
              ),
              border = BorderStroke(1.dp, Color.White)
            ) {
              Text(
                text = "⚔️ ENCARAR PROVA FINAL SOLO (SEM IA) 🏛️",
                fontWeight = FontWeight.Black,
                fontSize = 11.sp,
                letterSpacing = 0.5.sp
              )
            }
          } else {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Button(
                onClick = { onGoToSubjectQuest(trail.subject) },
                modifier = Modifier
                  .weight(1f)
                  .height(38.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                  containerColor = subjectColor.copy(alpha = 0.12f),
                  contentColor = subjectColor
                ),
                border = BorderStroke(1.dp, subjectColor.copy(alpha = 0.6f))
              ) {
                Text(
                  text = "PRATICAR NESTA TRILHA ⚔️",
                  fontWeight = FontWeight.Black,
                  fontSize = 10.sp,
                  letterSpacing = 0.5.sp
                )
              }

              Button(
                onClick = { onOpenSoloTrial(trail.subject) },
                modifier = Modifier.height(38.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                  containerColor = Color(0xFF2E1A47),
                  contentColor = Color(0xFFE9D5FF)
                ),
                border = BorderStroke(1.dp, Color(0xFFC084FC))
              ) {
                Text(
                  text = "PROVA SOLO 🏛️",
                  fontWeight = FontWeight.Black,
                  fontSize = 10.sp,
                  letterSpacing = 0.5.sp
                )
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(30.dp))
  }
}

// ---------------------------------------------------------
// 5. HEROIS SCREEN (CUSTOMIZAÇÃO 3D DO BONECO + MURAL DOS FUNDADORES)
// ---------------------------------------------------------

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HeroisView(
  customization: UserAvatarCustomization,
  hero: Hero,
  displayName: String,
  onUpdateModel3d: (String) -> Unit
) {
  var showSavedBadge by remember { mutableStateOf(false) }
  val scrollState = rememberScrollState()

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(scrollState)
      .padding(horizontal = 16.dp, vertical = 12.dp)
  ) {
    HeaderTitle(
      title = "SALA DOS HERÓIS & FUNDADORES",
      subtitle = "ESCOLHA SEU PERSONAGEM & HONRE OS CRIADORES"
    )

    // SECTION 1: VISUALIZAÇÃO DO HERÓI ATUAL DO ALUNO
    Card(
      modifier = Modifier
        .fillMaxWidth()
        .border(1.5.dp, PrimaryCyan, RoundedCornerShape(20.dp)),
      shape = RoundedCornerShape(20.dp),
      colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
      Column(
        modifier = Modifier.padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.Center
        ) {
          Text(
            text = "✦ SEU PERSONAGEM ATIVO ✦",
            fontSize = 12.sp,
            fontWeight = FontWeight.Black,
            color = PrimaryCyan,
            letterSpacing = 1.sp
          )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Large Character Display (Pure avatar, no hats or editable clutter)
        UserAvatarDoll(
          customization = customization,
          size = 150
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
          text = displayName,
          fontSize = 20.sp,
          fontWeight = FontWeight.Black,
          color = Color.White
        )

        Text(
          text = customization.title.uppercase(),
          fontSize = 11.sp,
          fontWeight = FontWeight.Bold,
          color = GoldAccent,
          letterSpacing = 1.sp,
          modifier = Modifier.padding(top = 2.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Stats summary row
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          StatItem(label = "NÍVEL", value = "${hero.level}", valueColor = PrimaryCyan, modifier = Modifier.weight(1f))
          StatItem(label = "HP", value = "${hero.hp}", valueColor = Color(0xFFFF4757), modifier = Modifier.weight(1f))
          StatItem(label = "MP", value = "${hero.mp}", valueColor = Color(0xFF2ED573), modifier = Modifier.weight(1f))
          StatItem(label = "XP", value = "${hero.xp}", valueColor = GoldAccent, modifier = Modifier.weight(1f))
        }

        AnimatedVisibility(visible = showSavedBadge) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF0F301F),
            border = BorderStroke(1.dp, Color(0xFF22C55E)),
            modifier = Modifier.padding(top = 14.dp)
          ) {
            Text(
              text = "Personagem Selecionado com Sucesso! ✨",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color(0xFF4ADE80),
              modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
            )
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(24.dp))

    // SECTION 2: SELETOR DE PERSONAGENS PARA O USUÁRIO
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(text = "🎮", fontSize = 24.sp, modifier = Modifier.padding(end = 8.dp))
      Column {
        Text(
          text = "ESCOLHA SEU PERSONAGEM",
          fontSize = 14.sp,
          fontWeight = FontWeight.Black,
          color = Color.White,
          letterSpacing = 1.sp
        )
        Text(
          text = "SELECIONE SEU HERÓI ENTRE OS GUERREIROS DO REINO",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = PrimaryCyan,
          letterSpacing = 0.5.sp
        )
      }
    }

    Text(
      text = "Os personagens abaixo são exclusivos para a sua escolha. Os heróis dos criadores são sagrados e inacessíveis para jogadores.",
      fontSize = 11.sp,
      color = TextMuted,
      lineHeight = 15.sp,
      modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
    )

    // Lista de Personagens 3D Disponíveis para o Usuário Escolher
    QuestIaData.AVATAR_3D_MODELS.forEach { model ->
      val isSelected = model.id == customization.model3dId
      val modelColor = try {
        Color(android.graphics.Color.parseColor(model.accentHex))
      } catch (_: Exception) {
        PrimaryCyan
      }

      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(bottom = 12.dp)
          .border(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) modelColor else Color(0xFF222B3D),
            shape = RoundedCornerShape(16.dp)
          )
          .clickable {
            onUpdateModel3d(model.id)
            showSavedBadge = true
          }
          .testTag("char_select_card_${model.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
          containerColor = if (isSelected) Color(0xFF141C2E) else CardBackground
        )
      ) {
        Row(
          modifier = Modifier.padding(14.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          // 3D Avatar Thumbnail
          Box(
            modifier = Modifier
              .size(76.dp)
              .clip(RoundedCornerShape(12.dp))
              .border(1.5.dp, if (isSelected) modelColor else Color(0xFF2E384D), RoundedCornerShape(12.dp))
          ) {
            Image(
              painter = painterResource(id = model.imageRes),
              contentDescription = model.name,
              contentScale = ContentScale.Crop,
              modifier = Modifier.fillMaxSize()
            )

            if (isSelected) {
              Surface(
                shape = RoundedCornerShape(bottomEnd = 6.dp),
                color = modelColor,
                modifier = Modifier.align(Alignment.TopStart)
              ) {
                Text(
                  text = "ATIVO",
                  fontSize = 8.sp,
                  fontWeight = FontWeight.Black,
                  color = Color(0xFF090C15),
                  modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
              }
            }
          }

          Spacer(modifier = Modifier.width(14.dp))

          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = model.name,
              fontSize = 15.sp,
              fontWeight = FontWeight.Black,
              color = Color.White
            )

            Text(
              text = model.archetype.uppercase(),
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = modelColor,
              letterSpacing = 0.5.sp,
              modifier = Modifier.padding(top = 2.dp)
            )

            Text(
              text = model.description,
              fontSize = 11.sp,
              color = TextMuted,
              lineHeight = 14.sp,
              modifier = Modifier.padding(top = 4.dp, bottom = 6.dp)
            )

            if (isSelected) {
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFF0F301F),
                border = BorderStroke(1.dp, Color(0xFF22C55E))
              ) {
                Row(
                  verticalAlignment = Alignment.CenterVertically,
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                  Text(text = "EM USO ✓", fontSize = 10.sp, fontWeight = FontWeight.Black, color = Color(0xFF4ADE80))
                }
              }
            } else {
              Button(
                onClick = {
                  onUpdateModel3d(model.id)
                  showSavedBadge = true
                },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                  containerColor = modelColor.copy(alpha = 0.15f),
                  contentColor = modelColor
                ),
                border = BorderStroke(1.dp, modelColor),
                modifier = Modifier
                  .height(34.dp)
                  .testTag("btn_select_character_${model.id}")
              ) {
                Text("Escolher Herói ✦", fontSize = 10.sp, fontWeight = FontWeight.Black)
              }
            }
          }
        }
      }
    }

    Spacer(modifier = Modifier.height(28.dp))

    // SECTION 3: MURAL DOS FUNDADORES (CRIADORES SAGRADOS - INACESSÍVEIS)
    Row(
      verticalAlignment = Alignment.CenterVertically,
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(text = "🏛️", fontSize = 24.sp, modifier = Modifier.padding(end = 8.dp))
      Column {
        Text(
          text = "MURAL DOS FUNDADORES (RPG ÉPICO)",
          fontSize = 14.sp,
          fontWeight = FontWeight.Black,
          color = Color.White,
          letterSpacing = 1.sp
        )
        Text(
          text = "OS CRIADORES DO REINO (HERÓIS INACESSÍVEIS PARA JOGADORES 🔒)",
          fontSize = 10.sp,
          fontWeight = FontWeight.Bold,
          color = Color(0xFFFF6B81),
          letterSpacing = 0.5.sp
        )
      }
    }

    // Inaccessible lock notice box
    Surface(
      shape = RoundedCornerShape(10.dp),
      color = Color(0xFF1F0E13),
      border = BorderStroke(1.dp, Color(0xFFFF4757).copy(alpha = 0.5f)),
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 10.dp)
    ) {
      Row(
        modifier = Modifier.padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Text(text = "🔒", fontSize = 20.sp, modifier = Modifier.padding(end = 10.dp))
        Text(
          text = "Estes personagens são os criadores históricos de QuestIA. Eles são preservados para honra e inspiração, e permanecem estritamente inacessíveis para seleção por jogadores.",
          fontSize = 11.sp,
          color = Color(0xFFF1B0B7),
          lineHeight = 15.sp
        )
      }
    }

    Spacer(modifier = Modifier.height(6.dp))

    QuestIaData.FOUNDERS.forEach { founder ->
      FounderDollCard(
        founder = founder,
        modifier = Modifier.padding(bottom = 14.dp)
      )
    }

    Spacer(modifier = Modifier.height(30.dp))
  }
}

// ---------------------------------------------------------
// COMMON COMPONENTS & NAVIGATION BAR
// ---------------------------------------------------------

@Composable
private fun QuestIaBottomNavigation(
  activeTab: QuestIaTab,
  onTabSelected: (QuestIaTab) -> Unit
) {
  val tabs = listOf(
    QuestIaTab.HOME to ("Home" to Icons.Default.Home),
    QuestIaTab.QUESTS to ("Quests" to Icons.Default.Shield),
    QuestIaTab.GUILDAS to ("Guildas" to Icons.Default.School),
    QuestIaTab.TRILHAS to ("Trilhas" to Icons.Default.AutoStories),
    QuestIaTab.HEROIS to ("Heróis" to Icons.Default.Person)
  )

  Surface(
    color = Color(0xFF0C101A),
    border = BorderStroke(1.dp, Color(0xFF1B2436)),
    modifier = Modifier
      .fillMaxWidth()
      .navigationBarsPadding()
  ) {
    Row(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 6.dp),
      horizontalArrangement = Arrangement.SpaceAround,
      verticalAlignment = Alignment.CenterVertically
    ) {
      tabs.forEach { (tab, info) ->
        val isSelected = activeTab == tab
        val tabColor by animateColorAsState(
          targetValue = if (isSelected) PrimaryCyan else TextMuted,
          label = "tab_color"
        )

        Column(
          horizontalAlignment = Alignment.CenterHorizontally,
          modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onTabSelected(tab) }
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .testTag("tab_${tab.name.lowercase()}")
        ) {
          Icon(
            imageVector = info.second,
            contentDescription = info.first,
            tint = tabColor,
            modifier = Modifier.size(22.dp)
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = info.first,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
            color = tabColor
          )
        }
      }
    }
  }
}

@Composable
private fun HeaderTitle(
  title: String,
  subtitle: String
) {
  Column(
    modifier = Modifier
      .fillMaxWidth()
      .padding(vertical = 12.dp)
  ) {
    Text(
      text = title,
      fontSize = 20.sp,
      fontWeight = FontWeight.Black,
      color = Color.White,
      letterSpacing = 2.sp
    )
    Text(
      text = subtitle,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      color = PrimaryCyan,
      letterSpacing = 1.sp,
      modifier = Modifier.padding(top = 2.dp)
    )
  }
}

@Composable
private fun StatItem(
  label: String,
  value: String,
  valueColor: Color,
  modifier: Modifier = Modifier
) {
  Surface(
    shape = RoundedCornerShape(10.dp),
    color = Color(0xFF0D121F),
    border = BorderStroke(1.dp, Color(0xFF1D2638)),
    modifier = modifier
  ) {
    Column(
      modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Text(
        text = label,
        fontSize = 8.sp,
        fontWeight = FontWeight.Bold,
        color = TextMuted,
        letterSpacing = 0.5.sp,
        textAlign = TextAlign.Center
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = value,
        fontSize = 12.sp,
        fontWeight = FontWeight.Black,
        color = valueColor,
        textAlign = TextAlign.Center
      )
    }
  }
}
