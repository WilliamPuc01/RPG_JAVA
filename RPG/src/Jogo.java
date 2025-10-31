import java.util.Random;
import java.util.Scanner;

public class Jogo {
    //Constantes para substituir 'Numeros Magicos'
    private static final int DADO_LADOS_BATALHA = 20; // Nosso D20
    private static final int DANO_MINIMO_ATAQUE = 1;
    private static final int TESTE_SUCESSO_FUGA = 10;

    private static final int CURA_PADRAO_FERMENTO = 25;
    private static final int CURA_GRANDE_BAUNILHA = 50;
    private static final int BONUS_ATAQUE_CAFE = 10;
    private static final int DEBUFF_DEFESA_FARINHA = -5;

    private static final int DANO_ARMADILHA_DESPENSA = 5;
    private static final int TESTE_SUCESSO_ARMADILHA = 10;

    private static final int QTD_QUEST_RATO_FARINHA = 3;

    private static final int XP_POR_NIVEL_INIMIGO = 25;

    private Personagem jogador;
    private final Scanner scanner;
    private final Random dado;
    private boolean jogoRodando;
    private int bonusAtaqueJogador;
    private int bonusDefesaInimigo;
    private String localAtual;
    private Personagem jogadorSalvo;
    private String localSalvo;
    private boolean questRatoCompleta = false;


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
        //Ganha dois fermentos por iniciar
        Item itemInicial = new Item("Fermento", "Um fermento de boa qualidade.","CURA", 2);
        this.jogador.getInventario().adicionarItem(itemInicial);
        this.localAtual = "Balcao";
        System.out.println("\n" + this.jogador.getNome() +
                " comeca sua jornada no " + localAtual + "!");
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

    //Loop principal:
    public void loopPrincipal() {
        while (this.jogoRodando) {
            exibirMenuLocal();
            String escolha = scanner.nextLine();
            processarEscolha(escolha);
            if (!this.jogador.isEstaVivo()) {
                handleMorte();
            }
        }
        System.out.println("\nGAME OVER. Obrigado por jogar.");
    }

    //METODO BATALHA!!!
    public void batalhar(Inimigo inimigo) {
        //Reseta os buffs e debuffs dos personagens no inicio de cada batalha.
        this.bonusAtaqueJogador = 0;
        this.bonusDefesaInimigo = 0;

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
                    //Passamos true para indicar que estamos em batalha
                    gerenciarInventario(true);
                    break;
                case "3":
                    //Jogador escolheu fugir
                    jogadorFugiu = tentarFugir();
                    break;
                default:
                    System.out.println("Escolha inválida. Você perdeu o turno!");
                    break;
            }

            if(jogadorFugiu) {
                break;
            }

            //Turno do inimigo
            if (inimigo.isEstaVivo()) {
                System.out.println("\nTurno do " + inimigo.getNome() + "...");
                realizarAtaque(inimigo, this.jogador);
            }

            //Status do fim da batalha
            System.out.println("\n--- Fim da Rodada ---");
            System.out.println(this.jogador.toString());
            if (this.bonusAtaqueJogador > 0) {
                System.out.println(" (Nonus de Ataque Ativo: +" + this.bonusAtaqueJogador + ")");
            }

            System.out.println(inimigo.toString());
            if (this.bonusDefesaInimigo > 0) {
                System.out.println(" (Debuff de Defesa Ativo: " + this.bonusDefesaInimigo + ")");
            }
            System.out.println("---------------------\n");
        }

        //Fim da batalha
        if (this.jogador.isEstaVivo() && !inimigo.isEstaVivo()) {
            System.out.println("VITORIA! Voce derrotou o " + inimigo.getNome() + "!");

            int xpGanha = inimigo.getNivel() * XP_POR_NIVEL_INIMIGO;
            this.jogador.ganharExperiencia(xpGanha);

            System.out.println("Saqueando os restos do inimigo...");
            Inventario loot = inimigo.getInventario().clone();

            if (!loot.estaVazio()) {
                System.out.println("Voce encontrou os seguintes itens: ");

                for (Item itemDoLoot : loot.getItens()) {
                    System.out.println("- " + itemDoLoot.getNome() + " (Qtd: " + itemDoLoot.getQuantidade() + ")");

                    this.jogador.getInventario().adicionarItem(itemDoLoot);
                }
            } else {
                System.out.println("O inimgo nao tinha nada de valor.");
            }

        } else if (!this.jogador.isEstaVivo()) {
            System.out.println("Derrota... Voce foi vencido pelo " + inimigo.getNome() + ".");
            this.jogoRodando = false;
        }
    }

    //Metodo para calcular o ataque
    private void realizarAtaque(Personagem atacante, Personagem defensor) {
        int rolagemDado = this.dado.nextInt(DADO_LADOS_BATALHA) + 1;

        int danoBase = atacante.getAtaque();
        int defesaDefensor = defensor.getDefesa();

        //Aplicar buff se o jogador estiver atacando
        if (atacante.equals(this.jogador)) {
            danoBase += this.bonusAtaqueJogador;
        }

        //Aplica debuff no inimigo
        if (defensor instanceof Inimigo) {
            defesaDefensor += this.bonusDefesaInimigo;
            if (defesaDefensor < 0) {
                defesaDefensor = 0;
            }
        }

        //Formula de dano:
        int danoTotal = (danoBase + rolagemDado) - defesaDefensor;

        //Dano tem que ser no minimo 1, caso a defesa for mto alta
        if (danoTotal <= 0) {
            danoTotal = DANO_MINIMO_ATAQUE;
            System.out.println(atacante.getNome() + " rolou " + rolagemDado +
                    ", mas a defesa de " + defensor.getNome() + "eh muito alta!");
        } else {
            System.out.println(atacante.getNome() + " ataca! (Rolagem do dado: " + rolagemDado + ")");
        }

        defensor.receberDano(danoTotal);
    }

    //Fuga!(com chances de falha)

    private boolean tentarFugir() {
        int rolagemFuga = this.dado.nextInt(DADO_LADOS_BATALHA) + 1;

        if (rolagemFuga >= TESTE_SUCESSO_FUGA) {
            System.out.println("Voce rolou " + rolagemFuga + " e conseguiu fugir com sucesso!");
            return true;
        } else {
            System.out.println("Voce rolou " + rolagemFuga + " e sua tentativa de fuga falhou...");
            System.out.println("Voce perdeu o turno...");
            return false;
        }
    }

    //Usar itens do inventario
    public void gerenciarInventario(boolean emBatalha) {
        System.out.println("\n--- Meu Inventario ---");

        String listaItens = this.jogador.getInventario().toString();
        System.out.println(listaItens);

        if (listaItens.contains("vazio")) {
            return;
        }

        System.out.println("Digite o nome do item que deseja usar (ou 'voltar'):");
        System.out.print("Sua escolha: ");
        String nomeItem = scanner.nextLine();

        if (nomeItem.equalsIgnoreCase("voltar")) {
            return;
        }

        Item item = this.jogador.getInventario().getItemPorNome(nomeItem);

        if (item == null) {
            System.out.println("Voce nao possui o item: " + nomeItem + ".");
            return;
        }

        //Aplicar efeito do item
        switch (item.getEfeito().toUpperCase()) {
            case "CURA":
                int cura = CURA_PADRAO_FERMENTO;
                this.jogador.curar(cura);
                System.out.println(this.jogador.getNome() + " usou " + item.getNome() + " e curou " +
                        cura + "de HP.");

                this.jogador.getInventario().usarItemPorNome(item.getNome());
                break;

            case "CURA_GRANDE":
                int curaGrande = CURA_GRANDE_BAUNILHA;
                this.jogador.curar(curaGrande);
                System.out.println(this.jogador.getNome() + " usou " + item.getNome() + " e curou " +
                        curaGrande + "de HP.");
                this.jogador.getInventario().usarItemPorNome(item.getNome());
                break;

            case "ATAQUE_UP":
                if (!emBatalha) {
                    System.out.println("Esse item so pode ser usado em batalha.");
                    return;
                }
                int bonusAtk = BONUS_ATAQUE_CAFE;
                this.bonusAtaqueJogador += bonusAtk;
                System.out.println(this.jogador.getNome() + " usou " + item.getNome() + "! Seu ataque aumentou em " +
                        bonusAtk + " nesta batalha.");
                this.jogador.getInventario().usarItemPorNome(item.getNome());
                break;

            case "DEBUFF_DEFESA" :
                if (!emBatalha) {
                    System.out.println("Esse item so pode ser usado em batalha.");
                    return;
                }
                int debuffDef = DEBUFF_DEFESA_FARINHA;
                this.bonusDefesaInimigo += debuffDef;
                System.out.println(this.jogador.getNome() + " usou " +
                        item.getNome() + "! A defesa do inimigo foi reduzida em 5.");
                this.jogador.getInventario().usarItemPorNome(item.getNome());
                break;

            case ("ITEM_QUEST") :
                System.out.println("Esse item parece ser importante para uma missao e nao pode ser usado agora.");
                break;


            default:
                System.out.println("Este item nao tem efeito aparente.");
                break;
        }
    }

    private void exibirMenuLocal() {
        System.out.println("\n==================================");
        System.out.println("Você esta em: " + this.localAtual);
        System.out.println("==================================\n");

        switch (this.localAtual) {
            case "Balcao":
                System.out.println("Voce esta no balcao principal. O ar cheira a acucar.\n");
                System.out.println("1. Ir para a 'Despensa' (Escura e empoeirada)");
                System.out.println("2. Ir para perto do 'Forno' (Quente e barulhento)");
                System.out.println("3. Falar com o 'Rato Padeiro'");

                if (this.questRatoCompleta) {
                    System.out.println("4. Ir para a 'Sala  do Trono de Acucar' (Revelada pelo Rato)");
                }
                break;

            case "Despensa":
                System.out.println("Voce esta na despensa. Sacos de farinha por toda parte.\n");
                System.out.println("1. Voltar para o 'Balcao'");
                System.out.println("2. Vasculhar um saco de farinha suspeito");
                System.out.println("3. Procurar 'Fermento' (Guardado por uma Massa Crua)");
                System.out.println("4. Procurar 'Farinha Velha' (Guardado por um Pao Mofado)");
                break;

            case "Forno":
                System.out.println("Voce esta perto do Forno. O calor eh intenso.\n");
                System.out.println("1. Voltar para o 'Balcao'");
                System.out.println("2. Pegar 'Cafe Expresso' (Guardado por um Cafe Queimado)");
                break;

            case "Sala do Trono de Acucar":
                System.out.println("Uma montanha de acucar cristalizado se ergue no centro.\n");
                System.out.println("1. Voltar para o 'Balcao' (Recuar)");
                System.out.println("2. Confrontar o 'Lorde Diabetes'");
                break;
        }

        System.out.println("\n--- Acoes Comuns ---");
        System.out.println("i. Ver Inventario e Usar Itens");
        System.out.println("s. Ver Status do Personagem");
        System.out.println("d. Descansar e Salvar Jogo");
        System.out.println("q. Sair do Jogo");
        System.out.print("Sua escolha: ");
    }

    private void processarEscolha(String escolha) {

        if (processarAcaoComum(escolha)) {
            return;
        }

        switch (this.localAtual) {
            case "Balcao":
                processarEscolhaBalcao(escolha);
                break;
            case "Despensa":
                processarEscolhaDespensa(escolha);
                break;
            case "Forno":
                processarEscolhaForno(escolha);
                break;
            case "Sala do Trono de Acucar":
                processarEscolhaSalaDoTrono(escolha);
                break;
            default:
                System.out.println("\nOpcao invalida. Tente novamente.");
                break;
        }
    }

    //Metodos Auxiliares do processarEscolha()
    private boolean processarAcaoComum(String escolha) {
        switch (escolha.toLowerCase()) {
            case "i":
                gerenciarInventario(false);
                return true;

            case "s":
                System.out.println("\n--- Status Atual ---");
                System.out.println(this.jogador.toString());
                return true;

            case "d":
                if (this.jogador instanceof Bolo) {
                    this.jogadorSalvo = new Bolo((Bolo) this.jogador);
                } else if (this.jogador instanceof Croissant) {
                    this.jogadorSalvo = new Croissant((Croissant) this.jogador);
                } else if (this.jogador instanceof BombaDeChocolate) {
                    this.jogadorSalvo = new BombaDeChocolate((BombaDeChocolate) this.jogador);
                }
                this.localSalvo = this.localAtual;
                this.jogador.setPontosVida(this.jogador.pontosVidaMax);
                System.out.println("Jogo salvo com sucesso! HP foi restaurado.");
                System.out.println("(Save point: " + this.jogador.getNome() + " em " + this.localSalvo + ")");
                return true;

            case "q":
                System.out.println("\nObrigado por jogar! Ate a proxima.");
                this.jogoRodando = false;
                return true;
        }
        return false;
    }

    private void processarEscolhaBalcao(String escolha) {
        switch (escolha) {
            case "1":
                System.out.println("\nVoce abre a porta rangente da despensa...");
                this.localAtual = "Despensa";
                break;
            case "2":
                System.out.println("\nVoce se aproxima do calor do forno...");
                this.localAtual = "Forno";
                break;
            case "3":
                falarComRatoPadeiro();
                break;
            case "4":
                if (this.questRatoCompleta) {
                    System.out.println("\nVoce usa a informacao do Rato e encontra a passagem secreta...");
                    this.localAtual = "Sala do Trono de Acucar";
                } else {
                    System.out.println("Opcao invalida. Tente novamente.");
                }
                break;
            default:
                System.out.println("\nOpcao invalida. Tente novamente.");
        }
    }

    private void processarEscolhaDespensa(String escolha) {
        switch (escolha) {
            case "1":
                System.out.println("\nVoce volta para a luz do balcao.");
                this.localAtual = "Balcao";
                break;
            case "2":
                System.out.println("\nVoce mexe em um saco de farinha aberto...");
                System.out.println("O 'Saco de Farinha' esta ali, mas voce ve o 'clique' de uma armadilha de rato!");
                System.out.print("Tentar pegar o item mesmo assim? (Requer " + TESTE_SUCESSO_ARMADILHA + "+ no D20) (s/n): ");

                String decisao = scanner.nextLine();
                if (decisao.equalsIgnoreCase("s")) {
                    int rolagemArmadilha = this.dado.nextInt(DADO_LADOS_BATALHA) + 1;
                    System.out.println("Voce tenta pegar... (Rolagem D20: " + rolagemArmadilha + ")");

                    if (rolagemArmadilha >= TESTE_SUCESSO_ARMADILHA) {
                        System.out.println("Sucesso! Voce desvia da armadilha por um triz!");
                        System.out.println("Voce encontrou um 'Saco de Farinha'!");
                        this.jogador.getInventario().adicionarItem(new Item("Saco de Farinha", "Jogue nos olhos!", "DEBUFF_DEFESA", 1));
                    } else {
                        System.out.println("SNAP! A armadilha acerta sua mao!");
                        System.out.println("Voce nao pegou o item e sofreu " + DANO_ARMADILHA_DESPENSA + " de dano.");
                        this.jogador.receberDano(DANO_ARMADILHA_DESPENSA);
                    }
                } else {
                    System.out.println("\nVoce decide que nao vale o risco e deixa o saco quieto.");
                }
                break;
            case "3":
                System.out.println("\nVoce ve um 'Fermento' brilhando, mas uma Massa Crua gosmenta o protege!");
                System.out.println("A Massa Crua ataca!");
                Inimigo massaCrua = new Inimigo("Massa Crua", 70, 5, 12, 2);
                massaCrua.getInventario().adicionarItem(new Item("Fermento", "Um fermento potente.", "CURA", 1));
                batalhar(massaCrua);
                break;
            case "4":
                System.out.println("\nUm Pao Mofado esta sentado em cima do saco de 'Farinha Velha'!");
                System.out.println("O Pao Mofado ataca!");
                Inimigo paoMofado = new Inimigo("Pao Mofado", 40, 8, 5, 1);
                paoMofado.getInventario().adicionarItem(new Item("Farinha Velha", "Po branco.", "ITEM_QUEST", 1));
                batalhar(paoMofado);
                break;
            default:
                System.out.println("\nOpcao invalida. Tente novamente.");
        }
    }

    private void processarEscolhaForno(String escolha) {
        switch (escolha) {
            case "1":
                System.out.println("\nVoce se afasta do calor.");
                this.localAtual = "Balcao";
                break;
            case "2": // Luta pelo BUFF (Cafe Queimado)
                System.out.println("\nUma xicara de 'Cafe Expresso' esta fumegando. Mas eh uma armadilha!");
                System.out.println("Um Cafe Queimado salta em voce!");
                Inimigo cafeQueimado = new Inimigo("Cafe Queimado", 35, 15, 3, 2);
                cafeQueimado.getInventario().adicionarItem(new Item("Cafe Expresso", "Ataque rapido!", "ATAQUE_UP", 1));
                batalhar(cafeQueimado);
                break;
            default:
                System.out.println("\nOpcao invalida. Tente novamente.");
        }
    }

    private void processarEscolhaSalaDoTrono(String escolha) {
        switch (escolha) {
            case "1":
                System.out.println("\nVoce recua para o balcao.");
                this.localAtual = "Balcao";
                break;
            case "2":
                System.out.println("\n'LORDE DIABETES': 'Entao voce chegou, Docinho insolente!'");
                System.out.println("O Lorde se levanta de seu trono de acucar!");

                Inimigo lordeDiabetes = new Inimigo("Lorde Diabetes", 300, 25, 15, 10);
                lordeDiabetes.getInventario().adicionarItem(new Item("Coroa de Acucar", "O trofeu da sua vitoria.", "TROFEU", 1));

                batalhar(lordeDiabetes);

                if (!lordeDiabetes.isEstaVivo()) {
                    System.out.println("\nCom um ultimo suspiro acucarado, o Lorde cai!");
                    System.out.println("Voce venceu! A Padaria esta salva da tirania doce!");
                    this.jogoRodando = false;
                }
                break;
            default:
                System.out.println("\nOpcao invalida. Tente novamente.");
        }
    }

    private void handleMorte() {
        System.out.println("\nVOCE FOI DERROTADO...");

        if (this.jogadorSalvo != null) {
            System.out.println("...mas voce tem um save point!");
            System.out.print("Deseja carregar seu ultimo save? (s/n): ");
            String escolha = scanner.nextLine();

            if (escolha.equalsIgnoreCase("s")) {
                //Criamos uma NOVA COPIA do save, para que o save original
                //nao seja modificado se o jogador morrer de novo.
                if (this.jogadorSalvo instanceof Bolo) {
                    this.jogador = new Bolo((Bolo) this.jogadorSalvo);
                } else if (this.jogadorSalvo instanceof Croissant) {
                    this.jogador = new Croissant((Croissant) this.jogadorSalvo);
                } else if (this.jogadorSalvo instanceof BombaDeChocolate) {
                    this.jogador = new BombaDeChocolate((BombaDeChocolate) this.jogadorSalvo);
                }

                this.localAtual = this.localSalvo;

                System.out.println("\nJogo carregado! Voce esta de volta ao " + this.localAtual + ".");
            } else {
                this.jogoRodando = false;
            }
        } else {
            this.jogoRodando = false;
        }
    }

    private void falarComRatoPadeiro() {
        System.out.println("\n--- Dialogo: Rato Padeiro ---");

        if (this.questRatoCompleta) {
            System.out.println("'Rato Padeiro': (Chiando) 'Obrigado pela ajuda, " + this.jogador.getNome() + "! Cuidado com o Lorde!'");
            return;
        }

        //Verifica se o jogador tem os itens da quest
        Item itemQuest = this.jogador.getInventario().getItemPorNome("Farinha Velha");

        int qtdNecessaria = QTD_QUEST_RATO_FARINHA;
        int qtdAtual = (itemQuest == null) ? 0 : itemQuest.getQuantidade();

        if (qtdAtual >= qtdNecessaria) {
            //O jogador tem os itens!!
            System.out.println("'Rato Padeiro': (Chiando) 'Você conseguiu! Minhas 3 Farinhas Velhas!'");
            System.out.println("'Rato Padeiro': 'Muito obrigado! Tome isto como recompensa.'");

            //Remove os itens da quest
            this.jogador.getInventario().removerQuantidadePorNome("Farinha Velha", 3);

            //Da a recompensa (Essencia de Baunilha - Cura Grande)
            Item recompensa = new Item("Essencia de Baunilha", "Cura 50 HP.", "CURA_GRANDE", 1);
            this.jogador.getInventario().adicionarItem(recompensa);

            System.out.println("'Rato Padeiro': 'Aquele tirano, o Lorde Diabetes, esta escondido!'");
            System.out.println("'Rato Padeiro': 'Ele esta na Sala do Trono de Acucar, passando pelo balcao!'");

            this.questRatoCompleta = true;

        } else {
            //O jogador ainda nao tem os itens
            System.out.println("'Rato Padeiro': (Chiando) 'Ola, valente! Estou tentando fazer um Pao Divino!'");
            System.out.println("'Rato Padeiro': 'Mas os Paes Mofados roubaram minha Farinha Velha!'");
            System.out.println("'Rato Padeiro': 'Traga-me " + qtdNecessaria +" sacos de Farinha Velha e eu o recompensarei!'");
            System.out.println("(Voce tem " + qtdAtual + "/" + qtdNecessaria + " Farinha Velha)");
        }
    }
}