file:///C:/Users/Alejandro/Desktop/Semestre%207/5.%20DESARROLLO_DE_SOFTWARE_III/Desarrollo_III/Desarrollo3/sistema-gestion-ordenes/backend/usuarios-service/src/test/java/com/tienda/usuarios/service/UserServiceTest.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 3015
uri: file:///C:/Users/Alejandro/Desktop/Semestre%207/5.%20DESARROLLO_DE_SOFTWARE_III/Desarrollo_III/Desarrollo3/sistema-gestion-ordenes/backend/usuarios-service/src/test/java/com/tienda/usuarios/service/UserServiceTest.java
text:
```scala
package com.tienda.usuarios.service;

import com.tienda.usuarios.dto.UserRequestDTO;
import com.tienda.usuarios.dto.UserResponseDTO;
import com.tienda.usuarios.model.Role;
import com.tienda.usuarios.model.User;
import com.tienda.usuarios.repository.RoleRepository;
import com.tienda.usuarios.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;  // ¡Aquí se agrega la anotación @Mock!


    @Mock
    private BCryptPasswordEncoder passwordEncoder;  // Aseguramos que se inicializa correctamente

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        Role adminRole = new Role();
        adminRole.setId(1);
        adminRole.setNombre("Admin");

        // Configurar el mock para devolver el rol cuando se solicite
        when(roleRepository.findByNombre(anyString())).thenAnswer(invocation -> {
            String requestedRole = invocation.getArgument(0);
            if ("Admin".equals(requestedRole)) {
                return Optional.of(adminRole);
            }
            return Optional.empty();
        });

        when(roleRepository.findAll()).thenReturn(Arrays.asList(adminRole));
    }
    @Test
    void testRegistrarUsuario_Exitoso() {
        // Arrange
        UserRequestDTO request = new UserRequestDTO();
        request.setNombre("Juan");
        request.setEmail("juan@example.com");
        request.setPassword("password123");

        // Asegurar que el rol en el request sea "Admin"
        Role role = new Role();
        role.setNombre("Admin");
        request.setRol(role);

        User user = new User();
        user.setId(1L);
        user.setNombre(request.getNombre());
        user.setEmail(request.getEmail());
        user.setPassword("encodedPassword");
        user.setRol(role);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Agregar impresión para ver qué devuelve r@@oleRepository.findByNombre()
        System.out.println("Buscando rol en base de datos: " + request.getRol().getNombre());
        List<Role> allRoles = roleRepository.findAll();
        System.out.println("Roles en la base de datos: " + allRoles);
        
        Optional<Role> foundRole = roleRepository.findByNombre(request.getRol().getNombre());

        

        System.out.println("Resultado de la búsqueda: " + foundRole);

        // Act
        UserResponseDTO response = userService.registrarUsuario(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Juan", response.getNombre());
        assertEquals("juan@example.com", response.getEmail());
        assertEquals("Admin", response.getRol());

        verify(userRepository, times(1)).save(any(User.class));
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