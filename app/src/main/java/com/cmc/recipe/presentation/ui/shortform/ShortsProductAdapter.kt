package com.cmc.recipe.presentation.ui.shortform


import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import com.cmc.recipe.data.model.Product
import com.cmc.recipe.databinding.ItemShortsProductBinding
import com.cmc.recipe.presentation.ui.base.BaseAdapter
import com.cmc.recipe.presentation.ui.base.BaseHolder
import com.cmc.recipe.utils.loadImagesWithGlideRound
import com.cmc.recipe.utils.toCurrencyFormat

class ShortsProductAdapter:
    BaseAdapter<Product, ItemShortsProductBinding, ShortsProductItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShortsProductItemHolder {
        return ShortsProductItemHolder(
            ItemShortsProductBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        )
    }
}

class ShortsProductItemHolder(viewBinding: ItemShortsProductBinding):
    BaseHolder<Product, ItemShortsProductBinding>(viewBinding){

    private val packageManager = viewBinding.root.context.packageManager
    private val context = viewBinding.root.context

    override fun bind(binding: ItemShortsProductBinding, item: Product?) {
        binding.ivProductMain.loadImagesWithGlideRound(item?.image,8)
        binding.tvProductName.text = item?.name
        binding.tvProductPrice.text = "${item?.price?.toCurrencyFormat()} ~"

        binding.product.setOnClickListener {
            if (isCoupangAppInstalled()) {
                openCoupangApp(item!!.url)
            } else {
                openInWebView(item!!.url)
            }
        }
    }

    interface OnClickListener{
        fun onMoveSite(url:String)
    }

    private fun isCoupangAppInstalled(): Boolean {  // coupang 앱 설치 여부를 확인
        val intent = packageManager.getLaunchIntentForPackage("com.coupang.mobile")
        return intent != null
    }

    private fun openCoupangApp(url:String) {  // coupang으로 링크 전송
        val intent = packageManager.getLaunchIntentForPackage("com.coupang.mobile")
        intent!!.data = Uri.parse(url)
        context.startActivity(intent)
    }

    private fun openInWebView(url: String) {
        // 웹뷰로 URL 열기 위한 로직
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    }
}