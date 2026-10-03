package com.example.data

import android.util.Log
import com.example.BuildConfig
import com.example.model.AxisFeedback
import com.example.model.ChatMessage
import com.example.model.Hero
import com.example.model.MessageSender
import com.example.model.Quest
import com.example.ui.components.MathMarkdownFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiSocraticRepository(
  private val fallbackEngine: SocraticTutorEngine = SocraticTutorEngine()
) {
  // Snappy timeouts (8s connect, 12s read) to guarantee fast and responsive replies
  private val client = OkHttpClient.Builder()
    .connectTimeout(8, TimeUnit.SECONDS)
    .readTimeout(12, TimeUnit.SECONDS)
    .writeTimeout(10, TimeUnit.SECONDS)
    .build()

  val isLiveGeminiConfigured: Boolean
    get() {
      val key = BuildConfig.GEMINI_API_KEY
      return key.isNotBlank() && key != "MY_GEMINI_API_KEY"
    }

  // Model cascade: gemini-3.8-flash (primary, fast & smart), then gemini-flash-latest, then gemini-3.1-flash-lite-preview
  private val candidateModels = listOf(
    "gemini-3.8-flash",
    "gemini-flash-latest",
    "gemini-3.1-flash-lite-preview"
  )

  private fun buildSystemPrompt(hero: Hero?, activeQuest: Quest?, studentName: String): String {
    val heroName = hero?.name ?: "Aprendiz"
    val heroRole = hero?.role ?: "Herói do Conhecimento"
    val heroProfession = hero?.profession ?: "Estudante do Ensino Médio"
    val questTitle = activeQuest?.title ?: "Jornada do Saber"
    val questDesc = activeQuest?.description ?: "Desenvolvimento de competências, combate ao Brain Rot e raciocínio crítico."
    val questSubject = activeQuest?.subject?.displayName ?: "Multidisciplinar"

    return """
      Você é o 'Copiloto da Luz', mentor e tutor socrático inteligente de IA de alto nível no RPG educacional QuestIA.
      Seu propósito sagrado é combater o Brain Rot (distração rápida, falta de foco e preguiça mental) e guiar estudantes do Ensino Médio nas 4 matérias fundamentais: Matemática, Português, Inglês e História.

      ESTUDANTE ATUAL:
      - Aluno(a): $studentName
      - Personagem: $heroName ($heroRole • $heroProfession)
      - Matéria Ativa: $questSubject
      - Missão Atual: $questTitle ($questDesc)

      SCRIPT PADRÃO E MANDAMENTOS DO TUTOR SOCRÁTICO:
      1. NUNCA ENTREGUE A RESPOSTA FINAL PRONTA DE BANDEJA. O aluno precisa raciocinar e construir a resposta passo a passo com a sua orientação.
      2. Responda de forma perspicaz, direta, ágil e inteligente como um excelente professor conversando ao vivo.
      3. Se o aluno pedir a resposta ou fizer uma pergunta conceitual/tarefa:
         - Faça uma explicação conceitual brilhante e concisa (1 a 2 parágrafos) desmistificando o tema.
         - Forneça 2 ou 3 pistas estruturadas ou oriente o primeiro passo prático.
         - Finalize sempre com uma pergunta reflexiva objetiva que desafie o aluno a dar o próximo passo lógico.
      4. Se o aluno tentar responder:
         - Se o raciocínio estiver correto: celebre no clima épico do RPG e peça o passo final.
         - Se houver equívoco: aponte o ponto de atenção com máxima gentileza e faça uma pergunta-guia para ele mesmo corrigir.
         - Se o aluno acertar a resposta final do desafio: parabenize calorosamente ("Parabéns, Guerreiro(a)! Desafio superado!"), confirme brevemente a solução correta e dê a vitória ao estudante!
      5. Estimule os 5 Eixos da Sabedoria Socrática: Acerto, Argumentação, Autonomia, Pesquisa e Correção.
      6. Tom de voz: motivador, inteligente, acolhedor e com toques sutis de RPG épico. Use formatação limpa com Markdown (negrito, tópicos).
      7. Responda sempre em português brasileiro impecável, mantendo o texto dinâmico e sem enrolação.
      8. MANDATÓRIO - FORMATAÇÃO DE MATEMÁTICA E EQUAÇÕES:
         - NUNCA use delimitadores de LaTeX crus como '$$' ou '$' ou '\( ... \)'.
         - Escreva a matemática diretamente com caracteres Unicode legíveis. Use SEMPRE 'x²' (NUNCA 'x2' ou 'x^2' ou 'ax2').
         - A equação canônica de 2º grau é 'ax² + bx + c = 0'. NUNCA use '==' nem 'ax2'.
         - A equação do enigma atual da missão é 'x² + 4x - 11 = 0'.
         - A fórmula do discriminante é 'Δ = b² - 4ac' e a fórmula de Bhaskara é 'x = (-b ± √Δ) / (2a)'.
         - NUNCA duplique sinais de igual em equações (use '=' e NUNCA '==').
         - Coloque termos e coeficientes importantes em negrito com asteriscos (*termo* ou **termo**).
    """.trimIndent()
  }

  suspend fun respond(
    conversationHistory: List<ChatMessage>,
    userInput: String,
    currentHero: Hero? = null,
    activeQuest: Quest? = null,
    studentName: String = "Aprendiz"
  ): TutorStepResult = withContext(Dispatchers.IO) {
    val apiKey = BuildConfig.GEMINI_API_KEY

    // If apiKey is empty or default placeholder, use the intelligent Socratic engine immediately
    if (!isLiveGeminiConfigured) {
      Log.d("GeminiSocratic", "Using offline Socratic engine")
      return@withContext fallbackEngine.processUserInput(userInput)
    }

    val systemInstruction = JSONObject()
      .put("parts", JSONArray().put(JSONObject().put("text", buildSystemPrompt(currentHero, activeQuest, studentName))))

    val contentsArray = JSONArray()

    // Add recent conversation history for rich context
    conversationHistory.takeLast(8).forEach { msg ->
      val role = when (msg.sender) {
        MessageSender.USER -> "user"
        MessageSender.COPILOT -> "model"
        MessageSender.SYSTEM -> null
      }
      if (role != null) {
        val partObj = JSONObject().put("text", msg.text)
        val contentObj = JSONObject()
          .put("role", role)
          .put("parts", JSONArray().put(partObj))
        contentsArray.put(contentObj)
      }
    }

    // Add current user prompt
    contentsArray.put(
      JSONObject()
        .put("role", "user")
        .put("parts", JSONArray().put(JSONObject().put("text", userInput)))
    )

    val generationConfig = JSONObject()
      .put("temperature", 0.7)
      .put("topP", 0.95)
      .put("maxOutputTokens", 700)

    val requestPayload = JSONObject()
      .put("contents", contentsArray)
      .put("systemInstruction", systemInstruction)
      .put("generationConfig", generationConfig)

    val mediaType = "application/json; charset=utf-8".toMediaType()
    val requestBody = requestPayload.toString().toRequestBody(mediaType)

    // Try candidate models in order (gemini-3.8-flash -> gemini-flash-latest -> gemini-3.1-flash-lite-preview)
    for (modelName in candidateModels) {
      try {
        val url = "https://generativelanguage.googleapis.com/v1beta/models/$modelName:generateContent?key=$apiKey"
        val request = Request.Builder()
          .url(url)
          .post(requestBody)
          .build()

        val response = client.newCall(request).execute()
        val responseBodyString = response.body?.string()

        if (response.isSuccessful && !responseBodyString.isNullOrBlank()) {
          val json = JSONObject(responseBodyString)
          val candidates = json.optJSONArray("candidates")
          if (candidates != null && candidates.length() > 0) {
            val firstCandidate = candidates.getJSONObject(0)
            val content = firstCandidate.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val rawAiText = parts?.getJSONObject(0)?.optString("text") ?: ""

            if (rawAiText.isNotBlank()) {
              val aiText = MathMarkdownFormatter.cleanMathAndEquations(rawAiText)
              val feedback = parseFeedbackFromText(aiText)
              val isQuestComplete = (aiText.contains("parabéns", ignoreCase = true) || aiText.contains("parabens", ignoreCase = true)) &&
                (aiText.contains("desafio superado", ignoreCase = true) ||
                 aiText.contains("concluiu", ignoreCase = true) ||
                 aiText.contains("resposta correta", ignoreCase = true) ||
                 aiText.contains("acertou", ignoreCase = true) ||
                 aiText.contains("vitória", ignoreCase = true) ||
                 aiText.contains("vitoria", ignoreCase = true))

              val rawHighlight = extractHighlight(aiText)
              val highlight = rawHighlight?.let { MathMarkdownFormatter.cleanMathAndEquations(it) }

              return@withContext TutorStepResult(
                responseText = aiText,
                feedbackList = feedback.ifEmpty {
                  listOf(
                    AxisFeedback("Argumentação", +15, "Reflexão ativa com o Copiloto da Luz"),
                    AxisFeedback("Autonomia", +10, "Construção passo a passo do saber")
                  )
                },
                nextPhase = if (isQuestComplete) QuestRiddlePhase.COMPLETED else QuestRiddlePhase.BHASKARA,
                isCompleted = isQuestComplete,
                equationHighlight = highlight
              )
            }
          }
        } else {
          Log.w("GeminiSocratic", "Model $modelName returned code ${response.code}, trying next model...")
        }
      } catch (e: Exception) {
        Log.w("GeminiSocratic", "Exception with model $modelName: ${e.message}, trying next...")
      }
    }

    // If live API calls fail or offline, use intelligent fallback engine
    Log.d("GeminiSocratic", "Falling back to local Socratic tutor engine")
    return@withContext fallbackEngine.processUserInput(userInput)
  }

  private fun parseFeedbackFromText(text: String): List<AxisFeedback> {
    val list = mutableListOf<AxisFeedback>()
    val axesNames = listOf("Acerto", "Argumentação", "Autonomia", "Pesquisa", "Correção")

    axesNames.forEach { axis ->
      if (text.contains(axis, ignoreCase = true)) {
        list.add(AxisFeedback(axis, +15, "Progresso avaliado em $axis"))
      }
    }
    return list
  }

  private fun extractHighlight(text: String): String? {
    val patterns = listOf(
      "x² + 4x - 11 = 0",
      "x = -2 ± √15",
      "Δ = b² - 4ac",
      "Δ = 60",
      "Redação ENEM",
      "Proposta de Intervenção",
      "False Friends",
      "Planalto Central",
      "Brasília"
    )
    for (p in patterns) {
      if (text.contains(p, ignoreCase = true)) return p
    }
    return null
  }
}
