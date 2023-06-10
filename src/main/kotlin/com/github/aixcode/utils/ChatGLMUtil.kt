package com.github.aixcode.utils

import cn.hutool.core.util.IdUtil
import cn.hutool.core.util.ObjectUtil
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import com.zhipu.utils.WuDaoUtils
import java.io.File

fun main(args: Array<String>) {
    val title = "人工智能之神经网络的前世今生和未来发展趋势"
    ChatGLMUtil.get_local(title)
}

val CHATGLM_LOCAL_API = "http://127.0.0.1:8000"


/**
 * 备注：调用该API需到平台用户手册中--》》新手指南下载平台调用工具包
 */
object ChatGLMUtil {

    // 文章创作请求地址
    private const val MODEL_REQUEST_URL = "https://maas.aminer.cn/api/paas/model/v1/open/engines/chatGLM/chatGLM"

    // 获取API Keys
    // 申请您的API密钥: https://open.bigmodel.ai/howuse/getapikey
    // 获取token 请求地址
    private const val AUTH_TOKEN_URL = "https://maas.aminer.cn/api/paas/passApiToken/createApiToken"


    private val token: String
        get() {
            try {

                // 用户API KEY（请登录开放平台获取 https://maas.aminer.cn ）
// 确保将 CHATGLM_API_KEY 替换为您的 API 密钥,并在本机目录下面创建一个文件名为："CHATGLM_API_KEY" 的文件，文件内容是你的 CHATGLM_API_KEY
                val CHATGLM_API_KEY = File("${System.getProperty("user.home")}/CHATGLM_API_KEY")
                    .readText(Charsets.UTF_8)
                    .trim() // trim() 方法用于删除字符串的头尾空白符


// 用户PUBLIC KEY（请登录开放平台获取 https://maas.aminer.cn ）
// 确保将 CHATGLM_PUBLIC_KEY 替换为您的 API 密钥,并在本机目录下面创建一个文件名为："CHATGLM_PUBLIC_KEY" 的文件，文件内容是你的 CHATGLM_PUBLIC_KEY
                val CHATGLM_PUBLIC_KEY = File("${System.getProperty("user.home")}/CHATGLM_PUBLIC_KEY")
                    .readText(Charsets.UTF_8)
                    .trim() // trim() 方法用于删除字符串的头尾空白符


                val resultMap = WuDaoUtils.getToken(AUTH_TOKEN_URL, CHATGLM_API_KEY, CHATGLM_PUBLIC_KEY)
                val code = resultMap["code"] as Int
                if (code == 200) {
                    return resultMap["data"].toString()
                }
            } catch (e: Exception) {
                println("获取token失败:$e")
            }
            return ""
        }

    /**
     * 注意这里仅为了简化编码每一次请求都去获取token，
     * 线上环境token有过期时间， 客户端可自行缓存，
     * 过期后重新获取。
     */
    fun get(prompt: String): String {
        try {
            /**
             * 注意这里仅为了简化编码每一次请求都去获取token，
             * 线上环境token有过期时间， 客户端可自行缓存，
             * 过期后重新获取。
             */
            val authToken = token
            if (ObjectUtil.isEmpty(authToken)) {
                println("获取鉴权token失败")
                return ""
            }
            println("获取鉴权token:$authToken")
            val requestTaskNo = IdUtil.getSnowflake(1, 2).nextIdStr()
            val paramsMap: HashMap<String, Any> = hashMapOf<String, Any>()
            paramsMap["top_p"] = 0.7
            paramsMap["temperature"] = 0.95
            paramsMap["prompt"] = buildPrompt(prompt)
            paramsMap["history"] = arrayOf<String>()
            paramsMap["requestTaskNo"] = requestTaskNo
            val resultMap = WuDaoUtils.executeEngine(MODEL_REQUEST_URL, authToken, paramsMap)

            println("resultMap=${resultMap}")

            val code = resultMap["code"] as Int
            if (code == 200) {
                val data = resultMap["data"] as Map<*, *>
                var outputText = data["outputText"].toString()

                outputText = replaceNewLineAndTab(outputText)

                println("outputText=${outputText}")

                return outputText
            }
        } catch (e: Exception) {
            println("获取数据失败:$e")
        }
        return ""
    }

    fun get_local(prompt: String): String {
        val data = mutableMapOf<String, Any>()
        data["prompt"] = buildPrompt(prompt)
        data["temperature"] = 0.95
        data["max_tokens"] = 4096
        data["top_p"] = 0.7

        val (_, _, result) = CHATGLM_LOCAL_API.httpPost()
            .appendHeader("Content-Type", "application/json")
            .timeout(600 * 1000)
            .jsonBody(Gson().toJson(data).toString())
            .timeout(600 * 1000)
            .timeoutRead(600 * 1000)
            .responseString()

        val res = result.get()

        return ParseChatGLMResponse(res)
    }

    private fun ParseChatGLMResponse(res: String): String {

        val gson = Gson()
        val GLMResponse = gson.fromJson(res, GLMResponse::class.java)

        if (GLMResponse.response.isNotEmpty()) {
            var text = GLMResponse.response
            println("text=$text")

            text = replaceNewLineAndTab(text)

            // 加个换行
            return """      
${text}       
"""
        } else {
            return "\n"
        }

    }

    class GLMResponse {
        var response: String = ""
        var history: List<List<String>> = listOf()
        var status: Int = 0
        var time: String = "2023-06-11 00:55:14"
    }

    private fun buildPrompt(prompt: String): String {
        val InputPrompt = """
         你是一位人工智能专家,程序员,软件架构师,CTO，请以《${prompt}》为标题，写一篇有深度有思考有见解的专业的技术博客，要求简洁、易懂、具有原理讲解和实操落地讲解的技术文章应包含以下章节：
         
         1.引言：简要介绍文章的主题和目的，以及讨论的技术和工具。
         2.概念和术语介绍：介绍技术的背景和相关概念、术语说明等。
         3.关键问题分析:详细介绍所讨论的技术或工具所解决的问题,思想起源,发展历史.
         4.问题解决方案:核心原理讲解,详细介绍所讨论的技术或工具解决方案核心思想和实现原理，以便读者了解其基本原理和实现方式。
         5.实战案例：提供实际操作的步骤和指南，包括如何设置环境、安装和配置所需的软件和库，具体的代码示例，代码说明，以及如何使用它们来完成任务或实现功能。
         6.结果分析：介绍运行的结果和分析，包括如何评估和比较不同方法或工具的性能和效果，以及如何解释和理解实验结果。
         7.总结和展望：总结文章的主要观点和结论，提供反思和展望，并指出未来的研究方向或应用场景。
         
         文章需要覆盖相关的技术和工具的原理、实现方法和应用场景，同时还需要提供实际操作的指南和示例，以便读者能够更好地理解和应用所讨论的技术和工具。
         文章的排版使用 markdown 格式输出,使用清晰的标题和段落、代码块和注释等，以便读者更容易地阅读和理解文章的内容。
         字数要求不少于5000字。
         
         """.trimIndent()

        println(InputPrompt)

        return InputPrompt
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
}
