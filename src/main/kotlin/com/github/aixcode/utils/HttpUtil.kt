package com.github.aixcode.utils

import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson

object HttpUtil {

    fun post(api: String, data: Map<String, String>): String {
        var res = ""

        val (_, _, result) = api.httpPost()
            .jsonBody(Gson().toJson(data).toString())
            .responseString()

        res = result.get()

        return res
    }

}