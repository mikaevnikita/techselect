package ru.stankin.mikaev.techselect.view.component;

import com.vaadin.flow.templatemodel.TemplateModel;

/**
 * MarkdownTextAreaModel.
 *
 * @author Nikita_Mikaev
 */
public interface MarkdownTextAreaModel extends TemplateModel {
    /**
     * Sets markdown.
     *
     * @param markdown
     *            markdown string
     */
    void setMarkdown(String markdown);
}