package com.example.data.local

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class QuestiaPersistenceRepository(
  private val context: Context?
) {
  private val db = context?.let { AppDatabase.getInstance(it) }
  private val dao = db?.userProfileDao()
  private val prefs: SharedPreferences? = context?.getSharedPreferences("questia_prefs", Context.MODE_PRIVATE)

  suspend fun loadProfile(): UserQuestiaProfile? = withContext(Dispatchers.IO) {
    try {
      // 1. Try reading from Room database first
      val roomProfile = dao?.getUserProfileSync()
      if (roomProfile != null) {
        return@withContext roomProfile
      }

      // 2. Fallback to SharedPreferences if Room hasn't been populated yet
      if (prefs != null && (prefs.getBoolean("is_registered", false) || !prefs.getString("user_name", "").isNullOrBlank())) {
        val spProfile = UserQuestiaProfile(
          id = 1,
          userName = prefs.getString("user_name", "") ?: "",
          isRegistered = prefs.getBoolean("is_registered", true),
          heroLevel = prefs.getInt("hero_level", 1),
          heroXp = prefs.getInt("hero_xp", 120),
          heroGold = prefs.getInt("hero_gold", 150),
          heroHp = prefs.getInt("hero_hp", 100),
          heroMp = prefs.getInt("hero_mp", 80),
          selectedModelId = prefs.getString("selected_model_id", "hero_arcane") ?: "hero_arcane",
          selectedModelTitle = prefs.getString("selected_model_title", "Erudito Arcano") ?: "Erudito Arcano",
          userGuildaId = prefs.getString("user_guilda_id", "g_solar"),
          trailMatematicaProgress = prefs.getInt("trail_mat_prog", 75).let { if (prefs.getInt("trail_mat_lvl", 1) == 1 && it < 75) 75 else it },
          trailMatematicaLevel = prefs.getInt("trail_mat_lvl", 1),
          trailPortuguesProgress = prefs.getInt("trail_port_prog", 0),
          trailPortuguesLevel = prefs.getInt("trail_port_lvl", 1),
          trailInglesProgress = prefs.getInt("trail_ing_prog", 0),
          trailInglesLevel = prefs.getInt("trail_ing_lvl", 1),
          trailHistoriaProgress = prefs.getInt("trail_hist_prog", 0),
          trailHistoriaLevel = prefs.getInt("trail_hist_lvl", 1),
          completedQuestsCsv = prefs.getString("completed_quests", "") ?: ""
        )
        // Backfill to Room
        dao?.insertOrUpdateProfile(spProfile)
        return@withContext spProfile
      }
    } catch (e: Exception) {
      Log.e("QuestiaPersistence", "Error loading profile", e)
    }
    return@withContext null
  }

  // Synchronous read from SharedPreferences for instant UI state initialization on process start
  fun loadProfileSync(): UserQuestiaProfile? {
    try {
      if (prefs != null && (prefs.getBoolean("is_registered", false) || !prefs.getString("user_name", "").isNullOrBlank())) {
        return UserQuestiaProfile(
          id = 1,
          userName = prefs.getString("user_name", "") ?: "",
          isRegistered = prefs.getBoolean("is_registered", true),
          heroLevel = prefs.getInt("hero_level", 1),
          heroXp = prefs.getInt("hero_xp", 120),
          heroGold = prefs.getInt("hero_gold", 150),
          heroHp = prefs.getInt("hero_hp", 100),
          heroMp = prefs.getInt("hero_mp", 80),
          selectedModelId = prefs.getString("selected_model_id", "hero_arcane") ?: "hero_arcane",
          selectedModelTitle = prefs.getString("selected_model_title", "Erudito Arcano") ?: "Erudito Arcano",
          userGuildaId = prefs.getString("user_guilda_id", "g_solar"),
          trailMatematicaProgress = prefs.getInt("trail_mat_prog", 75).let { if (prefs.getInt("trail_mat_lvl", 1) == 1 && it < 75) 75 else it },
          trailMatematicaLevel = prefs.getInt("trail_mat_lvl", 1),
          trailPortuguesProgress = prefs.getInt("trail_port_prog", 0),
          trailPortuguesLevel = prefs.getInt("trail_port_lvl", 1),
          trailInglesProgress = prefs.getInt("trail_ing_prog", 0),
          trailInglesLevel = prefs.getInt("trail_ing_lvl", 1),
          trailHistoriaProgress = prefs.getInt("trail_hist_prog", 0),
          trailHistoriaLevel = prefs.getInt("trail_hist_lvl", 1),
          completedQuestsCsv = prefs.getString("completed_quests", "") ?: ""
        )
      }
    } catch (e: Exception) {
      Log.e("QuestiaPersistence", "Error in loadProfileSync", e)
    }
    return null
  }

  // Synchronous write to SharedPreferences for instant atomic persistence on user actions or app exit
  fun saveProfileSync(profile: UserQuestiaProfile) {
    try {
      prefs?.edit()?.apply {
        putString("user_name", profile.userName)
        putBoolean("is_registered", profile.isRegistered)
        putInt("hero_level", profile.heroLevel)
        putInt("hero_xp", profile.heroXp)
        putInt("hero_gold", profile.heroGold)
        putInt("hero_hp", profile.heroHp)
        putInt("hero_mp", profile.heroMp)
        putString("selected_model_id", profile.selectedModelId)
        putString("selected_model_title", profile.selectedModelTitle)
        putString("user_guilda_id", profile.userGuildaId)
        putInt("trail_mat_prog", profile.trailMatematicaProgress)
        putInt("trail_mat_lvl", profile.trailMatematicaLevel)
        putInt("trail_port_prog", profile.trailPortuguesProgress)
        putInt("trail_port_lvl", profile.trailPortuguesLevel)
        putInt("trail_ing_prog", profile.trailInglesProgress)
        putInt("trail_ing_lvl", profile.trailInglesLevel)
        putInt("trail_hist_prog", profile.trailHistoriaProgress)
        putInt("trail_hist_lvl", profile.trailHistoriaLevel)
        putString("completed_quests", profile.completedQuestsCsv)
        commit()
      }
    } catch (e: Exception) {
      Log.e("QuestiaPersistence", "Error in saveProfileSync", e)
    }
  }

  suspend fun saveProfile(profile: UserQuestiaProfile) = withContext(Dispatchers.IO) {
    try {
      // Save to Room DB
      dao?.insertOrUpdateProfile(profile)

      // Synchronize to SharedPreferences
      prefs?.edit()?.apply {
        putString("user_name", profile.userName)
        putBoolean("is_registered", profile.isRegistered)
        putInt("hero_level", profile.heroLevel)
        putInt("hero_xp", profile.heroXp)
        putInt("hero_gold", profile.heroGold)
        putInt("hero_hp", profile.heroHp)
        putInt("hero_mp", profile.heroMp)
        putString("selected_model_id", profile.selectedModelId)
        putString("selected_model_title", profile.selectedModelTitle)
        putString("user_guilda_id", profile.userGuildaId)
        putInt("trail_mat_prog", profile.trailMatematicaProgress)
        putInt("trail_mat_lvl", profile.trailMatematicaLevel)
        putInt("trail_port_prog", profile.trailPortuguesProgress)
        putInt("trail_port_lvl", profile.trailPortuguesLevel)
        putInt("trail_ing_prog", profile.trailInglesProgress)
        putInt("trail_ing_lvl", profile.trailInglesLevel)
        putInt("trail_hist_prog", profile.trailHistoriaProgress)
        putInt("trail_hist_lvl", profile.trailHistoriaLevel)
        putString("completed_quests", profile.completedQuestsCsv)
        apply()
      }
    } catch (e: Exception) {
      Log.e("QuestiaPersistence", "Error saving profile", e)
    }
  }
}
