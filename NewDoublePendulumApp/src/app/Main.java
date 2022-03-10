package app;
/*

----------------
ERRORS
----------------

----------------
FIXED ERRORS
----------------
NIE DZIALA ZMIANA LICZBY LINI!!! ^done^
NIE WIEM CZEMU USTAWIA NAJPIERW X0,Y0 = 0, 0 :CCCCCC ^done^


----------------
TO DO
----------------
More lines naming convection

 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.image.PixelWriter;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Math.*;

public class Main extends Application {

    double startingX0, newX0;
    double startingY0, newY0;
    int N;
    int newN;

    double r1;
    double r2;
    double m1, newM1;
    double m2, newM2;
    double g, newG;
    DoublePendulum[] pendulums;
    ArrayList<Pair<Integer, Integer>> listOfPaintedPoints;
    ArrayList<Double> listOfNewParameters;

    double startingX1;
    double startingY1;
    double startingX2;
    double startingY2;
    double startingA1, newA1;
    double startingA2, newA2;

    // Boolean conditions
    boolean isRunning;
    boolean isShowingCircles1, isShowingCircles2, useNewPendulumsArray;
    boolean isPainting, formNewLines;
    boolean isShowingLines;
    boolean isSavingPaint;
    boolean isShowingAxis;

    // Objects to basic functionality
    @FXML
    Canvas canvas;
    @FXML
    GraphicsContext gc;
    @FXML
    PixelWriter px;
    AnimationTimer timer;
    Stage newWindow, mainWindow;
    Group root;

    // UI elements
    @FXML
    Button start;
    @FXML
    Button stop;
    @FXML
    Button reset;
    @FXML
    Button clearCanvas, configure;
    @FXML
    CheckBox showCircleCheckBox, showPaintCheckBox, showLineCheckBox, showSecondCircleCheckBox, showAxisCheckbox;
    @FXML
    Color colorOfPoints;
    BubbleChart bubbleChart;
    FileHandler fileHandler;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        // Creating empty Group
        root = new Group();

        // Creating file handler
        fileHandler = new FileHandler("data.txt");


        // Creating timer
        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if(isRunning) {
                    calculateXYPoints();

                    for(int i=0;i<N;i++) {
                        double a1 = pendulums[i].getA1();
                        double a2 = pendulums[i].getA2();
                        double a1_v = pendulums[i].getA1_v();
                        double a1_a = pendulums[i].getA1_a();
                        double a2_v = pendulums[i].getA2_v();
                        double a2_a = pendulums[i].getA2_a();

                        a1_v += a1_a;
                        a2_v += a2_a;
                        a1 += a1_v;
                        a2 += a2_v;

                        pendulums[i].setAngles(a1, a1_v, a1_a, a2, a2_v, a2_a);

                        showLines(i);

                        calculateAngles();
                        setCirclesProperties();
                        // Draw
                        paintCanvas();
                    }


                    try {
                        fileHandler.writeData(pendulums, N);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        };

        timer.start();



        // Adding objects
        root.getChildren().add(bubbleChart);


        for(int i=0;i<N;i++) {
            Line line1 = pendulums[i].getLine1();
            Line line2 = pendulums[i].getLine2();
            Circle circle1 = pendulums[i].getCircle1();
            Circle circle2 = pendulums[i].getCircle2();
            root.getChildren().addAll(line1, line2, circle1, circle2);
        }
        root.getChildren().add(canvas);
        root.getChildren().addAll(start, stop, reset, clearCanvas, configure);
        root.getChildren().addAll(showCircleCheckBox, showPaintCheckBox, showLineCheckBox, showSecondCircleCheckBox, showAxisCheckbox);

        // Scene configuration
        Scene scene = new Scene(root, 1200, 800);
        stage.setScene(scene);
        stage.setTitle("Double pendulum");
        stage.show();
        stage.setResizable(false);
        mainWindow = stage;
    }


    @Override
    public void init() throws Exception {
        super.init();
        pendulums = new DoublePendulum[1000];
        listOfNewParameters = new ArrayList<>();
        newX0 = 600;
        newY0 = 300;
        newM1 = 10;
        newM2 = 10;
        newG = 1;
        newA1 = PI;
        newA2 = PI;
        newN = 2;
        // Booleans set-up
        isShowingCircles1 = false;
        isShowingCircles2 = false;
        useNewPendulumsArray = false;
        isRunning = true;
        isPainting = true;
        isShowingLines = true;
        isSavingPaint = false;
        isShowingAxis = true;
        formNewLines = false;

        // Configuring basic parameters and objects
        setStartingParameters();
        createInstancesOfBasicObjects();
        createButtons();
        createCheckBoxes();
        setCirclesProperties();
        generateAxis();
    }

    private void showLines(int i) {
        if(isShowingLines) {
            pendulums[i].getLine1().setStrokeWidth(3);
            pendulums[i].getLine2().setStrokeWidth(3);
        } else {
            pendulums[i].getLine1().setStrokeWidth(0);
            pendulums[i].getLine2().setStrokeWidth(0);
        }
    }

    private void paintCanvas() {

            /*
             Drawing points in pattern:
              +
             +++
              +
             */
        if(isSavingPaint) {
            listOfPaintedPoints.add(new Pair<>((int) startingX2, (int) startingY2));
            listOfPaintedPoints.add(new Pair<>((int) startingX2 + 1, (int) startingY2));
            listOfPaintedPoints.add(new Pair<>((int) startingX2 - 1, (int) startingY2));
            listOfPaintedPoints.add(new Pair<>((int) startingX2, (int) startingY2 + 1));
            listOfPaintedPoints.add(new Pair<>((int) startingX2, (int) startingY2 - 1));
        }

        if(isPainting) {
            for (int i=0;i<N;i++) {
                DoublePendulum pendulum = pendulums[i];
                px.setColor((int) pendulum.getX2(), (int) pendulum.getY2(), colorOfPoints);
                px.setColor((int) pendulum.getX2() + 1, (int) pendulum.getY2(), colorOfPoints);
                px.setColor((int) pendulum.getX2() - 1, (int) pendulum.getY2(), colorOfPoints);
                px.setColor((int) pendulum.getX2(), (int) pendulum.getY2() + 1, colorOfPoints);
                px.setColor((int) pendulum.getX2(), (int) pendulum.getY2() - 1, colorOfPoints);

            }
        }
    }

    private void setCirclesProperties() {
        int a,b;
        if(isShowingCircles1) {
            a = 1;
        } else {
            a = 0;
        }

        if(isShowingCircles2) {
            b = 1;
        } else {
            b = 0;
        }

        for(int i=0;i<N;i++) {
            pendulums[i].setCircles(m1*a, m2*b);
            pendulums[i].getCircle1().setOpacity(1-((double) i/(double) N));
            pendulums[i].getCircle2().setOpacity(1-((double) i/(double) N));
        }

    }

    private void calculateXYPoints() {

        for(int i=0;i<N;i++) {
            double a1 = pendulums[i].getA1();
            double a2 = pendulums[i].getA2();
            startingX0 = pendulums[i].getX0();
            startingY0 = pendulums[i].getY0();

            startingX1 = startingX0 + r1 * sin(a1);
            startingY1 = startingY0 + r1 * cos(a1);

            startingX2 = startingX1 + r2 * sin(a2);
            startingY2 = startingY1 + r2 * cos(a2);

            pendulums[i].setPosition(startingX0, startingY0, startingX1, startingY1, startingX2, startingY2);
        }
    }

    private void calculateAngles() {
        for(int i=0;i<N;i++) {
            double a1 = pendulums[i].getA1();
            double a2 = pendulums[i].getA2();
            double a1_v = pendulums[i].getA1_v();
            double a1_a;
            double a2_v = pendulums[i].getA2_v();
            double a2_a;


            double n1 = -g * (2 * m1 + m2) * sin(a1);
            double n2 = -m2 * g * sin(a1 - 2 * a2);
            double n3 = -2 * sin(a1 - a2) * m2;
            double n4 = a2_v * a2_v * r2 + a1_v * a1_v * r1 * cos(a1 - a2);
            double n5 = r1 * (2 * m1 + m2 - m2 * cos(2 * a1 - 2 * a2));

            a1_a = (n1 + n2 + n3 * n4) / n5;

            n1 = 2 * sin(a1 - a2);
            n2 = (a1_v * a1_v * r1 * (m1 + m2));
            n3 = g * (m1 + m2) * cos(a1);
            n4 = a2_v * a2_v * r2 * m2 * cos(a1 - a2);
            n5 = r2 * (2 * m1 + m2 - m2 * cos(2 * a1 - 2 * a2));

            a2_a = n1 * (n2 + n3 + n4) / n5;
            pendulums[i].setAngles(a1, a1_v, a1_a, a2, a2_v, a2_a);
        }
    }

    private void createInstancesOfBasicObjects() {
        canvas = new Canvas(1200, 800);
        gc = canvas.getGraphicsContext2D();
        px = gc.getPixelWriter();
        colorOfPoints = Color.BLACK;
    }

    private void setStartingParameters() throws IOException {
        // Ints
        startingX0 = newX0;
        startingY0 = newY0;
        N = newN;

        // Doubles
        r1 = 200;
        r2 = 200;
        m1 = newM1;
        m2 = newM2;
        g = newG;
        startingA1 = newA1;
        startingA2 = newA2;
        listOfPaintedPoints = new ArrayList<>();

        if(formNewLines) {
            formNewLines = false;
            fileHandler = new FileHandler("data.txt");
        }

        if (!useNewPendulumsArray) {
            for (int i = 0; i < N; i++) {
                if (pendulums[i] == null) {
                    startingX1 = startingX0 + r1 * sin(startingA1);
                    startingY1 = startingY0 + r1 * cos(startingA1);

                    startingX2 = startingX1 + r2 * sin(startingA2);
                    startingY2 = startingY1 + r2 * cos(startingA2);

                    pendulums[i] = new DoublePendulum(startingX0, startingY0, startingX1, startingY1, startingX2, startingY2);
                    // Default line colors
                    pendulums[i].getLine1().setStroke(Color.BLACK);
                    pendulums[i].getLine2().setStroke(Color.BLACK);
                    pendulums[i].getLine2().setStroke(Color.BLACK);
                    pendulums[i].getLine1().setOpacity(1 - ((double) i / (double) N));
                    pendulums[i].getLine2().setOpacity(1 - ((double) i / (double) N));

                }
                    pendulums[i].setPosition(startingX0, startingY0, startingX1, startingY1, startingX2, startingY2);

                pendulums[i].setAngles(startingA1, 0, 0, startingA2 + 0.01 * ((double) i / (double) N - 10), 0, 0);
            }
        } else {
            // Saving order -> x, y, a1, a2, m1, m2
            for(int i=0, pendulumsIndex=0;i<listOfNewParameters.size();i+=6, pendulumsIndex++) {
                System.out.println("Niiiig");
                double x0t = listOfNewParameters.get(i);
                double y0t = listOfNewParameters.get(i+1);
                double a1t = listOfNewParameters.get(i+2);
                double a2t = listOfNewParameters.get(i+3);
                double x1t = x0t + r1*sin(a1t);
                double y1t = x0t + r1*cos(a1t);
                double x2t = x1t + r1*sin(a2t);
                double y2t = x1t + r1*cos(a2t);

                pendulums[pendulumsIndex].setPosition(
                        x0t, y0t, x1t, y1t, x2t, y2t
                        );
                pendulums[pendulumsIndex].setAngles(a1t, 0, 0, a2t, 0, 0);
            }

        }
    }

    private void createButtons() {
        start = new Button("Start");
        stop = new Button("Stop");
        reset = new Button("Reset");
        clearCanvas = new Button("Clear Points");
        configure = new Button("Configure");
        start.setLayoutX(30);
        start.setLayoutY(30);
        stop.setLayoutX(30);
        stop.setLayoutY(60);
        reset.setLayoutX(30);
        reset.setLayoutY(90);
        clearCanvas.setLayoutX(30);
        clearCanvas.setLayoutY(120);
        configure.setLayoutX(30);
        configure.setLayoutY(0);




        start.setOnMouseClicked(mouseEvent -> isRunning = true);

        stop.setOnMouseClicked(mouseEvent -> isRunning = false);

        reset.setOnMouseClicked(mouseEvent -> {
            try {
                restart();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        clearCanvas.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                clearCanvas();
            }
        });

        configure.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                generateNewWindow();
            }
        });


    }

    private void restart() throws IOException {
        setStartingParameters();
        calculateXYPoints();
        isRunning = false;
        listOfPaintedPoints.clear();
        clearCanvas();

    }

    private void createCheckBoxes() {
        // Checkboxes configuration
        showCircleCheckBox = new CheckBox("Show 1st mass");
        showCircleCheckBox.setSelected(isShowingCircles1);
        showCircleCheckBox.setLayoutX(30);
        showCircleCheckBox.setLayoutY(150);

        showSecondCircleCheckBox = new CheckBox("Show 2st mass");
        showSecondCircleCheckBox.setSelected(isShowingCircles2);
        showSecondCircleCheckBox.setLayoutX(30);
        showSecondCircleCheckBox.setLayoutY(170);

        showPaintCheckBox = new CheckBox("Show Paint");
        showPaintCheckBox.setSelected(isPainting);
        showPaintCheckBox.setLayoutX(30);
        showPaintCheckBox.setLayoutY(190);

        showLineCheckBox = new CheckBox("Show Lines");
        showLineCheckBox.setSelected(isShowingLines);
        showLineCheckBox.setLayoutX(30);
        showLineCheckBox.setLayoutY(210);

        showAxisCheckbox = new CheckBox("Show axis");
        showAxisCheckbox.setSelected(isShowingAxis);
        showAxisCheckbox.setLayoutX(30);
        showAxisCheckbox.setLayoutY(240);


        // Setting event handlers
        showCircleCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                isShowingCircles1 = !isShowingCircles1;
            }
        });

        showSecondCircleCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                isShowingCircles2 = !isShowingCircles2;
            }
        });


        showPaintCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(isPainting) {
                    isPainting = false;
                    clearCanvas();
                }
                else {
                    isPainting = true;
                    Thread generatePointsThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for(Pair<Integer, Integer> pair :   listOfPaintedPoints) {
                                px.setColor(pair.getKey(), pair.getValue(),Color.BLACK);
                            }
                        }
                    });
                    generatePointsThread.start();
                }
            }
        });

        showLineCheckBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(isShowingLines) {
                    isShowingLines = false;
                }
                else isShowingLines = true;
            }
        });

        showAxisCheckbox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(!isShowingAxis) {
                    bubbleChart.setLayoutX(0);
                    bubbleChart.setLayoutY(0);
                    isShowingAxis = true;
                } else {
                    bubbleChart.setLayoutX(10000);
                    bubbleChart.setLayoutY(100000);
                    isShowingAxis = false;
                }
            }
        });
    }

    private void clearCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void generateNewWindow() {
        int startingX=30, startingY=30;
        ArrayList<Node> txtNodes = new ArrayList<>();
        ArrayList<TextField> tfNodes = new ArrayList<>();

        Label x0Txt, y0Txt, a1Txt, a2Txt, m1Txt, m2Txt, numOfLineTxt;

        // Creating settings window
        Group gr = new Group();

        Label txt = new Label("STARTING PARAMETERS CONFIGURATION");
        txt.setLayoutY(0);
        txt.setLayoutX(0);

        Button btn = new Button("Apply");
        btn.setLayoutX(600);
        btn.setLayoutY(400);

        gr.getChildren().add(btn);



        // Gravity and number of lines
        Label nTxt = new Label("Num Of Lines: ");
        nTxt.setLayoutX(startingX);
        nTxt.setLayoutY(startingY);
        TextField nTF = new TextField(Integer.toString(newN));
        nTF.setLayoutX(startingX+80);
        nTF.setLayoutY(startingY-5);
        nTF.setPrefWidth(50);

        Label gTxt = new Label("G (m/s2): ");
        gTxt.setLayoutX(startingX+150);
        gTxt.setLayoutY(startingY);
        TextField gTF = new TextField(Double.toString(newG*9.81));
        gTF.setLayoutX(startingX+200);
        gTF.setLayoutY(startingY-5);
        gTF.setPrefWidth(50);

        startingX = 30;
        startingY = 55;

        tfNodes.add(nTF);
        tfNodes.add(gTF);

        for(int i=0;i<N;i++) {

            numOfLineTxt = new Label(Integer.toString(i+1) + ".");
            numOfLineTxt.setLayoutX(startingX-15);
            numOfLineTxt.setLayoutY(startingY);

            x0Txt = new Label("x0:");
            x0Txt.setLayoutX(startingX);
            x0Txt.setLayoutY(startingY);
            TextField x0TF = new TextField(Double.toString(pendulums[i].getX0()));
            x0TF.setLayoutX(startingX+20);
            x0TF.setLayoutY(startingY-5);
            x0TF.setPrefWidth(50);
            x0TF.setId("x-" + Integer.toString(i));


            y0Txt = new Label("y0:");
            y0Txt.setLayoutX(startingX+80);
            y0Txt.setLayoutY(startingY);
            TextField y0TF = new TextField(Double.toString(pendulums[i].getY0()));
            y0TF.setLayoutX(startingX + 100);
            y0TF.setLayoutY(startingY-5);
            y0TF.setPrefWidth(50);
            y0TF.setId("y-" + Integer.toString(i));

            a1Txt = new Label("a1:");
            a1Txt.setLayoutX(startingX+170);
            a1Txt.setLayoutY(startingY);
            TextField a1TF = new TextField(Double.toString(pendulums[i].getA1()));
            a1TF.setLayoutX(startingX+190);
            a1TF.setLayoutY(startingY-5);
            a1TF.setPrefWidth(50);
            a1TF.setId("a1-" + Integer.toString(i));

            a2Txt = new Label("a2:");
            a2Txt.setLayoutX(startingX+260);
            a2Txt.setLayoutY(startingY);
            TextField a2TF = new TextField(Double.toString(pendulums[i].getA2()));
            a2TF.setLayoutX(startingX + 280);
            a2TF.setLayoutY(startingY-5);
            a2TF.setPrefWidth(50);
            a2TF.setId("a2-" + Integer.toString(i));

            m1Txt = new Label("m1:");
            m1Txt.setLayoutX(startingX+350);
            m1Txt.setLayoutY(startingY);
            TextField m1TF = new TextField(Double.toString(newM1));
            m1TF.setLayoutX(startingX+370);
            m1TF.setLayoutY(startingY-5);
            m1TF.setPrefWidth(50);
            m1TF.setId("m1-" + Integer.toString(i));


            m2Txt = new Label("m2:");
            m2Txt.setLayoutX(startingX+430);
            m2Txt.setLayoutY(startingY);
            TextField m2TF = new TextField(Double.toString(newM2));
            m2TF.setLayoutX(startingX+450);
            m2TF.setLayoutY(startingY-5);
            m2TF.setPrefWidth(50);
            m2TF.setId("m2-" + Integer.toString(i));




            tfNodes.add(x0TF);
            tfNodes.add(y0TF);
            tfNodes.add(a1TF);
            tfNodes.add(a2TF);
            tfNodes.add(m1TF);
            tfNodes.add(m2TF);


            txtNodes.add(x0Txt);
            txtNodes.add(y0Txt);
            txtNodes.add(a1Txt);
            txtNodes.add(a2Txt);
            txtNodes.add(m1Txt);
            txtNodes.add(m2Txt);
            txtNodes.add(numOfLineTxt);


            startingY += 30;
        }



        txtNodes.add(txt);
        txtNodes.add(nTxt);
        txtNodes.add(gTxt);

        gr.getChildren().addAll(tfNodes);
        gr.getChildren().addAll(txtNodes);

        Scene sc = new Scene(gr, 500, 700);
        newWindow = new Stage();
        newWindow.setScene(sc);
        newWindow.setHeight(500);
        newWindow.setWidth(700);
        newWindow.setResizable(true);
        newWindow.requestFocus();
        newWindow.setAlwaysOnTop(true);
        newWindow.initOwner(mainWindow);
        newWindow.initModality(Modality.WINDOW_MODAL);
        newWindow.setTitle("Settings for double pendulum");
        newWindow.show();
        isRunning = false;

        newWindow.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                isRunning = true;
            }
        });


        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(newN != Integer.parseInt(tfNodes.get(0).getText())) formNewLines = true;
                newN = Integer.parseInt(tfNodes.get(0).getText());
                newG = Double.parseDouble(tfNodes.get(1).getText()) / 9.81;
                if(!formNewLines) {
                    useNewPendulumsArray = true;

                    listOfNewParameters = new ArrayList<>();
                    int startingPoint = 2;

                    for (int x = 2; x < tfNodes.size(); x++) {
                        listOfNewParameters.add(Double.parseDouble(tfNodes.get(x).getText()));
                    }
                }

                // Deleting all lines
                for (int i = 0; i < N; i++) {
                    root.getChildren().removeAll(pendulums[i].getLine1(), pendulums[i].getLine2(), pendulums[i].getCircle1(), pendulums[i].getCircle2());
                }

                try {
                    restart();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Adding new lines
                for (int i = 0; i < newN; i++) {
                    root.getChildren().addAll(pendulums[i].getLine1(), pendulums[i].getLine2(), pendulums[i].getCircle1(), pendulums[i].getCircle2());
                }
                newWindow.close();

            }
        });


    }

    private void generateAxis() {
        //Defining the axes
        NumberAxis xAxis = new NumberAxis(0, 1200, 50);
        xAxis.setLabel("x axis");
        NumberAxis yAxis = new NumberAxis(0, 800, 50);
        yAxis.setLabel("y axis");
        //Creating the Bubble chart
        bubbleChart = new BubbleChart(xAxis, yAxis);

        bubbleChart.setLayoutX(-10);
        bubbleChart.setLayoutY(-20);
        bubbleChart.setPrefWidth(1200);
        bubbleChart.setPrefHeight(800);
    }

}