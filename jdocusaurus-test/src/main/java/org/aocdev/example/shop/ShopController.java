package org.aocdev.example.shop;

import org.aocdev.example.shop.service.CatalogService;
import org.aocdev.jdocusaurus.annotations.api.*;
import org.aocdev.jdocusaurus.annotations.enums.HttpMethod;
import org.aocdev.jdocusaurus.annotations.enums.ParamLocation;

import java.util.List;

@JDocClass(
        name = "Shop Controller",
        description = "Controlador para la navegacion del catalogo de productos",
        basePath = "/api/v1/shop",
        version = "v1",
        group = "Tienda"
)
public class ShopController {

    private final CatalogService catalogService;

    public ShopController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @JDocEndpoint(
            method = HttpMethod.GET,
            path = "/products",
            description = "Obtiene la lista de productos disponibles en el catalogo",
            summary = "Listar productos"
    )
    @JDocResponse(code = 200, description = "Lista de productos obtenida correctamente")
    public List<Object> getProducts(
            @JDocParam(name = "page", description = "Numero de pagina", location = ParamLocation.QUERY, example = "0") Integer page,
            @JDocParam(name = "size", description = "Tamano de pagina", location = ParamLocation.QUERY, example = "20") Integer size
    ) {
        return (List<Object>) catalogService.findAllProducts();
    }

    @JDocEndpoint(
            method = HttpMethod.GET,
            path = "/products/category/{slug}",
            description = "Obtiene los productos filtrados por categoria",
            summary = "Productos por categoria"
    )
    @JDocResponse(code = 200, description = "Productos de la categoria obtenidos correctamente")
    @JDocResponse(code = 404, description = "Categoria no encontrada")
    public List<Object> getProductsByCategory(
            @JDocParam(
                    name = "slug",
                    description = "Slug de la categoria",
                    location = ParamLocation.PATH,
                    example = "electronica"
            ) String slug
    ) {
        return (List<Object>) catalogService.findByCategory(slug);
    }

    @JDocEndpoint(
            method = HttpMethod.GET,
            path = "/products/featured",
            description = "Obtiene los productos destacados del momento",
            summary = "Productos destacados"
    )
    @JDocResponse(code = 200, description = "Productos destacados obtenidos correctamente")
    public List<Object> getFeaturedProducts() {
        return (List<Object>) catalogService.findFeatured();
    }
}
