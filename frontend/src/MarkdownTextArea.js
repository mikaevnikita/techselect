import {PolymerElement, html} from '@polymer/polymer';
import '@polymer/marked-element/marked-element.js';

class MarkdownTextArea extends PolymerElement {
    static get template() {
        return html`
          <marked-element>
            <script type="text/markdown">
                Hello, **World**
            </script>
          </marked-element>`;
    }

    static get is() {
        return 'markdown-text-area';
    }
}
customElements.define(MarkdownTextArea.is, MarkdownTextArea);