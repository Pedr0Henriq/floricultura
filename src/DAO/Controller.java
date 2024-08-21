package DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.StringJoiner;

public class Controller {
    private Connection conexao;
    public Controller(){
        String url = "jdbc:mysql://localhost:3306/floricultura";
        String user = "root";
        String password = "databasepedrinho";
        try{
        conexao = DriverManager.getConnection(url, user, password);}
        catch(Exception e){
        System.out.println("Falha em se conectar com o banco de dados..."+e);
        System.exit(1);}
    }

    public int Create(String table, String campos, boolean retorno, String values){
        try{
            if(retorno){
            String query = "INSERT INTO "+table+"("+ campos +") VALUES("+values+") RETURNING id" +table+";";
            ResultSet rt = conexao.createStatement().executeQuery(query);
            if(rt.next())
                return rt.getInt("id"+table);
            }else{
                String query = "INSERT INTO "+table+"("+ campos +") VALUES("+values+")";
                return conexao.createStatement().executeUpdate(query);
            }
        }catch(Exception e){
            System.out.println("Falha ao inserir dados..."+e);
        }
        return -1;
    }

    public int Quantity(String pesquisa, String table, String condicao){

        try {

            if (pesquisa.isEmpty())
                pesquisa = "*";


            Statement st = conexao.createStatement();
            String consulta = "SELECT COUNT(" + pesquisa + ") FROM " + table  + condicao + ";";

            ResultSet rt = st.executeQuery(consulta);
            return rt.next() ? rt.getInt(1) : -1;

        } catch (Exception e) {
            System.out.println("Falha ao buscar dados..." + e);
        }
        return -2;
    }
    public ResultSet Read(String table, String campos, String condicao){
        ResultSet rt = null;
        try{
            String query = "SELECT "+campos+" FROM "+table+" "+condicao+";";
            rt = conexao.createStatement().executeQuery(query);
        }catch(Exception e){
            System.out.println("Falha ao buscar dados..."+e);
        }
        return rt;
    }

    public boolean Update(String table, String campos,String novo, String condicao){
        try{
            String query = "UPDATE "+table+" SET "+campos+" = "+ novo + " "+condicao+";";
            int u = conexao.createStatement().executeUpdate(query);
            if (u==1) {
                return true;
            }
        }catch(Exception e){
            System.out.println("Falha ao atualizar dados..."+e);
        }
        return false;
    }

    public boolean Delete(String table, String condicao1, String condicao2, boolean deletaTudo){
        try {
            Statement st = conexao.createStatement();
            if (deletaTudo){
                String consulta = "DELETE FROM " + table + ";";

                return !st.execute(consulta);
            } else {

                String consulta = "DELETE FROM " + table + " WHERE " + condicao1 + " = " + condicao2 + ";";

                return !st.execute(consulta);
            }
        } catch (Exception e){
            System.out.println("Falha ao excluir o produto..." + e);
        }
        return false;
    }

    public ResultSet login(String user, String password, String table) {
        try {
            ResultSet rt = Read(table, "*", " WHERE usuario = '" + user + "'");

            if (rt.next()){
                if (password.equalsIgnoreCase(rt.getString("senha")))
                    return rt;
            }

        } catch (Exception e) {
            System.out.println("Falha no login do usuario... " + e);
        }
        return null;
    }

    public ResultSet Select(String campos, String table, String infopesquisa, String pesquisa){
        try {
            return Read(table, campos, " WHERE " + pesquisa +
                    " = " + infopesquisa);
        } catch (Exception e) {
            System.out.println("Falha ao buscar dados... " + e);
        }
        return null;
    }

      public void printa(String table, String colunas){
        try{
            ResultSet rt = Read(table, colunas, "WHERE id_" + table + " >= 0");

            ResultSetMetaData rtMetaData = rt.getMetaData();
            int numeroDeColunas = rtMetaData.getColumnCount();

            while (rt.next()) {
                StringJoiner joiner = new StringJoiner(", ", "[", "]\n");
                for (int coluna = 1; coluna <= numeroDeColunas; coluna++) {
                    String nomeDaColuna = rtMetaData.getColumnName(coluna);
                    joiner.add(nomeDaColuna + " = " + rt.getString(coluna));
                }
                System.out.print(joiner.toString());
            }

        } catch (Exception e){
            System.out.println("Falha ao mostrar resultados... " + e);
        }
    }

    public void printa(String table, String id, String colunas){
        try{
            ResultSet rt = Read(table, colunas, " WHERE id_" + table + " = " + id +
                    " AND id_" + table + " >= 0");

            ResultSetMetaData rtMetaData = rt.getMetaData();
            int numeroDeColunas = rtMetaData.getColumnCount();

            while (rt.next()) {
                StringJoiner joiner = new StringJoiner(", ", "[", "]\n");
                for (int coluna = 1; coluna <= numeroDeColunas; coluna++) {
                    String nomeDaColuna = rtMetaData.getColumnName(coluna);
                    joiner.add(nomeDaColuna + " = " + rt.getObject(coluna));
                }
                System.out.print(joiner.toString());
            }

        } catch (Exception e){
            System.out.println("Falha ao mostrar resultados... " + e);
        }
    }
}
