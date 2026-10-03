/*
 * Copyright 2026 aocdev (Albert Ortells)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
