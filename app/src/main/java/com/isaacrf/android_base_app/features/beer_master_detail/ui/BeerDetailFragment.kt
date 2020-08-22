package com.isaacrf.android_base_app.features.beer_master_detail.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.isaacrf.android_base_app.R
import com.isaacrf.android_base_app.features.beer_master_detail.models.Beer
import com.isaacrf.android_base_app.features.beer_master_detail.viewmodels.BeerDetailViewModel
import com.isaacrf.android_base_app.features.beer_master_detail.viewmodels.BeerListViewModel
import com.isaacrf.android_base_app.shared.ui.MainActivity
import com.isaacrf.epicbitmaprenderer.core.EpicBitmapRenderer
import kotlinx.android.synthetic.main.beer_detail.*

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in the [MainActivity]
 * in two-pane mode (on tablets) or as [BeerDetailFragment] by itself
 * via navigation on handsets.
 */
class BeerDetailFragment : Fragment() {

    /**
     * ViewModel controls business logic and data representation. A saved state factory is created
     * to provide state retain across activity life cycle
     */
    private val beerListViewModel: BeerListViewModel by activityViewModels()
    private val beerDetailViewModel: BeerDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                val beerId = it.getString(ARG_ITEM_ID)?.toInt()

                beerDetailViewModel.getBeer().observe(this) { beer ->
                    updateUI(beer)
                }

                beerDetailViewModel.setBeer(beerListViewModel.getBeer(beerId))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.beer_detail, container, false)

        //TODO: Set change availability action
        rootView.findViewById<FloatingActionButton>(R.id.btnChangeAvailability)
            .setOnClickListener { view ->
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
            }

        return rootView
    }

    private fun updateUI(beer: Beer) {
        activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title = beer.name

        //TODO: Update UI
        EpicBitmapRenderer.decodeBitmapFromUrl(beer.imageUrl,
            image_beer.width,
            image_beer.height,
            { image_beer.setImageBitmap(it) },
            { Log.d("BeerDetailFragment", "Failed to decode image ${beer.imageUrl}") })

        text_beerdetail_description.text = beer.description
        text_beerdetail_alcoholbyvolume.text = beer.alcoholByVolume.toString()
        text_beerdetail_bitterness.text = beer.bitterness.toString()
        text_beerdetail_foodpairing.text = beer.foodPairing.joinToString(separator = "\n")
        text_beerdetail_availability.text =
            if (beer.available) getString(R.string.beer_available)
            else getString(R.string.beer_not_available)
        text_beerdetail_availability.setTextColor(
            if(beer.available) ContextCompat.getColor(requireContext(), R.color.colorTextBeerAvailable)
            else ContextCompat.getColor(requireContext(), R.color.colorTextBeerNotAvailable)
        )
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }
}