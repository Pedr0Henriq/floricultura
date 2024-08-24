import DAO.*;
import entidades.*;

import java.util.Scanner;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println(new Controller());

        Scanner tc = new Scanner(System.in);
        Vendedor vendedor = Funcoes.IniciaSistema(tc);

        System.out.print("-----------------------------------------------------------------------------\n" +
                "BEM-VINDO AO MENU DE VENDEDOR DA FLORICULTURA");

        do {

            System.out.println("\nQUAL A OPÇÃO DESEJADA?\n(ESCOLHA UMA DAS OPÇÕES ABAIXO)\n" +
                    "-----------------------------------------------------------------------------\n" +
                    "1 - Cadastrar\n" +
                    "2 - Alterar informação\n" +
                    "3 - Remover\n" +
                    "4 - Consultar\n" +
                    "5 - Adicionar no estoque\n" +
                    "6 - Gerar Relatório\n"+
                    "7 - Sair\n" +
                    "-----------------------------------------------------------------------------");
            int a = Integer.parseInt(tc.nextLine());

            switch (a) {
                case 1 -> {
                    System.out.println("\nO que você deseja cadastrar?\n\n" +
                            "1 - Cliente\n" +
                            "2 - Compra\n" +
                            "3 - Produto\n" +
                            "4 - Vendedor\n" +
                            "5 - Voltar ao menu principal\n" +
                            "-----------------------------------------------------------------------------");
                    a = Integer.parseInt(tc.nextLine());

                    switch (a) {
                        case 1 -> vendedor.cadastraCliente(tc);
                        case 2 -> vendedor.cadastraCompra(tc);
                        case 3 -> vendedor.cadastraProduto(tc);
                        case 4 -> vendedor.cadastraVendedor(tc);
                        case 5 -> {
                            continue;
                        }
                        default -> System.out.println("OPÇÃO INVÁLIDA!");
                    }
                }
                case 2 -> {
                    System.out.println("\nO que você deseja alterar?\n\n" +
                            "1 - Cliente\n" +
                            "2 - Vendedor\n" +
                            "3 - Produto\n" +
                            "4 - Voltar ao menu principal\n" +
                            "-----------------------------------------------------------------------------");
                    a = Integer.parseInt(tc.nextLine());

                    switch (a) {
                        case 1 -> vendedor.alteraCliente(tc);
                        case 2 -> vendedor.alteraVendedor(tc);
                        case 3 -> vendedor.alteraProduto(tc);
                        case 4 -> {
                            continue;
                        }
                        default -> System.out.println("OPÇÃO INVÁLIDA!");
                    }

                }
                case 3 -> {
                    System.out.println("\nO que você deseja remover?\n\n" +
                            "1 - Cliente\n" +
                            "2 - Vendedor\n" +
                            "3 - Produto\n" +
                            "4 - Voltar ao menu principal\n" +
                            "-----------------------------------------------------------------------------");
                    a = Integer.parseInt(tc.nextLine());

                    switch (a) {
                        case 1 -> vendedor.removeCliente(tc);
                        case 2 -> vendedor.removeVendedor(tc);
                        case 3 -> vendedor.removeProduto(tc);
                        case 4 -> {
                            continue;
                        }
                        default -> System.out.println("OPÇÃO INVÁLIDA!");
                    }
                }
                case 4 -> {
                    boolean all = false;
                    System.out.println("\nO que você deseja consultar?\n\n" +
                            "1 - Cliente\n" +
                            "2 - Vendedor\n" +
                            "3 - Produto\n" +
                            "4 - Compra\n" +
                            "5 - Voltar ao menu principal\n" +
                            "-----------------------------------------------------------------------------");
                    a = Integer.parseInt(tc.nextLine());

                    if (a != 5) {
                        System.out.println("Deseja filtrar consulta?");
                        all = !tc.nextLine().equalsIgnoreCase("sim");
                    }

                    switch (a) {
                        case 1 -> {
                            if (all)
                                vendedor.printCliente("nome, cpf, email");
                            else {
                                System.out.print("Digite o ID do cliente: ");
                                String id = tc.nextLine();
                                vendedor.printCliente(id, "nome, cpf, email");
                            }
                        }
                        case 2 -> {
                            if (all)
                                vendedor.printVendedor("nome, cpf");
                            else {
                                System.out.print("Digite o ID do vendedor: ");
                                String id = tc.nextLine();
                                vendedor.printVendedor(id, "nome, cpf");
                            }
                        }
                        case 3 -> {
                            if (all)
                                vendedor.printProduto("*");
                            else {
                                System.out.print("Digite o ID do produto: ");
                                String id = tc.nextLine();
                                vendedor.printProduto(id, "*");
                            }
                        }
                        case 4 -> {
                            if (all)
                                vendedor.printCompra("*");
                            else {
                                System.out.print("Digite o ID da compra: ");
                                String id = tc.nextLine();
                                vendedor.printCompra(id, "*");
                            }
                        }
                        case 5 -> {
                            continue;
                        }
                        default -> System.out.println("OPÇÃO INVÁLIDA!");
                    }
                }
                case 5 -> {
                    vendedor.adicionaproduto_noEstoque(tc);
                }
                case 6 -> {
                    System.out.println("\nVocê deseja gerar relatório sobre o que?\n\n" +
                            "1 - Clientes\n" +
                            "2 - Vendedores\n" +
                            "3 - Produtos\n" +
                            "4 - Compras\n" +
                            "5 - Voltar ao menu principal\n" +
                            "-----------------------------------------------------------------------------");
                    int r = Integer.parseInt(tc.nextLine());
                    switch (r) {
                        case 1:
                            new Controller().geraRelatorioCliente("id_cliente, nome, cpf, email, rua, numero");
                            break;
                        
                        case 2:
                            new Controller().geraRelatorioVendedor("id_vendedor, nome, cpf");
                            break;
                        
                        case 3:
                            new Controller().geraRelatorioProduto("id_produto, nome, preco, quantidade_estoque");
                            break;
                        
                        case 4:
                            new Controller().geraRelatorioCompra("id_compra, id_cliente, id_vendedor, id_carrinho, valor, data, forma_pagamento");
                            break;
                        default:
                            break;
                    }
                }
                case 7 -> {
                    System.out.println("DESLIGANDO...");
                    System.exit(0);
                }
                default -> System.out.println("OPÇÃO INVÁLIDA!");
            }
        } while (true);
    }
}
