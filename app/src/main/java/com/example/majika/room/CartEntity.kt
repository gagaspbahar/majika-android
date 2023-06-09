package com.example.majika.room

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "cart", primaryKeys = ["item", "price"])
data class CartEntity (
    @ColumnInfo(name = "item")
    var item: String = "",

    @ColumnInfo(name = "price")
    var price: Int = 0,

    @ColumnInfo(name = "quantity")
    var quantity: Int = 0,
)
