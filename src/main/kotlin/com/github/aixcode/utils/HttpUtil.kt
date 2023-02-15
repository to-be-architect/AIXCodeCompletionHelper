package com.github.aixcode.utils

import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson

object HttpUtil {

    fun post(api: String, data: Map<String, String>): String {
        var res = ""

        val (_, _, result) = api.httpPost()
            .timeout(60000)
            .jsonBody(Gson().toJson(data).toString())
            .responseString()

        res = result.get()

        return res
    }



}

class PromptResponse {
    lateinit var choices: List<Choices>
}

class Choices {
    var block = false
    var finishReason = "stop"
    var index = 0
    var text = ""
}



fun ParsePromptResponse(res: String): String {
    val gson = Gson()
    val PromptResponse = gson.fromJson(res, PromptResponse::class.java)
    if (PromptResponse.choices.size > 0) {
        val text = PromptResponse.choices[0].text
        println("aixcode=$text")
        return text
    } else {
        return ""
    }
}

fun main() {
    //    val aixcode = HttpUtil.post("https://api.forchange.cn/",
    //        mapOf("prompt" to "Human:用golang实现冒泡排序代码\nAI:")
    //    )
    val aixcode = """
        {"choices":[{"text":"\n\npackage main\n\nimport \"fmt\"\n\nfunc BubbleSort(arr []int) {\n\tfor i := 0; i \u003c len(arr); i++ {\n\t\tfor j := 0; j \u003c len(arr)-i-1; j++ {\n\t\t\tif arr[j] \u003e arr[j+1] {\n\t\t\t\tarr[j], arr[j+1] = arr[j+1], arr[j]\n\t\t\t}\n\t\t}\n\t}\n}\n\nfunc main() {\n\tarr := []int{5, 4, 3, 2, 1}\n\tBubbleSort(arr)\n\tfmt.Println(arr)\n}","index":0,"finish_reason":"stop","block":false}],"error":null}
    """.trimIndent()

    ParsePromptResponse(aixcode)

}