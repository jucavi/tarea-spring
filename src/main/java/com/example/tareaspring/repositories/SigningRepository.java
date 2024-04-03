package com.example.tareaspring.repositories;

import com.example.tareaspring.models.Signing;
import com.example.tareaspring.models.SigningKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SigningRepository extends JpaRepository<Signing, SigningKey> {
}
