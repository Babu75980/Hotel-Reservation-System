package com.jspiders.hrs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class MainOfHMS {
	private static final String dburl = "jdbc:mysql://localhost:3306/hotel_db";
	private static final String user = "root";
	private static final String password = "Babu@2201";

	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		try {
			Connection con = DriverManager.getConnection(dburl, user, password);
			while (true) {
				System.out.println();
				System.out.println("   WELCOME TO HOTEL RESERVATION SYSTRM	 ");
				System.out.println(" ");
				Scanner sc = new Scanner(System.in);
				System.out.println("1. RESERVER ROOM");
				System.out.println("2. VIEW RESERVATIONS ");
				System.out.println("3. GET ROOM NUMBER");
				System.out.println("4. UPDATE RESERVATIONS");
				System.out.println("5. DELETE RESERVATIONS");
				System.out.println("0. EXIT");
				System.out.println(" ");
				System.out.println("   Choose an Option: ");
				int choice = sc.nextInt();
				switch (choice) {
				case 1:
					reserveRoom(con, sc);
					break;
				case 2:
					viewReservations(con);
					break;
				case 3:
					getRoomNo(con, sc);
					break;
				case 4:
					updateReservations(con, sc);
					break;
				case 5:
					deleteReservations(con, sc);
					break;
				case 0:
					exit();
					sc.close();
					return;
				default:
					System.out.println("invalid choice");
				}
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// method for new reservation
	private static void reserveRoom(Connection con, Scanner sc) {
		try {
			System.out.println("Enter Guest Name: ");
			String gname = sc.next();
			sc.nextLine();
			System.out.println("Enter Guest Contact Number: ");
			String contact = sc.next();
			sc.nextLine();// for next line
			System.out.println("Enter Room Number:");
			int room = sc.nextInt();
			String query = "INSERT INTO RESERVATIONS (GUEST_NAME,GUEST_CONTACT,ROOM_NUMBER)VALUES" + "('" + gname
					+ "','" + contact + "','" + room + "')";

			try (Statement stmt = con.createStatement()) {
				int count = stmt.executeUpdate(query);
				if (count > 0) {
					System.out.println("RESERVATION SUCCESSFUL");
				} else {
					System.out.println("Reservation failed");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	// method for view reservations

	private static void viewReservations(Connection con) {
		String query = "SELECT R_ID,GUEST_NAME,GUEST_CONTACT,ROOM_NUMBER,R_DATE FROM RESERVATIONS";
		try (Statement stmt = con.createStatement()) {
			ResultSet rs = stmt.executeQuery(query);
			System.out.println("       CURRENT RESERVATIONS LIST   ");
			System.out.println("+  R_ID  +  GUEST_NAME  + GUEST_CONTACT +  ROOM_NO   +      R_DATE      ");
			while (rs.next()) {
				int id = rs.getInt("R_ID");
				String gname = rs.getString("GUEST_NAME");
				String contact = rs.getString("GUEST_CONTACT");
				int room = rs.getInt("ROOM_NUMBER");
				String date = rs.getTimestamp("R_DATE").toString();
				// format and display the reservation data in table format
				System.out.printf("| %-6d | %-12s | %-13s | %-10d | %-19s |\n", id, gname, contact, room, date);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// method for get RoomNo
	private static void getRoomNo(Connection con, Scanner sc) {
		try {
			System.out.println("Enter reservation ID: ");
			int id = sc.nextInt();
			System.out.println("Enter Guset Name: ");
			String gname = sc.next();
			String query = "SELECT ROOM_NUMBER FROM RESERVATIONS WHERE R_ID=" + id + " AND GUEST_NAME='" + gname + "'";
			try (Statement stmt = con.createStatement()) {
				ResultSet rs = stmt.executeQuery(query);
				if (rs.next()) {
					int room = rs.getInt("ROOM_NUMBER");
					System.out.println("ROOM NUMBER: " + room);
				} else {
					System.out.println("RESERVATION NOT FOUND FOR GIVEN ID: " + id);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();

		}
	}

	// method for update reservations

	private static void updateReservations(Connection con, Scanner sc) {
		try {
			System.out.println("enter reservation id to update:");
			int id = sc.nextInt();
			sc.nextLine();
			if (!reservationExists(con, id)) {
				System.out.println("Reservation not found for given ID.");
				return;
				// reservations
			}
			System.out.println("Enter the new guest Name:");
			String name = sc.nextLine();
			System.out.println("enter the new contact no:");
			String contact = sc.nextLine();
			System.out.println("Enter the new room numer");
			int room = sc.nextInt();
			String query = "UPDATE RESERVATIONS SET GUEST_NAME= '" + name + "', GUEST_CONTACT= '" + contact
					+ "',ROOM_NUMBER='" + room + "' WHERE R_ID=" + id;
			try (Statement stmt = con.createStatement()) {
				int count = stmt.executeUpdate(query);
				if (count > 0) {
					System.out.println("Reservation success");
				} else {
					System.out.println("reserves fails");
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// method for delete the reservation
	private static void deleteReservations(Connection con, Scanner sc) {
		try {
			System.out.println("enter reservation id to delete");
			int id = sc.nextInt();
			if (!reservationExists(con, id)) {
				System.out.println("reservation not found for given Id to delete.");
				return;
			}
			String query = "DELETE FROM RESERVATIONS WHERE R_ID=" + id;
			try (Statement stmt = con.createStatement()) {
				int count = stmt.executeUpdate(query);
				if (count > 0) {
					System.out.println("reservation delete success");
				} else {
					System.out.println("reservation delete fails");
				}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	// method for checking resevation is exist or not

	private static boolean reservationExists(Connection con, int id) {
		try {
			String query = "SELECT R_ID FROM RESERVATIONS WHERE R_ID=" + id;
			try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
				return rs.next();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// method for exit
	public static void exit() throws InterruptedException {
		System.out.println(" Existing System");
		System.out.print(" ");
		int i = 15;
		while (i != 0) {
			System.out.print("*");
			Thread.sleep(500);
			i--;

		}
		System.out.println();
		System.out.println(" Thank You for using Hotel Reservation System");
	}

}
