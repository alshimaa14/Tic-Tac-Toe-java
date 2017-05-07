package tictactoe;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import static tictactoe.Game.fileopen;
import static tictactoe.Game.showCounter;

public class TicTacToe extends Application {

    static Stage primaryStage;
    static TicTacToe myApp;
    private Game game;
    private OnlineGame onlineGame;

    char i;
    String playerPiece;
    public SingleGame singleGame;
    static final Button btns[] = new Button[9];

    String Mode = new String();
    MediaPlayer mediaplayer;

    void HomeGame() {
        try {
            String Dir = System.getProperty("user.dir");
            File homeMedia = new File(Dir, "/src/tictactoe/resources/images/sound1.wav");
            String mediaUrl;
            mediaUrl = homeMedia.toURI().toURL().toString();
            Media homeMusicFile = new Media(mediaUrl);
            mediaplayer = new MediaPlayer(homeMusicFile);
            mediaplayer.setAutoPlay(true);
        } catch (MalformedURLException ex) {
            Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
        }

        Button btnNew = new Button();
        btnNew.setId("button");
        Button btnLoad = new Button();
        btnLoad.setId("button");
        btnNew.setText("New Game");
        btnLoad.setText("Load Game");
        btnNew.setOnAction((ActionEvent event) -> {

            try {
                mediaplayer.pause();
                String Dir = System.getProperty("user.dir");
                File media = new File(Dir, "/src/tictactoe/resources/images/button.wav");
                String mediaUrl;
                mediaUrl = media.toURI().toURL().toString();
                Media musicFile = new Media(mediaUrl);
                mediaplayer = new MediaPlayer(musicFile);
                mediaplayer.setAutoPlay(true);
                ChooseGameMode();
            } catch (MalformedURLException ex) {
                Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        btnLoad.setOnAction((ActionEvent event) -> {
            try {
                String Dir = System.getProperty("user.dir");
                File media = new File(Dir, "/src/tictactoe/resources/images/button.wav");
                String mediaUrl;
                mediaUrl = media.toURI().toURL().toString();
                Media musicFile = new Media(mediaUrl);
                mediaplayer = new MediaPlayer(musicFile);
                mediaplayer.setAutoPlay(true);
                LoadBoard();
                game.loadGames();
            } catch (MalformedURLException ex) {
                Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        StackPane homePane = new StackPane();

        VBox box = new VBox(10);
        box.getChildren().addAll(btnNew, btnLoad);
        box.setAlignment(Pos.CENTER);
        homePane.getChildren().add(box);

        Scene homeScene = new Scene(homePane, 380, 400);
        primaryStage.setScene(homeScene);
        String css = TicTacToe.class.getResource("style.css").toExternalForm();
        homeScene.getStylesheets().add(css);
        primaryStage.setTitle("Tic-Tac-Toe");
//        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
    }

    void ChooseGameMode() {

//        Button btnHome = new Button();
//        btnHome.setId("btnHome");
        Button btnSinglePlayer = new Button();
        btnSinglePlayer.setId("button");
        Button btnTwoPlayers = new Button();
        btnTwoPlayers.setId("button");
        Button btnOnline = new Button();
        btnOnline.setId("button");
        btnSinglePlayer.setText("Single Player");
        btnTwoPlayers.setText("two Players");
        btnOnline.setText("Online");

        btnSinglePlayer.setOnAction((ActionEvent event) -> {
            try {
                String Dir = System.getProperty("user.dir");
                File media = new File(Dir, "/src/tictactoe/resources/images/button.wav");
                String mediaUrl;
                mediaUrl = media.toURI().toURL().toString();
                Media musicFile = new Media(mediaUrl);
                mediaplayer = new MediaPlayer(musicFile);
                mediaplayer.setAutoPlay(true);
                Mode = "single";
                ChooseSymbol();
            } catch (MalformedURLException ex) {
                Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        btnTwoPlayers.setOnAction((ActionEvent event) -> {

            try {
                String Dir = System.getProperty("user.dir");
                File media = new File(Dir, "/src/tictactoe/resources/images/button.wav");
                String mediaUrl;
                mediaUrl = media.toURI().toURL().toString();
                Media musicFile = new Media(mediaUrl);
                mediaplayer = new MediaPlayer(musicFile);
                mediaplayer.setAutoPlay(true);
            } catch (MalformedURLException ex) {
                Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
            }

            Mode = "multi";
            ChooseSymbol();
        });
        if (Mode == "multi") {
            game = new Game();
        } else if (Mode == "single") {
            singleGame = new SingleGame('x');
        }
        Board();

        btnOnline.setOnAction((ActionEvent event) -> {
            Mode = "online";
            onlineGame = new OnlineGame();

            while (!onlineGame.status) {
                onlineGame.waitAlert.setHeaderText("Waiting...");
                onlineGame.waitAlert.setContentText("Please wait for an opponent to connect");
                onlineGame.waitAlert.showAndWait();
            }
            onlineGame.waitAlert.close();
            Board();
            try {
                String Dir = System.getProperty("user.dir");
                File media = new File(Dir, "/src/tictactoe/resources/images/button.wav");
                String mediaUrl;
                mediaUrl = media.toURI().toURL().toString();
                Media musicFile = new Media(mediaUrl);
                mediaplayer = new MediaPlayer(musicFile);
                mediaplayer.setAutoPlay(true);
            } catch (MalformedURLException ex) {
                Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
//        btnHome.setOnAction((ActionEvent event) -> {
//            try {
//                String Dir = System.getProperty("user.dir");
//                File media = new File(Dir, "/src/tictactoe/resources/images/button.wav");
//                String mediaUrl;
//                mediaUrl = media.toURI().toURL().toString();
//                Media musicFile = new Media(mediaUrl);
//                mediaplayer = new MediaPlayer(musicFile);
//                mediaplayer.setAutoPlay(true);
//                HomeGame();
//            } catch (MalformedURLException ex) {
//                Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        });

        //create StackPane
        StackPane modePane = new StackPane();

        VBox modeBox = new VBox(10);
        modeBox.getChildren().addAll(btnSinglePlayer, btnTwoPlayers, btnOnline);
        modeBox.setAlignment(Pos.CENTER);

        modePane.getChildren().add(modeBox);
        Scene modeScene = new Scene(modePane, 380, 400);
        String css = TicTacToe.class.getResource("style.css").toExternalForm();
        modeScene.getStylesheets().add(css);
        primaryStage.setTitle("Tic-Tac-Toe");
        primaryStage.setScene(modeScene);
    }

    void ChooseSymbol() {
        StackPane symbolePane = new StackPane();
//        Button btnHome = new Button();
//        btnHome.setId("btnHome");
//        Button btnExit = new Button();
//        btnExit.setId("btnExit");

        Button btnX = new Button();
        btnX.setId("btnX");
        Button btnO = new Button();
        btnO.setId("btnO");

        btnX.setOnAction((ActionEvent event) -> {

            try {
                String Dir = System.getProperty("user.dir");
                File media = new File(Dir, "/src/tictactoe/resources/images/button.wav");
                String mediaUrl;
                mediaUrl = media.toURI().toURL().toString();
                Media musicFile = new Media(mediaUrl);
                mediaplayer = new MediaPlayer(musicFile);
                mediaplayer.setAutoPlay(true);

            } catch (MalformedURLException ex) {
                Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
            }

            playerPiece = "x";
            if ("multi".equals(Mode)) {
                game = new Game();
            } else if ("single".equals(Mode)) {
                singleGame = new SingleGame('x');
            }
            Board();//Draw The Board Scene
        });
        btnO.setOnAction((ActionEvent event) -> {
            try {
                String Dir = System.getProperty("user.dir");
                File media = new File(Dir, "/src/tictactoe/resources/images/button.wav");
                String mediaUrl;
                mediaUrl = media.toURI().toURL().toString();
                Media musicFile = new Media(mediaUrl);
                mediaplayer = new MediaPlayer(musicFile);
                mediaplayer.setAutoPlay(true);

            } catch (MalformedURLException ex) {
                Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
            }

            if ("multi".equals(Mode)) {
                game = new Game();
                Board();
            } else if ("single".equals(Mode)) {
                singleGame = new SingleGame('o');
                Board();
                singleGame.PC.makeTick();
            }
            //Draw The Board Scene
        });
//
//        btnHome.setOnAction((ActionEvent event) -> {
//            try {
//                String Dir = System.getProperty("user.dir");
//                File media = new File(Dir, "/src/tictactoe/resources/images/button.wav");
//                String mediaUrl;
//                mediaUrl = media.toURI().toURL().toString();
//                Media musicFile = new Media(mediaUrl);
//                mediaplayer = new MediaPlayer(musicFile);
//                mediaplayer.setAutoPlay(true);
//                HomeGame();
//            } catch (MalformedURLException ex) {
//                Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        });
        HBox box = new HBox(10);
        box.setAlignment(Pos.CENTER);
        box.getChildren().addAll(btnX, btnO);
        symbolePane.getChildren().addAll(box);
        symbolePane.setAlignment(Pos.CENTER);
        Scene symboleScene = new Scene(symbolePane, 380, 400);
        String css = TicTacToe.class.getResource("style.css").toExternalForm();
        symboleScene.getStylesheets().add(css);
        primaryStage.setTitle("ChooseSymbol");
        primaryStage.setScene(symboleScene);
    }

    void Board() {

        GridPane boardPane = new GridPane();
        boardPane.setPadding(new Insets(10, 10, 12, 12));
        boardPane.setVgap(10);
        boardPane.setHgap(10);
        boardPane.setId("board");
        Scene boardScene = new Scene(boardPane, 380, 385);
        int id = 0;
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                Button btn = new Button();
                btn.setOnAction((ActionEvent event) -> {
                    try {
                        String Dir = System.getProperty("user.dir");
                        File media = new File(Dir, "/src/tictactoe/resources/images/button.wav");
                        String mediaUrl;
                        mediaUrl = media.toURI().toURL().toString();
                        Media musicFile = new Media(mediaUrl);
                        mediaplayer = new MediaPlayer(musicFile);
                        mediaplayer.setAutoPlay(true);

                    } catch (MalformedURLException ex) {
                        Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    if ("multi".equals(Mode)) {
                        int y = game.tickHandle(btn);
                        if (y == 1) {
                            Result(game.result, "win");
                            System.out.println(game.result);
                        } else if (y == 3) {

                            Result(game.result, "draw");
                            System.out.println("draw");
                        }

                    } else if ("single".equals(Mode)) {
                        int x = singleGame.tickHandle(btn);
                        if (x == 1) {
                            System.out.println("player win");
                            Result(singleGame.result, "win");
                        } else if (x == 2) {
                            System.out.println("computer win");
                            Result(singleGame.result, "lose");
                        } else if (x == 4) {
                            System.out.println("draw");
                            Result(singleGame.result, "draw");
                        }
                    } else if (Mode == "online") {
                        
                        int x = onlineGame.tickHandle(btn);
                        if (x == 1) {
                            System.out.println("you win");
                            onlineGame.ps.println("opponent won you lose");
                            Result(onlineGame.result, "win");
                        } else if (x == 2) {
                            System.out.println("Opponent win");
                            Result(onlineGame.result, "lose");
                        } else if (x == 4) {
                            System.out.println("draw");
                            Result(onlineGame.result, "draw");
                        }

                    }
                });
                btn.setPrefSize(112, 114);
                btn.setId(String.valueOf(id));
                btns[id++] = btn;
                boardPane.add(btn, i, j);

            }
        }

        Button btnSave = new Button("save");
//        btnSave.setId("buttonSave");
        boardPane.add(btnSave, 1, 4);
        btnSave.setOnAction((ActionEvent event) -> {
            if ("multi".equals(Mode)) {
                game.saveGame();
            } else if ("single".equals(Mode)) {
                singleGame.saveGame();
            }
        });

        String css = TicTacToe.class.getResource("style.css").toExternalForm();
        boardPane.getStylesheets().add(css);
        boardPane.setAlignment(Pos.CENTER);

        primaryStage.setTitle(
                "Tic-Tac-Toe");
        primaryStage.setScene(boardScene);
    }

    void LoadBoard() {

        GridPane boardPane = new GridPane();
        boardPane.setPadding(new Insets(10, 10, 12, 12));
        boardPane.setVgap(10);
        boardPane.setHgap(10);
        boardPane.setId("board");
        Scene boardScene = new Scene(boardPane, 380, 385);
        String css = TicTacToe.class.getResource("style.css").toExternalForm();
        boardScene.getStylesheets().add(css);
        int id = 0;
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                Button btn = new Button();
                btn.setId(String.valueOf(id));
                btn.setPrefSize(112, 114);
                boardPane.add(btn, i, j);
                btns[id++] = btn;
            }
        }
//        Button btnHome = new Button();
//        btnHome.setId("btnHome");
//        boardPane.add(btnHome, 2, 4);
//        btnHome.setOnAction((ActionEvent event) -> {
//            try {
//                String Dir = System.getProperty("user.dir");
//                File media = new File(Dir, "/src/tictactoe/resources/images/button.wav");
//                String mediaUrl;
//                mediaUrl = media.toURI().toURL().toString();
//                Media musicFile = new Media(mediaUrl);
//                mediaplayer = new MediaPlayer(musicFile);
//                mediaplayer.setAutoPlay(true);
//                HomeGame();
//            } catch (MalformedURLException ex) {
//                Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        });
        Button btnNext = new Button("Next");
        btnNext.setId("button");
        boardPane.add(btnNext, 1, 4);

        Button btnPrev = new Button("Prev");
        btnPrev.setId("button");
        boardPane.add(btnPrev, 2, 4);
        btnNext.setOnAction((ActionEvent event) -> {
            try {
                String Dir = System.getProperty("user.dir");
                File media = new File(Dir, "/src/tictactoe/resources/images/button.wav");
                String mediaUrl;
                mediaUrl = media.toURI().toURL().toString();
                Media musicFile = new Media(mediaUrl);
                mediaplayer = new MediaPlayer(musicFile);
                mediaplayer.setAutoPlay(true);
                game.nextMove();
            } catch (MalformedURLException ex) {
                Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        btnPrev.setOnAction((ActionEvent event) -> {
            try {
                String Dir = System.getProperty("user.dir");
                File media = new File(Dir, "/src/tictactoe/resources/images/button.wav");
                String mediaUrl;
                mediaUrl = media.toURI().toURL().toString();
                Media musicFile = new Media(mediaUrl);
                mediaplayer = new MediaPlayer(musicFile);
                mediaplayer.setAutoPlay(true);
                game.prevMove();
            } catch (MalformedURLException ex) {
                Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        primaryStage.setTitle("Tic-Tac-Toe");
        primaryStage.setScene(boardScene);
    }

    void Result(String piece, String res) {
        MediaPlayer player;
        if ("win".equals(res)) {

            try {
                System.out.println("win");
                FlowPane winningPane = new FlowPane();
                winningPane.setAlignment(Pos.CENTER);
                winningPane.setId("winning");
                Text t = new Text(piece + " WON");
                DropShadow ds = new DropShadow();
                ds.setOffsetY(3.0f);
                ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
                t.setEffect(ds);
                t.setCache(true);
                t.setX(10.0f);
                t.setY(270.0f);
                t.setFill(Color.GREEN);
                t.setFont(Font.font("Cambria", FontPosture.ITALIC, 25));
//                Button btnHome = new Button();
//                btnHome.setId("btnHome");
//                btnHome.setOnAction((ActionEvent event) -> {
//                    try {
//
//                        String Dir = System.getProperty("user.dir");
//                        File media = new File(Dir, "/src/tictactoe/resources/images/button.wav");
//                        String mediaUrl;
//                        mediaUrl = media.toURI().toURL().toString();
//                        Media musicFile = new Media(mediaUrl);
//                        mediaplayer = new MediaPlayer(musicFile);
//                        mediaplayer.setAutoPlay(true);
//                        HomeGame();
//                    } catch (MalformedURLException ex) {
//                        Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//
//                });
                Button playAgainButton = new Button();
                playAgainButton.setId("button");
                playAgainButton.setText("Play Again");

                String workingDir = System.getProperty("user.dir");

                File video = new File(workingDir, "/src/tictactoe/resources/images/video.mp4");
                String url;
                url = video.toURI().toURL().toString();
                Media media = new Media(url);
                player = new MediaPlayer(media);
                player.play();
                MediaView mediaView = new MediaView(player);
                mediaView.setFitWidth(380);
                playAgainButton.setOnAction((ActionEvent event) -> {
                    try {
                        player.pause();
                        String Dir = System.getProperty("user.dir");
                        File media2 = new File(Dir, "/src/tictactoe/resources/images/button.wav");
                        String mediaUrl;
                        mediaUrl = media2.toURI().toURL().toString();
                        Media musicFile = new Media(mediaUrl);
                        mediaplayer = new MediaPlayer(musicFile);
                        mediaplayer.setAutoPlay(true);
                        Board();//choose symbol

                    } catch (MalformedURLException ex) {
                        Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                Button btnSave = new Button("save");
                btnSave.setId("button");

                btnSave.setOnAction((ActionEvent event) -> {
                    if ("multi".equals(Mode)) {
                        game.saveGame();
                    } else if ("single".equals(Mode)) {
                        singleGame.saveGame();
                    }
                });

                HBox box = new HBox(5);

                box.setPadding(new Insets(10, 15, 4, 5));

                box.getChildren().addAll(t);
                winningPane.getChildren().addAll(box, mediaView, btnSave, playAgainButton);
                Scene winningScene = new Scene(winningPane, 380, 400);
                String css = TicTacToe.class
                        .getResource("style.css").toExternalForm();
                winningScene.getStylesheets().add(css);
                primaryStage.setTitle("Result");
                primaryStage.setScene(winningScene);
            } catch (MalformedURLException ex) {

            }
        } else if ("lose".equals(res)) {

            FlowPane winningPane = new FlowPane();
            winningPane.setId("winning");
//            Button btnHome = new Button();
//            btnHome.setId("btnHome");
//            btnHome.setOnAction((ActionEvent event) -> {
//                try {
//                    String Dir = System.getProperty("user.dir");
//                    File media = new File(Dir, "/src/tictactoe/resources/images/button.wav");
//                    String mediaUrl;
//                    mediaUrl = media.toURI().toURL().toString();
//                    Media musicFile = new Media(mediaUrl);
//                    mediaplayer = new MediaPlayer(musicFile);
//                    mediaplayer.setAutoPlay(true);
//                    HomeGame();
//                } catch (MalformedURLException ex) {
//                    Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
//                }
//
//            });
            Button playAgainButton = new Button();
            playAgainButton.setId("button");
            playAgainButton.setText("Play Again");

            Image image = new Image(getClass().getResourceAsStream("lose.png"), 180, 150, true, true);

            ImageView iv1 = new ImageView(image);
            iv1.setImage(image);

            playAgainButton.setOnAction((ActionEvent event) -> {

                try {

                    String Dir = System.getProperty("user.dir");
                    File media2 = new File(Dir, "/src/tictactoe/resources/images/button.wav");
                    String mediaUrl;
                    mediaUrl = media2.toURI().toURL().toString();
                    Media musicFile = new Media(mediaUrl);
                    mediaplayer = new MediaPlayer(musicFile);
                    mediaplayer.setAutoPlay(true);
                    Board();//choose symbol

                } catch (MalformedURLException ex) {
                    Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            Button btnSave = new Button("save");
            btnSave.setId("button");

            btnSave.setOnAction((ActionEvent event) -> {
                if ("multi".equals(Mode)) {
                    game.saveGame();
                } else if ("single".equals(Mode)) {
                    singleGame.saveGame();
                }
            });
            VBox box = new VBox(15);
            box.setAlignment(Pos.CENTER);
            box.setPadding(new Insets(90, 20, 20, 90));
            box.getChildren().addAll(iv1, btnSave, playAgainButton);
            winningPane.getChildren().add(box);

            Scene winningScene = new Scene(winningPane, 380, 400);
            String css = TicTacToe.class
                    .getResource("style.css").toExternalForm();
            winningScene.getStylesheets().add(css);
            primaryStage.setTitle("Result");
            primaryStage.setScene(winningScene);

        } //draw
        else if ("draw".equals(res)) {

            FlowPane winningPane = new FlowPane();
            winningPane.setId("winning");

            Button playAgainButton = new Button();
            playAgainButton.setId("button");
            playAgainButton.setText("Play Again");
            Text drawTxt = new Text("Draw");
            DropShadow ds = new DropShadow();
            ds.setOffsetY(3.0f);
            ds.setColor(Color.color(0.4f, 0.4f, 0.4f));
            drawTxt.setEffect(ds);
            drawTxt.setCache(true);
            drawTxt.setX(10.0f);
            drawTxt.setY(270.0f);
            drawTxt.setFill(Color.RED);
            drawTxt.setFont(Font.font("Cambria", FontPosture.ITALIC, 25));

            playAgainButton.setOnAction((ActionEvent event) -> {

                try {

                    String Dir = System.getProperty("user.dir");
                    File media2 = new File(Dir, "/src/tictactoe/resources/images/button.wav");
                    String mediaUrl;
                    mediaUrl = media2.toURI().toURL().toString();
                    Media musicFile = new Media(mediaUrl);
                    mediaplayer = new MediaPlayer(musicFile);
                    mediaplayer.setAutoPlay(true);
                    Board();//choose symbol

                } catch (MalformedURLException ex) {
                    Logger.getLogger(TicTacToe.class.getName()).log(Level.SEVERE, null, ex);
                }
            });

            Button btnSave = new Button("save");
            btnSave.setId("button");

            btnSave.setOnAction((ActionEvent event) -> {
                if ("multi".equals(Mode)) {
                    game.saveGame();
                } else if ("single".equals(Mode)) {
                    singleGame.saveGame();
                }
            });
            VBox box = new VBox(10);
            box.setAlignment(Pos.CENTER);
            box.setPadding(new Insets(90, 20, 20, 90));
            box.getChildren().addAll(drawTxt, playAgainButton, btnSave);
            winningPane.getChildren().add(box);

            Scene winningScene = new Scene(winningPane, 380, 400);
            String css = TicTacToe.class
                    .getResource("style.css").toExternalForm();
            winningScene.getStylesheets().add(css);
            primaryStage.setTitle("Result");
            primaryStage.setScene(winningScene);

        }

    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        HomeGame();
        myApp = this;
    }

    public static void main(String[] args) {
        launch(args);

    }
}

//==============================================================================
class Game {

    Player currentPlayer;
    final Player[] players = new Player[2];
    final String[] moves = new String[9]; //moves of the game to be recorded
    int movesCounter = 0;
    String result;

    static File fileopen;
    static ArrayList<Integer> playerMoves = new ArrayList<>();
    static int showCounter = 1;
    static int flag = 1;

    static enum State {
        Blank, X, O
    };
    State[][] board = new State[3][3];

    static final Image X = new Image("/tictactoe/resources/images/X.png", 90, 90, true, true);
    static final Image O = new Image("/tictactoe/resources/images/O.png", 90, 90, true, true);

    Game() {
        players[0] = new Player(X, State.X);
        players[1] = new Player(O, State.O);
        this.currentPlayer = players[0];
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                board[j][i] = State.Blank;
            }
        }
    }

    int tickHandle(Button btn) {
        if (btn.getGraphic() == null) {
            currentPlayer.makeTick(btn);
            moves[movesCounter++] = btn.getId();
            board[GridPane.getRowIndex(btn)][GridPane.getColumnIndex(btn)] = currentPlayer.piece;
            int checkCase = checkResult(btn);
            if (checkCase == 2) {
                switchPlayer();
                return 2;
            } else if (checkCase == 3) {
                return 3;
            }
            return 1;
        }
        return 0;
    }

    int checkResult(Button btn) {
        int x = GridPane.getRowIndex(btn);
        int y = GridPane.getColumnIndex(btn);
        State s = currentPlayer.piece;

        //check end conditions
        //check col
        for (int i = 0; i < 3; i++) {
            if (board[x][i] != s) {
                break;
            }
            if (i == 2) {
                result = String.valueOf(s);
                return 1;
                //report win for s
            }
        }

        //check row
        for (int i = 0; i < 3; i++) {
            if (board[i][y] != s) {
                break;
            }
            if (i == 3 - 1) {
                //report win for s
                result = String.valueOf(s);

                return 1;
            }
        }

        //check diag
        if (x == y) {
            //we're on a diagonal
            for (int i = 0; i < 3; i++) {
                if (board[i][i] != s) {
                    break;
                }
                if (i == 3 - 1) {
                    //report win for s
                    result = String.valueOf(s);

                    return 1;
                }
            }
        }

        //check anti diag (thanks rampion)
        if (x + y == 2) {
            for (int i = 0; i < 3; i++) {
                if (board[i][(2) - i] != s) {
                    break;
                }
                if (i == 2) {
                    //report win for s
                    result = String.valueOf(s);

                    return 1;
                }
            }
        }

        //check draw
        if (movesCounter == (9)) {
            //report draw
            result = "draw";

            return 3;
        }
        return 2;
    }

    void switchPlayer() {
        if (currentPlayer == players[0]) {
            currentPlayer = players[1];
        } else {
            currentPlayer = players[0];
        }
    }

    static void loadGames() {
        FileChooser f = new FileChooser();
        fileopen = f.showOpenDialog(TicTacToe.primaryStage);
        try {
            if (fileopen == null) {
                System.out.println("Not opened");
            } else {
                FileInputStream fis = new FileInputStream(fileopen);

                int counter = 0;
                FileReader fileReader = null;
                String line = null;
                fileReader = new FileReader(fileopen);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                while ((line = bufferedReader.readLine()) != null) {
                    int btnPosition = Integer.parseInt(line);
                    if (counter == 0) {
                        TicTacToe.btns[btnPosition].setGraphic(new ImageView(X));
                    }
                    playerMoves.add(counter, btnPosition);
                    // playerMoves[counter] = btnPosition;
                    counter++;
                    flag = -flag;
                }
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void saveGame() {

        ComputerPlayer computer = new ComputerPlayer();
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(TicTacToe.primaryStage);

        try {
            if (file == null) {
                System.out.println("Not Saved");
            } else {
                try {
                    FileOutputStream fos = new FileOutputStream(file);
                    for (int i = 0; i < moves.length; i++) {
                        fos.write(moves[i].getBytes());
                        fos.write("\n".getBytes());
                    }
                } catch (NullPointerException ex) {
                    ex.printStackTrace();

                }
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    static void nextMove() {
        if (showCounter < 0) {
            showCounter = 0;
        }

        if (showCounter >= 0 && showCounter < playerMoves.size()) {
            if (showCounter == playerMoves.size()) {
                showCounter = playerMoves.size() - 1;
            }
            if (flag == 1) {
                TicTacToe.btns[playerMoves.get(showCounter)].setGraphic(new ImageView(O));
            } else if (flag == -1) {
                TicTacToe.btns[playerMoves.get(showCounter)].setGraphic(new ImageView(X));
            }
            ++showCounter;
            flag = -flag;

        }
    }

    static void prevMove() {

        if (showCounter > playerMoves.size() - 1) {
            showCounter = playerMoves.size() - 1;
        }
        if (showCounter < playerMoves.size()) {
            if (showCounter == -1) {
                showCounter = 0;
            }
            TicTacToe.btns[playerMoves.get(showCounter)].setGraphic(null);
            showCounter--;

        }

    }
}

//==============================================================================
class Player {

    private int id;
    private String name;
    private boolean status;
    private final Image pieceImg;
    final Game.State piece;

    Player(Image pieceImg, Game.State piece) {
        this.pieceImg = pieceImg;
        this.piece = piece;
    }

    void makeTick(Button btn) {
        btn.setGraphic(new ImageView(pieceImg));
    }

}

//==============================================================================
class ComputerPlayer {

    private int id;
    private String name;
    private boolean status;
    private final Image pieceImg;
    final Game.State piece;
    Random rand = new Random();
    int x;

    private final String[] moves = new String[9]; //moves of the game to be recorded
    private int movesCounter = 0;

    ComputerPlayer(Image pieceImg, Game.State piece) {
        this.pieceImg = pieceImg;
        this.piece = piece;
    }

    public ComputerPlayer() {
        this.pieceImg = null;
        this.piece = null;
    }

    Button makeTick() {
        do {
            x = rand.nextInt(9);
        } while (TicTacToe.btns[x].getGraphic() != null);

        TicTacToe.btns[x].setGraphic(new ImageView(pieceImg));

        return TicTacToe.btns[x];
    }
}
//==============================================================================

class SingleGame {

    ComputerPlayer computer = new ComputerPlayer();
    private Player player;
    ComputerPlayer PC;
    String result;

    private Player currentPlayer;
    private final Player[] players = new Player[2];
    private final String[] moves = new String[9]; //moves of the game to be recorded
    private int movesCounter = 0;

    static File fileopen;
    static int[] playerMoves = new int[9];
    static int showCounter = 1;
    static int flag = 1;

    Game.State[][] board = new Game.State[3][3];

    private static final Image X = new Image("/tictactoe/resources/images/X.png", 90, 90, true, true);
    private static final Image O = new Image("/tictactoe/resources/images/O.png", 90, 90, true, true);

    SingleGame(char s) {
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                board[j][i] = Game.State.Blank;
            }
        }
        if (s == 'x') {
            player = new Player(X, Game.State.X);
            PC = new ComputerPlayer(O, Game.State.O);
        } else if (s == 'o') {
            player = new Player(O, Game.State.O);
            PC = new ComputerPlayer(X, Game.State.X);
        }

    }

    int tickHandle(Button btn) {
        if (btn.getGraphic() == null) {
            player.makeTick(btn);
            moves[movesCounter++] = btn.getId();
            board[GridPane.getRowIndex(btn)][GridPane.getColumnIndex(btn)] = player.piece;

            int checkPlayerCase = checkResult(btn, player.piece);
            if (checkPlayerCase == 1) {
                return 1;
            } else if (checkPlayerCase == 3) {
                return 4;
            }

            Button pcbtn = PC.makeTick();
            moves[movesCounter++] = pcbtn.getId();
            board[GridPane.getRowIndex(pcbtn)][GridPane.getColumnIndex(pcbtn)] = PC.piece;
            int checkComCase = checkResult(pcbtn, PC.piece);
            if (checkComCase == 1) {
                return 2;
            }

            return 3;
        }
        return 0;
    }

    int checkResult(Button btn, Game.State s) {
        int x = GridPane.getRowIndex(btn);
        int y = GridPane.getColumnIndex(btn);

        //check end conditions
        //check col
        for (int i = 0; i < 3; i++) {
            if (board[x][i] != s) {
                break;
            }
            if (i == 2) {

//                System.out.println(String.valueOf(s) + "win");
                result = String.valueOf(s);
                return 1;
                //report win for s
            }
        }

        //check row
        for (int i = 0; i < 3; i++) {
            if (board[i][y] != s) {
                break;
            }
            if (i == 3 - 1) {
                //report win for s
                result = String.valueOf(s);
                return 1;
            }
        }

        //check diag
        if (x == y) {
            //we're on a diagonal
            for (int i = 0; i < 3; i++) {
                if (board[i][i] != s) {
                    break;
                }
                if (i == 3 - 1) {
                    //report win for s
                    result = String.valueOf(s);
                    return 1;
                }
            }
        }

        //check anti diag (thanks rampion)
        if (x + y == 2) {
            for (int i = 0; i < 3; i++) {
                if (board[i][(2) - i] != s) {
                    break;
                }
                if (i == 2) {
                    //report win for s
                    result = String.valueOf(s);
                    return 1;
                }
            }
        }

        //check draw
        if (movesCounter == (9)) {
            //report draw
            result = String.valueOf(s);
            return 3;
        }
        return 2;
    }

    boolean checkDraw(Button btn) {
        int x = GridPane.getRowIndex(btn);
        int y = GridPane.getColumnIndex(btn);

        if (movesCounter == (9)) {
            //report draw
//            result = String.valueOf(s);
            return true;
        }
        return false;
    }

    void saveGame() {
        try {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(TicTacToe.primaryStage);
            //handle not saved  exception
            if (file == null) {
                System.out.println("Not Saved");
            } else {
                FileOutputStream fos = new FileOutputStream(file);
                for (int i = 0; i < moves.length; i++) {
                    fos.write(moves[i].getBytes());
                    fos.write("\n".getBytes());
                }
            }

        } catch (NullPointerException ex) {
            ex.printStackTrace();

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
//=================================================================================

class OnlineGame extends Game {

    Player me;
    Player opponent;
    Socket mySocket;
    DataInputStream dis;
    PrintStream ps;
    String recieved;
    String username;
    boolean status = false; //if true start the game else wait for opponent
    Alert waitAlert = new Alert(Alert.AlertType.INFORMATION);
    int checkOpponent;

    OnlineGame() {
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3; i++) {
                board[j][i] = State.Blank;
            }
        }

        try {
            mySocket = new Socket("127.0.0.1", 5005);
            dis = new DataInputStream(mySocket.getInputStream());
            ps = new PrintStream(mySocket.getOutputStream());

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Thread th = new Thread(() -> {
            while (true) {
                try {
                    handleMsg(dis.readLine());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        th.start();
        prompt();
        sendMsg(username);
    }

    @Override
    int tickHandle(Button btn) {
        if (btn.getGraphic() == null && currentPlayer == me) {
            ps.println("move ".concat(btn.getId()));
            me.makeTick(btn);
            moves[movesCounter++] = btn.getId();
            board[GridPane.getRowIndex(btn)][GridPane.getColumnIndex(btn)] = me.piece;

            currentPlayer = opponent;
            int checkPlayerCase = checkResult(btn, me.piece);
            if (checkPlayerCase == 1) {
                return 1;
            } else if (checkPlayerCase == 3) {
                return 4;
            }
            return 3;

        } else {
            Alert invalid = new Alert(Alert.AlertType.INFORMATION);
            invalid.setTitle("Invalid Move");
            invalid.setHeaderText("Invalid Move");
            invalid.show();
        }
        return 0;
    }

    int playOpponentTick(Button btn) {
        Platform.runLater(() -> {
            opponent.makeTick(btn);
        });

        moves[movesCounter++] = btn.getId();
        board[GridPane.getRowIndex(btn)][GridPane.getColumnIndex(btn)] = opponent.piece;
        currentPlayer = me;
        int checkPlayerCase = checkResult(btn, opponent.piece);
        if (checkPlayerCase == 1) {
            return 1;
        } else if (checkPlayerCase == 3) {
            return 4;
        }
        return 3;
    }

    void sendMsg(String msg) {
        ps.println(msg);
    }

    void handleMsg(String msg) {
        if ("you've successfully registered".equals(msg) || "you've successfully logged in".equals(msg)) {
            Platform.runLater(() -> {
                waitAlert.setTitle("Status");
                waitAlert.setHeaderText(msg);
                waitAlert.setContentText("Please wait for an opponent to connect");
                waitAlert.showAndWait();
            });

        } else if (msg.startsWith("start")) {
            status = true;
            switch (msg.substring(6)) {
                case "X":
                    me = new Player(X, State.X);
                    opponent = new Player(O, State.O);
                    currentPlayer = me;
                    break;
                case "O":
                    me = new Player(O, State.O);
                    opponent = new Player(X, State.X);
                    currentPlayer = opponent;
                    break;
            }
        } else if (msg.startsWith("move")) {
            Button btn = TicTacToe.btns[Integer.valueOf(msg.substring(5))];
            checkOpponent = playOpponentTick(btn);
            Platform.runLater(() -> {
                int y = checkOpponent;
                if (y == 1) {
                    TicTacToe.myApp.Result(result, "lose");
                } else if (y == 2) {
                    TicTacToe.myApp.Result(result, "win");
                } else if (y == 4) {
                    TicTacToe.myApp.Result(result, "draw");
                }

            });

        } else if (msg == "lose") {
            System.out.println(msg);
        } else {
            System.out.println(msg);

        }
    }

    void prompt() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Registration/Login");
        dialog.setContentText("Please enter your username:");
        dialog.setGraphic(null);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            username = result.get();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("You have to enter your username to be able to \n play online");
            alert.showAndWait();
            prompt();
        }
    }

    int checkResult(Button btn, Game.State s) {
        int x = GridPane.getRowIndex(btn);
        int y = GridPane.getColumnIndex(btn);

        //check end conditions
        //check col
        for (int i = 0; i < 3; i++) {
            if (board[x][i] != s) {
                break;
            }
            if (i == 2) {

//                System.out.println(String.valueOf(s) + "win");
                result = String.valueOf(s);
                return 1;
                //report win for s
            }
        }

        //check row
        for (int i = 0; i < 3; i++) {
            if (board[i][y] != s) {
                break;
            }
            if (i == 3 - 1) {
                //report win for s
                result = String.valueOf(s);
                return 1;
            }
        }

        //check diag
        if (x == y) {
            //we're on a diagonal
            for (int i = 0; i < 3; i++) {
                if (board[i][i] != s) {
                    break;
                }
                if (i == 3 - 1) {
                    //report win for s
                    result = String.valueOf(s);
                    return 1;
                }
            }
        }

        //check anti diag (thanks rampion)
        if (x + y == 2) {
            for (int i = 0; i < 3; i++) {
                if (board[i][(2) - i] != s) {
                    break;
                }
                if (i == 2) {
                    //report win for s
                    result = String.valueOf(s);
                    return 1;
                }
            }
        }

        //check draw
        if (movesCounter == (9)) {
            //report draw
            result = String.valueOf(s);
            return 3;
        }
        return 2;
    }

    boolean checkDraw(Button btn) {
        int x = GridPane.getRowIndex(btn);
        int y = GridPane.getColumnIndex(btn);

        if (movesCounter == (9)) {
            //report draw
//            result = String.valueOf(s);
            return true;
        }
        return false;
    }

}
