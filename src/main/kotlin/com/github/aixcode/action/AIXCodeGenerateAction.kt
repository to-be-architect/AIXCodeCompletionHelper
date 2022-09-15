package com.github.aixcode.action

import com.github.aixcode.utils.HttpUtil
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

//const val HOST="http://72f7b478.vip.cpolar.cn"
const val HOST="http://127.0.0.1:9888"
class AIXCodeGenerateAction : AnAction("AIXCode") {

    override fun actionPerformed(event: AnActionEvent) {
        val psiFile: PsiFile? = event.getData(LangDataKeys.PSI_FILE)
        val editor: Editor? = event.getData(LangDataKeys.EDITOR)
        if (psiFile != null && editor != null) {
            try {
                if (editor.selectionModel.hasSelection()) {
                    val selectedText = editor.selectionModel.selectedText ?: ""
                    println("selectedText=${selectedText}")
                    // aixcoder api
                    val aixcode = HttpUtil.post("${HOST}/aix", mapOf("x" to selectedText))
                    println("aixcode=${aixcode}")

                    WriteCommandAction.runWriteCommandAction(
                        psiFile.project, "AIXCodeGenerate", "empty",
                        {
                            editor.document.insertString(editor.selectionModel.selectionEnd, aixcode)
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