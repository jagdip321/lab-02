package com.example.listycity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository()
        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = {cityRepository.addCity(it)},
                        onDeleteCity = {cityRepository.deleteCity(it)},
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun CityListScreen(cities: List<String>, onAddCity:(String) -> Unit, onDeleteCity:(String) -> Unit, modifier: Modifier = Modifier) {
    var newCityName by remember { mutableStateOf("") }
    var selectedCity by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = newCityName,
                onValueChange = { newCityName = it },
                label = { Text("City name") },
                modifier = Modifier.weight(1f)
            )
        }
        Row(modifier = Modifier.padding(16.dp)) {
            Button(
                onClick = {
                    if (newCityName.isNotBlank()) {
                        onAddCity(newCityName)
                        newCityName = ""
                    }
                }
            ) {
                Text("Add City")
            }
            Spacer(modifier = Modifier.width(8.dp))

            // New Button to delete city from list
            Button(
                onClick = {
                    if (selectedCity.isNotBlank()) {
                        onDeleteCity(selectedCity)
                        selectedCity = ""
                    }
                }
            ) {
                Text("Delete City")
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(cities) { city ->
                // Modifier is passed to the CityRow composable such that selectable will change the selectedCity variable properly.
                // To select, we use selectable and change background color if selected
                CityRow(city = city, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 14.dp)
                    .selectable(selected = city == selectedCity, onClick = {selectedCity = city})
                    .background(if (city == selectedCity) androidx.compose.ui.graphics.Color.LightGray else androidx.compose.ui.graphics.Color.Transparent))
            }
        }
    }
}

@Composable
fun CityRow(city: String, modifier: Modifier = Modifier) {
    Text(text = city,
        fontSize = 28.sp,
        modifier = modifier)
}

class CityRepository {
    // Need mutableState here to update the UI
    private val _cities = mutableStateListOf("Edmonton", "Vancouver",
        "Moscow", "Sydney", "Berlin",
        "Vienna", "Tokyo", "Beijing", "Osaka", "New Delhi")

    val cities: List<String>
        get() = _cities
    fun addCity(city: String) {
        // Check if the city already exists by iterating through the list. If it exists, we don't add it.
        if (!_cities.any {it.equals(city, ignoreCase = true)}) {
            _cities.add(city)
        }
    }

    fun deleteCity(city: String) {
        _cities.remove(city)
    }
}