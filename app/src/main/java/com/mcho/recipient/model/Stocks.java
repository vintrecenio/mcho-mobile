package com.mcho.recipient.model;

import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Stocks")
public class Stocks {

    @PrimaryKey
    @NonNull
    @SerializedName("item_id")
    private String itemId;

    @SerializedName("description")
    private String description;
    @SerializedName("stock")
    private int stock;
    @SerializedName("unit")
    private String unit;
    @SerializedName("category")
    private String category;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
