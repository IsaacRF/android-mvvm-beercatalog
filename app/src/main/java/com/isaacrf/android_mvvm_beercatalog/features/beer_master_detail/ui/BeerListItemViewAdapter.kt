package com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.isaacrf.epicbitmaprenderer.core.EpicBitmapRenderer
import com.isaacrf.android_mvvm_beercatalog.R
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.models.Beer
import com.isaacrf.android_mvvm_beercatalog.shared.ui.MainActivity

/**
 * UI Adapter for Beer List RecyclerView items
 */
class BeerListItemViewAdapter(
    private val parentActivity: MainActivity,
    private val values: List<Beer>,
    private val twoPane: Boolean
) :
    RecyclerView.Adapter<BeerListItemViewAdapter.ViewHolder>() {

    private val onClickListener: View.OnClickListener

    init {
        onClickListener = View.OnClickListener { v ->
            val item = v.tag as Beer
            val args = Bundle().apply {
                putString(BeerDetailFragment.ARG_ITEM_ID, item.id.toString())
            }

            //If view is in TwoPane mode, add the fragment to view. Navigate to fragment otherwise
            if (twoPane) {
                val fragment = BeerDetailFragment()
                    .apply {
                        arguments = args
                    }
                parentActivity.supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.beer_detail_container, fragment)
                    .commit()
            } else {
                findNavController(
                    parentActivity,
                    R.id.frNavHost
                ).navigate(R.id.action_beerListFragment_to_beerDetailFragment, args)
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
            { Log.d("MainActivity", "Failed to decode image ${item.imageUrl}") })

        holder.itemView.setBackgroundColor(
            ContextCompat.getColor(
                this.parentActivity,
                if (item.available) R.color.backgroundBeerAvailable
                else R.color.backgroundBeerNotAvailable
            )
        )

        with(holder.itemView) {
            tag = item
            setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount() = values.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtBeerName: TextView = view.findViewById(R.id.text_beerlist_beername)
        val txtBeerTagline: TextView = view.findViewById(R.id.text_beerlist_tagline)
        val txtAlcoholByVolume: TextView = view.findViewById(R.id.text_beerlist_alcoholbyvolume)
        val imgBeer: ImageView = view.findViewById(R.id.image_beer)
    }
}