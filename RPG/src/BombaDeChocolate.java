public class BombaDeChocolate extends Personagem {
    private static final int HP_INICIAL_BOLO = 80;
    private static final int ATAQUE_INICIAL_BOLO = 22;
    private static final int DEFESA_INICIAL_BOLO = 8;
    private static final int NIVEL_INICIAL = 1;

    //super eh uma palavra-chave do Java para acessar a classe "Mãe"
    public BombaDeChocolate(String nome) {
        super(nome, HP_INICIAL_BOLO, ATAQUE_INICIAL_BOLO, DEFESA_INICIAL_BOLO, NIVEL_INICIAL);
    }

    //original eh o nome que demos a variavel que guarda o objeto que estamos copiando.
    public BombaDeChocolate(BombaDeChocolate original) {
        super(original);
    }
}