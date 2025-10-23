import java.util.Objects;

public class Item implements Comparable<Item>{
    private String nome;
    private String descricao;
    private String efeito;
    private int quantidade;

    //Construtor
    public Item(String nome, String descricao, String efeito, int quantidade){
        this.nome = nome;
        this.descricao = descricao;
        this.efeito = efeito;
        this.quantidade = quantidade;
    }

    //Getters e Setters(set em quem muda e get em todos.)
    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getEfeito() {
        return  efeito;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    //Metodo para usar o item.
    public void usarItem() {
        if (this.quantidade > 0) {
            this.quantidade--;
            System.out.println("Usou 1 " + this.nome + ". Restam: " + this.quantidade);
        } else {
            System.out.println("Nao ha mais " +this.nome + "Para usar.");
        }
    }

    //Metodo para add mais do mesmo item.
    public void adicionarQuantidade(int quantidadeParaAdicionar) {
        this.quantidade += quantidadeParaAdicionar;
    }

    @Override
    public String toString() {
        return String.format("%s (Qtd: %d) - %s [Efeito: %s]",
                this.nome, this.quantidade, this.descricao, this.efeito);
    }


    @Override
    public boolean equals( Object o){
        if(o == this) return true;
        if(o == null || o.getClass() != this.getClass()) return false;
        Item i = (Item)o;
        return this.nome.equals(i.nome);

    }

    @Override
    public int hashCode(){
        return Objects.hash(this.nome);
    }


    @Override
    public int compareTo(Item outroI) {
        return this.nome.compareTo(outroI.nome);
    }

    public Item(Item itemOriginal) {
        this.nome = itemOriginal.nome;
        this.descricao = itemOriginal.descricao;
        this.efeito = itemOriginal.efeito;
        this.quantidade = itemOriginal.quantidade;
    }
}
