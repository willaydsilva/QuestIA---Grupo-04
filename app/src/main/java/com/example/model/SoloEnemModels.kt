package com.example.model

data class SoloEnemOption(
  val letter: String, // "A", "B", "C", "D", "E"
  val text: String
)

data class SoloEnemChallenge(
  val subject: QuestSubject,
  val enemYear: String = "2025",
  val questionNumber: String,
  val title: String,
  val loreIntro: String,
  val contextText: String,
  val questionStatement: String,
  val options: List<SoloEnemOption>,
  val correctOption: String,
  val explanation: String,
  val xpReward: Int = 500,
  val goldReward: Int = 100
)

object SoloEnemData {

  val CHALLENGES: Map<QuestSubject, SoloEnemChallenge> = mapOf(
    // 1. MATEMÁTICA - Questão 174 (ENEM 2025 - Caderno 6 Cinza)
    QuestSubject.MATEMATICA to SoloEnemChallenge(
      subject = QuestSubject.MATEMATICA,
      enemYear = "2025",
      questionNumber = "174",
      title = "A Prova de Fogo da Velocidade & Prorrogação",
      loreIntro = "Chegamos ao ápice da Trilha de Matemática! A névoa do santuário silencia a voz do Copiloto: aqui não há dicas de IA nem atalhos. Confie na sua mente matemática e mostre que você domina as grandezas sozinho!",
      contextText = "A final de um campeonato de futebol foi disputada em 2 tempos regulamentares, de 45 minutos cada, sem acréscimos, com uma prorrogação de 30 minutos, também sem acréscimos. Um jogador entrou no início do segundo tempo, com um equipamento para medir a distância percorrida durante sua participação no jogo. Ao final do segundo tempo regulamentar, esse jogador havia percorrido 4,5 km. Ele manteve na prorrogação a mesma velocidade média que havia mantido no segundo tempo regulamentar.",
      questionStatement = "A distância percorrida por esse jogador durante toda a sua participação na partida, em quilômetro, foi:",
      options = listOf(
        SoloEnemOption("A", "4,5"),
        SoloEnemOption("B", "6,0"),
        SoloEnemOption("C", "7,5"),
        SoloEnemOption("D", "9,0"),
        SoloEnemOption("E", "12,0")
      ),
      correctOption = "C",
      explanation = "No 2º tempo (45 minutos), o atleta percorreu 4,5 km. Sua velocidade média foi de 4,5 km / 45 min = 0,1 km/min (equivalente a 6 km/h). Na prorrogação de 30 minutos, mantendo o mesmo ritmo, ele percorreu 0,1 km/min × 30 min = 3,0 km. Portanto, a distância total percorrida na partida foi 4,5 km + 3,0 km = 7,5 km (Alternativa C)!"
    ),

    // 2. PORTUGUÊS - Questão 27 (ENEM 2025 - Caderno 3 Branco)
    QuestSubject.PORTUGUES to SoloEnemChallenge(
      subject = QuestSubject.PORTUGUES,
      enemYear = "2025",
      questionNumber = "27",
      title = "O Rito da Linguagem: Briga vs Luta",
      loreIntro = "Você dominou 100% da Trilha de Português! Agora é hora de provar sua autonomia interpretativa sem a assistência de IA. Leia com atenção crítica e decodifique o sentido profundo do texto!",
      contextText = "“A diferença entre briga e luta é a existência de juízes e medalhas? A briga desumaniza o outro e pode até matá-lo. Já na luta, as intenções do outro são consideradas sua proposta combativa e suas habilidades, enfim, sua meta de vencer. Na luta, o desenvolvimento passa pelo contato com a agressividade, a raiva, a frustração, o orgulho, a determinação e a fraqueza. Daí também a luta não ser apenas com o outro, mas consigo mesmo, num combate contra as próprias limitações, sobretudo, contra o próprio orgulho.” (C. Barreira)",
      questionStatement = "Esse texto apresenta as diferenças entre briga e luta, na medida em que aponta o(a):",
      options = listOf(
        SoloEnemOption("A", "superação pessoal na luta."),
        SoloEnemOption("B", "violência evidenciada na luta."),
        SoloEnemOption("C", "predomínio de regras na briga."),
        SoloEnemOption("D", "desafio externo presente na luta."),
        SoloEnemOption("E", "habilidade desenvolvida na briga.")
      ),
      correctOption = "A",
      explanation = "O autor defende que a luta é essencialmente um combate contra as próprias limitações, frustrações e fraquezas interiores, o que caracteriza a superação pessoal (Alternativa A)!"
    ),

    // 3. HISTÓRIA / HUMANAS - Questão 50 (ENEM 2025 - Caderno 3 Branco)
    QuestSubject.HISTORIA to SoloEnemChallenge(
      subject = QuestSubject.HISTORIA,
      enemYear = "2025",
      questionNumber = "50",
      title = "A Câmara da Pólis: As Três Formas de Governo",
      loreIntro = "100% de Maestria na Trilha de História & Cidadania! O Copiloto confia na sua capacidade de análise filosófica. Este é o seu momento de demonstrar raciocínio autônomo sobre os pilares da política clássica.",
      contextText = "“O corpo de cidadãos é o poder supremo dos Estados. A supremacia pode residir ou num homem, ou na minoria, ou em todos. Sempre que o Um, ou a Minoria, ou Todos governam, tendo em vista o bem-estar comum, essas constituições são justas; mas se procuram apenas o benefício de uma das partes, seja ela o Um, a Minoria ou Todos, estabelece-se um desvio.” (ARISTÓTELES. Política)",
      questionStatement = "No excerto encontra-se a base da teoria clássica das três formas legítimas de governo representadas pela:",
      options = listOf(
        SoloEnemOption("A", "tirania, oligarquia e república."),
        SoloEnemOption("B", "burocracia, autarquia e império."),
        SoloEnemOption("C", "ditadura, autocracia e anarquia."),
        SoloEnemOption("D", "plutocracia, tecnocracia e demagogia."),
        SoloEnemOption("E", "monarquia, aristocracia e democracia.")
      ),
      correctOption = "E",
      explanation = "Aristóteles classifica as três formas puras/legítimas de governo visando o bem comum de acordo com o número de governantes: Um governa (Monarquia), Poucos governam (Aristocracia) e Todos/Muitos governam (Democracia ou República) (Alternativa E)!"
    ),

    // 4. INGLÊS - Questão 04 (ENEM 2025 - Caderno 3 Branco)
    QuestSubject.INGLES to SoloEnemChallenge(
      subject = QuestSubject.INGLES,
      enemYear = "2025",
      questionNumber = "04",
      title = "The Sanctum of Thought: Angela Davis on Philosophy",
      loreIntro = "100% na Trilha de Inglês Internacional! O Copiloto da Luz guarda silêncio para que seu próprio cérebro traduza, compreenda e decida. Confie na sua leitura e responda sem IA!",
      contextText = "“My idea of philosophy is that if it is not relevant to human problems, if it does not tell us how we can go about eradicating some of the misery in this world, then it is not worth the name of philosophy. I think Socrates made a very profound statement when he asserted that philosophy is to teach us proper living. In this day and age ‘proper living’ means liberation from the urgent problems of poverty, economic necessity and indoctrination, mental oppression.” (DAVIS, A. Lectures on Liberation, 1971)",
      questionStatement = "Nesse texto, ao discorrer sobre a relevância da filosofia, a escritora Angela Davis tem por objetivo:",
      options = listOf(
        SoloEnemOption("A", "criticá-la pela restrição temática."),
        SoloEnemOption("B", "vinculá-la ao universo acadêmico."),
        SoloEnemOption("C", "afastá-la da abordagem socrática."),
        SoloEnemOption("D", "aproximá-la dos problemas sociais."),
        SoloEnemOption("E", "responsabilizá-la pela pobreza humana.")
      ),
      correctOption = "D",
      explanation = "Angela Davis defende que a filosofia deve se dedicar a erradicar a miséria e libertar os oprimidos, aproximando a disciplina dos dilemas sociais urgentes (Alternativa D)!"
    )
  )

  fun getChallengeForSubject(subject: QuestSubject): SoloEnemChallenge {
    return CHALLENGES[subject] ?: CHALLENGES[QuestSubject.MATEMATICA]!!
  }
}
