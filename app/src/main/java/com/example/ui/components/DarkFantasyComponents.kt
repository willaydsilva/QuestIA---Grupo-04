package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.CharacterProfile
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

@Composable
fun DarkFantasyCard(
  modifier: Modifier = Modifier,
  borderColor: Color = QuestiaCardBorder,
  backgroundColor: Color = QuestiaSurface,
  content: @Composable () -> Unit
) {
  Card(
    modifier = modifier
      .border(
        width = 1.5.dp,
        brush = Brush.linearGradient(
          colors = listOf(
            borderColor,
            borderColor.copy(alpha = 0.4f),
            borderColor
          )
        ),
        shape = RoundedCornerShape(14.dp)
      ),
    colors = CardDefaults.cardColors(
      containerColor = backgroundColor
    ),
    shape = RoundedCornerShape(14.dp),
    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
  ) {
    content()
  }
}

@Composable
fun GoldenRpgButton(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier,
  icon: ImageVector? = null,
  enabled: Boolean = true,
  testTag: String = "golden_button"
) {
  Button(
    onClick = onClick,
    enabled = enabled,
    modifier = modifier
      .height(54.dp)
      .testTag(testTag),
    shape = RoundedCornerShape(12.dp),
    colors = ButtonDefaults.buttonColors(
      containerColor = Color.Transparent,
      contentColor = TextDark
    ),
    contentPadding = PaddingValues(0.dp),
    border = BorderStroke(1.5.dp, GoldGlow)
  ) {
    Box(
      modifier = Modifier
        .fillMaxWidth()
        .height(54.dp)
        .background(
          brush = Brush.horizontalGradient(
            colors = listOf(
              Color(0xFFD97706),
              GoldPrimary,
              GoldBright,
              GoldPrimary,
              Color(0xFFD97706)
            )
          )
        ),
      contentAlignment = Alignment.Center
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.padding(horizontal = 20.dp)
      ) {
        if (icon != null) {
          Icon(
            imageVector = icon,
            contentDescription = null,
            tint = TextDark,
            modifier = Modifier.size(22.dp)
          )
          Spacer(modifier = Modifier.width(10.dp))
        }
        Text(
          text = text,
          color = TextDark,
          fontWeight = FontWeight.ExtraBold,
          fontSize = 16.sp,
          letterSpacing = 1.2.sp
        )
      }
    }
  }
}

@Composable
fun ReinoHeader(
  modifier: Modifier = Modifier
) {
  Column(
    modifier = modifier
      .fillMaxWidth()
      .padding(vertical = 12.dp),
    horizontalAlignment = Alignment.CenterHorizontally
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.Center
    ) {
      Icon(
        imageVector = Icons.Default.AutoAwesome,
        contentDescription = null,
        tint = GoldPrimary,
        modifier = Modifier.size(20.dp)
      )
      Spacer(modifier = Modifier.width(8.dp))
      Text(
        text = "REINO DE QUESTIA",
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Black,
          fontSize = 22.sp,
          letterSpacing = 3.sp
        ),
        color = GoldBright
      )
      Spacer(modifier = Modifier.width(8.dp))
      Icon(
        imageVector = Icons.Default.AutoAwesome,
        contentDescription = null,
        tint = GoldPrimary,
        modifier = Modifier.size(20.dp)
      )
    }
    Spacer(modifier = Modifier.height(4.dp))
    Text(
      text = "✦ Baluarte Contra o Brain Rot & Névoa da Ilusão ✦",
      style = MaterialTheme.typography.labelSmall.copy(
        fontSize = 11.sp,
        letterSpacing = 1.sp
      ),
      color = TextMuted
    )
  }
}

@Composable
fun HeroProfileCard(
  profile: CharacterProfile,
  modifier: Modifier = Modifier
) {
  DarkFantasyCard(
    modifier = modifier.fillMaxWidth(),
    borderColor = GoldPrimary
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      Row(
        verticalAlignment = Alignment.CenterVertically
      ) {
        // Hero Avatar with glowing gold border
        Box(
          modifier = Modifier
            .size(76.dp)
            .border(2.dp, GoldPrimary, CircleShape)
            .padding(3.dp)
            .clip(CircleShape)
            .background(QuestiaDeepNavy)
        ) {
          Image(
            painter = painterResource(id = R.drawable.hero_avatar),
            contentDescription = "Avatar do Herói",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
          )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = profile.name,
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
              ),
              color = TextLight
            )
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = GoldDark.copy(alpha = 0.7f),
              border = BorderStroke(1.dp, GoldPrimary)
            ) {
              Text(
                text = "Nível ${profile.level}",
                color = GoldBright,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
              )
            }
          }

          Spacer(modifier = Modifier.height(2.dp))

          Text(
            text = profile.title,
            style = MaterialTheme.typography.bodySmall,
            color = GoldGlow
          )

          Spacer(modifier = Modifier.height(8.dp))

          // XP Progress Bar
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = "XP",
              color = TextMuted,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(6.dp))
            val xpProgress by animateFloatAsState(
              targetValue = profile.currentXp.toFloat() / profile.maxXp.toFloat(),
              label = "xp_anim"
            )
            LinearProgressIndicator(
              progress = { xpProgress },
              modifier = Modifier
                .weight(1f)
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp)),
              color = GoldPrimary,
              trackColor = QuestiaDeepNavy
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "${profile.currentXp}/${profile.maxXp}",
              color = TextMuted,
              fontSize = 10.sp
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Anti-Brain Rot Resilience Banner
      Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        color = QuestiaDeepNavy,
        border = BorderStroke(1.dp, QuestiaCardBorder.copy(alpha = 0.5f))
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.Shield,
              contentDescription = null,
              tint = ArcaneMana,
              modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Resistência Anti-Brain Rot",
              color = TextLight,
              fontSize = 11.sp
            )
          }
          Text(
            text = "${profile.brainRotResistance}% Foco Racional",
            color = ArcaneMana,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp
          )
        }
      }
    }
  }
}

@Composable
fun CharacterStatsGrid(
  profile: CharacterProfile,
  modifier: Modifier = Modifier
) {
  Row(
    modifier = modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.spacedBy(10.dp)
  ) {
    // Stat 1: Vida 100/100
    StatBox(
      title = "Vida",
      value = profile.formattedHp,
      icon = Icons.Default.Favorite,
      accentColor = RubyHealth,
      modifier = Modifier.weight(1f)
    )

    // Stat 2: Energia 80/80
    StatBox(
      title = "Energia",
      value = profile.formattedEnergy,
      icon = Icons.Default.Bolt,
      accentColor = ArcaneMana,
      modifier = Modifier.weight(1f)
    )

    // Stat 3: Ouro 100
    StatBox(
      title = "Ouro",
      value = profile.formattedGold,
      icon = Icons.Default.MonetizationOn,
      accentColor = AmberGold,
      modifier = Modifier.weight(1f)
    )
  }
}

@Composable
private fun StatBox(
  title: String,
  value: String,
  icon: ImageVector,
  accentColor: Color,
  modifier: Modifier = Modifier
) {
  DarkFantasyCard(
    modifier = modifier,
    borderColor = accentColor.copy(alpha = 0.7f),
    backgroundColor = QuestiaSurfaceVariant
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 12.dp, horizontal = 8.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Box(
        modifier = Modifier
          .size(34.dp)
          .clip(CircleShape)
          .background(accentColor.copy(alpha = 0.15f)),
        contentAlignment = Alignment.Center
      ) {
        Icon(
          imageVector = icon,
          contentDescription = title,
          tint = accentColor,
          modifier = Modifier.size(18.dp)
        )
      }
      Spacer(modifier = Modifier.height(6.dp))
      Text(
        text = title.uppercase(),
        fontSize = 10.sp,
        fontWeight = FontWeight.SemiBold,
        letterSpacing = 1.sp,
        color = TextMuted
      )
      Spacer(modifier = Modifier.height(2.dp))
      Text(
        text = value,
        fontSize = 14.sp,
        fontWeight = FontWeight.Black,
        color = TextLight
      )
    }
  }
}

@Composable
fun MissionCard(
  onFaceChallengeClick: () -> Unit,
  questCompleted: Boolean,
  modifier: Modifier = Modifier
) {
  DarkFantasyCard(
    modifier = modifier.fillMaxWidth(),
    borderColor = GoldBright
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp)
    ) {
      // Mission Header Tag
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Surface(
          shape = RoundedCornerShape(6.dp),
          color = GoldDark,
          border = BorderStroke(1.dp, GoldPrimary)
        ) {
          Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = GoldBright,
              modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
              text = "MISSÃO DO DIA",
              color = GoldBright,
              fontSize = 11.sp,
              fontWeight = FontWeight.Black,
              letterSpacing = 1.sp
            )
          }
        }

        Surface(
          shape = RoundedCornerShape(6.dp),
          color = if (questCompleted) Color(0xFF14532D) else QuestiaDeepNavy,
          border = BorderStroke(1.dp, if (questCompleted) Color(0xFF22C55E) else QuestiaCardBorder)
        ) {
          Text(
            text = if (questCompleted) "✓ CONCLUÍDA" else "DIFICULDADE: MÉDIA",
            color = if (questCompleted) Color(0xFF86EFAC) else TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // Mission Title
      Text(
        text = "O Enigma da Equação",
        style = MaterialTheme.typography.titleLarge.copy(
          fontWeight = FontWeight.Black,
          fontSize = 20.sp
        ),
        color = TextLight
      )

      Spacer(modifier = Modifier.height(6.dp))

      Text(
        text = "A temível Máquina da Ilusão trancou as portas do templo racional. Para dissipar o torpor mental, decifre a equação quadrática talhada na pedra antiga.",
        style = MaterialTheme.typography.bodySmall,
        color = TextMuted,
        lineHeight = 18.sp
      )

      Spacer(modifier = Modifier.height(12.dp))

      // Portal Illustration banner with equation preview
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(130.dp)
          .clip(RoundedCornerShape(10.dp))
          .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
      ) {
        Image(
          painter = painterResource(id = R.drawable.mission_portal),
          contentDescription = "Portal Rúnico da Equação",
          modifier = Modifier.fillMaxWidth(),
          contentScale = ContentScale.Crop
        )

        // Overlay with gradient and glowing equation
        Box(
          modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color.Transparent,
                  Color.Black.copy(alpha = 0.85f)
                )
              )
            )
            .padding(12.dp),
          contentAlignment = Alignment.BottomCenter
        ) {
          Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFF090D18).copy(alpha = 0.9f),
            border = BorderStroke(1.dp, GoldGlow)
          ) {
            Text(
              text = "x² + 4x - 11 = 0",
              color = GoldBright,
              fontSize = 18.sp,
              fontWeight = FontWeight.ExtraBold,
              letterSpacing = 2.sp,
              fontFamily = FontFamily.Monospace,
              modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
          }
        }
      }

      Spacer(modifier = Modifier.height(12.dp))

      // Rewards Row
      Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
          RewardBadge(label = "+250 XP", color = GoldPrimary)
          RewardBadge(label = "+50 Ouro", color = AmberGold)
          RewardBadge(label = "+5 Sabedoria", color = ArcaneMana)
        }
      }

      Spacer(modifier = Modifier.height(16.dp))

      // Gold Action Button: 'ENFRENTAR DESAFIO'
      GoldenRpgButton(
        text = "ENFRENTAR DESAFIO",
        onClick = onFaceChallengeClick,
        icon = Icons.Default.Bolt,
        modifier = Modifier.fillMaxWidth(),
        testTag = "face_challenge_button"
      )
    }
  }
}

@Composable
private fun RewardBadge(label: String, color: Color) {
  Surface(
    shape = RoundedCornerShape(6.dp),
    color = QuestiaDeepNavy,
    border = BorderStroke(1.dp, color.copy(alpha = 0.4f))
  ) {
    Text(
      text = label,
      color = color,
      fontSize = 11.sp,
      fontWeight = FontWeight.Bold,
      modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    )
  }
}
