package studio.mobile_app.chamomile

import androidx.lifecycle.ViewModel
import java.util.ArrayList

class MainViewModel: ViewModel() {
    var productGroups: ArrayList<ProductGroup> = arrayListOf<ProductGroup>()
    var productCategories: ArrayList<ProductCategory> = arrayListOf<ProductCategory>()
}