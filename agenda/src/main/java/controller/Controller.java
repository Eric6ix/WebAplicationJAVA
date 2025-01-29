package controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import model.DAO;
import model.JavaBeans;

@WebServlet(urlPatterns = { "/Controller", "/main", "/insert", "/select", "/update", "/delete", "/report" })
public class Controller extends HttpServlet {
	private static final long serialVersionUID = 1L;
	DAO dao = new DAO();
	JavaBeans contato = new JavaBeans();

	public Controller() {
		super();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getServletPath();
		System.err.println(action);
		if (action.equals("/main")) {
			contatos(request, response);
		} else if (action.equals("/insert")) {
			adcionarContato(request, response);
		} else if (action.equals("/select")) {
			listarContato(request, response);
		} else if (action.equals("/update")) {
			editarContato(request, response);
		} else if (action.equals("/delete")) {
			removerContato(request, response);
		} else if (action.equals("/report")) {
			gerarRelatorio(request, response);
		} else {
			response.sendRedirect("index.html");
		}
	}

	// Listar Contatos
	protected void contatos(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Criando um objeto que irá receber os dados JavaBeans
		ArrayList<JavaBeans> lista = dao.listarContatos();
		// Encaminhar a lista ao documento agenda.jsp
		request.setAttribute("contatos", lista);
		RequestDispatcher rd = request.getRequestDispatcher("agenda.jsp");
		rd.forward(request, response);
	}

	// Nnovo Contato
	protected void adcionarContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// teste no console
		System.out.println(request.getParameter("nome"));
		System.out.println(request.getParameter("fone"));
		System.out.println(request.getParameter("email"));
		// setar as variáveis JavaBeans
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));
		// invocar o métudo inseriContato passando o objeto contato
		dao.inserirContato(contato);
		// Redireceior para o documento agenda.jsp
		response.sendRedirect("main");
	}

	// editar contato
	protected void listarContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// recebimento do id do contato que será editato
		// setar variável
		contato.setId(request.getParameter("id"));
		// Executar o método selecionarContato (DAO)
		dao.selecionarConatato(contato);
		// setar os atributos do formulário com o conteúdo JavaBenas
		request.setAttribute("id", contato.getId());
		request.setAttribute("nome", contato.getNome());
		request.setAttribute("fone", contato.getFone());
		request.setAttribute("email", contato.getEmail());
		// encaminhar ao documento edita.jsp
		RequestDispatcher rd = request.getRequestDispatcher("editar.jsp");
		rd.forward(request, response);
	}

	protected void editarContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// setar variável JavaBeans
		contato.setId(request.getParameter("id"));
		contato.setNome(request.getParameter("nome"));
		contato.setFone(request.getParameter("fone"));
		contato.setEmail(request.getParameter("email"));
		// Executar o método selecionarContato (DAO)
		dao.alterarConatato(contato);
		// Redirecionar para o documento agenda.jsp (atualizando as alterações)
		response.sendRedirect("main");
	}

	// remover um contato
	protected void removerContato(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// recebimento do id do contato para ser excluído (validador.js)
		// setar a variável id JavaBeans
		contato.setId(request.getParameter("id"));
		// executar o método deletarContato (DAO)passando o objeto conatao
		dao.deletarConatato(contato);
		// Redirecionar para o documento agenda.jsp (atualizando as alterações)
		response.sendRedirect("main");
	}

	protected void gerarRelatorio(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// geração de pdf
		Document documento = new Document();
		try {
			// tipo de conteúdo
			response.setContentType("apllication/pdf");
			// nome do documento
			response.addHeader("Content-Disposition", "inline; filename=" + "contatos.pdf");
			// criar documeto
			PdfWriter.getInstance(documento, response.getOutputStream());
			// abrir o documento -> conteúdo
			documento.open();
			documento.add(new Paragraph("Lista de Contatos:"));
			documento.add(new Paragraph(" "));
			// criar uma tabela
			PdfPTable tabela = new PdfPTable(3);
			// cabeçalho
			PdfPCell col1 = new PdfPCell(new Paragraph("Nome"));
			PdfPCell col2 = new PdfPCell(new Paragraph("Fone"));
			PdfPCell col3 = new PdfPCell(new Paragraph("E-mail"));
			tabela.addCell(col1);
			tabela.addCell(col2);
			tabela.addCell(col3);
			// popular a tabela com os conataos
			ArrayList<JavaBeans> lista = dao.listarContatos();
			for (int i = 0; i < lista.size(); i++) {
				tabela.addCell(lista.get(i).getNome());
				tabela.addCell(lista.get(i).getFone());
				tabela.addCell(lista.get(i).getEmail());
			}
			documento.add(tabela);
			documento.close();
		} catch (Exception e) {
			System.out.println(e);
			documento.close();
		}
	}
}
