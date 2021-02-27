package sample;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;

public class Main extends Application {

    StackPane [][]cells = new StackPane[10][10];

    public static <T> boolean areUnique(final Stream<T> stream) {
        final Set<T> seen = new HashSet<>();
        return stream.allMatch(seen::add);
    }

    public boolean checkRows(Label[][]labels, int n)
    {
        ArrayList<Boolean> b = new ArrayList<>();
        for(int i=0; i<n; i++)
        {
            ArrayList<Integer> list = new ArrayList<>();

            for(int j=0; j<n; j++)
            {
                if(labels[i][j].getId()==null)
                {
                    list.add(Integer.parseInt(labels[i][j].getText()));
                }

            }



            if(areUnique(list.stream())){
                b.add(true);
            }
        }

        return b.size()==n;
    }

    public boolean checkColumns(Label[][]labels, int n)
    {
        ArrayList<Boolean> b = new ArrayList<>();
        for(int i=0; i<n; i++)
        {
            ArrayList<Integer> list = new ArrayList<>();

            for(int j=0; j<n; j++)
            {
                if(labels[j][i].getId()==null)
                {
                    list.add(Integer.parseInt(labels[j][i].getText()));
                }

            }


            if(areUnique(list.stream())){
                b.add(true);
            }
        }

        return b.size()==n;
    }

    public boolean checkBlack(Label[][]labels, int n)
    {
        for(int i=0; i<n; i++)
        {
            for(int j=0; j<n-1; j++)
            {
                if(labels[i][j].getId()!=null && labels[i][j+1].getId()!=null)
                {
                    cells[i][j].setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                    cells[i][j+1].setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                    return false;
                }
                else
                {
                    if(cells[i][j].getId()==null) cells[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                    else cells[i][j].setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                    if(cells[i][j+1].getId()==null) cells[i][j+1].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                    else cells[i][j+1].setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }
        }

        for(int i=0; i<n; i++)
        {
            for(int j=0; j<n-1; j++)
            {
                if(labels[j][i].getId()!=null && labels[j+1][i].getId()!=null)
                {
                    cells[j][i].setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                    cells[j+1][i].setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                    return false;
                }

                else
                {
                    if(cells[j][i].getId()==null) cells[j][i].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                    else cells[j][i].setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                    if(cells[j+1][i].getId()==null) cells[j+1][i].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                    else cells[j+1][i].setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }
        }



        return true;

    }


    public boolean checkWhites(Label[][]labels, int n)
    {
        int x=0, y=0;
        ArrayList<Label>black = new ArrayList<>();
        for(int i=0; i<n; i++)
        {
            for(int j=0; j<n; j++)
            {
                if(labels[i][j].getId() == null)
                {
                    x=i;
                    y=j;
                    break;
                }
            }
        }

        for(int i=0; i<n; i++)
        {
            for(int j=0; j<n; j++)
            {
                if(labels[i][j].getId()!=null)
                {
                    black.add(labels[i][j]);
                }
            }
        }
        this.whiteNum=0;
        rapidFill(labels, x, y, n);



        for(int i=0; i<n; i++)
        {
            for(int j=0; j<n; j++)
            {
                labels[i][j].setDisable(false);
            }
        }

        if(n*n == whiteNum+black.size())
        {
            return true;
        }
        return false;
    }

    int whiteNum=0;
    void rapidFill(Label[][] labels, int x, int y,
                   int n)
    {

        if (x < 0 || x >= n || y < 0 || y >= n)
            return;
        if (labels[x][y].getId()!=null)
            return;

        if(labels[x][y].isDisabled())
            return;

        whiteNum++;
        labels[x][y].setDisable(true);

        rapidFill(labels, x+1, y, n);
        rapidFill(labels, x-1, y, n);
        rapidFill(labels, x, y+1, n);
        rapidFill(labels, x, y-1, n);
    }

    public void createGrid(int n, Label[][]labels, GridPane gameGrid, StackPane [][]cells, Label label)
    {
        //Creating the Grid
        for(int i=0; i<n; i++)
        {
            for(int j=0; j<n; j++)
            {
                StackPane cell = new StackPane();
                cell.setStyle("-fx-border-style: solid solid solid solid; -fx-border-width: 2;");
                cell.setMinSize(50, 50);
                cell.setPrefSize(70, 70);
                GridPane.setHgrow(cell, Priority.ALWAYS);
                GridPane.setVgrow(cell, Priority.ALWAYS);

                cell.setMaxHeight(Double.MAX_VALUE);
                cell.setMaxWidth(cell.getMaxHeight());

                cells[i][j] = cell;
                Label label1 = new Label("1");
                //label1.setMinSize(50,50);
                //label1.setPrefSize(70, 70);
                label1.setAlignment(Pos.CENTER);

                labels[i][j].setAlignment(Pos.CENTER);

                int x = i;
                int y=j;
                cell.heightProperty().addListener((observableValue, number, t1) -> {

                    labels[x][y].setFont(Font.font("Arial", cell.getHeight()/2));
                });

                cell.getChildren().add(labels[i][j]);

                gameGrid.add(cell, j, i);


                cells[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(mouseEvent.getButton() == MouseButton.PRIMARY)
                        {
                            cell.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
                            cells[x][y].setId("1");
                            labels[x][y].setId("1");
                        }
                        if(mouseEvent.getButton() == MouseButton.SECONDARY)
                        {
                            cell.setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                            cells[x][y].setId(null);
                            labels[x][y].setId(null);
                        }

                        checkBlack(labels, n);
                        if(!checkWhites(labels, n))
                        {
                            label.setVisible(true);
                        }
                        else label.setVisible(false);
                        if(checkRows(labels, n) && checkColumns(labels, n) && checkBlack(labels, n) && checkWhites(labels, n))
                        {

                            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                                    "You WIN!!!");
                            alert.showAndWait();
                        }
                        //checkRows(labels, n);

                    }
                });

                cell.onMouseClickedProperty().addListener(new ChangeListener<EventHandler<? super MouseEvent>>() {
                    @Override
                    public void changed(ObservableValue<? extends EventHandler<? super MouseEvent>> observableValue, EventHandler<? super MouseEvent> eventHandler, EventHandler<? super MouseEvent> t1) {


                    }
                });



            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        BorderPane root=new BorderPane();
        ToolBar topMenu = new ToolBar();
        Button button1 = new Button("Clear");
        Button button2 = new Button ("Load");
        Text title = new Text("Hitori");
        Label warning = new Label("All white cells need to be connected to each other in a single component.");
        warning.setTextFill(Color.RED);
        warning.setVisible(false);
        warning.setAlignment(Pos.CENTER);

        topMenu.getItems().addAll(title, new Separator(), button1, button2);
        final FileChooser fileCh = new FileChooser();
        final Label[][]primaryLabels;
        primaryLabels = new Label[][]{{new Label("1"),new Label("5"),new Label("3"),new Label("1"),new Label("2")},{new Label("5"),new Label("4"),new Label("1"),new Label("3"),new Label("4")}, {new Label("3"), new Label("4"), new Label("3"), new Label("1"), new Label("5")}, {new Label("4"),new Label("4"),new Label("2"),new Label("3"),new Label("3")},{new Label("2"),new Label("1"), new Label("5"),new Label("4"),new Label("4")}};
        ArrayList<Integer> num = new ArrayList<>();

        Label [][]labels = new Label[10][10];


        GridPane gameGrid = new GridPane();
        gameGrid.setAlignment(Pos.CENTER);
        gameGrid.setPadding(new Insets(50));
        cells=new StackPane[5][5];
        createGrid(5,primaryLabels, gameGrid, cells, warning);
        checkWhites(primaryLabels, 5);

        //Clear Button
        button1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Alert ask = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to clear the board?");
                Optional<ButtonType> result = ask.showAndWait();

                if(result.isPresent() && result.get() == ButtonType.OK)
                {
                    for(int i=0; i<cells.length; i++)
                    {
                        for(int j=0; j<cells.length; j++)
                        {
                            if(cells[i][j].getId() != null)
                            {
                                cells[i][j].setBackground(new Background(new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)));
                                cells[i][j].setId(null);
                                if(num.size()==0)
                                {
                                    primaryLabels[i][j].setId(null);
                                }
                                else {
                                    labels[i][j].setId(null);
                                }

                            }

                        }
                    }
                    warning.setVisible(false);
                }
            }
        });

        //Load Button
        button2.setOnAction(new EventHandler<>() {

            BufferedReader reader;
            int n=0;
            private void configureFileChooser(final FileChooser fileCH)
            {
                fileCH.setTitle("Choose Level");
                fileCH.setInitialDirectory(new File(System.getProperty("user.home")));
                fileCH.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("TXT", "*.txt"));
            }

            private void readFile()
            {
                int i=0;
                try
                {
                    while(reader.ready())
                    {
                        String line = reader.readLine();
                        if(line==null) break;
                        String[] wordSplit = line.split(" ");
                        n = wordSplit.length;

                        for(int j=0; j<n; j++)
                        {
                            labels[i][j] = new Label(wordSplit[j]);
                        }
                        i++;

                    }
                }
                catch (Exception e)
                {
                    System.out.println("Error in the file! Make sure the file is in the right format and does not have missing information.");
                }


            }

            private void deleteOld()
            {
                for(int i=0; i<n; i++)
                {
                    for(int j=0; j<n; j++)
                    {
                        labels[i][j] = null;
                        cells[i][j] = null;
                    }
                }

                gameGrid.getChildren().clear();
            }

            @Override
            public void handle(ActionEvent actionEvent) {
                configureFileChooser(fileCh);
                File file = fileCh.showOpenDialog(primaryStage);

                if (file != null) {
                    FileReader fr = null;

                    try {
                        fr = new FileReader(file);
                    }
                    catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    if(cells.length != 15  || num.size() == 0)
                    {
                        deleteOld();
                    }
                    reader = new BufferedReader(fr);
                    readFile();

                    cells = new StackPane[n][n];

                    num.add(1);
                    createGrid(n, labels, gameGrid, cells, warning);

                }
            }
        });


        root.setTop(topMenu);
        root.setBottom(warning);
        root.setCenter(gameGrid);
        primaryStage.setTitle("Hitori");
        primaryStage.setMinHeight(550);
        primaryStage.setMinWidth(500);
        primaryStage.setScene(new Scene(root, 560, 560));
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }
}
