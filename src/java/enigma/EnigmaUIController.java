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

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
//        Highlighter highlighter = new Highlighter();

        // Line numbers
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
        // Folding / Unfolding / Printing
 //       codeArea.setContextMenu( new DefaultContextMenu());

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
        //closeLanding();
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
       // closeLanding();
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

//    public void closeLanding() {
//        if (tabPane.getTabs().get(0).getId() == "welcome") {
//            tabPane.getTabs().remove(0);
//        }
//    }

    public void compile() throws IOException, InterruptedException {
        int index = tabPane.getSelectionModel().getSelectedIndex();
        String name = fileList.get(index).getName();
        String path = fileList.get(index).getParent();
        String nameOnly = "";
        String extension = "";


        boolean isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");

        if (name.lastIndexOf(".") != -1 && name.lastIndexOf(".") != 0)
        {
            extension = name.substring(name.lastIndexOf(".") + 1);
        }

        if (name.lastIndexOf(".") > 0) {
            nameOnly = name.substring(0,name.lastIndexOf("."));
        }
        else nameOnly = name;

        System.out.println("Names extracted");
        System.out.println(name);
        System.out.println(path);
        System.out.println(extension);
        System.out.println(nameOnly);
        System.out.println(isWindows);

        if (extension.equals("java")) {
            String[] commands1 = {"javac", name};
            String[] commands2 = {"java", nameOnly};

            ProcessBuilder builder1 = new ProcessBuilder(commands1);
            ProcessBuilder builder2 = new ProcessBuilder(commands2);
            builder1.directory(new File(path));
            builder2.directory(new File(path));
            builder1.start();

            Process process = builder2.start();

            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            int exitVal = process.waitFor();
            System.out.println("Success");
            System.out.println(output);
            System.out.println("Exit value" + exitVal);
        }

        else if (extension.equals("cpp")) {

//            String command1 = "cd " + path + "; ";
//            String command2 = "g++ " + path + "/" + name + " -o " + nameOnly;
//
//            System.out.println(command1 + command2);
//            Runtime.getRuntime().exec(command1 + command2);

            //String[] command = {"sh", "-c","g++", path + "/" + name, " - o", nameOnly};
            String[] command1 = {"g++", name, "-o", nameOnly};
            //String[] command2 = {"./", nameOnly};
            ProcessBuilder builder1 = new ProcessBuilder(command1);
            //ProcessBuilder builder2 = new ProcessBuilder(command2);

            System.out.println("First process done");

            builder1.directory(new File(path));
            //builder2.directory(new File(path));

            builder1.start();

            Process process = Runtime.getRuntime().exec(path + "/" + nameOnly);

            System.out.println("Final Process done");
//            Process process = null;
//            if (isWindows)
//                process = Runtime.getRuntime().exec(path + "/" + nameOnly + ".exe");
//            else
//                process = Runtime.getRuntime().exec(path + "/" + nameOnly);
//
//            System.out.println("process called");

            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;

            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }

            System.out.println("String built");
            int exitVal = process.waitFor();

            System.out.println(exitVal);
            System.out.println("Success");
            System.out.println(output);
            System.out.println(exitVal);
        }
    }
}