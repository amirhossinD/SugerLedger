package com.aht.sugerledger.ui.inventory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aht.sugerledger.SugarLedgerApplication
import com.aht.sugerledger.ui.inventory.components.ProductFormDialog
import com.aht.sugerledger.ui.inventory.components.ProductListItem

/**
 * Inventory screen — warehouse product list with search, add/edit, and stock controls.
 *
 * Architecture recap for learning:
 * 1. Compose UI collects StateFlow values from ViewModel
 * 2. User actions call ViewModel functions
 * 3. ViewModel updates Repository / Room
 * 4. Room Flow pushes new data back to UI automatically
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryScreen(
    viewModel: InventoryViewModel = inventoryViewModel()
) {
    // collectAsStateWithLifecycle = safe Flow collection that respects lifecycle (no leaks)
    val products by viewModel.products.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val showProductForm by viewModel.showProductForm.collectAsStateWithLifecycle()
    val productForm by viewModel.productForm.collectAsStateWithLifecycle()
    val formError by viewModel.formError.collectAsStateWithLifecycle()
    val lowStockCount by viewModel.lowStockCount.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Inventory")
                        if (lowStockCount > 0) {
                            Text(
                                text = "$lowStockCount low-stock item(s)",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = viewModel::openAddProductForm) {
                Icon(Icons.Default.Add, contentDescription = "Add product")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SearchBar(
                query = searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                onSearch = { },
                active = false,
                onActiveChange = { },
                placeholder = { Text("Search name, barcode, category…") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) { }

            if (products.isEmpty()) {
                EmptyInventoryState(
                    isSearching = searchQuery.isNotBlank(),
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(products, key = { it.id }) { product ->
                        ProductListItem(
                            product = product,
                            onEdit = { viewModel.openEditProductForm(product) },
                            onDelete = { viewModel.deleteProduct(product) },
                            onStockIncrease = { viewModel.adjustStock(product, 1) },
                            onStockDecrease = { viewModel.adjustStock(product, -1) }
                        )
                    }
                }
            }
        }
    }

    if (showProductForm) {
        ProductFormDialog(
            form = productForm,
            errorMessage = formError,
            onDismiss = viewModel::dismissProductForm,
            onSave = viewModel::saveProduct,
            onFieldChange = viewModel::updateFormField
        )
    }
}

@Composable
private fun EmptyInventoryState(
    isSearching: Boolean,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Inventory2,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = if (isSearching) "No products match your search" else "No products yet",
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = if (isSearching) {
                    "Try a different keyword."
                } else {
                    "Tap + to add your first confectionery product."
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

/**
 * Helper that wires ViewModel + Factory using our Application-level repository.
 * Extracted so the main composable stays readable.
 */
@Composable
private fun inventoryViewModel(): InventoryViewModel {
    val app = LocalContext.current.applicationContext as SugarLedgerApplication
    return viewModel(factory = InventoryViewModelFactory(app.productRepository))
}
