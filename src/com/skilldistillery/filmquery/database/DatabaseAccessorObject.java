package com.skilldistillery.filmquery.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skilldistillery.filmquery.entities.Actor;
import com.skilldistillery.filmquery.entities.Film;

public class DatabaseAccessorObject implements DatabaseAccessor {
//only class that connects to database
	private static final String URL = "jdbc:mysql://localhost:3306/sdvid?useSSL=false";
	private String user = "student";
	private String pass = "student";
	private Film film;

	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Film findFilmById(int filmId) throws SQLException {
		Film film = new Film();
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
			String language = findLanguageByFilmId(filmId);
			int rentDur = filmResult.getInt(6);
			double rate = filmResult.getDouble(7);
			String length = filmResult.getString(8);
			double repCost = filmResult.getDouble(9);
			String rating = filmResult.getString(10);
			String features = filmResult.getString(11);
			List<Actor> actors = findActorsByFilmId(filmId);
			film = new Film(id, title, desc, releaseYear, language, rentDur, rate, length, repCost, rating, features,
					actors);
		}
		filmResult.close();
		stmt.close();
		conn.close();
		return film;
	}
	
	public List<Film> findFilmByKeyword(String keyword) throws SQLException {
		List<Film> films = new ArrayList<>();
		Connection conn = DriverManager.getConnection(URL, user, pass);
		String sql = "SELECT * FROM film WHERE film.title LIKE ? OR film.description LIKE ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, "%"+keyword+"%");
		stmt.setString(2, "%"+keyword+"%");
		ResultSet filmResult = stmt.executeQuery();

		while (filmResult.next()) {
			int id = filmResult.getInt(1);
			String title = filmResult.getString(2);
			String desc = filmResult.getString(3);
			String releaseYear = filmResult.getString(4);
			String language = findLanguageByFilmId(id);
			int rentDur = filmResult.getInt(6);
			double rate = filmResult.getDouble(7);
			String length = filmResult.getString(8);
			double repCost = filmResult.getDouble(9);
			String rating = filmResult.getString(10);
			String features = filmResult.getString(11);
			List<Actor> actors = findActorsByFilmId(id);
			film = new Film(id, title, desc, releaseYear, language, rentDur, rate, length, repCost, rating, features,
					actors);
			films.add(film);
		}
		filmResult.close();
		stmt.close();
		conn.close();
		return films;
	}

	public Actor findActorById(int actorId) throws SQLException {
		Actor actor = null;
		Connection conn = DriverManager.getConnection(URL, user, pass);
		// ...
		String sql = "SELECT id, first_name, last_name FROM actor WHERE id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setInt(1, actorId);
		ResultSet actorResult = stmt.executeQuery();
		if (actorResult.next()) {
			actor = new Actor(); // Create the object
			// Here is our mapping of query columns to our object fields:
			actor.setId(actorResult.getInt(1));
			actor.setFirstName(actorResult.getString(2));
			actor.setLastName(actorResult.getString(3));
		}
		return actor;
	}

	public List<Actor> findActorsByFilmId(int filmId) {
		List<Actor> actors = new ArrayList<>();
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT actor.id, actor.first_name, actor.last_name "
					+ " FROM actor JOIN film_actor ON actor.id = film_actor.actor_id JOIN film ON film_actor.film_id = film.id "
					+ " WHERE film.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				int actorId = rs.getInt(1);
				String fName = rs.getString(2);
				String lName = rs.getString(3);

				Actor actor = new Actor(actorId, fName, lName);
				actors.add(actor);
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return actors;
	}
	
	public String findLanguageByFilmId(int filmId) {
		String language = "";
		try {
			Connection conn = DriverManager.getConnection(URL, user, pass);
			String sql = "SELECT language.name "
					+ " FROM language JOIN film ON language.id = film.language_id "
					+ " WHERE film.id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, filmId);
			ResultSet rs = stmt.executeQuery();
			while(rs.next())
			language = rs.getString(1);
			
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return language;
	}
}
