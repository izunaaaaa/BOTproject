package com.example.bot.retrofit;

import com.example.bot.model.Item;

import java.util.List;

public interface ResultListener {
    void onSuccess(List<Item> items);
    void onFail();
}
