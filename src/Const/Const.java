package Const;

import javafx.scene.paint.Color;

public class Const {

//    public static final String DB_HOST = "php.scweb.ca";
//    public static final String DB_NAME = "adalidb";
//    public static final String DB_USER = "adali";
//    public static final String DB_PASS = "j54czj54czhm4v1hm4v1";

    // Key for preferences
    public static final String DB_HOST = "host";
    public static final String DB_NAME = "database";
    public static final String DB_USER = "username";
    public static final String DB_PASS = "password";

    public static final byte CLOSED = 0;
    public static final byte OPEN = 1;

    public static final int SUCCESS = 1;
    public static final int EXIST = 0;
    public static final int FAILED = -1;
    public static final int NOT_FOUND = -2;


    // TABLES NAMES
    public static final String TABLE_PROJECT = "projects";
    public static final String TABLE_TASKS = "tasks";
    public static final String TABLE_STATUS = "status";
    public static final String TABLE_CATEGORY = "categories";
    public static final String TABLE_PRIORITY = "priorities";


    // TABLE PROJECT
    public static final String PROJECT_COLUMN_ID = "id";
    public static final String PROJECT_COLUMN_TITLE = "title";
    public static final String PROJECT_COLUMN_DESCRIPTION = "description";
    public static final String PROJECT_COLUMN_STATUS = "status";
    public static final String PROJECT_COLUMN_CATEGORY = "category";
    public static final String PROJECT_COLUMN_PRIORITY = "priority";
    public static final String PROJECT_COLUMN_START_DATE = "start_date";
    public static final String PROJECT_COLUMN_DUE_DATE = "due_date";
    public static final String PROJECT_COLUMN_OPEN = "open";

    // TABLE TASK
    public static final String TASK_COLUMN_ID = "id";
    public static final String TASK_COLUMN_NAME = "name";
    public static final String TASK_COLUMN_PROJECT = "project";
    public static final String TASK_COLUMN_OPEN = "open";

    // TABLES CATEGORY - STATUS - PRIORITY
    public static final String CSP_COLUMN_ID = "id";
    public static final String CSP_COLUMN_NAME = "name";
    public static final String CSP_COLUMN_COLOR = "color";


    // CREATE PROJECT TABLE
    public static final String CREATE_TABLE_PROJECT = "CREATE TABLE IF NOT EXISTS `" + TABLE_PROJECT + "` (\n" +
            "  `" + PROJECT_COLUMN_ID + "` INT PRIMARY KEY AUTO_INCREMENT,\n" +
            "  `" + PROJECT_COLUMN_TITLE + "` VARCHAR(255),\n" +
            "  `" + PROJECT_COLUMN_DESCRIPTION + "` VARCHAR(255),\n" +
            "  `" + PROJECT_COLUMN_STATUS + "` INT NOT NULL,\n" +
            "  `" + PROJECT_COLUMN_CATEGORY + "` INT NOT NULL,\n" +
            "  `" + PROJECT_COLUMN_PRIORITY + "` INT NOT NULL,\n" +
            "  `" + PROJECT_COLUMN_START_DATE + "` DATE NOT NULL,\n" +
            "  `" + PROJECT_COLUMN_DUE_DATE + "` DATE NOT NULL\n," +
            "  `" + PROJECT_COLUMN_OPEN + "` TINYINT NOT NULL DEFAULT 1\n" +
            ");";


    // CREATE TASKS TABLE
    public static final String CREATE_TABLE_TASKS = "CREATE TABLE IF NOT EXISTS `" + TABLE_TASKS + "` (\n" +
            "  `" + TASK_COLUMN_ID + "` INT PRIMARY KEY AUTO_INCREMENT,\n" +
            "  `" + TASK_COLUMN_NAME + "` VARCHAR(255) NOT NULL,\n" +
            "  `" + TASK_COLUMN_PROJECT + "` INT NOT NULL,\n" +
            "  `" + TASK_COLUMN_OPEN + "` TINYINT NOT NULL DEFAULT 1\n" +
            ");";


    // CREATE CATEGORY TABLE
    public static final String CREATE_TABLE_CATEGORY = "CREATE TABLE IF NOT EXISTS `" + TABLE_CATEGORY + "` (\n" +
            "  `" + CSP_COLUMN_ID + "` INT PRIMARY KEY AUTO_INCREMENT,\n" +
            "  `" + CSP_COLUMN_NAME + "` VARCHAR(255),\n" +
            "  `" + CSP_COLUMN_COLOR + "` VARCHAR(255)\n" +
            ");";

    // CREATE STATUS TABLE
    public static final String CREATE_TABLE_STATUS = "CREATE TABLE IF NOT EXISTS `" + TABLE_STATUS + "` (\n" +
            "  `" + CSP_COLUMN_ID + "` INT PRIMARY KEY AUTO_INCREMENT,\n" +
            "  `" + CSP_COLUMN_NAME + "` VARCHAR(255),\n" +
            "  `" + CSP_COLUMN_COLOR + "` VARCHAR(255)\n" +
            ");";

    // CREATE PRIORITY TABLE
    public static final String CREATE_TABLE_PRIORITY = "CREATE TABLE IF NOT EXISTS `" + TABLE_PRIORITY + "` (\n" +
            "  `" + CSP_COLUMN_ID + "` INT PRIMARY KEY AUTO_INCREMENT,\n" +
            "  `" + CSP_COLUMN_NAME + "` VARCHAR(255),\n" +
            "  `" + CSP_COLUMN_COLOR + "` VARCHAR(255)\n" +
            ");";


    // ALTER TABLES TO ADD FOREIGN KEYS
    public static final String[] ALTER_TABLES_FOREIGN_KEYS = {

             "ALTER TABLE `" + TABLE_PROJECT + "` ADD FOREIGN KEY (`" + PROJECT_COLUMN_STATUS + "`) REFERENCES `" + TABLE_STATUS + "` (`" + CSP_COLUMN_ID + "`);\n"
            ,
            "ALTER TABLE `" + TABLE_PROJECT + "` ADD FOREIGN KEY (`" + PROJECT_COLUMN_CATEGORY + "`) REFERENCES `" + TABLE_CATEGORY + "` (`" + CSP_COLUMN_ID + "`);\n"
            ,
            "ALTER TABLE `" + TABLE_PROJECT + "` ADD FOREIGN KEY (`" + PROJECT_COLUMN_PRIORITY + "`) REFERENCES `" + TABLE_PRIORITY + "` (`" + CSP_COLUMN_ID + "`);\n"
            ,
            "ALTER TABLE `" + TABLE_TASKS + "` ADD FOREIGN KEY (`" + TASK_COLUMN_PROJECT + "`) REFERENCES `" + TABLE_PROJECT + "` (`" + PROJECT_COLUMN_ID + "`);"

    };

    public static String toRGBCode(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }


}
