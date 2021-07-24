package com.churchofphilippi.webserver.repository;

import com.churchofphilippi.webserver.model.Signature;
import com.churchofphilippi.webserver.model.keys.SignatureKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignatureRepository extends JpaRepository<Signature, SignatureKey> {
}
