package com.example.model

data class WisdomAxes(
  val acerto: Int = 20,          // Precisão e exatidão dos cálculos
  val argumentacao: Int = 15,    // Clareza ao explicar o raciocínio
  val autonomia: Int = 25,       // Independência na dedução
  val pesquisa: Int = 10,        // Curiosidade e busca de princípios
  val correcao: Int = 10         // Capacidade de identificar e consertar erros
) {
  val totalScore: Int
    get() = (acerto + argumentacao + autonomia + pesquisa + correcao) / 5

  val rankTitle: String
    get() = when {
      totalScore >= 80 -> "Mestre da Luz Racional"
      totalScore >= 60 -> "Guardião da Lógica"
      totalScore >= 40 -> "Iniciado Desperto"
      totalScore >= 20 -> "Aprendiz Focado"
      else -> "Recruta Anti-Brain Rot"
    }
}

data class AxisFeedback(
  val axisName: String,
  val deltaPoints: Int,
  val reason: String
)
