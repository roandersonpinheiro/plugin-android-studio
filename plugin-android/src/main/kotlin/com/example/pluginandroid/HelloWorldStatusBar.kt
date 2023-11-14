package com.example.pluginandroid

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetProvider
import com.intellij.ui.JBColor
import com.intellij.util.Consumer
import java.awt.event.MouseEvent
import javax.swing.JLabel
import javax.swing.JPanel

class HelloWorldStatusBar : StatusBarWidgetProvider {
    override fun getWidget(project: Project): StatusBarWidget {
        return object : StatusBarWidget, StatusBarWidget.Multiframe {
            private val myPanel = JPanel()

            init {
                val label = JLabel("Hello, World!")
                label.foreground = JBColor.RED // Altere a cor do texto se necessário
                myPanel.add(label)
            }

            override fun ID(): String = "HelloWorldStatusBar"

            override fun install(statusBar: StatusBar) {
                // Implemente qualquer lógica necessária para a instalação do widget
            }

            override fun dispose() {
                // Implemente qualquer lógica necessária para a liberação do widget
            }

            override fun getPresentation(): StatusBarWidget.WidgetPresentation =
                object : StatusBarWidget.TextPresentation {
                    override fun getText(): String = "Hello, World!"

                    override fun getTooltipText(): String? = "Greetings from your plugin!"

                    override fun getClickConsumer(): Consumer<MouseEvent>? {
                        // Implemente qualquer lógica de clique se necessário
                        return null
                    }

                    override fun getAlignment(): Float = 0.0f // Ajuste o alinhamento se necessário
                }

            override fun copy(): StatusBarWidget = this
        }
    }
}
