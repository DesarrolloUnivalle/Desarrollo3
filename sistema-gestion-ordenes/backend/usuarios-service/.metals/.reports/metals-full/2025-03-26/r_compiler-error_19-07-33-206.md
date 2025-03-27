file:///C:/Users/Alejandro/Desktop/Semestre%207/5.%20DESARROLLO_DE_SOFTWARE_III/Desarrollo_III/Desarrollo3/sistema-gestion-ordenes/backend/usuarios-service/src/main/java/com/tienda/usuarios/service/UserService.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 2719
uri: file:///C:/Users/Alejandro/Desktop/Semestre%207/5.%20DESARROLLO_DE_SOFTWARE_III/Desarrollo_III/Desarrollo3/sistema-gestion-ordenes/backend/usuarios-service/src/main/java/com/tienda/usuarios/service/UserService.java
text:
```scala
package com.tienda.usuarios.service;


import com.tienda.usuarios.dto.UserRequestDTO;
import com.tienda.usuarios.dto.UserResponseDTO;
import com.tienda.usuarios.model.Role;
import com.tienda.usuarios.model.User;
import com.tienda.usuarios.repository.RoleRepository;
import com.tienda.usuarios.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository; // Agregado
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        // Buscar el rol en la BD (por defecto "Cliente" si no envían otro)
        Role defaultRole = roleRepository.findByNombreIgnoreCase(user.getRol().getNombre())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Asignar el rol al usuario
        user.setRol(defaultRole);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Cifrar la contraseña

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setNombre(user.getNombre());
        response.setEmail(user.getEmail());
        response.setRol(user.getRol().getNombre());
        
        return response;
    }
    
    public UserResponseDTO registrarUsuario(UserRequestDTO request) {
        Optional<User> usuarioExistente = userRepository.findByEmail(request.getEmail());
        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }
        System.out.println("🔍 Buscando rol en la BD con nombre: [" + request.getRol().getNombre() + "]");
        List<Role> roles = roleRepository.findAll();
        System.out.println("🔍 Roles en la base de datos: " + roles);
        
        // Buscar el rol en la base de datos
        Role role = roleRepository.findByNom@@breIgnoreCase(request.getRol().getNombre())
                .orElseThrow(() -> new IllegalArgumentException("🚨 El rol no existe en la base de datos: " + request.getRol().getNombre()));

        
        System.out.println("✅ Resultado de la búsqueda: " + role);
    
        // Crear usuario
        User user = new User();
        user.setNombre(request.getNombre());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encriptar contraseña
        user.setRol(role);
    
        // Guardar en la base de datos
        user = userRepository.save(user);
    
        // ✅ Crear respuesta asegurándonos de que el rol se maneja como String
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setNombre(user.getNombre());
        response.setEmail(user.getEmail());
        response.setRol(user.getRol().getNombre()); // 🔥 Extraer solo el nombre del rol
    
        return response;
    }
    
}

```



#### Error stacktrace:

```
scala.collection.Iterator$$anon$19.next(Iterator.scala:973)
	scala.collection.Iterator$$anon$19.next(Iterator.scala:971)
	scala.collection.mutable.MutationTracker$CheckedIterator.next(MutationTracker.scala:76)
	scala.collection.IterableOps.head(Iterable.scala:222)
	scala.collection.IterableOps.head$(Iterable.scala:222)
	scala.collection.AbstractIterable.head(Iterable.scala:935)
	dotty.tools.dotc.interactive.InteractiveDriver.run(InteractiveDriver.scala:164)
	dotty.tools.pc.CachingDriver.run(CachingDriver.scala:45)
	dotty.tools.pc.HoverProvider$.hover(HoverProvider.scala:40)
	dotty.tools.pc.ScalaPresentationCompiler.hover$$anonfun$1(ScalaPresentationCompiler.scala:389)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator