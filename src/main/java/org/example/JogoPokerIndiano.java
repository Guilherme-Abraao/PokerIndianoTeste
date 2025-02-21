package org.example;

import java.util.*;

public class JogoPokerIndiano {
    private Baralho baralho;
    private Jogador jogador;
    private Jogador sistema;
    private Set<Carta> cartasUsadas;

    private final List<String> valoresValidos = Arrays.asList("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K");
    private final Map<String, String> naipesValidos = criarMapNaipesValidos();

    public JogoPokerIndiano() {
        baralho = new Baralho();
        cartasUsadas = new HashSet<>();
    }

    public void iniciar() {
        Scanner scanner = new Scanner(System.in);
        jogador = criarJogador(scanner);
        sistema = criarSistema();
        exibirCartas();
        determinarVencedor();
    }

    private Jogador criarJogador(Scanner scanner) {
        Carta carta1 = solicitarCarta(scanner, "primeira");
        Carta carta2 = solicitarCarta(scanner, "segunda");
        baralho.removerCarta(carta1);
        baralho.removerCarta(carta2);
        return new Jogador(carta1, carta2);
    }

    private Jogador criarSistema() {
        Carta cartaSistema1 = gerarCartaSistema();
        Carta cartaSistema2 = gerarCartaSistema();
        return new Jogador(cartaSistema1, cartaSistema2);
    }

    private Carta gerarCartaSistema() {
        Carta carta;
        do {
            carta = baralho.gerarCartaAleatoria();
        } while (cartasUsadas.contains(carta));
        cartasUsadas.add(carta);
        return carta;
    }

    private void exibirCartas() {
        System.out.println("\nSuas cartas: " + jogador.getCarta1() + " e " + jogador.getCarta2());
        System.out.println("Cartas do sistema: " + sistema.getCarta1() + " e " + sistema.getCarta2() + "\n");
    }

    private Carta solicitarCarta(Scanner scanner, String ordem) {
        String valorCarta = solicitarValorCarta(scanner, ordem);
        String naipeCarta = solicitarNaipeCarta(scanner, ordem);
        Carta carta = new Carta(valorCarta, naipeCarta);
        cartasUsadas.add(carta);
        return carta;
    }

    private String solicitarValorCarta(Scanner scanner, String ordem) {
        return solicitarEntrada(scanner, "valor da sua " + ordem + " carta (A, 2, 3... J, Q, K):", valoresValidos);
    }

    private String solicitarNaipeCarta(Scanner scanner, String ordem) {
        return solicitarEntrada(scanner, "naipe da sua " + ordem + " carta (Copas, Ouros, Paus, Espadas):", new ArrayList<>(naipesValidos.keySet()));
    }

    private String solicitarEntrada(Scanner scanner, String mensagem, List<String> valoresValidos) {
        String entrada;
        do {
            System.out.println("Digite o " + mensagem);
            entrada = normalizarEntrada(scanner.nextLine());
            if (!valoresValidos.contains(entrada)) {
                System.out.println("Entrada inválida!\n");
            }
        } while (!valoresValidos.contains(entrada));
        return entrada;
    }

    private String normalizarEntrada(String entrada) {
        return entrada.trim().toUpperCase();
    }

    private static Map<String, String> criarMapNaipesValidos() {
        Map<String, String> naipes = new HashMap<>();
        naipes.put("COPAS", "COPAS");
        naipes.put("OUROS", "OUROS");
        naipes.put("PAUS", "PAUS");
        naipes.put("ESPADAS", "ESPADAS");
        naipes.put("COPA", "COPAS");
        naipes.put("OURO", "OUROS");
        naipes.put("PAU", "PAUS");
        naipes.put("ESPADA", "ESPADAS");
        return naipes;
    }

    private void determinarVencedor() {
        if (jogador.temPar() && !sistema.temPar()) {
            informarVencedor("Você ganhou com um par de " + jogador.getCartaDeMaiorValor().getValor() + "!");
        } else if (!jogador.temPar() && sistema.temPar()) {
            informarVencedor("O adversário ganhou com um par de " + sistema.getCartaDeMaiorValor().getValor() + "!");
        } else if (jogador.temPar() && sistema.temPar()) {
            compararPares();
        } else if (jogador.temMesmoNaipe() && !sistema.temMesmoNaipe()) {
            informarVencedor("Você ganhou com naipes iguais!");
        } else if (!jogador.temMesmoNaipe() && sistema.temMesmoNaipe()) {
            informarVencedor("O adversário ganhou com naipes iguais!");
        } else {
            compararCartas();
        }
    }

    private void compararPares() {
        int resultadoComparacao = compararValores(jogador.getCartaDeMaiorValor(), sistema.getCartaDeMaiorValor());
        if (resultadoComparacao > 0) {
            informarVencedor("Você ganhou com um par de " + jogador.getCartaDeMaiorValor().getValor() + "!");
        } else if (resultadoComparacao < 0) {
            informarVencedor("O adversário ganhou com um par de " + sistema.getCartaDeMaiorValor().getValor() + "!");
        }
    }

    private void compararCartas() {
        Carta maiorCartaJogador = jogador.getCartaDeMaiorValor();
        Carta maiorCartaSistema = sistema.getCartaDeMaiorValor();

        int resultadoComparacao = compararValores(maiorCartaJogador, maiorCartaSistema);
        if (resultadoComparacao == 0) {
            compararSegundasCartas();
        } else if (resultadoComparacao > 0) {
            informarVencedor("Você ganhou com a carta de maior valor!");
        } else {
            informarVencedor("O adversário ganhou com a carta de maior valor!");
        }
    }

    private void compararSegundasCartas() {
        Carta menorCartaJogador = jogador.getCartaDeMenorValor();
        Carta menorCartaSistema = sistema.getCartaDeMenorValor();

        int resultadoComparacao = compararValores(menorCartaJogador, menorCartaSistema);
        if (resultadoComparacao > 0) {
            informarVencedor("Você ganhou com a segunda carta de maior valor!");
        } else if (resultadoComparacao < 0) {
            informarVencedor("O adversário ganhou com a segunda carta de maior valor!");
        } else {
            informarVencedor("Empate nas duas cartas!");
        }
    }

    private void informarVencedor(String mensagem) {
        System.out.println(mensagem);
    }

    private int compararValores(Carta carta1, Carta carta2) {
        return carta1.getValor().compareTo(carta2.getValor());
    }

    public static void main(String[] args) {
        JogoPokerIndiano jogo = new JogoPokerIndiano();
        jogo.iniciar();
    }
}
