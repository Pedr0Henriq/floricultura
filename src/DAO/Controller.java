package DAO;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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

    public int Create(String table, String valores_campos, boolean retorno, String nomes_campos){
        try{
            if(retorno){
            String query = "INSERT INTO "+table+"("+ nomes_campos +") VALUES("+valores_campos+");";   
            PreparedStatement ps = conexao.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            
            // Executa a query
            ps.executeUpdate();
            
            // Obtém o ID gerado
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next())
                return rs.getInt(1);
            }else{
                String query = "INSERT INTO "+table+"("+ nomes_campos +") VALUES("+valores_campos+");";
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
    public ResultSet Read(String table, String campos, String condicao) {
        ResultSet rt = null;
        try {
            String query = "SELECT " + campos + " FROM " + table + " " + condicao + ";";
            rt = conexao.createStatement().executeQuery(query);
        } catch (Exception e) {
            System.out.println("Falha ao buscar dados..." + e);
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
            if(table.equals("vendedor")){
                ResultSet rt = Read(table, "*", " WHERE usuario = '" + user + "';");

                if (rt.next()){
                    if (password.equalsIgnoreCase(rt.getString("senha")))
                        return rt;
                }
            }
            else{
            ResultSet rt = Read(table, "*", " WHERE cpf = '" + user + "';");

            if (rt.next()){
                if (password.equalsIgnoreCase(rt.getString("senha")))
                    return rt;
            }}

        } catch (Exception e) {
            System.out.println("Falha no login do usuario... " + e);
        }
        return null;
    }

    public ResultSet Select(String campos, String table, String infopesquisa, String pesquisa) {
        try {
            return Read(table, campos, " WHERE " + pesquisa + " = '" + infopesquisa + "'");
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

            if (!rt.next()) {
                System.out.println("Nenhum registro encontrado...");
                return;
            }
            else{
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

        }} catch (Exception e){
            System.out.println("Falha ao mostrar resultados... " + e);
        }
    }

    public void geraRelatorioCliente(String colunas){
        String query = "SELECT " + colunas + " FROM cliente;";
        int total=0;
        try {
            if(Quantity("", "cliente", "")==0){
                System.out.println("Nenhum registro encontrado...");
                return;
            }
            else{
            ResultSet rt = conexao.createStatement().executeQuery(query);
            FileWriter fw = new FileWriter("relatorioclientes.txt");

            fw.write("Relatório de clientes:\n\n");
            while (rt.next()) {
            int id = rt.getInt("id_cliente");
            String nome = rt.getString("nome");
            String cpf = rt.getString("cpf");
            String email = rt.getString("email");
            String rua = rt.getString("rua");
            int numero = rt.getInt("numero");
            
            fw.write("ID: " + id + "\nNome: " + nome + "\nCPF: " + cpf + "\nEmail: " + email + "\nRua: " + rua + "\nNúmero: " + numero + "\n\n");
            total++;
            }
            fw.write("_________________________________\n");
            fw.write("Total de registros: " + total+"\n");
            fw.close();
        }} catch (SQLException e) {
            System.out.println("Falha ao acessar banco de dados... " + e);
        }catch(IOException e){
            System.out.println("Falha ao escrever no arquivo... " + e);
        }
    }

    public void geraRelatorioVendedor(String colunas) {
        String query = "SELECT " + colunas + " FROM vendedor;";
        int total=0;
        try {
            if(Quantity("", "vendedor", "")==0){
                System.out.println("Nenhum registro encontrado...");
                return;
            }
            else{
            ResultSet rt = conexao.createStatement().executeQuery(query);
            FileWriter fw = new FileWriter("relatoriovendedores.txt");

            fw.write("Relatório de vendedores:\n\n");
            while (rt.next()) {
            int id = rt.getInt("id_vendedor");
            String nome = rt.getString("nome");
            String cpf = rt.getString("cpf");
                       
            fw.write("ID: " + id + "\nNome: " + nome + "\nCPF: " + cpf + "\n\n");
            total++;
            }
            fw.write("_________________________________\n");
            fw.write("Total de registros: " + total+"\n");
            fw.close();
        }} catch (SQLException e) {
            System.out.println("Falha ao acessar banco de dados... " + e);
        }catch(IOException e){
            System.out.println("Falha ao escrever no arquivo... " + e);
        }
    }

    public void geraRelatorioProduto(String colunas) {
        String query = "SELECT " + colunas + " FROM produto;";
        int total=0;
        try {
            if(Quantity("", "produto", "")==0){
                System.out.println("Nenhum registro encontrado...");
                return;
            }
            else{
            ResultSet rt = conexao.createStatement().executeQuery(query);
            FileWriter fw = new FileWriter("relatorioprodutos.txt");

            fw.write("Relatório de produtos:\n\n");
            while (rt.next()) {
            int id = rt.getInt("id_produto");
            String nome = rt.getString("nome");
            int quantidade = rt.getInt("quantidade_estoque");
            double preco = rt.getDouble("preco");
                       
            fw.write("ID: " + id + "\nNome: " + nome + "\nPreço: " + preco + "\nQuantidade: "+quantidade+" \n\n");
            total++;
            }
            fw.write("_________________________________\n");
            fw.write("Total de registros: " + total+"\n");
            fw.close();
        }} catch (SQLException e) {
            System.out.println("Falha ao acessar banco de dados... " + e);
        }catch(IOException e){
            System.out.println("Falha ao escrever no arquivo... " + e);
        }
    }

    public void geraRelatorioCompra(String colunas) {
        String query = "SELECT " + colunas + " FROM compra;";
        int total=0;
        try {
            if(Quantity("", "compra", "")==0){
                System.out.println("Nenhum registro encontrado...");
                return;
            }
            else{
            ResultSet rt = conexao.createStatement().executeQuery(query);
            FileWriter fw = new FileWriter("relatoriocompras.txt");

            fw.write("Relatório de compras:\n\n");
            while (rt.next()) {
            int id_Compra = rt.getInt("id_compra");
            int id_Cliente = rt.getInt("id_cliente");
            int id_Vendedor = rt.getInt("id_vendedor");
            int id_Carrinho = rt.getInt("id_carrinho");
            String forma = rt.getString("forma_pagamento");
            double valor = rt.getDouble("valor");
            String data = rt.getString("data");
            
            fw.write("ID: " + id_Compra + "\nID Cliente: " + id_Cliente + "\nID Vendedor: " + id_Vendedor + "\nID Carrinho: " + id_Carrinho + "\nForma de pagamento: " + forma + "\nValor: " + valor + "\nData: " + data + "\n\n");
            
            total++;
            }
            fw.write("_________________________________\n");
            fw.write("Total de registros: " + total+"\n");
            fw.close();
        }} catch (SQLException e) {
            System.out.println("Falha ao acessar banco de dados... " + e);
        }catch(IOException e){
            System.out.println("Falha ao escrever no arquivo... " + e);
        }}
}
