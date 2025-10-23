public class Inimigo extends Personagem {

    public Inimigo(String nome, int pontosVida, int ataque, int defesa, int nivel) {
        super(nome, pontosVida, ataque, defesa, nivel);
    }

    public Inimigo(Inimigo original) {
        super(original);
    }

}