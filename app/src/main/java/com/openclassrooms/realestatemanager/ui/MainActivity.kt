package com.openclassrooms.realestatemanager.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.data.room.entities.Realty
import com.openclassrooms.realestatemanager.ui.theme.RealEstateManagerTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: MainViewModel

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            viewModel = koinViewModel()

            val realties by viewModel.realties.collectAsState(initial = emptyList())

            RealEstateManagerTheme {
                Scaffold(modifier = Modifier.fillMaxSize(), topBar = {
                    TopAppBar(
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Color.Gray,
                            navigationIconContentColor = Color.White,
                            titleContentColor = Color.White,
                            actionIconContentColor = Color.White
                        ),
                        title = { Text(text = "Real Estate") },
                        actions = {
                            IconButton(onClick = {
                                val intent = Intent(this@MainActivity, FormActivity::class.java)
                                startActivity(intent)
                            }) {
                                Icon(
                                    imageVector = Icons.Filled.Add,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Filled.Create,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Filled.Search,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = {}) {
                                Icon(
                                    imageVector = Icons.Filled.Menu,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                    )
                }) { innerPadding ->
                    if (realties.isNotEmpty()) {
                        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {
                            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                                items(realties.size) { index ->
                                    RealtyItem(
                                        realty = realties[index],
                                        viewModel = viewModel,
                                        context = this@MainActivity
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RealtyItem(realty: Realty, viewModel: MainViewModel, context: Context) {
    val uri = Uri.parse(realty.pictures.first().uriString)
    val bitmap = viewModel.uriToBitmapLegacy(context, uri) ?: return
    Column(modifier = Modifier.fillMaxWidth().height(100.dp), verticalArrangement = Arrangement.Center) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier.size(90.dp)
            )
            Spacer(modifier = Modifier.size(10.dp))

            Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxHeight()) {
                Text(text = realty.primaryInfo.realtyType.name, color = Color.Black, fontWeight = FontWeight.Bold)
                Text(text = realty.primaryInfo.realtyPlace.name, color = Color.Gray)
                Text(text = realty.primaryInfo.price.toString() + "$", color = Color.Red, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RealEstateManagerTheme {

    }
}