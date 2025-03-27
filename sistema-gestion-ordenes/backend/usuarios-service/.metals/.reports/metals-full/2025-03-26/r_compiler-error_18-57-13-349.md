file:///C:/Users/Alejandro/Desktop/Semestre%207/5.%20DESARROLLO_DE_SOFTWARE_III/Desarrollo_III/Desarrollo3/sistema-gestion-ordenes/backend/usuarios-service/src/test/java/com/tienda/usuarios/service/UserServiceTest.java
### java.util.NoSuchElementException: next on empty iterator

occurred in the presentation compiler.

presentation compiler configuration:


action parameters:
offset: 4458
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
    
        // 🔥 Crear un rol simulado "Admin"
        Role adminRole = new Role();
        adminRole.setId(1);
        adminRole.setNombre("Admin");
    
        // 🔥 Simular que `roleRepository.findByNombre("Admin")` devuelve el rol
        when(roleRepository.[INFO] -------------------------------------------------------------
        [ERROR] COMPILATION ERROR :
        [INFO] -------------------------------------------------------------
        [ERROR] /C:/Users/Alejandro/Desktop/Semestre 7/5. DESARROLLO_DE_SOFTWARE_III/Desarrollo_III/Desarrollo3/sistema-gestion-ordenes/backend/usuarios-service/src/test/java/com/tienda/usuarios/service/UserServiceTest.java:[54,28] cannot find symbol
          symbol:   method findByNombre(java.lang.String)
          location: variable roleRepository of type com.tienda.usuarios.repository.RoleRepository
        [ERROR] /C:/Users/Alejandro/Desktop/Semestre 7/5. DESARROLLO_DE_SOFTWARE_III/Desarrollo_III/Desarrollo3/sistema-gestion-ordenes/backend/usuarios-service/src/test/java/com/tienda/usuarios/service/UserServiceTest.java:[112,41] cannot find symbol
          symbol:   method findByNombre(java.lang.String)
          location: interface com.tienda.usuarios.repository.RoleRepository
        [INFO] 2 errors
        [INFO] -------------------------------------------------------------
        [INFO] ------------------------------------------------------------------------
        [INFO] BUILD FAILURE
        [INFO] ------------------------------------------------------------------------
        [INFO] Total time:  8.683 s
        [INFO] Finished at: 2025-03-26T18:56:24-05:00
        [INFO] ------------------------------------------------------------------------
        [ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.10.1:testCompile (default-testCompile) on project
         usuarios-service: Compilation failure: Compilation failure:
        [ERROR] /C:/Users/Alejandro/Desktop/Semestre 7/5. DESARROLLO_DE_SOFTWARE_III/Desarrollo_III/Desarrollo3/sistema-gestion-ordenes/backend/usuarios-service/src/test/java/com/tienda/usuarios/service/UserServiceTest.java:[54,28] cannot find symbol
        [ERROR]   symbol:   method findByNombre(java.lang.String)
        [ERROR]   location: variable roleRepository of type com.tienda.usuarios.repository.RoleRepository
        [ERROR] /C:/Users/Alejandro/Desktop/Semestre 7/5. DESARROLLO_DE_SOFTWARE_III/Desarrollo_III/Desarrollo3/sistema-gestion-ordenes/backend/usuarios-service/src/test/java/com/tienda/usuarios/service/UserServiceTest.java:[112,41] cannot find symbol
        [ERROR]   symbol:   method findByNombre(java.lang.String)
        [ERROR]   location: interface com.tienda.usuarios.repository.RoleRepository
        [ERROR] -> [Help 1]
        [ERROR]
        [ERROR] To see the full stack trace of the errors, re-run Maven with the -e switch.
        [ERROR] Re-run Maven using the -X switch to@@ enable full debug logging.
        [ERROR]
        [ERROR] For more information about the errors and possible solutions, please read the following articles:
        [ERROR] [Help 1] http://cwiki.apache.org/confluence/display/MAVEN/MojoFailureException
        PS <HOME>\Desktop\Semestre 7\5. DESARROLLO_DE_SOFTWARE_III\Desarrollo_III\Desarrollo3\sistema-gestion-ordenes\backend(anyString())).thenReturn(Optional.of(adminRole));

    
        // 🔥 Simular que `roleRepository.findAll()` devuelve una lista con el rol
        when(roleRepository.findAll()).thenReturn(Arrays.asList(adminRole));
    
        // 🔥 Imprimir los roles en la base de datos simulada
        System.out.println("Roles simulados en la BD (mockeados): " + roleRepository.findAll());
    }
    


    @Test
    void testRegistrarUsuario_Exitoso() {
        // Arrange
        UserRequestDTO request = new UserRequestDTO();
        request.setNombre("Juan");
        request.setEmail("juan@example.com");
        request.setPassword("password123");

        // Crear un rol simulado
        Role role = new Role();
        role.setId(1);
        role.setNombre("Admin");

        request.setRol(role);

        User user = new User();
        user.setId(1L);
        user.setNombre(request.getNombre());
        user.setEmail(request.getEmail());
        user.setPassword("encodedPassword");
        user.setRol(role);

        // 🔥 Simular el comportamiento de los repositorios
        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByNombreIgnoreCase(eq("Admin"))).thenReturn(Optional.of(role));
        when(roleRepository.findByNombreIgnoreCase(eq(" Admin"))).thenReturn(Optional.of(role));
        when(roleRepository.findByNombreIgnoreCase(eq("Admin "))).thenReturn(Optional.of(role));
        when(roleRepository.findByNombreIgnoreCase(eq(" admin"))).thenReturn(Optional.of(role));
        when(roleRepository.findByNombreIgnoreCase(eq("admin"))).thenReturn(Optional.of(role));

        when(roleRepository.findByNombreIgnoreCase(eq("Admin"))).thenReturn(Optional.of(role));  
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        UserResponseDTO response = userService.registrarUsuario(request);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Juan", response.getNombre());
        assertEquals("juan@example.com", response.getEmail());
        assertEquals("Admin", response.getRol());

        // ✅ Verificar llamadas
        verify(userRepository, times(1)).save(any(User.class));
        verify(roleRepository, times(1)).findByNombre(eq("Admin"));  // 🔥 Asegurar que se llamó correctamente
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