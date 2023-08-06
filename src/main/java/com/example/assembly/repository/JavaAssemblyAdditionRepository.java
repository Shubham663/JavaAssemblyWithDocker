package com.example.assembly.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.assembly.entity.AdditionStatus;

@Repository
public interface JavaAssemblyAdditionRepository extends JpaRepository<AdditionStatus,Integer>{

}
