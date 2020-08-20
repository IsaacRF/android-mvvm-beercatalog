package com.isaacrf.android_base_app.features.beer_list.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.isaacrf.epicbitmaprenderer.core.EpicBitmapRenderer
import com.isaacrf.android_base_app.R
import com.isaacrf.android_base_app.features.beer_detail.ui.BeerDetailActivity
import com.isaacrf.android_base_app.features.beer_detail.ui.BeerDetailFragment
import com.isaacrf.android_base_app.features.beer_list.models.Beer
import kotlinx.android.synthetic.main.beer_list_item_view.view.*

/**
 * UI Adapter for Beer List RecyclerView items
 */
class BeerListItemViewAdapter(private val parentActivity: BeerListActivity,
                                    private val values: List<Beer>,
                                    private val twoPane: Boolean) :
    RecyclerView.Adapter<BeerListItemViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Beer
            if (twoPane) {
                val fragment = BeerDetailFragment()
                    .apply {
                        arguments = Bundle().apply {
                            putString(BeerDetailFragment.ARG_ITEM_ID, item.id.toString())
                        }
                    }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.item_detail_container, fragment)
                    .commit()
            } else {
                val intent = Intent(v.context, BeerDetailActivity::class.java).apply {
                    putExtra(BeerDetailFragment.ARG_ITEM_ID, item.id.toString())
                }
                v.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.beer_list_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.txtBeerName.text = item.name
        holder.txtBeerTagline.text = item.tagline
        holder.txtAlcoholByVolume.text = item.alcoholByVolume.toString()

        /*
            This library is also developed by me, and automatically handles image decoding
            and caching (https://github.com/IsaacRF/EpicBitmapRenderer)
         */
        EpicBitmapRenderer.decodeBitmapFromUrl(item.imageUrl,
            holder.imgBeer.width,
            holder.imgBeer.height,
            { holder.imgBeer.setImageBitmap(it) },
            { Log.d("BeerListActivity", "Failed to decode image ${item.imageUrl}") })

        if (!item.available) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    this.parentActivity,
                    R.color.colorBeerNotAvailable
                )
            )
        }

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtBeerName: TextView = view.findViewById(R.id.txtBeerName)
        val txtBeerTagline: TextView = view.findViewById(R.id.txtBeerTagline)
        val txtAlcoholByVolume: TextView = view.findViewById(R.id.txtAlcoholByVolume)
        val imgBeer: ImageView = view.findViewById(R.id.imgBeer)
    }
}