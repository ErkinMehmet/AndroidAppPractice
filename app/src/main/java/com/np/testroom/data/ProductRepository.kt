package com.np.testroom.data

import com.np.testroom.dao.ProductDao
import com.np.testroom.data.Product

class ProductRepository(private val productDao: ProductDao) {

    // Insert a product into the database
    suspend fun insertProduct(product: Product) {
        productDao.insert(product)
    }

    // Get all products from the database
    suspend fun getAllProducts(): List<Product> {
        return productDao.getAllProducts()
    }
}