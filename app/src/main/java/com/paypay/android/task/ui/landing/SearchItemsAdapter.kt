package com.paypay.android.task.ui.landing

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paypay.android.task.data.response.SearchCityResoponseItem
import com.paypay.android.task.databinding.ItemCityBinding


class SearchItemsAdapter(
    private var forecastList: List<SearchCityResoponseItem>? = arrayListOf(),
    private val context: Context,
    private val onClickListener: OnClickListener

) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(playerList: List<SearchCityResoponseItem>) {
        this.forecastList = playerList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return forecastList?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderForecast(
            ItemCityBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        forecastList?.get(position)
            ?.let {
                (holder as ViewHolderForecast).bind(
                    context = context,
                    forecast = it,
                    onClickListener
                )
            }
    }

    private class ViewHolderForecast(val binding: ItemCityBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(
            context: Context,
            forecast: SearchCityResoponseItem,
            onClickListener: OnClickListener
        ) {
            binding.txtCityName.text = "${forecast.name} - "
            binding.txtCountryName.text = "${forecast.country}"

            binding.rootLayout.setOnClickListener {
                onClickListener.clickListener(forecast)
            }

        }
    }


    class OnClickListener(val clickListener: (RepoResponseItem: SearchCityResoponseItem) -> Unit) {
        fun onClick(RepoResponseItem: SearchCityResoponseItem) = clickListener(RepoResponseItem)
    }
}