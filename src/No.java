public class No implements Comparable<No> {

    char caractere;
    int frequencia;
    No esquerda;
    No direita;

    public No(int frequencia, char caractere) {
        this.frequencia = frequencia;
        this.caractere = caractere;
        this.esquerda = null;
        this.direita = null;
    }

    public No(int frequencia, No esquerda, No direita) {
        this.frequencia = frequencia;
        this.esquerda = esquerda;
        this.direita = direita;
        this.caractere = '\0';
    }

    @Override
    public int compareTo(No outroNo) {
        return this.frequencia - outroNo.frequencia;
    }

    public boolean ehFolha() {
        return esquerda == null && direita == null;
    }
}