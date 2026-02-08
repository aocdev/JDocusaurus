package org.aocdev.example.model;

import org.aocdev.jdocusaurus.annotations.data.JDocEntity;
import org.aocdev.jdocusaurus.annotations.data.JDocField;
import org.aocdev.jdocusaurus.annotations.data.JDocRelation;
import org.aocdev.jdocusaurus.annotations.enums.RelationType;

@JDocEntity(
        name = "Direccion",
        description = "Direccion postal asociada a un usuario",
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

    @JDocRelation(target = "Usuario", type = RelationType.MANY_TO_ONE, description = "Usuario propietario de la direccion")
    private Object user;
}
