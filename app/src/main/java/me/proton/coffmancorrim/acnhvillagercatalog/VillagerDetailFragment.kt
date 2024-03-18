package me.proton.coffmancorrim.acnhvillagercatalog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import me.proton.coffmancorrim.acnhvillagercatalog.viewmodels.MainViewModel


class VillagerDetailFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()

    private lateinit var villagerIconImageView: ImageView
    private lateinit var villagerNameTextView: TextView
    private lateinit var villagerGenderTextView: TextView
    private lateinit var villagerPersonalityTextView: TextView
    private lateinit var villagerSpeciesTextView: TextView
    private lateinit var villagerBirthdayTextView: TextView
    private lateinit var textVillagerId: TextView

    private lateinit var textQuote: TextView
    private lateinit var textCatchphrase: TextView
    private lateinit var textFavStyles: TextView
    private lateinit var textFavColors: TextView
    private lateinit var textHobby: TextView
    private lateinit var textHouseExteriorUrl: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_villager_detail, container, false)
        villagerIconImageView = view.findViewById<ImageView>(R.id.image_villager_icon)
        villagerNameTextView = view.findViewById<TextView>(R.id.text_villager_name)
        villagerGenderTextView = view.findViewById<TextView>(R.id.text_villager_gender)
        villagerPersonalityTextView = view.findViewById<TextView>(R.id.text_villager_personality)
        villagerSpeciesTextView = view.findViewById<TextView>(R.id.text_villager_species)
        villagerBirthdayTextView = view.findViewById<TextView>(R.id.text_villager_birthday)
        textVillagerId = view.findViewById(R.id.text_villager_id)

        textQuote = view.findViewById(R.id.text_quote)
        textCatchphrase = view.findViewById(R.id.text_catchphrase)
        textFavStyles = view.findViewById(R.id.text_fav_styles)
        textFavColors = view.findViewById(R.id.text_fav_colors)
        textHobby = view.findViewById(R.id.text_hobby)
        textHouseExteriorUrl = view.findViewById(R.id.text_house_exterior_url)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val villager = mainViewModel.detailVillager
        if (villager != null) {
            Glide
                .with(this)
                .load("ignorethis")
                .placeholder(R.drawable.placeholder)
                .into(villagerIconImageView)
            villagerNameTextView.text = villager.name
            villagerGenderTextView.text = villager.gender.name
            villagerPersonalityTextView.text = villager.personality.name
            villagerSpeciesTextView.text = villager.species.name
            villagerBirthdayTextView.text = "${villager.birthday.month} ${villager.birthday.day}"
            textVillagerId.text = villager.id

            textQuote.text = villager.nhDetails.quote
            textCatchphrase.text = villager.nhDetails.catchphrase
            textFavStyles.text = villager.nhDetails.favStyles.joinToString(", ")
            textFavColors.text = villager.nhDetails.favColors.joinToString(", ")
            textHobby.text = villager.nhDetails.hobby.name
            Glide
                .with(this)
                .load("ignorethis")
                .placeholder(R.drawable.placeholder)
                .into(textHouseExteriorUrl)
        }

    }
}
