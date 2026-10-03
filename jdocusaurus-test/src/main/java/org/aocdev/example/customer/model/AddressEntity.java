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
package org.aocdev.example.customer.model;

import org.aocdev.jdocusaurus.annotations.data.JDocEntity;
import org.aocdev.jdocusaurus.annotations.data.JDocField;
import org.aocdev.jdocusaurus.annotations.data.JDocRelation;
import org.aocdev.jdocusaurus.annotations.enums.RelationType;

@JDocEntity(
        name = "Direccion",
        description = "Direccion postal asociada a un cliente",
        table = "addresses"
)
public class AddressEntity {

    @JDocField(description = "Identificador unico", nullable = false, constraints = "PK, AUTO_INCREMENT")
    private Long id;

    @JDocField(description = "Calle y numero", example = "Calle Mayor 10", nullable = false)
    private String street;

    @JDocField(description = "Ciudad", example = "Madrid", nullable = false)
    private String city;

    @JDocField(description = "Codigo postal", example = "28001")
    private String zipCode;

    @JDocField(description = "Pais", example = "Espana", nullable = false)
    private String country;

    @JDocRelation(target = "Cliente", type = RelationType.MANY_TO_ONE, description = "Cliente propietario de la direccion")
    private Object customer;
}
