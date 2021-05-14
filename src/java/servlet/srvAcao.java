package servlet;

import apoio.ConexaoBD;
import apoio.Cripto;
import apoio.Validacao;
import dao.PessoaDao;
import entidade.Categoria;
import entidade.Pessoa;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Usuario
 */
public class srvAcao extends HttpServlet {

    ConexaoBD bd = new ConexaoBD();
    ResultSet r = null;
    Categoria categoria = new Categoria();
    Pessoa pep = new Pessoa();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet srvAcao</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet srvAcao at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("ESTOU NO GET");

        String param = request.getParameter("param");

        // -----------------LOGIN-----------------
        if (param.equals("logout")) {
            System.out.println("LOGOUTTTTTT");
            HttpSession sessao = request.getSession();
            sessao.invalidate();
            response.sendRedirect("/WebMarket/login.jsp");

            //PESSOA
        } else if (param.equals("excluirPessoa")) {
            String id = request.getParameter("id");
            pep = new PessoaDao().consultarId(Integer.parseInt(id));

            if (pep != null) {
                PessoaDao exc = new PessoaDao();
                exc.excluir(Integer.parseInt(id));
                encaminharPagina("login.jsp", request, response);
            } else {
                encaminharPagina("erro.jsp", request, response);
            }
        }

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("ESTOU NO POST");

        String param = request.getParameter("param");

        // SALVAR PESSOA  
        if (param.equals("cadastroPessoa")) {
            Pessoa p = new Pessoa();
            int id = Integer.parseInt(request.getParameter("id"));
            String nome = request.getParameter("nome");
            String email = request.getParameter("email");
            String senha = request.getParameter("senha");
            String endereco = request.getParameter("endereco");
            String telefone = request.getParameter("telefone");

            if (!nome.matches("^[A-Za-z ]{5,45}$") || nome.isEmpty()) {
                request.setAttribute("erroCadastro", "erro");
                return;
            } else if (!Validacao.isEmail(email)) {
                request.setAttribute("erroCadastro", "erro");
                return;
            } else if (!senha.matches("^.{8,22}$")) {
                request.setAttribute("erroCadastro", "erro");
                return;
            } else if (!telefone.matches("^((\\+\\d{1,2})?\\d{2})?\\d{9}$")) {
                request.setAttribute("erroCadastro", "erro");
                return;
            } else if (!nome.isEmpty() && !senha.isEmpty() && !email.isEmpty() && !telefone.isEmpty()) {
                p.id = id;
                p.nome = nome;
                p.email = email;
                p.senha = Cripto.criptografar(senha);
                p.endereco = endereco;
                p.telefone = telefone;
            }
            String retorno = null;
            if (id == 0) {
                retorno = new PessoaDao().salvar(p);
                encaminharPagina("/WebMarket/login.jsp", request, response);
            }
        } else if (param.equals("editarPessoa")) {
            PessoaDao pd = new PessoaDao();
            HttpSession sessao = ((HttpServletRequest) request).getSession();

            Pessoa f = (Pessoa) sessao.getAttribute("usuarioLogado");

            f = pd.consultarEmail(f.email);
            int id = Integer.parseInt(request.getParameter("id"));
            String nome = request.getParameter("nome");
            String email = request.getParameter("email");
            String endereco = request.getParameter("endereco");
            String telefone = request.getParameter("telefone");

            if (!nome.matches("^[A-Za-z ]{5,45}$") || nome.isEmpty()) {
                System.out.println(nome);
                request.setAttribute("erroEdit", "erro");
                encaminharPagina("/WebMarket/pessoa/dadosConta.jsp", request, response);
                return;
            } else if (!nome.isEmpty() && !email.isEmpty() && !telefone.isEmpty() && !endereco.isEmpty()) {
                f.id = id;
                f.nome = nome;
                f.email = email;
                f.endereco = endereco;
                f.telefone = telefone;
            }

            if (id != 0) {
                String retorno = pd.atualizar(f);
                encaminharPagina("/WebMarket/pessoa/dadosConta.jsp", request, response);
                System.out.println(retorno);
            }
        } else if (param.equals("mudarSenha")) {
            PessoaDao pd = new PessoaDao();
            HttpSession sessao = ((HttpServletRequest) request).getSession();
            String senha = request.getParameter("senha");
            String senhaNova = request.getParameter("senhaNova");
            String confirmarSenha = request.getParameter("confirmarSenha");

            Pessoa f = (Pessoa) sessao.getAttribute("usuarioLogado");

            f = pd.consultarEmail(f.email);

            if (!senha.isEmpty() && !senhaNova.isEmpty() && !confirmarSenha.isEmpty()) {

                if (Cripto.eIgual(f.senha, senha) && senhaNova.equals(confirmarSenha)) {
                    f.senha = Cripto.criptografar(senhaNova);
                    pd.atualizar(f);
                    encaminharPagina("sucesso.jsp", request, response);
                }
            } else {
                encaminharPagina("erro.jsp", request, response);
            }
        }

        // -------------------LOGIN------------------
        if (param.equals("login")) {
            // ignorando autenticacao = demo
            Pessoa pes = new Pessoa();
            String email = request.getParameter("email");
            String senha = request.getParameter("senha");

            try {
                ResultSet set = bd.getConnection().createStatement()
                        .executeQuery("SELECT * FROM pessoa WHERE email = '" + email + "'");

                if (!set.next()) {
                    request.setAttribute("msgLogin", "erro");
                    encaminharPagina("login.jsp", request, response);
                }

                if (Cripto.eIgual(set.getString("senha"), new String(senha))) {
                    pes.email = email;

                    HttpSession sessao = ((HttpServletRequest) request).getSession();

                    sessao.setAttribute("usuarioLogado", pes);

                    encaminharPagina("index.jsp", request, response);
                   
                    System.out.println("DEU CERTO");
                } else {
                    request.setAttribute("msgLogin", "erro");
                    encaminharPagina("login.jsp", request, response);
                }

            } catch (SQLException ex) {
                Logger.getLogger(srvAcao.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void encaminharPagina(String pagina, HttpServletRequest request, HttpServletResponse response) {
        try {
            RequestDispatcher rd = request.getRequestDispatcher(pagina);
            rd.forward(request, response);
        } catch (Exception e) {
            System.out.println("Erro ao encaminhar: " + e);
        }
    }
}
