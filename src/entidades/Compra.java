package entidades;

import java.util.LinkedList;

public class Compra {
    private LinkedList<Produto> produtos;
    private LinkedList<Integer> quantidades;

    public Compra(){
        this.produtos = new LinkedList<Produto>();
        this.quantidades = new LinkedList<Integer>();
    }

    public void adicionarProduto(Produto produto, int quantidade){
        this.produtos.add(produto);
        this.quantidades.add(quantidade);
    }

    public void removerProduto(){
        this.produtos.remove(this.produtos.size()-1);
    }

    public void getCompra(){
        for(int i = 0; i < this.produtos.size(); i++){
            System.out.println("Produto: " + this.produtos.get(i).getId() +
            ", Nome: "+ this.produtos.get(i).getNome()+ ", Preço: R$" + this.produtos.get(i).getPreco()+ ", Quantidade: " + this.quantidades.get(i)+"\n");
        }
    }

    public int getSize(){
        return this.produtos.size();
    }

    public int getQuantidade(int i){
        return this.quantidades.get(i);
    }

    public Produto getProduto(int i){
        return this.produtos.get(i);
    }
}
