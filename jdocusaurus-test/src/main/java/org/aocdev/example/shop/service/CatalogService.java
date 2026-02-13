package org.aocdev.example.shop.service;

import org.aocdev.jdocusaurus.annotations.config.JDocConfig;

@JDocConfig(key = "shop.featured-count", description = "Numero de productos destacados a mostrar", defaultValue = "10", example = "20")
@JDocConfig(key = "shop.cache-ttl-seconds", description = "Tiempo de vida de cache del catalogo en segundos", defaultValue = "300", example = "600")
public class CatalogService {

    public Object findAllProducts() {
        return null;
    }

    public Object findByCategory(String slug) {
        return null;
    }

    public Object findFeatured() {
        return null;
    }
}
