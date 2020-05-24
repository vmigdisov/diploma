package studio.mobile_app.chamomile

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.media.Image
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import studio.mobile_app.chamomile.ui.products.ProductFragment
import java.util.*

class DBManager(context: Context, name: String? = "chamomile.db", factory: SQLiteDatabase.CursorFactory? = null, version: Int = 1): SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table PRODUCT_GROUPS(ID integer primary key, NAME text)")
        db.execSQL("create table PRODUCT_CATEGORIES(ID integer primary key, NAME text, PRODUCT_GROUP integer)")
        db.execSQL("create table PRODUCTS(ID text primary key, NAME text, CATEGORY integer, PRICE numeric, ICON blob, IMAGE blob)")
        db.execSQL("create table FAVOURITES(ID text primary key)")
        db.execSQL("create table BASKET(ID text primary key, QUANTITY integer)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists PRODUCT_CATEGORIES")
        db.execSQL("drop table if exists PRODUCT_GROUPS")
        db.execSQL("drop table if exists PRODUCTS")
        db.execSQL("drop table if exists FAVOURITES")
        db.execSQL("drop table if exists BASKET")
        db.execSQL("drop table if exists ORDERS")
        db.execSQL("drop table if exists ORDER_ITEMS")
        onCreate(db)
    }

    fun saveProductGroup(productGroup: ProductGroup) {
        val values = ContentValues()
        values.put("ID", productGroup.id)
        values.put("NAME", productGroup.name)
        val db = this.writableDatabase
        val rows = db.update("PRODUCT_GROUPS", values, "ID = ${productGroup.id}", null)
        if (rows == 0) {
            db.insert("PRODUCT_GROUPS", null, values)
        }
        db.close()
    }

    fun saveProductCategory(productCategory: ProductCategory) {
        val values = ContentValues()
        values.put("ID", productCategory.id)
        values.put("NAME", productCategory.name)
        values.put("PRODUCT_GROUP", productCategory.productGroup)
        val db = this.writableDatabase
        val rows = db.update("PRODUCT_CATEGORIES", values, "ID = ${productCategory.id}", null)
        if (rows == 0) {
            db.insert("PRODUCT_CATEGORIES", null, values)
        }
        db.close()
    }

    fun saveProduct(product: Product) {
        val values = ContentValues()
        values.put("ID", product.id)
        values.put("NAME", product.name)
        values.put("CATEGORY", product.category)
        values.put("PRICE", product.price)
        values.put("ICON", product.icon)
        values.put("IMAGE", product.image)
        val db = this.writableDatabase
        val rows = db.update("PRODUCTS", values, "ID = ?", arrayOf(product.id))
        if (rows == 0) {
            db.insert("PRODUCTS", null, values)
        }
        db.close()
    }

    fun getProducts(productCategory: Int): ArrayList<Product> {
        val db = this.writableDatabase
        var query: String
            when (productCategory) {
            ProductCategory.FAVOURITES -> query = "SELECT PRODUCTS.ID, PRODUCTS.NAME, PRODUCTS.CATEGORY, PRODUCTS.PRICE, PRODUCTS.ICON, BASKET.QUANTITY FROM FAVOURITES LEFT JOIN PRODUCTS ON FAVOURITES.ID = PRODUCTS.ID LEFT JOIN BASKET ON FAVOURITES.ID = BASKET.ID"
            ProductCategory.BASKET -> query = "SELECT PRODUCTS.ID, PRODUCTS.NAME, PRODUCTS.CATEGORY, PRODUCTS.PRICE, PRODUCTS.ICON, BASKET.QUANTITY FROM BASKET LEFT JOIN PRODUCTS ON BASKET.ID = PRODUCTS.ID"
            else -> query = "SELECT PRODUCTS.ID, PRODUCTS.NAME, PRODUCTS.CATEGORY, PRODUCTS.PRICE, PRODUCTS.ICON, BASKET.QUANTITY FROM PRODUCTS LEFT JOIN BASKET ON PRODUCTS.ID = BASKET.ID WHERE PRODUCTS.CATEGORY = ${productCategory}"
        }
        val cursor = db.rawQuery(query, null)
        var products: ArrayList<Product> = arrayListOf<Product>()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id: String = cursor.getString(0)
                    val name: String = cursor.getString(1)
                    val categoty: Int = cursor.getInt(2)
                    val price: Double = cursor.getDouble(3)
                    val icon: ByteArray = cursor.getBlob(4)
                    val product = Product(id, name, categoty, price, icon)
                    product.basketQuantity = cursor.getInt(5)
                    products.add(product)
                } while (cursor.moveToNext())
            }
        }
        db.close()
        return products
    }

    fun getProduct(productId: String): Product? {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT PRODUCTS.NAME, PRODUCTS.CATEGORY, PRODUCTS.PRICE, PRODUCTS.ICON, PRODUCTS.IMAGE, BASKET.QUANTITY FROM PRODUCTS LEFT JOIN BASKET ON PRODUCTS.ID = BASKET.ID WHERE PRODUCTS.ID = '${productId}'", null)
        var product: Product? = null
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                product = Product(id = productId)
                product.name = cursor.getString(0)
                product.category = cursor.getInt(1)
                product.price = cursor.getDouble(2)
                product.icon = cursor.getBlob(3)
                product.image = cursor.getBlob(4)
                product.basketQuantity = cursor.getInt(5)
            }
        }
        db.close()
        return product
    }

    fun deleteProduct(product: Product) {
        val db = this.writableDatabase
        db.delete("PRODUCTS", "ID = ?", arrayOf(product.id))
        db.close()
    }

    fun getProductGroups(): ArrayList<ProductGroup> {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT ID, NAME FROM PRODUCT_GROUPS order by ID ASC", null)
        var productGroups: ArrayList<ProductGroup> = arrayListOf<ProductGroup>()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id: Int = cursor.getInt(0)
                    val name: String = cursor.getString(1)
                    productGroups.add(ProductGroup(id, name))
                } while (cursor.moveToNext())
            }
        }
        db.close()
        return productGroups
    }

    fun getProductCategories(): ArrayList<ProductCategory> {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT ID, NAME, PRODUCT_GROUP FROM PRODUCT_CATEGORIES order by ID ASC", null)
        var categories: ArrayList<ProductCategory> = arrayListOf<ProductCategory>()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id: Int = cursor.getInt(0)
                    val name: String = cursor.getString(1)
                    val productGroup: Int = cursor.getInt(2)
                    categories.add(ProductCategory(id, name, productGroup))
                } while (cursor.moveToNext())
            }
        }
        db.close()
        return categories
    }

    fun deleteFavourite(product: Product) {
        val db = this.writableDatabase
        db.delete("FAVOURITES", "ID = ?", arrayOf(product.id))
        db.close()
    }

    fun saveFavourite(product: Product) {
        val values = ContentValues()
        values.put("ID", product.id)
        val db = this.writableDatabase
        val rows = db.update("FAVOURITES", values, "ID = ?", arrayOf(product.id))
        if (rows == 0) {
            db.insert("FAVOURITES", null, values)
        }
        db.close()
    }

    fun saveBasket(product: Product) {
        val values = ContentValues()
        values.put("ID", product.id)
        values.put("QUANTITY", product.basketQuantity)
        val db = this.writableDatabase
        if (product.basketQuantity < 1) {
            db.delete("BASKET", "ID = ?", arrayOf(product.id))
        } else {
            val rows = db.update("BASKET", values, "ID = ?", arrayOf(product.id))
            if (rows == 0) {
                db.insert("BASKET", null, values)
            }
        }
        db.close()
    }
}

@IgnoreExtraProperties
data class Product(
    var id: String = "",
    var name: String = "",
    var category: Int = -1,
    var price: Double = 0.0,
    var icon: ByteArray = ByteArray(0),
    var image: ByteArray = ByteArray(0)) {

    var basketQuantity = 0
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "name" to name,
            "category" to category,
            "price" to price
        )
    }

}

@IgnoreExtraProperties
data class ProductCategory(
    var id: Int = -1,
    var name: String = "",
    var productGroup: Int = -1) {
    companion object {
        const val FAVOURITES = -100
        const val BASKET = -200
    }
}

@IgnoreExtraProperties
data class ProductGroup(
    var id: Int = -1,
    var name: String = "") {
}

class Order {

    var id: Int = 0
    var orderDate: Date = Date()
    var deliveryDate: Date = Date()
    var addressId: Int = 0

}

class OrderItem {

    var id: Int = 0
    var orderId: Int = 0
    var ProductId: Int = 0

}