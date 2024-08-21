package conexao;
import  java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Conexao {
private static final String url = "jdbc:mysql://localhost:3306/floricultura";
private static final String user = "root";
private static final String password = "databasepedrinho";
private static Connection conexao;
public static Connection getConnection() throws SQLException{
    
    try{
    if(conexao == null){

        conexao = DriverManager.getConnection(url, user, password);
        return conexao;
    }
else{return conexao;}
    }
    catch(Exception e){
        e.printStackTrace();return null;}
}}
