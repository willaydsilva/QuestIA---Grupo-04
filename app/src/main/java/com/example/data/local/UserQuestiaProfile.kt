package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profile")
data class UserQuestiaProfile(
  @PrimaryKey val id: Int = 1,
  val userName: String = "",
  val isRegistered: Boolean = false,
  val heroLevel: Int = 1,
  val heroXp: Int = 120,
  val heroGold: Int = 150,
  val heroHp: Int = 100,
  val heroMp: Int = 80,
  val selectedModelId: String = "hero_arcane",
  val selectedModelTitle: String = "Erudito Arcano",
  val userGuildaId: String? = "g_solar",
  val trailMatematicaProgress: Int = 0,
  val trailMatematicaLevel: Int = 1,
  val trailPortuguesProgress: Int = 0,
  val trailPortuguesLevel: Int = 1,
  val trailInglesProgress: Int = 0,
  val trailInglesLevel: Int = 1,
  val trailHistoriaProgress: Int = 0,
  val trailHistoriaLevel: Int = 1,
  val completedQuestsCsv: String = ""
)
