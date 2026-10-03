package com.example.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.GeminiSocraticRepository
import com.example.data.QuestRiddlePhase
import com.example.data.SocraticTutorEngine
import com.example.data.local.QuestiaPersistenceRepository
import com.example.data.local.UserQuestiaProfile
import com.example.model.AxisFeedback
import com.example.model.CharacterProfile
import com.example.model.ChatMessage
import com.example.model.Guilda
import com.example.model.Hero
import com.example.model.MessageSender
import com.example.model.Quest
import com.example.model.QuestIaData
import com.example.model.QuestSubject
import com.example.model.Trail
import com.example.model.UserAvatarCustomization
import com.example.model.WisdomAxes
import com.example.ui.components.MathMarkdownFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

enum class QuestIaTab {
  HOME,
  QUESTS,
  GUILDAS,
  TRILHAS,
  HEROIS
}

enum class AppScreen {
  QUESTIA_MAIN,
  COPILOT_CHAT
}

data class QuestiaUiState(
  val userName: String = "",
  val isRegistered: Boolean = false,
  val activeTab: QuestIaTab = QuestIaTab.HOME,
  val currentHero: Hero = QuestIaData.USER_HERO_DEFAULT,
  val currentScreen: AppScreen = AppScreen.QUESTIA_MAIN,
  val activeQuest: Quest? = null,
  val isLiveGeminiActive: Boolean = false,
  // 4 Quests (uma por matéria: Matemática, Português, Inglês, História)
  val quests: List<Quest> = QuestIaData.QUESTS,
  // Guildas / Escolas
  val guildas: List<Guilda> = QuestIaData.DEFAULT_GUILDAS,
  val userGuildaId: String? = "g_solar",
  val guildaSearchQuery: String = "",
  // Trilhas conectadas com o progresso das quests
  val userTrails: List<Trail> = QuestIaData.DEFAULT_TRAILS,
  // Customização do personagem do usuário
  val avatarCustomization: UserAvatarCustomization = UserAvatarCustomization(),
  // Chat & Copilot state
  val character: CharacterProfile = CharacterProfile(),
  val messages: List<ChatMessage> = defaultMessages(),
  val inputText: String = "",
  val isTyping: Boolean = false,
  val currentPhase: QuestRiddlePhase = QuestRiddlePhase.COEFFICIENTS,
  val showWisdomSheet: Boolean = false,
  val showVictoryDialog: Boolean = false,
  val questRewardClaimed: Boolean = false
) {
  fun getDisplayName(): String {
    val trimmed = userName.trim()
    return if (trimmed.isNotEmpty()) trimmed else "Aprendiz"
  }

  val userGuilda: Guilda?
    get() = guildas.firstOrNull { it.id == userGuildaId }

  val filteredGuildas: List<Guilda>
    get() {
      val query = guildaSearchQuery.trim().lowercase()
      if (query.isEmpty()) return guildas.sortedBy { it.rank }
      return guildas.filter {
        it.name.lowercase().contains(query) ||
          it.schoolName.lowercase().contains(query) ||
          it.city.lowercase().contains(query) ||
          it.code.lowercase().contains(query)
      }.sortedBy { it.rank }
    }
}

private fun defaultMessages(): List<ChatMessage> = listOf(
  ChatMessage(
    id = "sys_welcome",
    sender = MessageSender.SYSTEM,
    text = "A Máquina da Ilusão trancou a porta. O Copiloto da Luz conectou-se."
  ),
  ChatMessage(
    id = "copilot_intro",
    sender = MessageSender.COPILOT,
    text = "Saudações, Aprendiz! Sou o Copiloto da Luz, seu mentor de IA contra o Brain Rot e tutor socrático no Reino de QuestIA. Pergunte-me qualquer dúvida sobre Matemática, Português, Inglês, História ou seus desafios!",
    equationHighlight = "Mentor IA Ativo"
  )
)

class QuestiaViewModel(
  application: Application? = null,
  private val repository: GeminiSocraticRepository = GeminiSocraticRepository(SocraticTutorEngine()),
  private val persistenceRepository: QuestiaPersistenceRepository = QuestiaPersistenceRepository(application)
) : ViewModel() {

  constructor(application: Application) : this(
    application = application,
    repository = GeminiSocraticRepository(SocraticTutorEngine()),
    persistenceRepository = QuestiaPersistenceRepository(application)
  )

  constructor() : this(
    application = null,
    repository = GeminiSocraticRepository(SocraticTutorEngine()),
    persistenceRepository = QuestiaPersistenceRepository(null)
  )

  private val _uiState = MutableStateFlow(
    QuestiaUiState(isLiveGeminiActive = repository.isLiveGeminiConfigured)
  )
  val uiState: StateFlow<QuestiaUiState> = _uiState.asStateFlow()

  init {
    val fastSaved = persistenceRepository.loadProfileSync()
    if (fastSaved != null && fastSaved.isRegistered) {
      applyLoadedProfile(fastSaved)
    }
    loadPersistedData()
  }

  private fun applyLoadedProfile(saved: UserQuestiaProfile) {
    val completedIds = saved.completedQuestsCsv.split(",").filter { it.isNotBlank() }.toSet()
    val restoredQuests = QuestIaData.QUESTS.map { q ->
      if (completedIds.contains(q.id)) q.copy(isCompleted = true) else q
    }

    val restoredTrails = QuestIaData.DEFAULT_TRAILS.map { trail ->
      when (trail.subject) {
        QuestSubject.MATEMATICA -> trail.copy(progress = saved.trailMatematicaProgress, level = saved.trailMatematicaLevel)
        QuestSubject.PORTUGUES -> trail.copy(progress = saved.trailPortuguesProgress, level = saved.trailPortuguesLevel)
        QuestSubject.INGLES -> trail.copy(progress = saved.trailInglesProgress, level = saved.trailInglesLevel)
        QuestSubject.HISTORIA -> trail.copy(progress = saved.trailHistoriaProgress, level = saved.trailHistoriaLevel)
      }
    }

    val restoredHero = QuestIaData.USER_HERO_DEFAULT.copy(
      name = if (saved.userName.isNotBlank()) saved.userName else "Aprendiz",
      level = saved.heroLevel,
      xp = saved.heroXp,
      gold = saved.heroGold,
      hp = saved.heroHp,
      mp = saved.heroMp,
      trails = restoredTrails
    )

    _uiState.update { state ->
      state.copy(
        userName = saved.userName,
        isRegistered = saved.isRegistered,
        currentHero = restoredHero,
        avatarCustomization = UserAvatarCustomization(
          model3dId = saved.selectedModelId,
          title = saved.selectedModelTitle
        ),
        userGuildaId = saved.userGuildaId,
        userTrails = restoredTrails,
        quests = restoredQuests
      )
    }
  }

  private fun loadPersistedData() {
    viewModelScope.launch {
      val saved = persistenceRepository.loadProfile()
      if (saved != null) {
        applyLoadedProfile(saved)
      }
    }
  }

  private fun persistCurrentState() {
    val state = _uiState.value
    val matTrail = state.userTrails.firstOrNull { it.subject == QuestSubject.MATEMATICA }
    val portTrail = state.userTrails.firstOrNull { it.subject == QuestSubject.PORTUGUES }
    val ingTrail = state.userTrails.firstOrNull { it.subject == QuestSubject.INGLES }
    val histTrail = state.userTrails.firstOrNull { it.subject == QuestSubject.HISTORIA }

    val completedCsv = state.quests.filter { it.isCompleted }.joinToString(",") { it.id }

    val profile = UserQuestiaProfile(
      id = 1,
      userName = state.userName,
      isRegistered = state.isRegistered,
      heroLevel = state.currentHero.level,
      heroXp = state.currentHero.xp,
      heroGold = state.currentHero.gold,
      heroHp = state.currentHero.hp,
      heroMp = state.currentHero.mp,
      selectedModelId = state.avatarCustomization.model3dId,
      selectedModelTitle = state.avatarCustomization.title,
      userGuildaId = state.userGuildaId,
      trailMatematicaProgress = matTrail?.progress ?: 0,
      trailMatematicaLevel = matTrail?.level ?: 1,
      trailPortuguesProgress = portTrail?.progress ?: 0,
      trailPortuguesLevel = portTrail?.level ?: 1,
      trailInglesProgress = ingTrail?.progress ?: 0,
      trailInglesLevel = ingTrail?.level ?: 1,
      trailHistoriaProgress = histTrail?.progress ?: 0,
      trailHistoriaLevel = histTrail?.level ?: 1,
      completedQuestsCsv = completedCsv
    )

    // 1. Immediately write to disk atomically so progress cannot be lost even on sudden kill
    persistenceRepository.saveProfileSync(profile)

    // 2. Persist to Room database asynchronously
    viewModelScope.launch {
      persistenceRepository.saveProfile(profile)
    }
  }

  fun saveOnAppExit() {
    persistCurrentState()
  }

  fun onUserNameChanged(name: String) {
    _uiState.update { it.copy(userName = name) }
  }

  fun completeOnboarding() {
    // Ao se registrar, o usuário começa todas as 4 trilhas com 0% e nível 1
    val initialTrails = QuestIaData.DEFAULT_TRAILS.map { it.copy(progress = 0, level = 1) }
    _uiState.update {
      it.copy(
        isRegistered = true,
        currentHero = it.currentHero.copy(
          name = it.getDisplayName(),
          xp = 0,
          level = 1,
          trails = initialTrails
        ),
        userTrails = initialTrails,
        quests = QuestIaData.QUESTS.map { q -> q.copy(isCompleted = false) },
        activeTab = QuestIaTab.HOME
      )
    }
    persistCurrentState()
  }

  fun completeQuestDirectly(questId: String) {
    val quest = _uiState.value.quests.firstOrNull { it.id == questId } ?: return
    if (quest.isCompleted) return

    _uiState.update { state ->
      val updatedQuests = state.quests.map { q ->
        if (q.id == questId) q.copy(isCompleted = true) else q
      }
      state.copy(quests = updatedQuests)
    }
    advanceSubjectTrail(quest.subject, quest.xp)
  }

  fun setActiveTab(tab: QuestIaTab) {
    _uiState.update { it.copy(activeTab = tab, currentScreen = AppScreen.QUESTIA_MAIN) }
  }

  fun startQuest(quest: Quest) {
    val questIntroMessage = ChatMessage(
      id = UUID.randomUUID().toString(),
      sender = MessageSender.COPILOT,
      text = "⚔️ **MISSÃO INICIADA: ${quest.title}** (${quest.subject.displayName})\n\n${quest.description}\n\nEstou pronto para guiá-lo pelo método socrático! Por onde deseja começar nossa reflexão?",
      equationHighlight = quest.title
    )

    _uiState.update {
      it.copy(
        activeQuest = quest,
        currentScreen = AppScreen.COPILOT_CHAT,
        messages = it.messages + questIntroMessage
      )
    }

    if (quest.challengePrompt.isNotBlank()) {
      sendQuickPrompt(quest.challengePrompt)
    }
  }

  fun openAiChat(contextPrompt: String? = null) {
    _uiState.update {
      it.copy(currentScreen = AppScreen.COPILOT_CHAT)
    }
    if (!contextPrompt.isNullOrBlank()) {
      sendQuickPrompt(contextPrompt)
    }
  }

  fun navigateToChat() {
    _uiState.update { it.copy(currentScreen = AppScreen.COPILOT_CHAT) }
  }

  fun navigateToMain() {
    _uiState.update { it.copy(currentScreen = AppScreen.QUESTIA_MAIN) }
  }

  fun onInputTextChanged(newText: String) {
    _uiState.update { it.copy(inputText = newText) }
  }

  fun toggleWisdomSheet(show: Boolean) {
    _uiState.update { it.copy(showWisdomSheet = show) }
  }

  fun dismissVictoryDialog() {
    _uiState.update { it.copy(showVictoryDialog = false) }
  }

  // Guildas system
  fun onGuildaSearchChanged(query: String) {
    _uiState.update { it.copy(guildaSearchQuery = query) }
  }

  fun joinGuilda(guildaId: String) {
    _uiState.update { state ->
      val updatedGuildas = state.guildas.map { g ->
        if (g.id == guildaId) g.copy(memberCount = g.memberCount + 1)
        else if (g.id == state.userGuildaId) g.copy(memberCount = (g.memberCount - 1).coerceAtLeast(1))
        else g
      }
      state.copy(
        userGuildaId = guildaId,
        guildas = updatedGuildas
      )
    }
    persistCurrentState()
  }

  fun leaveGuilda() {
    _uiState.update { state ->
      val updatedGuildas = state.guildas.map { g ->
        if (g.id == state.userGuildaId) g.copy(memberCount = (g.memberCount - 1).coerceAtLeast(1))
        else g
      }
      state.copy(
        userGuildaId = null,
        guildas = updatedGuildas
      )
    }
    persistCurrentState()
  }

  fun createGuilda(name: String, schoolName: String, motto: String, city: String, emblem: String) {
    val newGuilda = Guilda(
      id = "g_custom_${System.currentTimeMillis()}",
      name = name.ifBlank { "Guilda dos Paladinos" },
      schoolName = schoolName.ifBlank { "Escola da Comunidade" },
      code = "SCH-${(100..999).random()}",
      emblem = emblem.ifBlank { "🛡️" },
      motto = motto.ifBlank { "Foco, conhecimento e união." },
      city = city.ifBlank { "Brasil" },
      totalXp = 1000,
      memberCount = 1,
      rank = _uiState.value.guildas.size + 1,
      accentHex = "#00E5FF"
    )

    _uiState.update { state ->
      state.copy(
        guildas = state.guildas + newGuilda,
        userGuildaId = newGuilda.id
      )
    }
    persistCurrentState()
  }

  // Escolha do Personagem do Jogador (Fundadores são estritamente inacessíveis)
  fun updateAvatarModel3d(modelId: String) {
    val model = QuestIaData.AVATAR_3D_MODELS.firstOrNull { it.id == modelId } ?: return
    _uiState.update {
      it.copy(
        avatarCustomization = it.avatarCustomization.copy(
          model3dId = model.id,
          title = model.name
        )
      )
    }
    persistCurrentState()
  }

  // Progressão da trilha baseada na quest concluída
  fun advanceSubjectTrail(subject: QuestSubject, xpReward: Int) {
    _uiState.update { state ->
      val updatedTrails = state.userTrails.map { trail ->
        if (trail.subject == subject) {
          val newProgress = trail.progress + 25
          if (newProgress >= 100) {
            trail.copy(progress = newProgress % 100, level = trail.level + 1)
          } else {
            trail.copy(progress = newProgress)
          }
        } else {
          trail
        }
      }

      val updatedHero = state.currentHero.copy(
        xp = state.currentHero.xp + xpReward,
        gold = state.currentHero.gold + 50,
        trails = updatedTrails
      )

      // Também adiciona XP para a guilda do usuário se houver
      val updatedGuildas = state.guildas.map { g ->
        if (g.id == state.userGuildaId) g.copy(totalXp = g.totalXp + xpReward)
        else g
      }

      state.copy(
        userTrails = updatedTrails,
        currentHero = updatedHero,
        guildas = updatedGuildas
      )
    }
    persistCurrentState()
  }

  fun sendQuickPrompt(promptText: String) {
    _uiState.update { it.copy(inputText = promptText) }
    sendMessage()
  }

  fun sendMessage() {
    val text = _uiState.value.inputText.trim()
    if (text.isBlank() || _uiState.value.isTyping) return

    val userMessage = ChatMessage(
      id = UUID.randomUUID().toString(),
      sender = MessageSender.USER,
      text = text
    )

    _uiState.update { state ->
      state.copy(
        messages = state.messages + userMessage,
        inputText = "",
        isTyping = true
      )
    }

    viewModelScope.launch {
      val currentHistory = _uiState.value.messages
      val currentHero = _uiState.value.currentHero
      val activeQuest = _uiState.value.activeQuest
      val studentName = _uiState.value.getDisplayName()

      val result = repository.respond(
        conversationHistory = currentHistory,
        userInput = text,
        currentHero = currentHero,
        activeQuest = activeQuest,
        studentName = studentName
      )

      val updatedAxes = applyAxisFeedback(_uiState.value.character.wisdomAxes, result.feedbackList)

      var updatedCharacter = _uiState.value.character.copy(
        wisdomAxes = updatedAxes
      )

      var showVictory = false

      if (result.isCompleted && !_uiState.value.questRewardClaimed) {
        val xpGain = activeQuest?.xp ?: 300
        val newXp = updatedCharacter.currentXp + xpGain
        val newLevel = 1 + (newXp / 100)
        val remainingXp = newXp % 100
        val newGold = updatedCharacter.gold + (activeQuest?.goldReward ?: 50)

        updatedCharacter = updatedCharacter.copy(
          level = newLevel,
          currentXp = remainingXp,
          gold = newGold,
          brainRotResistance = 100,
          questCompleted = true
        )
        showVictory = true

        // Atualiza a quest como concluída
        if (activeQuest != null) {
          _uiState.update { state ->
            val updatedQuests = state.quests.map { q ->
              if (q.id == activeQuest.id) q.copy(isCompleted = true) else q
            }
            state.copy(quests = updatedQuests)
          }
          // Avança a trilha daquela matéria!
          advanceSubjectTrail(activeQuest.subject, xpGain)
        }
      }

      val cleanText = MathMarkdownFormatter.cleanMathAndEquations(result.responseText)
      val cleanHighlight = result.equationHighlight?.let {
        MathMarkdownFormatter.cleanMathAndEquations(it).replace("`", "").trim()
      }

      val copilotMessage = ChatMessage(
        id = UUID.randomUUID().toString(),
        sender = MessageSender.COPILOT,
        text = cleanText,
        axisFeedback = result.feedbackList,
        isSocraticHint = true,
        equationHighlight = cleanHighlight
      )

      _uiState.update { state ->
        state.copy(
          messages = state.messages + copilotMessage,
          isTyping = false,
          currentPhase = result.nextPhase,
          character = updatedCharacter,
          showVictoryDialog = showVictory,
          questRewardClaimed = state.questRewardClaimed || result.isCompleted,
          isLiveGeminiActive = repository.isLiveGeminiConfigured
        )
      }

      persistCurrentState()
    }
  }

  private fun applyAxisFeedback(current: WisdomAxes, feedbackList: List<AxisFeedback>): WisdomAxes {
    var acerto = current.acerto
    var argumentacao = current.argumentacao
    var autonomia = current.autonomia
    var pesquisa = current.pesquisa
    var correcao = current.correcao

    feedbackList.forEach { fb ->
      when (fb.axisName.lowercase()) {
        "acerto" -> acerto = (acerto + fb.deltaPoints).coerceIn(0, 100)
        "argumentação", "argumentacao" -> argumentacao = (argumentacao + fb.deltaPoints).coerceIn(0, 100)
        "autonomia" -> autonomia = (autonomia + fb.deltaPoints).coerceIn(0, 100)
        "pesquisa" -> pesquisa = (pesquisa + fb.deltaPoints).coerceIn(0, 100)
        "correção", "correcao" -> correcao = (correcao + fb.deltaPoints).coerceIn(0, 100)
      }
    }

    return current.copy(
      acerto = acerto,
      argumentacao = argumentacao,
      autonomia = autonomia,
      pesquisa = pesquisa,
      correcao = correcao
    )
  }

  fun resetQuest() {
    _uiState.update {
      it.copy(
        currentScreen = AppScreen.QUESTIA_MAIN,
        character = CharacterProfile(),
        messages = defaultMessages(),
        currentPhase = QuestRiddlePhase.COEFFICIENTS
      )
    }
  }
}
