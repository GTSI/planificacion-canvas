package com.gtsi.controllers;

import java.sql.SQLException;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import business_services.users.UserCanvasService;
import db.DBConnection;
import db.models.MigUsuario;

@RestController
public class MigUsuariosController {
	
	@RequestMapping(value = "/maestrias/migusuario", method = RequestMethod.GET)
	public List<MigUsuario> getAll() throws SQLException {
	    DBConnection configDB = DBConnection.getInstance(null);

	    UserCanvasService userService = UserCanvasService.getInstance(configDB.getConnectionDestino());
	    return userService.txgetAll();
		
	}

	@RequestMapping(value = "/maestrias/migusuario/paginated", method = RequestMethod.GET, params= {"page", "size"})
	public List<MigUsuario> getPaginated(@RequestParam("page") int page, 
			@RequestParam("size") int size) throws SQLException {
	    DBConnection configDB = DBConnection.getInstance(null);

	    UserCanvasService userService = UserCanvasService.getInstance(configDB.getConnectionDestino());
	    return userService.txgetAllPaginated(page, size);
		
	}
}