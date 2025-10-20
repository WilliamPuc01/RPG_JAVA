public abstract class  Personagem {
    private String nome;
    private double pontosVida;
    private double ataque;
    private int nivel;
    private Inventario inventario;


    public Personagem(String nome, double pontosVida, double ataque, int nivel, Inventario inventario){
        this.nivel = nivel;
        this.nome = nome;
        this.pontosVida= pontosVida;
        this.ataque= ataque;
        this.inventario = inventario;
    }


}



