package br.com.generic.base.utils

import androidx.recyclerview.widget.DiffUtil

/**
 * O DataDiffUtil é utilizado para otimizar a atualização de um Recycler View
 */
class DataDiffUtil<T>(private val oldList: List<T>, private val newList: List<T> ): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

}