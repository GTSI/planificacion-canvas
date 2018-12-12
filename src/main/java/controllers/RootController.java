package controllers;

import db.DBConnection;

public abstract class RootController {

  private DBConnection dbConnection;

  public RootController(DBConnection dbConnection) {
    this.dbConnection = dbConnection;
  }

  public DBConnection getDbConnection() {
    return dbConnection;
  }
}
