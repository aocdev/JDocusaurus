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
package org.aocdev.example.shop.model;

import org.aocdev.jdocusaurus.annotations.data.JDocEntity;
import org.aocdev.jdocusaurus.annotations.data.JDocField;
import org.aocdev.jdocusaurus.annotations.data.JDocRelation;
import org.aocdev.jdocusaurus.annotations.enums.RelationType;

import java.util.List;

@JDocEntity(
        name = "Categoria",
        description = "Entidad que representa una categoria del catalogo de productos",
        table = "categories"
)
public class CategoryEntity {

    @JDocField(description = "Identificador unico de la categoria", nullable = false, constraints = "PK, AUTO_INCREMENT")
    private Long id;

    @JDocField(description = "Nombre de la categoria", example = "Electronica", nullable = false)
    private String name;

    @JDocField(description = "Slug URL-friendly de la categoria", example = "electronica", nullable = false, constraints = "UNIQUE")
    private String slug;

    @JDocRelation(target = "Producto", type = RelationType.ONE_TO_MANY, description = "Productos que pertenecen a esta categoria")
    private List<Object> products;
}
