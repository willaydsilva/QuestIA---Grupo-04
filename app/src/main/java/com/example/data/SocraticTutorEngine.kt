package com.example.data

import com.example.model.AxisFeedback

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

  fun processUserInput(input: String): TutorStepResult {
    val clean = input.trim().lowercase()
    val normalized = clean.replace(" ", "")

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
}
