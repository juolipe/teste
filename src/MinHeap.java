import java.util.ArrayList;

public class MinHeap {

    private ArrayList<No> heap;

    public MinHeap() {
        heap = new ArrayList<>();
    }

    public int tamanho() {
        return heap.size();
    }

    public boolean vazio() {
        return heap.isEmpty();
    }

    public void inserir(No novoNo) {
        heap.add(novoNo);

        int indice = heap.size() - 1;

        while (indice > 0) {
            int pai = (indice - 1) / 2;

            if (heap.get(indice).compareTo(heap.get(pai)) >= 0) {
                break;
            }

            trocar(indice, pai);
            indice = pai;
        }
    }

    public No removerMin() {
        if (heap.isEmpty()) {
            return null;
        }

        No menor = heap.get(0);
        No ultimo = heap.remove(heap.size() - 1);

        if (!heap.isEmpty()) {
            heap.set(0, ultimo);

            int indice = 0;

            while (true) {
                int esquerda = 2 * indice + 1;
                int direita = 2 * indice + 2;
                int menorIndice = indice;

                if (esquerda < heap.size()
                        && heap.get(esquerda).compareTo(heap.get(menorIndice)) < 0) {
                    menorIndice = esquerda;
                }

                if (direita < heap.size()
                        && heap.get(direita).compareTo(heap.get(menorIndice)) < 0) {
                    menorIndice = direita;
                }

                if (menorIndice == indice) {
                    break;
                }

                trocar(indice, menorIndice);
                indice = menorIndice;
            }
        }

        return menor;
    }

    private void trocar(int i, int j) {
        No temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    @Override
    public String toString() {
        return heap.toString();
    }
}