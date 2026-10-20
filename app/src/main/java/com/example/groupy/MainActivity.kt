package com.example.groupy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@Composable
fun AppHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun TeamCard(teamResult: TeamResult) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = teamResult.teamName,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            teamResult.members.forEach { member ->
                Text(
                    text = "• $member",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun CustomIntInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = { input ->
            if (input.isEmpty() || input.toIntOrNull() != null) {
                onValueChange(input)
            }
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
fun MainScreen(viewModel: TeamViewModel = viewModel()) {
    var currentStep by rememberSaveable { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .statusBarsPadding()
    ) {
        AppHeader(title = if (currentStep == 1) "Groupy" else "Groups are ready!")
        Spacer(modifier = Modifier.height(16.dp))

        if (currentStep == 1) {
            Column(modifier = Modifier.weight(1f)) {
                CustomInputField(
                    value = viewModel.namesInput,
                    onValueChange = { viewModel.updateNames(it) },
                    label = "Enter your names (Jake, Peter, Sam...)"
                )
                Spacer(modifier = Modifier.height(12.dp))
                CustomIntInputField(
                    value = viewModel.teamsCountInput,
                    onValueChange = { viewModel.updateTeamsCount(it) },
                    label = "Number of groups"
                )
            }

            Button(
                onClick = {
                    viewModel.generateTeams()
                    currentStep = 2
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Group us!")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(viewModel.generatedTeams) { team ->
                    TeamCard(teamResult = team)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { currentStep = 1 },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Back to main")
            }
        }
    }
}