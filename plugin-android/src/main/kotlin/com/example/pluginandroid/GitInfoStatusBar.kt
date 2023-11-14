package com.example.pluginandroid

import com.intellij.openapi.project.Project
import com.intellij.openapi.vcs.changes.*
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetProvider
import com.intellij.openapi.wm.WindowManager
import com.intellij.ui.JBColor
import com.intellij.util.Consumer
import java.awt.event.MouseEvent
import javax.swing.JLabel
import javax.swing.JPanel

class GitInfoStatusBar : StatusBarWidgetProvider {
    override fun getWidget(project: Project): StatusBarWidget {
        return object : StatusBarWidget, StatusBarWidget.Multiframe {
            private val myPanel = JPanel()
            private val gitInfoLabel = JLabel()

            init {
                gitInfoLabel.foreground = JBColor.BLUE // Cor azul (RGB)
                myPanel.add(gitInfoLabel)

                // Registra um ouvinte para eventos de lista de alterações
                ChangeListManager.getInstance(project).addChangeListListener(object : ChangeListListener {
                    override fun changeListChanged(list: ChangeList?) {
                        super.changeListChanged(list)
                        updateGitInfo(project)
                    }

                    override fun changesAdded(changes: MutableCollection<Change>?, toList: ChangeList?) {
                        super.changesAdded(changes, toList)
                        updateGitInfo(project)
                    }

                    override fun changesRemoved(changes: MutableCollection<Change>?, fromList: ChangeList?) {
                        super.changesRemoved(changes, fromList)
                        updateGitInfo(project)
                    }

                    override fun changedFileStatusChanged() {
                        super.changedFileStatusChanged()
                        updateGitInfo(project)
                    }
                })
            }

            private fun updateGitInfo(project: Project) {
                val changeListManager = ChangeListManager.getInstance(project)
                val localChangeList = changeListManager.defaultChangeList

                if (localChangeList != null && localChangeList.changes.isNotEmpty()) {
                    val addedFiles = mutableListOf<String>()
                    val removedFiles = mutableListOf<String>()
                    val changedFiles = mutableSetOf<String>()
                    var linesAdded = 0
                    var linesRemoved = 0

                    for (change in localChangeList.changes) {
                        val beforeRevision = change.beforeRevision
                        val afterRevision = change.afterRevision

                        if (beforeRevision == null) {
                            // Adicionado
                            addedFiles.add(afterRevision?.file?.name ?: "")
                            linesAdded += afterRevision?.content?.lines()?.size ?: 0
                        } else if (afterRevision == null) {
                            // Removido
                            removedFiles.add(beforeRevision.file.name)
                            linesRemoved += beforeRevision.content?.lines()?.size ?: 0
                        } else {
                            // Alterado
                            changedFiles.add(afterRevision.file.name)
                            val afterLines = afterRevision.content?.lines() ?: emptyList()
                            val beforeLines = beforeRevision.content?.lines() ?: emptyList()


                            linesAdded += maxOf(afterLines.size - beforeLines.size, 0)
                            linesRemoved += maxOf(beforeLines.size - afterLines.size, 0)
                        }
                    }
                    val addedFilesCount = addedFiles.size
                    val removedFilesCount = removedFiles.size
                    val changedFilesCount = changedFiles.size

                    val changesText = buildChangesText(addedFiles, removedFiles, changedFilesCount, linesAdded, linesRemoved)

                    gitInfoLabel.text = "Git Changes: $changesText"
                    WindowManager.getInstance().getStatusBar(project)?.updateWidget(ID())
                } else {
                    gitInfoLabel.text = "No Git changes found"
                }
            }

            private fun buildChangesText(
                    addedFiles: List<String>,
                    removedFiles: List<String>,
                    changedFiles: Int,
                    linesAdded: Int,
                    linesRemoved: Int
            ): String {
                val addedFilesText = if (addedFiles.isNotEmpty()) "${addedFiles.size} file(s) added, " else ""
                val removedFilesText = if (removedFiles.isNotEmpty()) "${removedFiles.size} file(s) removed, " else ""
                val changedFilesText = if (changedFiles > 0) "$changedFiles file(s) changed, " else ""

                return "$addedFilesText$removedFilesText$changedFilesText$linesAdded insertion(s), $linesRemoved deletion(s)"
            }

            override fun ID(): String = "GitInfoStatusBar"

            override fun install(statusBar: StatusBar) {
            }

            override fun dispose() {
            }

            override fun getPresentation(): StatusBarWidget.WidgetPresentation =
                    object : StatusBarWidget.TextPresentation {
                        override fun getText(): String = gitInfoLabel.text

                        override fun getTooltipText(): String? = "Git Information"

                        override fun getClickConsumer(): Consumer<MouseEvent>? {
                            return null
                        }

                        override fun getAlignment(): Float = 0.0f // Ajuste o alinhamento se necessário
                    }

            override fun copy(): StatusBarWidget = this
        }
    }
}
