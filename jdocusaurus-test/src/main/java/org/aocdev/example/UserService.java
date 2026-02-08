package org.aocdev.example;

public class UserService {

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

    public Object create(Object user) {
        Object saved = userRepository.save(user);
        notificationService.sendWelcomeEmail(user);
        return saved;
    }

    public Object update(Long id, Object user) {
        Object existing = userRepository.findById(id);
        if (existing != null) {
            return userRepository.save(user);
        }
        return null;
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
