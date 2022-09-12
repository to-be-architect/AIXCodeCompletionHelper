package com.github.aixcode.config

import com.github.aixcode.utils.createNotification
import com.github.aixcode.utils.showFullNotification
import com.intellij.ide.plugins.IdeaPluginDescriptor
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.notification.NotificationListener.UrlOpeningListener
import com.intellij.notification.NotificationType
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity

/**
 * @author: github.com/izhangzhihao ，tuchg
 * @date: 2020/11/20 13:42
 * @description:
 */
class UpdateNotifyActivity : StartupActivity {

    override fun runActivity(project: Project) {
        val settings = PluginSettingsState.instance
        if (getPlugin()?.version != settings.version) {
            settings.version = getPlugin()!!.version
            showUpdate(project)
        }
    }

    companion object {
        private const val pluginId = "com.github.tuchg.nonasciicodecompletionhelper"

        private val updateContent: String by lazy {
            //language=HTML
            """
            <br/>
            欢迎使用 aiXCoder !!!
            <hr>
           """
        }

        private fun changelog(): String {
            val plugin = getPlugin()
            return if (plugin == null) {
                """https://github.com/tuchg/ChinesePinyin-CodeCompletionHelper/releases"""
            } else {
                """https://github.com/tuchg/ChinesePinyin-CodeCompletionHelper/releases/tag/v${plugin.version}"""
            }

        }

        fun getPlugin(): IdeaPluginDescriptor? = PluginManagerCore.getPlugin(PluginId.getId(pluginId))

        private fun updateMsg(): String {
            val plugin = getPlugin()
            return if (plugin == null) {
                "ChinesePinyin-CodeCompletionHelper 已安装。"
            } else {
                "ChinesePinyin-CodeCompletionHelper 已更新至 ${plugin.version}"
            }
        }

        private fun showUpdate(project: Project) {
            val notification = createNotification(
                updateMsg(),
                updateContent,
                NotificationType.INFORMATION,
                UrlOpeningListener(false)
            )
            showFullNotification(project, notification)
        }
    }
}