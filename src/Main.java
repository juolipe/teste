public class Main {

    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println(
                    "Uso:");
            System.out.println(
                    "Para comprimir:");
            System.out.println(
                    "java -jar huffman.jar -c <arquivo_original> <arquivo_comprimido>");

            System.out.println(
                    "Para descomprimir:");
            System.out.println(
                    "java -jar huffman.jar -d <arquivo_comprimido> <arquivo_restaurado>");

            return;
        }

        String operacao = args[0];
        String arquivoEntrada = args[1];
        String arquivoSaida = args[2];

        try {

            Huffman huffman = new Huffman();

            if (operacao.equals("-c")) {

                huffman.comprimir(
                        arquivoEntrada,
                        arquivoSaida
                );

            } else if (operacao.equals("-d")) {

                huffman.descomprimir(
                        arquivoEntrada,
                        arquivoSaida
                );

                System.out.println(
                        "Descompressao concluida!");

            } else {

                System.out.println(
                        "Operacao invalida. Use -c ou -d.");
            }

        } catch (Exception e) {

            System.out.println(
                    "Erro: " + e.getMessage());
        }
    }
}