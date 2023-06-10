package com.github.aixcode.action

import com.github.aixcode.utils.ChatGLMUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

class ChatGLM : AnAction("ChatGLM") {

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

                    val code = ChatGLMUtil.get_local(prompt)

                    WriteCommandAction.runWriteCommandAction(
                        psiFile.project, "ChatGLM", "empty",
                        {
                            editor.document.insertString(editor.selectionModel.selectionEnd, code)
                        },
                        psiFile
                    )

                }
            } catch (e: Exception) {
                println("ChatGLM code gen failed:$e")
            }
        } else {
            println("psiFile is null")
        }
    }
}