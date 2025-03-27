file:///C:/Users/Alejandro/Desktop/Semestre%207/5.%20DESARROLLO_DE_SOFTWARE_III/Desarrollo_III/Desarrollo3/sistema-gestion-ordenes/backend/usuarios-service/src/test/java/com/tienda/usuarios/service/UserServiceTest.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 3431
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

import jakarta.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@Transactional  // ❗ Esto asegura que los datos creados en la prueba se revierten después de ejecutarla
class RoleRepositoryTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;
    @Test
    void testRegistrarUsuario_Exitoso() {
        // 🔹 1️⃣ Preparar los datos
        UserRequestDTO request = new UserRequestDTO();
        request.setNombre("Carlos Perez");
        request.setEmail("carlos.perez@example.com");
        request.setPassword("securePassword123");

        // 🔹 2️⃣ Buscar un rol existente en la BD (por ejemplo, "Cliente")
        Optional<Role> optionalRole = roleRepository.findByNombre("Cliente");
        assertTrue(optionalRole.isPresent(), "El rol 'Cliente' debería existir en la base de datos");
        request.setRol(optionalRole.get());

        // 🔹 3️⃣ Ejecutar el método de registro
        UserResponseDTO response = userService.registrarUsuario(request);

        // 🔹 4️⃣ Validaciones
        assertNotNull(response);
        assertNotNull(response.getId(), "El usuario registrado debe tener un ID");
        assertEquals("Carlos Perez", response.getNombre());
        assertEquals("carlos.perez@example.com", response.getEmail());
        assertEquals("Cliente", response.getRol()); // 🔥 Asegurar que se guardó el rol correcto

        // 🔹 5️⃣ Verificar que el usuario realmente está en la base de datos
        Optional<User> usuarioGuardado = userRepository.findByEmail("carlos.perez@example.com");
        assertTrue(usuarioGuardado.isPresent(), "El usuario debería haberse guardado en la base de datos");
        assertEquals("Carlos Perez", usuarioGuardado.get().getNombre());
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

      System.out.println("🔍 Resultado de@@ buscar 'Admin': " + adminRole);

      assertTrue(adminRole.isPresent(), "El rol 'Admin' debería existir en la base de datos");
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