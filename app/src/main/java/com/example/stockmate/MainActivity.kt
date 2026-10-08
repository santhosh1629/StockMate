package com.example.stockmate

import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StockMateApp()
        }


    }
}
@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onFinished()
    }
    Column(modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Image(
            painterResource(R.drawable.ic_stockmate),
            contentDescription = "Welcome StockMate",
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text ="Welcome to StockMate",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

    }

}
@Composable
fun StockMateApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash"){
            SplashScreen(
                onFinished = {
                    navController.navigate("home"){
                        popUpTo("splash"){ inclusive = true
                        }
                    }
                }
            )
        }
        composable("home"){
            HomeScreen(navController)
        }
        composable("Products"){
            Text("ProductsScreen",
                Modifier.padding(top = 450.dp, start = 150.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp)


        }
        composable("Sales"){
            Text("SalesScreen",
                Modifier.padding(top = 450.dp, start = 150.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp)


        }
        composable("Profile"){
            Text("ProfileScreen",
                Modifier.padding(top = 450.dp, start = 150.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp)


        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "StockMate",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF00A86B),
                    titleContentColor = Color.White
                )
            )

        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        navController.navigate("home")
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = {
                        Text("Home")
                    }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("products") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = "Products"
                        )
                    },
                    label = {
                        Text("Products")
                    }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("sales") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Sales"
                        )
                    },
                    label = {
                        Text("Sales")
                    }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {  navController.navigate("profile") },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile"
                        )
                    },
                    label = {
                        Text("Profile")
                    }
                )

            }
        }


    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = 2.dp, bottom = 8.dp, start = 3.dp, end = 3.dp),
        ) {
            Spacer(
                modifier = Modifier.height(2.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .padding(vertical = 15.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF9BEF9F)
                )
            ) {
                Column(
                    modifier = Modifier.padding(
                        top = 20.dp,
                        bottom = 8.dp,
                        start = 3.dp,
                        end = 3.dp
                    )
                ) {
                    Text(
                        text = "Welcome To",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    )
                    Text(
                        text = "StockMate",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF009E60)
                    )
                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )
                    Text(
                        text = "Smart Inventory Management",
                        fontSize = 16.sp,
                        color = Color.Gray

                    )
                }
            }


            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF0AE119)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painterResource(R.drawable.ic_orderbox),
                            contentDescription = "ic_orderbox",
                            modifier = Modifier.size(50.dp)
                        )

                        Text(
                            text = "12",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,

                        )

                        Text(
                            text = "Total Products",
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE32A2A)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painterResource(R.drawable.ic_lowstock),
                            contentDescription = "ic_orderbox",
                            modifier = Modifier.size(50.dp)
                        )


                        Text(
                            text = "3",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Low Stock",
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF0954EA)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painterResource(R.drawable.ic_totalsales),
                            contentDescription = "ic_orderbox",
                            modifier = Modifier.size(50.dp)
                        )


                        Text(
                            text = "₹1629",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Total Sales",
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(1f)
                        .padding(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFC822EE)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painterResource(R.drawable.ic_totalsales),
                            contentDescription = "Ic_todaysales",
                            modifier = Modifier.size(50.dp)
                        )

                        Text(
                            text = "3",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Today's Sales",
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(20.dp)
            )
            Text(
                text = "Quick Actions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(15.dp))
            Row(modifier = Modifier.fillMaxWidth()
            ) {
                Card(modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
                    .padding(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, start = 15.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "➕",
                            fontSize = 25.sp
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(text = "Add Product",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Card(modifier = Modifier
                    .weight(1f)
                    .height(100.dp)
                    .padding(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, start = 15.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "\uD83D\uDCCB",
                            fontSize = 30.sp
                        )
                        Spacer(modifier = Modifier.width(11.dp))
                        Text(
                            text = "View Products",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Row(modifier = Modifier.fillMaxWidth()
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .padding(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp, start = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "\uD83D\uDCE5",
                            fontSize = 25.sp
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(
                            text = "Stock In",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .padding(4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 30.dp, start = 15.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "\uD83D\uDCE4",
                            fontSize = 25.sp
                        )
                        Spacer(modifier = Modifier.width(15.dp))
                        Text(
                            text = "Stock Out",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                }
            }
        }

}














