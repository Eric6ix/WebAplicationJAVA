package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DAO {
	/* Módlo de conexção */
	// Parâmetros de conexão
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/dbagenda?useTimezone=true&serverTimezone=UTC";
	private String user = "root";
	private String password = "root";

	// Método de Conexão
	private Connection conectar() {
		Connection con = null;
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			return (con);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	/** CURD CREATE **/
	public void inserirContato(JavaBeans contato) {
		String create = "insert into contatos (nome,fone,email) values (?,?,?)";
		try {
			// abrir conexão
			Connection con = conectar();
			// preparar a query execução no banco de dados
			PreparedStatement pst = con.prepareStatement(create);
			// Substituir os parametros (?) pelo conteúdo das variáveis JavaBeans
			pst.setString(1, contato.getNome());
			pst.setString(2, contato.getFone());
			pst.setString(3, contato.getEmail());
			// Executar a Query
			pst.executeUpdate();
			// Encerrar a conexão co mo banco
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// ** CRUD READ **//
	public ArrayList<JavaBeans> listarContatos() {
		// Criando um objeto para acessar a classe JavaBeans
		ArrayList<JavaBeans> contatos = new ArrayList<>();
		String read = "select * from contatos order by nome";
		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(read);
			ResultSet rs = pst.executeQuery();
			// o laço abaixo será executato enquanto houver contatos
			while (rs.next()) {
				// variáveis de apoio que recebem os dados do banco
				String id = rs.getString(1);
				String nome = rs.getString(2);
				String fone = rs.getString(3);
				String email = rs.getString(4);
				// populando ArrayList
				contatos.add(new JavaBeans(id, nome, fone, email));
			}
			con.close();
			return contatos;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}

	// CRUD UPDATE
	public void selecionarConatato(JavaBeans contato) {
		String read2 = "select * from contatos where id = ?";
		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(read2);
			pst.setString(1, contato.getId());
			ResultSet rs = pst.executeQuery();
			while (rs.next()) {
				// setar as variáveis JavaBenas
				contato.setId(rs.getString(1));
				contato.setNome(rs.getString(2));
				contato.setFone(rs.getString(3));
				contato.setEmail(rs.getString(4));
			}
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// editar o contato
	public void alterarConatato(JavaBeans contato) {
		String update = "update contatos set nome=?, fone=?, email=? where id = ?";
		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(update);
			pst.setString(1, contato.getNome());
			pst.setString(2, contato.getFone());
			pst.setString(3, contato.getEmail());
			pst.setString(4, contato.getId());
			pst.executeUpdate();
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	// CRUD delete
	public void deletarConatato(JavaBeans contato) {
		String delete = "delete from contatos where id = ?";
		try {
			Connection con = conectar();
			PreparedStatement pst = con.prepareStatement(delete);
			pst.setString(1, contato.getId());
			pst.executeUpdate();
			con.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
