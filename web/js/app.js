function mapCategory(category) {
    return `
    <li class="nav-item">
        <span class="nav-link" data-category="${category.name}">${category.name}</span>
    </li>
    `;
}

function mapProduct(product) {
    return `
    <div class="card border-light mb-3 col-sm-4">
        <div class="card-header">${product.name}</div>
        <div class="card-body">
            <h6 class="card-title">${product.description}</h6>
            <p class="card-text">Price: ${product.price}$</p>
            <span class="btn btn-link add-to-cart" data-name="${product.name}" data-id="${product.id}">Add to card</a>
        </div>
    </div>
    `;
}

function getProductsForCategory(category) {
    $.get(`ProductsServlet?category=${category}`, {}, function (productList) {
        const $products = $("#products");
        $products.empty();
        $products.append(productList.map(mapProduct));

        registerAddToCartListener();
    });
}

function registerAddToCartListener() {
    $("#products .add-to-cart").each(function () {
        $(this).click(function (event) {
            event.stopPropagation();
            const productName = $(this).data("name");
            const productId = $(this).data("id");
            addProductToCart(productName, productId);
        });
    });
}

function addProductToCart(productName, productId) {
    $.post('ProductsServlet', {productId: productId}, function () {
        const logMessage = `Product ${productName} (with id: ${productId}) was added to card!`;
        alert(logMessage);
    })
}

function registerCategoryClickListener() {
    $('#category-menu li span').each(function () {
        $(this).click(function (event) {
            event.stopPropagation();
            const category = $(this).data("category");
            getProductsForCategory(category);
        });
    });
}

$(document).ready(function () {
    $.get('MainServlet', {}, function (responseText) {
        const categoryList = responseText.map(mapCategory);
        $("#category-menu").append(categoryList);
        registerCategoryClickListener();
    });
});