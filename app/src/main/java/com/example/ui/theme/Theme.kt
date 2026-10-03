package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val QuestiaColorScheme = darkColorScheme(
  primary = GoldPrimary,
  onPrimary = TextDark,
  primaryContainer = GoldDark,
  onPrimaryContainer = GoldGlow,
  secondary = ArcaneMana,
  onSecondary = TextDark,
  secondaryContainer = ArcaneManaDark,
  onSecondaryContainer = TextLight,
  tertiary = AmberGold,
  onTertiary = TextDark,
  background = QuestiaDarkBg,
  onBackground = TextLight,
  surface = QuestiaSurface,
  onSurface = TextLight,
  surfaceVariant = QuestiaSurfaceVariant,
  onSurfaceVariant = TextMuted,
  outline = QuestiaCardBorder,
  error = RubyHealth,
  onError = TextLight
)

@Composable
fun MyApplicationTheme(
  content: @Composable () -> Unit,
) {
  MaterialTheme(
    colorScheme = QuestiaColorScheme,
    typography = Typography,
    content = content
  )
}

