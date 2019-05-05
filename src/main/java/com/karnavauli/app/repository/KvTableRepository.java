package com.karnavauli.app.repository;

import com.karnavauli.app.model.entities.KvTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KvTableRepository  extends JpaRepository<KvTable, Long>{
    Optional<KvTable> findByName(String name);
}
