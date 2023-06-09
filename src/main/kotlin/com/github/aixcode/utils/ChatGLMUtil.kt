package com.github.aixcode.utils

import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import java.io.File


object ChatGLMUtil {


    val API = "https://api.openai.com/v1/chat/completions"

    // 确保将 OPENAI_API_KEY 替换为您的 API 密钥,并在本机目录下面创建一个文件名为："OPENAI_API_KEY" 的文件，文件内容是你的 API_KEY
    val OPENAI_API_KEY = File("${System.getProperty("user.home")}/OPENAI_API_KEY")
        .readText(Charsets.UTF_8)
        .trim() // trim() 方法用于删除字符串的头尾空白符

    fun GetAIXCode(prompt: String): String {


        var res = ""

        // -d 请求入参
        val data = mutableMapOf<String, Any>()
        // data["model"] = "text-davinci-003"
        data["model"] = "gpt-3.5-turbo"

        val inputMessages = listOf(Message("user", prompt))
        data["messages"] = inputMessages
        // 剩下的参数使用默认值即可:
        // data["prompt"] = prompt
        // data["temperature"] = 0.7
        // data["max_tokens"] = 520
        // data["top_p"] = 1
        // data["frequency_penalty"] = 0
        // data["presence_penalty"] = 0

        println("OPENAI_API_KEY=$OPENAI_API_KEY")

        val (_, _, result) = API.httpPost()
            .appendHeader("Content-Type", "application/json")
            .appendHeader("Authorization", "Bearer $OPENAI_API_KEY")
            .timeout(60 * 1000)
            .jsonBody(Gson().toJson(data).toString())
            .timeout(60 * 1000)
            .timeoutRead(60 * 1000)
            .responseString()

        res = result.get()

        return ParseChatGPTResponse(res)
    }


    class Message(
        var role: String = "",
        var content: String = ""
    )

    // {"message":"package mainimport \"fmt\"func bubbleSort(a []int) {\tfor i := 0; i < len(a); i++ {\t\tfor j := 0; j < len(a)-1-i; j++ {\t\t\tif a[j] > a[j+1] {  \/\/ compare the two neighbors\t\t\t\t\/\/ swap if the left one is bigger than the right one                 temp := a[j+1]  \/\/ store the right one in a temp variable for later use                 a[j+1] = a[j]   \/\/ assign the left one to be the right one                  a[j] = temp     \/\/ assign the stored value to be the left one              }        }    }    fmt.Println(\"Sorted array: \", a)   \/\/ print out sorted array } func main() {     arr := []int{5, 2, 8, 4, 1, 9}  \/\/ declare an array of ints to sort      fmt.Println(\"Unsorted array: \", arr)     \/\/ print out unsorted array     bubbleSort(arr)      \/\/ call bubble sort function and pass in our array as an argument  }","raw_message":"\npackage main\n\nimport \"fmt\"\n\nfunc bubbleSort(a []int) {\n\tfor i := 0; i < len(a); i++ {\n\t\tfor j := 0; j < len(a)-1-i; j++ {\n\t\t\tif a[j] > a[j+1] {  \/\/ compare the two neighbors\n\t\t\t\t\/\/ swap if the left one is bigger than the right one \n                temp := a[j+1]  \/\/ store the right one in a temp variable for later use \n                a[j+1] = a[j]   \/\/ assign the left one to be the right one  \n                a[j] = temp     \/\/ assign the stored value to be the left one  \n\n            }\n\n        }\n\n    }\n\n    fmt.Println(\"Sorted array: \", a)   \/\/ print out sorted array \n}\n\n func main() { \n\n    arr := []int{5, 2, 8, 4, 1, 9}  \/\/ declare an array of ints to sort  \n\n    fmt.Println(\"Unsorted array: \", arr)     \/\/ print out unsorted array \n\n    bubbleSort(arr)      \/\/ call bubble sort function and pass in our array as an argument  \n}","status":"success"}
    fun ParseChatGPTResponse(res: String): String {
        val gson = Gson()
        val GPTResponse = gson.fromJson(res, GPTResponse::class.java)

        if (GPTResponse.choices.size > 0) {
            var text = GPTResponse.choices[0].message.content
            println("aixcode=$text")

            text = replaceNewLineAndTab(text)

            // 加个换行
            return """      
${text}       
"""

        } else {
            return "\n"
        }

    }

    fun replaceNewLineAndTab(text: String): String {
        // 代码中的换行 制表符的处理
        var text = text
        text = text.replace(
            "\\n",
            """
"""
        )

        text = text.replace("\\t", """    """)
        return text
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
        var message = com.github.aixcode.utils.Message()
    }

}


fun main() {
    println(OPENAI_API_KEY)
    val aixcode = ChatGPTUtil.GetAIXCode("用 golang 实现bubble sort")
    println("aixcode=${aixcode}")
}