file:///C:/Users/Alejandro/Desktop/Semestre%207/5.%20DESARROLLO_DE_SOFTWARE_III/Desarrollo_III/Desarrollo3/sistema-gestion-ordenes/backend/usuarios-service/src/test/java/com/tienda/usuarios/service/UserServiceTest.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 3055
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
import com.tienda.usuarios.repository.RoleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class RoleRepositoryTest {
    @Mock
    private RoleRepository roleRepository;
    private UserService userService;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    
        // 🔥 Crear y simular un rol "Admin"
        Role adminRole = new Role();
        adminRole.setId(1);
        adminRole.setNombre("Admin");
    
        // Simular que `findByNombre("Admin")` devuelve el rol
        when(roleRepository.findByNombre(anyString())).thenAnswer(invocation -> {
            String nombre = invocation.getArgument(0);
            if ("Admin".equalsIgnoreCase(nombre.trim())) {
                return Optional.of(adminRole);
            }
            return Optional.empty();
        });
    
        // Simular `findAll()` con una lista de roles
        when(roleRepository.findAll()).thenReturn(List.of(adminRole));
    
        System.out.println("📌 Setup: Roles simulados en la BD -> " + roleRepository.findAll());
    }
    @Test
    void testObtenerRolesDeLaBaseDeDatos() {
        // Act: Obtener todos los roles
        List<Role> roles = roleRepository.findAll();

        // Mostrar los roles en la consola
        System.out.println("🔍 Roles en la base de datos: " + roles);

        // Assert: Asegurar que no está vacío
        assertFalse(roles.isEmpty(), "La lista de roles no debería estar vacía");

        // Opcional: Verificar si existe "Admin"
        boolean existeAdmin = roles.stream().anyMatch(role -> "Admin".equalsIgnoreCase(role.getNombre()));
        assertTrue(existeAdmin, "El rol 'Admin' debería existir en la base de datos");
    }
    @Test
    void testBuscarRolPorNombre() {
      Optional<Role> adminRole = roleRepository.findByNombre("Admin");

      System.out.println("🔍 Resultado de buscar 'Admin': " + adminRole);

      assertTrue(adminRole.isPresent(), "El rol 'Admin' debería existir en la base de datos");
  }
    @Test
    void testRegistrarUsuario() {
      User user = new User();
      user.setNombre("John Doe");
      user.setEmail("JohnDoe.@@"
        

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
	dotty.tools.pc.completions.CompletionProvider.completions(CompletionProvider.scala:72)
	dotty.tools.pc.ScalaPresentationCompiler.complete$$anonfun$1(ScalaPresentationCompiler.scala:150)
```
#### Short summary: 

java.util.NoSuchElementException: next on empty iterator