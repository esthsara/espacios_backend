package com.espaciosdeportivos.repository;

import java.util.List;
//import java.util.Optional;

//import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.espaciosdeportivos.model.incluye;
import com.espaciosdeportivos.model.incluyeId;

@Repository
public  interface incluyeRepository extends JpaRepository<incluye, incluyeId>{
    
     List<incluye> findByCanchaIdCancha(Long idCancha);
}

