package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.R

enum class QuestSubject(
  val displayName: String,
  val icon: String,
  val colorHex: String,
  val tagBgHex: String
) {
  MATEMATICA("Matemática", "📐", "#00E5FF", "#00363A"),
  PORTUGUES("Português", "📜", "#FFA502", "#3D2600"),
  INGLES("Inglês", "🌍", "#39FF14", "#0A3311"),
  HISTORIA("História", "🏛️", "#FF073A", "#3B020B")
}

data class Trail(
  val subject: QuestSubject,
  val name: String,
  val progress: Int, // 0 to 100
  val icon: String,
  val level: Int = 1
)

data class Quest(
  val id: String,
  val subject: QuestSubject,
  val title: String,
  val description: String,
  val xp: Int,
  val goldReward: Int = 50,
  val isCompleted: Boolean = false,
  val challengePrompt: String = "",
  val enemYear: String? = null,
  val enemQuestionNumber: String? = null
) {
  val enemLabel: String?
    get() = if (enemYear != null) {
      if (enemQuestionNumber != null) "ENEM $enemYear • Q.$enemQuestionNumber" else "ENEM $enemYear"
    } else null
}

data class Guilda(
  val id: String,
  val name: String,
  val schoolName: String,
  val code: String,
  val emblem: String,
  val motto: String,
  val city: String,
  val totalXp: Int,
  val memberCount: Int,
  val rank: Int,
  val accentHex: String
) {
  val accentColor: Color
    get() = try {
      Color(android.graphics.Color.parseColor(accentHex))
    } catch (_: Exception) {
      Color(0xFF00E5FF)
    }
}

enum class AvatarItemType {
  MODEL_3D
}

data class Avatar3dModel(
  val id: String,
  val name: String,
  val archetype: String,
  val description: String,
  val imageRes: Int,
  val accentHex: String
)

data class AvatarItem(
  val id: String,
  val type: AvatarItemType,
  val name: String,
  val icon: String,
  val previewHex: String,
  val description: String
)

data class UserAvatarCustomization(
  val model3dId: String = "hero_arcane",
  val hatId: String = "",
  val outfitId: String = "",
  val weaponId: String = "",
  val auraId: String = "",
  val baseFaceId: String = "",
  val title: String = "Erudito Arcano"
)

data class Founder(
  val id: String,
  val name: String,
  val profession: String,
  val role: String,
  val quote: String,
  val accentHex: String,
  val dollEmoji: String,
  val dollHat: String,
  val dollOutfit: String,
  val dollWeapon: String,
  val dollAura: String,
  val personalityDesc: String,
  val relic: String,
  val specialSkills: List<String>,
  val imageRes: Int
) {
  val accentColor: Color
    get() = try {
      Color(android.graphics.Color.parseColor(accentHex))
    } catch (_: Exception) {
      Color(0xFF00E5FF)
    }
}

data class Hero(
  val id: String,
  val name: String,
  val role: String,
  val profession: String,
  val level: Int,
  val xp: Int,
  val gold: Int,
  val hp: Int,
  val mp: Int,
  val accentHex: String,
  val relic: String,
  val skills: List<String>,
  val trails: List<Trail> = emptyList(),
  val avatar: String = "🧙‍♂️"
) {
  val accentColor: Color
    get() = try {
      Color(android.graphics.Color.parseColor(accentHex))
    } catch (_: Exception) {
      Color(0xFF00E5FF)
    }
}

object QuestIaData {

  val DEFAULT_TRAILS: List<Trail> = listOf(
    Trail(QuestSubject.MATEMATICA, "Matemática & Lógica Algébrica", 75, "📐", 1),
    Trail(QuestSubject.PORTUGUES, "Português, Gramática & Retórica", 0, "📜", 1),
    Trail(QuestSubject.INGLES, "Inglês Instrumental & Tech", 0, "🌍", 1),
    Trail(QuestSubject.HISTORIA, "História Geral & Cidadania", 0, "🏛️", 1)
  )

  val USER_HERO_DEFAULT = Hero(
    id = "user_hero",
    name = "Aprendiz",
    role = "Herói do Conhecimento",
    profession = "Estudante do Ensino Médio",
    level = 1,
    xp = 120,
    gold = 150,
    hp = 100,
    mp = 80,
    accentHex = "#00e5ff",
    relic = "Diário do Guerreiro Estudante",
    skills = listOf("Foco Racional", "Curiosidade Crítica", "Resistência ao Brain Rot"),
    trails = DEFAULT_TRAILS,
    avatar = "🧙‍♂️"
  )

  // As 4 Quests requeridas pelo usuário (uma de cada matéria)
  val QUESTS: List<Quest> = listOf(
    // 1. MATEMÁTICA - ENEM 2025 (Questão 153: Jogo Digital, Herói e Vilões no Plano Cartesiano)
    Quest(
      id = "quest_enem_heroi_cartesiano",
      subject = QuestSubject.MATEMATICA,
      title = "O Escudo Cartesiano dos Heróis",
      description = "Em um jogo digital, um herói e dois vilões movem-se no plano cartesiano. O herói só escapa dos ataques se caminhar por uma rota equidistante dos vilões (ponto V(8, 6) e S(6, 2)). Encontre a equação da trajetória da reta de sobrevivência!",
      xp = 420,
      goldReward = 75,
      challengePrompt = "Olá Copiloto da Luz! Desejo encarar a questão 153 do ENEM 2025 sobre a rota equidistante entre herói e vilões no plano cartesiano.",
      enemYear = "2025",
      enemQuestionNumber = "153"
    ),

    // 2. MATEMÁTICA - ENEM 2025 (Questão 148: Fábrica de Tijolos Ecológicos)
    Quest(
      id = "quest_enem_tijolos_ecologicos",
      subject = QuestSubject.MATEMATICA,
      title = "A Fábrica de Tijolos Ecológicos",
      description = "3 artesãos trabalhando 6h/dia produzem 720 tijolos ecológicos diários. A fábrica expande para 5 artesãos trabalhando 9h/dia mantendo a mesma taxa por hora. Qual será a nova produção diária?",
      xp = 380,
      goldReward = 65,
      challengePrompt = "Olá Copiloto da Luz! Vamos analisar a questão 148 do ENEM 2025 sobre proporcionalidade e produção de tijolos ecológicos.",
      enemYear = "2025",
      enemQuestionNumber = "148"
    ),

    // 3. MATEMÁTICA - ENEM 2025 (Questão 141: Autonomia e Gás Veicular GNV)
    Quest(
      id = "quest_enem_gnv",
      subject = QuestSubject.MATEMATICA,
      title = "A Rota Econômica do GNV",
      description = "Um veículo roda 30 km diários durante os 7 dias da semana e consome 1 m³ de GNV a cada 13 km. Entre os cilindros de 10, 14, 17, 21 e 25 m³, qual é a menor capacidade que garante 1 abastecimento semanal?",
      xp = 360,
      goldReward = 60,
      challengePrompt = "Olá Copiloto da Luz! Desafio oficial ENEM 2025 (Questão 141): qual o menor cilindro de GNV para suprir a autonomia semanal?",
      enemYear = "2025",
      enemQuestionNumber = "141"
    ),

    // 4. PORTUGUÊS - ENEM 2025 (Questão 44: Intertextualidade - Hércules e o Trabalhador)
    Quest(
      id = "quest_enem_hercules",
      subject = QuestSubject.PORTUGUES,
      title = "Os Doze Trabalhos do Cidadão",
      description = "Compare o mito grego dos 12 Trabalhos de Hércules com a narrativa do operário brasileiro João Antonio da Silva, que assume múltiplos ofícios. Como a intertextualidade ressignifica a figura heroica?",
      xp = 340,
      goldReward = 55,
      challengePrompt = "Olá Copiloto! Quero analisar a questão 44 do ENEM 2025 sobre intertextualidade entre mitologia e o cotidiano do trabalhador.",
      enemYear = "2025",
      enemQuestionNumber = "44"
    ),

    // 5. PORTUGUÊS - ENEM 2025 (Questão 23: Jargões e Clichês Linguísticos)
    Quest(
      id = "quest_enem_jargoes",
      subject = QuestSubject.PORTUGUES,
      title = "A Crônica dos Clichês Modernos",
      description = "Examine a reflexão irônica da autora sobre jargões corporativos e redes sociais ('sair da caixa', 'ressignificar', 'cringe', 'sarrafo alto') e como frases feitas automatizam a fala humana.",
      xp = 320,
      goldReward = 50,
      challengePrompt = "Olá Copiloto! Vamos refletir sobre a questão 23 do ENEM 2025 sobre expressões automatizadas e a crítica linguística.",
      enemYear = "2025",
      enemQuestionNumber = "23"
    ),

    // 6. INGLÊS - ENEM 2025 (Questão 01: Snowflake Generation & Resiliência)
    Quest(
      id = "quest_enem_resilience",
      subject = QuestSubject.INGLES,
      title = "The Resilience Dilemma",
      description = "Interprete o texto do The Guardian sobre a 'snowflake generation' nas universidades e por que a verdadeira resiliência emocional exige aprender a lidar com frustrações e críticas.",
      xp = 330,
      goldReward = 55,
      challengePrompt = "Hello Copilot! Let's examine ENEM 2025 Question 01 on English reading comprehension and resilience vs fragility.",
      enemYear = "2025",
      enemQuestionNumber = "01"
    ),

    // 7. HISTÓRIA - ENEM 2025 (Questão 47: A Reforma Eleitoral de 1881 e Exclusão Social)
    Quest(
      id = "quest_enem_reforma_eleitoral",
      subject = QuestSubject.HISTORIA,
      title = "O Voto e a Reforma Eleitoral de 1881",
      description = "No Brasil Império, a Lei Saraiva (1881) cortou quase 90% do eleitorado nacional, reduzindo os votantes de 13% para 0,8%. Qual exigência provocou esse brutal retrocesso na participação política?",
      xp = 360,
      goldReward = 60,
      challengePrompt = "Olá Copiloto da Luz! Vamos desvendar a questão 47 do ENEM 2025 sobre cidadania, voto e exclusão eleitoral no Império.",
      enemYear = "2025",
      enemQuestionNumber = "47"
    ),

    // 8. HISTÓRIA / FILOSOFIA - ENEM 2025 (Questão 87: A Pólis Justa de Platão)
    Quest(
      id = "quest_enem_platao_polis",
      subject = QuestSubject.HISTORIA,
      title = "A Pólis Justa e a Ética do Poder",
      description = "Na filosofia política clássica de Platão, por que o governo dos filósofos e sábios garante o bem comum enquanto o governo de interesses econômicos particulares degenera a cidade?",
      xp = 350,
      goldReward = 55,
      challengePrompt = "Olá Copiloto! Quero debater a questão 87 do ENEM 2025 sobre a cidade justa de Platão e a relação entre ética e poder.",
      enemYear = "2025",
      enemQuestionNumber = "87"
    ),

    // 9. CLÁSSICA - Álgebra Quadrática
    Quest(
      id = "quest_matematica",
      subject = QuestSubject.MATEMATICA,
      title = "O Enigma das Equações Sagradas",
      description = "Identifique os coeficientes e calcule o discriminante da equação quadrática x² + 4x - 11 = 0 para quebrar os grilhões da desatenção e desbloquear a lógica pura.",
      xp = 350,
      goldReward = 60,
      challengePrompt = "Olá Copiloto da Luz! Desejo resolver o enigma matemático da equação de 2º grau x² + 4x - 11 = 0 pelo método socrático."
    )
  )

  // Guildas das Escolas (Ranking inicial com escolas imaginárias)
  val DEFAULT_GUILDAS: List<Guilda> = listOf(
    Guilda(
      id = "g_solar",
      name = "Academia Solar de Nova Alexandria",
      schoolName = "Colégio Estadual Modelo Alexandria",
      code = "SOLAR-01",
      emblem = "☀️",
      motto = "O saber ilumina as trevas do esquecimento e do desfoque.",
      city = "Brasília - DF",
      totalXp = 48500,
      memberCount = 142,
      rank = 1,
      accentHex = "#FFD700"
    ),
    Guilda(
      id = "g_titas",
      name = "Ordem dos Titãs do Saber",
      schoolName = "Instituto Federal Politécnico do Futuro",
      code = "TITAN-02",
      emblem = "🛡️",
      motto = "Ciência, técnica e honra inabalável.",
      city = "São Paulo - SP",
      totalXp = 42100,
      memberCount = 118,
      rank = 2,
      accentHex = "#00E5FF"
    ),
    Guilda(
      id = "g_phoenix",
      name = "Grêmio Phoenix da Sabedoria",
      schoolName = "Escola Municipal São Bento dos Paladinos",
      code = "PHNX-03",
      emblem = "🦅",
      motto = "Renascendo a cada desafio com foco e bravura.",
      city = "Belo Horizonte - MG",
      totalXp = 37800,
      memberCount = 95,
      rank = 3,
      accentHex = "#FF073A"
    ),
    Guilda(
      id = "g_cyber",
      name = "Liceu Cyber-Vanguarda",
      schoolName = "Colégio Técnico de Inovação Digital",
      code = "CYBER-04",
      emblem = "⚡",
      motto = "Algoritmos para o bem comum e a educação aberta.",
      city = "Recife - PE",
      totalXp = 31400,
      memberCount = 84,
      rank = 4,
      accentHex = "#BC13FE"
    ),
    Guilda(
      id = "g_esmeralda",
      name = "Guardiões da Floresta Esmeralda",
      schoolName = "Escola Agrotécnica Integrada Parnaso",
      code = "ESMR-05",
      emblem = "🌿",
      motto = "Sabedoria viva das raízes ao cosmos.",
      city = "Curitiba - PR",
      totalXp = 26900,
      memberCount = 72,
      rank = 5,
      accentHex = "#39FF14"
    )
  )

  // Personagens de RPG Exclusivos para o Jogador Escolher (Distintos dos Criadores)
  val AVATAR_3D_MODELS: List<Avatar3dModel> = listOf(
    Avatar3dModel(
      id = "hero_arcane",
      name = "Erudito Arcano",
      archetype = "Mago Guardião do Foco",
      description = "Armadura cyber-mística com runas azuis de foco mental e concentração profunda.",
      imageRes = R.drawable.img_hero_3d,
      accentHex = "#00E5FF"
    ),
    Avatar3dModel(
      id = "hero_paladin",
      name = "Paladino Solar",
      archetype = "Guerreiro da Disciplina",
      description = "Placas de aço áureo forjadas pela persistência inabalável nos estudos.",
      imageRes = R.drawable.hero_avatar,
      accentHex = "#FFD700"
    ),
    Avatar3dModel(
      id = "hero_mage",
      name = "Feiticeiro Rúnico",
      archetype = "Místico do Saber",
      description = "Canaliza circuitos de energia violeta e fórmulas avançadas contra o brain rot.",
      imageRes = R.drawable.img_char_mage,
      accentHex = "#BC13FE"
    ),
    Avatar3dModel(
      id = "hero_ranger",
      name = "Patrulheiro Sombrio",
      archetype = "Rastreador Crítico",
      description = "Agilidade tática de capuz com visor esmeralda para decifrar pegadinhas de provas.",
      imageRes = R.drawable.img_char_ranger,
      accentHex = "#39FF14"
    ),
    Avatar3dModel(
      id = "hero_valkyrie",
      name = "Valquíria Cibernética",
      archetype = "Heroína da Sabedoria",
      description = "Armadura platina futurista e coroa de dados pronta para conquistar o ENEM e vestibulares.",
      imageRes = R.drawable.img_char_valkyrie,
      accentHex = "#FFA502"
    )
  )

  // Listas vazias legadas para compatibilidade
  val AVATAR_HATS: List<AvatarItem> = emptyList()
  val AVATAR_OUTFITS: List<AvatarItem> = emptyList()
  val AVATAR_WEAPONS: List<AvatarItem> = emptyList()
  val AVATAR_AURAS: List<AvatarItem> = emptyList()

  // Mural dos Fundadores (Criadores) com Personagens de RPG
  val FOUNDERS: List<Founder> = listOf(
    Founder(
      id = "carlos",
      name = "Carlos",
      profession = "Nutricionista",
      role = "Druida da Vitalidade",
      quote = "“A mente brilha quando o corpo e a nutrição estão em pleno equilíbrio.”",
      accentHex = "#39FF14",
      dollEmoji = "🌿",
      dollHat = "Coroa de Louros Botânica 🍃",
      dollOutfit = "Jaleco de Alquimista Nutricional 🥼",
      dollWeapon = "Cálice de Elixir da Vitalidade 🧪",
      dollAura = "Resplendor Esmeralda da Vida ✨",
      personalityDesc = "Especialista em metabolismo, foco cerebral e saúde do estudante. Guia os guerreiros a manterem energia máxima sem depender de energéticos artificiais.",
      relic = "Cálice Botânico Ancestral",
      specialSkills = listOf("Bioquímica Celular", "Metabolismo Ativo", "Equilíbrio Vital", "Fitoterapia"),
      imageRes = R.drawable.img_founder_carlos
    ),
    Founder(
      id = "flavia",
      name = "Flávia",
      profession = "Vendas & Negócios",
      role = "Estrategista de Negócios",
      quote = "“Saber comunicar seu valor é a arte suprema de abrir qualquer porta no mundo.”",
      accentHex = "#FF073A",
      dollEmoji = "💎",
      dollHat = "Diadema Carmesim da Liderança 👑",
      dollOutfit = "Traje Executivo Imperial 👠",
      dollWeapon = "Contrato de Ouro Rubro 📜",
      dollAura = "Brilho Rubro de Autoridade 🔴",
      personalityDesc = "Mestra da persuasão ética, oratória assertiva e fechamento de acordos. Ensina como transformar ideias brilhantes em projetos concretos e inspiradores.",
      relic = "Contrato de Ouro Rubro",
      specialSkills = listOf("Persuasão Crítica", "Comunicação Não-Violenta", "Pitch de Impacto", "Visão Estratégica"),
      imageRes = R.drawable.img_founder_flavia
    ),
    Founder(
      id = "kaio",
      name = "Kaio",
      profession = "Dentista",
      role = "Guardião da Saúde Bucal",
      quote = "“Um sorriso confiante e a saúde integrada são o primeiro escudo contra as dores do mundo.”",
      accentHex = "#00E5FF",
      dollEmoji = "🦷",
      dollHat = "Visor Cirúrgico Espectral 🥽",
      dollOutfit = "Manto Celestial Cirúrgico 🥋",
      dollWeapon = "Espelho Solar de Precisão 🪞",
      dollAura = "Pulso Ciano de Cura & Assepsia 💠",
      personalityDesc = "Cirurgião da precisão milimétrica, anatomia craniofacial e empatia no atendimento. Lembra aos guerreiros que cuidar de si é indispensável para vencer.",
      relic = "Espelho Odontológico Solar",
      specialSkills = listOf("Precisão Anatômica", "Biomedicina Preventiva", "Assepsia Arcana", "Empatia Tática"),
      imageRes = R.drawable.img_founder_kaio
    ),
    Founder(
      id = "victor",
      name = "Victor",
      profession = "Advogado",
      role = "Paladino da Justiça & Lei",
      quote = "“Onde reinam as leis e o debate justo, a tirania da desinformação não tem vez.”",
      accentHex = "#FFD700",
      dollEmoji = "⚖️",
      dollHat = "Elmo Cerimonial do Tribuno 🏛️",
      dollOutfit = "Toga Dourada da Magistratura 👘",
      dollWeapon = "Balança da Justiça Eterna ⚖️",
      dollAura = "Cúpula Áurea de Proteção Moral 🟡",
      personalityDesc = "Defensor dos direitos fundamentais, lógica jurídica impecável e retórica inquebrável. Inspira os alunos a argumentarem com embasamento e serenidade.",
      relic = "Balança da Justiça Eterna",
      specialSkills = listOf("Argumentação Jurídica", "Retórica Forense", "Mediação de Conflitos", "Ética Pública"),
      imageRes = R.drawable.img_founder_victor
    ),
    Founder(
      id = "wagner",
      name = "Wagner",
      profession = "Robótica & Mecatrônica",
      role = "Artífice da Inovação",
      quote = "“A melhor forma de prever o futuro é construí-lo com código, engrenagens e inteligência.”",
      accentHex = "#BC13FE",
      dollEmoji = "🤖",
      dollHat = "Óculos de Solda Quântica 👓",
      dollOutfit = "Exoesqueleto de Fibra de Carbono 🦾",
      dollWeapon = "Chave Mecatrônica de Freqüência 🔧",
      dollAura = "Centelhas Elétricas Violeta ⚡",
      personalityDesc = "Gênio da automação, cinemática, hardware livre e inteligência artificial embarcada. Mostra como o raciocínio matemático se materializa em robôs.",
      relic = "Núcleo Mecatrônico Quântico",
      specialSkills = listOf("Cinemática Robótica", "Programação Embarcada", "Modelagem & Prototipagem", "IoT e Sensores"),
      imageRes = R.drawable.img_founder_wagner
    ),
    Founder(
      id = "willaydson",
      name = "Willaydson",
      profession = "Professor & Cientista de Dados",
      role = "Grão-Mestre da Pedagogia",
      quote = "“Educação libertadora e ciência são os maiores feitiços que um ser humano pode conjurar.”",
      accentHex = "#FF00FF",
      dollEmoji = "🧙‍♂️",
      dollHat = "Capuz Estelar da Sabedoria 🌌",
      dollOutfit = "Manto Pedagógico das Constelações 👘",
      dollWeapon = "Grimório da Pedagogia Ativa 📖",
      dollAura = "Nebulosa Cósmica de Ideias 🔮",
      personalityDesc = "Arquiteto do Reino de QuestIA, mestre em metodologias ativas, gamificação pedagógica e inteligência artificial educativa. Mentor do Copiloto da Luz.",
      relic = "Grimório da Sabedoria Pedagógica",
      specialSkills = listOf("Metodologias Ativas", "Ciência de Dados Educacional", "Engenharia de Prompt", "Mentoria Adaptativa"),
      imageRes = R.drawable.img_founder_willaydson
    )
  )

  val HEROES: Map<String, Hero> = mapOf(
    "user_hero" to USER_HERO_DEFAULT,
    "carlos" to Hero("carlos", "Carlos", "Druida", "Nutricionista", 14, 8900, 1450, 850, 620, "#39ff14", "Cálice Botânico", listOf("Bioquímica"), DEFAULT_TRAILS, "🌿"),
    "flavia" to Hero("flavia", "Flávia", "Negociadora", "Vendas", 15, 9800, 3200, 790, 740, "#ff073a", "Contrato Ouro", listOf("Persuasão"), DEFAULT_TRAILS, "💎"),
    "kaio" to Hero("kaio", "Kaio", "Clérigo", "Dentista", 13, 7600, 1800, 920, 810, "#00e5ff", "Espelho Solar", listOf("Anatomia"), DEFAULT_TRAILS, "🦷"),
    "victor" to Hero("victor", "Victor", "Paladino", "Advogado", 14, 8400, 2100, 980, 590, "#ffd700", "Balança Eterna", listOf("Oratória"), DEFAULT_TRAILS, "⚖️"),
    "wagner" to Hero("wagner", "Wagner", "Artífice", "Robótica", 15, 9500, 2400, 820, 900, "#bc13fe", "Núcleo Quântico", listOf("Robótica"), DEFAULT_TRAILS, "🤖"),
    "willaydson" to Hero("willaydson", "Willaydson", "Estrategista", "Professor", 15, 9990, 2900, 880, 980, "#ff00ff", "Grimório Sagrado", listOf("Pedagogia"), DEFAULT_TRAILS, "🧙‍♂️")
  )
}
