package com.softwarica.sondr.view.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.softwarica.sondr.R
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softwarica.sondr.model.UserModel
import com.softwarica.sondr.repository.UserRepositoryImpl

@Composable
fun SearchPage() {
    var query by remember { mutableStateOf("") }
    var searchResults by remember { mutableStateOf<List<UserModel>>(emptyList()) }
    val userRepository = UserRepositoryImpl(LocalContext.current)

    LaunchedEffect(query) {
        if (query.isNotEmpty()) {
            userRepository.usersRef.orderByChild("username").startAt(query).endAt(query + "\uf8ff").get()
                .addOnSuccessListener { snapshot ->
                    val users = mutableListOf<UserModel>()
                    snapshot.children.forEach { childSnapshot ->
                        childSnapshot.getValue(UserModel::class.java)?.let { users.add(it) }
                    }
                    searchResults = users
                }
        } else {
            searchResults = emptyList()
        }
    }

    Column(
        modifier = Modifier
            .background(color = Color(0xff121212))
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            query = query,
            onQueryChange = { query = it }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(searchResults) { user ->
                UserSearchResult(user = user)
            }
        }
    }
}

@Composable
fun UserSearchResult(user: UserModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1E1E1E), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(0xFF313B4D)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.blue_user),
                contentDescription = "User Icon",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )

        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = user.username,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = user.bio,
                style = TextStyle(
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 14.sp
                )
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
                            text = "Search for users",
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
