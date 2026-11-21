package com.idat.qa.tests;

import com.idat.qa.components.AddedToCartModal;
import com.idat.qa.models.CartItem;
import com.idat.qa.models.Product;
import com.idat.qa.pages.CartPage;
import com.idat.qa.pages.ProductDetailPage;
import com.idat.qa.pages.ProductsPage;
import com.idat.qa.utils.ExpectedMessages;
import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;

public class CartTest extends BaseTest {

    @Test(description = "Agregar varios productos desde el catálogo y validar el carrito")
    public void shouldAddMultipleProductsToCart() {
        ProductsPage productsPage = new ProductsPage(driver()).open();
        Product first = productsPage.productInfo(1);
        Product second = productsPage.productInfo(3);

        step("Agregar '" + first.name() + "' y '" + second.name() + "'");
        AddedToCartModal modal = productsPage.addToCart(1);
        assertEquals(modal.message(), ExpectedMessages.PRODUCT_ADDED_TO_CART);
        modal.continueShopping();
        CartPage cartPage = productsPage.addToCart(3).viewCart();

        assertEquals(cartPage.items(), List.of(
                new CartItem(first.name(), first.price(), 1, first.price()),
                new CartItem(second.name(), second.price(), 1, second.price())));
    }

    @Test(description = "Agregar un producto con cantidad personalizada desde su detalle")
    public void shouldAddProductWithCustomQuantity() {
        int quantity = 4;
        ProductDetailPage detailPage = new ProductsPage(driver()).open().openProductDetail(2);
        Product product = detailPage.productInfo();

        step("Agregar " + quantity + " unidades de '" + product.name() + "'");
        CartPage cartPage = detailPage.setQuantity(quantity).addToCart().viewCart();

        assertEquals(cartPage.items(), List.of(
                new CartItem(product.name(), product.price(), quantity, product.price() * quantity)),
                "La cantidad o el total del producto en el carrito es incorrecto");
    }
}
