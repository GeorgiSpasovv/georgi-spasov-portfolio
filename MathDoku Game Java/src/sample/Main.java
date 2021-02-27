package sample;

import javafx.animation.*;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import java.io.*;
import java.util.*;
import java.util.function.UnaryOperator;

public class Main extends Application {


    public int n = 0;
    BorderPane root = new BorderPane();
    Scene scene;

    private double height;
    private double width;
    private TextField [][]textFields;
    private Pane[] panes = new Pane[50];
    private ArrayList<Integer>[] cells;
    private int [][] textNumbers;
    private ArrayList<Pane> mistakePanes = new ArrayList<>();


    private Stack<TextField> undoStack = new Stack ();
    private Stack<TextField> redoStack = new Stack ();
    private HashMap<TextField, Stack<Integer>> mapUndoStack = new HashMap<> ();
    private HashMap<TextField, Stack<Integer>> mapRedoStack = new HashMap<> ();

    Button button1 = new Button("Undo");
    Button button2 = new Button("Redo");
    Button button3 = new Button("Clear");
    Button button4 = new Button("Load");
    Button button5 = new Button("Load from Text");
    CheckBox button6 = new CheckBox("Show mistakes");

    private BufferedReader reader;
    private BufferedReader reader2;
    private double fontSizeDivider = 5;
    List<Boolean> check = new ArrayList<>();
    List<Boolean> check2 = new ArrayList<>();
    List<Boolean> check3 = new ArrayList<>();
    ArrayList<ArrayList> mistakes1 = new ArrayList<>();
    ArrayList<ArrayList> mistakes2 = new ArrayList<>();


    //Setting th size of the grid
    private void setN(int g)
    {
        n=g;
    }

    //Finding the last focused field
    private static class FocusData {

        private TextField textField = null;

        public TextField getTextField() {
            return textField;
        }

        public void setFocusedNode(Node node) {
            this.textField = node instanceof TextField ? (TextField) node : null;
        }

        public boolean isTextField() {
            return textField != null;
        }

    }

    //Changing the font
    public void changeVariableS()
    {
        fontSizeDivider = 6;
    }
    public void changeVariableL()
    {
        fontSizeDivider = 3.5;
    }
    public void changeVariableM()
    {
        fontSizeDivider = 5;
    }


    //.......................................Mistakes
    //Marking the wrong rows
    public void checkMistakesRow()
    {

        ArrayList<Integer> data = new ArrayList<>();

        for(int i=0; i<check.size(); i++)
        {
            int place;
            int i1=0;

            if(!check.get(i))
            {
                i1= i;


                for(int j=1; j<=n; j++)
                {
                    place = i1*n+j;

                    panes[place].setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                    data.add(place);
                }
                mistakes1.add(data);

            }

        }


    }

    //Marking the wrong columns
    public void checkMistakesCol()
    {

        ArrayList<Integer> data = new ArrayList<>();


        for(int i=0; i<check2.size(); i++)
        {
            int place;
            int i1=0;
            int j1=0;


            if(!check2.get(i))
            {
                i1= i;


                for(int j=0; j<n*n; j=j+n)
                {
                    place = i1+1+j;

                    panes[place].setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                    data.add(place);
                }
                mistakes2.add(data);

            }


        }



    }

    //Marking the wrong cages
    public void checkMistakesCages(ArrayList <ArrayList<Integer>> labelsCells)
    {
        for(int i=0; i<check3.size(); i++)
        {
            if(!check3.get(i))
            {
                for(int j=0; j<labelsCells.get(i).size(); j++)
                {
                    int place = labelsCells.get(i).get(j);
                    panes[place].setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                    mistakePanes.add(panes[place]);
                }
            }
        }
    }

    //Unlabeling the mistakes
    public void uncheckMistakes()
    {

        for(int i=0; i<mistakes1.size(); i++)
        {

            ArrayList<Integer> data = mistakes1.get(i);

            for(int j=0; j<mistakes1.get(i).size(); j++)
            {
                //panes[data.get(j)].setStyle(substyle.get(j));
                panes[data.get(j)].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));

            }
        }
        mistakes1.clear();



        for(int i=0; i<mistakes2.size(); i++)
        {
            ArrayList<Integer> data = mistakes2.get(i);

            for(int j=0; j<mistakes2.get(i).size(); j++)
            {
                //panes[data.get(j)].setStyle(substyle.get(j));
                panes[data.get(j)].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
            }
        }
        mistakes1.clear();

        for(Pane p : mistakePanes)
        {
            p.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
        }
        mistakePanes.clear();



    }
    //.................................................


    //.......................................Results
    //Checking if the gamefield is full
    public boolean checkIfFull(int [][] textNumbers, int n)
    {
        for(int i=0; i<n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                if(textNumbers[i][j] == 0) return false;
            }
        }
        return true;
    }

    //Checking if all elements are equal in a list
    public boolean allElEqual(List list) {
        return new HashSet<String>(list).size() <= 1;
    }

    //Checking if the results are correct
    public void checkResults(int [][] textNumbers, int n, ArrayList <Label> labels, ArrayList <ArrayList<Integer>> labelsCells)
    {
        check.clear();
        check2.clear();
        check3.clear();


        for(int i=0; i<n; i++)
        {
            int sum=0;
            ArrayList<Integer> a = new ArrayList<>();
            for(int j=0; j<n; j++)
            {
                if(!a.contains(textNumbers[i][j]))
                {
                    sum = sum+ textNumbers[i][j];
                    a.add(textNumbers[i][j]);
                }
            }
            if(sum == n*(n+1)/2)
            {
                check.add(true);
            }
            else check.add(false);

        }

        for(int j=0; j<n; j++)
        {
            int sum=0;
            ArrayList<Integer> a = new ArrayList<>();
            for(int i=0; i<n; i++)
            {
                if(!a.contains(textNumbers[i][j]))
                {
                    sum = sum+ textNumbers[i][j];
                    a.add(textNumbers[i][j]);
                }
            }
            if(sum == n*(n+1)/2)
            {
                check2.add(true);
            }
            else check2.add(false);
        }

        //Checking the cages.
        for(int a=0; a<labels.size(); a++)
        {
            char sign = readLabelSign(labels.get(a));
            int number = readLabelNumber(labels.get(a));

            if(sign == '+')
            {
                int sum = 0;
                for(int t : labelsCells.get(a))
                {

                    sum = sum + textNumbers[getCoordinatesI(n, t)][getCoordinatesJ(n, t)];
                }
                if(sum == number)
                {
                    check3.add(true);
                }
                else
                {
                    check3.add(false);
                }

            }

            if(sign == 'x')
            {
                int sum = 1;
                for(int t : labelsCells.get(a))
                {

                    sum = sum * textNumbers[getCoordinatesI(n, t)][getCoordinatesJ(n, t)];
                }
                if(sum == number)
                {
                    check3.add(true);
                }
                else
                {
                    check3.add(false);
                }
            }

            if(sign == '-')
            {
                int sum =0;
                ArrayList<Integer> numbers = new ArrayList<>();
                for(int t : labelsCells.get(a))
                {

                    //sum = sum * textNumbers[getCoordinatesI(n, t)][getCoordinatesJ(n, t)];
                    numbers.add(textNumbers[getCoordinatesI(n, t)][getCoordinatesJ(n, t)]);
                }

                sum = Collections.max(numbers);

                numbers.remove(numbers.indexOf(sum));
                Collections.sort(numbers);
                Collections.reverse(numbers);

                for(int g : numbers)
                {
                    sum = sum - g;
                }

                if(sum == number)
                {
                    check3.add(true);
                }
                else
                {
                    check3.add(false);
                }

            }

            if(sign == '/')
            {
                int sum =0;
                ArrayList<Integer> numbers = new ArrayList<>();
                for(int t : labelsCells.get(a))
                {

                    //sum = sum * textNumbers[getCoordinatesI(n, t)][getCoordinatesJ(n, t)];
                    numbers.add(textNumbers[getCoordinatesI(n, t)][getCoordinatesJ(n, t)]);
                }

                sum = Collections.max(numbers);

                numbers.remove(numbers.indexOf(sum));
                Collections.sort(numbers);
                Collections.reverse(numbers);

                for(int g : numbers)
                {
                    sum = sum/g;
                }

                if(sum == number)
                {
                    check3.add(true);
                }
                else
                {
                    check3.add(false);
                }
            }
            if(sign == '0')
            {

                int number1 = textNumbers[getCoordinatesI(n, labelsCells.get(a).get(0))][getCoordinatesJ(n, labelsCells.get(a).get(0))];
                if(number1 == number)
                {
                    check3.add(true);
                }
                else
                {
                    check3.add(false);
                }
            }

        }

        if(check.get(0) && check2.get(0) && check3.get(0))
        {
            if(allElEqual(check) && allElEqual(check2) && allElEqual(check3))
            {
                System.out.println("You WINNNNNNNNNNNNNNNNNN");
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "You WIN!!!");

                button6.setDisable(true);

                final Animation animation = new Transition() {

                    {
                        setCycleDuration(Duration.millis(1000));
                        setInterpolator(Interpolator.EASE_BOTH);
                    }

                    @Override
                    protected void interpolate(double frac) {
                        Color vColor = new Color(1, 1, 0, 1 - frac);
                        root.setBackground(new Background(new BackgroundFill(vColor, CornerRadii.EMPTY, Insets.EMPTY)));
                    }
                };

                    animation.setCycleCount(1000);
                    animation.play();
                    Optional<ButtonType> result = alert.showAndWait();
                    animation.stop();
                    root.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

            }
        }

    }
    //Transforming number to coordinate
    public int getCoordinatesI(int n, int t)
    {
        int i = 0;

        if(n>=t)
        {
            i = 0;
        }
        else
        {
            if(t%n == 0)
            {
                i = t/n-1;
            }
            else i = t/n;
        }
        return i;

    }

    //Transforming number to coordinate
    public int getCoordinatesJ(int n, int t)
    {
        int j=0;
        if(n>=t)
        {
            j = t-1;
        }
        else
        {
            if(t%n == 0)
            {
                j=n-1;
            }
            else j=t%n-1;
        }
        return j;

    }

    //Returning the label math sign
    public char readLabelSign(Label label)
    {
        String text = label.getText();
        if(text.length() == 1)
            return '0';
        else
            return text.charAt(text.length()-1);
    }

    //Returning the label number
    public Integer readLabelNumber(Label label)
    {
        String text = label.getText();
        if(text.length() == 1)
        {
            return Integer.parseInt(text);
        }
        else
        {
            StringBuilder sb = new StringBuilder(text);
            sb.deleteCharAt(text.length()-1);

            return Integer.parseInt(sb.toString());
        }

    }
    //.................................................


    //.......................................Create Grid
    //Making the cages
    private void createCages(int cell1, int cell2, ArrayList<Integer>[] cells, Pane [] panes, ArrayList <ArrayList<Integer>> labelsCells)
    {

        if(cell2 - cell1 == 1)
        {
            // for right cell
            if (cells[cell1].isEmpty ())
            {
                panes[cell1].setStyle ("-fx-border-style: solid dashed solid solid;" + "-fx-border-width: 2 3 2 2");
                panes[cell2].setStyle ("-fx-border-style: solid solid solid dashed;" + "-fx-border-width: 2 2 2 3");
                cells[cell1].add (2);
                cells[cell2].add (4);
            }

            if (cells[cell1].size () == 1)
            {
                // for right cell with previous down
                if (cells[cell1].get (0) == 3)
                {
                    panes[cell1].setStyle ("-fx-border-style: solid dashed dashed solid;" + "-fx-border-width: 2 3 3 2");
                    panes[cell2].setStyle ("-fx-border-style: solid solid solid dashed;" + "-fx-border-width: 2 2 2 3");
                    cells[cell1].add (2);
                    cells[cell2].add (4);
                }

                // for right cell with previous up
                if (cells[cell1].get (0) == 1)
                {
                    if(cells[cell2].size ()!= 0)
                    {
                        if(cells[cell2].get (0) == 1)
                        {
                            panes[cell1].setStyle ("-fx-border-style: dashed dashed solid solid;" + "-fx-border-width: 3 3 2 2");
                            panes[cell2].setStyle ("-fx-border-style: dashed solid solid dashed;" + "-fx-border-width: 3 2 2 3");
                            cells[cell1].add (2);
                            cells[cell2].add (4);
                        }
                    }
                    else
                    {
                        panes[cell1].setStyle ("-fx-border-style: dashed dashed solid solid;" + "-fx-border-width: 3 3 2 2");
                        panes[cell2].setStyle ("-fx-border-style: solid solid solid dashed;" + "-fx-border-width: 2 2 2 3");
                        cells[cell1].add (2);
                        cells[cell2].add (4);
                    }

                }

                // for right cell with previous left
                if (cells[cell1].get (0) == 4)
                {
                    panes[cell1].setStyle ("-fx-border-style: solid dashed solid dashed;" + "-fx-border-width: 2 3 2 3");
                    panes[cell2].setStyle ("-fx-border-style: solid solid solid dashed;" + "-fx-border-width: 2 2 2 3");
                    cells[cell1].add (2);
                    cells[cell2].add (4);
                }
            }
            else if (cells[cell1].size () == 2)
            {
                // for right cell with previous down and up
                if ((cells[cell1].get (0) == 1 && cells[cell1].get (1) == 3) || (cells[cell1].get (0) == 3 && cells[cell1].get (1) == 1))
                {
                    panes[cell1].setStyle ("-fx-border-style: dashed dashed dashed solid;" + "-fx-border-width: 3 3 3 2");
                    panes[cell2].setStyle ("-fx-border-style: solid solid solid dashed;" + "-fx-border-width: 2 2 2 3");
                    cells[cell1].add (2);
                    cells[cell2].add (4);
                }

                // for right cell with previous down and left
                if ((cells[cell1].get (0) == 4 && cells[cell1].get (1) == 3) || (cells[cell1].get (0) == 3 && cells[cell1].get (1) == 4))
                {
                    panes[cell1].setStyle ("-fx-border-style: solid dashed dashed dashed;" + "-fx-border-width: 2 3 3 3");
                    panes[cell2].setStyle ("-fx-border-style: solid solid solid dashed;" + "-fx-border-width: 2 2 2 3");
                    cells[cell1].add (2);
                    cells[cell2].add (4);
                }

                // for right cell with previous up and left
                if ((cells[cell1].get (0) == 1 && cells[cell1].get (1) == 4) || (cells[cell1].get (0) == 4 && cells[cell1].get (1) == 1))
                {
                    panes[cell1].setStyle ("-fx-border-style: dashed dashed solid dashed;" + "-fx-border-width: 3 3 2 3");
                    panes[cell2].setStyle ("-fx-border-style: solid solid solid dashed;" + "-fx-border-width: 2 2 2 3");
                    cells[cell1].add (2);
                    cells[cell2].add (4);
                }
            }
            else  // for right cell with all previous
            {
                panes[cell1].setStyle ("-fx-border-style: dashed dashed dashed dashed;" + "-fx-border-width: 3 3 3 3");
                panes[cell2].setStyle ("-fx-border-style: solid solid solid dashed;" + "-fx-border-width: 2 2 2 3");
                cells[cell1].add (2);
                cells[cell2].add (4);
            }
        }


        if(cell2 - cell1 == getN(labelsCells))
        {
            // for down cell
            if (cells[cell1].size () == 0)
            {
                panes[cell1].setStyle ("-fx-border-style: solid solid dashed solid;" + "-fx-border-width: 2 2 3 2");
                panes[cell2].setStyle ("-fx-border-style: dashed solid solid solid;" + "-fx-border-width: 3 2 2 2");
                cells[cell1].add (3);
                cells[cell2].add (1);
            }

            if (cells[cell1].size () == 1)
            {
                // for down cell with previous right
                if (cells[cell1].get (0) == 2)
                {
                    panes[cell1].setStyle ("-fx-border-style: solid dashed dashed solid;" + "-fx-border-width: 2 3 3 2");
                    panes[cell2].setStyle ("-fx-border-style: dashed solid solid solid;" + "-fx-border-width: 3 2 2 2");
                    cells[cell1].add (3);
                    cells[cell2].add (1);
                }

                // for down cell with previous up
                if (cells[cell1].get (0) == 1)
                {
                    panes[cell1].setStyle ("-fx-border-style: dashed solid dashed solid;" + "-fx-border-width: 3 2 3 2");
                    panes[cell2].setStyle ("-fx-border-style: dashed solid solid solid;" + "-fx-border-width: 3 2 2 2");
                    cells[cell1].add (3);
                    cells[cell2].add (1);
                }

                // for down cell with previous left
                if (cells[cell1].get (0) == 4)
                {
                    panes[cell1].setStyle ("-fx-border-style: solid solid dashed dashed;" + "-fx-border-width: 2 2 3 3");
                    panes[cell2].setStyle ("-fx-border-style: dashed solid solid solid;" + "-fx-border-width: 3 2 2 2");
                    cells[cell1].add (3);
                    cells[cell2].add (1);
                }
            }
            else if (cells[cell1].size () == 2)
            {
                // for down cell with previous right and up
                if ((cells[cell1].get (0) == 1 && cells[cell1].get (1) == 2) || (cells[cell1].get (0) == 2 && cells[cell1].get (1) == 1))
                {
                    panes[cell1].setStyle ("-fx-border-style: dashed dashed dashed solid;" + "-fx-border-width: 3 3 3 2");
                    panes[cell2].setStyle ("-fx-border-style: dashed solid solid solid;" + "-fx-border-width: 3 2 2 2");
                    cells[cell1].add (3);
                    cells[cell2].add (1);
                }

                // for down cell with previous right and left
                if ((cells[cell1].get (0) == 4 && cells[cell1].get (1) == 2) || (cells[cell1].get (0) == 2 && cells[cell1].get (1) == 4))
                {
                    panes[cell1].setStyle ("-fx-border-style: solid dashed dashed dashed;" + "-fx-border-width: 2 3 3 3");
                    panes[cell2].setStyle ("-fx-border-style: dashed solid solid solid;" + "-fx-border-width: 3 2 2 2");
                    cells[cell1].add (3);
                    cells[cell2].add (1);
                }

                // for down cell with previous up and left
                if ((cells[cell1].get (0) == 4 && cells[cell1].get (1) == 1) || (cells[cell1].get (0) == 1 && cells[cell1].get (1) == 4))
                {
                    panes[cell1].setStyle ("-fx-border-style: dashed solid dashed dashed;" + "-fx-border-width: 3 2 3 3");
                    panes[cell2].setStyle ("-fx-border-style: dashed solid solid solid;" + "-fx-border-width: 3 2 2 2");
                    cells[cell1].add (3);
                    cells[cell2].add (1);
                }
            }
            else   // for down cell with all previous
            {
                panes[cell1].setStyle ("-fx-border-style: dashed dashed dashed dashed;" + "-fx-border-width: 3 3 3 3");
                panes[cell2].setStyle ("-fx-border-style: dashed solid solid solid;" + "-fx-border-width: 3 2 2 2");
                cells[cell1].add (3);
                cells[cell2].add (1);
            }
        }

    }

    //Finding the size of the grid from the text input
    private int getN(ArrayList <ArrayList<Integer>> labelsCells)
    {
        ArrayList<Integer> bigNumbers = new ArrayList<>();

        for(ArrayList<Integer> a : labelsCells)
        {
                bigNumbers.add(Collections.max(a));
        }
        return (int)Math.round(Math.sqrt(Collections.max(bigNumbers)));
    }

    //Creating the play area
    private void createGrid(Pane[] panes, ArrayList<Integer>[] cells, int [][] textNumbers, ArrayList <Label> labels, ArrayList <ArrayList<Integer>> labelsCells,TextField [][]textFields, int n)
    {
        GridPane game  = new GridPane();

        game.setPadding(new Insets(60));
        game.setAlignment(Pos.CENTER);
        game.setMaxSize(1000, 1000);
        int paneNumber = 1;

        for(int i=0; i<n; i++)
        {
            for(int j=0; j<n; j++)
            {
                //Creating the panes with borders
                StackPane pane = new StackPane();
                pane.setStyle("-fx-border-style: solid solid solid solid; -fx-border-width: 2;");
                panes[paneNumber] = pane;
                cells[paneNumber] = new ArrayList<>();
                paneNumber++;
                pane.setMinSize(60, 60);
                pane.setPrefSize(70, 70);
                pane.setMaxHeight(Double.MAX_VALUE);
                pane.setMaxWidth(Double.MAX_VALUE);
                GridPane.setHgrow(pane, Priority.ALWAYS);
                GridPane.setVgrow(pane, Priority.ALWAYS);


                //Creating the textFields and adding filters to them
                TextField textField = new TextField();
                textField.setStyle("-fx-background-color:transparent;");

                // converter that converts text to Integers, and vice-versa:
                StringConverter<Integer> stringConverter = new StringConverter<Integer>() {

                    @Override
                    public String toString(Integer object) {
                        if (object == null || object.intValue() == 0) {
                            return "";
                        }
                        return object.toString() ;
                    }

                    @Override
                    public Integer fromString(String string) {
                        if (string == null || string.isEmpty()) {
                            return 0 ;
                        }
                        return Integer.parseInt(string);
                    }

                };

                // filter only allows digits, and ensures only one digit the text field:
                UnaryOperator<TextFormatter.Change> textFilter = c -> {

                    // if text is a single digit, replace current text with it:
                    if (c.getText().matches("[0-9]")) {
                        c.setRange(0, textField.getText().length());
                        return c ;
                    } else
                        // if not adding any text (delete or selection change), accept as is
                        if (c.getText().isEmpty()) {
                            return c ;
                        }
                    // otherwise veto change
                    return null ;
                };

                TextFormatter<Integer> formatter = new TextFormatter<Integer>(stringConverter, 0, textFilter);


                formatter.valueProperty().addListener((obs, oldValue, newValue) -> {
                    // whatever you need to do here when the actual value changes:
                    int old = oldValue.intValue();
                    int updated = newValue.intValue();


                });

                final int i1 = i;
                final int j1 = j;
                textField.textProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {

                        if(!t1.equals(""))
                        {
                            textNumbers[i1][j1] = Integer.parseInt(t1);

                            undoStack.add (textField);
                            mapUndoStack.computeIfAbsent (textField, k -> new Stack());
                            mapUndoStack.get (textField).add (Integer.parseInt (textField.getText ()));
                            button1.setDisable (false);

                            if(checkIfFull(textNumbers, n))
                            {
                                System.out.println("Full");
                                button6.setDisable(false);

                                checkResults(textNumbers, n, labels, labelsCells);
                            }
                        }

                    }
                });


                //Formatting the textfield
                textField.setTextFormatter(formatter);
                StackPane.setAlignment(textField, Pos.BOTTOM_CENTER);
                textField.setAlignment(Pos.CENTER);
                textFields[i][j] = textField;


                //Dynamically resizing the textFields
                textField.setPrefSize(pane.getWidth(), pane.getHeight() /2);
                textField.setMaxSize(pane.getWidth(), pane.getHeight() /2);
                textField.setFont(Font.font("", pane.getHeight()/2/2));
                //textField.setFont(Font.font("Arial", pane.getHeight() /fontSizeDivider));


                //Height resizing
                pane.heightProperty().addListener((observableValue, number, t1) -> {
                    Double height = (Double) t1;
                    textField.setPrefHeight(height/2);
                    textField.setMaxHeight(height/2);
                    textField.setFont(Font.font("Arial", pane.getHeight() /fontSizeDivider));

                    for(Label l : labels)
                    {
                        l.setFont(Font.font("Arial", pane.getHeight() / fontSizeDivider));
                    }
                });

                //Width resizing
                pane.widthProperty().addListener((observableValue, number, t1) -> {
                    Double width = (Double) t1;
                    textField.setPrefWidth(width);
                    textField.setMaxWidth(width);
                });


                textField.setVisible(false);
                pane.getChildren().addAll(textField);
                game.add(pane, j, i);


                //Showing the textFields upon click
                pane.setOnMouseClicked(mouseEvent -> {

                    textField.setVisible(true);
                    textField.requestFocus();
                });

            }
        }
        root.setCenter(game);
        height = panes[1].getPrefHeight()*n+200;
        width = panes[1].getPrefWidth()*n+400;


    }
    //.................................................


    //Redo logic
    public void redo()
    {
        TextField textField = redoStack.pop ();
        int number = mapRedoStack.get (textField).pop ();
        textField.setText (Integer.toString (number));
        undoStack.add (textField);
        mapUndoStack.computeIfAbsent (textField, k -> new Stack());
        mapUndoStack.get (textField).add (number);
        button1.setDisable (false);

        if (redoStack.isEmpty ()) button2.setDisable (true);
        if (mapRedoStack.isEmpty ())button2.setDisable (true);
    }

    //Undo logic
    public void undo()
    {
        TextField textField = undoStack.pop ();
        int number = mapUndoStack.get (textField).pop ();
        if(mapUndoStack.get (textField).isEmpty ()) textField.setText ("");
        else
        {
            int nextNumber = mapUndoStack.get (textField).peek ();
            textField.setText (Integer.toString (nextNumber));
        }
        redoStack.add (textField);
        mapRedoStack.computeIfAbsent (textField, k -> new Stack());
        mapRedoStack.get (textField).add (number);
        button2.setDisable (false);

        if (undoStack.isEmpty ()) button1.setDisable (true);
        if (mapUndoStack.isEmpty ())button1.setDisable (true);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        final FocusData focusData = new FocusData();
        ArrayList <Label> labels= new ArrayList<>();

        //Number pad variables
        GridPane numbers = new GridPane();
        VBox vbox = new VBox();
        Button [][]numberButtons = new Button[3][3];

        //Load button variables
        final FileChooser fileChooser = new FileChooser();
        ArrayList <ArrayList<Integer>> labelsCells= new ArrayList<>();

        //Creating the top bar
        button1.setDisable (true);
        button2.setDisable (true);
        button6.setDisable(true);
        button6.setPadding(new Insets(0, 0, 0, 0));
        String [] st = {"Small", "Medium", "Large"};
        ChoiceBox choiceBox = new ChoiceBox(FXCollections.observableArrayList(st));

        Text text = new Text("MathDoku");
        text.setStyle("-fx-fill: linear-gradient(from 0% 0% to 100% 200%, repeat, purple 50%, green 50%);-fx-stroke: black;-fx-stroke-width: 0.1;");
        //Label label1 = new Label(text);

        Label label3 = new Label("Font size");
        //label1.setPadding(new Insets(0, 0, 0, 0));
        label3.setPadding(new Insets(0, 0, 0, 0));

        ToolBar toolBar = new ToolBar();
        toolBar.getItems().addAll(text, new Separator(), button1, button2, button3, button4, button5, button6, choiceBox, label3);

        //Creating the Number Pad
        int p = 1;
        numbers.setAlignment(Pos.CENTER);
        numbers.setHgap(5);
        numbers.setVgap(5);
        numbers.setMaxSize(500, 500);

        for(int i=0; i<3; i++)
        {
            for(int j=0; j<3; j++)
            {
                Button button = new Button(Integer.toString(p));
                button.setMinSize(60, 60);
                button.setPrefSize(60, 60);
                button.setMaxHeight(Double.MAX_VALUE);
                button.setMaxWidth(Double.MAX_VALUE);
                GridPane.setHgrow(button, Priority.ALWAYS);
                //GridPane.setVgrow(button, Priority.ALWAYS);

                numbers.add(button, j, i);
                numberButtons[j][i] = button;

                button.setOnAction(actionEvent -> {
                    if(focusData.isTextField())
                    {
                        TextField focusText = focusData.getTextField();
                        focusText.setText(button.getText());


                        undoStack.add (focusText);
                        mapUndoStack.computeIfAbsent (focusText, k -> new Stack());
                        mapUndoStack.get (focusText).add (Integer.parseInt (focusText.getText ()));
                        button1.setDisable (false);

                    }
                });
                p++;
            }
        }

        //Styling the number pad
        numbers.setPadding(new Insets(10, 50, 10, 10));
        Label label2 = new Label("Number Pad");
        label2.setAlignment(Pos.CENTER);
        label2.setPadding(new Insets(0, 50, 10, 10));
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(label2, numbers);




        //Undo action
        button1.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            try
            {
                undo();
            }
            catch (Exception e)
            {
                button1.setDisable(true);
                System.out.println("Enough");
            }

        }
    });

        //Redo action
        button2.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent actionEvent) {
            try
            {
                redo();
            }
            catch (Exception e)
            {
                button2.setDisable(true);
                System.out.println("Enough");
            }

        }
    });


        //Clear button logic
        button3.setOnAction(actionEvent -> {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Are you sure you want to clear the board?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                for(int i=0; i<n; i++)
                {
                    for(int j=0; j<n; j++)
                    {
                        textFields[i][j].setText("");
                        textFields[i][j].setVisible(false);
                        textNumbers[i][j] = 0;

                    }
                }
            }
            button1.setDisable (true);
            button2.setDisable (true);
            button6.setDisable(true);
            while (! undoStack.isEmpty ()) mapUndoStack.get (undoStack.pop ()).empty ();

        });


        //Loading a game from a file
        //Load button action
        button4.setOnAction(new EventHandler<>()
        {


            //Putting txt filter on the fileChooser
            private void configureFileChooser(
                    final FileChooser fileChooser)
            {
                fileChooser.setTitle("View Pictures");
                fileChooser.setInitialDirectory(
                        new File(System.getProperty("user.home"))
                );
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("TXT", "*.txt")
                );
            }

            //Deleting objects loaded previously
            private void deleteOld()
            {
                labels.clear();
                labelsCells.clear();
                //cells.clear();

                for(int i=1; i<=n*n; i++)
                {
                    cells[i].clear();
                }

                for(int i=1; i<panes.length; i++)
                {

                    panes[i].getChildren().remove(panes[i].lookup(".label"));
                    panes[i].setStyle("-fx-border-style: solid solid solid solid; -fx-border-width: 1;");
                }
            }

            //Creating the cages
            private void createCages(ArrayList <ArrayList<Integer>> labelsCells)
            {

                for(ArrayList<Integer> coordinates : labelsCells)
                {
                    for(int i=0; i< coordinates.size(); i++)
                    {
                        if(i< coordinates.size()-1)
                        {
                            for(int j=i+1; j< coordinates.size(); j++)
                            {
                                Main.this.createCages(coordinates.get(i), coordinates.get(j), cells, panes, labelsCells);
                            }

                        }

                    }
                }
            }


            ArrayList<String>LabelsNames = new ArrayList<>();

            private void createLabels(ArrayList <ArrayList<Integer>> labelsCells)
            {
                for(int i=0; i<labelsCells.size(); i++)
                {
                    Label label = new Label(LabelsNames.get(i));
                    label.setFont(Font.font("Arial", 17.5));
                    StackPane.setAlignment(label, Pos.TOP_LEFT);
                    label.setPadding(new Insets(5, 0, 0, 5));

                    labels.add(label);
                    panes[labelsCells.get(i).get(0)].getChildren().add(label);
                }
            }

            //Reading from the file;
            private void readFile(ArrayList<Integer> allcoordinates)
            {
                try {

                    while (reader.ready()) {

                        String line2 = reader.readLine();
                        if(line2==null) break;
                        String[] lineVector2 = line2.split(" ");
                        LabelsNames.add(lineVector2[0]);

                        ArrayList<Integer> coordinates = new ArrayList();


                        String line = lineVector2[1];

                        if (line.length() > 2) {
                            String[] lineVector = line.split(",");
                            for (int i = 0; i < lineVector.length; i++) {
                                coordinates.add(Integer.parseInt(lineVector[i]));
                                allcoordinates.add(Integer.parseInt(lineVector[i]));
                            }
                        } else
                        {
                            coordinates.add(Integer.parseInt(line));
                            allcoordinates.add(Integer.parseInt(line));
                        }


                        labelsCells.add(coordinates);




                    }
                }
                catch (Exception e)
                {
                    System.out.println("Error in the file! Make sure the file is in the right format and does not have missing information.");
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Error in the file!. Please check the format and try again.");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.isPresent() && result.get() == ButtonType.OK) {
                        labelsCells.clear();
                        allcoordinates.clear();
                        LabelsNames.clear();
                    }
                }

                //System.out.println(readLabelNumber(labels.get(0)));
               // System.out.println(readLabelSign(labels.get(0)));
            }

            //Validates if the cells are adjacent
            private  boolean validate(ArrayList <ArrayList<Integer>> labelsCells)
            {
                for(ArrayList<Integer> a : labelsCells)
                {
                    if(a.size()>2)
                    {
                        ArrayList<Integer> check = new ArrayList<>();
                        for(int i=0; i<a.size(); i++)
                        {
                            for(int j=i+1; j<a.size(); j++)
                            {


                                if(a.get(i)+1 == a.get(j) )
                                {
                                    System.out.println("Ok");
                                    check.add(1);
                                }

                                if(a.get(i)+n == a.get(j))
                                {
                                    System.out.println("Ok");
                                    check.add(1);
                                }



                            }
                        }

                        if(a.size()-1 > check.size()) return false;
                    }
                    else
                    {
                        for(int i=0; i<a.size(); i++)
                        {
                            for(int j=i+1; j<a.size(); j++) {
                                if(a.get(i)+1 == a.get(j) || a.get(i)+n == a.get(j))
                                {
                                    System.out.println("Ok");
                                }
                                else return false;
                            }

                            }

                    }

                }
                return true;
            }

            //Handling the action
            @Override
            public void handle(ActionEvent actionEvent)
            {
                //Opening the file chooser and setting the file
                configureFileChooser(fileChooser);
                File file = fileChooser.showOpenDialog(primaryStage);
                ArrayList<Integer> allcoordinates = new ArrayList<>();

                if (file != null)
                {
                    FileReader fr = null;

                    try
                    {
                        fr = new FileReader(file);
                    }
                    catch (FileNotFoundException e)
                    {
                        e.printStackTrace();
                    }


                    //Checking if another file was loaded previously
                    if(labelsCells.size()>0)
                    {
                        deleteOld();
                    }

                    reader = new BufferedReader(fr);
                    readFile(allcoordinates);
                    Set <Integer> set=new HashSet<>(allcoordinates);
                    int g = getN(labelsCells);
                    setN(g);

                    if(set.size()==allcoordinates.size() && validate(labelsCells))
                    {

                        textFields = new TextField[n][n];
                        panes= new Pane[n*n+1];
                        cells = new ArrayList[(n+1)*(n+1)];
                        textNumbers = new int [n][n];

                        createGrid(panes, cells, textNumbers, labels, labelsCells, textFields, g);
                        createLabels(labelsCells);
                        createCages(labelsCells);

                        primaryStage.setMinHeight(height);
                        primaryStage.setMinWidth(width);
                        primaryStage.setHeight(height);
                        primaryStage.setWidth(width);



                    }
                    else {


                        Alert alert = new Alert(Alert.AlertType.ERROR,
                                "Error in the file!. Please check the format and try again.");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            labelsCells.clear();
                            allcoordinates.clear();
                            LabelsNames.clear();
                        }
                    }

                }

            }
        });

        //Loading from text
        button5.setOnAction(actionEvent -> {
            VBox vbox1 = new VBox();
            TextArea textArea = new TextArea();
            textArea.setPrefSize(200, 400);
            Button button = new Button("Load");
            button.setPrefWidth(200);
            vbox1.setSpacing(20);
            vbox1.getChildren().addAll(textArea, button);

            Scene scene = new Scene(vbox1);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Load from Text");

            button.setOnAction(new EventHandler<ActionEvent>() {


                //Deleting objects loaded previously
                private void deleteOld()
                {
                    labels.clear();
                    labelsCells.clear();
                    //cells.clear();

                    for(int i=1; i<=n*n; i++)
                    {
                        cells[i].clear();
                    }

                    for(int i=1; i<panes.length; i++)
                    {

                        panes[i].getChildren().remove(panes[i].lookup(".label"));
                        panes[i].setStyle("-fx-border-style: solid solid solid solid; -fx-border-width: 1;");
                    }
                }

                //Creating the cages
                private void createCages(ArrayList <ArrayList<Integer>> labelsCells1)
                {

                    for(ArrayList<Integer> coordinates : labelsCells1)
                    {
                        for(int i=0; i< coordinates.size(); i++)
                        {
                            if(i< coordinates.size()-1)
                            {
                                for(int j=i+1; j< coordinates.size(); j++)
                                {
                                    Main.this.createCages(coordinates.get(i), coordinates.get(j), cells, panes, labelsCells1);
                                }

                            }

                        }
                    }
                }

                ArrayList<String>LabelsNames = new ArrayList<>();

                //Creating the labels on the grid
                private void createLabels(ArrayList <ArrayList<Integer>> labelsCells1)
                {
                    for(int i = 0; i< labelsCells1.size(); i++)
                    {
                        Label label = new Label(LabelsNames.get(i));
                        label.setFont(Font.font("Arial", 17.5));
                        StackPane.setAlignment(label, Pos.TOP_LEFT);
                        label.setPadding(new Insets(5, 0, 0, 5));

                        labels.add(label);
                        panes[labelsCells1.get(i).get(0)].getChildren().add(label);
                    }
                }

                //Reading from the file;
                private void readFile(ArrayList<Integer> allcoordinates)
                {

                    try {

                        while (reader2.ready()) {

                            String line2 = reader2.readLine();
                            if(line2==null) break;
                            String[] lineVector2 = line2.split(" ");
                            LabelsNames.add(lineVector2[0]);

                            ArrayList<Integer> coordinates = new ArrayList();


                            String line = lineVector2[1];

                            if (line.length() > 2) {
                                String[] lineVector = line.split(",");
                                for (int i = 0; i < lineVector.length; i++) {
                                    coordinates.add(Integer.parseInt(lineVector[i]));
                                    allcoordinates.add(Integer.parseInt(lineVector[i]));
                                }
                            } else
                            {
                                coordinates.add(Integer.parseInt(line));
                                allcoordinates.add(Integer.parseInt(line));
                            }


                            labelsCells.add(coordinates);




                        }
                    }
                    catch (Exception e)
                    {
                        System.out.println("Error in the file! Make sure the file is in the right format and does not have missing information.");
                        Alert alert = new Alert(Alert.AlertType.ERROR,
                                "Error in the file!. Please check the format and try again.");
                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            labelsCells.clear();
                            allcoordinates.clear();
                            LabelsNames.clear();
                        }

                    }

                    //System.out.println(readLabelNumber(labels.get(0)));
                    // System.out.println(readLabelSign(labels.get(0)));
                }

                //Validates if the cells are adjacent
                private  boolean validate(ArrayList <ArrayList<Integer>> labelsCells1)
                {
                    for(ArrayList<Integer> a : labelsCells1)
                    {
                        if(a.size()>2)
                        {
                            ArrayList<Integer> check = new ArrayList<>();
                            for(int i=0; i<a.size(); i++)
                            {
                                for(int j=i+1; j<a.size(); j++)
                                {


                                    if(a.get(i)+1 == a.get(j) )
                                    {
                                        System.out.println("Ok");
                                        check.add(1);
                                    }

                                    if(a.get(i)+n == a.get(j))
                                    {
                                        System.out.println("Ok");
                                        check.add(1);
                                    }



                                }
                            }

                            if(a.size()-1 > check.size()) return false;
                        }
                        else
                        {
                            for(int i=0; i<a.size(); i++)
                            {
                                for(int j=i+1; j<a.size(); j++) {
                                    if(a.get(i)+1 == a.get(j) || a.get(i)+n == a.get(j))
                                    {
                                        System.out.println("Ok");
                                    }
                                    else return false;
                                }

                            }

                        }

                    }
                    return true;
                }

                @Override
                public void handle(ActionEvent actionEvent) {

                    String input = textArea.getText();
                    ArrayList<Integer> allcoordinates = new ArrayList<>();

                    if (input.length() != 0)
                    {
                        reader2 = new BufferedReader(new StringReader(input));

                        //Checking if another file was loaded previously
                        if(labelsCells.size()>0)
                        {
                            deleteOld();
                        }


                        readFile(allcoordinates);
                        Set <Integer> set=new HashSet<>(allcoordinates);
                        int g = getN(labelsCells);
                        setN(g);

                        if(set.size()==allcoordinates.size() && validate(labelsCells))
                        {

                            textFields = new TextField[n][n];
                            panes= new Pane[n*n+1];
                            cells = new ArrayList[(n+1)*(n+1)];
                            textNumbers = new int [n][n];

                            createGrid(panes, cells, textNumbers, labels, labelsCells, textFields, g);
                            createLabels(labelsCells);
                            createCages(labelsCells);

                            primaryStage.setMinHeight(height);
                            primaryStage.setMinWidth(width);
                            primaryStage.setHeight(height);
                            primaryStage.setWidth(width);



                        }
                        else {


                            Alert alert = new Alert(Alert.AlertType.ERROR,
                                    "Error in the file!. Please check the format and try again.");
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                labelsCells.clear();
                                allcoordinates.clear();
                                LabelsNames.clear();
                            }
                        }
                    }
                }


            });

            stage.show();

        });


        //Showing mistakes
        button6.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if(t1)
                {
                    System.out.println("Clicked");
                    checkMistakesRow();
                    checkMistakesCol();
                    checkMistakesCages(labelsCells);
                }
                else
                {
                    System.out.println("Unclicked");
                    uncheckMistakes();
                }

            }
        });

        //Changing the font
        choiceBox.setValue("Medium");
        choiceBox.getSelectionModel().selectedIndexProperty().addListener(((observableValue, number, t1) ->
        {
            if(t1.intValue () == 0)
            {
                changeVariableS ();
            }
            else if (t1.intValue () == 1)
            {
                changeVariableM ();
            }
            else
            {
                changeVariableL ();
            }

            for(Label l : labels)
            {
                l.setFont(Font.font("Arial", panes[1].getHeight() /fontSizeDivider));
            }

            for(int i=0; i<n; i++)
            {
                for(int j=0; j<n; j++)
                {
                    textFields[i][j].setFont(Font.font("Arial", panes[1].getHeight() /fontSizeDivider));
                }
            }

        }
        ));

        //Adding to the root
        root.setRight(vbox);
        root.setTop(toolBar);

        scene = new Scene(root);
        scene.focusOwnerProperty().addListener(new ChangeListener<Node>() {
            @Override
            public void changed(ObservableValue<? extends Node> observableValue, Node node, Node t1) {
                focusData.setFocusedNode(node);
            }
        });

        //The normal bullshit
        primaryStage.setTitle("MathDoku");
        primaryStage.setScene(scene);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("images.png")));


        primaryStage.show();
    }




    public static void main(String[] args) {
        launch(args);
    }
}
