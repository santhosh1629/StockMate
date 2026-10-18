package com.example.stockmate

import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material3.IconButton
import com.google.androidgamesdk.gametextinput.Settings

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
            ProductsScreen(navController)

        }
        composable("Sales"){
            SalesScreen(navController)

        }
        composable("Profile"){
            ProfileScreen(navController)

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
            NavigationBar(contentColor = Color(0xFFFCFDFD)) {
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        navController.navigate("home"){
                            launchSingleTop = true
                        }
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
                    onClick = { navController.navigate("products") {
                        launchSingleTop = true
                    }
                              },
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
                    onClick = { navController.navigate("sales"){
                        launchSingleTop = true
                    } },
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
                    onClick = {  navController.navigate("profile"){
                        launchSingleTop = true
                    }
                              },
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
@Composable
fun ProductsScreen(navController: NavHostController) {
    var selectedItem by remember {
        mutableIntStateOf(1)
    }

    Scaffold(
        bottomBar = {
            NavigationBar (
                containerColor = Color.White,
                contentColor = Color(0xFF008577)
            ){

                NavigationBarItem(
                    selected = selectedItem == 0,
                    onClick = { navController.navigate("home"){
                        launchSingleTop = true
                    }},
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = { Text("Home") } ,
                    colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color(0xFF008577),
                    selectedTextColor = Color(0xFF008577),
                    indicatorColor = Color(0xFFE0F2E9),
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray
                )
                )

                NavigationBarItem(
                    selected = selectedItem == 1,
                    onClick = {  navController.navigate("products"){
                        launchSingleTop = true
                    }
                              },
                    icon = {
                        Icon(
                            Icons.Default.Inventory2,
                            contentDescription = "Products"
                        )
                    },
                    label = { Text("Products") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF008577),
                        selectedTextColor = Color(0xFF008577),
                        indicatorColor = Color(0xFFE0F2E9),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )


                NavigationBarItem(
                    selected = selectedItem == 2,
                    onClick = {  navController.navigate("sales"){
                        launchSingleTop = true
                    } },
                    icon = {
                        Icon(
                            Icons.Default.BarChart,
                            contentDescription = "Sales"
                        )
                    },
                    label = { Text("Sales") }
                )

                NavigationBarItem(
                    selected = selectedItem == 3,
                    onClick = {  navController.navigate("profile"){
                        launchSingleTop = true
                    } },
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile"
                        )
                    },
                    label = { Text("Profile") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF008577),
                        selectedTextColor = Color(0xFF008577),
                        indicatorColor = Color(0xFFE0F2E9),
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray
                    )
                )

            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7F5))
                .padding(innerPadding)

        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF008577))
                    .statusBarsPadding()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Products",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = {
                        Text("Search products...")
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {},
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Add")
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)) {

                Text(
                    text = "All Products",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                ProductCard(
                    name = "Rice Bag",
                    price = "₹850",
                    quantity = 25,
                    isLowStock = false
                )

                ProductCard(
                    name = "Cooking Oil",
                    price = "₹180",
                    quantity = 3,
                    isLowStock = true
                )

                ProductCard(
                    name = "Sugar",
                    price = "₹50",
                    quantity = 15,
                    isLowStock = false
                )
                ProductCard(
                    name = "Notebook",
                    price = "₹40",
                    quantity = 50,
                    isLowStock = false
                )

                ProductCard(
                    name = "Pen",
                    price = "₹10",
                    quantity = 2,
                    isLowStock = true
                )
            }
        }
    }
}
@Composable
fun ProductCard(
    name: String,
    price: String,
    quantity: Int,
    isLowStock: Boolean
) {
    var showMenu by remember {
        mutableStateOf(false)
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Price: $price",
                    fontSize = 14.sp
                )

                Text(
                    text = "Stock: $quantity",
                    fontSize = 14.sp
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (isLowStock) "Low Stock" else "In Stock",
                    color = if (isLowStock) Color.Red else Color(0xFF2E7D32),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp
                )
                Box {
                    IconButton(
                        onClick = {
                            showMenu = true

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More options",
                            tint = Color.Gray
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = {
                            showMenu = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Edit") },
                            onClick = {
                                showMenu = false
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                showMenu = false
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SalesScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            var selectedItem by remember {
                mutableIntStateOf(2)
            }

            NavigationBar {
                NavigationBarItem(
                    selected = selectedItem == 0,
                    onClick = {  navController.navigate("home"){
                        launchSingleTop = true
                    }},
                    icon = {
                        Icon(
                            Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = selectedItem == 1,
                    onClick = {  navController.navigate("products"){
                        launchSingleTop = true
                    } },
                    icon = {
                        Icon(
                            Icons.Default.Inventory2,
                            contentDescription = "Products"
                        )
                    },
                    label = { Text("Products") }
                )

                NavigationBarItem(
                    selected = selectedItem == 2,
                    onClick = { navController.navigate("sales"){
                        launchSingleTop = true
                    }},

                    icon = {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Sales",
                            tint = if (selectedItem == 2)
                                Color(0xFF008577)
                            else
                                Color.Gray
                        )
                    },
                    label = { Text("Sales") }
                )

                NavigationBarItem(
                    selected = selectedItem == 3,
                    onClick = {  navController.navigate("profile"){
                        launchSingleTop = true
                    } },
                    icon = {
                        Icon(
                            Icons.Default.Person,
                            contentDescription = "Profile"
                        )
                    },
                    label = { Text("Profile") }
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7F5))
                .padding(innerPadding)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF008577))
                    .statusBarsPadding()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 10.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sales",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE0F2E9)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Today's Sales",
                            tint = Color(0xFF008577),
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "8",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Today's Sales",
                            fontSize = 14.sp
                        )
                    }
                }

                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE3EDFF)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Total Sales",
                            tint = Color(0xFF1976D2),
                            modifier = Modifier.size(32.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "50",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "Total Sales",
                            fontSize = 14.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Recent Sales",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                TextButton(
                    onClick = {}
                ) {
                    Text(
                        text = "View All",
                        color = Color(0xFF008577)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Rice Bag",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "2 x ₹850",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )

                        Text(
                            text = "Oct 17, 10:30 AM",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    Text(
                        text = "₹1,700",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Cooking Oil",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "3 x ₹180",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )

                        Text(
                            text = "Oct 17, 09:45 AM",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    Text(
                        text = "₹540",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 2.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Sugar",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = "5 x ₹50",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )

                        Text(
                            text = "Oct 16, 05:20 PM",
                            fontSize = 11.sp,
                            color = Color.Gray
                        )
                    }

                    Text(
                        text = "₹250",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
@Composable
fun ProfileScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            val selectedItem by remember {
                mutableIntStateOf(0)
            }
            NavigationBar(
                containerColor = Color(0xFFFCFDFD)
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("home") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home"
                        )
                    },
                    label = { Text("Home") }
                )


                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("products") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Inventory2,
                            contentDescription = "Products"
                        )
                    },
                    label = { Text("Products") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("sales") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Sales"
                        )
                    },
                    label = { Text("Sales") }
                )


                NavigationBarItem(
                    selected = true,
                    onClick = {
                        navController.navigate("profile") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile"
                        )
                    },
                    label = {
                        Text("Profile")
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF008577),
                        selectedTextColor = Color(0xFF008577),
                        unselectedIconColor = Color.DarkGray,
                        unselectedTextColor = Color.DarkGray,
                        indicatorColor = Color(0xFFE0F2EF)
                    )
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F7F5))
                .padding(innerPadding)
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF008577))
                    .statusBarsPadding()
                    .padding(
                        horizontal = 16.dp,
                        vertical = 10.dp
                    ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Profile",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(
                            color = Color(0xFFE0E5EB),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile",
                        modifier = Modifier.size(65.dp),
                        tint = Color(0xFF78909C)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Santhosh.P",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Admin",
                    fontSize = 15.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(28.dp))


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE8F5F1)
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 0.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(vertical = 8.dp)
                ) {

                    ProfileOption(
                        icon = Icons.Default.Person,
                        title = "Account Information"
                    )

                    ProfileOption(
                        icon = Icons.Default.Settings,
                        title = "App Settings"
                    )

                    ProfileOption(
                        icon = Icons.Default.Help,
                        title = "Help & Support"
                    )

                    ProfileOption(
                        icon = Icons.Default.Info,
                        title = "About StockMate"
                    )
                }
            }


            Spacer(modifier = Modifier.height(28.dp))


            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE53945)
                )
            ) {

                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Logout",
                    tint = Color.White
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Logout",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}


    @Composable
    fun ProfileOption(
        icon: ImageVector,
        title: String
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(
                    horizontal = 16.dp,
                    vertical = 16.dp
                ),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF008577),
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = title,
                modifier = Modifier.weight(1f),
                fontSize = 15.sp,
                color = Color.Black
            )

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = "Open $title",
                tint = Color.Gray
            )
        }
    }

@Preview(showBackground = true)
@Composable
fun ProductsScreenPreview(){
    ProductsScreen(rememberNavController())
}

















