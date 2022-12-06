// Created by Eric B. 29.11.2022 18:49
package de.ericzones.permissionsystem.global.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SqlAdapter {

    private final SqlConnection connection;

    /*
     Converting java methods to sql commands
     */

    public SqlAdapter(SqlConnection sqlConnection) {
        this.connection = sqlConnection;
    }

    public void createTable(String table, Pair<String, SqlDataType>[] columns) {
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS "+table+" (");
        for(int i = 0; i < columns.length; i++)
            builder.append(columns[i].getFirstObject()).append(" ").append(columns[i].getSecondObject().getTag()).append(i + 1 >= columns.length ? ")" : ", ");
        connection.update(builder.toString());
    }

    public void createTable(String table, Pair<String, SqlDataType>[] columns, String primaryKey) {
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS "+table+" (");
        for(int i = 0; i < columns.length; i++)
            builder.append(columns[i].getFirstObject()).append(" ").append(columns[i].getSecondObject().getTag()).append(", ");
        builder.append("PRIMARY KEY(").append(primaryKey).append("))");
        connection.update(builder.toString());
    }

    public void createTable(String table, Pair<String, SqlDataType>[] columns, String primaryKey, boolean autoNumbers) {
        if(!autoNumbers) {
            createTable(table, columns, primaryKey);
            return;
        }
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS "+table+" (");
        for(int i = 0; i < columns.length; i++) {
            if(i == 0)
                builder.append(columns[i].getFirstObject()).append(" ").append(columns[i].getSecondObject().getTag()).append(" NOT NULL AUTO_INCREMENT").append(", ");
            else
                builder.append(columns[i].getFirstObject()).append(" ").append(columns[i].getSecondObject().getTag()).append(", ");
        }
        builder.append("PRIMARY KEY(").append(primaryKey).append("))");
        connection.update(builder.toString());
    }

    public void updateInTable(String table, Pair<String, Object>[] conditions, String column, Object value) {
        StringBuilder builder = new StringBuilder("UPDATE "+table+" SET "+column+"=");
        if(value instanceof String)
            builder.append("'").append(value).append("'");
        else
            builder.append(value);
        for(int i = 0; i < conditions.length; i++) {
            if(i == 0)
                builder.append(" WHERE ");
            if(conditions[i].getSecondObject() instanceof String)
                builder.append(conditions[i].getFirstObject()).append("='").append(conditions[i].getSecondObject()).append("'").append(i + 1 >= conditions.length ? "" : " AND ");
            else
                builder.append(conditions[i].getFirstObject()).append("=").append(conditions[i].getSecondObject()).append(i + 1 >= conditions.length ? "" : " AND ");
        }
        connection.update(builder.toString());
    }

    public void addToTable(String table, Pair<String, Object>[] columnValues) {
        StringBuilder builder = new StringBuilder("INSERT INTO "+table+" (");
        for(int i = 0; i < columnValues.length; i++)
            builder.append(columnValues[i].getFirstObject()).append(i + 1 >= columnValues.length ? ") " : ", ");
        for(int i = 0; i < columnValues.length; i++) {
            if(i == 0)
                builder.append("VALUES (");
            if(columnValues[i].getSecondObject() instanceof String) {
                builder.append("'").append(columnValues[i].getSecondObject()).append(i + 1 >= columnValues.length ? "')" : "', ");
            } else {
                builder.append(columnValues[i].getSecondObject()).append(i + 1 >= columnValues.length ? ")" : ", ");
            }
        }
        connection.update(builder.toString());
    }

    public void addToTable(String table, List<String> columns, List<Object> values) {
        Pair<String, Object>[] columnValues = new Pair[columns.size()];
        for(int i = 0; i < columns.size(); i++)
            columnValues[i] = new Pair<>(columns.get(i), values.get(i));
        addToTable(table, columnValues);
    }

    public void removeFromTable(String table, Pair<String, Object>[] conditions) {
        StringBuilder builder = new StringBuilder("DELETE FROM "+table+" WHERE ");
        for(int i = 0; i < conditions.length; i++) {
            if(conditions[i].getSecondObject() instanceof String)
                builder.append(conditions[i].getFirstObject()).append("='").append(conditions[i].getSecondObject()).append(i + 1 >= conditions.length ? "'" : "' AND ");
            else
                builder.append(conditions[i].getFirstObject()).append("=").append(conditions[i].getSecondObject()).append(i + 1 >= conditions.length ? "" : " AND ");
        }
        connection.update(builder.toString());
    }

    public boolean existsInTable(String table, Pair<String, Object>[] conditions) {
        StringBuilder builder = new StringBuilder("SELECT * FROM "+table);
        for(int i = 0; i < conditions.length; i++) {
            if(i == 0)
                builder.append(" WHERE ");
            if(conditions[i].getSecondObject() instanceof String)
                builder.append(conditions[i].getFirstObject()).append("='").append(conditions[i].getSecondObject()).append(i + 1 >= conditions.length ? "'" : "' AND ");
            else
                builder.append(conditions[i].getFirstObject()).append("=").append(conditions[i].getSecondObject()).append(i + 1 >= conditions.length ? "" : " AND ");
        }
        ResultSet resultSet = connection.getResult(builder.toString());
        try {
            if(resultSet.next())
                return true;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public List<Pair<String, Object>[]> getDataFromTable(String table, String[] columns, Pair<String, Object>[] conditions) {
        StringBuilder builder = new StringBuilder("SELECT * FROM "+table);
        if(conditions != null) {
            for (int i = 0; i < conditions.length; i++) {
                if (i == 0)
                    builder.append(" WHERE ");
                if(conditions[i].getSecondObject() instanceof String)
                    builder.append(conditions[i].getFirstObject()).append("='").append(conditions[i].getSecondObject()).append(i + 1 >= conditions.length ? "'" : "' AND ");
                else
                    builder.append(conditions[i].getFirstObject()).append("=").append(conditions[i].getSecondObject()).append(i + 1 >= conditions.length ? "" : " AND ");
            }
        }
        ResultSet resultSet = connection.getResult(builder.toString());
        List<Pair<String, Object>[]> data = new ArrayList<>();
        try {
            while (resultSet.next()) {
                Pair<String, Object>[] pairs = new Pair[columns.length];
                for(int i = 0; i < columns.length; i++)
                    pairs[i] = new Pair<>(columns[i], resultSet.getObject(i+1));
                data.add(pairs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }

    public List<Object> getDataFromTable(String table, Pair<String, Object>[] conditions, String column) {
        StringBuilder builder = new StringBuilder("SELECT "+column+" FROM "+table);
        if(conditions != null) {
            for (int i = 0; i < conditions.length; i++) {
                if (i == 0)
                    builder.append(" WHERE ");
                if(conditions[i].getSecondObject() instanceof String)
                    builder.append(conditions[i].getFirstObject()).append("='").append(conditions[i].getSecondObject()).append(i + 1 >= conditions.length ? "'" : "' AND ");
                else
                    builder.append(conditions[i].getFirstObject()).append("=").append(conditions[i].getSecondObject()).append(i + 1 >= conditions.length ? "" : " AND ");
            }
        }
        ResultSet resultSet = connection.getResult(builder.toString());
        List<Object> data = new ArrayList<>();
        try {
            while (resultSet.next())
                data.add(resultSet.getObject(column));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return data;
    }

    public Object getObjectFromTable(String table, Pair<String, Object>[] conditions, String column) {
        StringBuilder builder = new StringBuilder("SELECT * FROM "+table);
        for(int i = 0; i < conditions.length; i++) {
            if(i == 0)
                builder.append(" WHERE ");
            if(conditions[i].getSecondObject() instanceof String)
                builder.append(conditions[i].getFirstObject()).append("='").append(conditions[i].getSecondObject()).append(i + 1 >= conditions.length ? "'" : "' AND ");
            else
                builder.append(conditions[i].getFirstObject()).append("=").append(conditions[i].getSecondObject()).append(i + 1 >= conditions.length ? "" : " AND ");
        }
        ResultSet resultSet = connection.getResult(builder.toString());
        try {
            while (resultSet.next())
                return resultSet.getObject(column);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public List<Object> getHighestObjectFromTable(String table, String column, String sortColumn, int amount) {
        StringBuilder builder = new StringBuilder("SELECT * FROM "+table);
        builder.append(" ORDER BY "+sortColumn+" DESC LIMIT "+amount);
        List<Object> objects = new ArrayList<>();
        ResultSet resultSet = connection.getResult(builder.toString());
        try {
            while (resultSet.next())
                objects.add(resultSet.getObject(column));
            return objects;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public SqlConnection getConnection() {
        return this.connection;
    }

}
