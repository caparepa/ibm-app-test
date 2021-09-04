package com.example.international.business.men.network.interceptor

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptorImpl : HeaderInterceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
        return chain.proceed(request.build())
    }
}