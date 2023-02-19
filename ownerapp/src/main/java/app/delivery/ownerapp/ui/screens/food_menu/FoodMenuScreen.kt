package app.delivery.ownerapp.ui.screens.food_menu

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import app.delivery.ownerapp.R
import app.delivery.ownerapp.ui.components.ErrorComponent
import app.delivery.ownerapp.ui.theme.OwnerAppTheme
import app.delivery.ownerapp.ui.viewmodels.MenuScreenViewModel

@Composable
fun FoodMenuScreen(viewModel: MenuScreenViewModel = hiltViewModel()) {
    val viewState = viewModel.viewState.collectAsState()
    FoodMenuComponent(
        viewState = viewState.value,
        onChange = { foodMenuItem, available ->
            viewModel.changeAvailability(foodMenuItem, available)
        }
    )

    LaunchedEffect(key1 = Unit) {
        viewModel.fetchFoodMenu()
    }
}

@Composable
private fun FoodMenuComponent(
    viewState: MenuScreenViewModel.ViewState,
    onChange: (foodMenuItem: MenuScreenViewModel.MenuItem.Food, available: Boolean) -> Unit
) {

    val snackbarHostState = remember { SnackbarHostState() }

    ErrorComponent(snackbarHostState = snackbarHostState)

    LaunchedEffect(key1 = viewState.actionError) {
        viewState.actionError?.consume()?.let { throwable ->
            snackbarHostState.showSnackbar(message = throwable.message ?: "Error")
        }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        if (viewState.menuItemList.isNotEmpty()) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(viewState.menuItemList) { menuItem ->
                    Column {
                        if (menuItem is MenuScreenViewModel.MenuItem.Section) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.padding(dimensionResource(id = R.dimen.small_padding))
                            ) {
                                Text(
                                    text = menuItem.name,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                        } else if (menuItem is MenuScreenViewModel.MenuItem.Food) {
                            FoodMenuItemComponent(
                                foodMenuItem = menuItem,
                                onChange = { isAvailable ->
                                    onChange(menuItem, isAvailable)
                                })
                        }
                    }
                }
            }
        } else if (viewState.loading.not()) {
            Text(text = "Unable to fetch menu", style = MaterialTheme.typography.bodyLarge)
        }

        if (viewState.loading) {
            CircularProgressIndicator()
        }
    }


}

@Composable
private fun FoodMenuItemComponent(
    foodMenuItem: MenuScreenViewModel.MenuItem.Food,
    onChange: (isAvailable: Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.small_padding)),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = foodMenuItem.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.small_padding),
            )
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "₹ ${foodMenuItem.price}",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(
                start = dimensionResource(id = R.dimen.small_padding),
                end = dimensionResource(id = R.dimen.small_padding)
            )
        )
        Switch(
            checked = foodMenuItem.isAvailable,
            onCheckedChange = { value -> onChange(value) },
            modifier = Modifier.padding(
                end = dimensionResource(id = R.dimen.small_padding)
            )
        )
    }
}

@Preview
@Composable
private fun PreviewFoodMenuComponent() {
    OwnerAppTheme {
        FoodMenuComponent(
            viewState = MenuScreenViewModel.ViewState(
                menuItemList = listOf(
                    MenuScreenViewModel.MenuItem.Section("veg", "Veg", 0),
                    MenuScreenViewModel.MenuItem.Food("veg", "sambar", "Sambar", 40.0, true, 0)
                )
            ),
            onChange = { _, _ -> }
        )
    }
}

@Preview
@Composable
private fun PreviewFoodMenuItemComponent() {
    FoodMenuItemComponent(
        foodMenuItem = MenuScreenViewModel.MenuItem.Food("veg", "sambar", "Sambar", 82.0, true, 0),
        {})
}