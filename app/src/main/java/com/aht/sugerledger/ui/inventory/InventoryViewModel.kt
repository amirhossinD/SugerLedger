package com.aht.sugerledger.ui.inventory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aht.sugerledger.data.repository.ProductRepository
import com.aht.sugerledger.domain.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * UI state for the product form dialog (add or edit).
 * Keeping form fields in one data class makes the dialog easier to manage.
 */
data class ProductFormState(
    val id: Long = 0,
    val name: String = "",
    val barcode: String = "",
    val category: String = "Other",
    val price: String = "",
    val costPrice: String = "",
    val stockQuantity: String = "0",
    val lowStockThreshold: String = "5",
    val unit: String = "piece",
    val notes: String = ""
) {
    val isEditing: Boolean get() = id != 0L
}

/**
 * ViewModel — survives configuration changes (rotation) and holds UI logic.
 *
 * It reads/writes through the Repository and exposes StateFlow for Compose to observe.
 */
class InventoryViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _showProductForm = MutableStateFlow(false)
    val showProductForm: StateFlow<Boolean> = _showProductForm.asStateFlow()

    private val _productForm = MutableStateFlow(ProductFormState())
    val productForm: StateFlow<ProductFormState> = _productForm.asStateFlow()

    private val _formError = MutableStateFlow<String?>(null)
    val formError: StateFlow<String?> = _formError.asStateFlow()

    /**
     * combine merges two Flows: live product list + search text.
     * stateIn converts the result into a StateFlow the UI can collect safely in Compose.
     */
    val products: StateFlow<List<Product>> = combine(
        productRepository.observeProducts(),
        _searchQuery
    ) { allProducts, query ->
        if (query.isBlank()) {
            allProducts
        } else {
            allProducts.filter { product ->
                product.name.contains(query, ignoreCase = true) ||
                    product.barcode.contains(query, ignoreCase = true) ||
                    product.category.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    val lowStockCount: StateFlow<Int> = products
        .combine(_searchQuery) { list, _ -> list.count { it.isLowStock } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 0)

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun openAddProductForm() {
        _productForm.value = ProductFormState()
        _formError.value = null
        _showProductForm.value = true
    }

    fun openEditProductForm(product: Product) {
        _productForm.value = ProductFormState(
            id = product.id,
            name = product.name,
            barcode = product.barcode,
            category = product.category,
            price = product.price.toString(),
            costPrice = product.costPrice.toString(),
            stockQuantity = product.stockQuantity.toString(),
            lowStockThreshold = product.lowStockThreshold.toString(),
            unit = product.unit,
            notes = product.notes
        )
        _formError.value = null
        _showProductForm.value = true
    }

    fun dismissProductForm() {
        _showProductForm.value = false
        _formError.value = null
    }

    fun updateFormField(update: ProductFormState.() -> ProductFormState) {
        _productForm.value = _productForm.value.update()
    }

    fun saveProduct() {
        val form = _productForm.value
        val parsed = parseForm(form) ?: return

        viewModelScope.launch {
            productRepository.saveProduct(parsed)
            dismissProductForm()
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            productRepository.deleteProduct(product)
        }
    }

    fun adjustStock(product: Product, delta: Int) {
        if (product.stockQuantity + delta < 0) return
        viewModelScope.launch {
            productRepository.adjustStock(product.id, delta)
        }
    }

    /** Validates form strings and converts them into a typed Product domain object. */
    private fun parseForm(form: ProductFormState): Product? {
        if (form.name.isBlank()) {
            _formError.value = "Product name is required."
            return null
        }

        val price = form.price.toDoubleOrNull()
        if (price == null || price < 0) {
            _formError.value = "Enter a valid selling price."
            return null
        }

        val costPrice = form.costPrice.toDoubleOrNull() ?: 0.0
        val stock = form.stockQuantity.toIntOrNull() ?: 0
        val threshold = form.lowStockThreshold.toIntOrNull() ?: 5

        if (stock < 0 || threshold < 0) {
            _formError.value = "Stock values cannot be negative."
            return null
        }

        _formError.value = null
        return Product(
            id = form.id,
            name = form.name,
            barcode = form.barcode,
            category = form.category,
            price = price,
            costPrice = costPrice,
            stockQuantity = stock,
            lowStockThreshold = threshold,
            unit = form.unit,
            notes = form.notes
        )
    }
}

/**
 * ViewModelProvider.Factory — tells Android how to create InventoryViewModel
 * with our ProductRepository dependency (no Hilt/Dagger yet).
 */
class InventoryViewModelFactory(
    private val repository: ProductRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            return InventoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}
