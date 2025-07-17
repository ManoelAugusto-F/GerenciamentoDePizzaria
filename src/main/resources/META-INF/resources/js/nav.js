function renderNavbar() {
    const navbar = `
  <nav class="navbar"> 
    <div class="container">
      <a href="index.html" class="logo">Pizzaria</a>
      <div class="nav-links" id="mainMenu">
        <a href="index.html" class="active">Início</a>
        <a href="cardapio.html">Cardápio</a>
       ${auth.isAdmin() ? '<a href="admin.html" className="active admin-only">Administração</a>' : ''} 
       '<a href="cliente.html" className="active client-only">Meus Pedidos</a>'
        ${auth.isAttendant() ? '<a href="atendente.html" className="active attendant-only">Painel Atendente</a>' : ''}
        
${auth.isAuthenticated()
        ? '<a href="#" id="logoutBtn" >Sair</a>'
        : '<a href="login.html" id="loginLink">Login</a>'
    }
        <a href="admin.html" class="admin-only" style="display:none">Administração</a>
        <a href="atendente.html" class="attendant-only" style="display:none">Painel Atendente</a>
        <a href="cliente.html" class="client-only" style="display:none">Meus Pedidos</a>
        <span id="userInfo" style="margin-left: 1rem;"></span>
      </div>
    </div>
  </nav>
  `;
    document.getElementById('navbar-render').innerHTML = navbar;

}

renderNavbar();
auth.setupEventListeners();
