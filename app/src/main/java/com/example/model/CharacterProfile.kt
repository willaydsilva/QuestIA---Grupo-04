package com.example.model

data class CharacterProfile(
  val name: String = "Random",
  val title: String = "Explorador do Desconhecido",
  val level: Int = 1,
  val currentXp: Int = 35,
  val maxXp: Int = 100,
  val hp: Int = 100,
  val maxHp: Int = 100,
  val energy: Int = 80,
  val maxEnergy: Int = 80,
  val gold: Int = 100,
  val brainRotResistance: Int = 88, // Percentual de resistência à névoa da distração
  val wisdomAxes: WisdomAxes = WisdomAxes(),
  val questCompleted: Boolean = false
) {
  val formattedHp: String get() = "$hp/$maxHp"
  val formattedEnergy: String get() = "$energy/$maxEnergy"
  val formattedGold: String get() = "$gold"
  val fullDisplayName: String get() = "$name - $title"
}
