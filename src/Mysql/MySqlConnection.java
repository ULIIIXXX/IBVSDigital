package Mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MySqlConnection {

    private static final String SERVER = "jdbc:mysql://localhost:3306/sensores?allowPublicKeyRetrieval=true&amp&ampuseSSL=false;";
    private static final String USER = "root";
    private static final String PASSWORD = "782833248";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static Connection conexion;

    static {
        try {
            conexion = DriverManager.getConnection(SERVER,USER,PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    PreparedStatement stmt = null;

    public MySqlConnection() throws SQLException {
    }

    public static  Connection MySqlConnection(){
        try{
            Class.forName(DRIVER);
            conexion = DriverManager.getConnection(SERVER,USER,PASSWORD);
            System.out.println("Conexion establecida");
        }catch(ClassNotFoundException | SQLException ex){
            ex.printStackTrace();
        }
        return  conexion;
    }

    public void insertarDatos(String name, byte[] huella){
        try{
            stmt = conexion.prepareStatement("INSERT INTO huellas (nombre,huella) VALUES(?,?)");
            stmt.setString(1,name);
            stmt.setBytes(2,huella);
            stmt.executeUpdate();
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }



}
