package com.example.stockmate

import android.os.Bundle
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.IconButton
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.DisposableEffect

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
fun StockMateApp() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(
                onFinished = {
                    val destination =
                        if (FirebaseAuth.getInstance().currentUser != null) {
                            "home"
                        } else {
                            "login"
                        }

                    navController.navigate(destination) {
                        popUpTo("splash") {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(route = "login") {
            LoginScreen(
                onLoginClick = { email, password ->
                    FirebaseAuth.getInstance()
                        .signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                navController.navigate("home") {
                                    popUpTo("login") {
                                        inclusive = true
                                    }
                                }
                            } else {
                                Toast.makeText(
                                    navController.context,
                                    task.exception?.message ?: "Login failed",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }
        composable(route = "register") {
            RegisterScreen(
                onRegisterClick = { name, email, password ->
                    FirebaseAuth.getInstance()
                        .createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                val user = FirebaseAuth.getInstance().currentUser

                                val profileUpdates =
                                    com.google.firebase.auth.UserProfileChangeRequest.Builder()
                                        .setDisplayName(name)
                                        .build()

                                user?.updateProfile(profileUpdates)
                                    ?.addOnCompleteListener { updateTask ->
                                        if (updateTask.isSuccessful) {
                                            navController.navigate("home") {
                                                popUpTo("register") {
                                                    inclusive = true
                                                }
                                            }
                                        } else {
                                            Toast.makeText(
                                                navController.context,
                                                "Name save failed",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                            } else {
                                Toast.makeText(
                                    navController.context,
                                    task.exception?.message ?: "Registration failed",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                },
                onLoginClick = {
                    navController.navigate("login") {
                        popUpTo("register") {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable("home"){
            HomeScreen(navController)
        }
        composable("newBill"){
            NewBillScreen(navController)

        }
        composable("products"){
            ProductsScreen(navController)

        }
        composable("sales"){
            SalesScreen(navController)

        }
        composable("profile"){
            ProfileScreen(navController)

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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    var products by remember {
        mutableStateOf<List<Product>>(emptyList())
    }

    var sales by remember {
        mutableStateOf<List<Sale>>(emptyList())
    }

    val productRepository = remember {
        ProductRepository()
    }

    val salesRepository = remember {
        SalesRepository()
    }

    val totalProducts = products.size

    val lowStockCount = products.count {
        it.quantity <= 5
    }

    val totalSales = sales.sumOf {
        it.totalAmount
    }

    val todaySalesCount = sales.count { sale ->
        val saleCalendar = java.util.Calendar.getInstance()
        saleCalendar.timeInMillis = sale.timestamp

        val todayCalendar = java.util.Calendar.getInstance()

        saleCalendar.get(java.util.Calendar.YEAR) ==
                todayCalendar.get(java.util.Calendar.YEAR) &&
                saleCalendar.get(java.util.Calendar.DAY_OF_YEAR) ==
                todayCalendar.get(java.util.Calendar.DAY_OF_YEAR)
    }

    DisposableEffect(Unit) {
        val productListener = productRepository.listenToProducts(
            onProductsChanged = { updatedProducts ->
                products = updatedProducts
            },
            onFailure = { error ->
                android.util.Log.e("HomeScreen", error)
            }
        )

        val salesListener = salesRepository.listenToSales(
            onSalesChanged = { updatedSales ->
                sales = updatedSales
            },
            onFailure = { error ->
                android.util.Log.e("HomeScreen", error)
            }
        )

        onDispose {
            productListener.remove()
            salesListener.remove()
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "StockMate",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF259468)
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFFFCFDFD)
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = {
                        navController.navigate("home") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            Icons.Default.Home,
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
                            Icons.Default.Inventory2,
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
                            Icons.Default.BarChart,
                            contentDescription = "Sales"
                        )
                    },
                    label = { Text("Sales") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("profile") {
                            launchSingleTop = true
                        }
                    },
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
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFE2E2E3)
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    Text(
                        text = "Welcome to StockMate",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Smart Inventory Management",
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HomeStatCard(
                    modifier = Modifier.weight(1f),
                    image = R.drawable.ic_orderbox,
                    value = totalProducts.toString(),
                    title = "Total Products",
                    color = Color(0xFFA2EEDD)
                )

                HomeStatCard(
                    modifier = Modifier.weight(1f),
                    image = R.drawable.ic_lowstock,
                    value = lowStockCount.toString(),
                    title = "Low Stock",
                    color = Color(0xFFFA8484)
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                HomeStatCard(
                    modifier = Modifier.weight(1f),
                    image = R.drawable.ic_totalsales,
                    value = "₹%.2f".format(totalSales),
                    title = "Total Sales",
                    color = Color(0xFFAFD4F1)
                )

                HomeStatCard(
                    modifier = Modifier.weight(1f),
                    image = R.drawable.ic_todaysales,
                    value = todaySalesCount.toString(),
                    title = "Today's Sales",
                    color = Color(0xFFDABDEC)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Quick Actions",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 8.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    onClick = {
                        navController.navigate("newBill") {
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .padding(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_orderbox),
                            contentDescription = "New Bill",
                            modifier = Modifier.size(36.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "New Bill",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                Card(
                    onClick = {
                        navController.navigate("sales") {
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(100.dp)
                        .padding(4.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_totalsales),
                            contentDescription = "View Bills",
                            modifier = Modifier.size(36.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text(
                            text = "View Bills",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(
                    onClick = {
                        navController.navigate("products") {
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(90.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_lowstock),
                            contentDescription = "Stock In",
                            modifier = Modifier.size(36.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text("Stock In", fontSize = 14.sp)
                    }
                }

                Card(
                    onClick = {
                        navController.navigate("products") {
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(90.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE8F5E9)
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_lowstock),
                            contentDescription = "Stock Out",
                            modifier = Modifier.size(36.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        Text("Stock Out", fontSize = 14.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun HomeStatCard(
    modifier: Modifier,
    image: Int,
    value: String,
    title: String,
    color: Color
) {
    Card(
        modifier = modifier
            .height(145.dp)
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = color
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(image),
                contentDescription = title,
                modifier = Modifier.size(42.dp)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = title,
                fontSize = 13.sp
            )
        }
    }
}
@Composable
fun ProductsScreen(navController: NavHostController) {
    var showAddProductDialog by remember { mutableStateOf(false) }
    var showEditProductDialog by remember { mutableStateOf(false) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var editName by remember { mutableStateOf("") }
    var editPrice by remember { mutableStateOf("") }
    var editQuantity by remember { mutableStateOf("") }
    var editCategory by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productQuantity by remember { mutableStateOf("") }
    var productCategory by remember { mutableStateOf("") }
    var selectedItem by remember { mutableIntStateOf(1) }
    var searchQuery by remember { mutableStateOf("") }

    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var saveMessage by remember { mutableStateOf<String?>(null) }

    val repository = remember { ProductRepository() }

    DisposableEffect(Unit) {
        val registration = com.google.firebase.firestore.FirebaseFirestore
            .getInstance()
            .collection("products")
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    errorMessage = error.localizedMessage ?: "Unable to load products"
                    isLoading = false
                    return@addSnapshotListener
                }

                products = snapshot?.documents?.mapNotNull { document ->
                    document.toObject(Product::class.java)?.copy(id = document.id)
                } ?: emptyList()

                errorMessage = null
                isLoading = false
            }

        onDispose {
            registration.remove()
        }

    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                contentColor = Color(0xFF008577)
            ) {
                NavigationBarItem(
                    selected = selectedItem == 0,
                    onClick = {
                        selectedItem = 0
                        navController.navigate("home") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = selectedItem == 1,
                    onClick = {
                        selectedItem = 1
                        navController.navigate("products") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(Icons.Default.Inventory2, contentDescription = "Products")
                    },
                    label = { Text("Products") }
                )

                NavigationBarItem(
                    selected = selectedItem == 2,
                    onClick = {
                        selectedItem = 2
                        navController.navigate("sales") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(Icons.Default.BarChart, contentDescription = "Sales")
                    },
                    label = { Text("Sales") }
                )

                NavigationBarItem(
                    selected = selectedItem == 3,
                    onClick = {
                        selectedItem = 3
                        navController.navigate("profile") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
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
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Products",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text("Search products...")
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = {
                        productName = ""
                        productPrice = ""
                        productQuantity = ""
                        productCategory = ""
                        saveMessage = null
                        showAddProductDialog = true
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Add")
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "All Products",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 15.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(20.dp)
                    )
                }

                errorMessage != null -> {
                    Text(
                        text = errorMessage!!,
                        color = Color.Red,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                products.isEmpty() -> {
                    Text(
                        text = "No products yet. Tap Add to create one.",
                        modifier = Modifier.padding(16.dp)
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        products.filter { product ->
                            product.name.contains(searchQuery, ignoreCase = true) ||
                                    product.category.contains(searchQuery, ignoreCase = true)
                        }.forEach { product ->
                            ProductCard(
                                productId = product.id,
                                name = product.name,
                                price = "₹${product.price}",
                                quantity = product.quantity,
                                isLowStock = product.quantity <= 5,
                                onEdit = {
                                    selectedProduct = product
                                    editName = product.name
                                    editPrice = product.price.toString()
                                    editQuantity = product.quantity.toString()
                                    editCategory = product.category
                                    showEditProductDialog = true
                                },
                                onDelete = {
                                    repository.deleteProduct(
                                        productId = product.id,
                                        onSuccess = {},
                                        onFailure = { error ->
                                            errorMessage = error
                                        }
                                    )
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }

    if (showAddProductDialog) {
        AlertDialog(
            onDismissRequest = {
                showAddProductDialog = false
            },
            title = {
                Text("Add Product")
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = productName,
                        onValueChange = { productName = it },
                        label = { Text("Product Name") },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = productPrice,
                        onValueChange = { productPrice = it },
                        label = { Text("Price") },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = productQuantity,
                        onValueChange = { productQuantity = it },
                        label = { Text("Quantity") },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = productCategory,
                        onValueChange = { productCategory = it },
                        label = { Text("Category") },
                        singleLine = true
                    )

                    if (saveMessage != null) {
                        Text(
                            text = saveMessage!!,
                            color = Color.Red
                        )
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val price = productPrice.toDoubleOrNull()
                        val quantity = productQuantity.toIntOrNull()

                        when {
                            productName.isBlank() -> {
                                saveMessage = "Enter product name"
                            }

                            price == null || price < 0 -> {
                                saveMessage = "Enter a valid price"
                            }

                            quantity == null || quantity < 0 -> {
                                saveMessage = "Enter a valid quantity"
                            }

                            productCategory.isBlank() -> {
                                saveMessage = "Enter product category"
                            }

                            else -> {
                                saveMessage = null

                                val product = Product(
                                    name = productName.trim(),
                                    price = price,
                                    quantity = quantity,
                                    category = productCategory.trim()
                                )

                                repository.addProduct(
                                    product = product,
                                    onSuccess = {
                                        showAddProductDialog = false
                                        productName = ""
                                        productPrice = ""
                                        productQuantity = ""
                                        productCategory = ""
                                    },
                                    onFailure = { message ->
                                        saveMessage = message
                                    }
                                )
                            }
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showAddProductDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
    if (showEditProductDialog && selectedProduct != null) {
        AlertDialog(
            onDismissRequest = {
                showEditProductDialog = false
            },
            title = {
                Text("Edit Product")
            },
            text = {
                Column {
                    OutlinedTextField(
                        value = editName,
                        onValueChange = { editName = it },
                        label = { Text("Product Name") }
                    )

                    OutlinedTextField(
                        value = editPrice,
                        onValueChange = { editPrice = it },
                        label = { Text("Price") }
                    )

                    OutlinedTextField(
                        value = editQuantity,
                        onValueChange = { editQuantity = it },
                        label = { Text("Quantity") }
                    )

                    OutlinedTextField(
                        value = editCategory,
                        onValueChange = { editCategory = it },
                        label = { Text("Category") }
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val price = editPrice.toDoubleOrNull()
                        val quantity = editQuantity.toIntOrNull()

                        if (
                            editName.isNotBlank() &&
                            price != null && price >= 0 &&
                            quantity != null && quantity >= 0 &&
                            editCategory.isNotBlank()
                        ) {
                            val updatedProduct = selectedProduct!!.copy(
                                name = editName.trim(),
                                price = price,
                                quantity = quantity,
                                category = editCategory.trim()
                            )

                            repository.updateProduct(
                                product = updatedProduct,
                                onSuccess = {
                                    showEditProductDialog = false
                                    selectedProduct = null
                                },
                                onFailure = { error ->
                                    errorMessage = error
                                }
                            )
                        }
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        showEditProductDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

}
@Composable
fun ProductCard(
    productId: String,
    name: String,
    price: String,
    quantity: Int,
    isLowStock: Boolean,
    onEdit: () -> Unit,
    onDelete: () -> Unit
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
                    color = if (isLowStock) Color.Red
                    else Color(0xFF2E7D32),
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
                                onEdit()
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Delete") },
                            onClick = {
                                showMenu = false
                                onDelete()
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

    var sales by remember { mutableStateOf<List<Sale>>(emptyList()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val salesRepository = remember { SalesRepository() }


    val todaySalesCount = sales.count { sale ->
        val saleDate = java.util.Calendar.getInstance().apply {
            timeInMillis = sale.timestamp
        }

        val today = java.util.Calendar.getInstance()

        saleDate.get(java.util.Calendar.YEAR) ==
                today.get(java.util.Calendar.YEAR) &&
                saleDate.get(java.util.Calendar.DAY_OF_YEAR) ==
                today.get(java.util.Calendar.DAY_OF_YEAR)
    }

    val todayRevenue = sales
        .filter { sale ->
            val saleDate = java.util.Calendar.getInstance().apply {
                timeInMillis = sale.timestamp
            }

            val today = java.util.Calendar.getInstance()

            saleDate.get(java.util.Calendar.YEAR) ==
                    today.get(java.util.Calendar.YEAR) &&
                    saleDate.get(java.util.Calendar.DAY_OF_YEAR) ==
                    today.get(java.util.Calendar.DAY_OF_YEAR)
        }
        .sumOf { it.totalAmount }


    DisposableEffect(Unit) {
        val registration = salesRepository.listenToSales(
            onSalesChanged = { updatedSales ->
                sales = updatedSales.sortedByDescending { it.timestamp }
                errorMessage = null
            },
            onFailure = { error ->
                errorMessage = error
            }
        )

        onDispose {
            registration.remove()
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar {

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("home") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(
                            Icons.Default.Home,
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
                            Icons.Default.Inventory2,
                            contentDescription = "Products"
                        )
                    },
                    label = { Text("Products") }
                )

                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(
                            Icons.Default.BarChart,
                            contentDescription = "Sales",
                            tint = Color(0xFF008577)
                        )
                    },
                    label = { Text("Sales") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("profile") {
                            launchSingleTop = true
                        }
                    },
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
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Sales & Bills",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {


                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                                modifier = Modifier.size(30.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "$todaySalesCount",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Today's Sales",
                                fontSize = 13.sp
                            )
                        }
                    }


                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE3E8FF)
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
                                contentDescription = "Total Orders",
                                tint = Color(0xFF3949AB),
                                modifier = Modifier.size(30.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "${sales.size}",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Text(
                                text = "Total Orders",
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))


                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 2.dp
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Today's Revenue",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "₹${"%.2f".format(todayRevenue)}",
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF008577)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))


                Text(
                    text = "Recent Bills",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(10.dp))


                when {
                    errorMessage != null -> {
                        Text(
                            text = errorMessage ?: "Unable to load bills",
                            color = Color.Red
                        )
                    }

                    sales.isEmpty() -> {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.White
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(42.dp)
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = "No bills yet",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )

                                Text(
                                    text = "Create your first bill from Home.",
                                    color = Color.Gray,
                                    fontSize = 13.sp
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Button(
                                    onClick = {
                                        navController.navigate("newBill")
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFF008577)
                                    )
                                ) {
                                    Text("Create New Bill")
                                }
                            }
                        }
                    }

                    else -> {
                        sales.forEach { sale ->

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White
                                ),
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 2.dp
                                )
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp)
                                ) {


                                    Text(
                                        text = "Bill #${sale.id.take(8)}",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    Text(
                                        text = "Total Items: ${sale.totalItems}",
                                        fontSize = 13.sp,
                                        color = Color.Gray
                                    )

                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )


                                    sale.items.forEach { item ->

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(vertical = 4.dp),
                                            horizontalArrangement =
                                                Arrangement.SpaceBetween,
                                            verticalAlignment =
                                                Alignment.CenterVertically
                                        ) {
                                            Column(
                                                modifier = Modifier.weight(1f)
                                            ) {
                                                Text(
                                                    text = item.productName,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Medium
                                                )

                                                Text(
                                                    text = "${item.quantity} × ₹${item.price}",
                                                    fontSize = 12.sp,
                                                    color = Color.Gray
                                                )
                                            }

                                            Text(
                                                text = "₹${"%.2f".format(item.totalPrice)}",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Medium
                                            )
                                        }
                                    }

                                    HorizontalDivider(
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    )


                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement =
                                            Arrangement.SpaceBetween,
                                        verticalAlignment =
                                            Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Grand Total",
                                            fontSize = 15.sp,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Text(
                                            text = "₹${"%.2f".format(sale.totalAmount)}",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF008577)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(6.dp))


                                    Text(
                                        text = java.text.SimpleDateFormat(
                                            "dd MMM yyyy, hh:mm a",
                                            java.util.Locale.getDefault()
                                        ).format(
                                            java.util.Date(sale.timestamp)
                                        ),
                                        fontSize = 12.sp,
                                        color = Color.Gray
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
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("products"){
                            launchSingleTop =true
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
                        navController.navigate("sales"){
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
                    text = FirebaseAuth.getInstance().currentUser?.displayName ?: "User",
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
                onClick  = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo(0) {
                            inclusive = true
                        }
                    }
                },
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
