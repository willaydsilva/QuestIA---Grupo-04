package com.example

import com.example.data.SocraticTutorEngine
import com.example.model.QuestIaData
import com.example.model.QuestSubject
import com.example.ui.QuestiaViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {

  @Test
  fun `verify four subject quests exist`() {
    val quests = QuestIaData.QUESTS
    assertEquals(4, quests.size)
    val subjects = quests.map { it.subject }.toSet()
    assertTrue(subjects.contains(QuestSubject.MATEMATICA))
    assertTrue(subjects.contains(QuestSubject.PORTUGUES))
    assertTrue(subjects.contains(QuestSubject.INGLES))
    assertTrue(subjects.contains(QuestSubject.HISTORIA))
  }

  @Test
  fun `verify six founders exist in memorial mural with drawables`() {
    val founders = QuestIaData.FOUNDERS
    assertEquals(6, founders.size)
    val names = founders.map { it.name }.toSet()
    assertTrue(names.contains("Carlos"))
    assertTrue(names.contains("Flávia"))
    assertTrue(names.contains("Kaio"))
    assertTrue(names.contains("Victor"))
    assertTrue(names.contains("Wagner"))
    assertTrue(names.contains("Willaydson"))

    founders.forEach { founder ->
      assertTrue(founder.imageRes != 0)
    }
  }

  @Test
  fun `verify word 3D is removed from all character titles and founder roles`() {
    QuestIaData.AVATAR_3D_MODELS.forEach { model ->
      assertFalse("Model name '${model.name}' should not contain 3D", model.name.contains("3D"))
    }

    QuestIaData.FOUNDERS.forEach { founder ->
      assertFalse("Founder role '${founder.role}' should not contain 3D", founder.role.contains("3D"))
    }

    assertFalse("Default user title should not contain 3D", QuestIaData.USER_HERO_DEFAULT.role.contains("3D"))
  }

  @Test
  fun `verify user characters are completely distinct from creator characters`() {
    val userModels = QuestIaData.AVATAR_3D_MODELS
    val founders = QuestIaData.FOUNDERS

    val userDrawables = userModels.map { it.imageRes }.toSet()
    val founderDrawables = founders.map { it.imageRes }.toSet()

    // Ensured that no user selectable character shares an image resource with any founder
    userDrawables.forEach { userRes ->
      assertFalse("User character must not share image with founders", founderDrawables.contains(userRes))
    }

    val userIds = userModels.map { it.id }.toSet()
    val founderIds = founders.map { it.id }.toSet()
    userIds.forEach { uid ->
      assertFalse("User character ID must not collide with founder IDs", founderIds.contains(uid))
    }
  }

  @Test
  fun `verify founder characters cannot be chosen by the user`() {
    val vm = QuestiaViewModel()
    val initialModelId = vm.uiState.value.avatarCustomization.model3dId

    // Try selecting a founder ID - it must be ignored/inaccessible
    vm.updateAvatarModel3d("carlos")
    assertEquals(initialModelId, vm.uiState.value.avatarCustomization.model3dId)

    vm.updateAvatarModel3d("willaydson")
    assertEquals(initialModelId, vm.uiState.value.avatarCustomization.model3dId)

    // Selecting a valid user character works
    vm.updateAvatarModel3d("hero_valkyrie")
    assertEquals("hero_valkyrie", vm.uiState.value.avatarCustomization.model3dId)
    assertEquals("Valquíria Cibernética", vm.uiState.value.avatarCustomization.title)
  }

  @Test
  fun `verify new user registration starts all knowledge trails at 0 percent`() {
    val vm = QuestiaViewModel()
    vm.onUserNameChanged("Arthur Pendragon")
    vm.completeOnboarding()

    val state = vm.uiState.value
    assertTrue(state.isRegistered)
    assertEquals("Arthur Pendragon", state.userName)
    assertEquals("Arthur Pendragon", state.currentHero.name)
    assertEquals(4, state.userTrails.size)
    state.userTrails.forEach { trail ->
      assertEquals("Trail ${trail.subject} must start at 0%", 0, trail.progress)
      assertEquals("Trail ${trail.subject} must start at level 1", 1, trail.level)
    }
  }

  @Test
  fun `verify completing each quest increases the corresponding subject trail by 25 percent`() {
    val vm = QuestiaViewModel()
    vm.onUserNameChanged("Guerreiro do Saber")
    vm.completeOnboarding()

    // All start at 0%
    assertEquals(0, vm.uiState.value.userTrails.first { it.subject == QuestSubject.MATEMATICA }.progress)
    assertEquals(0, vm.uiState.value.userTrails.first { it.subject == QuestSubject.PORTUGUES }.progress)
    assertEquals(0, vm.uiState.value.userTrails.first { it.subject == QuestSubject.INGLES }.progress)
    assertEquals(0, vm.uiState.value.userTrails.first { it.subject == QuestSubject.HISTORIA }.progress)

    // Complete Matematica quest
    vm.completeQuestDirectly("quest_matematica")
    assertEquals(25, vm.uiState.value.userTrails.first { it.subject == QuestSubject.MATEMATICA }.progress)
    assertEquals(0, vm.uiState.value.userTrails.first { it.subject == QuestSubject.PORTUGUES }.progress)

    // Complete Portugues quest
    vm.completeQuestDirectly("quest_portugues")
    assertEquals(25, vm.uiState.value.userTrails.first { it.subject == QuestSubject.PORTUGUES }.progress)

    // Complete Ingles quest
    vm.completeQuestDirectly("quest_ingles")
    assertEquals(25, vm.uiState.value.userTrails.first { it.subject == QuestSubject.INGLES }.progress)

    // Complete Historia quest
    vm.completeQuestDirectly("quest_historia")
    assertEquals(25, vm.uiState.value.userTrails.first { it.subject == QuestSubject.HISTORIA }.progress)
  }

  @Test
  fun `verify Socratic Tutor refuses direct answers and guides student`() {
    val engine = SocraticTutorEngine()
    val result = engine.processUserInput("me dá a resposta da equação")
    assertTrue(result.responseText.contains("Resistir ao atalho") || result.responseText.contains("primeiro passo"))
    assertFalse(result.isCompleted)
  }

  @Test
  fun `verify Socratic Tutor responds intelligently across multiple subjects`() {
    val engine = SocraticTutorEngine()

    val portResult = engine.processUserInput("O que é uma metáfora?")
    assertTrue(portResult.responseText.contains("Metáfora"))

    val ingResult = engine.processUserInput("O que significa actually em inglês?")
    assertTrue(ingResult.responseText.contains("Actually"))

    val histResult = engine.processUserInput("Como foi a revolução industrial?")
    assertTrue(histResult.responseText.contains("Revolução Industrial"))

    val mathResult = engine.processUserInput("Como calcular porcentagem?")
    assertTrue(mathResult.responseText.contains("Porcentagens"))
  }

  @Test
  fun `verify 5 distinct characters available for user selection`() {
    val models = QuestIaData.AVATAR_3D_MODELS
    assertEquals(5, models.size)
    val ids = models.map { it.id }.toSet()
    assertTrue(ids.contains("hero_arcane"))
    assertTrue(ids.contains("hero_paladin"))
    assertTrue(ids.contains("hero_mage"))
    assertTrue(ids.contains("hero_ranger"))
    assertTrue(ids.contains("hero_valkyrie"))
  }

  @Test
  fun `verify default guildas exist`() {
    val guildas = QuestIaData.DEFAULT_GUILDAS
    assertTrue(guildas.size >= 5)
    val ranks = guildas.map { it.rank }
    assertEquals(listOf(1, 2, 3, 4, 5), ranks)
  }

  @Test
  fun `verify math equations and latex bugs are cleaned properly`() {
    val raw = "A equação canônica é \$\$ax2 + bx + c == 0\$\$ e a da missão é \$\$x2 + 4x - 11 == 0\$\$."
    val cleaned = com.example.ui.components.MathMarkdownFormatter.cleanMathAndEquations(raw)
    assertFalse("Cleaned string should not contain raw double dollars", cleaned.contains("$$"))
    assertFalse("Cleaned string should not contain double equals", cleaned.contains("=="))
    assertTrue("Should contain ax² + bx + c = 0", cleaned.contains("ax² + bx + c = 0"))
    assertTrue("Should contain x² + 4x - 11 = 0", cleaned.contains("x² + 4x - 11 = 0"))
  }

  @Test
  fun `verify markdown asterisks are parsed into bold styles`() {
    val raw = "Observe os coeficientes: *A = 1*, **B = 4** e *C = -11* na equação."
    val annotated = com.example.ui.components.MathMarkdownFormatter.buildMarkdownAnnotatedString(raw)
    // The rendered text should not show the surrounding asterisks literally
    assertFalse("Rendered text should not contain literal double asterisks", annotated.text.contains("**"))
    assertTrue("Annotated string should have span styles for bold", annotated.spanStyles.isNotEmpty())
  }

  @Test
  fun `verify direct quest completion updates state and subject trail`() {
    val vm = QuestiaViewModel()
    val initialQuests = vm.uiState.value.quests
    val matQuest = initialQuests.first { it.id == "quest_matematica" }
    assertFalse("Quest should start not completed", matQuest.isCompleted)

    vm.completeQuestDirectly("quest_matematica")

    val updatedQuests = vm.uiState.value.quests
    val updatedMatQuest = updatedQuests.first { it.id == "quest_matematica" }
    assertTrue("Quest should be completed after direct completion", updatedMatQuest.isCompleted)

    val matTrail = vm.uiState.value.userTrails.first { it.subject == QuestSubject.MATEMATICA }
    assertTrue("Math trail should have gained progress", matTrail.progress >= 25)
  }

  @Test
  fun `verify UserQuestiaProfile models completed quests and all 4 trails accurately`() {
    val profile = com.example.data.local.UserQuestiaProfile(
      userName = "Guerreiro Teste",
      isRegistered = true,
      heroLevel = 3,
      heroXp = 850,
      heroGold = 320,
      trailMatematicaProgress = 50,
      trailMatematicaLevel = 2,
      trailPortuguesProgress = 25,
      trailPortuguesLevel = 1,
      trailInglesProgress = 75,
      trailInglesLevel = 1,
      trailHistoriaProgress = 25,
      trailHistoriaLevel = 1,
      completedQuestsCsv = "quest_matematica,quest_portugues"
    )

    assertEquals("Guerreiro Teste", profile.userName)
    assertEquals(850, profile.heroXp)
    val completedList = profile.completedQuestsCsv.split(",")
    assertTrue(completedList.contains("quest_matematica"))
    assertTrue(completedList.contains("quest_portugues"))
    assertEquals(50, profile.trailMatematicaProgress)
  }
}
