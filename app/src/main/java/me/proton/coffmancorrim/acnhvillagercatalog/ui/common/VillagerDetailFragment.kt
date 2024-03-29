package me.proton.coffmancorrim.acnhvillagercatalog.ui.common

import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import me.proton.coffmancorrim.acnhvillagercatalog.R
import me.proton.coffmancorrim.acnhvillagercatalog.databinding.FragmentVillagerDetailBinding
import me.proton.coffmancorrim.acnhvillagercatalog.viewmodels.MainViewModel


class VillagerDetailFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentVillagerDetailBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVillagerDetailBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val villager = mainViewModel.detailVillager
        if (villager != null) {

            val backgroundColor = Color.parseColor("#${villager.titleColor}")
            val textColor = Color.parseColor("#${villager.textColor}")
            binding.imageVillagerBackgroundColor.setBackgroundColor(backgroundColor)
            binding.textHeaderCatchphrase.setBackgroundColor(backgroundColor)
            binding.textHeaderBirthday.setBackgroundColor(backgroundColor)
            binding.textHeaderFavColors.setBackgroundColor(backgroundColor)
            binding.textHeaderHobby.setBackgroundColor(backgroundColor)
            binding.textHeaderQuote.setBackgroundColor(backgroundColor)
            binding.textHeaderFavStyles.setBackgroundColor(backgroundColor)
            binding.textHeaderSpecies.setBackgroundColor(backgroundColor)
            binding.textHouse.setBackgroundColor(backgroundColor)
            binding.imageOuterBackground.setBackgroundColor(backgroundColor)

            binding.textHeaderCatchphrase.setTextColor(textColor)
            binding.textHeaderBirthday.setTextColor(textColor)
            binding.textHeaderFavColors.setTextColor(textColor)
            binding.textHeaderHobby.setTextColor(textColor)
            binding.textHeaderQuote.setTextColor(textColor)
            binding.textHeaderFavStyles.setTextColor(textColor)
            binding.textHeaderSpecies.setTextColor(textColor)
            binding.textHouse.setTextColor(textColor)
            binding.textHouseExteriorUrl.setBackgroundColor(textColor)


            binding.textVillagerName.setTextColor(textColor)
            binding.textVillagerGender.setTextColor(textColor)
            binding.textVillagerPersonality.setTextColor(textColor)

            val backgroundCircle = ShapeDrawable(OvalShape())
            backgroundCircle.paint.color = Color.parseColor("#${villager.textColor}")
            binding.imageVillagerIcon.background = backgroundCircle
            Glide
                .with(this)
                .load(villager.nhDetails.iconUrl)
                .placeholder(R.drawable.placeholder)
                .into(binding.imageVillagerIcon)

            binding.textVillagerName.text = villager.name
            binding.textVillagerGender.text = villager.gender.name
            binding.textVillagerPersonality.text = villager.personality.name
            binding.textVillagerSpecies.text = villager.species.name
            binding.textVillagerBirthday.text = "${villager.birthdayMonth} ${villager.birthdayDay}"

            binding.textQuote.text = villager.nhDetails.quote
            binding.textCatchphrase.text = villager.nhDetails.catchphrase
            binding.textFavStyles.text = villager.nhDetails.favStyles.joinToString(", ")
            binding.textFavColors.text = villager.nhDetails.favColors.joinToString(", ")
            binding.textHobby.text = villager.nhDetails.hobby.name
            Glide
                .with(this)
                .load(villager.nhDetails.houseExteriorUrl)
                .placeholder(R.drawable.placeholder)
                .into(binding.textHouseExteriorUrl)
        }

    }
}
