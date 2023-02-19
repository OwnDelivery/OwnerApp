package app.delivery.ownerapp.ui.screens.home

import android.widget.Toast
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.delivery.ownerapp.R
import app.delivery.ownerapp.services.OrderAlertService
import app.delivery.ownerapp.ui.Routes
import app.delivery.ownerapp.ui.components.ErrorComponent
import app.delivery.ownerapp.ui.components.ShopCloseDialog
import app.delivery.ownerapp.ui.screens.food_menu.FoodMenuScreen
import app.delivery.ownerapp.ui.screens.orders.OrderScreen
import app.delivery.ownerapp.ui.viewmodels.HomeScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController, viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val viewState = viewModel.viewState.collectAsState()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val closeShopDialogOpen = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        context.startService(OrderAlertService.createIntent(context))
        viewModel.checkOnline()
    }

    if (closeShopDialogOpen.value) {
        ShopCloseDialog(timing = viewState.value.timing, onConfirm = { timing ->
            viewModel.updateStatus(false, timing)
            closeShopDialogOpen.value = false
        }, onDismiss = { closeShopDialogOpen.value = false })
    }

    Scaffold(
        topBar = {
            var showMenu by remember { mutableStateOf(false) }
            TopAppBar(title = {
                Text(text = "Mayas Shop App", modifier = Modifier.pointerInput(Unit) {
                    detectTapGestures(onDoubleTap = {
                        viewModel.syncMenu()
                        Toast.makeText(context, "Synced menu", Toast.LENGTH_LONG).show()
                    })
                })
            }, actions = {
                Switch(checked = viewState.value.isOnline, thumbContent = {
                    if (viewState.value.isOnline) {
                        Icon(
                            imageVector = Icons.Default.Check, contentDescription = ""
                        )
                    }
                }, onCheckedChange = { isOnline ->
                    if (isOnline.not()) {
                        closeShopDialogOpen.value = true
                    } else {
                        viewModel.updateStatus(isOnline, viewState.value.timing)
                    }
                })
                IconButton(onClick = { showMenu = !showMenu }) {
                    Icon(Icons.Default.MoreVert, "")
                }
                DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                    DropdownMenuItem(text = { Text(text = "Past Orders") },
                        onClick = { navController.navigate(Routes.PastOrders.route) })
                    DropdownMenuItem(text = { Text(text = "Logout") },
                        onClick = { viewModel.logout() })
                }
            })
        },
    ) { paddingValues ->
        Surface {
            ConstraintLayout(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {

                val homeNavController = rememberNavController()
                val (navHostRef, navigationRef) = createRefs()

                NavHost(
                    modifier = Modifier.constrainAs(navHostRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(navigationRef.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        height = Dimension.fillToConstraints
                        width = Dimension.fillToConstraints
                    }, navController = homeNavController, startDestination = "orders"
                ) {
                    composable("orders") {
                        OrderScreen()
                    }
                    composable("menu") {
                        FoodMenuScreen()
                    }
                }

                val navBackStackEntry by homeNavController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                NavigationBar(modifier = Modifier.constrainAs(navigationRef) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                }) {
                    NavigationBarItem(icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_checklist_24),
                            contentDescription = ""
                        )
                    }, selected = currentRoute == "orders", onClick = {
                        homeNavController.navigate("orders") {
                            popUpTo(0)
                        }
                    })
                    NavigationBarItem(icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_baseline_menu_book_24),
                            contentDescription = ""
                        )
                    }, selected = currentRoute == "menu", onClick = {
                        homeNavController.navigate("menu") {
                            popUpTo(0)
                        }
                    })
                }
            }
        }
    }

    LaunchedEffect(key1 = viewState.value.userLoggedOut) {
        if (viewState.value.userLoggedOut) {
            navController.navigate(Routes.Login.route) {
                popUpTo(0)
            }
        }
    }

    ErrorComponent(snackbarHostState = snackbarHostState)

    LaunchedEffect(key1 = viewState.value.actionError) {
        viewState.value.actionError?.consume()?.let { throwable ->
            snackbarHostState.showSnackbar(message = throwable.message ?: "Error")
        }
    }
}