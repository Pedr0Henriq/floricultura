package entidades;

public class Cliente {

    private int id;
    private String nome;
    private String cpf;
    private String senha;
    private String email;
    private String rua;
    private int numero;

    public Cliente(String nome, String cpf, String senha, String email, String rua, int numero) {
        this.nome = nome;
        this.cpf = cpf;
        this.senha = senha;
        this.email = email;
        this.rua = rua;
        this.numero = numero;
    }

    public int getId(){
        return this.id;
    }

    public String getNome(){
        return this.nome;
    }

    public String getCpf(){
        return this.cpf;
    }

    public String getSenha(){
        return this.senha;
    }

    public String getEmail(){
        return this.email;
    }

    public String getRua(){
        return this.rua;
    }

    public int getNumero(){
        return this.numero;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setCpf(String cpf){
        this.cpf = cpf;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setRua(String rua){
        this.rua = rua;
    }

    public void setNumero(int numero){
        this.numero = numero;
    }

    public void setId(int id){
        this.id = id;
    }
}
