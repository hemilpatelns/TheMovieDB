package com.example.tmdb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Home()
        }
    }
}

@Preview
@Composable
private fun HomePreview() {
    Home()
}

@Composable
fun Home() {
    var selectedCategory by remember {
        mutableStateOf("Movies")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF8000FF), Color(0xFF303243), Color(0xFF303243)),
                ),
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 30.dp)
                .padding(top = 50.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "What do you want to watch today?",
                style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.weight(.7f),
            )
            Spacer(modifier = Modifier.width(15.dp))
            Image(
                modifier = Modifier
                    .size(40.dp)
                    .border(2.dp, Color.White, RoundedCornerShape(50))
                    .padding(3.dp),
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_person),
                contentDescription = ""
            )
        }
        SearchScreen()
        CategorySelector(
            categories = listOf("Movies", "Series", "Anime", "Novels", "Documentaries"),
            onCategorySelected = {
                selectedCategory = it
            }
        )
        VideoList("Trending", selectedCategory)
        VideoList("Most Popular", selectedCategory)
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 30.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // TextField for input
        TextField(
            value = query,
            onValueChange = { onQueryChange(it) },
            placeholder = { Text("Search", color = Color(0x8FFFFFFF)) },
            singleLine = true,
            modifier = Modifier
                .weight(1f),
            trailingIcon = {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_search), // Replace with your search icon
                    contentDescription = "Search",
                    tint = Color(0x8FFFFFFF)
                )
            },
            shape = RoundedCornerShape(50.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF36076B),
                unfocusedContainerColor = Color(0xFF36076B),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun SearchScreen() {
    var searchQuery by remember { mutableStateOf("") }

    Column {
        SearchBar(
            query = searchQuery,
            onQueryChange = { searchQuery = it }
        )
    }
}

@Composable
fun VideoCard(name: String) {
    val context = LocalContext.current
    Card(
        modifier = Modifier,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.background(color = Color.Transparent),
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_home),
                contentDescription = "Video image",
                modifier = Modifier
                    .width(150.dp)
                    .height(230.dp)
                    .aspectRatio(.65f)
                    .clip(RoundedCornerShape(10))
                    .clickable { setOnClick(context) },
                contentScale = ContentScale.Crop
            )
            Text(
                text = name,
                color = Color.White
            )
        }
    }
}

@Composable
fun VideoList(videoType: String, videoName: String) {
    Text(
        text = videoType,
        style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold),
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .padding(top = 30.dp, bottom = 20.dp),
        color = Color.White
    )
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy((-48).dp)
    ) {
        items(6) {
            Spacer(modifier = Modifier.padding(start = 30.dp))
            VideoCard("$videoName $it")
            Spacer(modifier = Modifier.padding(end = 30.dp))
        }
    }
}

@Composable
fun CategoryItem(category: String, isSelected: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .padding(vertical = 10.dp)
            .clickable { onClick() }
            .background(
                if (isSelected) Color(0xFFFF1F8A)
                else Color.Transparent,
                shape = RoundedCornerShape(50.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
            text = category,
            color = if (isSelected) Color.White else Color(0x8FFFFFFF),
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
            fontSize = 16.sp
        )
    }
}

@Composable
fun CategorySelector(categories: List<String>, onCategorySelected: (String) -> Unit) {
    var selectedCategory by remember { mutableStateOf(categories.first()) }

    LazyRow(
        horizontalArrangement = Arrangement.spacedBy((-45).dp),
        modifier = Modifier
            .fillMaxWidth()
    ) {
        items(categories) { category ->
            Spacer(modifier = Modifier.padding(start = 30.dp))
            CategoryItem(
                category = category,
                isSelected = category == selectedCategory,
                onClick = {
                    selectedCategory = category
                    onCategorySelected(category)
                }
            )
            Spacer(modifier = Modifier.padding(start = 30.dp))
        }
    }
}

private fun setOnClick(context: Context) {
    Intent(context, VideoDetailActivity::class.java).also {
        context.startActivity(it)
    }
}
