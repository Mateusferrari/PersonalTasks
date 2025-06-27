package com.mateus.personaltasks.model

import java.io.Serializable

data class Task(
    val id: String? = null,
    val title: String = "",
    val description: String = "",
    val deadline: String = "",
    val isDone: Boolean = false,
    val deleted: Boolean = false,
    val prioridade: String = "Média"  // Novo campo com valor padrão
) : Serializable
