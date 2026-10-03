package com.example.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

object MathMarkdownFormatter {

  /**
   * Cleans math formatting bugs, converts raw LaTeX notation ($$ ... $$) into
   * clean readable Unicode mathematics (x², ax² + bx + c = 0, Δ = b² - 4ac, etc.),
   * and fixes double equals (== -> =).
   */
  fun cleanMathAndEquations(rawText: String): String {
    if (rawText.isBlank()) return ""

    var text = rawText

    // 1. Normalize bullet points at beginning of lines
    text = text.replace(Regex("""(?m)^\s*\*\s+"""), "• ")
    text = text.replace(Regex("""(?m)^\s*-\s+"""), "• ")

    // 2. Extract and sanitize double dollar LaTeX blocks ($$ ... $$)
    val doubleDollarRegex = Regex("""\$\$(.+?)\$\$""", RegexOption.DOT_MATCHES_ALL)
    text = doubleDollarRegex.replace(text) { matchResult ->
      val mathContent = matchResult.groupValues[1].trim()
      "`${sanitizeMathExpression(mathContent)}`"
    }

    // 3. Extract and sanitize single dollar LaTeX blocks ($ ... $)
    val singleDollarRegex = Regex("""(?<!\$)\$([^$\n]+?)\$(?!\$)""")
    text = singleDollarRegex.replace(text) { matchResult ->
      val mathContent = matchResult.groupValues[1].trim()
      "`${sanitizeMathExpression(mathContent)}`"
    }

    // 4. Sanitize LaTeX brackets \[ ... \] and \( ... \)
    text = text.replace(Regex("""\\\[(.+?)\\\]""", RegexOption.DOT_MATCHES_ALL)) { match ->
      "`${sanitizeMathExpression(match.groupValues[1].trim())}`"
    }
    text = text.replace(Regex("""\\\((.+?)\\\)""")) { match ->
      "`${sanitizeMathExpression(match.groupValues[1].trim())}`"
    }

    // 5. Global fixes for math expressions outside LaTeX tags:
    text = sanitizeGeneralMathArtifacts(text)

    return text
  }

  /**
   * Sanitizes specific math expression strings (inside LaTeX or formulas)
   */
  private fun sanitizeMathExpression(expr: String): String {
    var s = expr

    // Fix double equals
    s = s.replace("==", "=")

    // LaTeX command translations
    s = s.replace(Regex("""\\pm"""), "±")
    s = s.replace(Regex("""\\Delta"""), "Δ")
    s = s.replace(Regex("""\\delta"""), "Δ")
    s = s.replace(Regex("""\\cdot"""), "·")
    s = s.replace(Regex("""\\times"""), "·")
    s = s.replace(Regex("""\\le(q)?"""), "≤")
    s = s.replace(Regex("""\\ge(q)?"""), "≥")
    s = s.replace(Regex("""\\ne(q)?"""), "≠")
    s = s.replace(Regex("""\\sqrt\{([^}]+)\}"""), "√($1)")
    s = s.replace(Regex("""\\sqrt"""), "√")
    s = s.replace(Regex("""\\frac\{([^}]+)\}\{([^}]+)\}"""), "($1)/($2)")
    s = s.replace(Regex("""\\text\{([^}]+)\}"""), "$1")
    s = s.replace(Regex("""\\left|\\right"""), "")

    // Specific equation replacements
    s = s.replace("ax2", "ax²")
    s = s.replace("bx2", "bx²")
    s = s.replace("cx2", "cx²")
    s = s.replace("x2", "x²")
    s = s.replace("b2", "b²")
    s = s.replace("4ac", "4ac")

    // Caret superscripts: x^2 -> x², x^3 -> x³
    s = s.replace(Regex("""\^2"""), "²")
    s = s.replace(Regex("""\^3"""), "³")
    s = s.replace(Regex("""\^([0-9])""")) { m ->
      when (m.groupValues[1]) {
        "0" -> "⁰"
        "1" -> "¹"
        "4" -> "⁴"
        "5" -> "⁵"
        "6" -> "⁶"
        "7" -> "⁷"
        "8" -> "⁸"
        "9" -> "⁹"
        else -> "^${m.groupValues[1]}"
      }
    }

    // Clean whitespace
    s = s.replace(Regex("""\s+"""), " ").trim()
    return s
  }

  /**
   * Applies global fixes to math artifacts appearing in normal text
   */
  private fun sanitizeGeneralMathArtifacts(raw: String): String {
    var s = raw

    // Fix double equals appearing in math equations
    s = s.replace(Regex("""(\b[a-zA-Z0-9\+\-\*\/\(\)\^²³Δ√±·\s]+)\s*==\s*([a-zA-Z0-9\+\-\*\/\(\)\^²³Δ√±·\s]+)""")) { match ->
      // If it looks like an equation (e.g. ax2 + bx + c == 0 or x2 + 4x - 11 == 0 or delta == 60)
      val full = match.value
      if (full.contains("x") || full.contains("a") || full.contains("b") || full.contains("c") || full.contains("0") || full.contains("60")) {
        full.replace("==", "=")
      } else {
        full
      }
    }

    // Specific well-known bug patterns reported by user:
    // e.g. "ax2 + bx + c = 0" or "ax2 + bx + c == 0"
    s = s.replace(Regex("""\bax2\b"""), "ax²")
    s = s.replace(Regex("""\bbx2\b"""), "bx²")
    s = s.replace(Regex("""\bcx2\b"""), "cx²")
    s = s.replace(Regex("""\bx2\b"""), "x²")
    s = s.replace(Regex("""\bb2\b"""), "b²")

    // Fix equations like "x^2 + 4x - 11" or "x^2"
    s = s.replace("x^2", "x²")
    s = s.replace("x^3", "x³")
    s = s.replace("b^2", "b²")
    s = s.replace("4^2", "4²")

    // Fix trailing double equals
    s = s.replace(" == 0", " = 0")
    s = s.replace(" == ", " = ")

    return s
  }

  /**
   * Parses Markdown bold (**word** or *word*), inline code (`code`),
   * and italics (_word_) into an AnnotatedString with proper styling.
   */
  fun buildMarkdownAnnotatedString(
    rawText: String,
    baseColor: Color = Color(0xFFCAD5E2),
    boldColor: Color = Color.White,
    codeColor: Color = Color(0xFFFFD700)
  ): AnnotatedString {
    val cleanedText = cleanMathAndEquations(rawText)

    return buildAnnotatedString {
      // Regex matching:
      // Group 1: full token
      // Group 2: inside **...**
      // Group 3: inside *...*
      // Group 4: inside `...`
      // Group 5: inside __...__
      // Group 6: inside _..._
      val tokenRegex = Regex("""(\*\*([^*]+?)\*\*|\*([^*\n]+?)\*|`([^`]+?)`|__([^_]+?)__|(?<!\w)_([^_]+?)_(?!\w))""")
      var lastIndex = 0

      tokenRegex.findAll(cleanedText).forEach { match ->
        // Append text preceding the match
        if (match.range.first > lastIndex) {
          append(cleanedText.substring(lastIndex, match.range.first))
        }

        when {
          // Double asterisk bold: **text**
          match.groups[2] != null -> {
            val content = match.groups[2]!!.value
            val start = length
            append(content)
            addStyle(
              SpanStyle(fontWeight = FontWeight.Bold, color = boldColor),
              start,
              length
            )
          }

          // Single asterisk bold: *text* (as explicitly requested by user)
          match.groups[3] != null -> {
            val content = match.groups[3]!!.value
            val start = length
            append(content)
            addStyle(
              SpanStyle(fontWeight = FontWeight.Bold, color = boldColor),
              start,
              length
            )
          }

          // Inline code / formula: `text`
          match.groups[4] != null -> {
            val content = match.groups[4]!!.value
            val start = length
            append(" $content ")
            addStyle(
              SpanStyle(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = codeColor,
                background = Color(0x33000000)
              ),
              start,
              length
            )
          }

          // Underscore bold: __text__
          match.groups[5] != null -> {
            val content = match.groups[5]!!.value
            val start = length
            append(content)
            addStyle(
              SpanStyle(fontWeight = FontWeight.Bold, color = boldColor),
              start,
              length
            )
          }

          // Single underscore italic: _text_
          match.groups[6] != null -> {
            val content = match.groups[6]!!.value
            val start = length
            append(content)
            addStyle(
              SpanStyle(fontStyle = FontStyle.Italic, color = boldColor),
              start,
              length
            )
          }

          else -> {
            append(match.value)
          }
        }

        lastIndex = match.range.last + 1
      }

      // Append remaining text
      if (lastIndex < cleanedText.length) {
        append(cleanedText.substring(lastIndex))
      }
    }
  }
}

/**
 * Rich text composable that formats equations, LaTeX, superscripts,
 * and markdown bold (*...* and **...**) seamlessly.
 */
@Composable
fun RichChatMessageText(
  text: String,
  modifier: Modifier = Modifier,
  color: Color = Color(0xFFCAD5E2),
  boldColor: Color = Color.White,
  codeColor: Color = Color(0xFFFFD700),
  fontSize: TextUnit = 14.sp,
  lineHeight: TextUnit = 20.sp,
  textAlign: TextAlign? = null
) {
  val annotatedString = remember(text, color, boldColor, codeColor) {
    MathMarkdownFormatter.buildMarkdownAnnotatedString(
      rawText = text,
      baseColor = color,
      boldColor = boldColor,
      codeColor = codeColor
    )
  }

  Text(
    text = annotatedString,
    modifier = modifier.fillMaxWidth(),
    color = color,
    fontSize = fontSize,
    lineHeight = lineHeight,
    textAlign = textAlign
  )
}
