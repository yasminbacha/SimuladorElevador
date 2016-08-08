package utilitários;

import java.sql.*;
import javax.swing.JOptionPane;

public class ConectaBanco {

    public Statement stm;//responsável por preparar e realizar pesquisas no BD
    public ResultSet rs;//responsável por armazenar o resultado de uma pesquisa passada para o Statement
    private String driver = "org.postgresql.Driver"; //responsável por identificar o serviço de BD
    private String caminho = "jdbc:postgresql://localhost:5432/ProjetoElevador";//responsável por setar o local do BD 
    private String usuario = "postgres";
    private String senha = "3434";
    public Connection conn;//responsável por realizar a conexão com o BD

    public void conexao() {//realiza a conexão com o BD
        try {
            System.setProperty("jdbc.Drivers", driver);//seta a propriedade do driver de conexão
            conn = DriverManager.getConnection(caminho, usuario, senha);//realiza a conexão com o BD
            System.out.println("Conectado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão!\n Erro: " + ex.getMessage());
        }
    }

    public void desconeta() {//fecha a conexão com o BD
        try {
            conn.close();
        } catch (SQLException ex) {
            System.out.println("Erro ao fechar a conexão!\n Erro: " + ex.getMessage());
        }

    }

    public void executaSQL(String sql) {
        try {
            stm = conn.createStatement(rs.TYPE_SCROLL_INSENSITIVE, rs.CONCUR_READ_ONLY);
            rs = stm.executeQuery(sql);
        } catch (SQLException ex) {
            System.out.println("Erro no executa SQL. ERRO: " + ex);
        }

    }

}
