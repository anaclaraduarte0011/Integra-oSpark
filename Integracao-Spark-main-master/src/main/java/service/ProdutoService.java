package service;

import dao.ProdutoDAO;
import model.Produto;
import spark.Request;
import spark.Response;

public class ProdutoService {

	private ProdutoDAO produtoDAO;

	public ProdutoService() {
		ProdutoDAO produtoDAO = new ProdutoDAO();
		produtoDAO.conectar();
	}

	public Object add(Request request, Response response) {
		String descricao = request.queryParams("descricao");
		float preco = Float.parseFloat(request.queryParams("preco"));
		int quantidade = Integer.parseInt(request.queryParams("quantidade"));
		String dataFabricacao = request.queryParams("dataFabricacao").toString();
		String dataValidade = request.queryParams("dataValidade").toString();

		int id = produtoDAO.getMaxId() + 1;

		Produto produto = new Produto(id, descricao, preco, quantidade, dataFabricacao, dataValidade);

		produtoDAO.add(produto);

		response.status(201);
		return id;
	}

	public Object get(Request request, Response response) {
		int id = Integer.parseInt(request.params(":id"));
		
		Produto produto = (Produto) produtoDAO.get(id);
		
		if (produto != null) {
    	    response.header("Content-Type", "application/xml");
    	    response.header("Content-Encoding", "UTF-8");

            return "<produto>\n" + 
            		"\t<id>" + produto.getId() + "</id>\n" +
            		"\t<descricao>" + produto.getDescricao() + "</descricao>\n" +
            		"\t<preco>" + produto.getPreco() + "</preco>\n" +
            		"\t<quantidade>" + produto.getQuant() + "</quantidade>\n" +
            		"\t<fabricacao>" + produto.getDataFabricacao() + "</fabricacao>\n" +
            		"\t<validade>" + produto.getDataValidade() + "</validade>\n" +
            		"</produto>\n";
        } else {
            response.status(404);
            return "Produto " + id + " não encontrado.";
        }

	}

	public Object remove(Request request, Response response) {
        int id = Integer.parseInt(request.params(":id"));
        
        if (produtoDAO.remove(id) != false) {
            response.status(200);
        	return id;
        } else {
            response.status(404);
            return "Produto não encontrado.";
        }
	}

	public Object getAll(Request request, Response response) {
		StringBuffer returnValue = new StringBuffer("<produtos type=\"array\">");		
		
		for (Produto produto : produtoDAO.getAll()) {
			returnValue.append("\n<produto>\n" + 
            		"\t<id>" + produto.getId() + "</id>\n" +
            		"\t<descricao>" + produto.getDescricao() + "</descricao>\n" +
            		"\t<preco>" + produto.getPreco() + "</preco>\n" +
            		"\t<quantidade>" + produto.getQuant() + "</quantidade>\n" +
            		"\t<fabricacao>" + produto.getDataFabricacao() + "</fabricacao>\n" +
            		"\t<validade>" + produto.getDataValidade() + "</validade>\n" +
            		"</produto>\n");
		}
		returnValue.append("</produtos>");
	    response.header("Content-Type", "application/xml");
	    response.header("Content-Encoding", "UTF-8");
		return returnValue.toString();
	}
}