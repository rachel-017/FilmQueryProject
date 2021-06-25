package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
//only class that connects to database
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	private String user = "student";
	private String pass = "student";
	private Film film;
	private List<Film> films = new ArrayList<>();

	public DatabaseAccessorObject() throws ClassNotFoundException{
		Class.forName("com.mysql.jdbc.Driver");
	}

	@Override
	public Film findFilmById(int filmId){
		try {
		Connection conn = DriverManager.getConnection(URL, user, pass);
		String sql = "SELECT * FROM film WHERE film.id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, filmId);
		ResultSet filmResult = stmt.executeQuery();
		
		if (filmResult.next()) {
			int id = filmResult.getInt(1);
			String title = filmResult.getString(2);
			String desc = filmResult.getString(3);
			String releaseYear = filmResult.getString(4);
			int langId = filmResult.getInt(5);
			int rentDur = filmResult.getInt(6);
			double rate = filmResult.getDouble(7);
			String length = filmResult.getString(8);
			double repCost = filmResult.getDouble(9);
			String rating = filmResult.getString(10);
			String features = filmResult.getString(11);
			Film film = new Film( id, title, desc, releaseYear, langId, rentDur, rate, length, repCost, rating,
					features);
		}
		filmResult.close();
		stmt.close();
		conn.close();
		}catch (SQLException e) {
			e.printStackTrace();
		}
		return film;
	}

}
