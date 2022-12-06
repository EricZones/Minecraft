// Created by Eric B. 29.11.2022 18:27
package de.ericzones.permissionsystem.global.database;

import de.ericzones.permissionsystem.PermissionSystem;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.Timer;
import java.util.TimerTask;

public class SqlConnection {

    private final PermissionSystem instance;

    private final Timer timer;
    private final String host, database, username, password;
    private final int port;

    private Connection connection;
    private TimerTask timerTask;

    public SqlConnection(PermissionSystem instance, String host, int port, String database, String username, String password) {
        this.instance = instance;
        this.host = host;
        this.port = port;
        this.database = database;
        this.username = username;
        this.password = password;
        this.timer = new Timer();
    }

    public void connect() {
        try {
            if(isConnected()) {
                Bukkit.getConsoleSender().sendMessage(instance.getPluginName()+" SQL is already connected");
                return;
            }
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://"+host+":"+port+"/"+database+"?useJDBCCompliantTimezoneShift=true&&useUnicode=true&autoReconnect=true", username, password);
            Bukkit.getConsoleSender().sendMessage(instance.getPluginName()+" SQL connected to "+database);
        } catch (Exception ex) {
            ex.printStackTrace();
            return;
        }
        if(timerTask != null)
            timerTask.cancel();
        timer.schedule(timerTask = new TimerTask() {
            @Override
            public void run() {
                if(!isConnected()) connect();
                getResult("SELECT 1");
            }
        }, 60*1000, 60*1000);
    }

    public boolean isConnected() {
        try {
            if(connection != null && !connection.isClosed())
                return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void disconnect() {
        try {
            if(timerTask != null) {
                timerTask.cancel();
                timerTask = null;
            }
            if(isConnected()) {
                connection.close();
                Bukkit.getConsoleSender().sendMessage(instance.getPluginName()+" SQL disconnected from "+database);
                return;
            }
            Bukkit.getConsoleSender().sendMessage(instance.getPluginName()+" SQL is already disconnected");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void update(String sqlUpdate) {
        try {
            if(!isConnected()) connect();
            PreparedStatement statement = connection.prepareStatement(sqlUpdate);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ResultSet getResult(String sqlQuery) {
        try {
            if(!isConnected()) connect();
            PreparedStatement statement = connection.prepareStatement(sqlQuery);
            return statement.executeQuery();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

}
