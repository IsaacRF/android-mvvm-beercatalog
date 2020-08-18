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
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.isaacrf.android_base_app.R

import com.isaacrf.android_base_app.dummy.DummyContent
import com.isaacrf.android_base_app.features.beer_detail.ui.BeerDetailActivity
import com.isaacrf.android_base_app.features.beer_detail.ui.BeerDetailFragment
import com.isaacrf.android_base_app.features.beer_list.viewmodels.BeerListViewModel
import com.isaacrf.android_base_app.shared.Status
import dagger.hilt.android.AndroidEntryPoint

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
    private val repoListViewModel: BeerListViewModel by viewModels()

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_beer_list)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = title

        //Observe live data changes and update UI accordingly
        //TODO: Update UI
        repoListViewModel.beerList.observe(this) {
            when(it.status) {
                Status.LOADING -> {
                    Log.d("GET BEERS", "LOADING...")
                }
                Status.SUCCESS -> {
                    Log.d("GET BEERS", "SUCCESS")
                }
                Status.ERROR -> {
                    Log.d("GET BEERS", "ERROR")
                }
            }
        }

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        if (findViewById<NestedScrollView>(R.id.item_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView(findViewById(R.id.item_list))
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter =
            SimpleItemRecyclerViewAdapter(
                this,
                DummyContent.ITEMS,
                twoPane
            )
    }

    class SimpleItemRecyclerViewAdapter(private val parentActivity: BeerListActivity,
                                        private val values: List<DummyContent.DummyItem>,
                                        private val twoPane: Boolean) :
            RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as DummyContent.DummyItem
                if (twoPane) {
                    val fragment = BeerDetailFragment()
                        .apply {
                        arguments = Bundle().apply {
                            putString(BeerDetailFragment.ARG_ITEM_ID, item.id)
                        }
                    }
                    parentActivity.supportFragmentManager
                            .beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit()
                } else {
                    val intent = Intent(v.context, BeerDetailActivity::class.java).apply {
                        putExtra(BeerDetailFragment.ARG_ITEM_ID, item.id)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.beer_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item.id
            holder.contentView.text = item.content

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.findViewById(R.id.id_text)
            val contentView: TextView = view.findViewById(R.id.content)
        }
    }
}