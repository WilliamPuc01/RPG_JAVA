public class Inimigo extends Personagem {

    //super eh uma palavra-chave do Java para acessar a classe "Mãe"
    public Inimigo(String nome, int pontosVida, int ataque, int defesa, int nivel) {
        super(nome, pontosVida, ataque, defesa, nivel);
    }

    //original eh o nome que demos a variavel que guarda o objeto que estamos copiando.
    public Inimigo(Inimigo original) {
        super(original);
    }
}