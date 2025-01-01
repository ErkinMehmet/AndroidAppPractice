package com.np.testroom.daos

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.np.testroom.data.Product

@Dao
interface ProductDao {
    @Insert
    suspend fun insert(product: Product)

    @Query("SELECT * FROM products")
    suspend fun getAllProducts(): List<Product>
}