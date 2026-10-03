package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Avatar3dModel
import com.example.model.Founder
import com.example.model.QuestIaData
import com.example.model.UserAvatarCustomization

@Composable
fun UserAvatarDoll(
  customization: UserAvatarCustomization,
  modifier: Modifier = Modifier,
  size: Int = 180,
  sizeDp: Int = size,
  showTitleBadge: Boolean = true
) {
  val model3d = QuestIaData.AVATAR_3D_MODELS.firstOrNull { it.id == customization.model3dId }
    ?: QuestIaData.AVATAR_3D_MODELS.first()

  val modelColor = try {
    Color(android.graphics.Color.parseColor(model3d.accentHex))
  } catch (_: Exception) {
    Color(0xFF00E5FF)
  }

  val infiniteTransition = rememberInfiniteTransition(label = "pulse")
  val pulseGlow by infiniteTransition.animateFloat(
    initialValue = 0.5f,
    targetValue = 0.85f,
    animationSpec = infiniteRepeatable(
      animation = tween(2200, easing = FastOutSlowInEasing),
      repeatMode = RepeatMode.Reverse
    ),
    label = "glowAlpha"
  )

  Box(
    modifier = modifier.size(sizeDp.dp),
    contentAlignment = Alignment.Center
  ) {
    // Ambient aura radial background
    Box(
      modifier = Modifier
        .size((sizeDp * 0.95).dp)
        .clip(CircleShape)
        .background(
          Brush.radialGradient(
            colors = listOf(
              modelColor.copy(alpha = pulseGlow * 0.35f),
              Color(0xFF080C16).copy(alpha = 0.8f),
              Color.Transparent
            )
          )
        )
    )

    // RPG Character Frame
    Surface(
      shape = RoundedCornerShape((sizeDp * 0.16).dp),
      color = Color(0xFF0C101A),
      border = BorderStroke(2.dp, modelColor.copy(alpha = pulseGlow)),
      shadowElevation = 10.dp,
      modifier = Modifier
        .size((sizeDp * 0.88).dp)
        .shadow(
          elevation = 12.dp,
          shape = RoundedCornerShape((sizeDp * 0.16).dp),
          ambientColor = modelColor,
          spotColor = modelColor
        )
    ) {
      Box(modifier = Modifier.fillMaxSize()) {
        Image(
          painter = painterResource(id = model3d.imageRes),
          contentDescription = model3d.name,
          contentScale = ContentScale.Crop,
          modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape((sizeDp * 0.16).dp))
        )

        // Subtle gradient overlay for contrast only when title badge is shown
        if (showTitleBadge) {
          Box(
            modifier = Modifier
              .fillMaxSize()
              .background(
                Brush.verticalGradient(
                  colors = listOf(
                    Color.Transparent,
                    Color.Transparent,
                    Color(0xFF050811).copy(alpha = 0.85f)
                  )
                )
              )
          )
        }
      }
    }

    // RPG Archetype Badge
    if (showTitleBadge) {
      Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF050811).copy(alpha = 0.92f),
        border = BorderStroke(1.dp, modelColor),
        modifier = Modifier
          .align(Alignment.BottomCenter)
          .padding(bottom = 2.dp)
      ) {
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
        ) {
          Text(text = "✦", fontSize = 9.sp, color = modelColor)
          Spacer(modifier = Modifier.width(4.dp))
          Text(
            text = model3d.name.uppercase(),
            fontSize = 9.sp,
            fontWeight = FontWeight.Black,
            color = Color.White,
            letterSpacing = 0.5.sp
          )
        }
      }

      Surface(
        modifier = Modifier
          .align(Alignment.TopEnd)
          .padding(end = 4.dp, top = 4.dp),
        shape = RoundedCornerShape(6.dp),
        color = Color(0xFF0A0F1D).copy(alpha = 0.85f),
        border = BorderStroke(1.dp, modelColor.copy(alpha = 0.5f))
      ) {
        Text(
          text = model3d.archetype,
          fontSize = 8.sp,
          fontWeight = FontWeight.Bold,
          color = Color.White,
          maxLines = 1,
          modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
      }
    }
  }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FounderDollCard(
  founder: Founder,
  modifier: Modifier = Modifier
) {
  val accent = founder.accentColor

  Surface(
    modifier = modifier.fillMaxWidth(),
    shape = RoundedCornerShape(20.dp),
    color = Color(0xFF0F1523),
    border = BorderStroke(1.5.dp, accent.copy(alpha = 0.6f))
  ) {
    Column(modifier = Modifier.padding(16.dp)) {
      // 1. HEADER: FOTO DO CRIADOR COM NOME, PROFISSÃO E BADGE DE INACESSÍVEL
      Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
      ) {
        Box(
          modifier = Modifier
            .size(92.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, accent, RoundedCornerShape(16.dp))
        ) {
          Image(
            painter = painterResource(id = founder.imageRes),
            contentDescription = founder.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
          )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
          ) {
            Text(
              text = founder.name,
              fontSize = 20.sp,
              fontWeight = FontWeight.Black,
              color = Color.White
            )

            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFF38141B),
              border = BorderStroke(1.dp, Color(0xFFFF4757))
            ) {
              Text(
                text = "CRIADOR",
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                color = Color(0xFFFF6B81),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          // Role and Profession Chips
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(top = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = accent.copy(alpha = 0.15f),
              border = BorderStroke(1.dp, accent.copy(alpha = 0.8f))
            ) {
              Text(
                text = founder.role.uppercase(),
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                color = accent,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
              )
            }

            Surface(
              shape = RoundedCornerShape(6.dp),
              color = Color(0xFF1E283D),
              border = BorderStroke(1.dp, Color(0xFF354460))
            ) {
              Text(
                text = founder.profession.uppercase(),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFCAD5E2),
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
              )
            }
          }

          // Dedicated non-intrusive status: character is exclusive and unavailable for selection
          Surface(
            shape = RoundedCornerShape(6.dp),
            color = Color(0xFF281119),
            border = BorderStroke(1.dp, Color(0xFFFF4757).copy(alpha = 0.45f)),
            modifier = Modifier.padding(top = 6.dp)
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
            ) {
              Text(text = "🔒", fontSize = 9.sp)
              Spacer(modifier = Modifier.width(4.dp))
              Text(
                text = "Personagem Exclusivo • Indisponível para seleção",
                fontSize = 8.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFF9BA9),
                letterSpacing = 0.3.sp
              )
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(14.dp))

      // 2. DESCRIÇÃO / SOBRE O MENTOR (BOX ESTRUTURADO COM ÍCONE)
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF090E18),
        border = BorderStroke(1.dp, Color(0xFF1E283D)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "📖", fontSize = 12.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "SOBRE O MENTOR",
              fontSize = 9.sp,
              fontWeight = FontWeight.Black,
              color = accent,
              letterSpacing = 1.sp
            )
          }
          Text(
            text = founder.personalityDesc,
            fontSize = 11.sp,
            color = Color(0xFFCBD5E1),
            lineHeight = 16.sp,
            modifier = Modifier.padding(top = 6.dp)
          )
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // 3. RELÍQUIA E HABILIDADES ESPECIAIS (ORGANIZADAS VISUALMENTE)
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF090E18),
        border = BorderStroke(1.dp, Color(0xFF1E283D)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Column(modifier = Modifier.padding(12.dp)) {
          // Relic highlight
          Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
          ) {
            Text(text = "🛡️", fontSize = 13.sp)
            Spacer(modifier = Modifier.width(6.dp))
            Text(
              text = "Relíquia Sagrada: ",
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              color = Color.White
            )
            Text(
              text = founder.relic,
              fontSize = 11.sp,
              fontWeight = FontWeight.Black,
              color = accent
            )
          }

          // Special skills chips flow
          FlowRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
          ) {
            founder.specialSkills.forEach { skill ->
              Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFF162135),
                border = BorderStroke(1.dp, Color(0xFF2E3E5C))
              ) {
                Text(
                  text = "✦ $skill",
                  fontSize = 10.sp,
                  fontWeight = FontWeight.Medium,
                  color = Color(0xFFA5B8D0),
                  modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                )
              }
            }
          }
        }
      }

      Spacer(modifier = Modifier.height(10.dp))

      // 4. CITAÇÃO DO CRIADOR (ESTILO PERGAMINHO DOURADO)
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF171915).copy(alpha = 0.6f),
        border = BorderStroke(1.dp, Color(0xFFFFA502).copy(alpha = 0.45f)),
        modifier = Modifier.fillMaxWidth()
      ) {
        Row(
          modifier = Modifier.padding(12.dp),
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = "📜",
            fontSize = 16.sp,
            modifier = Modifier.padding(end = 8.dp)
          )
          Text(
            text = "“${founder.quote.replace("“", "").replace("”", "")}”",
            fontSize = 11.sp,
            color = Color(0xFFFFD166),
            fontStyle = FontStyle.Italic,
            lineHeight = 15.sp,
            modifier = Modifier.weight(1f)
          )
        }
      }
    }
  }
}
