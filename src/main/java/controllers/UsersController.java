package controllers;

import db.DBConnection;

import java.sql.SQLException;

public class UsersController extends RootController {

    public UsersController(DBConnection dbConnection) {
        super(dbConnection);
    }

    /* Si no existen mig Usuarios entonces retornamos false caso contrario true*/
    public boolean createUsersFromMigUsuariosTable() throws SQLException {
      return true;
    }
}
