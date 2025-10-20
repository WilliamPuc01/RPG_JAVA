public class Item implements Cloneable, Comparable<Item>{
    private String nome;
    private String descricao;
    private String efeito;
    private int quantidade;

    public Item(String nome, String descricao, String efeito, int quantidade){
        this.nome = nome;
        this.descricao = descricao;
        this.efeito = efeito;
        this.quantidade = quantidade;
    }


    @Override
    public boolean equals( Object obj){
        if(obj == this) return true;
        if(obj == null) return false;
        if( obj.getClass() != this.getClass()) return false;
        Item i = (Item)obj;
        return nome.equals(i.nome) &&
                descricao.equals(i.descricao) &&
                efeito.equals(i.efeito);

    }

    @Override
    public int hashCode(){
        int retorno = 1;

        retorno = retorno * 2 + (this.nome).hashCode();
        retorno = retorno * 2 + (this.descricao).hashCode();
        retorno = retorno * 2 + (this.efeito).hashCode();


        return retorno;
    }


    @Override
    public int compareTo(Item i) {
        if (this == i) return 0;

        int nomeComparacao = this.nome.compareToIgnoreCase(i.nome);
        if (nomeComparacao != 0)
            return nomeComparacao;

        return Integer.compare(this.quantidade, i.quantidade);
    }
}
