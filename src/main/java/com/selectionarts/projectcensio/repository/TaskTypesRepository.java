package com.selectionarts.projectcensio.repository;

import com.selectionarts.projectcensio.model.Types;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskTypesRepository extends JpaRepository<Types, Long> {


}
