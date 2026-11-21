package com.idat.qa.models;

/**
 * Producto tal como se muestra en el catálogo o en el carrito. El precio se guarda en Rs. (rupias, moneda del sitio).
 */
public record Product(String name, int price) {

    /** Convierte textos como "Rs. 1500" a 1500. */
    public static int parsePrice(String priceText) {
        return Integer.parseInt(priceText.replaceAll("[^0-9]", ""));
    }
}
