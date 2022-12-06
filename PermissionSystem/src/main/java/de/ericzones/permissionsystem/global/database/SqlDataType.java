// Created by Eric B. 29.11.2022 18:26
package de.ericzones.permissionsystem.global.database;

public enum SqlDataType {

    VARCHAR("VARCHAR(255)"),
    BOOLEAN("BIT"),
    BIGINT("BIGINT"),
    DOUBLE("DOUBLE(1500,500)"),
    TEXT("MEDIUMTEXT");

    private SqlDataType(String sqlTag){
        this.sqlTag = sqlTag;
    };

    private final String sqlTag;

    public String getTag() {
        return sqlTag;
    }

}
