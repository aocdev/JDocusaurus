package org.aocdev.example.customer;

import org.aocdev.example.customer.service.CustomerService;
import org.aocdev.jdocusaurus.annotations.api.*;
import org.aocdev.jdocusaurus.annotations.enums.HttpMethod;
import org.aocdev.jdocusaurus.annotations.enums.ParamLocation;
import org.aocdev.jdocusaurus.annotations.event.JDocProduces;
import org.aocdev.jdocusaurus.annotations.flow.*;
import org.aocdev.jdocusaurus.annotations.enums.ParticipantType;
import org.aocdev.example.customer.event.CustomerCreatedEvent;

import java.util.List;

@JDocClass(
        name = "Customer Controller",
        description = "Controlador para la gestion de clientes del sistema",
        basePath = "/api/v1/customers",
        version = "v1",
        group = "Clientes"
)
@JDocParticipant(name = "CustomerController", alias = "Controller", type = ParticipantType.SERVICE)
@JDocFlow(name = "customer-registration", description = "Flujo completo de registro de un nuevo cliente", title = "Registro de Cliente")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @JDocEndpoint(
            method = HttpMethod.GET,
            path = "/",
            description = "Obtiene la lista completa de clientes registrados en el sistema",
            summary = "Listar clientes"
    )
    @JDocResponse(code = 200, description = "Lista de clientes obtenida correctamente")
    @JDocResponse(code = 500, description = "Error interno del servidor")
    @JDocHeader(name = "Authorization", description = "Token JWT de autenticacion")
    public List<Object> getAllCustomers() {
        return (List<Object>) customerService.findAll();
    }

    @JDocEndpoint(
            method = HttpMethod.GET,
            path = "/{id}",
            description = "Obtiene los datos de un cliente por su identificador",
            summary = "Obtener cliente"
    )
    @JDocResponse(code = 200, description = "Cliente encontrado")
    @JDocResponse(code = 404, description = "Cliente no encontrado")
    public Object getCustomerById(
            @JDocParam(
                    name = "id",
                    description = "Identificador unico del cliente",
                    location = ParamLocation.PATH,
                    example = "123"
            ) Long id
    ) {
        return customerService.findById(id);
    }

    @JDocEndpoint(
            method = HttpMethod.POST,
            path = "/",
            description = "Crea un nuevo cliente en el sistema",
            summary = "Crear cliente",
            auth = "Bearer JWT"
    )
    @JDocResponse(code = 201, description = "Cliente creado exitosamente")
    @JDocResponse(code = 400, description = "Datos de entrada invalidos")
    @JDocResponse(code = 409, description = "El email ya esta registrado")
    @JDocFlowStep(flow = "customer-registration", order = 1, from = "Client", to = "CustomerController", message = "POST /api/v1/customers")
    @JDocFlowStep(flow = "customer-registration", order = 2, from = "CustomerController", to = "CustomerService", message = "create(customer)")
    @JDocFlowStep(flow = "customer-registration", order = 3, from = "CustomerService", to = "CustomerRepository", message = "save(customer)", returnMessage = "savedCustomer")
    @JDocFlowStep(flow = "customer-registration", order = 4, from = "CustomerService", to = "NotificationService", message = "sendWelcomeEmail(customer)")
    @JDocFlowStep(flow = "customer-registration", order = 5, from = "CustomerController", to = "Client", message = "201 Created")
    @JDocProduces(event = CustomerCreatedEvent.class, topic = "customer.created", description = "Emite evento al crear cliente")
    public Object createCustomer(
            @JDocParam(
                    name = "customer",
                    description = "Datos del nuevo cliente",
                    location = ParamLocation.BODY
            ) Object customer
    ) {
        return customerService.create(customer);
    }

    @JDocEndpoint(
            method = HttpMethod.PUT,
            path = "/{id}",
            description = "Actualiza los datos de un cliente existente",
            summary = "Actualizar cliente",
            auth = "Bearer JWT"
    )
    @JDocResponse(code = 200, description = "Cliente actualizado correctamente")
    @JDocResponse(code = 404, description = "Cliente no encontrado")
    @JDocResponse(code = 400, description = "Datos de entrada invalidos")
    public Object updateCustomer(
            @JDocParam(name = "id", description = "Identificador del cliente", location = ParamLocation.PATH, example = "123") Long id,
            @JDocParam(name = "customer", description = "Datos actualizados del cliente", location = ParamLocation.BODY) Object customer
    ) {
        return customerService.update(id, customer);
    }

    @JDocEndpoint(
            method = HttpMethod.DELETE,
            path = "/{id}",
            description = "Elimina un cliente del sistema",
            summary = "Eliminar cliente",
            auth = "Bearer JWT",
            deprecated = true,
            deprecatedMessage = "Usar DELETE /api/v2/customers/{id} en su lugar"
    )
    @JDocResponse(code = 204, description = "Cliente eliminado correctamente")
    @JDocResponse(code = 404, description = "Cliente no encontrado")
    public void deleteCustomer(
            @JDocParam(name = "id", description = "Identificador del cliente a eliminar", location = ParamLocation.PATH) Long id
    ) {
        customerService.delete(id);
    }
}
