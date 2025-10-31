public class Croissant extends Personagem {
    private static final int HP_INICIAL_BOLO = 90;
    private static final int ATAQUE_INICIAL_BOLO = 18;
    private static final int DEFESA_INICIAL_BOLO = 10;
    private static final int NIVEL_INICIAL = 1;

    //super eh uma palavra-chave do Java para acessar a classe "Mãe"
    public Croissant(String nome) {
        super(nome, HP_INICIAL_BOLO, ATAQUE_INICIAL_BOLO, DEFESA_INICIAL_BOLO, NIVEL_INICIAL);
    }

    //original eh o nome que demos a variavel que guarda o objeto que estamos copiando.
    public Croissant(Croissant original) {
        super(original);
    }
}