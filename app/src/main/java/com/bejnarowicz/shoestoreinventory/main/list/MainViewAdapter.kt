package com.bejnarowicz.shoestoreinventory.main.list

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bejnarowicz.shoestoreinventory.databinding.ItemListBinding
import com.bejnarowicz.shoestoreinventory.database.model.Shoe


class MainViewAdapter : RecyclerView.Adapter<MainViewAdapter.MyViewHolder>() {

    private var shoeList = emptyList<Shoe>()


    class MyViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            ItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun getItemCount(): Int = shoeList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = shoeList[position]

        holder.binding.tvShoeName.text = currentItem.name
        holder.binding.tvShoeBrand.text = currentItem.brand
        holder.binding.tvAmount.text = currentItem.stock
        holder.binding.ivShoePhoto.setImageURI(Uri.parse(currentItem.photo))

        holder.binding.rowLayout.setOnClickListener {
           val action = MainViewFragmentDirections.actionMainViewFragmentToUpdateFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
       }

   }

    fun setData(shoe: List<Shoe>) {
        this.shoeList = shoe
        notifyDataSetChanged()
    }


}