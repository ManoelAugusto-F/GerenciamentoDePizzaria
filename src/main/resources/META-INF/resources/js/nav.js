let openHide = false;
let cartIcon = '<i class="fa-solid fa-cart-shopping"></i>';



function renderNavbar() {
    const navbar = `
  <nav class="navbar"> 
    <div class="container">
      <a href="index.html" class="logo">Pizzaria</a>
      <div class="nav-links" id="mainMenu">
        <a href="index.html" class="navigation-links">Início</a>
        <a href="cardapio.html" class="navigation-links">Cardápio</a>
        ${auth.isAdmin() ? '<a href="admin.html" class="admin-only navigation-links">Administração</a>' : ''} 
        ${auth.isAuthenticated() ? '<a href="cliente.html" class=" client-only navigation-links">Meus Pedidos</a>' : ''}
        ${auth.isAttendant() ? '<a href="atendente.html" class=" attendant-only navigation-links">Painel Atendente</a>' : ''}
        ${auth.isAuthenticated() ? `<a href="perfilUser.html" onclick="app.navigateTo('profile')" class=" admin-only navigation-links">Meu Perfil</a>` : ''}
        
        ${auth.isAuthenticated()
        ? '<a href="#" id="logoutBtn" class="sair-btn">Sair</a>'
        : '<a href="login.html" id="loginLink" class="sair-btn">Login</a>'}

        <a href="admin.html" class="admin-only" style="display:none">Administração</a>
        <a href="atendente.html" class="attendant-only" style="display:none">Painel Atendente</a>
        <a href="cliente.html" class="client-only" style="display:none">Meus Pedidos</a>
       ${auth.isAuthenticated() ?  `<button class="btnCart" onclick="openCart()">${cartIcon}</button>`:''}
      </div>
    </div>
  </nav>
  `;
    document.getElementById('navbar-render').innerHTML = navbar;
    auth.setupEventListeners(); // chama de novo após re-renderizar
}

function openCart() {
    openHide = !openHide;
    cartIcon = openHide
        ? '<i class="fa-solid fa-circle-xmark"></i>'
        : '<i class="fa-solid fa-cart-shopping"></i>';

    // Atualiza o ícone no botão
    document.querySelector('.btnCart').innerHTML = cartIcon;

    // Mostra ou esconde o carrinho
    const cartContainer = document.querySelector('.cart-container');
    if (cartContainer) {
        cartContainer.style.display = openHide ? 'block' : 'none';
    }
}

// verifica a pagina e ativa o link pra trocar de cor

function ativarLinkAtual() {
    const links = document.querySelectorAll('.navigation-links');
    const currentPath = window.location.pathname.split("/").pop() || 'index.html';

    links.forEach(link => {
        const linkHref = link.getAttribute('href').split("/").pop();
        if (linkHref === currentPath) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });
}

// Inicializa tudo
renderNavbar();
ativarLinkAtual()
