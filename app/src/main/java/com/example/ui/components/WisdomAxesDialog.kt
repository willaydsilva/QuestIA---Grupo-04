package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.model.WisdomAxes
import com.example.ui.theme.AmberGold
import com.example.ui.theme.ArcaneMana
import com.example.ui.theme.GoldBright
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.QuestiaCardBorder
import com.example.ui.theme.QuestiaDarkBg
import com.example.ui.theme.QuestiaDeepNavy
import com.example.ui.theme.QuestiaSurface
import com.example.ui.theme.QuestiaSurfaceVariant
import com.example.ui.theme.RubyHealth
import com.example.ui.theme.TextDark
import com.example.ui.theme.TextLight
import com.example.ui.theme.TextMuted

@Composable
fun WisdomAxesDialog(
  wisdom: WisdomAxes,
  onDismiss: () -> Unit
) {
  Dialog(onDismissRequest = onDismiss) {
    DarkFantasyCard(
      modifier = Modifier
        .fillMaxWidth()
        .padding(vertical = 16.dp),
      borderColor = GoldPrimary,
      backgroundColor = QuestiaDarkBg
    ) {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(18.dp)
          .verticalScroll(rememberScrollState())
      ) {
        // Header
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
              imageVector = Icons.Default.AutoAwesome,
              contentDescription = null,
              tint = GoldBright,
              modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
              text = "EIXOS DA SABEDORIA",
              style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp
              ),
              color = GoldBright
            )
          }

          IconButton(
            onClick = onDismiss,
            modifier = Modifier.size(28.dp)
          ) {
            Icon(
              imageVector = Icons.Default.Close,
              contentDescription = "Fechar",
              tint = TextMuted
            )
          }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
          text = "Pilares de avaliação do Copiloto Socrático contra o Brain Rot",
          style = MaterialTheme.typography.bodySmall,
          color = TextMuted,
          fontSize = 11.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Overall Rank Pill
        Surface(
          shape = RoundedCornerShape(10.dp),
          color = QuestiaDeepNavy,
          border = BorderStroke(1.dp, QuestiaCardBorder)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(
                text = "STATUS DO RACIOCÍNIO",
                color = TextMuted,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
              )
              Text(
                text = wisdom.rankTitle,
                color = GoldPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Black
              )
            }
            Text(
              text = "${wisdom.totalScore} pts",
              color = GoldBright,
              fontSize = 16.sp,
              fontWeight = FontWeight.Black
            )
          }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Axis 1: Acerto
        AxisRow(
          title = "Acerto",
          subtitle = "Precisão e rigor nos cálculos matemáticos",
          score = wisdom.acerto,
          icon = Icons.Default.CheckCircle,
          color = RubyHealth
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Axis 2: Argumentação
        AxisRow(
          title = "Argumentação",
          subtitle = "Clareza lógica ao articular seu raciocínio",
          score = wisdom.argumentacao,
          icon = Icons.Default.Psychology,
          color = ArcaneMana
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Axis 3: Autonomia
        AxisRow(
          title = "Autonomia",
          subtitle = "Independência na busca da solução sem pedir atalhos",
          score = wisdom.autonomia,
          icon = Icons.Default.Shield,
          color = GoldPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Axis 4: Pesquisa
        AxisRow(
          title = "Pesquisa",
          subtitle = "Curiosidade intelectual pelos princípios e fórmulas",
          score = wisdom.pesquisa,
          icon = Icons.AutoMirrored.Filled.MenuBook,
          color = AmberGold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Axis 5: Correção
        AxisRow(
          title = "Correção",
          subtitle = "Capacidade de identificar e corrigir os próprios erros",
          score = wisdom.correcao,
          icon = Icons.Default.Lightbulb,
          color = Color(0xFFA855F7)
        )

        Spacer(modifier = Modifier.height(16.dp))

        GoldenRpgButton(
          text = "RETORNAR AO ENIGMA",
          onClick = onDismiss,
          modifier = Modifier.fillMaxWidth()
        )
      }
    }
  }
}

@Composable
private fun AxisRow(
  title: String,
  subtitle: String,
  score: Int,
  icon: ImageVector,
  color: Color
) {
  Surface(
    shape = RoundedCornerShape(10.dp),
    color = QuestiaSurfaceVariant,
    border = BorderStroke(1.dp, color.copy(alpha = 0.3f))
  ) {
    Column(
      modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
    ) {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
      ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
          Box(
            modifier = Modifier
              .size(28.dp)
              .clip(CircleShape)
              .background(color.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
          ) {
            Icon(
              imageVector = icon,
              contentDescription = null,
              tint = color,
              modifier = Modifier.size(16.dp)
            )
          }
          Spacer(modifier = Modifier.width(8.dp))
          Text(
            text = title,
            color = TextLight,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
          )
        }
        Text(
          text = "$score / 100",
          color = color,
          fontSize = 12.sp,
          fontWeight = FontWeight.Black
        )
      }

      Spacer(modifier = Modifier.height(4.dp))

      Text(
        text = subtitle,
        color = TextMuted,
        fontSize = 10.sp
      )

      Spacer(modifier = Modifier.height(6.dp))

      val progress by animateFloatAsState(
        targetValue = (score.toFloat() / 100f).coerceIn(0.05f, 1f),
        label = "axis_anim"
      )

      LinearProgressIndicator(
        progress = { progress },
        modifier = Modifier
          .fillMaxWidth()
          .height(5.dp)
          .clip(RoundedCornerShape(3.dp)),
        color = color,
        trackColor = QuestiaDeepNavy
      )
    }
  }
}
