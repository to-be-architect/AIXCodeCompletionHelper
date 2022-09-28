package com.github.aixcode.completion.contributors

import com.github.aixcode.completion.CodePrefixMatcher
import com.github.aixcode.completion.CodeTemplateLookupElement
import com.github.aixcode.constant.codeMap
import com.github.aixcode.utils.TextDistanceUtil
import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElement


// 自定义了的语言
val languages = arrayOf("Go", "Kotlin", "C#")

open class AiXCodeCompletionContributor : CompletionContributor() {
    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
        // 手工过滤没必要执行的贡献器流程
        if (!languages.contains(parameters.originalFile.fileType.name)) {
            return
        }

        val prefix = result.prefixMatcher.prefix
        println("prefix=${prefix}")

        val resultSet = result.withPrefixMatcher(CodePrefixMatcher(result.prefixMatcher))
        resultSet.addLookupAdvertisement("aiXCoder")

        var code = ""
        // TODO 配置常用代码模板,然后使用 缩写关键词进行智能补全
        for (key in codeMap.keys) {
            if (matches(prefix, key)) {
                code = codeMap[key]!!
                resultSet.addElement(
                    PrioritizedLookupElement.withPriority(
                        CodeTemplateLookupElement(prefix, code), 1000.0
                    )
                )
            }
        }

        // 先跳过当前 Contributors 获取包装后的 lookupElement而后进行修改装饰
        resultSet.runRemainingContributors(parameters) { r ->
            val element = r.lookupElement
            println("lookupString=${element.lookupString}")
            resultSet.passResult(r)
            renderElementHandle(element, code, 500.0, resultSet, r)
        }

        // 修复 输入单个字符无响应
        resultSet.restartCompletionWhenNothingMatches()

        super.fillCompletionVariants(parameters, result)
    }

    open val renderElementHandle: (element: LookupElement, codeCompletion: String, priority: Double, rs: CompletionResultSet, r: CompletionResult) -> Unit =
        { element, _, priority, rs, r ->
            val withPriority = PrioritizedLookupElement.withPriority(element, priority)
            val wrap = CompletionResult.wrap(withPriority, r.prefixMatcher, r.sorter)
            rs.passResult(wrap!!)
        }
}

private fun matches(prefix: String, key: String) = TextDistanceUtil.getSimilarity(prefix, key) > 0.5
