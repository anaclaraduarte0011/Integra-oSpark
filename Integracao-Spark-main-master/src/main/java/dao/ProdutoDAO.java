package dao;

import java.sql.*;

import model.Produto;

public class ProdutoDAO {

	private Connection conexao;

	public ProdutoDAO() {
		conexao = null;
	}

	public boolean conectar() {
		String driverName = "org.postgresql.Driver";
		String serverName = "localhost";
		String mydatabase = "Produtos";
		int porta = 5432;
		String url = "jdbc:postgresql://" + serverName + ":" + porta + "/" + mydatabase;
		String username = "postgres";
		String password = "23078899";
		boolean status = false;

		try {
			Class.forName(driverName);
			conexao = DriverManager.getConnection(url, username, password);
			status = (conexao == null);
			System.out.println("Conexão efetuada com o postgres!");
		} catch (ClassNotFoundException e) {
			System.err.println("Conexão NÃO efetuada com o postgres -- Driver não encontrado -- " + e.getMessage());
		} catch (SQLException e) {
			System.err.println("Conexão NÃO efetuada com o postgres -- " + e.getMessage());
		}

		return status;
	}

	public boolean close() {
		boolean status = false;

		try {
			conexao.close();
			status = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return status;
	}

	public Produto[] getAll() {
		Produto[] produtos = null;

		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM produtos");

			if (rs.next()) {
				rs.last();
				produtos = new Produto[rs.getRow()];
				rs.beforeFirst();

				for (int i = 0; rs.next(); i++) {
					produtos[i] = new Produto(rs.getInt("id"), rs.getString("descricao"), rs.getFloat("preco"),
							rs.getInt("quantidade"), rs.getString("fabricacao"), rs.getString("validade"));
				}
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		
		return produtos;
	}

	public boolean add(Produto produto) {
		boolean status = false;
		try {
			Statement st = conexao.createStatement();
			st.executeUpdate("INSERT INTO produtos (id, descricao, preco, quantidade, fabricacao, validade) "
					+ "VALUES (" + produto.getId() + ", '" + produto.getDescricao() + "', '" + produto.getPreco()
					+ "', '" + produto.getQuant() + ", '" + produto.getDataFabricacao() + "', '"
					+ produto.getDataValidade() + "');");
			st.close();
			 
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		return status;
	}

	public int getMaxId() {
		int[] ids = null;
		int maxId = 0;

		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM produtos");

			if (rs.next()) {
				rs.last();
				ids = new int[rs.getRow()];
				rs.beforeFirst();

				for (int i = 0; rs.next(); i++) {
					ids[i] = rs.getInt("id");

					if (ids[i] > maxId) {
						maxId = ids[i];
					}
				}
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}

		return maxId;
	}

	public Produto get(int id) {
		Produto[] produto = new Produto[getMaxId()];

		try {
			Statement st = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			ResultSet rs = st.executeQuery("SELECT * FROM produtos");

			if (rs.next()) {
				rs.last();
				rs.beforeFirst();

				for (int i = 0; rs.next(); i++) {
					produto[i] = new Produto(rs.getInt("id"), rs.getString("descricao"), rs.getFloat("preco"),
							rs.getInt("quantidade"), rs.getString("fabricacao"), rs.getString("validade"));

					if (id == produto[i].getId()) {
						return produto[i];
					}
				}
			}
			st.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return null;
	}

	public boolean remove(int id) {
		boolean status = false;
		try {
			Statement st = conexao.createStatement();
			st.executeUpdate("DELETE FROM produtos WHERE id = " + id);
			st.close();
			status = true;
		} catch (SQLException u) {
			throw new RuntimeException(u);
		}
		return status;
	}
}