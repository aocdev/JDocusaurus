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
package org.aocdev.example.customer.service;

import org.aocdev.example.customer.CustomerRepository;
import org.aocdev.example.shared.notification.NotificationService;
import org.aocdev.jdocusaurus.annotations.config.JDocConfig;
import org.aocdev.jdocusaurus.annotations.enums.RuleSeverity;
import org.aocdev.jdocusaurus.annotations.enums.ServiceType;
import org.aocdev.jdocusaurus.annotations.integration.JDocExternalService;
import org.aocdev.jdocusaurus.annotations.rule.JDocBusinessRule;

@JDocConfig(key = "customer.max-active", description = "Numero maximo de clientes activos permitidos", defaultValue = "10000", example = "5000")
@JDocConfig(key = "customer.password-min-length", description = "Longitud minima de contrasena", defaultValue = "8", required = true, example = "12")
@JDocConfig(key = "customer.jwt-secret", description = "Clave secreta para firmar tokens JWT", required = true, secret = true)
public class CustomerService {

    @JDocExternalService(name = "Customer Database", description = "Base de datos principal de clientes", type = ServiceType.REST, url = "jdbc:postgresql://localhost:5432/customers", owner = "Team Backend")
    private final CustomerRepository customerRepository;

    private final NotificationService notificationService;

    public CustomerService(CustomerRepository customerRepository, NotificationService notificationService) {
        this.customerRepository = customerRepository;
        this.notificationService = notificationService;
    }

    public Object findAll() {
        return customerRepository.findAll();
    }

    public Object findById(Long id) {
        return customerRepository.findById(id);
    }

    @JDocBusinessRule(id = "BR-001", rule = "El email del cliente debe ser unico en el sistema", severity = RuleSeverity.MANDATORY)
    @JDocBusinessRule(id = "BR-002", rule = "La contrasena debe tener al menos 8 caracteres, una mayuscula y un numero", severity = RuleSeverity.MANDATORY, relatedRules = {"BR-001"})
    public Object create(Object customer) {
        Object saved = customerRepository.save(customer);
        notificationService.sendWelcomeEmail(customer);
        return saved;
    }

    @JDocBusinessRule(id = "BR-003", rule = "Solo el propio cliente o un admin puede actualizar los datos", severity = RuleSeverity.MANDATORY, relatedRules = {"BR-001"})
    public Object update(Long id, Object customer) {
        Object existing = customerRepository.findById(id);
        if (existing != null) {
            return customerRepository.save(customer);
        }
        return null;
    }

    @JDocBusinessRule(id = "BR-004", rule = "El borrado es logico, no se elimina fisicamente el registro", severity = RuleSeverity.WARNING)
    public void delete(Long id) {
        customerRepository.deleteById(id);
    }
}
