package com.espaciosdeportivos.repository;

//import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.espaciosdeportivos.model.sepractica;
import com.espaciosdeportivos.model.sepracticaId;

@Repository
public interface sepracticaRepository extends JpaRepository<sepractica, sepracticaId>{
    //List<sepractica> findByDiciplinaIdCancha(Long idCancha);
}
