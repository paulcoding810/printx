package com.paulcoding.intellij.printx.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange

/**
 * Action that inserts a println statement in the format: println("🚀 ~ x: $x")
 * Triggered by Ctrl+Option+L keyboard shortcut
 */
class PrintSelectedTextAction : AnAction() {

    override fun actionPerformed(e: AnActionEvent) {
        val editor: Editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val project = e.project ?: return
        val document = editor.document

        val selectedText = editor.selectionModel.selectedText
        val caretModel = editor.caretModel

        // Determine the variable name to use
        val varName = selectedText ?: "x"

        // Create the println statement
        val printStatement = "println(\"🚀 ~ $varName: \$$varName\")\n"

        // Get the current line and calculate proper indentation
        val caretOffset = caretModel.offset
        val lineNumber = document.getLineNumber(caretOffset)
        val lineStartOffset = document.getLineStartOffset(lineNumber)
        val lineEndOffset = document.getLineEndOffset(lineNumber)
        val currentLine = document.getText(TextRange(lineStartOffset, lineEndOffset))

        // Extract indentation from current line
        val indentation = currentLine.takeWhile { it.isWhitespace() }

        // Insert the println statement at the end of the current line
        WriteCommandAction.runWriteCommandAction(project) {
            document.insertString(lineEndOffset, "\n$indentation$printStatement")
        }
    }

    override fun update(e: AnActionEvent) {
        // Make the action available only when an editor is present
        val editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabled = editor != null
    }
}
