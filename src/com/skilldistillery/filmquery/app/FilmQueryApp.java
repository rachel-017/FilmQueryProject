package com.skilldistillery.filmquery.app;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import com.skilldistillery.filmquery.database.DatabaseAccessor;
import com.skilldistillery.filmquery.database.DatabaseAccessorObject;
import com.skilldistillery.filmquery.entities.Film;

public class FilmQueryApp {

	DatabaseAccessor db = new DatabaseAccessorObject();

	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		FilmQueryApp app = new FilmQueryApp();
//		app.test();
		app.launch();
	}

//	private void test() throws SQLException {
//		List<Film> films = db.findFilmByKeyword("%man");
//    if(film == null) {
//    	System.out.println("There are no films with that ID");
//    }
//    else {
//   System.out.println(film);
//    }
//		for (Film film : films) {
//			System.out.println(film);
//		}
//	}

	private void launch() throws SQLException {
		Scanner input = new Scanner(System.in);

		startUserInterface(input);

		input.close();
	}

	private void startUserInterface(Scanner input) throws SQLException {
		int selection = 0;
		while (selection != 3) {

			System.out.println("\t~Menu~\n");
			System.out.println("1 Search for a film with it's ID");
			System.out.println("2 Search for films with a keyword");
			System.out.println("3 Exit\n");
			try {
				selection = input.nextInt();

				if (selection == 1) {
					System.out.print("Enter the film ID: ");
					int id = input.nextInt();
					Film film = db.findFilmById(id);
					if (film == null) {
						System.out.println("There are no films with that ID");
					} else {
						System.out.println(film);
					}
				}

				if (selection == 2) {
					System.out.print("Enter the keyword: ");
					String keyword = input.next();
					List<Film> films = db.findFilmByKeyword(keyword);
					if (films.isEmpty()) {
						System.out.println("There are no films featuring that keyword");
					} else {
						for (Film film : films) {
							System.out.println(film);
						}
					}
				}

				if (selection == 3) {
					System.out.println("Bye!");
				}
			} catch (InputMismatchException e) {
				System.out.println("Input must be 1, 2, or 3");
			}
		}
	}

}
