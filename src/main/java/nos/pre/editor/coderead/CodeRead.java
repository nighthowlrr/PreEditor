package nos.pre.editor.coderead;

import org.jetbrains.annotations.Contract;

import javax.swing.text.Document;

public class CodeRead {
    private final Document document;
    public Document getDocument() {
        return document;
    }

    @Contract(pure = true)
    public CodeRead(Document document) {
        this.document = document;
    }
}
