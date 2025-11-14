package com.example.tmdb.presentation.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.example.tmdb.R

@Composable
fun SearchSection(
    modifier: Modifier = Modifier,
    onSearchClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 30.dp)
            .clip(RoundedCornerShape(50))
//            .border(2.dp, Color(0x8FFFFFFF), RoundedCornerShape(50))
            .background(Color(0xFF36076B))
            .clickable {
                onSearchClick()
            },
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            "Search...",
            color = Color(0x8FFFFFFF),
            modifier = Modifier
                .padding(15.dp)
        )
        Icon(
            modifier = Modifier
                .padding(15.dp),
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_search),
            contentDescription = "Search",
            tint = Color(0x8FFFFFFF)
        )
    }
}