/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoe_server;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;

/**
 *
 * @author islam
 */
public class TicTacToeServer {

    ServerSocket serverSocket;

    public TicTacToeServer() {

        try {
            serverSocket = new ServerSocket(5005);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        System.out.println("Tic Tac Toe Server is Running");
        while (true) {
            try {
                new PlayerHandler(serverSocket.accept());

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        new TicTacToeServer();
    }
}

class PlayerHandler {

    static PlayerHandler availablePlayer = null;
    DataInputStream dis;
    PrintStream ps;
    PlayerHandler opponent = null;

    public PlayerHandler(Socket player) {
        try {
            dis = new DataInputStream(player.getInputStream());
            ps = new PrintStream(player.getOutputStream());

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
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        makeAvailable();
    }

    void sendMsg(String msg) {
        ps.println(msg);
    }

    void handleMsg(String msg) {
        if (opponent == null && availablePlayer == null) {
            Database search = new Database("select * from player where name='" + msg + "'");
            if (search.name == null) {
                Database insert = new Database("insert into player set name='" + msg + "'");
                if (insert.affected == 1) {
                    sendMsg("you've successfully registered");
                }

            } else if (search.name.equals(msg)) {
                sendMsg("you've successfully logged in");
            }
        } else if (opponent != null) {
            opponent.ps.println(msg);
        }
        

    }

    void makeAvailable() {
        if (availablePlayer != null) {
            opponent = availablePlayer;
            opponent.opponent = this;
            availablePlayer = null;

            this.ps.println("start O");
            opponent.ps.println("start X");

        } else {
            availablePlayer = this;
        }
    }
}
//=========================================================

class Database {

    int affected;
    Statement stmt = null;
    ResultSet rs = null;
    Connection con = null;
    String name;

    public Database(String query) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/TicTacToe?useSSL=false", "islam", "iti");
            stmt = con.createStatement();
            boolean queryType = stmt.execute(query);
            if (queryType == true) { //Query is SELECT
                rs = stmt.getResultSet();
                while (rs.next()) {
                    //System.out.println(rs.getInt(1)+rs.getString(2)+rs.getString(3)+rs.getString(4));
                    name = rs.getString(2);
                }
            }
            if (queryType == false) { //Query is INSERT,UPDATE, or DELETE
                affected = stmt.getUpdateCount();
                System.out.println(affected);
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        } finally {
            try {
                stmt.close();
                con.close();

            } catch (SQLException sqlEx) {
                sqlEx.printStackTrace();
            }
        }
    }
}
