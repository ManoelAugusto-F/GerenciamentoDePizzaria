function renderCartContainer() {
      document.getElementById('cart-render').innerHTML = `
 <div class="cart-container" >
            <h3>Seu Carrinho</h3>
            <ul id="cart-items" class="cart-items"></ul>
            <div id="cart-total" class="cart-total"></div>
            <button onclick="checkout()" class="btn btn-primary btn-block">Finalizar Pedido</button>
        </div>
    `;
}
renderCartContainer()