package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import data.ConfigData;

public class DataBaseUtils {
	Connection con = null;	
	int tableNumber = -1;
	
	/**
	 * Devuelve el estado de la conexión.
	 * 
	 * @return
	 * @throws SQLException
	 */
	public boolean getDataBaseStatus() throws SQLException {
		if (con == null) {
			return false;
		} else if (con.isClosed()) {
			return false;
		} else {
			return true;
		}
	}
	
	/**
	 * Este método abre la conexión a la base de datos MySQL.
	 * 
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void openDataBase() throws ClassNotFoundException, SQLException {
		String generalURL = null;
		Class.forName("com.mysql.jdbc.Driver");
		System.out.print("Connecting to Runnstein MySQL database...");
        if (ConfigData.dbName == null) generalURL = "jdbc:mysql://"+ConfigData.dbURL+":"+ConfigData.dbPort;
        else generalURL = "jdbc:mysql://"+ConfigData.dbURL+":"+ConfigData.dbPort+"/"+ConfigData.dbName;
        con = DriverManager.getConnection(generalURL, ConfigData.dbUser, ConfigData.dbPassword);
        System.out.println(" Connected!");
	}
	
	/**
	 * Este método cierra la conexión a la base de datos.
	 * 
	 * @throws SQLException
	 */
	public void closeDataBase() throws SQLException {
		if (con != null) {
			System.out.print("Disconnecting to Runnstein MySQL database...");
			con.close();
			System.out.println(" Disconnected!");
		}
	}
	
	public void useDataBase(String dbName) throws SQLException {
		if (con != null) {
			Statement stmt = con.createStatement();
			stmt.execute("USE "+dbName);
		}
	}
	
	public int getNumberColumns(String tbName) throws SQLException {
		if (con != null) {
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM "+tbName);
			ResultSet rs = prepStmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			return rsmd.getColumnCount();
		} else return 0;
	}
	
	public ArrayList<String> getColumnsNames(String tbName) throws SQLException {
		if (con != null) {
			ArrayList<String> columnsName = new ArrayList<>();
			PreparedStatement prepStmt = con.prepareStatement("SELECT * FROM "+tbName);
			ResultSet rs = prepStmt.executeQuery();
			ResultSetMetaData rsmd = rs.getMetaData();
			for (int i = 1; i < getNumberColumns(tbName); i++) {
				columnsName.add(rsmd.getColumnName(i));
			}
			return columnsName;
		} else return null;
	}
	
	public int getNumberRows(ResultSet rs) throws SQLException {
		if (con != null) {
			rs.last();
			return rs.getRow();
		} else return -1;
	}
	
	public ArrayList<String> getTableNamesArrayList() throws SQLException {
		if (con != null) {
			ArrayList<String> tables = new ArrayList<>();
			java.sql.DatabaseMetaData dbmd = con.getMetaData();
			ResultSet rs = dbmd.getTables(null, null, "%", null);
			while (rs.next()) {
				tables.add(rs.getString(3));
			}
			tableNumber = tables.size();
			return tables;
		} else return null;
	}
	
	public int getTableNumber() throws SQLException {
		if (tableNumber == -1) {
			@SuppressWarnings("unused")
			ArrayList<String> tablasTemp = getTableNamesArrayList();
		}
		return tableNumber;
	}
	
	public String [] getTableNamesArray() throws ClassNotFoundException, SQLException {
		openDataBase();
		ArrayList<String> listaTablas = getTableNamesArrayList();
		String tableNames [] = listaTablas.toArray(new String [getTableNumber()]);
		closeDataBase();
		return tableNames;
	}
	
	public Connection getDataBaseConnection() {
		return con;
	}
	
	public ResultSet exeQuery(String query) throws SQLException {
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		return rs;
	}
	
	public void exeStmt(String query) throws SQLException {
		Statement stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}
	
	public String stringSelect(String [] columnNames, String [] columnAlias, String table) {
		String query = "SELECT ";
		if ((columnAlias == null) && (columnNames != null)) {
			for (int i = 0; i < columnNames.length; i++) {
				if (i == (columnNames.length - 1)) {
					query += columnNames[i]+" ";
				} else query += columnNames[i]+", ";
			}
		} else if (columnNames.length == columnAlias.length) {
			for (int i = 0; i < columnNames.length; i++) {
				if (i == (columnNames.length - 1)) {
					query += columnNames[i]+" AS '"+columnAlias[i]+"' ";
				} else query += columnNames[i]+" AS '"+columnAlias[i]+"', ";
			}
		} else {
			query += "* ";
		}
		query += "FROM "+table;
		return query;
	}
	
	public String stringInsert(String [] columnNames, String [] columnValues, String table) {
		String query = "INSERT INTO "+table+"(";
		if (columnNames != null) {
			for (int i = 0; i < columnNames.length; i++) {
				if (i == (columnNames.length - 1)) {
					query += columnNames[i]+") ";
				} else query += columnNames[i]+", ";
			}
		}
		query += "VALUES (";
		if (columnNames.length == columnValues.length) {
			for (int i = 0; i < columnValues.length; i++) {
				if ((columnValues[i].matches("[0-9]+")) || ((columnValues[i].equals("true")) || (columnValues[i].equals("false")))) {
					query += columnValues[i];
				} else if (columnValues[i].matches("^\\d{4}-\\d{2}-\\d{2}.*$")) {
					int ano = Integer.parseInt(columnValues[i].substring(0, 4));
					int mes = Integer.parseInt(columnValues[i].substring(5, 7));
					int dia = Integer.parseInt(columnValues[i].substring(8, 10));
					int horas = Integer.parseInt(columnValues[i].substring(11, 13));
					int minutos = Integer.parseInt(columnValues[i].substring(14, 16));
					int segundos = Integer.parseInt(columnValues[i].substring(17, 19));
					String nuevaFecha = ano+"-"+mes+"-"+dia+"T"+horas+":"+minutos+":"+segundos+"Z";
					query += "STR_TO_DATE('"+nuevaFecha+"','%Y-%m-%dT%H:%i:%sZ')";
				} else {
					columnValues[i].replaceAll("'", "''");
					query += "'"+columnValues[i]+"'";
				}
				if (i == (columnNames.length - 1)) {
					query += ") ";
				} else query += ", ";
			}
		}
		return query;
	}
	
	public String stringDelete(String wColumnName, String wColumnValue, String table) {
		String query = "DELETE FROM "+table+" WHERE ";
		if ((wColumnName != null) || (wColumnValue != null)) {
			query += wColumnName+" ";
			if (wColumnValue.contains("%")) {
				query += "LIKE ";
			} else query += "= ";
			if ((wColumnValue.matches("[0-9]+")) || ((wColumnValue.equals("true")) || (wColumnValue.equals("false")))) {
				query += wColumnValue;
			} else if (wColumnValue.matches("^\\d{4}-\\d{2}-\\d{2}.*$")) {
				int ano = Integer.parseInt(wColumnValue.substring(0, 4));
				int mes = Integer.parseInt(wColumnValue.substring(5, 7));
				int dia = Integer.parseInt(wColumnValue.substring(8, 10));
				int horas = Integer.parseInt(wColumnValue.substring(11, 13));
				int minutos = Integer.parseInt(wColumnValue.substring(14, 16));
				int segundos = Integer.parseInt(wColumnValue.substring(17, 19));
				String nuevaFecha = ano+"-"+mes+"-"+dia+"T"+horas+":"+minutos+":"+segundos+"Z";
				query += "STR_TO_DATE('"+nuevaFecha+"','%Y-%m-%dT%H:%i:%sZ')";
			} else {
				wColumnValue.replaceAll("'", "''");
				query += "'"+wColumnValue+"'";
			}
			return query;
		} else return null;
	}
	
	public String stringUpdate(String columnName, String columnValue, String wColumnName, String wColumnValue, String table) {
		String query = "UPDATE "+table+" SET ";
		if ((columnName != null) || (columnValue != null)) {
			query += columnName+" = ";
			if ((columnValue.matches("[0-9]+")) || ((columnValue.equals("true")) || (columnValue.equals("false")))) {
				query += columnValue;
			} else if (columnValue.matches("^\\d{4}-\\d{2}-\\d{2}.*$")) {
				int ano = Integer.parseInt(columnValue.substring(0, 4));
				int mes = Integer.parseInt(columnValue.substring(5, 7));
				int dia = Integer.parseInt(columnValue.substring(8, 10));
				int horas = Integer.parseInt(columnValue.substring(11, 13));
				int minutos = Integer.parseInt(columnValue.substring(14, 16));
				int segundos = Integer.parseInt(columnValue.substring(17, 19));
				String nuevaFecha = ano+"-"+mes+"-"+dia+"T"+horas+":"+minutos+":"+segundos+"Z";
				query += "STR_TO_DATE('"+nuevaFecha+"','%Y-%m-%dT%H:%i:%sZ')";
			} else {
				columnValue.replaceAll("'", "''");
				query += "'"+columnValue+"'";
			}
		} else return null;
		query += " WHERE ";
		if ((wColumnName != null) && (wColumnValue != null)) {
			query += wColumnName+" ";
			if (wColumnValue.contains("%")) {
				query += "LIKE ";
			} else query += "= ";
			if ((wColumnValue.matches("[0-9]+")) || ((wColumnValue.equals("true")) || (wColumnValue.equals("false")))) {
				query += wColumnValue;
			} else if (wColumnValue.matches("^\\d{4}-\\d{2}-\\d{2}.*$")) {
				int ano = Integer.parseInt(wColumnValue.substring(0, 4));
				int mes = Integer.parseInt(wColumnValue.substring(5, 7));
				int dia = Integer.parseInt(wColumnValue.substring(8, 10));
				int horas = Integer.parseInt(wColumnValue.substring(11, 13));
				int minutos = Integer.parseInt(wColumnValue.substring(14, 16));
				int segundos = Integer.parseInt(wColumnValue.substring(17, 19));
				String nuevaFecha = ano+"-"+mes+"-"+dia+"T"+horas+":"+minutos+":"+segundos+"Z";
				query += "STR_TO_DATE('"+nuevaFecha+"','%Y-%m-%dT%H:%i:%sZ')";
			} else {
				wColumnValue.replaceAll("'", "''");
				query += "'"+wColumnValue+"'";
			}
			return query;
		} else return null;
	}
}
