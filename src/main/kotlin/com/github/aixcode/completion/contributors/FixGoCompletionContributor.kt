package com.github.aixcode.completion.contributors

import com.github.aixcode.completion.CodeTemplateLookupElement
import com.intellij.codeInsight.completion.CompletionResult
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.PrioritizedLookupElement
import com.intellij.codeInsight.lookup.LookupElement

class FixGoCompletionContributor : AiXCodeCompletionContributor() {
    override val renderElementHandle: (
        element: LookupElement,
        codeCompletion: String,
        priority: Double,
        rs: CompletionResultSet,
        r: CompletionResult
    ) -> Unit =
        { element: LookupElement,
          codeCompletion: String,
          priority: Double,
          rs: CompletionResultSet,
          r: CompletionResult ->

            val codeTemplateLookupElement = CodeTemplateLookupElement(element.lookupString, codeCompletion)
            rs.addElement(PrioritizedLookupElement.withPriority(codeTemplateLookupElement, priority))
        }
}