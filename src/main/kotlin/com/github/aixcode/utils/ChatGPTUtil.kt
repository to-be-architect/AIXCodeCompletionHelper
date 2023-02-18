package com.github.aixcode.utils

import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson


/**
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
 *
 * Authentication 验证
 * The OpenAI API uses API keys for authentication. Visit your API Keys page to retrieve the API key you'll use in your requests.
 * OpenAI API 使用 API 密钥进行身份验证。访问您的 API 密钥页面以检索您将在请求中使用的 API 密钥。
 *
 * Remember that your API key is a secret! Do not share it with others or expose it in any client-side code (browsers, apps). Production requests must be routed through your own backend server where your API key can be securely loaded from an environment variable or key management service.
 * 请记住，您的 API 密钥是秘密的！不要与他人共享或在任何客户端代码（浏览器、应用程序）中公开它。生产请求必须通过您自己的后端服务器进行路由，您的 API 密钥可以从环境变量或密钥管理服务中安全加载。
 *
 * All API requests should include your API key in an Authorization HTTP header as follows:
 * 所有 API 请求都应在 Authorization HTTP 标头中包含您的 API 密钥，如下所示：
 *
 * Authorization: Bearer YOUR_API_KEY
 * Requesting organization 请求组织
 * For users who belong to multiple organizations, you can pass a header to specify which organization is used for an API request. Usage from these API requests will count against the specified organization's subscription quota.
 * 对于属于多个组织的用户，您可以传递一个标头来指定哪个组织用于 API 请求。来自这些 API 请求的使用将计入指定组织的订阅配额。
 *
 * Example curl command: 卷曲命令示例：

 * curl https://api.openai.com/v1/models \
 *   -H 'Authorization: Bearer YOUR_API_KEY' \
 *   -H 'OpenAI-Organization: YOUR_ORG_ID'
 *
 * Example with the openai Python package:
 * openai Python 包的示例：

 * import os
 * import openai
 * openai.organization = "YOUR_ORG_ID"
 * openai.api_key = os.getenv("OPENAI_API_KEY")
 * openai.Model.list()
 *
 * Example with the openai Node.js package:
 * openai Node.js 包的示例：
 *

 * import { Configuration, OpenAIApi } from "openai";
 * const configuration = new Configuration({
 *     organization: "YOUR_ORG_ID",
 *     apiKey: process.env.OPENAI_API_KEY,
 * });
 * const openai = new OpenAIApi(configuration);
 * const response = await openai.listEngines();
 * Organization IDs can be found on your Organization settings page.
 * 组织 ID 可以在您的组织设置页面上找到。
 *
 * Making requests 发出请求
 * You can paste the command below into your terminal to run your first API request. Make sure to replace YOUR_API_KEY with your secret API key.
 * 您可以将下面的命令粘贴到您的终端中以运行您的第一个 API 请求。确保将 YOUR_API_KEY 替换为您的 API 密钥。

 * curl https://api.openai.com/v1/completions \
 * -H "Content-Type: application/json" \
 * -H "Authorization: Bearer YOUR_API_KEY" \
 * -d '{"model": "text-davinci-003", "prompt": "Say this is a test", "temperature": 0, "max_tokens": 7}'
 * This request queries the Davinci model to complete the text starting with a prompt of "Say this is a test". The max_tokens parameter sets an upper bound on how many tokens the API will return. You should get a response back that resembles the following:
 * 此请求查询 Davinci 模型以完成以提示“Say this is a test”开头的文本。 max_tokens 参数设置了 API 将返回的令牌数量的上限。您应该会收到类似于以下内容的响应：
 *
 * {
 *     "id": "cmpl-GERzeJQ4lvqPk8SkZu4XMIuR",
 *     "object": "text_completion",
 *     "created": 1586839808,
 *     "model": "text-davinci:003",
 *     "choices": [
 *         {
 *             "text": "\n\nThis is indeed a test",
 *             "index": 0,
 *             "logprobs": null,
 *             "finish_reason": "length"
 *         }
 *     ],
 *     "usage": {
 *         "prompt_tokens": 5,
 *         "completion_tokens": 7,
 *         "total_tokens": 12
 *     }
 * }
 * Now you've generated your first completion. If you concatenate the prompt and the completion text (which the API will do for you if you set the echo parameter to true), the resulting text is "Say this is a test. This is indeed a test." You can also set the stream parameter to true for the API to stream back text (as data-only server-sent events).
 * 现在你已经生成了你的第一个完成。如果您连接提示和完成文本（如果您将 echo 参数设置为 true ，API 将为您执行此操作），则生成的文本为“Say this is a test. This indeed a test.”您还可以将 API 的 stream 参数设置为 true 以流回文本（作为仅数据服务器发送的事件）。
 */

object ChatGPTUtil {

    val API = "https://api.openai.com/v1/completions"

    // 查询 API_KEY 余额： https://chat.kejicode.cn/

    // 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
    // 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
    // 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

    // val OPEN_API_KEY = "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" // 确保将 YOUR_API_KEY 替换为您的 API 密钥。
    val OPEN_API_KEY = System.getenv("OPEN_API_KEY") // 确保将 OPEN_API_KEY 替换为您的 API 密钥,并设置到系统环境变量中。

    fun GetAIXCode(prompt: String): String {
        val OPEN_API_KEY = System.getenv("OPEN_API_KEY")

        var res = ""

        // -d 请求入参
        val data = mutableMapOf<String, Any>()
        data["model"] = "text-davinci-003"
        data["prompt"] = prompt
        data["temperature"] = 0
        data["max_tokens"] = 1000

        val (_, _, result) = API.httpPost()
            .appendHeader("Content-Type", "application/json")
            .appendHeader("Authorization", "Bearer $OPEN_API_KEY")
            .timeout(60 * 1000)
            .jsonBody(Gson().toJson(data).toString())
            .timeout(60 * 1000)
            .timeoutRead(60 * 1000)
            .responseString()

        res = result.get()

        return ParseChatGPTResponse(res)
    }


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