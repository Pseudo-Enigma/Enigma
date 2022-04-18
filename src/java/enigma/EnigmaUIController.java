package enigma;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import enigma.Highlighter;

public class EnigmaUIController {

    @FXML
    private TabPane tabPane;
    @FXML
    private Tab welcome;

    ArrayList<File> fileList = new ArrayList<>();

    public void addTab(ActionEvent e) {
        Tab tab = new Tab();
        tab.setText("Kimochi");


        CodeArea codeArea = new CodeArea();
        Highlighter highlighter = new Highlighter();

        // Line numbers
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        // Folding / Unfolding / Printing
        codeArea.setContextMenu( new DefaultContextMenu());

//        codeArea.getVisibleParagraphs().addModificationObserver
//                (
//                        new VisibleParagraphStyler<>( codeArea, this::computeHighlighting )
//                );

        // auto-indent: insert previous line's indents on enter
        final Pattern whiteSpace = Pattern.compile( "^\\s+" );
        codeArea.addEventHandler( KeyEvent.KEY_PRESSED, KE ->
        {
            if ( KE.getCode() == KeyCode.ENTER ) {
                int caretPosition = codeArea.getCaretPosition();
                int currentParagraph = codeArea.getCurrentParagraph();
                Matcher m0 = whiteSpace.matcher( codeArea.getParagraph( currentParagraph-1 ).getSegments().get( 0 ) );
                if ( m0.find() ) Platform.runLater( () -> codeArea.insertText( caretPosition, m0.group() ) );
            }
        });


        tab.setContent(codeArea);
        tabPane.getTabs().add(tab);
    }

    public void newFile(ActionEvent e)
    {
        FileChooser fileChooser = new FileChooser();
        //file = fileChooser.showOpenDialog(new Stage());
        fileList.add(fileChooser.showSaveDialog(new Stage()));

        Tab tab = new Tab();
        closeLanding();
        tab.setText(fileList.get(fileList.size() - 1).getName());

        tab.setOnCloseRequest(event -> {
            int index = tabPane.getSelectionModel().getSelectedIndex();
            fileList.remove(index);
        });

        CustomCodeArea codeArea = new CustomCodeArea();

        tab.setClosable(true);
        tab.setContent(codeArea);
        tabPane.getTabs().add(tab);
    }

    public void openFile(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        //file = fileChooser.showOpenDialog(new Stage());
        fileList.add(fileChooser.showOpenDialog(new Stage()));

        Tab tab = new Tab();
        closeLanding();
        tab.setText(fileList.get(fileList.size() - 1).getName());

        tab.setOnCloseRequest(event -> {
            int index = tabPane.getSelectionModel().getSelectedIndex();
            fileList.remove(index);
        });

        CustomCodeArea codeArea = new CustomCodeArea();

        try {
            Scanner scanner = new Scanner(fileList.get(fileList.size() - 1));
            while (scanner.hasNextLine()) {
                codeArea.appendText(scanner.nextLine() + "\n");
            }
        } catch (FileNotFoundException er) {
            er.printStackTrace();
        }

        tab.setClosable(true);
        tab.setContent(codeArea);
        tabPane.getTabs().add(tab);
    }

    public void saveFile(ActionEvent e) {
        int index = tabPane.getSelectionModel().getSelectedIndex();
        CustomCodeArea area = (CustomCodeArea) tabPane.getTabs().get(index).getContent();
        String content = area.getText();
        //System.out.println(content);
        try {
            PrintWriter printWriter = new PrintWriter(fileList.get(index));
            printWriter.write(content);
            printWriter.close();
        }
        catch (FileNotFoundException er) {
            er.printStackTrace();
        }
    }

    public void saveFileAs(ActionEvent e) {
        int index = tabPane.getSelectionModel().getSelectedIndex();
        CustomCodeArea area = (CustomCodeArea) tabPane.getTabs().get(index).getContent();
        String content = area.getText();

        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(new Stage());
        //System.out.println(content);
        try {
            PrintWriter printWriter = new PrintWriter(file);
            printWriter.write(content);
            printWriter.close();
        }
        catch (FileNotFoundException er) {
            er.printStackTrace();
        }
    }

    public void closeLanding() {
        if (tabPane.getTabs().get(0).getId() == "welcome") {
            tabPane.getTabs().remove(0);
        }
    }
}