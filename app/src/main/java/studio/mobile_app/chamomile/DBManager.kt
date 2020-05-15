package studio.mobile_app.chamomile

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import java.util.*

class DBManager(context: Context, name: String? = "chamomile.db", factory: SQLiteDatabase.CursorFactory? = null, version: Int = 1): SQLiteOpenHelper(context, name, factory, version) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("create table PRODUCT_GROUPS(ID integer primary key, NAME text)")
        db.execSQL("create table PRODUCT_CATEGORIES(ID integer primary key, NAME text, PRODUCT_GROUP integer)")
        db.execSQL("create table PRODUCTS(ID text primary key, NAME text, CATEGORY integer, PRICE numeric, REMOTE_ID text)")
        db.execSQL("create table FAVOURITES(ID text primary key)")
        db.execSQL("create table BASKET(ID text primary key, QUANTITY integer)")
        loadTestData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop table if exists PRODUCT_CATEGORIES")
        db.execSQL("drop table if exists PRODUCT_GROUPS")
        db.execSQL("drop table if exists PRODUCTS")
        db.execSQL("drop table if exists ORDERS")
        db.execSQL("drop table if exists ORDER_ITEMS")
        onCreate(db)
    }

    fun addProductGroup(productGroup: ProductGroup) {
        val values = ContentValues()
        values.put("ID", productGroup.id)
        values.put("NAME", productGroup.name)
        val db = this.writableDatabase
        db.insert("PRODUCT_GROUPS", null, values)
        db.close()
    }

    fun addProductCategory(productCategory: ProductCategory) {
        val values = ContentValues()
        values.put("ID", productCategory.id)
        values.put("NAME", productCategory.name)
        values.put("PRODUCT_GROUP", productCategory.productGroup)
        val db = this.writableDatabase
        db.insert("PRODUCT_CATEGORIES", null, values)
        db.close()
    }

    fun saveProduct(product: Product) {
        val values = ContentValues()
        values.put("ID", product.id)
        values.put("NAME", product.name)
        values.put("CATEGORY", product.category)
        values.put("PRICE", product.price)
        values.put("REMOTE_ID", product.remoteID)
        val db = this.writableDatabase
        val rows = db.update("PRODUCTS", values, "ID = ?", arrayOf(product.id))
        if (rows == 0) {
            db.insert("PRODUCTS", null, values)
        }
        db.close()
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

    fun getProducts(productCategory: Int): ArrayList<Product> {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT ID, NAME, CATEGORY, PRICE, REMOTE_ID FROM PRODUCTS WHERE CATEGORY = ${productCategory}", null)
        var products: ArrayList<Product> = arrayListOf<Product>()
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val id: String = cursor.getString(0)
                    val name: String = cursor.getString(1)
                    val categoty: Int = cursor.getInt(2)
                    val price: Double = cursor.getDouble(3)
                    val remoteID: String = cursor.getString(4)
                    products.add(Product(id, name, categoty, price, remoteID))
                } while (cursor.moveToNext())
            }
        }
        db.close()
        return products
    }

    fun loadTestData(db: SQLiteDatabase) {
        db.execSQL("insert into PRODUCT_GROUPS(ID, NAME) values(1, 'Женщины')")
            db.execSQL("insert into PRODUCT_CATEGORIES(ID, NAME, PRODUCT_GROUP) values(11, 'Джинсы', 1)")
                db.execSQL("insert into PRODUCTS(ID, NAME, CATEGORY, PRICE, REMOTE_ID) values('111', 'Джинсы Pull&Bear', 11, 1799.0, '')")
                db.execSQL("insert into PRODUCTS(ID, NAME, CATEGORY, PRICE, REMOTE_ID) values('112', 'Джинсы Bershka', 11, 1799.0, '')")
                db.execSQL("insert into PRODUCTS(ID, NAME, CATEGORY, PRICE, REMOTE_ID) values('113', 'Джинсы Colins', 11, 1912.0, '')")
                db.execSQL("insert into PRODUCTS(ID, NAME, CATEGORY, PRICE, REMOTE_ID) values('114', 'Джинсы Stradivarius', 11, 1799.0, '')")
                db.execSQL("insert into PRODUCTS(ID, NAME, CATEGORY, PRICE, REMOTE_ID) values('115', 'Джинсы Dorothy Perkins', 11, 3299.0, '')")
                db.execSQL("insert into PRODUCTS(ID, NAME, CATEGORY, PRICE, REMOTE_ID) values('116', 'Джинсы Mango', 11, 1499.0, '')")
            db.execSQL("insert into PRODUCT_CATEGORIES(ID, NAME, PRODUCT_GROUP) values(12, 'Блузы', 1)")
                db.execSQL("insert into PRODUCTS(ID, NAME, CATEGORY, PRICE, REMOTE_ID) values('121', 'Блуза Hugo', 12, 9600.0, '')")
                db.execSQL("insert into PRODUCTS(ID, NAME, CATEGORY, PRICE, REMOTE_ID) values('122', 'Блуза Zarina', 12, 1551.0, '')")
                db.execSQL("insert into PRODUCTS(ID, NAME, CATEGORY, PRICE, REMOTE_ID) values('123', 'Блуза oodji', 12, 799.0, '')")
                db.execSQL("insert into PRODUCTS(ID, NAME, CATEGORY, PRICE, REMOTE_ID) values('124', 'Блуза Violeta by Mango', 12, 1869.0, '')")
            db.execSQL("insert into PRODUCT_CATEGORIES(ID, NAME, PRODUCT_GROUP) values(13, 'Колготки', 1)")
                db.execSQL("insert into PRODUCTS(ID, NAME, CATEGORY, PRICE, REMOTE_ID) values('131', 'Колготки Calzedonia', 13, 399.0, '')")
                db.execSQL("insert into PRODUCTS(ID, NAME, CATEGORY, PRICE, REMOTE_ID) values('132', 'Колготки oodji', 13, 599.0, '')")
                db.execSQL("insert into PRODUCTS(ID, NAME, CATEGORY, PRICE, REMOTE_ID) values('133', 'Колготки Teatro', 13, 439.0, '')")
            db.execSQL("insert into PRODUCT_CATEGORIES(ID, NAME, PRODUCT_GROUP) values(14, 'Джемперы', 1)")
                db.execSQL("insert into PRODUCTS(ID, NAME, CATEGORY, PRICE, REMOTE_ID) values('141', 'Джемпер Love Republic', 14, 1279.0, '')")
                db.execSQL("insert into PRODUCTS(ID, NAME, CATEGORY, PRICE, REMOTE_ID) values('142', 'Джемпер Concept Club', 14, 799.0, '')")
                db.execSQL("insert into PRODUCTS(ID, NAME, CATEGORY, PRICE, REMOTE_ID) values('143', 'Джемпер Zarina', 14, 766.0, '')")
            db.execSQL("insert into PRODUCT_CATEGORIES(ID, NAME, PRODUCT_GROUP) values(15, 'Нижнее белье', 1)")
                db.execSQL("insert into PRODUCTS(ID, NAME, CATEGORY, PRICE, REMOTE_ID) values('151', 'Нижнее белье 1', 15, 99.99, '')")
            db.execSQL("insert into PRODUCT_CATEGORIES(ID, NAME, PRODUCT_GROUP) values(16, 'Платье', 1)")
                db.execSQL("insert into PRODUCTS(ID, NAME, CATEGORY, PRICE, REMOTE_ID) values('161', 'Платье 1', 16, 99.99, '')")
        db.execSQL("insert into PRODUCT_GROUPS(ID, NAME) values(2, 'Мужчины')")
            db.execSQL("insert into PRODUCT_CATEGORIES(ID, NAME, PRODUCT_GROUP) values(21, 'Джинсы', 2)")
            db.execSQL("insert into PRODUCT_CATEGORIES(ID, NAME, PRODUCT_GROUP) values(22, 'Брюки', 2)")
            db.execSQL("insert into PRODUCT_CATEGORIES(ID, NAME, PRODUCT_GROUP) values(23, 'Костюмы', 2)")
        db.execSQL("insert into PRODUCT_GROUPS(ID, NAME) values(3, 'Дети')")
            db.execSQL("insert into PRODUCT_CATEGORIES(ID, NAME, PRODUCT_GROUP) values(31, 'Шорты', 3)")
            db.execSQL("insert into PRODUCT_CATEGORIES(ID, NAME, PRODUCT_GROUP) values(32, 'Гольфы', 3)")
            db.execSQL("insert into PRODUCT_CATEGORIES(ID, NAME, PRODUCT_GROUP) values(33, 'Сандали', 3)")
    }
}

@IgnoreExtraProperties
data class Product(
    var id: String = "",
    var name: String = "",
    var category: Int = -1,
    var price: Double = 0.0,
    var remoteID: String = "") {

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "name" to name,
            "category" to category,
            "price" to price
        )
    }

}

class ProductCategory(id: Int, name: String, productGroup: Int) {

    var id: Int
    var name: String
    var productGroup: Int

    init {
        this.id = id
        this.name = name
        this.productGroup = productGroup
    }

}

class ProductGroup(id: Int, name: String) {

    var id: Int = 0
    var name: String = ""

    init {
        this.id = id
        this.name = name
    }

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