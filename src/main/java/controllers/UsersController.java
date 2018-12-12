package controllers;

import db.DBConnection;
import db.daos.MigUsuariosDao;
import db.daos.UserDao;
import db.models.MigUsuario;
import db.models.User;
import helpers.UsersHelper;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class UsersController extends RootController {

    public UsersController(DBConnection dbConnection) {
        super(dbConnection);
    }

    /* Si no existen mig Usuarios entonces retornamos false caso contrario true*/
    public boolean createUsersFromMigUsuariosTable() throws SQLException {
        DBConnection configDB = DBConnection.getInstance("development");

        MigUsuariosDao migUsuariosDao = new MigUsuariosDao(configDB.getConnectionDestino());
        UserDao usuariosDao = new UserDao(configDB.getConnectionDestino());

        List<MigUsuario> migUsuarios = migUsuariosDao.getAll();

        if(migUsuarios.isEmpty()) return false;

        List<User> usuarios = migUsuarios.stream().map(UsersHelper::getUserFromMigUsuario).collect(Collectors.toList());

        try {
            usuariosDao.saveAll(usuarios);
        } catch(NullPointerException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
