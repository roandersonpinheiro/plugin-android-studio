package com.example.pluginandroid

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextPane
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.StyleConstants

class CustomMessageDialog(project: Project?, title: String?, private val message: String) : DialogWrapper(project) {

    init {
        init()
        setTitle(title)
    }

    override fun createCenterPanel(): JComponent? {
        val panel = JPanel()
        val textPane = JTextPane()
        textPane.contentType = "text/html"
        textPane.text = formatHtmlMessage(message)
        textPane.isEditable = false
        panel.add(textPane)
        return panel
    }

    private fun formatHtmlMessage(message: String): String {
        val lines = message.split("\n")
        val formattedLines = lines.map { formatLine(it) }
        return formattedLines.joinToString("<br>")
    }

    private fun formatLine(line: String): String {
        val parts = line.split(" - ")
        if (parts.size == 2) {
            val fileName = "<font color='blue'>${parts[0]}</font>"
            val changes = parts[1].replace("insertion(s)", "<font color='green'>insertion(s)</font>")
                .replace("deletion(s)", "<font color='red'>deletion(s)</font>")
            return "$fileName - $changes"
        }
        return line
    }
}
