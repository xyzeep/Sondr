package com.softwarica.sondr.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier

import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softwarica.sondr.R


@Composable
fun SearchPage() {

    var query by remember { mutableStateOf("") }

    Column (
        modifier = Modifier
            .background(color = Color(0xff121212))
            .fillMaxSize()
            .padding(16.dp ),
        horizontalAlignment = Alignment.CenterHorizontally

    ){
            Row (
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center

                )
            {
                SearchBar(
                    query = query,
                    onQueryChange = { query = it }
                )
            }

    }

}


@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.run {
            fillMaxWidth()
                .height(44.dp)
                .padding(horizontal = 0.dp)
                .background(
                    color = Color(0xFF313B4D),
                    shape = RoundedCornerShape(8.dp)
                )
                .border(
                    width = 1.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(horizontal = 12.dp, vertical = 8.dp)
        },
        contentAlignment = Alignment.CenterStart
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp

                ),
                cursorBrush = SolidColor(Color.White),
                modifier = Modifier.weight(1f),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(
                            text = "Search for posts or users",
                            color = Color.White.copy(alpha = 0.5f),
                            fontSize = 20.sp
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}

