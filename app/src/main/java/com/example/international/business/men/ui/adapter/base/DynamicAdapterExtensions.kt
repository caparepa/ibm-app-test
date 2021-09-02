package com.example.international.business.men.ui.adapter.base

import com.google.gson.Gson

inline fun <reified T : Any> Any?.mapTo(newClass: Class<T>): T? =
    Gson().run {
        fromJson(toJson(this@mapTo), newClass)
    }
