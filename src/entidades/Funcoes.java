package entidades;

import DAO.Controller;

import java.sql.ResultSet;
import java.util.Scanner;

public class Funcoes {

    public static void clearBuffer(Scanner scanner) {
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }

    public static Vendedor IniciaSistema(Scanner tc) {
        Controller controle = new Controller();
        if (controle.Quantity("", "vendedor", "") == 1) {
            System.out.println("-----------------------------------------------------------------------------\n" +
                    "BEM-VINDO AO SISTEMA DA FLORICULTURA! NENHUM REGISTRO DE VENDEDORES FOI ENCONTRADO." +
                    " PARA UTILIZAR O SISTEMA \nÉ NECESSÁRIO SER UM VENDEDOR. DESEJA CADASTRAR UM" +
                    " NOVO VENDEDOR?");

            if (tc.nextLine().equalsIgnoreCase("sim") || tc.nextLine().equalsIgnoreCase("s")) {
                while (true) {
                    System.out.print("MUITO BEM, INSIRA AS SEGUINTES INFORMAÇÕES:\nNome: ");
                    String nome = tc.nextLine();

                    System.out.print("CPF: ");
                    String CPF = tc.nextLine();

                    System.out.print("Nome de acesso: ");
                    String user = tc.nextLine();

                    System.out.print("Senha de acesso: ");
                    String senha = tc.nextLine();

                    System.out.print("-------------------------------------------------------------------------" +
                            "\n\nVERIFIQUE SE AS INFORMAÇÕES ESTÃO CORRETAS. SE SIM, DIGITE " +
                            "'Sim', SE NÃO, DIGITE 'Não'\nNome: " + nome + "\nCPF: " + CPF + "\n");

                    if (tc.nextLine().equalsIgnoreCase("sim")|| tc.nextLine().equalsIgnoreCase("s")) {
                        String Create = "'" + nome + "', '" + user + "', '" + CPF + "', '" + senha + "'";
                        if (controle.Create("Vendedor", Create, false,
                                "nome, usuario, cpf, senha") != -1) {
                            System.out.println("CADASTRO CONCLUÍDO COM SUCESSO! PARA LOGAR, UTILIZE O USUÁRIO: " +
                                    user + " E A " +
                                    "SENHA INFORMADA.");
                            break;
                        }
                    }
                }

            } else {
                System.exit(0);
            }
        }
        System.out.print("----------------------------------------------------" +
                "\nLOGIN NO SISTEMA\n\n");

        Vendedor vendedor = new Vendedor();

        do {
            System.out.print("Usuário: ");
            String user = tc.nextLine();
            System.out.print("Senha: ");
            String senha = tc.nextLine();

            ResultSet rt = controle.login(user, senha, "vendedor");
            if (rt != null) {
                try {
                    vendedor.setNome(rt.getString("nome"));
                    vendedor.setId(rt.getInt("id_vendedor"));
                    vendedor.setCpf(rt.getString("cpf"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.print("\nLOGIN EFETUADO COM SUCESSO!\n\n");
                break;
            } else {
                System.out.print("SENHA OU USUÁRIO INCORRETO! \nTENTE NOVAMENTE\n\n");
            }
        } while (true);
        controle = null;
        return vendedor;
    }
}
