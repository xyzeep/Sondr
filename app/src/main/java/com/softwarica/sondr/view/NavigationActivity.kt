package com.softwarica.sondr.view
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.softwarica.sondr.R
import com.softwarica.sondr.view.pages.HomeFeedPage
import com.softwarica.sondr.view.pages.SearchPage
import com.softwarica.sondr.ui.theme.InterFont
import com.softwarica.sondr.ui.theme.LoraFont


// main class
class NavigationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavigationBody()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationBody() {
    data class BottomNavItem(val label: String, val iconResId: Int)
    var selectedIndex by remember { mutableIntStateOf(0) }
    // change this to 0

    val context = LocalContext.current
    val activity = context as? Activity

    val bottomNavItems = listOf(
        BottomNavItem("Home", R.drawable.baseline_home_24),
        BottomNavItem("Search", R.drawable.baseline_search_24),
        BottomNavItem("Create", R.drawable.baseline_add_circle_24),
        BottomNavItem("Notifications", R.drawable.baseline_notifications_none_24),
        BottomNavItem("Profile", R.drawable.baseline_person_outline_24)

    )

    // for top bar
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(

        topBar = {
            if (0 == selectedIndex) {
            TopAppBar(
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF121212),
                    navigationIconContentColor = Color.White,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sondr_logo),
                            contentDescription = null,
                            modifier = Modifier
                                .height(40.dp)
                                .width(40.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // space between logo and text
                        Text(
                            text = "Sondr.",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = LoraFont,
                            color = Color.White
                        )
                    }
                },
                actions = {
                    MoreOptionsMenu()
                }
            )
            }
        },

        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF121212),
                modifier = Modifier
                    .height(64.dp)
            ) {

                bottomNavItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = item.iconResId),
                                contentDescription = item.label,
                                modifier = Modifier.size(32.dp), // icon size
                            )
                        },
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.White.copy(alpha = 0.6f),
                            indicatorColor = Color.Transparent // no background color when selected
                        )
                    )
                }
            }
        }

    ) { innerPadding ->
        Box(modifier = Modifier

            .background(Color(0xFF121212))
            .padding(innerPadding)
            .fillMaxSize()) {
            when (selectedIndex) {
                0 -> HomeFeedPage()
                1 -> SearchPage()
            }
        }
    }
}

@Composable
fun MoreOptionsMenu() {
    var expanded by remember { mutableStateOf(false) }
    val bgColor = Color(0xFF242830)

    val context = LocalContext.current
    val activity = context as? Activity

    Box(
        modifier = Modifier
            .wrapContentSize(Alignment.TopEnd)
            .background(Color.Transparent)
    ) {
        Icon(
            painter = painterResource(R.drawable.baseline_menu_24),
            contentDescription = "menu_icon",
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .clickable {
                expanded = true
            },
            tint = Color.White
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(bgColor)
                .padding(vertical = 0.dp)
                .clip(RoundedCornerShape(0.dp)),

            // if something goes wrong in future, look up "properties = PopupProperties"

        )  {
            Column(modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(bgColor)
            )

            {
                DropdownMenuItem(
                    text = {
                        Text("Settings", fontFamily = InterFont, color = Color.White)
                    },

                    onClick = { expanded = false },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.baseline_settings_24),
                            contentDescription = "Settings Icon",
                            tint = Color.White
                        )
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text("About Sondr.", fontFamily = InterFont, color = Color.White)
                    },
                    onClick = { expanded = false },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.baseline_info_24),
                            contentDescription = "About Icon",
                            tint = Color.White
                        )
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text("Help & Support", fontFamily = InterFont, color = Color.White)
                    },
                    onClick = { expanded = false },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.baseline_help_24),
                            contentDescription = "Help Icon",
                            tint = Color.White
                        )
                    }
                )
                DropdownMenuItem(
                    text = {
                        Text("Logout", fontFamily = InterFont, color = Color.Red)
                    },
                    onClick = {
                        expanded = false
                        var intent = Intent(context, LoginActivity::class.java)
                        context.startActivity(intent)
                        activity?.finish()
                    },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(R.drawable.baseline_logout_24),
                            contentDescription = "Logout Icon",
                            tint = Color.Red
                        )
                    }
                )

            }

        }
    }
}

@Preview
@Composable
fun PreviewNavigation() {
    NavigationBody()
}