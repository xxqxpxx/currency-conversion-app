package com.paypay.android.task.ui.landing

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.paypay.android.task.data.response.CurrencyModel
import com.paypay.android.task.databinding.ItemRateBinding
import java.math.RoundingMode
import java.text.DecimalFormat

class CurrencyAdapter(
    private var currencyList: List<CurrencyModel>? = arrayListOf(),
    private val context: Context
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(playerList: List<CurrencyModel>) {
        this.currencyList = playerList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return currencyList?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolderForecast(
            ItemRateBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        currencyList?.get(position)
            ?.let { (holder as ViewHolderForecast).bind(context = context, currncy = it) }
    }

    private class ViewHolderForecast(val binding: ItemRateBinding) :
        RecyclerView.ViewHolder(binding.root) {
         fun bind(context: Context, currncy: CurrencyModel) {


             binding.txtCurrencyId .text = currncy.id
              binding.txtRate.text = roundOffDecimal(currncy.rate)
                binding.txtCurrencyName.text = currncy.name

        }

        fun roundOffDecimal(number: Double): String {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            return df.format(number).toDouble().toString()
        }


    }



}