package entidades;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Date;
import java.util.LinkedList;

import DAO.Controller;

public class Vendedor {
    private int id;
    private String nome;
    private String usuario;
    private String senha;
    private String cpf;
    private Controller con;

    public Vendedor() {
        con = new Controller();
    }

    public boolean cadastraCliente(Scanner tc){
        String cpf_cliente;
        try{
        while(true){
        System.out.println("Digite o nome: ");
        String nome = tc.nextLine();
        System.out.println("Digite a rua: ");
        String rua = tc.nextLine();
        System.out.println("Digite o número: ");
        int numero = Integer.parseInt(tc.nextLine());
        System.out.println("Digite o email do cliente: ");
        String email = tc.nextLine();
        System.out.println("Agora seguiremos para as informações de login!");
        while (true) {
        System.out.println("Digite o CPF: ");
        cpf_cliente = tc.nextLine();
        if(con.Quantity("", "cliente", " WHERE cpf ="+"'"+cpf_cliente+"'")>0)
            System.out.println("CPF já cadastrado, tente novamente!");
        else
            break;
        }
        System.out.println("Digite a senha: ");
        String senha = tc.nextLine();
        System.out.println("VERIFIQUE SE AS INFORMAÇÕES ESTÃO CORRETAS!");
        System.out.println("""
                \nNome:"""+ nome+"""
                \nRua:"""+ rua+"""                
                \nNúmero:"""+ numero+"""               
                \nEmail:"""+ email
                );
        if(tc.nextLine().equalsIgnoreCase("sim")||tc.nextLine().equalsIgnoreCase("s")){
            String insere = "'"+ nome+"', '"+ rua+"', "+ numero+", '"+ email+"', '"+cpf_cliente+"', '"+ senha+"'";
            if(con.Create("cliente", insere, false, "nome, rua, numero, email, cpf, senha")!=-1){
                System.out.println("Cliente cadastrado com sucesso!");
                return true;}
            else{
                System.out.println("Falha ao cadastrar cliente!");
                return false;}
        }else{
            System.out.println("DESEJA INSERIR NOVAMENTE?");
            if(!tc.nextLine().equalsIgnoreCase("sim")||!tc.nextLine().equalsIgnoreCase("s"))
                break;
        }
     }} catch (Exception e){
        System.out.println("Falha ao cadastrar cliente...");
        e.printStackTrace();}
      return false;
    }

    public boolean cadastraCompra(Scanner tc){
        Compra c = new Compra();
        while (true) {
            System.out.println("ID do Produto: ");
            int id = Integer.parseInt(tc.nextLine());

            if (con.Quantity("", "produto", " WHERE id_produto = " + id + "") > 0) {
                ResultSet rt = null;
                try {
                    rt = con.Select("nome,preco,quantidade_estoque", "produto", Integer.toString(id),"id_produto");
                    if (rt != null && rt.next()) { // Verifica se rt não é null e se há resultados
                        int quantidade_estoque = rt.getInt("quantidade_estoque");
                        if (quantidade_estoque == 0) {
                            System.out.println("Produto sem estoque!");
                            continue;
                        }
                        
                        String tratapreco = rt.getString("preco").substring(3).replace(",", ".");
                        Produto p1 = new Produto(rt.getString("nome"), rt.getDouble("preco"), rt.getInt("quantidade_estoque"));
                        
                        while (true) {
                            System.out.println("Quantidade: ");
                            int quantidade = Integer.parseInt(tc.nextLine());
                            if (quantidade > quantidade_estoque) {
                                System.out.println("Quantidade indisponível! Deseja tentar novamente?");
                                if (!tc.nextLine().equalsIgnoreCase("sim") && !tc.nextLine().equalsIgnoreCase("s")) {
                                    break;
                                }
                            } else {
                                c.adicionarProduto(p1, quantidade);
                                produtoAdquirido(id, quantidade);
                                break;
                            }
                        }
                    } else {
                        System.out.println("Produto não encontrado!");
                    }
                } catch(Exception e){
                    System.out.println("Erro ao adicionar produto!");
                    e.printStackTrace();
                } finally {
                    if (rt != null) {
                        try {
                            rt.close();
                        } catch (Exception e) {
                            System.out.println("Erro ao fechar o ResultSet: " + e.getMessage());
                        }
                    }
                }
        } else{

            System.out.println("Produto não encontrado! Deseja cadastrar?");
                if (tc.nextLine().equalsIgnoreCase("sim")||tc.nextLine().equalsIgnoreCase("s")){
                    cadastraProduto(tc);
                    continue;
                }}
                c.getCompra();
                System.out.println("\nO produto adicionado é o correto? (sim/não)");
                
                String respostaProduto = tc.nextLine();
                if (respostaProduto.equalsIgnoreCase("não") || respostaProduto.equalsIgnoreCase("n")|| respostaProduto.equalsIgnoreCase("nao")) {
                    c.removerProduto();
                }

                System.out.println("\nJá adicionou todos os produtos da compra? (sim/não)");
                String respostaFinalizacao = tc.nextLine();
                if (respostaFinalizacao.equalsIgnoreCase("sim") || respostaFinalizacao.equalsIgnoreCase("s")) {
                    break;
                }

        }

        double total = 0;
        for (int i = 0; i < c.getSize(); i++) {
            total += c.getProduto(i).getPreco() * c.getQuantidade(i);}
        System.out.println("\nPreço total: R$" + total + "\nQual a forma de pagamento? ");
        String formaPagamento = tc.nextLine();

        System.out.print("O cliente possui cadastro na loja? (sim/não) ");
        String respostaCadastro = tc.nextLine();
        if (!respostaCadastro.equalsIgnoreCase("sim") || !respostaCadastro.equalsIgnoreCase("s")) {
            System.out.println("PARA REALIZAÇÃO DA COMPRA É NECESSÁRIO ESTAR CADASTRADO!\n" +
            "REALIZE O CADASTRO DO CLIENTE");

            cadastraCliente(tc);}
        
        String usuario,senha;
        ResultSet rs;
        while (true) {
            System.out.println("Usuario: ");
            usuario = tc.nextLine();
            System.out.println("Senha: ");
            senha = tc.nextLine();
            rs = con.login(usuario, senha, "cliente");
            if (rs != null) 
                break;
            else    
                System.out.println("Usuario ou senha incorretos! Tente novamente!");
        }

        try{
            String id_c = rs.getString("id_cliente");
            rs.close();

            Date date= new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            int id_carrinho = con.Create("carrinho", id_c,true, "id_cliente");
            if (id_carrinho == -1) {
                System.out.println("Falha ao criar carrinho!");
                return false;
                
            }

            for(int i = 0; i< c.getSize(); i++){
                con.Create("carrinho_produto", id_carrinho + ", " + Integer.toString(c.getProduto(i).getId()) + ", "+c.getQuantidade(i),false,"id_carrinho, id_produto, quantidade");
            }
            int id_compra = con.Create("compra", "'"+formaPagamento+ "', date('" + sdf.format(date) + "'), " + total + ", " + this.id + ", " + id_carrinho +
            ", " + id_c, true, "forma_pagamento, data, valor," +
            " id_vendedor, id_carrinho, id_cliente");

            if(id_compra !=-1){
                System.out.println("Compra realizada com sucesso!");
                c = null;
                return true;
            } 
        } catch (Exception e){
            System.out.println("Falha ao realizar compra!");
            e.printStackTrace();
    }
    c = null;
    return false;
}
    


    public void cadastraProduto(Scanner tc){
        System.out.println("Digite o nome do produto: ");
        String nome = tc.nextLine();
        System.out.println("Quantidade que será adicionada: ");
        int quantidade = Integer.parseInt(tc.nextLine());
        System.out.println("Preço do produto: ");
        double preco = Double.parseDouble(tc.nextLine());

        String informacoes = "'"+nome+"', "+Double.toString(preco)+", "+Integer.toString(quantidade);
        int id_Produto = con.Create("produto", informacoes, true, "nome,preco,quantidade_estoque");
        if(id_Produto != -1){
            System.out.println("Produto cadastrado com sucesso!Seu id é: "+id_Produto);
        } else{
            System.out.println("Falha ao cadastrar produto!");
        }
    }

    public void cadastraVendedor(Scanner tc){
        while(true){
            System.out.println("Digite o nome: ");
            String nome = tc.nextLine();
            System.out.println("Digite o CPF: ");
            String cpf = tc.nextLine();
            System.out.println("Digite o usuário: ");
            String usuario = tc.nextLine();
            System.out.println("Digite a senha: ");
            String senha = tc.nextLine();
            
            System.out.println("Verifique se as informações estão corretas!");
            System.out.println("""
                    Nome:"""+ nome+"""
                    CPF:"""+ cpf               
                    );

            if(tc.nextLine().equalsIgnoreCase("sim")||tc.nextLine().equalsIgnoreCase("s")){
                String insere = "'"+ nome+"', '"+ cpf+"', '"+ usuario+"', '"+ senha+"'";
                if(con.Create("vendedor", insere, false, "nome, cpf, usuario, senha")!=-1){
                    System.out.println("Vendedor cadastrado com sucesso!\nUtilize o usuario"+usuario+" e a senha "+senha+" para login!");
                    break;}
                
        }
    }
}
    public void removeProduto(Scanner tc){
        while(true){
            System.out.println("Digite o ID do produto que deseja remover: ");
            String id_produto = tc.nextLine();

            if(con.Quantity(id_produto, "produto", "")>0){
                try{
                    ResultSet rs = con.Select("nome,preco,quantidade_estoque", "produto", id_produto, "id_produto");
                    if(rs.next()){
                    System.out.println("O produto a ser removido é: ");
                    System.out.println("Nome: "+rs.getString("nome")+"\nPreço: R$"+rs.getString("preco")+"\nQuantidade: "+rs.getString("quantidade_estoque"));
                    if(tc.nextLine().equalsIgnoreCase("sim")||tc.nextLine().equalsIgnoreCase("s")){
                        if(con.Delete("produto", id_produto, "id_produto",false))
                            System.out.println("Produto removido com sucesso!");
                            
                        else
                            System.out.println("Falha ao remover produto!");
                    }}
                    else{
                        System.out.println("Produto não encontrado.");
                    } 
                }
                catch(Exception e){
                    System.out.println("Falha ao remover produto!");
                    e.printStackTrace();
                }
                System.out.println("Deseja remover outro produto?");
                if(tc.nextLine().equalsIgnoreCase("não")||tc.nextLine().equalsIgnoreCase("n")||tc.nextLine().equalsIgnoreCase("nao"))
                    break;
            }
            else{System.out.println("Não há produtos no estoque!");break;}

        }
    }

    public void removeVendedor(Scanner tc) {
        while (true) {
            System.out.println("Digite o ID do vendedor que deseja remover: ");
            String id_vendedor = tc.nextLine();
    
            if (con.Quantity(id_vendedor, "vendedor", "") > 0) {
                try {
                    ResultSet rs = con.Select("nome, cpf", "vendedor", id_vendedor, "id_vendedor");
                    if (rs.next()) {
                        System.out.println("O vendedor a ser removido é: ");
                        System.out.println("Nome: " + rs.getString("nome") + "\nCPF: " + rs.getString("cpf"));
                        
                        System.out.println("Deseja remover este vendedor? (sim/s)");
                        String resposta = tc.nextLine();
                        if (resposta.equalsIgnoreCase("sim") || resposta.equalsIgnoreCase("s")) {
                            if (con.Delete("vendedor", "id_vendedor", id_vendedor, false)) {
                                System.out.println("Vendedor removido com sucesso!");
                            } else {
                                System.out.println("Falha ao remover vendedor!");
                            }
                        }
                    } else {
                        System.out.println("Vendedor não encontrado.");
                    }
                } catch (Exception e) {
                    System.out.println("Falha ao remover vendedor...");
                    e.printStackTrace();
                }
    
                System.out.println("Deseja remover outro vendedor? (não/n)");
                String continuar = tc.nextLine();
                if (continuar.equalsIgnoreCase("não") || continuar.equalsIgnoreCase("n")|| continuar.equalsIgnoreCase("nao")) {
                    break;
                }
            } else {
                System.out.println("ID de vendedor inválido ou não encontrado.");
            }
        }
    }

    public void removeCliente(Scanner tc){
        System.out.println("PARA REMOVER O CADASTRO DO CLIENTE É NECESSÁRIO REALIZAR O LOGIN");
        String user;
        String senha;
        ResultSet rs;
        while (true) {
            System.out.print("Usuário: ");
            user = tc.nextLine();
            System.out.print("Senha: ");
            senha = tc.nextLine();
            rs = con.login(user, senha, "cliente");
            if (rs != null)
                break;
            else
                System.out.print("Inforamações incorretas!");
        }

        System.out.println("Deseja remover seu cadastro na loja?");
        if (tc.nextLine().equalsIgnoreCase("sim")){
            try {
                if (con.Delete("cliente", "id_cliente", rs.getString("id_cliente"),false))
                    System.out.println("Cliente removido");
                else
                    System.out.println("Erro, tente novamente");
            }catch (Exception e){
                System.out.println("Falha ao remover cliente..." + e);
            }
        }
    }


    public void alteraProduto(Scanner tc){
        
        try{
        while (con.Quantity("", "produto", "")>0) {
        System.out.println("O que você deseja alterar?");
        System.out.println("1 - Nome\n"+"2 - Preço\n"+"3 - Quantidade\n"+"4 - Sair");
        int op = Integer.parseInt(tc.nextLine());
        switch (op) {
            case 1:
                System.out.println("Digite o ID do produto que deseja alterar: ");
                String id_produto = tc.nextLine();

                System.out.println("Digite o novo nome: ");
                String novo_nome = tc.nextLine();

                if(con.Update("produto", "nome", "'" +novo_nome+"'", "WHERE id_produto = "+ id_produto)){
                    System.out.println("Nome alterado com sucesso!");
                } else{
                    System.out.println("Não existe esse registro!");
                }
                break;
            case 2:
                System.out.println("Digite o ID do produto que deseja alterar: ");
                id_produto = tc.nextLine();

                System.out.println("Digite o novo preço: ");
                String novo_preco = tc.nextLine();

                if(con.Update("produto", "preco", "'"+novo_preco+"'", "WHERE id_produto = "+ id_produto)){
                    System.out.println("Preço alterado com sucesso!");
                } else{
                    System.out.println("Não existe esse registro!");
                }
                break;
            case 3:
                System.out.println("Digite o ID do produto que deseja alterar: ");
                id_produto = tc.nextLine();

                System.out.println("Digite a nova quantidade: ");
                String nova_quantidade = tc.nextLine();

                if(con.Update("produto", "quantidade_estoque", "'"+nova_quantidade+"'", "WHERE id_produto = "+ id_produto)){
                    System.out.println("Quantidade alterada com sucesso!");
                } else{
                    System.out.println("Não existe esse registro!");
                }
                break;
            case 4:
                return;
            default:
                System.out.println("Opção inválida!");
                break;
        }}
    if (con.Quantity("", "produto", "")==0){
        System.out.println("Não há produtos no estoque!");
        
    }
    }catch(Exception e){
        System.out.println("Falha ao alterar produto!");
        e.printStackTrace();
    }
}
    
    public void alteraVendedor(Scanner tc){
        try{
        while (true) {
        System.out.println("O que você deseja alterar?");
        System.out.println("1 - Nome\n"+"2 - CPF\n"+"3 - Sair");
        int op = Integer.parseInt(tc.nextLine());
        switch (op) {
            case 1:
                System.out.println("Digite o ID do vendedor que deseja alterar: ");
                String id_vendedor = tc.nextLine();

                System.out.println("Digite o novo nome: ");
                String novo_nome = tc.nextLine();

                if(con.Update("vendedor", "nome", "'"+novo_nome+"'", "WHERE id_vendedor = "+ id_vendedor)){
                    System.out.println("Nome alterado com sucesso!");
                } else{
                    System.out.println("Falha ao alterar nome!");
                }
                break;
            case 2:
                System.out.println("Digite o ID do vendedor que deseja alterar: ");
                id_vendedor = tc.nextLine();

                System.out.println("Digite o novo CPF: ");
                String novo_cpf = tc.nextLine();

                if(con.Update("vendedor", "cpf", "'"+novo_cpf+"'", "WHERE id_vendedor = "+ id_vendedor)){
                    System.out.println("CPF alterado com sucesso!");
                } else{
                    System.out.println("Falha ao alterar CPF!");
                }
                break;
            case 3:
                return;
            default:
                System.out.println("Opção inválida!");
                break;}}}
    catch(Exception e){
        System.out.println("Falha ao alterar vendedor!");
        e.printStackTrace();
    }

}
    public void alteraCliente(Scanner tc){
        try {
            System.out.println("PARA ALTERAR O CADASTRO DO CLIENTE É NECESSÁRIO REALIZAR O LOGIN");
            String user;
            String senha;
            ResultSet rs;
            while (true) {
                System.out.print("Usuário: ");
                user = tc.nextLine();
                System.out.print("Senha: ");
                senha = tc.nextLine();
                rs = con.login(user, senha, "cliente");
                if (!rs.equals(null))
                    break;
                else
                    System.out.print("Informações incorretas!");
            }
            System.out.println("Login realizado com sucesso!");
            while (true) {
                System.out.println("O que você deseja alterar?\n1 - Nome \n2 - CPF \n3 - Rua \n4 - Número " +
                        "\n5 - Email \n6 - Sair");
                int escolha = Integer.parseInt(tc.nextLine());
                switch (escolha) {
                    case 1 -> {
                        System.out.print("Insira o novo nome: ");
                        String novo = tc.nextLine();
                        if (con.Update("cliente", "nome", "'" + novo + "'",
                                "WHERE id_cliente = " + rs.getString("id_cliente"))) {
                            System.out.println("Nome alterado com sucesso!");
                        } else
                            System.out.println("Falha ao alterar nome!");
                    }

                    case 2 -> {
                        System.out.print("Insira o novo CPF: ");
                        int novo = Integer.parseInt(tc.nextLine());
                        if (con.Update("cliente", "cpf", Integer.toString(novo),
                                "WHERE id_cliente = " + rs.getString("id_cliente"))) {
                            System.out.println("Cpf alterado com sucesso!");
                        } else
                            System.out.println("Falha ao alterar cpf!");
                    }

                    case 3 -> {
                        System.out.print("Insira a nova rua: ");
                        String novo = tc.nextLine();
                        if (con.Update("cliente", "rua", "'" + novo + "'",
                                "WHERE id_cliente = " + rs.getString("id_cliente"))) {
                            System.out.println("Rua alterada com sucesso!");
                        } else
                            System.out.println("Falha ao alterar rua!");
                    }

                    case 4 -> {
                        System.out.print("Insira o novo número de residência: ");
                        int novo = Integer.parseInt(tc.nextLine());
                        if (con.Update("cliente", "numero", Integer.toString(novo),
                                "WHERE id_cliente = " + rs.getString("id_cliente"))) {
                            System.out.println("Número alterado com sucesso!");
                        } else
                            System.out.println("Falha ao alterar número!");
                    }

                    case 5 -> {
                        System.out.print("Insira o novo email: ");
                        String novo = tc.nextLine();
                        if (con.Update("cliente", "email", "'" + novo + "'",
                                "WHERE id_cliente = " + rs.getString("id_cliente"))) {
                            System.out.println("Email alterado com sucesso!");
                        } else
                            System.out.println("Falha ao alterar email!");
                    }

                    case 6 -> {
                        return;
                    }
                    default -> System.out.println("OPÇÃO INVÁLIDA!");
                }
            }
        }catch (Exception e){
            System.out.println("Falha ao alterar cliente..." + e);
        }
    }

    public void printProduto(String colunas){con.printa("Produto", colunas);}
    public void printProduto(String id, String colunas){con.printa("Produto", id, colunas);}

    public void printCliente(String colunas){con.printa("cliente", colunas);}
    public void printCliente(String id, String colunas){con.printa("cliente", id, colunas);}

    public void printVendedor(String colunas){con.printa("vendedor", colunas);}
    public void printVendedor(String id, String colunas){con.printa("vendedor", id, colunas);}

    public void printCompra(String colunas){con.printa("compra", colunas);}
    public void printCompra(String id, String colunas){
        try {
            ResultSet rs = con.Select("id_carrinho", "compra", id, "id_compra");
            rs.next();

            int idCarrinho = rs.getInt("id_carrinho");

            rs = con.Select("id_produto", "carrinho_produto", Integer.toString(idCarrinho),
                    "id_carrinho");

            con.printa("compra", id, colunas);
            System.out.println("Produtos:");
            while (rs.next()) {
                con.printa("produto", rs.getString("id_produto"), "nome, preco, quantidade_estoque");
            }
        } catch (Exception e){
            System.out.println("Falha ao printar compra..." + e);
        }
    }

    private void produtoAdquirido (int id_produto, int quantidade) {
        con.Update("produto", "quantidade_estoque", "quantidade_estoque - "
                + quantidade, " WHERE id_produto = " + id_produto);
    }

    public void adicionaproduto_noEstoque(Scanner tc) {
        LinkedList<Integer> produtos = new LinkedList<>();
        LinkedList<Integer> quantidade = new LinkedList<>();
        while (true){
            System.out.println("Qual o ID do produto recebido?");
            produtos.add(Integer.parseInt(tc.nextLine()));

            System.out.println("Qual a quantidade de produtos com esse ID que foram recebidos?");
            quantidade.add(Integer.parseInt(tc.nextLine()));

            System.out.println("Foram recebidos outros produtos com IDs diferentes?");
            if (!tc.nextLine().equalsIgnoreCase("sim"))
                break;
        }
        while (!produtos.isEmpty()){
            if (!con.Update("produto", "quantidade_estoque", "quantidade_estoque + " +
                    quantidade.pop().toString(), "WHERE id_produto =" + produtos.pop())){
                System.out.println("Não existe esse produto!");
                return;}

            System.out.println("ATUALIZAÇÃO FEITA COM SUCESSO!");
        }

    }


    public Integer getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

}
