
package com.example.bot.model;

import com.google.gson.annotations.Expose;

import java.util.List;

@SuppressWarnings("unused")
public class ItemsDetail {

    @Expose
    private Item item;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

}
