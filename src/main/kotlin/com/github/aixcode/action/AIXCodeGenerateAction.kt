package com.github.aixcode.action

import com.github.aixcode.utils.ChatGPTUtil
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

                    // 这地方可以定制：加一些内置语言的文本:golang
                    val prompt = "${selectedText}"

                    val code = ChatGPTUtil.GetAIXCode(prompt)

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