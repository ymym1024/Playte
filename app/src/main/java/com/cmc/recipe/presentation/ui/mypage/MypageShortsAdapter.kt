package com.cmc.recipe.presentation.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import com.cmc.recipe.data.model.RecipeStep
import com.cmc.recipe.data.model.SearchShorts
import com.cmc.recipe.data.model.entity.ShortsEntity
import com.cmc.recipe.data.model.response.ShortsContent
import com.cmc.recipe.databinding.ItemMypageShortsBinding
import com.cmc.recipe.databinding.ItemRecipeStepBinding
import com.cmc.recipe.databinding.ItemSearchShortsBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.presentation.ui.base.OnClickListener
import com.cmc.recipe.utils.loadImagesWithGlide
import com.cmc.recipe.utils.loadImagesWithGlideRound

class MypageShortsAdapter:
    BaseAdapter<ShortsEntity, ItemMypageShortsBinding, MypageShortsItemHolder>() {

    private lateinit var clickListener: OnClickListener
    fun setListener(clickListener: OnClickListener){
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MypageShortsItemHolder {
        return MypageShortsItemHolder(
            ItemMypageShortsBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            clickListener
        )
    }
}

class MypageShortsItemHolder(viewBinding: ItemMypageShortsBinding, val clickListener: OnClickListener):
    BaseHolder<ShortsEntity, ItemMypageShortsBinding>(viewBinding){
    override fun bind(binding: ItemMypageShortsBinding, item: ShortsEntity?) {
        binding.let { view->
            view.ivShortsThumbnail.loadImagesWithGlideRound(item?.recipe_thumbnail_img!!,20)
            view.tvShortsNick.text = "${item?.shortform_name}"

            view.shortsItem.setOnClickListener {
                clickListener.onMovePage(item.shorts_id)
            }
        }
    }
}
