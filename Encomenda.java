// package AtividadeFinal;

import java.util.GregorianCalendar;

public class Encomenda {
    private int nroPedido;
    private GregorianCalendar postagem;
    private float peso;
    private float valorfrete;
    
    public int getNroPedido() {
        return nroPedido;
    }
    public void setNroPedido(int nroPedido) {
        this.nroPedido = nroPedido;
    }
    public GregorianCalendar getPostagem() {
        return postagem;
    }
    public void setPostagem(GregorianCalendar postagem) {
        this.postagem = postagem;
    }
    public float getPeso() {
        return peso;
    }
    public void setPeso(float peso) {
        this.peso = peso;
    }
    public float getValorfrete() {
        return valorfrete;
    }
    public void setValorfrete(float valorfrete) {
        this.valorfrete = valorfrete;
    }
    

}
