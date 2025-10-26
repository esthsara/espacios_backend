package com.espaciosdeportivos.repository;

import java.util.List;
//import java.util.Optional;

//import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.espaciosdeportivos.model.Incluye;
import com.espaciosdeportivos.model.IncluyeId;

@Repository
public  interface incluyeRepository extends JpaRepository<Incluye, IncluyeId>{
    
     List<Incluye> findByCanchaIdCancha(Long idCancha);
}

