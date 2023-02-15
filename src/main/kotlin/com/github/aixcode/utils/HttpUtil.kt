package com.github.aixcode.utils

import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson

object HttpUtil {

    fun post(api: String, data: Map<String, String>): String {
        var res = ""

        val (_, _, result) = api.httpPost()
            .timeout(60 * 1000)
            .jsonBody(Gson().toJson(data).toString())
            .timeout(60 * 1000)
            .timeoutRead(60 * 1000)
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
        var text = PromptResponse.choices[0].text
        println("aixcode=$text")
        // 代码中的换行 制表符的处理
        text = text.replace("\\n",
            """
""")
        text = text.replace("\\t", """    """)

        // 加个换行
        return """
            
${text}
            
"""

    } else {
        return "\n"
    }
}

fun main() {
    //    val aixcode = HttpUtil.post("https://api.forchange.cn/",
    //        mapOf("prompt" to "Human:用golang实现冒泡排序代码\nAI:")
    //    )
    val aixcode = """
        {"choices":[{"text":"\n\npackage main\n\nimport \"fmt\"\n\nfunc BubbleSort(arr []int) {\n\tfor i := 0; i \u003c len(arr); i++ {\n\t\tfor j := 0; j \u003c len(arr)-i-1; j++ {\n\t\t\tif arr[j] \u003e arr[j+1] {\n\t\t\t\tarr[j], arr[j+1] = arr[j+1], arr[j]\n\t\t\t}\n\t\t}\n\t}\n}\n\nfunc main() {\n\tarr := []int{5, 4, 3, 2, 1}\n\tBubbleSort(arr)\n\tfmt.Println(arr)\n}","index":0,"finish_reason":"stop","block":false}],"error":null}
    """.trimIndent()

    // package main\n\nimport (\n\t"fmt"\n)\n\nfunc main() {\n\tarr := []int{3, 44, 38, 5, 47, 15, 36, 26, 27, 2, 46, 4, 19, 50, 48}\n\n\tfmt.Println("排序前：", arr)\n\n\tfor i := 0; i < len(arr)-1; i++ {\n\t\tfor j := 0; j < len(arr)-1-i; j++ {\n\t\t\tif arr[j] > arr[j+1] {\n\t\t\t\ttemp := arr[j]\n\t\t\t\tarr[j] = arr[j+1]\n\t\t\t\tarr[j+1] = temp\n\t\t\t}\n\t\t}\n\t}\n\n\tfmt.Println("排序后：", arr)\n}
    val rawCode = ParsePromptResponse(aixcode)
    println(rawCode)

}