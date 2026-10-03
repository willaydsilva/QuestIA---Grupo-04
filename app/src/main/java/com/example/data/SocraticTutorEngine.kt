package com.example.data

import com.example.model.AxisFeedback
import com.example.model.Quest

enum class QuestRiddlePhase {
  COEFFICIENTS, // Step 1: Identifying A, B, C
  DISCRIMINANT, // Step 2: Calculating Delta = b² - 4ac
  BHASKARA,     // Step 3: Applying Bhaskara x = (-b ± √Δ) / 2a
  COMPLETED     // Solved! The Illusion door unlocks
}

data class TutorStepResult(
  val responseText: String,
  val feedbackList: List<AxisFeedback>,
  val nextPhase: QuestRiddlePhase,
  val isCompleted: Boolean = false,
  val equationHighlight: String? = null
)

class SocraticTutorEngine {

  var currentPhase: QuestRiddlePhase = QuestRiddlePhase.COEFFICIENTS
    private set

  fun reset() {
    currentPhase = QuestRiddlePhase.COEFFICIENTS
  }

  fun processUserInput(input: String, activeQuest: Quest? = null): TutorStepResult {
    val clean = input.trim().lowercase()
    val normalized = clean.replace(" ", "")

    // 0. ENEM 2025 Quests Routing
    val questId = activeQuest?.id ?: ""
    if (questId == "quest_enem_heroi_cartesiano" || clean.contains("153") || (clean.contains("herói") && clean.contains("vilão")) || (clean.contains("heroi") && clean.contains("vilao")) || clean.contains("equidistante")) {
      return processEnem153HeroiCartesiano(clean)
    }

    if (questId == "quest_enem_tijolos_ecologicos" || clean.contains("148") || clean.contains("tijolo") || clean.contains("tijolos") || (clean.contains("operário") && clean.contains("6 horas")) || (clean.contains("operario") && clean.contains("6 horas"))) {
      return processEnem148Tijolos(clean)
    }

    if (questId == "quest_enem_gnv" || clean.contains("141") || clean.contains("gnv") || clean.contains("cilindro") || clean.contains("abastecimento semanal")) {
      return processEnem141Gnv(clean)
    }

    if (questId == "quest_enem_hercules" || clean.contains("44") || clean.contains("hércules") || clean.contains("hercules") || clean.contains("joão antonio") || clean.contains("joao antonio")) {
      return processEnem44Hercules(clean)
    }

    if (questId == "quest_enem_jargoes" || clean.contains("23") || clean.contains("cringe") || clean.contains("jargão") || clean.contains("jargao") || clean.contains("sarrafo alto") || clean.contains("ressignificar")) {
      return processEnem23Jargoes(clean)
    }

    if (questId == "quest_enem_resilience" || clean.contains("01") || clean.contains("snowflake") || clean.contains("resilience") || clean.contains("resiliência") || clean.contains("resiliencia")) {
      return processEnem01Resilience(clean)
    }

    if (questId == "quest_enem_reforma_eleitoral" || clean.contains("47") || clean.contains("1881") || clean.contains("lei saraiva") || clean.contains("reforma eleitoral")) {
      return processEnem47ReformaEleitoral(clean)
    }

    if (questId == "quest_enem_platao_polis" || clean.contains("87") || clean.contains("platão") || clean.contains("platao") || clean.contains("cidade justa") || clean.contains("pólis justa")) {
      return processEnem87Platao(clean)
    }

    // 1. Saudações e Apresentação
    if (clean == "oi" || clean == "olá" || clean == "ola" || clean == "opa" || clean.startsWith("bom dia") || clean.startsWith("boa tarde") || clean.startsWith("boa noite") || clean.contains("quem é você") || clean.contains("como funciona")) {
      return TutorStepResult(
        responseText = """
          🔮 **Saudações, Nobre Guerreiro do Saber!**
          
          Eu sou o **Copiloto da Luz**, seu mentor de Inteligência Artificial contra a distração e a névoa do Brain Rot. Meu juramento pedagógico é guiá-lo pelo **Método Socrático**: eu não entrego respostas prontas, mas forneço pistas estratégicas, analogias e perguntas instigantes para que o seu próprio cérebro conquiste o conhecimento!
          
          Podemos explorar desafios e dúvidas em:
          📐 **Matemática** (Álgebra, Bhaskara, Funções, Geometria)
          📜 **Português** (Redação ENEM, Figuras de Linguagem, Gramática)
          🌍 **Inglês** (Interpretação Instrumental, Falsos Cognatos)
          🏛️ **História** (Cidadania, Revoluções, Sociedade)
          
          Por qual enigma ou matéria você deseja começar nossa reflexão hoje?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Autonomia", +10, "Início da sessão de mentoria socrática"),
          AxisFeedback("Pesquisa", +5, "Abertura para reflexão ativa")
        ),
        nextPhase = currentPhase
      )
    }

    // 2. Pedidos de resposta mastigada / atalho
    if (clean.contains("me dá a resposta") || clean.contains("qual a resposta") || clean.contains("fala a resposta") || clean.contains("resolve pra mim") || clean.contains("dá a resposta") || clean.contains("qual o resultado")) {
      return TutorStepResult(
        responseText = """
          ⚔️ **Resistir ao atalho é o primeiro teste de um verdadeiro Herói!**
          
          A Máquina da Ilusão quer que você consuma respostas prontas para enfraquecer seu raciocínio. No Reino de QuestIA, o poder está em **construir o caminho**.
          
          Diga-me: qual é o primeiro elemento que você consegue identificar no problema? Que tal começarmos pelo passo inicial juntos?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Autonomia", +15, "Recusa da resposta pronta e compromisso com o raciocínio"),
          AxisFeedback("Argumentação", +10, "Estímulo à dedução ativa")
        ),
        nextPhase = currentPhase
      )
    }

    // 3. MATEMÁTICA: Equação do 2º Grau e Bhaskara (Missão de Matemática)
    if (clean.contains("x²") || clean.contains("x2") || clean.contains("x^2") || clean.contains("ax2") || clean.contains("equação") || clean.contains("equacao") || clean.contains("bhaskara") || clean.contains("delta") || clean.contains("coeficiente") || clean.contains("segundo grau") || clean.contains("2º grau") || currentPhase != QuestRiddlePhase.COMPLETED && (clean.contains("a=") || clean.contains("b=") || clean.contains("c=") || clean.contains("60") || clean.contains("raiz"))) {
      return processMathEquation(clean, normalized)
    }

    // 4. MATEMÁTICA: Equação do 1º Grau, Frações, Porcentagem e Geometria
    if (clean.contains("porcentagem") || clean.contains("por cento") || clean.contains("%") || clean.contains("fração") || clean.contains("fracao") || clean.contains("pitágoras") || clean.contains("pitagoras") || clean.contains("geometria") || clean.contains("triângulo") || clean.contains("área") || clean.contains("função") || clean.contains("funcao")) {
      return processGeneralMath(clean)
    }

    // 5. PORTUGUÊS: Redação ENEM & Argumentação
    if (clean.contains("redação") || clean.contains("redacao") || clean.contains("enem") || clean.contains("dissertativa") || clean.contains("intervenção") || clean.contains("intervencao") || clean.contains("tese") || clean.contains("parágrafo")) {
      return TutorStepResult(
        responseText = """
          📜 **A Arquitetura Sagrada da Redação Nota 1000 do ENEM:**
          
          Um texto dissertativo-argumentativo poderoso não nasce do improviso, mas de uma estrutura firme em 4 atos:
          
          1. **Introdução**: Contextualização do tema + apresentação clara de **duas teses (causas/problemas)**.
          2. **D1 (Desenvolvimento 1)**: Argumentação e repertório legitimado da primeira tese.
          3. **D2 (Desenvolvimento 2)**: Aprofundamento crítico da segunda tese.
          4. **Proposta de Intervenção**: Os 5 elementos obrigatórios (*Agente, Ação, Meio/Modo, Efeito e Detalhamento*).
          
          Pense no tema: *"Os impactos do uso excessivo de telas na saúde mental dos jovens"*. Qual repertório histórico, filosófico ou sociológico você convocaria para abrir seu texto?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Argumentação", +25, "Estruturação de raciocínio dissertativo para o ENEM"),
          AxisFeedback("Pesquisa", +20, "Articulação de repertório sociocultural")
        ),
        nextPhase = currentPhase,
        equationHighlight = "Redação ENEM: Os 5 Elementos"
      )
    }

    // 6. PORTUGUÊS: Figuras de Linguagem
    if (clean.contains("figura") || clean.contains("metáfora") || clean.contains("metafora") || clean.contains("metonímia") || clean.contains("metonimia") || clean.contains("antítese") || clean.contains("antitese") || clean.contains("hipérbole") || clean.contains("hiperbole") || clean.contains("paradoxo") || clean.contains("ironia")) {
      return TutorStepResult(
        responseText = """
          📜 **As Figuras de Linguagem — As Armas Místicas da Oratória:**
          
          As figuras de linguagem tornam a mensagem expressiva e perspicaz:
          
          • **Metáfora**: Comparação direta sem termo comparativo (*"Seu foco é uma espada reluzente"*).
          • **Comparação**: Possui conectivo explícito (*"Seu foco é como uma espada"*).
          • **Metonímia**: Substitui um termo por outro afim (*"Devorei Machado de Assis ontem à noite"*).
          • **Antítese**: Aproxima ideias opostas (*"Guerra e paz, sombra e luz"*).
          • **Hipérbole**: O exagero deliberado (*"Esperei uma eternidade pelo sinal da aula"*).
          
          Se dissermos: *"O Copiloto é a bússola que orienta o estudante"*, trata-se de uma metáfora ou de uma metonímia? Qual elemento chave você percebe aí?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Argumentação", +20, "Identificação e análise de recursos expressivos da língua"),
          AxisFeedback("Pesquisa", +15, "Diferenciação conceitual de figuras de linguagem")
        ),
        nextPhase = currentPhase,
        equationHighlight = "Português: Domínio das Figuras"
      )
    }

    // 7. PORTUGUÊS: Crase e Gramática
    if (clean.contains("crase") || clean.contains("à") || clean.contains("concordância") || clean.contains("concordancia") || clean.contains("regência") || clean.contains("regencia") || clean.contains("vírgula") || clean.contains("virgula") || clean.contains("gramática") || clean.contains("gramatica")) {
      return TutorStepResult(
        responseText = """
          ✒️ **O Mistério do Acento Grave (A Crase):**
          
          A crase (à) é a fusão de duas energias: a **preposição 'a'** (exigida por um verbo ou nome) + o **artigo feminino 'a'** que acompanha uma palavra feminina.
          
          💡 **A Regra de Ouro Socrática**:
          Troque a palavra feminina seguinte por uma masculina equivalente.
          • Se virar **"ao"**, tem crase com acento grave!
          • Exemplo: *"Vou à escola"* -> *"Vou ao colégio"*. (Virou 'ao', então leva crase!).
          
          E na frase: *"Estou pronto para ir ___ praia"*, usando o teste do masculino com *"parque"*, como fica a regência?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Acerto", +20, "Compreensão da regra prática da crase"),
          AxisFeedback("Autonomia", +15, "Aplicação do teste de substituição sintática")
        ),
        nextPhase = currentPhase,
        equationHighlight = "Gramática: Fusão da Crase (a + a = à)"
      )
    }

    // 8. INGLÊS: Falsos Cognatos & Leitura Instrumental
    if (clean.contains("inglês") || clean.contains("ingles") || clean.contains("english") || clean.contains("cognat") || clean.contains("false friend") || clean.contains("actually") || clean.contains("pretend") || clean.contains("push") || clean.contains("reading")) {
      return TutorStepResult(
        responseText = """
          🌍 **The Cyber-Scrolls — Decifrando o Inglês Instrumental:**
          
          No vestibular e no mundo tech, os **False Friends (falsos cognatos)** são armadilhas clássicas que você precisa desarmar:
          
          • **Actually**: Significa *na realidade / de fato* (NÃO significa atualmente — 'atualmente' é *currently*!).
          • **Pretend**: Significa *fingir* (NÃO significa pretender — 'pretender' é *intend*!).
          • **Push**: Significa *empurrar* (NÃO significa puxar — 'puxar' é *pull*!).
          • **Parents**: Significa *pais (pai e mãe)* (NÃO parentes — 'parentes' é *relatives*!).
          
          Se um scroll disser: *"She actually pretended to study, but she was on social media"*, como você interpretaria o sentido real dessa frase?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Pesquisa", +20, "Identificação de falsos cognatos em inglês"),
          AxisFeedback("Autonomia", +20, "Interpretação textual contextualizada")
        ),
        nextPhase = currentPhase,
        equationHighlight = "English: False Friends & Vocabulary"
      )
    }

    // 9. HISTÓRIA: Revoluções & Cidadania
    if (clean.contains("história") || clean.contains("historia") || clean.contains("revolução") || clean.contains("revolucao") || clean.contains("industrial") || clean.contains("cidadania") || clean.contains("direitos") || clean.contains("constituição") || clean.contains("guerra") || clean.contains("vargas") || clean.contains("república") || clean.contains("republica")) {
      return TutorStepResult(
        responseText = """
          🏛️ **As Crônicas Históricas da Cidadania e Revoluções:**
          
          A História nos ensina que direitos não são concessões gratuitas: são frutos de lutas e consciência coletiva:
          
          • **Revolução Industrial (Inglaterra, séc. XVIII)**:
            - Substituição da força humana pela máquina a vapor.
            - Êxodo rural maciço e nascimento da classe operária (proletariado).
            - Jornadas de até 16h diárias sem direitos trabalhistas iniciais.
          
          • **A Tríade dos Direitos da Cidadania (T.H. Marshall)**:
            1. **Civis** (séc. XVIII): Liberdade de expressão, ir e vir, propriedade.
            2. **Políticos** (séc. XIX): Voto, elegibilidade, manifestação.
            3. **Sociais** (séc. XX): Educação, saúde, previdência e dignidade no trabalho.
          
          Reflita: Como o surgimento da inteligência artificial e da automação hoje se conecta aos dilemas vividos pelos artesãos na 1ª Revolução Industrial?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Argumentação", +25, "Análise comparativa entre marcos históricos e a era tecnológica"),
          AxisFeedback("Pesquisa", +20, "Compreensão da evolução dos direitos de cidadania")
        ),
        nextPhase = currentPhase,
        equationHighlight = "História: Cidadania & Revolução Industrial"
      )
    }

    // 10. FOCO E COMBATE AO BRAIN ROT
    if (clean.contains("brain rot") || clean.contains("foco") || clean.contains("distração") || clean.contains("distracao") || clean.contains("celular") || clean.contains("estudar") || clean.contains("atenção") || clean.contains("atencao") || clean.contains("procrastin") || clean.contains("cansaço") || clean.contains("cansaco") || clean.contains("sono")) {
      return TutorStepResult(
        responseText = """
          🛡️ **O Escudo Anti-Brain Rot do Guerreiro Estudante:**
          
          O "Brain Rot" é a fragmentação da atenção provocada pelo fluxo ininterrupto de vídeos curtos e notificações. O cérebro acostuma-se à dopamina sem esforço e perde a capacidade de foco profundo (*Deep Work*).
          
          Para retomar a sua armadura mental:
          1. **Protocolo 25/5 (Pomodoro)**: 25 minutos de estudo com foco absoluto e tela do celular virada para baixo em outro local.
          2. **Recuperação Ativa (*Active Recall*)**: Em vez de reler passivamente, feche o caderno e tente explicar o conceito em voz alta para você mesmo.
          3. **Intenção de Batalha**: Defina UMA única meta antes de abrir o livro: *"Hoje eu vou dominar o cálculo de Delta"*.
          
          Que tal iniciarmos agora um ciclo de 15 minutos em uma das matérias? Qual desafio você escolhe encarar?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Autonomia", +25, "Desenvolvimento de estratégias ativas contra o Brain Rot"),
          AxisFeedback("Correção", +15, "Diagnóstico e enfrentamento de hábitos dispersivos")
        ),
        nextPhase = currentPhase
      )
    }

    // 11. Dúvidas gerais ou pedidos de ajuda
    return TutorStepResult(
      responseText = """
        💡 **O Copiloto da Luz acolhe sua dúvida!**
        
        Você trouxe uma reflexão valiosa. Para avançarmos de maneira socrática:
        
        1. Qual é o conceito central ou fórmula que você lembra a respeito disso?
        2. Que parte parece mais misteriosa ou desafiadora no momento?
        
        Compartilhe sua linha de raciocínio e eu lhe darei a pista precisa para desbloquear o próximo nível!
      """.trimIndent(),
      feedbackList = listOf(
        AxisFeedback("Argumentação", +10, "Estímulo à formulação clara da dúvida"),
        AxisFeedback("Autonomia", +10, "Busca ativa por soluções conceituais")
      ),
      nextPhase = currentPhase
    )
  }

  private fun processMathEquation(clean: String, normalized: String): TutorStepResult {
    return when (currentPhase) {
      QuestRiddlePhase.COEFFICIENTS -> evaluateCoefficients(clean, normalized)
      QuestRiddlePhase.DISCRIMINANT -> evaluateDiscriminant(clean, normalized)
      QuestRiddlePhase.BHASKARA -> evaluateBhaskara(clean, normalized)
      QuestRiddlePhase.COMPLETED -> TutorStepResult(
        responseText = "🎉 **O portal das equações sagradas já foi conquistado por você!**\n\nSua mente agora domina a lógica quadrática. Deseja revisar o método socrático ou avançar para outra matéria?",
        feedbackList = listOf(
          AxisFeedback("Autonomia", +10, "Domínio consolidado da equação de 2º grau")
        ),
        nextPhase = QuestRiddlePhase.COMPLETED,
        isCompleted = true
      )
    }
  }

  private fun evaluateCoefficients(clean: String, normalized: String): TutorStepResult {
    val hasA1 = (normalized.contains("a=1") || normalized.contains("a:1") || normalized.contains("aé1") || normalized.contains("1,4,-11") || (normalized.contains("1") && normalized.contains("4") && normalized.contains("-11")))
    val hasB4 = (normalized.contains("b=4") || normalized.contains("b:4") || normalized.contains("bé4") || normalized.contains("4"))
    val hasCMinus11 = (normalized.contains("c=-11") || normalized.contains("c:-11") || normalized.contains("-11"))
    val hasCPlus11 = (normalized.contains("c=11") || normalized.contains("c:11") || normalized.contains("11")) && !normalized.contains("-11")

    if (hasA1 && hasB4 && hasCMinus11) {
      currentPhase = QuestRiddlePhase.DISCRIMINANT
      return TutorStepResult(
        responseText = """
          ✨ **Visão Racional Impecável, Aprendiz!**
          
          Você identificou com maestria os três guardiões da equação `x² + 4x - 11 = 0`:
          • **A = 1** (o coeficiente de x²)
          • **B = 4** (o coeficiente de x)
          • **C = -11** (o termo constante independente)
          
          Agora, a fechadura rúnica pulsa e requer o **Discriminante Sagrado (Δ - Delta)**!
          
          Lembrando da fórmula `Δ = b² - 4ac`:
          Qual é o valor de `4²` e quanto resulta a multiplicação `-4 · (1) · (-11)`?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Acerto", +25, "Identificou corretamente os coeficientes A=1, B=4, C=-11"),
          AxisFeedback("Argumentação", +15, "Associação estrutural perfeita à forma ax² + bx + c = 0"),
          AxisFeedback("Autonomia", +20, "Dedução precisa sem atalhos")
        ),
        nextPhase = QuestRiddlePhase.DISCRIMINANT,
        equationHighlight = "A = 1, B = 4, C = -11"
      )
    }

    if (hasCPlus11 && !hasCMinus11) {
      return TutorStepResult(
        responseText = """
          🛡️ **Atenção aos detalhes, Nobre Estudante!**
          
          Você encontrou A = 1 e B = 4 com perfeição! Porém, observe com atenção a equação na porta rúnica:
          `x² + 4x - 11 = 0`
          
          O sinal antes do número 11 é uma subtração (-). Na forma canônica `ax² + bx + c = 0`, que sinal deve acompanhar o coeficiente C?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Correção", +15, "Atenção guiada ao sinal negativo do coeficiente C"),
          AxisFeedback("Acerto", +10, "Coeficientes A e B corretos")
        ),
        nextPhase = QuestRiddlePhase.COEFFICIENTS,
        equationHighlight = "- 11"
      )
    }

    return TutorStepResult(
      responseText = """
        💡 **O Copiloto ilumina a estrutura canônica:**
        
        Toda equação de 2º grau se molda como:
        `a·x² + b·x + c = 0`
        
        Compare com a nossa missão:
        `1·x² + 4·x + (-11) = 0`
        
        Quem é o multiplicador `a` ao lado de x²?
        Quem é o número `b` ao lado de x?
        E qual é o número isolado `c` no final?
      """.trimIndent(),
      feedbackList = listOf(
        AxisFeedback("Pesquisa", +15, "Estudo comparativo dos coeficientes polinomiais"),
        AxisFeedback("Argumentação", +10, "Associação estrutural")
      ),
      nextPhase = QuestRiddlePhase.COEFFICIENTS,
      equationHighlight = "ax² + bx + c = 0"
    )
  }

  private fun evaluateDiscriminant(clean: String, normalized: String): TutorStepResult {
    if (normalized.contains("60") || normalized.contains("delta=60") || normalized.contains("deltaé60") || normalized.contains("d=60")) {
      currentPhase = QuestRiddlePhase.BHASKARA
      return TutorStepResult(
        responseText = """
          ⚡ **Um clarão dourado irrompe das pedras!**
          
          Exatamente! **Δ = 60**.
          `Δ = 4² - [4 · 1 · (-11)] = 16 - (-44) = 16 + 44 = 60`.
          
          Como Δ > 0, sabemos com certeza matemática que a equação possui **duas raízes reais e distintas**!
          
          Agora chega a etapa final: a fórmula suprema de **Bhaskara**:
          `x = (-b ± √Δ) / (2a)`
          
          Substituindo `b = 4`, `Δ = 60` e `a = 1`, como fica a expressão antes de simplificarmos a raiz?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Acerto", +30, "Cálculo impecável do discriminante Δ = 60"),
          AxisFeedback("Correção", +20, "Superou a regra dos sinais (- com - = +)"),
          AxisFeedback("Autonomia", +20, "Dedução rápida e independente")
        ),
        nextPhase = QuestRiddlePhase.BHASKARA,
        equationHighlight = "Δ = 60"
      )
    }

    if (normalized.contains("-28") || normalized.contains("28")) {
      return TutorStepResult(
        responseText = """
          ⚠️ **Cuidado com a armadilha dos sinais!**
          
          Você calculou `4² = 16` e `4 · 1 · 11 = 44`. Muito bem!
          Mas note a fórmula sagrada: `Δ = b² - 4·a·c`.
          Ou seja: `16 - [4 · 1 · (-11)]` = `16 - (-44)`.
          
          O que acontece na aritmética quando subtraímos um número negativo? (Menos com menos?)
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Correção", +20, "Revisão da regra de sinais na subtração de números negativos")
        ),
        nextPhase = QuestRiddlePhase.DISCRIMINANT,
        equationHighlight = "16 - (-44) = 16 + 44"
      )
    }

    return TutorStepResult(
      responseText = """
        📜 **Pista para o cálculo do Delta:**
        `Δ = b² - 4·a·c`
        
        Substituindo `a = 1`, `b = 4` e `c = -11`:
        1. `4²` = 16.
        2. `-4 · 1 · (-11)` = +44.
        
        Quanto dá a soma de 16 com 44?
      """.trimIndent(),
      feedbackList = listOf(
        AxisFeedback("Pesquisa", +10, "Passo a passo do cálculo do discriminante")
      ),
      nextPhase = QuestRiddlePhase.DISCRIMINANT,
      equationHighlight = "Δ = b² - 4ac"
    )
  }

  private fun evaluateBhaskara(clean: String, normalized: String): TutorStepResult {
    val isSolved = normalized.contains("-2+√15") || normalized.contains("-2-√15") || normalized.contains("-2±√15") ||
      (normalized.contains("-2") && normalized.contains("15")) ||
      normalized.contains("(-4±√60)/2") || normalized.contains("-4±√60")

    if (isSolved) {
      currentPhase = QuestRiddlePhase.COMPLETED
      return TutorStepResult(
        responseText = """
          🎉 **A RAZÃO PURA DESPEDACIA AS CORRENTES DO BRAIN ROT!**
          
          A porta sagrada se abre com estrondo! As raízes exatas foram desvendadas:
          
          `x = (-4 ± √60) / 2`
          Como `60 = 4 · 15`, temos `√60 = 2√15`:
          `x = (-4 ± 2√15) / 2 = -2 ± √15`
          
          🌟 **Raiz 1**: `x₁ = -2 + √15` (≈ 1,87)
          🌟 **Raiz 2**: `x₂ = -2 - √15` (≈ -5,87)
          
          Você venceu a missão por meio do próprio raciocínio dedutivo! Conquistou **+350 XP** para o seu Herói e para sua Guilda Escolar!
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Acerto", +35, "Raízes exatas da equação quadrática deduzidas com maestria"),
          AxisFeedback("Argumentação", +25, "Aplicação e simplificação correta de radicais"),
          AxisFeedback("Autonomia", +30, "Conclusão triunfante da missão pelo método socrático"),
          AxisFeedback("Correção", +20, "Raciocínio analítico sem depender de atalhos")
        ),
        nextPhase = QuestRiddlePhase.COMPLETED,
        isCompleted = true,
        equationHighlight = "x = -2 ± √15"
      )
    }

    return TutorStepResult(
      responseText = """
        🕯️ **Pista final para libertar as raízes:**
        
        Substituindo na fórmula de Bhaskara:
        `x = (-4 ± √60) / 2`
        
        Pense em como simplificar `√60`: como `60 = 4 · 15`, a raiz quadrada de 4 salta para fora como `2`.
        Logo: `√60 = 2√15`.
        
        Dividindo `-4` e `2√15` por `2`, qual resultado final você obtém para x?
      """.trimIndent(),
      feedbackList = listOf(
        AxisFeedback("Pesquisa", +15, "Estudo da simplificação de radicais"),
        AxisFeedback("Autonomia", +10, "Reta final da dedução matemática")
      ),
      nextPhase = QuestRiddlePhase.BHASKARA,
      equationHighlight = "x = (-4 ± 2√15) / 2"
    )
  }

  private fun processGeneralMath(clean: String): TutorStepResult {
    if (clean.contains("porcentagem") || clean.contains("%") || clean.contains("por cento")) {
      return TutorStepResult(
        responseText = """
          📐 **A Sabedoria das Porcentagens:**
          
          "Por cento" significa literalmente "por cada cem" (dividir por 100).
          
          💡 **Truques Mentais Socráticos**:
          • **10%**: Basta andar a vírgula uma casa para a esquerda (ex: 10% de 80 = 8).
          • **1%**: Ande duas casas para a esquerda (ex: 1% de 80 = 0,8).
          • **20%**: Calcule 10% e multiplique por 2 (ex: 10% de 80 = 8 -> 8 × 2 = 16).
          • **50%**: É exatamente a metade!
          
          Que valor você gostaria de calcular agora usando essa lógica intuitiva?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Acerto", +20, "Aplicação de raciocínio proporcional e cálculo mental"),
          AxisFeedback("Autonomia", +15, "Domínio de métodos ágeis de porcentagem")
        ),
        nextPhase = currentPhase,
        equationHighlight = "Porcentagem: Fração Centesimal"
      )
    }

    if (clean.contains("fração") || clean.contains("fracao")) {
      return TutorStepResult(
        responseText = """
          📐 **O Segredo das Frações:**
          
          Uma fração representa partes de um todo: o **numerador** (em cima) diz quantas partes temos; o **denominador** (embaixo) diz em quantas partes o todo foi dividido.
          
          Para somar frações com denominadores diferentes (ex: `1/2 + 1/3`):
          Elas precisam estar no mesmo "idioma" (denominador comum, via MMC).
          
          Qual cálculo com frações você está enfrentando agora?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Pesquisa", +15, "Conceituação geométrica de partes e todo"),
          AxisFeedback("Argumentação", +15, "Compreensão de equivalência fracionária")
        ),
        nextPhase = currentPhase
      )
    }

    return TutorStepResult(
      responseText = """
        📐 **Geometria & Raciocínio Espacial:**
        
        As formas geométricas regem o mundo material e a arquitetura! Seja no **Teorema de Pitágoras** (`a² = b² + c²`) para triângulos retângulos ou no cálculo de áreas e perímetros, cada fórmula reflete uma harmonia lógica.
        
        Qual figura ou problema geométrico estamos analisando? Descreva os lados ou medidas que você conhece!
      """.trimIndent(),
      feedbackList = listOf(
        AxisFeedback("Pesquisa", +15, "Exploração de propriedades geométricas")
      ),
      nextPhase = currentPhase
    )
  }

  // ==========================================
  // ENEM 2025 HANDLERS COM ESCADA PEDAGÓGICA
  // ==========================================

  // 1. MATEMÁTICA - Questão 153 (ENEM 2025): Herói e Vilões no Plano Cartesiano
  private fun processEnem153HeroiCartesiano(clean: String): TutorStepResult {
    // Acerto final
    if (clean.contains("y = -3x + 20") || clean.contains("y=-3x+20") || clean.contains("-3x + 20") || clean == "a" || clean.startsWith("letra a") || clean.startsWith("opção a") || clean.startsWith("opcao a")) {
      return TutorStepResult(
        responseText = """
          🏆 **Parabéns, Nobre Estrategista! Desafio do ENEM 2025 superado!**
          
          Você encontrou com maestria a equação da trajetória de fuga: **`y = -3x + 20`** (Alternativa A)!
          
          Ao manter o herói sobre a reta mediatriz que divide o plano entre os vilões, nenhum dos dois consegue ser o mais próximo, garantindo a sua invulnerabilidade com o poder da Geometria Analítica!
          
          ⚔️ *+420 XP Conquistados! Ouro e honra adicionados ao seu perfil!*
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Acerto", +30, "Determinação da equação da reta mediatriz no ENEM 2025"),
          AxisFeedback("Autonomia", +25, "Raciocínio analítico independente"),
          AxisFeedback("Argumentação", +20, "Aplicação lógica de geometria analítica em jogos")
        ),
        nextPhase = QuestRiddlePhase.COMPLETED,
        isCompleted = true,
        equationHighlight = "ENEM 2025 Q.153: y = -3x + 20"
      )
    }

    // Se continuar com dificuldade / pedir fórmula
    if (clean.contains("fórmula") || clean.contains("formula") || clean.contains("qual a fórmula") || clean.contains("como calcular") || clean.contains("travado") || clean.contains("não consigo")) {
      return TutorStepResult(
        responseText = """
          📐 **A Fórmula do Caminho Sagrado:**
          
          Para encontrar a reta no plano cartesiano, usamos a **Equação Fundamental da Reta**:
          `y - y₀ = m · (x - x₀)`
          
          Onde:
          • `(x₀, y₀)` é o ponto por onde o herói parte: `S(6, 2)`, ou seja, `x₀ = 6` e `y₀ = 2`.
          • `m` é o coeficiente angular da trajetória no quadrado: `m = -3`.
          
          Substituindo esses valores sagrados:
          `y - 2 = -3 · (x - 6)`
          `y - 2 = -3x + 18`
          
          Agora dê o passo final: somando 2 a ambos os lados da equação, **qual é a equação reduzida `y = ax + b` resultante?**
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Pesquisa", +20, "Estudo da equação fundamental da reta"),
          AxisFeedback("Correção", +15, "Substituição orientada de coordenadas")
        ),
        nextPhase = currentPhase,
        equationHighlight = "Equação da Reta: y - y₀ = m(x - x₀)"
      )
    }

    // Se apresentar primeira dúvida ou pedir dica
    if (clean.contains("dica") || clean.contains("ajuda") || clean.contains("não sei") || clean.contains("nao sei") || clean.contains("como assim") || clean.contains("o que fazer") || clean.contains("dúvida") || clean.contains("duvida")) {
      return TutorStepResult(
        responseText = """
          💡 **Dica Socrática da Sabedoria:**
          
          Pense no significado geométrico de **equidistância**:
          O conjunto de todos os pontos que estão à mesma distância de dois pontos fixos forma a **reta mediatriz** do segmento que os une!
          
          No quadrado desenhado na prova, essa linha divisória passa exatamente pelo ponto inicial do herói `S(6, 2)` e tem uma inclinação decrescente (`m = -3`).
          
          Se você tem um ponto `(x₀, y₀) = (6, 2)` e a inclinação `m = -3`, como podemos relacionar `x` e `y`? Você se lembra da equação da reta?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Argumentação", +20, "Compreensão do conceito de equidistância e mediatriz"),
          AxisFeedback("Autonomia", +15, "Reflexão sobre as propriedades geométricas da trajetória")
        ),
        nextPhase = currentPhase,
        equationHighlight = "Conceito: Reta Mediatriz Equidistante"
      )
    }

    // Apresentação inicial do problema
    return TutorStepResult(
      responseText = """
        🏛️ **[ENEM 2025 • Questão 153 - Matemática & Games]**
        
        Nobre Guerreiro, seja bem-vindo a este enigma oficial do ENEM 2025!
        
        Em um jogo digital, um herói e dois vilões movem-se num plano cartesiano. A regra de IA do jogo diz que o vilão mais próximo sempre ataca o herói. A única salvação é o herói percorrer uma trajetória que o mantenha rigorosamente **equidistante** dos vilões, partindo de S(6, 2) enquanto os vilões ocupam os vértices do quadrado.
        
        💡 *Como seu tutor socrático, não lhe darei a resposta pronta nem fórmulas de início: vamos construir a dedução passo a passo!*
        
        Pense comigo para o primeiro passo: **O que a questão está exigindo quando diz que o herói deve se manter equidistante dos vilões? Qual é a propriedade geométrica dessa trajetória?**
      """.trimIndent(),
      feedbackList = listOf(
        AxisFeedback("Autonomia", +15, "Início do desafio oficial do ENEM 2025"),
        AxisFeedback("Pesquisa", +10, "Interpretação do enunciado analítico")
      ),
      nextPhase = currentPhase,
      equationHighlight = "ENEM 2025 Q.153: Trajetória dos Heróis"
    )
  }

  // 2. MATEMÁTICA - Questão 148 (ENEM 2025): Fábrica de Tijolos Ecológicos
  private fun processEnem148Tijolos(clean: String): TutorStepResult {
    if (clean.contains("1800") || clean.contains("1.800") || clean == "d" || clean.startsWith("letra d") || clean.startsWith("opção d") || clean.startsWith("opcao d")) {
      return TutorStepResult(
        responseText = """
          🏆 **Parabéns, Guerreiro da Sustentabilidade! Desafio do ENEM 2025 superado!**
          
          A nova capacidade diária da fábrica é exatamente de **1.800 tijolos ecológicos** (Alternativa D)!
          
          Você deduziu que cada operário produz 40 tijolos por hora. Com 5 operários trabalhando 9 horas: `5 × 9 × 40 = 1.800` tijolos!
          
          ⚔️ *+380 XP Conquistados! Raciocínio proporcional impecável!*
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Acerto", +30, "Resolução exata da questão 148 do ENEM 2025"),
          AxisFeedback("Autonomia", +20, "Cálculo proporcional estruturado"),
          AxisFeedback("Argumentação", +20, "Domínio de regra de três composta e produtividade")
        ),
        nextPhase = QuestRiddlePhase.COMPLETED,
        isCompleted = true,
        equationHighlight = "ENEM 2025 Q.148: 1.800 Tijolos/dia"
      )
    }

    if (clean.contains("fórmula") || clean.contains("formula") || clean.contains("como calcular") || clean.contains("travado") || clean.contains("ajuda")) {
      return TutorStepResult(
        responseText = """
          📐 **A Fórmula da Proporcionalidade Composta:**
          
          Podemos modelar a produção como:
          `Produção = (Nº de Operários) × (Horas/dia) × (Taxa horária por operário)`
          
          1. Na situação inicial: `720 = 3 × 6 × Taxa` -> `720 = 18 × Taxa` -> `Taxa = 720 / 18 = 40 tijolos/hora por operário`.
          2. Na nova situação: `Produção = 5 operários × 9 horas/dia × 40 tijolos/hora`.
          
          Multiplique esses três valores: **quanto resulta `5 × 9 × 40`?**
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Pesquisa", +20, "Estruturação de equações de proporção"),
          AxisFeedback("Correção", +15, "Cálculo da taxa horária unitária")
        ),
        nextPhase = currentPhase,
        equationHighlight = "Taxa Unitária = 720 / (3 × 6) = 40"
      )
    }

    if (clean.contains("dica") || clean.contains("não sei") || clean.contains("nao sei") || clean.contains("dúvida") || clean.contains("duvida")) {
      return TutorStepResult(
        responseText = """
          💡 **Dica Socrática Reflexiva:**
          
          Divida o problema em duas etapas simples:
          • Primeiro: se 3 artesãos juntos produzem 720 tijolos em 6 horas, quantos tijolos o grupo produz em apenas 1 hora? (`720 / 6 = 120`).
          • Segundo: se os 3 juntos fazem 120 tijolos em 1 hora, quantos tijolos cada um faz individualmente por hora? (`120 / 3`).
          
          Quantos tijolos 1 único artesão produz por hora?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Argumentação", +20, "Decomposição do problema em taxas unitárias"),
          AxisFeedback("Autonomia", +15, "Estímulo à dedução lógica")
        ),
        nextPhase = currentPhase,
        equationHighlight = "Pista: Produção unitária por hora"
      )
    }

    return TutorStepResult(
      responseText = """
        🏛️ **[ENEM 2025 • Questão 148 - Matemática & Sustentabilidade]**
        
        Uma fábrica de tijolos ecológicos possui 3 funcionários que, trabalhando 6 horas por dia, produzem 720 unidades diárias. A demanda cresce e agora são 5 funcionários trabalhando 9 horas por dia, todos com a mesma produtividade individual.
        
        💡 *Não entregaremos respostas ou fórmulas prontas: vamos pensar criticamente!*
        
        Para começarmos: **Qual é o primeiro dado que você precisa descobrir sobre o ritmo de trabalho de cada funcionário antes de calcular a nova produção total?**
      """.trimIndent(),
      feedbackList = listOf(
        AxisFeedback("Autonomia", +15, "Início da questão 148 ENEM 2025"),
        AxisFeedback("Pesquisa", +10, "Análise de variáveis de produção")
      ),
      nextPhase = currentPhase,
      equationHighlight = "ENEM 2025 Q.148: Tijolos Ecológicos"
    )
  }

  // 3. MATEMÁTICA - Questão 141 (ENEM 2025): Autonomia e GNV
  private fun processEnem141Gnv(clean: String): TutorStepResult {
    if (clean.contains("17") || clean.contains("17 m³") || clean.contains("17m3") || clean == "c" || clean.startsWith("letra c") || clean.startsWith("opção c") || clean.startsWith("opcao c")) {
      return TutorStepResult(
        responseText = """
          🏆 **Parabéns, Nobre Condutor! Desafio do ENEM 2025 superado!**
          
          O cilindro correto é exatamente o de **17 m³** (Alternativa C)!
          
          Você calculou que a viagem semanal de `30 km × 7 = 210 km` exige `210 / 13 ≈ 16,15 m³` de gás. O cilindro de 14 m³ acabaria antes da semana terminar, logo o menor cilindro seguro e econômico é o de 17 m³!
          
          ⚔️ *+360 XP Conquistados! Aplicação brilhante de matemática no consumo inteligente!*
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Acerto", +30, "Cálculo exato da capacidade mínima no ENEM 2025"),
          AxisFeedback("Autonomia", +20, "Interpretação da margem de segurança do cilindro"),
          AxisFeedback("Argumentação", +20, "Raciocínio sobre consumo sustentável")
        ),
        nextPhase = QuestRiddlePhase.COMPLETED,
        isCompleted = true,
        equationHighlight = "ENEM 2025 Q.141: Cilindro de 17 m³"
      )
    }

    if (clean.contains("fórmula") || clean.contains("formula") || clean.contains("como calcular") || clean.contains("ajuda") || clean.contains("travado")) {
      return TutorStepResult(
        responseText = """
          📐 **A Relação de Consumo e Autonomia:**
          
          `Volume necessário (m³) = Distância Total da Semana (km) / Rendimento (km/m³)`
          
          1. Distância na semana: `30 km/dia × 7 dias = 210 km`.
          2. Rendimento: `13 km/m³`.
          3. Cálculo: `210 / 13 = 16,15 m³`.
          
          Os modelos disponíveis na loja são: **10, 14, 17, 21 e 25 m³**.
          Se ele precisa de 16,15 m³ e o preço aumenta com a capacidade, **qual é o menor cilindro que aguenta a semana sem faltar gás?**
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Pesquisa", +20, "Aplicação da fórmula de consumo de combustível"),
          AxisFeedback("Correção", +15, "Avaliação das opções comerciais")
        ),
        nextPhase = currentPhase,
        equationHighlight = "Consumo = 210 km / 13 km/m³ ≈ 16,15 m³"
      )
    }

    if (clean.contains("dica") || clean.contains("não sei") || clean.contains("nao sei") || clean.contains("dúvida") || clean.contains("duvida")) {
      return TutorStepResult(
        responseText = """
          💡 **Dica Socrática:**
          
          Primeiro, calcule quantos quilômetros o carro roda em uma semana completa (7 dias rodando 30 km a cada dia).
          Depois, sabendo que cada 1 m³ de gás dura 13 km, divida a quilometragem total por 13.
          
          Qual valor de volume você encontrou nessa divisão?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Argumentação", +20, "Cálculo da quilometragem total semanal"),
          AxisFeedback("Autonomia", +15, "Estruturação das grandezas envolvidas")
        ),
        nextPhase = currentPhase,
        equationHighlight = "Pista: 30 km × 7 dias = 210 km"
      )
    }

    return TutorStepResult(
      responseText = """
        🏛️ **[ENEM 2025 • Questão 141 - Matemática & Eficiência Energética]**
        
        Um motorista quer instalar um kit GNV. Seu carro rodará 30 km por dia, durante os 7 dias da semana, e tem rendimento de 1 m³ a cada 13 km rodados. Ele quer comprar o cilindro de menor preço (menor volume) que garanta abastecer apenas uma vez por semana, dentre as opções de 10, 14, 17, 21 e 25 m³.
        
        💡 *Como mentor socrático, não lhe darei fórmulas prontas de início: quero ver seu raciocínio crítico!*
        
        Diga-me: **Quantos quilômetros no total o veículo percorre ao longo dos 7 dias da semana?**
      """.trimIndent(),
      feedbackList = listOf(
        AxisFeedback("Autonomia", +15, "Início do desafio do GNV no ENEM 2025"),
        AxisFeedback("Pesquisa", +10, "Interpretação do consumo energético")
      ),
      nextPhase = currentPhase,
      equationHighlight = "ENEM 2025 Q.141: Rota do GNV"
    )
  }

  // 4. PORTUGUÊS - Questão 44 (ENEM 2025): Intertextualidade Hércules & Operário
  private fun processEnem44Hercules(clean: String): TutorStepResult {
    if (clean.contains("intertextualidade") || clean.contains("intertextual") || clean == "a" || clean.startsWith("letra a") || clean.startsWith("opção a") || clean.startsWith("opcao a")) {
      return TutorStepResult(
        responseText = """
          🏆 **Parabéns, Guerreiro da Literatura! Desafio do ENEM 2025 superado!**
          
          A resposta exata é a **intertextualidade com o mito de Hércules** (Alternativa A)!
          
          O conto de João Antonio da Silva — o trabalhador multitarefas que se desdobra em pedreiro, motorista e jardineiro — dialoga diretamente com os 'Doze Trabalhos' mitológicos, ressignificando a figura do herói na dureza da sobrevivência brasileira contemporânea!
          
          ⚔️ *+340 XP Conquistados! Visão crítica e domínio textual nota 1000!*
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Acerto", +30, "Identificação do recurso da intertextualidade"),
          AxisFeedback("Argumentação", +25, "Análise do diálogo entre literatura clássica e realidade social"),
          AxisFeedback("Pesquisa", +20, "Reconhecimento de recursos interdiscursivos no ENEM")
        ),
        nextPhase = QuestRiddlePhase.COMPLETED,
        isCompleted = true,
        equationHighlight = "ENEM 2025 Q.44: Intertextualidade Mitológica"
      )
    }

    if (clean.contains("dica") || clean.contains("ajuda") || clean.contains("não sei") || clean.contains("nao sei") || clean.contains("o que significa")) {
      return TutorStepResult(
        responseText = """
          💡 **Dica Socrática sobre Linguagens:**
          
          Quando um texto moderno faz referência direta a uma história antiga (como os 12 Trabalhos de Hércules na mitologia grega) e dá o apelido de 'Hércules' ao protagonista da crônica, como a teoria literária chama esse diálogo entre duas obras?
          
          Seria metalinguagem, intertextualidade ou ambiguidade?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Argumentação", +20, "Diferenciação de conceitos de teoria literária"),
          AxisFeedback("Pesquisa", +15, "Reflexão sobre diálogo entre textos")
        ),
        nextPhase = currentPhase,
        equationHighlight = "Conceito: Diálogo entre Dois Textos"
      )
    }

    return TutorStepResult(
      responseText = """
        🏛️ **[ENEM 2025 • Questão 44 - Linguagens & Sociedade]**
        
        O Texto I apresenta o mito grego dos Doze Trabalhos de Hércules (matar o Leão de Nemeia, a Hidra, limpar os estábulos). O Texto II narra a rotina de João Antonio da Silva, trabalhador brasileiro que faz bico de bombeiro, pedreiro, jardineiro e motorista, terminando com: *"Seu nome: João Antonio da Silva. Mas pode chamar de Hércules"*.
        
        💡 *Vamos refletir sem respostas prontas!*
        
        **Como o autor do Texto II utilizou a história clássica de Hércules para falar da realidade de um trabalhador comum no Brasil? Que recurso linguístico é esse?**
      """.trimIndent(),
      feedbackList = listOf(
        AxisFeedback("Autonomia", +15, "Início da análise literária do ENEM 2025"),
        AxisFeedback("Pesquisa", +10, "Compreensão de figuras e estratégias narrativas")
      ),
      nextPhase = currentPhase,
      equationHighlight = "ENEM 2025 Q.44: Os Trabalhos de Hércules"
    )
  }

  // 5. PORTUGUÊS - Questão 23 (ENEM 2025): Jargões e Clichês Modernos
  private fun processEnem23Jargoes(clean: String): TutorStepResult {
    if (clean.contains("humor") || clean.contains("ironia") || clean.contains("estruturas linguísticas") || clean.contains("estruturas linguisticas") || clean == "e" || clean.startsWith("letra e") || clean.startsWith("opção e") || clean.startsWith("opcao e")) {
      return TutorStepResult(
        responseText = """
          🏆 **Parabéns, Nobre Crítico! Desafio do ENEM 2025 superado!**
          
          A resposta é o **humor gerado pelo uso das estruturas linguísticas que são objeto da reflexão desenvolvida** (Alternativa E)!
          
          A autora critica expressões batidas como 'proativo', 'novo normal', 'lugar de fala', 'sarrafo alto' e 'cringe' justamente escrevendo seu texto com essas próprias expressões, criando uma sátira perspicaz sobre a automatização da linguagem!
          
          ⚔️ *+320 XP Conquistados! Consciência crítica sobre a língua em sociedade!*
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Acerto", +30, "Identificação do recurso de ironia e humor metalinguístico"),
          AxisFeedback("Argumentação", +25, "Crítica à automatização do discurso nas redes"),
          AxisFeedback("Autonomia", +20, "Pensamento crítico sobre modismos sociolinguísticos")
        ),
        nextPhase = QuestRiddlePhase.COMPLETED,
        isCompleted = true,
        equationHighlight = "ENEM 2025 Q.23: Humor e Crítica aos Clichês"
      )
    }

    if (clean.contains("dica") || clean.contains("ajuda") || clean.contains("não sei") || clean.contains("nao sei")) {
      return TutorStepResult(
        responseText = """
          💡 **Dica Socrática:**
          
          Observe a construção do texto: a autora está reclamando das frases clichês que nos 'infernizam'. Mas como ela escreve a própria reclamação? Ela diz: *"se você for proativo... novo normal... sarrafo muito alto... acho cringe"*.
          
          Ela está usando os próprios clichês para fazer o leitor rir e perceber o ridículo da repetição mecânica! Que efeito isso provoca?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Argumentação", +20, "Percepção do efeito de sentido e ironia"),
          AxisFeedback("Pesquisa", +15, "Análise discursiva da crônica")
        ),
        nextPhase = currentPhase,
        equationHighlight = "Pista: A sátira através do próprio jargão"
      )
    }

    return TutorStepResult(
      responseText = """
        🏛️ **[ENEM 2025 • Questão 23 - Linguagens & Comunicação Social]**
        
        Na crônica, a autora reflete sobre frases e expressões da moda que invadiram o cotidiano em 2023 ('zona de conforto', 'ressignificar', 'sarrafo alto', 'cringe'). Ela aponta que 'frases feitas dispensam as pessoas de pensar'.
        
        💡 *Vamos exercitar o pensamento crítico sem atalhos:*
        
        **Qual foi a estratégia estilística inteligente que a autora usou para fazer essa crítica? Ela escreveu um tratado formal ou adotou outro tom?**
      """.trimIndent(),
      feedbackList = listOf(
        AxisFeedback("Autonomia", +15, "Início da questão 23 do ENEM 2025"),
        AxisFeedback("Pesquisa", +10, "Observação de recursos estilísticos")
      ),
      nextPhase = currentPhase,
      equationHighlight = "ENEM 2025 Q.23: Clichês Modernos"
    )
  }

  // 6. INGLÊS - Questão 01 (ENEM 2025): Snowflake Generation & Resiliência
  private fun processEnem01Resilience(clean: String): TutorStepResult {
    if (clean.contains("contrariedades") || clean.contains("avessa a contrariedades") || clean.contains("fragilidade") || clean == "e" || clean.startsWith("letra e") || clean.startsWith("opção e") || clean.startsWith("opcao e")) {
      return TutorStepResult(
        responseText = """
          🏆 **Awesome, Master of English! Desafio do ENEM 2025 superado!**
          
          A expressão *snowflake generation* é usada para **apontar posturas de uma juventude avessa a contrariedades** (Alternativa E)!
          
          O texto argumenta que a vida adulta traz decepções e que a verdadeira resiliência (*resilience*) não é alimentar o ego dizendo que a criança é especial para sempre, mas sim fortalecê-la para se recuperar diante das falhas (*bouncing back from failure*)!
          
          ⚔️ *+330 XP Conquistados! Leitura instrumental e pensamento crítico internacional!*
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Acerto", +30, "Compreensão exata da questão 01 do ENEM 2025"),
          AxisFeedback("Pesquisa", +25, "Domínio de vocabulário e inferência contextual em inglês"),
          AxisFeedback("Argumentação", +20, "Reflexão sobre amadurecimento e resiliência psicológica")
        ),
        nextPhase = QuestRiddlePhase.COMPLETED,
        isCompleted = true,
        equationHighlight = "ENEM 2025 Q.01: Snowflake Generation"
      )
    }

    if (clean.contains("dica") || clean.contains("ajuda") || clean.contains("não sei") || clean.contains("nao sei") || clean.contains("significa")) {
      return TutorStepResult(
        responseText = """
          💡 **Socratic Reading Hint:**
          
          Olhe para este trecho: *"university students, who are so delicate they can't handle controversial ideas being put forward in their lectures"*.
          
          O que a palavra *delicate* e a incapacidade de lidar com *controversial ideas* (ideias polêmicas ou divergentes) sugerem sobre o comportamento dessas pessoas diante de opiniões contrárias?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Pesquisa", +20, "Identificação de pistas contextuais em inglês"),
          AxisFeedback("Autonomia", +15, "Inferência do significado de 'delicate' e 'controversial'")
        ),
        nextPhase = currentPhase,
        equationHighlight = "Hint: 'delicate they can't handle controversial ideas'"
      )
    }

    return TutorStepResult(
      responseText = """
        🏛️ **[ENEM 2025 • Questão 01 - Inglês Instrumental]**
        
        O artigo do The Guardian discute a emergência da chamada *'snowflake generation'* nas universidades e afirma que resiliência não é sobre inflar o ego de crianças, mas fortalecê-las para enfrentar dificuldades e erros.
        
        💡 *Como mentor socrático da luz, convido você a analisar o texto:*
        
        **No contexto do artigo, por que a expressão metafórica 'snowflake' (floco de neve) foi associada a esses estudantes universitários? O que um floco de neve simboliza fisicamente?**
      """.trimIndent(),
      feedbackList = listOf(
        AxisFeedback("Autonomia", +15, "Início da questão de inglês do ENEM 2025"),
        AxisFeedback("Pesquisa", +10, "Análise de metáfora em língua estrangeira")
      ),
      nextPhase = currentPhase,
      equationHighlight = "ENEM 2025 Q.01: The Resilience Dilemma"
    )
  }

  // 7. HISTÓRIA - Questão 47 (ENEM 2025): Reforma Eleitoral de 1881 e Exclusão Social
  private fun processEnem47ReformaEleitoral(clean: String): TutorStepResult {
    if (clean.contains("alfabetização") || clean.contains("alfabetizacao") || clean.contains("analfabeto") || clean == "d" || clean.startsWith("letra d") || clean.startsWith("opção d") || clean.startsWith("opcao d")) {
      return TutorStepResult(
        responseText = """
          🏆 **Parabéns, Guardião da História e da Cidadania! Desafio do ENEM 2025 superado!**
          
          O brutal corte eleitoral decorreu da **exigência da alfabetização** (Alternativa D)!
          
          A Lei Saraiva de 1881 proibiu o voto dos analfabetos. Como no século XIX a educação formal era negada a mais de 80% do povo brasileiro (especialmente escravizados e trabalhadores pobres), o eleitorado caiu de 13% para míseros 0,8%, revelando como os direitos políticos foram historicamente manipulados pelas elites!
          
          ⚔️ *+360 XP Conquistados! Pensamento histórico e consciência cidadã pública de elite!*
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Acerto", +30, "Reconhecimento da Lei Saraiva e exigência de alfabetização"),
          AxisFeedback("Argumentação", +25, "Articulação de cidadania e exclusão social no Brasil"),
          AxisFeedback("Pesquisa", +20, "Domínio da história política do Segundo Reinado")
        ),
        nextPhase = QuestRiddlePhase.COMPLETED,
        isCompleted = true,
        equationHighlight = "ENEM 2025 Q.47: Exigência da Alfabetização"
      )
    }

    if (clean.contains("dica") || clean.contains("ajuda") || clean.contains("não sei") || clean.contains("nao sei")) {
      return TutorStepResult(
        responseText = """
          💡 **Dica Histórica para Pensamento Público:**
          
          Pense no perfil da população brasileira no final do Império (1881). Havia pouquíssimas escolas públicas e a imensa maioria dos trabalhadores não sabia ler nem escrever.
          
          Se uma reforma eleitoral exige que o cidadão comprove saber assinar o próprio nome e ler para poder votar, o que acontece com a grande maioria da população trabalhadora?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Argumentação", +20, "Relação entre escolarização e participação política"),
          AxisFeedback("Autonomia", +15, "Dedução do impacto da barreira letrada")
        ),
        nextPhase = currentPhase,
        equationHighlight = "Pista: A barreira da escrita no século XIX"
      )
    }

    return TutorStepResult(
      responseText = """
        🏛️ **[ENEM 2025 • Questão 47 - História do Brasil & Cidadania]**
        
        Em 1872, o Brasil tinha 1 milhão de votantes (13% da população livre). Em 1886, após a Reforma de 1881 (Lei Saraiva), o eleitorado despencou para cerca de 100 mil votantes (0,8% da população total). Houve um corte de quase 90% dos cidadãos com direito a voto.
        
        💡 *Vamos refletir como cidadãos conscientes:*
        
        **Qual nova exigência legal foi imposta pela Reforma de 1881 que provocou esse corte tão devastador na cidadania política brasileira?**
      """.trimIndent(),
      feedbackList = listOf(
        AxisFeedback("Autonomia", +15, "Início da análise histórica do ENEM 2025"),
        AxisFeedback("Pesquisa", +10, "Reflexão sobre reformas políticas no Império")
      ),
      nextPhase = currentPhase,
      equationHighlight = "ENEM 2025 Q.47: Reforma Eleitoral de 1881"
    )
  }

  // 8. FILOSOFIA - Questão 87 (ENEM 2025): A Cidade Justa de Platão
  private fun processEnem87Platao(clean: String): TutorStepResult {
    if (clean.contains("ética") || clean.contains("etica") || clean.contains("exercício do poder") || clean.contains("exercicio do poder") || clean == "e" || clean.startsWith("letra e") || clean.startsWith("opção e") || clean.startsWith("opcao e")) {
      return TutorStepResult(
        responseText = """
          🏆 **Parabéns, Nobre Filósofo! Desafio do ENEM 2025 superado!**
          
          Platão postula a indissociabilidade entre **ética e o exercício do poder** (Alternativa E)!
          
          Na visão platônica de 'A República', a cidade justa só pode ser conduzida por governantes que praticam a virtude e o conhecimento do Bem, governando para toda a pólis e não em benefício de interesses econômicos particulares!
          
          ⚔️ *+350 XP Conquistados! Filosofia política e ética cívica dominadas!*
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Acerto", +30, "Compreensão da teoria política platônica"),
          AxisFeedback("Argumentação", +25, "Conexão entre ética e poder no espaço público"),
          AxisFeedback("Pesquisa", +20, "Domínio da história da filosofia clássica no ENEM")
        ),
        nextPhase = QuestRiddlePhase.COMPLETED,
        isCompleted = true,
        equationHighlight = "ENEM 2025 Q.87: Ética e Exercício do Poder"
      )
    }

    if (clean.contains("dica") || clean.contains("ajuda") || clean.contains("não sei") || clean.contains("nao sei")) {
      return TutorStepResult(
        responseText = """
          💡 **Dica Socrática:**
          
          Platão afirma que os governantes não devem buscar o lucro ou vantagens privadas, mas a sabedoria e o bem comum.
          
          Portanto, para governar legitimamente uma sociedade, qual virtude moral deve estar obrigatoriamente unida à autoridade política?
        """.trimIndent(),
        feedbackList = listOf(
          AxisFeedback("Argumentação", +20, "Reflexão sobre a moralidade na governança"),
          AxisFeedback("Autonomia", +15, "Estímulo ao pensamento filosófico autônomo")
        ),
        nextPhase = currentPhase,
        equationHighlight = "Pista: Bem comum vs Interesses particulares"
      )
    }

    return TutorStepResult(
      responseText = """
        🏛️ **[ENEM 2025 • Questão 87 - Filosofia Política]**
        
        Na obra 'A República' de Platão, a cidade justa é dirigida racionalmente pelos filósofos para o bem comum de toda a pólis, enquanto a cidade injusta é governada por proprietários que priorizam seus interesses econômicos particulares.
        
        💡 *Como mentor socrático da luz, estimulo seu pensamento público:*
        
        **Segundo a tese platônica, que dois elementos fundamentais da vida humana devem estar inseparavelmente unidos para que a governança seja justa?**
      """.trimIndent(),
      feedbackList = listOf(
        AxisFeedback("Autonomia", +15, "Início da análise filosófica do ENEM 2025"),
        AxisFeedback("Pesquisa", +10, "Reflexão sobre teoria política clássica")
      ),
      nextPhase = currentPhase,
      equationHighlight = "ENEM 2025 Q.87: A Cidade Justa de Platão"
    )
  }
}
