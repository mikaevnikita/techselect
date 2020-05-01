package ru.stankin.mikaev.techselect.view.component;

import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;

/**
 * MarkdownTextArea.
 *
 * @author Nikita_Mikaev
 */
@Tag("markdown-text-area")
@NpmPackage(value = "@polymer/marked-element", version = "3.0.1")
@JsModule("./src/MarkdownTextArea.js")
public class MarkdownTextArea extends PolymerTemplate<MarkdownTextAreaModel> {

    public MarkdownTextArea(String markdown) {
        setId("template");
        getModel().setMarkdown(markdown);
    }
}
