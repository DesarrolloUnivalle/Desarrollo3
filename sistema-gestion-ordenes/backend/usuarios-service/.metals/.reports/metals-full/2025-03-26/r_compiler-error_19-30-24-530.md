file:///C:/Users/Alejandro/Desktop/Semestre%207/5.%20DESARROLLO_DE_SOFTWARE_III/Desarrollo_III/Desarrollo3/sistema-gestion-ordenes/backend/usuarios-service/src/test/java/com/tienda/usuarios/service/UserServiceTest.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 1051
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
    private RoleRepository roleRepository;@@
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
        // Arrange: Crear usuario de prueba
        User usuario = new User();
        Role rol = new Role();
        rol.setId(1);
        rol.setNombre("Admin");
        usuario.setId(1);
        usuario.setNombre("Alejandro");
        usuario.setEmail("alejandro@example.com");
        usuario.setPassword("123456");
        usuario.setRol(rol);

        // Act: Registrar usuario
        User usuarioGuardado = userService.registrarUsuario(usuario);

        // Assert: Verificar que se guardó correctamente
        assertNotNull(usuarioGuardado, "El usuario guardado no debería ser null");
        assertEquals("Alejandro", usuarioGuardado.getNombre(), "El nombre del usuario no coincide");
        assertEquals("Admin", usuarioGuardado.getRol().getNombre(), "El rol del usuario no es 'Admin'");
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