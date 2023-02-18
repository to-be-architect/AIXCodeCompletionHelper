package com.github.aixcode.utils

import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson

object ChatGPTUtil {


    /**
     * 每个API大概可以问答45万汉字：
     * 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
     * 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
     *
     * curl https://api.openai.com/v1/completions \
     * -H "Content-Type: application/json" \
     * -H "Authorization: Bearer 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" \
     * -d '{"model": "text-davinci-003", "prompt": "使用golang实现冒泡排序算法", "temperature": 0, "max_tokens": 1000}'
     *
     *
     * {"id":"cmpl-6lN5SUkcBPBJkIZliK1OvYGBjqnCQ","object":"text_completion","created":1676748182,"model":"text-davinci-003","choices":[{"text":"\n\npackage main\n\nimport \"fmt\"\n\nfunc main() {\n\tarr := []int{3, 5, 2, 8, 1, 9, 4, 6, 7}\n\tfmt.Println(\"排序前：\", arr)\n\tbubbleSort(arr)\n\tfmt.Println(\"排序后：\", arr)\n}\n\n// 冒泡排序\nfunc bubbleSort(arr []int) {\n\tfor i := 0; i < len(arr)-1; i++ {\n\t\tfor j := 0; j < len(arr)-1-i; j++ {\n\t\t\tif arr[j] > arr[j+1] {\n\t\t\t\tarr[j], arr[j+1] = arr[j+1], arr[j]\n\t\t\t}\n\t\t}\n\t}\n}","index":0,"logprobs":null,"finish_reason":"stop"}],"usage":{"prompt_tokens":25,"completion_tokens":212,"total_tokens":237}}
     */

    /**
     * curl https://api.openai.com/v1/completions \
     * -H "Content-Type: application/json" \
     * -H "Authorization: Bearer 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" \
     * -d '{"model": "text-davinci-003", "prompt": "使用golang实现冒泡排序算法", "temperature": 0, "max_tokens": 1000}'
     */

    val API = "https://api.openai.com/v1/completions"
    val API_KEY = "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"

    fun GetAIXCode(prompt: String): String {
        var res = ""

        // -d 请求入参
        val data = mutableMapOf<String, Any>()
        data["model"] = "text-davinci-003"
        data["prompt"] = prompt
        data["temperature"] = 0
        data["max_tokens"] = 1000

        val (_, _, result) = API.httpPost()
            .appendHeader("Content-Type", "application/json")
            .appendHeader("Authorization", "Bearer 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")
            .timeout(60 * 1000)
            .jsonBody(Gson().toJson(data).toString())
            .timeout(60 * 1000)
            .timeoutRead(60 * 1000)
            .responseString()

        res = result.get()

        return ParseChatGPTResponse(res)
    }


}

class PromptResponse {
    lateinit var choices: List<Choices>
}

class GPTResponse {
    var id: String = ""
    var model: String = "" // text-davinci-003
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
        text = text.replace(
            "\\n",
            """
"""
        )
        text = text.replace("\\t", """    """)

        // 加个换行
        return """
            
${text}
            
"""

    } else {
        return "\n"
    }
}


// {"message":"package mainimport \"fmt\"func bubbleSort(a []int) {\tfor i := 0; i < len(a); i++ {\t\tfor j := 0; j < len(a)-1-i; j++ {\t\t\tif a[j] > a[j+1] {  \/\/ compare the two neighbors\t\t\t\t\/\/ swap if the left one is bigger than the right one                 temp := a[j+1]  \/\/ store the right one in a temp variable for later use                 a[j+1] = a[j]   \/\/ assign the left one to be the right one                  a[j] = temp     \/\/ assign the stored value to be the left one              }        }    }    fmt.Println(\"Sorted array: \", a)   \/\/ print out sorted array } func main() {     arr := []int{5, 2, 8, 4, 1, 9}  \/\/ declare an array of ints to sort      fmt.Println(\"Unsorted array: \", arr)     \/\/ print out unsorted array     bubbleSort(arr)      \/\/ call bubble sort function and pass in our array as an argument  }","raw_message":"\npackage main\n\nimport \"fmt\"\n\nfunc bubbleSort(a []int) {\n\tfor i := 0; i < len(a); i++ {\n\t\tfor j := 0; j < len(a)-1-i; j++ {\n\t\t\tif a[j] > a[j+1] {  \/\/ compare the two neighbors\n\t\t\t\t\/\/ swap if the left one is bigger than the right one \n                temp := a[j+1]  \/\/ store the right one in a temp variable for later use \n                a[j+1] = a[j]   \/\/ assign the left one to be the right one  \n                a[j] = temp     \/\/ assign the stored value to be the left one  \n\n            }\n\n        }\n\n    }\n\n    fmt.Println(\"Sorted array: \", a)   \/\/ print out sorted array \n}\n\n func main() { \n\n    arr := []int{5, 2, 8, 4, 1, 9}  \/\/ declare an array of ints to sort  \n\n    fmt.Println(\"Unsorted array: \", arr)     \/\/ print out unsorted array \n\n    bubbleSort(arr)      \/\/ call bubble sort function and pass in our array as an argument  \n}","status":"success"}
fun ParseChatGPTResponse(res: String): String {
    val gson = Gson()
    val GPTResponse = gson.fromJson(res, GPTResponse::class.java)

    if (GPTResponse.choices.size > 0) {
        var text = GPTResponse.choices[0].text
        println("aixcode=$text")
        // 代码中的换行 制表符的处理
        text = text.replace(
            "\\n",
            """
"""
        )

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
    val aixcode = ChatGPTUtil.GetAIXCode("用 golang 实现bubble sort")
    println("aixcode=${aixcode}")
}