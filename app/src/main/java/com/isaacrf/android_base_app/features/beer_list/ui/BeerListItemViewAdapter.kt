package com.isaacrf.android_base_app.features.beer_list.ui

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.isaacrf.epicbitmaprenderer.core.EpicBitmapRenderer
import com.isaacrf.android_base_app.R
import com.isaacrf.android_base_app.features.beer_list.models.Beer
import kotlinx.android.synthetic.main.beer_list_item_view.view.*

/**
 * UI Adapter for Beer List RecyclerView items
 */
class BeerListItemViewAdapter(
    private val beers: List<Beer>,
    private val onBeerListener: OnBeerListener
) : RecyclerView.Adapter<BeerListItemViewAdapter.ViewHolder>() {

    private lateinit var context: Context

    /**
     * Listener to override click and long click actions
     */
    interface OnBeerListener {
        fun onBeerClick(beer: Beer)
        fun onBeerLongClick(beer: Beer)
    }

    /**
     * Provide a reference to the views for each data item
     */
    class ViewHolder(
        view: View,
        private val onBeerListener: OnBeerListener,
        private val beers: List<Beer>
    ) :
        RecyclerView.ViewHolder(view), View.OnClickListener, View.OnLongClickListener {

        init {
            view.setOnClickListener(this)
            view.setOnLongClickListener(this)
        }

        override fun onClick(view: View) {
            onBeerListener.onBeerClick(beers[adapterPosition])
        }

        override fun onLongClick(view: View): Boolean {
            onBeerListener.onBeerLongClick(beers[adapterPosition])
            return true
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.beer_list_item_view, parent, false) as View
        return ViewHolder(view, onBeerListener, beers)
    }

    override fun getItemCount(): Int = beers.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val beer = beers[position]
        holder.itemView.txtBeerName.text = beer.name
        holder.itemView.txtBeerTagline.text = beer.tagline
        holder.itemView.txtAlcoholByVolume.text = beer.alcoholByVolume.toString()

        /*
            This library is also developed by me, and automatically handles image decoding
            and caching (https://github.com/IsaacRF/EpicBitmapRenderer)
         */
        EpicBitmapRenderer.decodeBitmapFromUrl(beer.imageUrl,
            holder.itemView.imgBeer.width,
            holder.itemView.imgBeer.height,
            { holder.itemView.imgBeer.setImageBitmap(it) },
            { Log.d("BeerListActivity", "Failed to decode image ${beer.imageUrl}") })

        if (!beer.available) {
            holder.itemView.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.colorBeerNotAvailable
                )
            )
        }
    }
}