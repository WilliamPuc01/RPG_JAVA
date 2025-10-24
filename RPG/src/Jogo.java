import java.util.Random;
import java.util.Scanner;

public class Jogo {
    private Personagem jogador;
    private Scanner scanner;
    private Random dado;
    private boolean jogoRodando;

    public static void main(String[] args) {
        Jogo meuJogo = new Jogo();

        meuJogo.iniciar();
    }

    public Jogo() {
        this.scanner = new Scanner(System.in);

        this.dado = new Random();
        this.jogoRodando = true;
    }

    public void iniciar() {
        System.out.println("=========================================");
        System.out.println("   Bem-vindo a BATALHA DA PADARIA RPG!");
        System.out.println("=========================================");

        criarPersonagem();

        Item itemInicial = new Item("Fermento", "Um fermento de boa qualidade.","CURA", 2);
        this.jogador.getInventario().adicionarItem(itemInicial);

        System.out.println("\n" + this.jogador.getNome() + "comeca sua jornada!");

        loopPrincipal();
    }

    public void criarPersonagem() {
        System.out.println("Digite o nome do seu Heroi da Padaria: ");
        String nome = scanner.nextLine();

        System.out.println("Escolha sua classe:");
        System.out.println("1. Bolo (Guerreiro) - (HP: 120, Atk: 10, Def: 15)");
        System.out.println("2. Croissant (Arqueiro) - (HP: 90, Atk: 18, Def: 10)");
        System.out.println("3. Bomba de Chocolate (Mago) - (HP: 80, Atk: 22, Def: 8)");
        System.out.print("Digite o numero (1, 2 ou 3): ");

        int escolha = -1;
        while (escolha < 1 || escolha > 3) {
            try {
                escolha = Integer.parseInt(scanner.nextLine());
                switch (escolha) {
                    case 1:
                        this.jogador = new Bolo(nome);
                        break;
                    case 2:
                        this.jogador = new Croissant(nome);
                        break;
                    case 3:
                        this.jogador = new BombaDeChocolate(nome);
                        break;
                    default:
                        System.out.print("Entrada invalida. Digite 1, 2 ou 3: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Entrada invalida. Digite um numero(1, 2 ou 3): ");
            }
        }

        System.out.println("\nPersonagem criado com sucesso!");
        System.out.println(this.jogador.toString());
    }

    //Loop principal: Historia e navegacao.
    public void loopPrincipal() {
        while (this.jogoRodando && this.jogador.isEstaVivo()) {
            System.out.println("\n--- Menu Principal ---");
            System.out.println("O que voce deseja fazer?");
            System.out.println("1. Explorar a Cozinha (Navegacao)");
            System.out.println("2. Ver Inventario e Usar Itens");
            System.out.println("3. Ver Status do Personagem");
            System.out.println("9. Sair do Jogo");
            System.out.print("Sua escolha: ");

            String escolha = scanner.nextLine();

            switch (escolha) {
                case "1":
                    //explorar();
                    System.out.println("\nVoce se aventura pela cozinha escura...");
                    //Vamos adicionar a batalha aqui em breve
                    break;
                case "2":
                    //gerenciarInventario();
                    System.out.println("\nVisualizando inventario...");
                    break;
                case "3":
                    //Mostra o status do jogador
                    System.out.println("\n--- Status Atual ---");
                    System.out.println(this.jogador.toString());
                    break;
                case "9":
                    System.out.println("\nObrigado por jogar! Ate a proxima.");
                    this.jogoRodando = false;
                    break;
                default:
                    System.out.println("\nOpcao invalida. Tente novamente.");
                    break;
            }
        }

        if (!this.jogador.isEstaVivo()) {
            System.out.println("\nGAME OVER.");
        }

        //METODO BATALHA!!!
        public void batalhar(Inimigo inimigo) {
            System.out.println("\n--- BATALHA INICIADA ---");
            System.out.println(this.jogador.getNome() + " encontra um " + inimigo.getNome() + "!");
            System.out.println(this.jogador.toString());
            System.out.println(inimigo.toString());
            System.out.println("--------------------------\n");

            while (this.jogador.isEstaVivo() && inimigo.isEstaVivo()) {

                System.out.println("Turno do Jogador. O que fazer?");
                System.out.println("1. Atacar");
                System.out.println("2. Usar Item (Inventário)");
                System.out.println("3. Tentar Fugir");
                System.out.print("Sua escolha: ");
                String escolha = scanner.nextLine();

                boolean jogadorFugiu = false;

                switch (escolha) {
                    case "1":
                        realizarAtaque(this.jogador, inimigo);
                        break;
                    case "2":
                        //Passamos 'true' para indicar que estamos em batalha
                        gerenciarInventario(true);
                        break;
                    case "3":
                        //Jogador escolheu fugir (Requisito 5)
                        jogadorFugiu = tentarFugir();
                        break;
                    default:
                        System.out.println("Escolha inválida. Você perdeu o turno!");
                        break;
                }

                if(jogadorFugiu) {
                    break;
                }

                //Turno do inimigo.
                if (inimigo.isEstaVivo()) {
                    System.out.println("\nTurno do " + inimigo.getNome() + "...");
                    realizarAtaque(inimigo, this.jogador);
                }

                //Status do fim da batalha.
                System.out.println("\n--- Fim da Rodada ---");
                System.out.println(this.jogador.toString());
                System.out.println(inimigo.toString());
                System.out.println("---------------------\n");
            }

            //Fim da batalha.
            if (this.jogador.isEstaVivo() && !inimigo.isEstaVivo()) {
                System.out.println("VITORIA! Voce derrotou o " + inimigo.getNome() + "!");

                System.out.println("Saqueando os restos do inimigo...");
                Inventario loot = inimigo.getInventario().clone();

                //Sistema de loot a ser implementado aqui.
            } else if (!this.jogador.isEstaVivo()) {
                System.out.println("Derrota... Voce foi vencido pelo " + inimigo.getNome() + ".");
                this.jogoRodando = false;
            }
        }

        //Metodo para calcular o ataque.
    }
}
