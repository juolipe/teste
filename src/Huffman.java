import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class Huffman {

    private int[] frequencias;
    private String[] codigos;
    private No raiz;

    public Huffman() {
        frequencias = new int[256];
        codigos = new String[256];
        raiz = null;
    }
    public void comprimir(String arquivoOriginal, String arquivoComprimido)
            throws IOException {

        contarFrequencias(arquivoOriginal);

        mostrarFrequencias();

        construirArvore();

        System.out.println("--------------------------------------------------");
        System.out.println("ETAPA 2: Min-Heap Inicial (Vetor)");
        System.out.println("--------------------------------------------------");

        gerarCodigos(raiz, "");

        System.out.println("--------------------------------------------------");
        System.out.println("ETAPA 3: Arvore de Huffman");
        System.out.println("--------------------------------------------------");

        mostrarArvore(raiz, "");

        System.out.println("--------------------------------------------------");
        System.out.println("ETAPA 4: Tabela de Codigos de Huffman");
        System.out.println("--------------------------------------------------");

        mostrarCodigos();

        escreverArquivoComprimido(
                arquivoOriginal,
                arquivoComprimido
        );

        System.out.println("--------------------------------------------------");
        System.out.println("ETAPA 5: Resumo da Compressao");
        System.out.println("--------------------------------------------------");

        long tamanhoOriginal = Files.size(
                Path.of(arquivoOriginal)
        );

        long tamanhoComprimido = Files.size(
                Path.of(arquivoComprimido)
        );

        System.out.println(
                "Tamanho original....: "
                + tamanhoOriginal * 8
                + " bits ("
                + tamanhoOriginal
                + " bytes)"
        );

        System.out.println(
                "Tamanho comprimido..: "
                + tamanhoComprimido
                + " bytes"
        );

        double taxa = (
                1.0
                - (double) tamanhoComprimido / tamanhoOriginal
        ) * 100.0;

        System.out.printf(
                "Taxa de compressao..: %.2f%%%n",
                taxa
        );
    }

    private void contarFrequencias(String arquivo)
            throws IOException {

        Arrays.fill(frequencias, 0);

        try (
            InputStream entrada =
                    new FileInputStream(arquivo)
        ) {

            int byteLido;

            while ((byteLido = entrada.read()) != -1) {
                frequencias[byteLido]++;
            }
        }
    }

    private void mostrarFrequencias() {

        System.out.println(
                "--------------------------------------------------"
        );

        System.out.println(
                "ETAPA 1: Tabela de Frequencia de Caracteres"
        );

        System.out.println(
                "--------------------------------------------------"
        );

        for (int i = 0; i < frequencias.length; i++) {

            if (frequencias[i] > 0) {

                char caractere = (char) i;

                if (i >= 32 && i <= 126) {

                    System.out.println(
                            "Caractere '"
                            + caractere
                            + "' (ASCII: "
                            + i
                            + "): "
                            + frequencias[i]
                    );

                } else {

                    System.out.println(
                            "ASCII "
                            + i
                            + ": "
                            + frequencias[i]
                    );
                }
            }
        }
    }


    private void construirArvore() {

        MinHeap heap = new MinHeap();

        // Cria um nó para cada caractere que apareceu
        for (int i = 0; i < frequencias.length; i++) {

            if (frequencias[i] > 0) {

                No novoNo = new No(
                        frequencias[i],
                        (char) i
                );

                heap.inserir(novoNo);
            }
        }

        // Caso exista apenas um caractere no arquivo
        if (heap.tamanho() == 1) {

            raiz = heap.removerMin();

            return;
        }

        // Enquanto houver mais de um nó,
        // junta os dois de menor frequência
        while (heap.tamanho() > 1) {

            No esquerda = heap.removerMin();

            No direita = heap.removerMin();

            No pai = new No(
                    esquerda.frequencia + direita.frequencia,
                    esquerda,
                    direita
            );

            heap.inserir(pai);
        }

        raiz = heap.removerMin();
    }


    private void gerarCodigos(
            No no,
            String codigo
    ) {

        if (no == null) {
            return;
        }

        // Se chegou em uma folha,
        // guarda o código daquele caractere
        if (no.ehFolha()) {

            // Caso especial:
            // arquivo possui apenas um caractere
            if (codigo.isEmpty()) {
                codigo = "0";
            }

            codigos[(int) no.caractere] = codigo;

            return;
        }

        // Esquerda = 0
        gerarCodigos(
                no.esquerda,
                codigo + "0"
        );

        // Direita = 1
        gerarCodigos(
                no.direita,
                codigo + "1"
        );
    }

    private void mostrarCodigos() {

        for (int i = 0; i < codigos.length; i++) {

            if (codigos[i] != null) {

                char caractere = (char) i;

                if (i >= 32 && i <= 126) {

                    System.out.println(
                            "Caractere '"
                            + caractere
                            + "': "
                            + codigos[i]
                    );

                } else {

                    System.out.println(
                            "ASCII "
                            + i
                            + ": "
                            + codigos[i]
                    );
                }
            }
        }
    }

    private void mostrarArvore(
            No no,
            String prefixo
    ) {

        if (no == null) {
            return;
        }

        if (no.ehFolha()) {

            System.out.println(
                    prefixo
                    + "- ('"
                    + no.caractere
                    + "', "
                    + no.frequencia
                    + ")"
            );

        } else {

            System.out.println(
                    prefixo
                    + "- (RAIZ/Nó, "
                    + no.frequencia
                    + ")"
            );
        }

        mostrarArvore(
                no.esquerda,
                prefixo + "  "
        );

        mostrarArvore(
                no.direita,
                prefixo + "  "
        );
    }
    private void escreverArquivoComprimido(
            String arquivoOriginal,
            String arquivoComprimido
    ) throws IOException {

        try (
            DataOutputStream saida =
                    new DataOutputStream(
                            new BufferedOutputStream(
                                    new FileOutputStream(
                                            arquivoComprimido
                                    )
                            )
                    )
        ) {

            for (int frequencia : frequencias) {

                saida.writeInt(frequencia);
            }
            try (
                InputStream entrada =
                        new BufferedInputStream(
                                new FileInputStream(
                                        arquivoOriginal
                                )
                        )
            ) {

                int byteLido;

                int buffer = 0;

                int quantidadeBits = 0;

                while ((byteLido = entrada.read()) != -1) {

                    String codigo = codigos[byteLido];

                    for (int i = 0;
                         i < codigo.length();
                         i++) {

                        // Desloca os bits para a esquerda
                        buffer = (buffer << 1)
                                | (codigo.charAt(i) - '0');

                        quantidadeBits++;

                        // Quando temos 8 bits,
                        // transformamos em um byte
                        if (quantidadeBits == 8) {

                            saida.writeByte(buffer);

                            buffer = 0;

                            quantidadeBits = 0;
                        }
                    }
                }

                // Se sobraram bits, completa com zeros
                if (quantidadeBits > 0) {

                    buffer = buffer << (
                            8 - quantidadeBits
                    );

                    saida.writeByte(buffer);
                }

                // Guarda quantos bits são válidos
                // no último byte
                saida.writeByte(quantidadeBits);
            }
        }
    }
    public void descomprimir(
            String arquivoComprimido,
            String arquivoRestaurado
    ) throws IOException {

        try (
            DataInputStream entrada =
                    new DataInputStream(
                            new BufferedInputStream(
                                    new FileInputStream(
                                            arquivoComprimido
                                    )
                            )
                    )
        ) {

            frequencias = new int[256];

            for (int i = 0; i < 256; i++) {

                frequencias[i] = entrada.readInt();
            }


            construirArvore();


            byte[] dados = entrada.readAllBytes();

            if (dados.length == 0) {
                return;
            }
            int bitsValidosUltimoByte =
                    dados[dados.length - 1] & 0xFF;

            if (bitsValidosUltimoByte == 0) {

                bitsValidosUltimoByte = 8;
            }

            No atual = raiz;

            for (
                int i = 0;
                i < dados.length - 1;
                i++
            ) {

                int valor = dados[i] & 0xFF;

                int quantidadeBits;

                // Se estamos no último byte de dados,
                // usamos somente os bits válidos
                if (i == dados.length - 2) {

                    quantidadeBits =
                            bitsValidosUltimoByte;

                } else {

                    quantidadeBits = 8;
                }

                // Lê os bits da esquerda para a direita
                for (
                    int bit = 7;
                    bit >= 8 - quantidadeBits;
                    bit--
                ) {

                    int valorBit =
                            (valor >> bit) & 1;

                    // 0 = esquerda
                    if (valorBit == 0) {

                        atual = atual.esquerda;

                    // 1 = direita
                    } else {

                        atual = atual.direita;
                    }

                    // Chegou em uma folha
                    if (atual.ehFolha()) {

                        saidaEscreve(
                                arquivoRestaurado,
                                atual.caractere
                        );

                        atual = raiz;
                    }
                }
            }
        }
    }
    private void saidaEscreve(
            String arquivoRestaurado,
            char caractere
    ) throws IOException {

        try (
            FileOutputStream saida =
                    new FileOutputStream(
                            arquivoRestaurado,
                            true
                    )
        ) {

            saida.write(caractere);
        }
    }
}