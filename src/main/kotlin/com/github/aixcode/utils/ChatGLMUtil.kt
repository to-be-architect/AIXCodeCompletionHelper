package com.github.aixcode.utils

import cn.hutool.core.util.IdUtil
import cn.hutool.core.util.ObjectUtil
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import com.zhipu.utils.WuDaoUtils
import java.io.File

fun main(args: Array<String>) {
    val title = "大型语言模型的实现技术原理与应用"
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
        data["max_tokens"] = 8192
        data["top_p"] = 0.9

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

            return """
[toc]                      
                
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
         你是一位人工智能专家,程序员,软件架构师,CTO，请以《${prompt}》为标题，写一篇有深度有思考有见解的专业的技术博客文章， 字数要求不少于5000字。文章目录如下：

## 1. 引言

- 1.1. 背景介绍
- 1.2. 文章目的
- 1.3. 目标受众

## 2. 技术原理及概念

- 2.1. 基本概念解释
- 2.2. 技术原理介绍
- 2.3. 相关技术比较

## 3. 实现步骤与流程

- 3.1. 准备工作：环境配置与依赖安装
- 3.2. 核心模块实现
- 3.3. 集成与测试

## 4. 示例与应用

- 4.1. 实例分析
- 4.2. 应用场景介绍

## 5. 优化与改进

- 5.1. 性能优化
- 5.2. 可扩展性改进
- 5.3. 安全性加固

## 6. 结论与展望

- 6.1. 技术总结
- 6.2. 未来发展趋势与挑战

## 7. 附录：常见问题与解答


请保持逻辑清晰、结构紧凑，以便读者更容易理解和掌握所讲述的技术知识。
             
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
