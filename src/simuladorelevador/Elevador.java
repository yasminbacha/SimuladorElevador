package simuladorelevador;

import visao.Simulacao;

public class Elevador {

    public int elevador;

    public String direcao;

    public Piso pisoCorrente;

    public Piso pisoDestino;

    public Elevador(int _elevador, int _pisoCorrente, int _pisoDestino) {
        this.elevador = _elevador;
        this.pisoCorrente = new Piso(_pisoCorrente);
        this.pisoDestino = new Piso(_pisoDestino);

        encontrarDirecao();

    }

    public final void encontrarDirecao() {
        if (pisoCorrente.getPiso() > pisoDestino.getPiso()) {
            direcao = Simulacao.SUBIR;
        } else if (pisoCorrente.getPiso() < pisoDestino.getPiso()) {
            direcao = Simulacao.DESCER;
        } else {
            direcao = Simulacao.PERMANECER;
        }
    }

    public String getDirecao() {
        return null;
    }

}
