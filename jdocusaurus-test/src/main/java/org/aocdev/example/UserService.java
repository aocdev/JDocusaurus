package org.aocdev.example;

import org.aocdev.jdocusaurus.annotations.config.JDocConfig;
import org.aocdev.jdocusaurus.annotations.enums.RuleSeverity;
import org.aocdev.jdocusaurus.annotations.enums.ServiceType;
import org.aocdev.jdocusaurus.annotations.integration.JDocExternalService;
import org.aocdev.jdocusaurus.annotations.rule.JDocBusinessRule;

@JDocConfig(key = "user.max-active", description = "Numero maximo de usuarios activos permitidos", defaultValue = "10000", example = "5000")
@JDocConfig(key = "user.password-min-length", description = "Longitud minima de contrasena", defaultValue = "8", required = true, example = "12")
@JDocConfig(key = "user.jwt-secret", description = "Clave secreta para firmar tokens JWT", required = true, secret = true)
public class UserService {

    @JDocExternalService(name = "User Database", description = "Base de datos principal de usuarios", type = ServiceType.REST, url = "jdbc:postgresql://localhost:5432/users", owner = "Team Backend")
    private final UserRepository userRepository;

    private final NotificationService notificationService;

    public UserService(UserRepository userRepository, NotificationService notificationService) {
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    public Object findAll() {
        return userRepository.findAll();
    }

    public Object findById(Long id) {
        return userRepository.findById(id);
    }

    @JDocBusinessRule(id = "BR-001", rule = "El email del usuario debe ser unico en el sistema", severity = RuleSeverity.MANDATORY)
    @JDocBusinessRule(id = "BR-002", rule = "La contrasena debe tener al menos 8 caracteres, una mayuscula y un numero", severity = RuleSeverity.MANDATORY, relatedRules = {"BR-001"})
    public Object create(Object user) {
        Object saved = userRepository.save(user);
        notificationService.sendWelcomeEmail(user);
        return saved;
    }

    @JDocBusinessRule(id = "BR-003", rule = "Solo el propio usuario o un admin puede actualizar los datos", severity = RuleSeverity.MANDATORY, relatedRules = {"BR-001"})
    public Object update(Long id, Object user) {
        Object existing = userRepository.findById(id);
        if (existing != null) {
            return userRepository.save(user);
        }
        return null;
    }

    @JDocBusinessRule(id = "BR-004", rule = "El borrado es logico, no se elimina fisicamente el registro", severity = RuleSeverity.WARNING)
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
