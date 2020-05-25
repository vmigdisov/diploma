package studio.mobile_app.chamomile

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.fragment_profile.*
import studio.mobile_app.chamomile.ui.catalog.ProductGroupsPagerAdapter
import studio.mobile_app.chamomile.ui.products.ProductFragment
import studio.mobile_app.chamomile.ui.products.ProductsFragment

class MainActivity : AppCompatActivity() {

    override fun onBackPressed() {
        supportFragmentManager.let { fm ->
            for (fragment in fm.fragments) {
                for (fr in fragment.childFragmentManager.fragments) {
                    for (subfr in fr.childFragmentManager.fragments) {
                        if (subfr.isVisible && !subfr.childFragmentManager.fragments.isEmpty()) {
                            for (frag in subfr.childFragmentManager.fragments) {
                                if (frag is ProductFragment || frag is ProductsFragment) {
                                    subfr.childFragmentManager.popBackStack()
                                    return
                                }
                            }

                        }
                    }
                }
            }
        }
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        /*val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_catalog, R.id.navigation_basket, R.id.navigation_favourites, R.id.navigation_profile))
        setupActionBarWithNavController(navController, appBarConfiguration)*/
        navView.setupWithNavController(navController)
        val dbHandler = DBManager(this)
        var mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        mainViewModel.productGroups = dbHandler.getProductGroups()
        mainViewModel.productCategories = dbHandler.getProductCategories()
        val fdb = Firebase.database.reference
        val storageRef = Firebase.storage.reference
//        val childUpdates = HashMap<String, Any>()
//        dbHandler.getProducts(14).forEach() { product ->
//            fdb.child("products").push().key.let {
//                product.remoteID = it!!
//                dbHandler.saveProduct(product)
//                childUpdates["/products/$it"] = product.toMap()
//            }
//        }
//        for (i in 11..14) {
//            dbHandler.getProducts(i).forEach() { product ->
//                childUpdates["/products/${product.id}"] = product.toMap()
//            }
//        }
//        fdb.updateChildren(childUpdates)

        val productsEventListener = object: ChildEventListener {

            fun updateProduct(dataSnapshot: DataSnapshot) {
                dataSnapshot.getValue(Product::class.java).let {
                    if (it != null && dataSnapshot.key != null) {
                        val product = it
                        product.id = dataSnapshot.key!!
                        dbHandler.saveProduct(product)
                        storageRef.child("products/icon${dataSnapshot.key}.png").getBytes(Long.MAX_VALUE).addOnSuccessListener { iconByteArray ->
                            product.icon = iconByteArray
                            dbHandler.saveProduct(product)
                            storageRef.child("products/image${dataSnapshot.key}.png").getBytes(Long.MAX_VALUE).addOnSuccessListener { imageByteArray ->
                                product.image = imageByteArray
                                dbHandler.saveProduct(product)
                            }.addOnFailureListener { imageError -> Log.d("Fetch image error", imageError.localizedMessage ?: "") }
                        }.addOnFailureListener { iconError -> Log.d("Fetch icon error", iconError.localizedMessage ?: "") }
                    }
                }
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) { updateProduct(dataSnapshot) }
            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?)  { updateProduct(dataSnapshot) }
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}
        }

        val productCategoriesEventListener = object: ChildEventListener {

            fun updateProductCategory(dataSnapshot: DataSnapshot) {
                dataSnapshot.getValue(ProductCategory::class.java).let {
                    if (it != null && dataSnapshot.key != null) {
                        val productCategory = it
                        productCategory.id = dataSnapshot.key!!.toInt()
                        dbHandler.saveProductCategory(productCategory)
                        mainViewModel.productCategories = dbHandler.getProductCategories()
                    }
                }
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) { updateProductCategory(dataSnapshot) }
            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?)  { updateProductCategory(dataSnapshot) }
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}
        }

        val productGroupsEventListener = object: ChildEventListener {

            fun updateProductGroup(dataSnapshot: DataSnapshot) {
                dataSnapshot.getValue(ProductGroup::class.java).let {
                    if (it != null && dataSnapshot.key != null) {
                        val productGroup = it
                        productGroup.id = dataSnapshot.key!!.toInt()
                        dbHandler.saveProductGroup(productGroup)
                        mainViewModel.productGroups = dbHandler.getProductGroups()
                    }
                }
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) { updateProductGroup(dataSnapshot) }
            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?)  { updateProductGroup(dataSnapshot) }
            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}
            override fun onCancelled(p0: DatabaseError) {}
            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {}
        }
        fdb.child("product_groups").addChildEventListener(productGroupsEventListener)
        fdb.child("product_categories").addChildEventListener(productCategoriesEventListener)
        fdb.child("products").addChildEventListener(productsEventListener)
    }
}
