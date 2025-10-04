/*package com.espaciosdeportivos.repository;


import com.espaciosdeportivos.model.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagenRepository extends JpaRepository<Imagen, Long> {
    List<Imagen> findByOwnerTypeAndOwnerIdAndActivaTrue(String ownerType, Long ownerId);
}
*/

// repository/ImagenRepository.java
// repository/ImagenRepository.java
package com.espaciosdeportivos.repository;

import com.espaciosdeportivos.model.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImagenRepository extends JpaRepository<Imagen, Long> {
    List<Imagen> findByDisciplinaIdAndEstadoTrue(Long disciplinaId);
}
