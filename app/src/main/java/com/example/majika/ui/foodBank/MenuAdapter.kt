package com.example.majika.ui.foodBank

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.majika.databinding.MenuCardBinding
import com.example.majika.models.Menu
import com.example.majika.room.CartEntity
import com.example.majika.ui.shoppingCart.ShoppingCartViewModel
import kotlinx.android.synthetic.main.menu_card.view.*

class MenuAdapter: RecyclerView.Adapter<MenuAdapter.Holder>(){
    class Holder(val view: View) : RecyclerView.ViewHolder(view)

    private var menuList = arrayListOf<Menu>()
    private lateinit var viewModel: ShoppingCartViewModel
    private lateinit var binding: MenuCardBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        binding = MenuCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding.root)
    }

    override fun getItemCount(): Int {
        return menuList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (position == 0 && menuList[position].type == "Drink") {
            holder.view.section_name.visibility = View.VISIBLE
            holder.view.section_name.text = "Menu Minuman"
        } else if (position == 0 && menuList[position].type == "Food") {
            holder.view.section_name.visibility = View.VISIBLE
            holder.view.section_name.text = "Menu Makanan"
        } else if (menuList[position].type != menuList[position-1].type) {
            holder.view.section_name.visibility = View.VISIBLE
            if (menuList[position].type == "Drink") {
                holder.view.section_name.text = "Menu Minuman"
            } else if (menuList[position].type == "Food") {
                holder.view.section_name.text = "Menu Makanan"
            }
        } else {
            holder.view.section_name.visibility = View.GONE
        }
        holder.view.menu_name.text = menuList[position].name
        holder.view.menu_price.text = menuList[position].currency.plus(" ").plus(menuList[position].price.formatDecimalSeparator())
        holder.view.menu_sold.text = menuList[position].sold.formatDecimalSeparator().plus(" Terjual!")
        holder.view.menu_desc.text = menuList[position].description
        viewModel.listCart.observeForever {
            for (i in it) {
                if (i.item == menuList[position].name && i.price == menuList[position].price) {
                    holder.view.quantity.text = i.quantity.toString()
                    holder.view.quantity.visibility = View.VISIBLE
                    holder.view.btn_minus.visibility = View.VISIBLE
                }
            }
        }
        holder.view.btn_plus.setOnClickListener {
            val quantityNow = holder.view.quantity.text.toString().toInt() + 1
            val newCart = CartEntity(menuList[position].name, menuList[position].price, quantityNow)
            if (holder.view.quantity.text.toString().toInt() == 0) {
                viewModel.insertCart(newCart)
                holder.view.btn_minus.visibility = View.VISIBLE
            } else {
                viewModel.updateCart(newCart)
            }
            holder.view.quantity.text = quantityNow.toString()
            holder.view.quantity.visibility = View.VISIBLE
        }
        holder.view.btn_minus.setOnClickListener {
            val quantityNow = holder.view.quantity.text.toString().toInt() - 1
            if (quantityNow == 0) {
                val newCart = CartEntity(menuList[position].name, menuList[position].price, 1)
                viewModel.deleteCart(newCart)
                holder.view.quantity.visibility = View.INVISIBLE
                holder.view.btn_minus.visibility = View.INVISIBLE
            } else {
                val newCart = CartEntity(menuList[position].name, menuList[position].price, quantityNow)
                viewModel.updateCart(newCart)
            }

            holder.view.quantity.text = quantityNow.toString()
        }
    }

    fun showData(newMenuList: ArrayList<Menu>, cartViewModel: ShoppingCartViewModel){
        viewModel = cartViewModel
        menuList.clear()
        menuList.addAll(newMenuList)
        notifyDataSetChanged()
    }

    fun Int.formatDecimalSeparator(): String {
        return toString()
            .reversed()
            .chunked(3)
            .joinToString(",")
            .reversed()
    }
}