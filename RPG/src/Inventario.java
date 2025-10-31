import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;

public class Inventario implements Cloneable {
    //Esse "<>" sao os Generics, eles sao uma forma de garantir que essa List so
    //aceite o tipo de objeto que voce quer
    private List<Item> itens;

    public Inventario() {
        this.itens = new ArrayList<>();
    }

    //Construtor de copia profunda(deep copy)
    //vamos usar para os save points do personagem.
    public Inventario(Inventario original) {
        this.itens = new ArrayList<>();

        for (Item itemOriginal : original.itens) {
            this.itens.add(new Item(itemOriginal));
        }
    }

    public void adicionarItem(Item itemParaAdicionar) {
        //O metodo indexOf usa o Item.equals() que implementamos(ele checa por nome)
        int indice = this.itens.indexOf(itemParaAdicionar);

        //se for diferente de -1 quer dizer que achou. Ja que o indice comeca no 0
        if (indice != -1) {
            Item itemExistente = this.itens.get(indice);
            itemExistente.adicionarQuantidade(itemParaAdicionar.getQuantidade());
        } else {
            this.itens.add(itemParaAdicionar);
        }
    }

    public boolean usarItemPorNome(String nomeDoItem) {
        //Usamos um Iterator para poder remover itens da lista
        //de forma segura ENQUANTO iteramos por ela
        //(ele sabe como se remover da lista sem quebrar o jogo)
        Iterator<Item> iterador = this.itens.iterator();

        //hasNext() eh o metodo que o loop while usa para perguntar a ele: "Ja acabamos?" kkkkk
        while (iterador.hasNext()) {
            //iterador.next(): Pega o item atual e passa para o proximo
            Item item = iterador.next();

            if (item.getNome().equals(nomeDoItem)) {
                item.usarItem();

                if (item.getQuantidade() <= 0) {
                    //Remove da lista o ultimo item que next() te deu
                    iterador.remove();
                    System.out.println(item.getNome() + " acabou.");
                }
                return true;
            }
        }

        System.out.println("Item: " + nomeDoItem + " nao encontrado no inventario.");
        return false;
    }

    public boolean removerQuantidadePorNome (String nomeDoItem, int quantidadeParaRemover) {
        Iterator<Item> iterador = this.itens.iterator();

        while (iterador.hasNext()) {
            Item item = iterador.next();

            if (item.getNome().equals(nomeDoItem)) {
                if (item.getQuantidade() >= quantidadeParaRemover) {
                    item.setQuantidade(item.getQuantidade() - quantidadeParaRemover);
                    System.out.println(quantidadeParaRemover + "x " + item.getNome() + " foram removidos do invetario.");

                    if (item.getQuantidade() <= 0) {
                        iterador.remove();
                        System.out.println(item.getNome() + " acabou.");
                    }
                    return true;//Remocao bem sucedida.
                } else {
                    return false;//Nao tem quantidade suficiente.
                }
            }
        }
        return false;//Item nao encontrado.
    }

    @Override
    public String toString() {
        if (this.itens.isEmpty()) {
            return "Inventario vazio.";
        }

        //Ordena a lista usando o Item.compareTo().
        Collections.sort(this.itens);

        StringBuilder sb = new StringBuilder();
        sb.append("--- Inventario Ordenado ---\n");
        for (Item item : this.itens) {
            sb.append(item.toString()).append("\n");
        }
        sb.append("---------------------------\n");
        return sb.toString();
    }

    @Override
    public Inventario clone() {
        try {
            //Primeiro fazer uma copia rasa.
            Inventario inventarioClonado = (Inventario) super.clone();

            //Corrigir a copia rasa. Criando uma nova lista de itens.
            inventarioClonado.itens = new ArrayList<>();

            //Enche a nova lista com copias dos itens originais.
            for (Item itemOriginal : this.itens) {
                inventarioClonado.itens.add(new Item(itemOriginal));
            }
            return inventarioClonado;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Falha ao clonar inventario", e);
        }
    }

    //Metodo auxiliar para pegar o item (para aplicar efeito)
    public Item getItemPorNome(String nomeDoItem) {
        for (Item item : this.itens) {
            if (item.getNome().equals(nomeDoItem)) {
                return item;
            }
        }
        return null;
    }

    //Importante para o sistema de loot.
    public List<Item> getItens() {
        return this.itens;
    }

    public boolean estaVazio() {
        return this.itens.isEmpty();
    }
}