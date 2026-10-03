package com.example.model

enum class MessageSender {
  SYSTEM,
  COPILOT,
  USER
}

data class ChatMessage(
  val id: String,
  val sender: MessageSender,
  val text: String,
  val timestamp: Long = System.currentTimeMillis(),
  val axisFeedback: List<AxisFeedback>? = null,
  val isSocraticHint: Boolean = false,
  val equationHighlight: String? = null
)
