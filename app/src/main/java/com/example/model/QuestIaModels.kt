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
  val challengePrompt: String = ""
)

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
    Trail(QuestSubject.MATEMATICA, "Matemática & Lógica Algébrica", 0, "📐", 1),
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
    Quest(
      id = "quest_matematica",
      subject = QuestSubject.MATEMATICA,
      title = "O Enigma das Equações Sagradas",
      description = "Identifique os coeficientes e calcule o discriminante da equação quadrática para quebrar os grilhões da desatenção e desbloquear a lógica pura.",
      xp = 350,
      goldReward = 60,
      challengePrompt = "Olá Copiloto da Luz! Desejo resolver o enigma matemático da equação de 2º grau x² + 4x - 11 = 0 pelo método socrático."
    ),
    Quest(
      id = "quest_portugues",
      subject = QuestSubject.PORTUGUES,
      title = "A Fortaleza das Figuras de Linguagem",
      description = "Diferencie metáfora, metonímia, hipérbole e antítese para decodificar textos clássicos e articular redações com poder persuasivo nota 1000.",
      xp = 300,
      goldReward = 50,
      challengePrompt = "Olá Copiloto! Quero treinar figuras de linguagem e argumentação para a redação do Enem."
    ),
    Quest(
      id = "quest_ingles",
      subject = QuestSubject.INGLES,
      title = "The Global Cyber-Scrolls",
      description = "Interprete termos de tecnologia em inglês instrumental, falsos amigos (falsos cognatos) e conectivos para dominar a literatura científica global.",
      xp = 300,
      goldReward = 50,
      challengePrompt = "Hello Copilot! Let's practice English reading comprehension, tech vocabulary and false friends for high school!"
    ),
    Quest(
      id = "quest_historia",
      subject = QuestSubject.HISTORIA,
      title = "As Crônicas das Revoluções & Cidadania",
      description = "Analise as causas e os impactos da Revolução Industrial, a luta pelos direitos fundamentais e como a história molda o nosso presente.",
      xp = 320,
      goldReward = 55,
      challengePrompt = "Olá Copiloto! Vamos analisar os marcos históricos da Revolução Industrial e a formação dos direitos humanos e da cidadania."
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
