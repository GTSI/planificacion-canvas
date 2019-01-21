package com.gtsi.planificacion;

import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import business_services.users.UserCanvasService;
import db.DBConnection;

@SpringBootApplication @ComponentScan({"com.gtsi","com.gtsi.controllers"})
public class PlanificacionApplication {

	public static void main(String[] args) {

		try {
			DBConnection.getInstance("development");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		SpringApplication.run(PlanificacionApplication.class, args);
	}

}