package com.github.aixcode.action

import com.github.aixcode.utils.HttpUtil
import com.github.aixcode.utils.ParsePromptResponse
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

class AIXCodeGenerateAction : AnAction("AIXCodeGenerate") {

    override fun actionPerformed(event: AnActionEvent) {
        val psiFile: PsiFile? = event.getData(LangDataKeys.PSI_FILE)
        val editor: Editor? = event.getData(LangDataKeys.EDITOR)
        if (psiFile != null && editor != null) {
            try {
                if (editor.selectionModel.hasSelection()) {
                    val selectedText = editor.selectionModel.selectedText ?: ""
                    println("selectedText=${selectedText}")

                    // 内置语言:golang
                    val newSelectedText = "Human:用golang实现${selectedText}代码\\nAI:"

                    val aixcode = HttpUtil.post("https://api.forchange.cn/",
                        mapOf("prompt" to newSelectedText)
                    )
                    println("aixcode=${aixcode}")

                    val code = ParsePromptResponse(aixcode)
                    println("code=${aixcode}")

                    WriteCommandAction.runWriteCommandAction(
                        psiFile.project, "AIXCodeGenerate", "empty",
                        {
                            editor.document.insertString(editor.selectionModel.selectionEnd, code)
                        },
                        psiFile
                    )

                }
            } catch (e: Exception) {
                println("AIXCodeGen code failed:$e")
            }
        } else {
            println("psiFile is null")
        }
    }
}