package br.com.generic.base.ui.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import br.com.generic.base.databinding.HomeListItemsBinding
import br.com.generic.base.models.view.response.generic.GenericResponseNames
import br.com.generic.base.utils.DataDiffUtil

class HomeAdapter : RecyclerView.Adapter<HomeAdapter.ReceivingViewHolder>() {

    class ReceivingViewHolder(val binding: HomeListItemsBinding) : RecyclerView.ViewHolder(binding.root)

    private var items : List<GenericResponseNames?> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceivingViewHolder {
        val binding = HomeListItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReceivingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReceivingViewHolder, position: Int) {

        val viewItem = items[position]
        holder.binding.apply {

            tvHomeField01.text = viewItem?.genericUserCode?.viewField
            tvHomeField02.text = viewItem?.genericUserName?.viewField

        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    // Função que faz a atualização da lista dos itens do recycler view
    fun setUpItems(itemsData : ArrayList<GenericResponseNames?>) {
        val itemsDiffUtil = DataDiffUtil(this.items, itemsData)
        val diffUtilResult = DiffUtil.calculateDiff(itemsDiffUtil)
        this.items = itemsData
        diffUtilResult.dispatchUpdatesTo(this)
    }
}