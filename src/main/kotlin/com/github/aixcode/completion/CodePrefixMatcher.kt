package com.github.aixcode.completion

import com.intellij.codeInsight.completion.PlainPrefixMatcher
import com.intellij.codeInsight.completion.PrefixMatcher


class CodePrefixMatcher(prefixMatcher: PrefixMatcher) : PlainPrefixMatcher(prefixMatcher.prefix) {
    private var originalMatcher: PrefixMatcher? = prefixMatcher

    override fun prefixMatches(name: String): Boolean {
        return true
    }

    override fun cloneWithPrefix(prefix: String) =
        if (prefix == this.prefix) this else CodePrefixMatcher(originalMatcher!!.cloneWithPrefix(prefix))
}
