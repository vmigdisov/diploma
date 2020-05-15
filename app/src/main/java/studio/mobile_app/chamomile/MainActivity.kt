package studio.mobile_app.chamomile

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

import java.util.HashMap

class MainActivity : AppCompatActivity() {

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
//        val childUpdates = HashMap<String, Any>()
//        dbHandler.getProducts(14).forEach() { product ->
//            fdb.child("products").push().key.let {
//                product.remoteID = it!!
//                dbHandler.saveProduct(product)
//                childUpdates["/products/$it"] = product.toMap()
//            }
//        }
//        fdb.updateChildren(childUpdates)

        val childEventListener = object: ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("FDB", "onChildAdded:" + dataSnapshot.key!!)
                //val comment = dataSnapshot.getValue<Product>()
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("FDB", "onChildChanged: ${dataSnapshot.key}")
                dataSnapshot.getValue(Product::class.java).let {
                    dbHandler.saveProduct(it!!)
                }
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d("FDB", "onChildRemoved:" + dataSnapshot.key!!)
                val commentKey = dataSnapshot.key
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.w("FDB", "Products:onCancelled", p0.toException())
                Toast.makeText(applicationContext, "Failed to load products.", Toast.LENGTH_SHORT).show()
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d("FDB", "onChildMoved:" + dataSnapshot.key!!)
                //val movedComment = dataSnapshot.getValue<Comment>()
                //val commentKey = dataSnapshot.key

            }
        }
        fdb.child("products").addChildEventListener(childEventListener)
    }
}
