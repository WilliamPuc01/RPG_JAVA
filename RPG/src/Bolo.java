public class Bolo extends Personagem {

    private static final int HP_INICIAL_BOLO = 120;
    private static final int ATAQUE_INICIAL_BOLO = 10;
    private static final int DEFESA_INICIAL_BOLO = 15;
    private static final int NIVEL_INICIAL = 1;

    public Bolo(String nome) {
        super(nome, HP_INICIAL_BOLO, ATAQUE_INICIAL_BOLO, DEFESA_INICIAL_BOLO, NIVEL_INICIAL);
    }

    public Bolo(Bolo original) {
        super(original);
    }

}