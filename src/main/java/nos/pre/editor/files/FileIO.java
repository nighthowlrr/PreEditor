package nos.pre.editor.files;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class FileIO {
    public static boolean openFileToDocument(File fileToOpen, Document document) {
        try {
            Scanner scanner = new Scanner(fileToOpen);
            while (scanner.hasNextLine()) {
                document.insertString(document.getLength(), scanner.nextLine() + "\n", null);
            }

            // Remove the last "\n" character
            document.remove(document.getLength() - 1, 1);
            scanner.close();

            return true;
        } catch (FileNotFoundException | BadLocationException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean saveFile(File fileToSave, String text) {
        try {
            PrintWriter writer = new PrintWriter(fileToSave);
            writer.write(text);
            writer.close();

            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}