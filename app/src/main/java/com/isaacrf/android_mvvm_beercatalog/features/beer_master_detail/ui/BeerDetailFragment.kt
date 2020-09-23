package com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.CollapsingToolbarLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.isaacrf.android_mvvm_beercatalog.R
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.models.Beer
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.viewmodels.BeerDetailViewModel
import com.isaacrf.android_mvvm_beercatalog.features.beer_master_detail.viewmodels.BeerListViewModel
import com.isaacrf.android_mvvm_beercatalog.shared.ui.MainActivity
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

                beerDetailViewModel.beer.observe(this) { beer ->
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

        rootView.findViewById<FloatingActionButton>(R.id.button_beerdetail_changeavailability)
            .setOnClickListener {
                beerListViewModel
                    .changeAvailability(beerDetailViewModel.beer.value?.id!!)
                    .observe(viewLifecycleOwner) {beer ->
                        beerDetailViewModel.setBeer(beer)
                    }
            }

        return rootView
    }

    /**
     * Updates UI info including toolbar title
     */
    private fun updateUI(beer: Beer) {
        activity?.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout)?.title = beer.name

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
        button_beerdetail_changeavailability.setImageResource(
            if(beer.available) R.drawable.ic_barrel_no
            else R.drawable.ic_barrel_ok
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