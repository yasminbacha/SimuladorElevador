package visao;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import simuladorelevador.*;
import utilitários.ConectaBanco;

public class Simulacao extends JFrame {

    //************************************************//
    //FUNÇÃO DE INÍCIO
    //************************************************//
    public Simulacao() {
        super("Simulador elevadores");
        iniciarComponentes();
        conecta.conexao();
    }

    public void iniciarComponentes() {

        grid = new JPanel(new GridLayout(10, 3, 2, 2));
        grid.setBackground(Color.BLACK);
        grid.setBorder(new MatteBorder(2, 2, 2, 2, Color.BLACK));
        botoes = new JButton[10];
        labelsEsq = new JLabel[10];
        labelsDir = new JLabel[10];

        iconParado = new ImageIcon(getClass().getResource("/disponivel.jpg"));
        iconAndamento = new ImageIcon(getClass().getResource("/executando.jpg"));
        iconChegou = new ImageIcon(getClass().getResource("/chegou.jpg"));
        AcaoBotao acao = new AcaoBotao();

        for (int i = 0; i < 10; i++) {

            labelsEsq[i] = new JLabel();
            labelsEsq[i].setText("");
            labelsEsq[i].setOpaque(true);
            labelsEsq[i].setHorizontalAlignment(SwingConstants.CENTER);

            grid.add(labelsEsq[i]);

            botoes[i] = new JButton("Chamar " + (10 - i));
            botoes[i].setName("" + i);
            botoes[i].addActionListener(acao);
            grid.add(botoes[i]);

            labelsDir[i] = new JLabel();
            labelsDir[i].setText("");
            labelsDir[i].setOpaque(true);
            labelsDir[i].setHorizontalAlignment(SwingConstants.CENTER);

            grid.add(labelsDir[i]);
        }

        labelsEsq[3].setText(PARADO);
        labelsEsq[3].setIcon(iconParado);

        labelsDir[5].setText(PARADO);
        labelsDir[5].setIcon(iconParado);

        add(grid);

        Object[] opcoes = {"Aleatóriamente", "Manualmente"};
        Object resposta = JOptionPane.showInputDialog(null, "Como deseja fazer a simulação?", "Elevadores", JOptionPane.PLAIN_MESSAGE, null, opcoes, "Aleatóriamente");
        System.out.println("Respostas: " + resposta.toString());
        if (resposta.toString().equals("Aleatóriamente")) {
            Aleatorio();
        }
    }

    private class AcaoBotao implements ActionListener {

        public void actionPerformed(ActionEvent event) {
            JButton botao = (JButton) event.getSource();
            String pisoQueChamou = botao.getName();
            Chamada(Integer.parseInt(pisoQueChamou), false);
        }
    }

    //************************************************//
    //FUNÇÕES QUE MOVIMENTAM O ELEVADOR 
    //************************************************//
    public void trocarPiso(int elevador, int i, String direcao, Icon icone) {
        int j;
        if (elevador == 1) {

            labelsEsq[i].setText(direcao);
            labelsEsq[i].setIcon(icone);

            if (direcao.equals(SUBINDO)) {
                j = i + 1;
            } else {
                j = i - 1;
            }
            if (j != -1 && j != 10) {

                labelsEsq[j].setText("");
                labelsEsq[j].setIcon(null);
            }
        }
        if (elevador == 2) {

            if (direcao.equals(SUBINDO)) {
                j = i + 1;
            } else {
                j = i - 1;
            }
            if (j != -1 && j != 10) {

                labelsDir[j].setText("");
                labelsDir[j].setIcon(null);
            }
            labelsDir[i].setText(direcao);
            labelsDir[i].setIcon(icone);

        }
    }

    public void chegouPisoDestino(int elevador, int i, Icon icone) {

        if (elevador == 1) {
            labelsEsq[i].setText(CHEGOU);
            labelsEsq[i].setIcon(icone);
        }
        if (elevador == 2) {
            labelsDir[i].setText(CHEGOU);
            labelsDir[i].setIcon(icone);
        }
    }

    public void chegouPisoRequerido(int elevador, int i) {

        if (elevador == 1) {
            labelsEsq[i].setText(PARADO);
            labelsEsq[i].setIcon(iconParado);
        }
        if (elevador == 2) {
            labelsDir[i].setText(PARADO);
            labelsDir[i].setIcon(iconParado);
        }
    }

    //************************************************//
    //FUNÇÕES DE CONTROLE DE VARIÁVEIS
    //************************************************//
    public int getPosicaoElevador1() {

        String status1 = "";
        for (int i = 0; i < 10; i++) {

            status1 = labelsEsq[i].getText().toString();

            if (!(status1.equals(""))) {
                return i;
            }

        }
        return -1;

    }

    public String getStatusElevador1() {

        String status1 = "";
        for (int i = 0; i < 10; i++) {

            status1 = labelsEsq[i].getText().toString();

            if (!(status1.equals(""))) {
                return status1;
            }

        }
        return status1;

    }

    public int getPosicaoElevador2() {

        String status2 = "";
        for (int i = 0; i < 10; i++) {

            status2 = labelsDir[i].getText().toString();

            if (!(status2.equals(""))) {
                return i;
            }

        }
        return -1;

    }

    public String getStatusElevador2() {

        String status2 = "";
        for (int i = 0; i < 10; i++) {

            status2 = labelsDir[i].getText().toString();

            if (!(status2.equals(""))) {
                return status2;
            }

        }
        return status2;

    }

    //************************************************//
    //CLASSE DE ELEVADOR QUE GERA O CONTROLE DE ATENDIMENTO
    //SIMULA O PAINEL DE DENTRO DO ELEVADOR
    //************************************************//
    public class ElevadorViewer extends JFrame {

        private JList listaPisos;
        private Elevador elevador;
        private JLabel queElevador;

        public ElevadorViewer(Elevador _elevador) {
            this.elevador = _elevador;
            iniciarComponentesElevador();
        }
        Time tempoInicioAtend = new Time(System.currentTimeMillis());

        public void iniciarComponentesElevador() {
            setSize(new Dimension(300, 300));
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            if (elevador.elevador == 2) {
                setLocation(1000, 200);
            } else {
                setLocation(70, 200);
            }
            setLayout(new FlowLayout());
            String[] data = {"Piso 10", "Piso 9", "Piso 8", "Piso 7", "Piso 6", "Piso 5", "Piso 4", "Piso 3", "Piso 2", "Piso 1"};

            listaPisos = new JList(data);
            queElevador = new JLabel("Painel de controle do elevador: " + elevador.elevador, SwingConstants.CENTER);
            queElevador.setFont(new Font("Serif", Font.BOLD, 18));
            listaPisos.addListSelectionListener(new ListSelectionListener() {

                @Override
                public void valueChanged(ListSelectionEvent arg0) {
                    if (!arg0.getValueIsAdjusting()) {

                        Atendimento(elevador, listaPisos.getSelectedIndex());

                    }
                }
            });
            add(queElevador);
            add(listaPisos);

            setVisible(true);
        }
    }

    //************************************************//
    //FUNÇÕES BASEADAS EM ALGORITMO DE UM ELEVADOR COMUM
    //************************************************//
    public int escolherElevadorMetodoComum(int pisoQueChamou) {
        //Módulo da distancia entre a chamada e os elevadores
        int escolhido = 0;

        int distanciaElevador1 = pisoQueChamou - getPosicaoElevador1();
        if (distanciaElevador1 < 0) {
            distanciaElevador1 *= -1;
        }

        int distanciaElevador2 = pisoQueChamou - getPosicaoElevador2();
        if (distanciaElevador2 < 0) {
            distanciaElevador2 *= -1;
        }

        if (distanciaElevador1 <= distanciaElevador2) {
            if (disponibilidadeElevador1(pisoQueChamou)) {
                escolhido = 1;
            } else if (disponibilidadeElevador2(pisoQueChamou)) {
                escolhido = 2;
            } else {
                System.out.println("MANDAR ESPERAR");
            }
        } else {
            if (disponibilidadeElevador2(pisoQueChamou)) {
                escolhido = 2;
            } else if (disponibilidadeElevador1(pisoQueChamou)) {
                escolhido = 1;
            } else {
                System.out.println("MANDAR ESPERAR");
            }
        }

        return escolhido;

        //Se o elevador está subindo e a requisição é de piso de baixo / escolhe o outro se o outro estiver parado senão espera- ignora
        //Se o elevador está descendo e a requisição é de piso de cima - ignora
        //Se um elevador está subindo e outro parado e a chamada é pra cima - só considera só a que esta na menor distancia
        //Se um elevador está descendo e outro parado e a chamada é pra baixo - só considera só a que esta na menor distancia
    }

    public boolean disponibilidadeElevador1(int pisoQueChamou) {
        boolean disponivel = false;

        if (getStatusElevador1().equals(PARADO)) {
            disponivel = true;
        } else if (getStatusElevador1().equals(SUBINDO)) {
            if (pisoQueChamou <= getPosicaoElevador1()) {
                disponivel = true;
            } else {
                disponivel = false;
                System.out.println("Verificar 2 ou esperar");
            }
        } else if (getStatusElevador1().equals(DESCENDO)) {
            if (pisoQueChamou >= getPosicaoElevador1()) {
                disponivel = true;
            } else {
                disponivel = false;
                System.out.println("Verificar 2 ou esperar");
            }
        } else if (getStatusElevador1().equals(CHEGOU)) {
            disponivel = false;
            System.out.println("Verificar 2 ou esperar");
        }
        return disponivel;
    }

    public boolean disponibilidadeElevador2(int pisoQueChamou) {
        boolean disponivel = false;

        if (getStatusElevador2().equals(PARADO)) {
            disponivel = true;
        } else if (getStatusElevador2().equals(SUBINDO)) {
            if (pisoQueChamou <= getPosicaoElevador2()) {
                disponivel = true;
            } else {
                disponivel = false;
                System.out.println("Verificar 2 ou esperar");
            }
        } else if (getStatusElevador2().equals(DESCENDO)) {
            if (pisoQueChamou >= getPosicaoElevador2()) {
                disponivel = true;
            } else {
                disponivel = false;
                System.out.println("Verificar 2 ou esperar");
            }
        } else if (getStatusElevador2().equals(CHEGOU)) {
            disponivel = false;
            System.out.println("Verificar 2 ou esperar");
        }
        return disponivel;
    }

    //************************************************//
    //FUNÇÃO ALEATÓRIA
    //************************************************//
    public void Aleatorio() {

        new Thread(new Runnable() {

            public void run() {
                Random gerador = new Random();
                for (int i = 0; i < QUANTALEATORIOS; i++) {
                    Chamada(gerador.nextInt(9), true);
                    Random gerador1 = new Random();
                    try {
                        Thread.sleep(gerador1.nextInt(31) * 1000);
                    } catch (InterruptedException ex) {
                        System.out.println("Erro Thread. Erro: " + ex);
                    }
                }
            }
        }).start();

    }

    //************************************************//
    //FUNÇÕES DE CONTROLE
    //************************************************//
    public void Chamada(int pisoQueChamou, final boolean aleatorio) {
        int posElevador1 = getPosicaoElevador1();
        int posElevador2 = getPosicaoElevador2();

        System.out.println("Chamada do piso: " + pisoQueChamou);
        System.out.println("Onde o elevador 1 está: " + posElevador1);
        System.out.println("Onde o elevador 2 está: " + posElevador2);

        elevadorEscolhido = escolherElevadorMetodoComum(pisoQueChamou);
        int pisoCorrente;
        if (elevadorEscolhido == 1) {
            pisoCorrente = posElevador1;
        } else {
            pisoCorrente = posElevador2;
        }

        final Elevador elevador = new Elevador(elevadorEscolhido, pisoCorrente, pisoQueChamou);

        new Thread(new Runnable() {

            public void run() {
                try {
                    Time tempoInicio = new Time(System.currentTimeMillis());

                    if (elevador.direcao.equals(SUBIR)) {
                        for (int i = elevador.pisoCorrente.getPiso(); i >= elevador.pisoDestino.getPiso(); i--) {

                            trocarPiso(elevador.elevador, i, SUBINDO, iconAndamento);
                            //Esperar 3 segundos - (Veja arquivo "Padrões de funcionamento do sistema.pdf")
                            if (i != elevador.pisoDestino.getPiso()) {
                                Thread.sleep(TEMPOPORANDAR);
                            }

                        }
                    } else if (elevador.direcao.equals(DESCER)) {
                        for (int i = elevador.pisoCorrente.getPiso(); i <= elevador.pisoDestino.getPiso(); i++) {

                            trocarPiso(elevador.elevador, i, DESCENDO, iconAndamento);
                            //Esperar 3 segundos - (Veja arquivo "Padrões de funcionamento do sistema.pdf")
                            if (i != elevador.pisoDestino.getPiso()) {
                                Thread.sleep(TEMPOPORANDAR);
                            }
                        }
                    } else {
                        System.out.println(PERMANECER);
                    }
                    chegouPisoDestino(elevador.elevador, elevador.pisoDestino.getPiso(), iconChegou);
                    Thread.sleep(TEMPOPORANDAR);

                    salvarChamada(PARADO, CHEGOU, tempoInicio, elevador.pisoCorrente.getPiso(), elevador.pisoDestino.getPiso(), elevador.elevador);
                    if (aleatorio == true) {
                        Random gerador2 = new Random();
                        Atendimento(elevador, gerador2.nextInt(9));
                    } else {
                        painelElevador = new ElevadorViewer(elevador);
                    }
                } catch (InterruptedException ex) {
                    System.out.println("Erro no Sleep da Thread do elevador - Erro: " + ex);
                }
            }
        }).start();

    }

    public void Atendimento(Elevador _elevador, int pisoDestino) {
        final Time tempoInicioAtend = new Time(System.currentTimeMillis());

        final Elevador elevador = new Elevador(_elevador.elevador, _elevador.pisoDestino.getPiso(), pisoDestino);

        new Thread(new Runnable() {

            public void run() {
                try {
                    if (painelElevador != null) {
                        painelElevador.setVisible(false);
                    }
                    if (elevador.direcao.equals(Simulacao.SUBIR)) {
                        for (int i = elevador.pisoCorrente.getPiso(); i >= elevador.pisoDestino.getPiso(); i--) {

                            trocarPiso(elevador.elevador, i, Simulacao.SUBINDO, iconAndamento);
                            //Esperar 3 segundos - (Veja arquivo "Padrões de funcionamento do sistema.pdf")
                            if (i != elevador.pisoDestino.getPiso()) {
                                Thread.sleep(Simulacao.TEMPOPORANDAR);
                            }

                        }
                    } else if (elevador.direcao.equals(Simulacao.DESCER)) {
                        for (int i = elevador.pisoCorrente.getPiso(); i <= elevador.pisoDestino.getPiso(); i++) {

                            trocarPiso(elevador.elevador, i, Simulacao.DESCENDO, iconAndamento);
                            //Esperar 3 segundos - (Veja arquivo "Padrões de funcionamento do sistema.pdf")
                            if (i != elevador.pisoDestino.getPiso()) {
                                Thread.sleep(Simulacao.TEMPOPORANDAR);
                            }
                        }
                    } else {
                        System.out.println(Simulacao.PERMANECER);
                    }
                    chegouPisoRequerido(elevador.elevador, elevador.pisoDestino.getPiso());
                    Thread.sleep(Simulacao.TEMPOPORANDAR);

                    if (Thread.interrupted()) {
                        System.out.print("thread terminou");
                    }
                    salvarAtendimento(CHEGOU, PARADO, tempoInicioAtend, elevador.pisoCorrente.getPiso(), elevador.pisoDestino.getPiso(), elevador.elevador);
                } catch (InterruptedException ex) {
                    System.out.println("Erro no Sleep da Thread do elevador - Erro: " + ex);
                }
            }
        }).start();

    }

    //************************************************//
    //FUNÇÕES DE BANCO
    //************************************************//
    public void salvarChamada(String estadoInicial, String estadoFinal, Time tempoInicio, int pisoInicial, int pisoFinal, int elevador) {

        try {
            PreparedStatement pst = conecta.conn.prepareStatement(""
                    + "insert into chamadas (data, hora_inicio, hora_final, estado_inicial, estado_final, piso_inicial, piso_final, elevador) "
                    + "values (?,?,?,?,?,?,?, ?)");

            Date data = new Date(System.currentTimeMillis());

            Time tempoFinalChamada = new Time(System.currentTimeMillis());

            pst.setDate(1, data);
            pst.setTime(2, tempoInicio);
            pst.setTime(3, tempoFinalChamada);
            pst.setString(4, estadoInicial);
            pst.setString(5, estadoFinal);
            pst.setInt(6, pisoInicial);
            pst.setInt(7, pisoFinal);
            pst.setInt(8, elevador);

            pst.executeUpdate();
            System.out.println("Inserção relaizada com sucesso!");
        } catch (SQLException ex) {
            System.out.println("Inserção relaizada com sucesso!\n ERRO: " + ex);
        }
    }

    public void salvarAtendimento(String estadoInicial, String estadoFinal, Time tempoInicio, int pisoInicial, int pisoFinal, int elevador) {

        try {
            PreparedStatement pst = conecta.conn.prepareStatement(""
                    + "insert into atendimentos (id_chamadas, data, hora_inicio_atendimento, "
                    + "hora_final_atendimento, estado_inicial_atendimento, estado_final_atendimento, "
                    + "piso_inicial_atendimento, piso_final_atendimento, elevador) "
                    + "values (?,?,?,?,?,?,?,?,?)");

            Date data = new Date(System.currentTimeMillis());
            Time tempoFinal = new Time(System.currentTimeMillis());
            pst.setInt(1, BuscarIdChamada(pisoInicial, elevador));
            pst.setDate(2, data);
            pst.setTime(3, tempoInicio);
            pst.setTime(4, tempoFinal);
            pst.setString(5, estadoInicial);
            pst.setString(6, estadoFinal);
            pst.setInt(7, pisoInicial);
            pst.setInt(8, pisoFinal);
            pst.setInt(9, elevador);

            pst.executeUpdate();
            System.out.println("Inserção relaizada com sucesso!");
        } catch (SQLException ex) {
            System.out.println("Inserção relaizada com sucesso!\n ERRO: " + ex);
        }
    }

    public int BuscarIdChamada(int pisoFinal, int elevador) {
        int id = -1;
        try {
            conecta.executaSQL("select id from chamadas as c where c.piso_final = " + pisoFinal + " and elevador = " + elevador);
            conecta.rs.last();
            id = conecta.rs.getInt("id");
        } catch (SQLException ex) {
            System.out.println("ERRO NO SELECT: " + ex);
        }
        return id;
    }

    //************************************************//
    //FUNÇÃO PRINCIPAL
    //************************************************//
    public static void main(String[] args) {
        Simulacao frame = new Simulacao();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    //************************************************//
    //DECLARAÇÃO DE VARIÁVEIS GLOBAIS
    //************************************************//
    public JPanel grid;
    public JButton[] botoes;
    public JLabel[] labelsEsq;
    public JLabel[] labelsDir;

    public Icon iconParado;
    public Icon iconAndamento;
    public Icon iconChegou;

    public static String PARADO = "Parado";
    public static String SUBINDO = "Subindo";
    public static String DESCENDO = "Descendo";
    public static String CHEGOU = "Chegou";
    public static String SUBIR = "Subir";
    public static String PERMANECER = "Permanecer";
    public static String DESCER = "Descer";
    public static int QUANTALEATORIOS = 3;
    int elevadorEscolhido;

    public static int TEMPOPORANDAR = 3 * 1000;
    ElevadorViewer painelElevador;
    ConectaBanco conecta = new ConectaBanco();

}
