package com.example.groupy

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

data class TeamResult(
    val teamName: String,
    val members: List<String>
)

class TeamViewModel : ViewModel() {
    var namesInput by mutableStateOf("")
        private set

    var teamsCountInput by mutableStateOf("2")
        private set

    var generatedTeams by mutableStateOf<List<TeamResult>>(emptyList())
        private set

    fun updateNames(newNames: String) {
        namesInput = newNames
    }

    fun updateTeamsCount(newCount: String) {
        teamsCountInput = newCount
    }

    fun generateTeams() {
        val namesList = namesInput
            .split(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }

        val count = teamsCountInput.toIntOrNull() ?: 2
        if (namesList.isEmpty() || count <= 0) return

        val shuffledNames = namesList.shuffled()
        val teams = Array(count) { mutableListOf<String>() }

        shuffledNames.forEachIndexed { index, name ->
            teams[index % count].add(name)
        }

        generatedTeams = teams.mapIndexed { index, members ->
            TeamResult(teamName = "Team ${index + 1}", members = members)
        }
    }
}