package com.np.testroom.data
import android.content.Context
import com.np.testroom.daos.ProductDao

class ProductRepository(private val context: Context) {
    private val db = AppDatabase.getDatabase(context)


}
