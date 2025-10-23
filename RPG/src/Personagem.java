import java.util.Objects;

public abstract class Personagem {

    protected String nome;
    protected int pontosVida;
    protected int pontosVidaMax;
    protected int ataque;
    protected int defesa;
    protected int nivel;
    protected Inventario inventario;
    protected boolean estaVivo;

    //Construtor principal
    public Personagem(String nome, int pontosVida, int ataque, int defesa, int nivel) {
        this.nome = nome;
        this.pontosVida = pontosVida;
        this.pontosVidaMax = pontosVida;
        this.ataque = ataque;
        this.defesa = defesa;
        this.nivel = nivel;
        this.inventario = new Inventario();
        this.estaVivo = true;
    }

    public Personagem(Personagem original) {
        this.nome = original.nome;
        this.pontosVida = original.pontosVida;
        this.pontosVidaMax = original.pontosVidaMax;
        this.ataque = original.ataque;
        this.defesa = original.defesa;
        this.nivel = original.nivel;
        //Criar um construtor de copia do inventario
        this.inventario = new Inventario(original.inventario);
        this.estaVivo = original.estaVivo;
    }

    public void receberDano(int danoRecebido) {
        if (!this.estaVivo) return;

        this.pontosVida -= danoRecebido;
        System.out.println(this.nome + " recebeu dano " + danoRecebido + " de dano!");

        if (this.pontosVida <= 0) {
            this.pontosVida = 0;
            this.estaVivo = false;
            System.out.println(this.nome + " foi derrotado!");
        }
    }

    public void curar(int pontosDeCura) {
        if (!this.estaVivo) return;

        this.pontosVida += pontosDeCura;
        if(this.pontosVida > this.pontosVidaMax) {
            this.pontosVida = this.pontosVidaMax;
        }
        System.out.println(this.nome + " foi curado em " + pontosDeCura + " pontos.");
    }

    public String getNome() {
        return nome;
    }

    public int getPontosVida() {
        return pontosVida;
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefesa() {
        return defesa;
    }

    public int getNivel() {
        return nivel;
    }

    public Inventario getInventario() {
        return inventario;
    }

    public boolean isEstaVivo() {
        return estaVivo;
    }

    public void setPontosVida(int pontosVida) {
        this.pontosVida = pontosVida;
        if(this.pontosVida > this.pontosVidaMax) {
            this.pontosVida = this.pontosVidaMax;
        }
        if (this.pontosVida <= 0) {
            this.pontosVida = 0;
            this.estaVivo = false;
        }
    }

    @Override
    public String toString() {
        String status = this.estaVivo ?"Vivo":"Derrotado";
        return String.format("[%s (Nivel: %d)] - HP: %d/%d | Atk: %d | Def: %d | Status: %s",
                this.nome, this.nivel, this.pontosVida, this.pontosVidaMax,
                this.ataque, this.defesa, status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Personagem that = (Personagem) o;
        return Objects.equals(nome, that.nome);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome);
    }

}



