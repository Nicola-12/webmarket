<%-- 
    Document   : login
    Created on : 29 de mar. de 2021, 19:26:01
    Author     : Usuario
--%>


<%@page import="java.util.ArrayList"%>
<%@page import="entidade.Pessoa"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="Mark Otto, Jacob Thornton, and Bootstrap contributors">
        <meta name="generator" content="Hugo 0.80.0">
        <title>Login</title>

        <link rel="canonical" href="https://getbootstrap.com/docs/5.0/examples/sign-in/">



        <!-- Bootstrap core CSS -->
        <link href="/WebMarket/css/bootstrap.min.css" rel="stylesheet">
        <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
        <!-- Favicons -->
        <link rel="apple-touch-icon" href="/docs/5.0/assets/img/favicons/apple-touch-icon.png" sizes="180x180">
        <link rel="icon" href="/docs/5.0/assets/img/favicons/favicon-32x32.png" sizes="32x32" type="image/png">
        <link rel="icon" href="/docs/5.0/assets/img/favicons/favicon-16x16.png" sizes="16x16" type="image/png">
        <link rel="manifest" href="/docs/5.0/assets/img/favicons/manifest.json">
        <link rel="mask-icon" href="/docs/5.0/assets/img/favicons/safari-pinned-tab.svg" color="#7952b3">
        <link rel="icon" href="/docs/5.0/assets/img/favicons/favicon.ico">
        <meta name="theme-color" content="#7952b3">


        <style>
            .bd-placeholder-img {
                font-size: 1.125rem;
                text-anchor: middle;
                -webkit-user-select: none;
                -moz-user-select: none;
                user-select: none;
            }

            @media (min-width: 768px) {
                .bd-placeholder-img-lg {
                    font-size: 3.5rem;
                }
            }
            #msgErroLogin {
                color: #0c4128;
            }

            a {
                width: 100%;
                color: black;
                text-decoration: none;
                font-size: 20px;
                margin-top: 5px;
            }

            a:hover {
                color: blue;
            }

            input:invalid:not(:placeholder-shown){
                box-shadow: 0px 0px 0px 0.25rem rgba(255,0,0,.25);
                border-color: red;
                z-index:2;
            }


        </style>
        <!-- Custom styles for this template -->
        <link href="/WebMarket/css/signin.css" rel="stylesheet">
        <script src="https://unpkg.com/sweetalert/dist/sweetalert.min.js"></script>
    </head>
    <body class="text-center">

        <main class="form-signin">
            <form method="post" class="formLog" action="/WebMarket/acao?param=login" >            

                <h1 class="h3 mb-3 fw-normal">Autenticação</h1>

                <h2>Web Market</h2>
                <br>
                <label for="inputEmail" class="visually-hidden">Email address</label>
                <input type="email" name="email" pattern="^[_A-Za-z0-9-\+]+(\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\.[A-Za-z0-9]+)*(\.[A-Za-z]{2,})$" 
                       id="inputEmail" class="form-control" placeholder="E-mail" required autofocus>

                <label for="inputPassword" class="visually-hidden">Password</label>
                <input type="password" name="senha" id="inputPassword" pattern="^.{8,22}$" title="De 8 a 22 caracteres" class="form-control" placeholder="Senha" required>
                <br>

                <script>

                    document.addEventListener('readystatechange', () => {
                        if (document.readyState !== 'complete')
                            return;

                        const params = new URL(location.href).searchParams

                        if (params.get('erro') === 'ERRO') {
                            swal({
                                title: "Ooops!",
                                text: "Usuário ou senha não conferem!",
                                icon: "warning",
                                button: "Tenta de novo!"
                            });
                        } else if (params.get('certo') === 'TRUE') {
                            swal({
                                title: "Correto!",
                                text: "Usuário e senha conferem!",
                                icon: "success",
                                buttons: false,
                                timer: 1500
                            }).then(() => {
                                location = '/WebMarket/index.jsp'
                            });
                        }

                    })


                </script>

                <button class="w-100 btn btn-lg btn-dark" type="submit" value="logar" >Acessar</button>            

                <div class="text-center">
                    <a href="pessoa/cadastroLogin.jsp" >Cadastrar-se</a>
                </div>

            </form>
        </main>


    </body>

</html>

