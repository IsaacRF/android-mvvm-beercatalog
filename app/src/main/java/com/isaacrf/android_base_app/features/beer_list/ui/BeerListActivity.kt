package com.isaacrf.android_base_app.features.beer_list.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.widget.NestedScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.isaacrf.android_base_app.R

import com.isaacrf.android_base_app.features.beer_detail.ui.BeerDetailActivity
import com.isaacrf.android_base_app.features.beer_detail.ui.BeerDetailFragment
import com.isaacrf.android_base_app.features.beer_list.models.Beer
import com.isaacrf.android_base_app.features.beer_list.viewmodels.BeerListViewModel
import com.isaacrf.android_base_app.shared.NetworkResource
import com.isaacrf.android_base_app.shared.Status
import com.isaacrf.epicbitmaprenderer.core.EpicBitmapRenderer
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.beer_list.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [BeerDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
@AndroidEntryPoint
class BeerListActivity : AppCompatActivity() {

    /**
     * ViewModel controls business logic and data representation. A saved state factory is created
     * to provide state retain across activity life cycle
     */
    private val beerListViewModel: BeerListViewModel by viewModels()

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        //Observe live data changes and update UI accordingly
        beerListViewModel.getBeers().observe(this) {
            when (it.status) {
                Status.LOADING -> {
                    Log.d("GET BEERS", "LOADING...")
                    txtError.visibility = View.GONE
                    pbRepoListLoading.visibility = View.VISIBLE
                }
                Status.SUCCESS -> {
                    Log.d("GET BEERS", "SUCCESS")
                    setupRecyclerView(rvBeerList, it.data!!)
                    pbRepoListLoading.visibility = View.GONE
                }
                Status.ERROR -> {
                    Log.d("GET BEERS", "ERROR")
                    pbRepoListLoading.visibility = View.GONE
                    txtError.visibility = View.VISIBLE
                    txtError.text = it.message
                }
            }
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            beerListViewModel.changeAvailability(1)
        }

        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, items: List<Beer>) {
        layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            rvBeerList.context,
            layoutManager.orientation
        )
        rvBeerList.addItemDecoration(dividerItemDecoration)
        rvBeerList.layoutManager = layoutManager

        recyclerView.adapter =
            BeerListItemViewAdapter(
                this,
                items,
                twoPane
            )
    }


}