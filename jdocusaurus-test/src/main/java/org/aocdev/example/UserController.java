package org.aocdev.example;

import org.aocdev.jdocusaurus.annotations.api.*;
import org.aocdev.jdocusaurus.annotations.enums.HttpMethod;
import org.aocdev.jdocusaurus.annotations.enums.ParamLocation;
import org.aocdev.jdocusaurus.annotations.event.JDocProduces;
import org.aocdev.jdocusaurus.annotations.flow.*;
import org.aocdev.jdocusaurus.annotations.enums.ParticipantType;
import org.aocdev.example.event.UserCreatedEvent;

import java.util.List;

@JDocClass(
        name = "User Controller",
        description = "Controlador para la gestion de usuarios del sistema",
        basePath = "/api/v1/users",
        version = "v1",
        group = "Usuarios"
)
@JDocParticipant(name = "UserController", alias = "Controller", type = ParticipantType.SERVICE)
@JDocFlow(name = "user-registration", description = "Flujo completo de registro de un nuevo usuario", title = "Registro de Usuario")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @JDocEndpoint(
            method = HttpMethod.GET,
            path = "/",
            description = "Obtiene la lista completa de usuarios registrados en el sistema",
            summary = "Listar usuarios"
    )
    @JDocResponse(code = 200, description = "Lista de usuarios obtenida correctamente")
    @JDocResponse(code = 500, description = "Error interno del servidor")
    @JDocHeader(name = "Authorization", description = "Token JWT de autenticacion")
    public List<Object> getAllUsers() {
        return (List<Object>) userService.findAll();
    }

    @JDocEndpoint(
            method = HttpMethod.GET,
            path = "/{id}",
            description = "Obtiene los datos de un usuario por su identificador",
            summary = "Obtener usuario"
    )
    @JDocResponse(code = 200, description = "Usuario encontrado")
    @JDocResponse(code = 404, description = "Usuario no encontrado")
    public Object getUserById(
            @JDocParam(
                    name = "id",
                    description = "Identificador unico del usuario",
                    location = ParamLocation.PATH,
                    example = "123"
            ) Long id
    ) {
        return userService.findById(id);
    }

    @JDocEndpoint(
            method = HttpMethod.POST,
            path = "/",
            description = "Crea un nuevo usuario en el sistema",
            summary = "Crear usuario",
            auth = "Bearer JWT"
    )
    @JDocResponse(code = 201, description = "Usuario creado exitosamente")
    @JDocResponse(code = 400, description = "Datos de entrada invalidos")
    @JDocResponse(code = 409, description = "El email ya esta registrado")
    @JDocFlowStep(flow = "user-registration", order = 1, from = "Client", to = "UserController", message = "POST /api/v1/users")
    @JDocFlowStep(flow = "user-registration", order = 2, from = "UserController", to = "UserService", message = "create(user)")
    @JDocFlowStep(flow = "user-registration", order = 3, from = "UserService", to = "UserRepository", message = "save(user)", returnMessage = "savedUser")
    @JDocFlowStep(flow = "user-registration", order = 4, from = "UserService", to = "NotificationService", message = "sendWelcomeEmail(user)")
    @JDocFlowStep(flow = "user-registration", order = 5, from = "UserController", to = "Client", message = "201 Created")
    @JDocProduces(event = UserCreatedEvent.class, topic = "user.created", description = "Emite evento al crear usuario")
    public Object createUser(
            @JDocParam(
                    name = "user",
                    description = "Datos del nuevo usuario",
                    location = ParamLocation.BODY
            ) Object user
    ) {
        return userService.create(user);
    }

    @JDocEndpoint(
            method = HttpMethod.PUT,
            path = "/{id}",
            description = "Actualiza los datos de un usuario existente",
            summary = "Actualizar usuario",
            auth = "Bearer JWT"
    )
    @JDocResponse(code = 200, description = "Usuario actualizado correctamente")
    @JDocResponse(code = 404, description = "Usuario no encontrado")
    @JDocResponse(code = 400, description = "Datos de entrada invalidos")
    public Object updateUser(
            @JDocParam(name = "id", description = "Identificador del usuario", location = ParamLocation.PATH, example = "123") Long id,
            @JDocParam(name = "user", description = "Datos actualizados del usuario", location = ParamLocation.BODY) Object user
    ) {
        return userService.update(id, user);
    }

    @JDocEndpoint(
            method = HttpMethod.DELETE,
            path = "/{id}",
            description = "Elimina un usuario del sistema",
            summary = "Eliminar usuario",
            auth = "Bearer JWT",
            deprecated = true,
            deprecatedMessage = "Usar DELETE /api/v2/users/{id} en su lugar"
    )
    @JDocResponse(code = 204, description = "Usuario eliminado correctamente")
    @JDocResponse(code = 404, description = "Usuario no encontrado")
    public void deleteUser(
            @JDocParam(name = "id", description = "Identificador del usuario a eliminar", location = ParamLocation.PATH) Long id
    ) {
        userService.delete(id);
    }
}
