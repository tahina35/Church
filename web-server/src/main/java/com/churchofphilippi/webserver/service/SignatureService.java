package com.churchofphilippi.webserver.service;

import com.churchofphilippi.webserver.model.Member;
import com.churchofphilippi.webserver.model.Signature;
import com.churchofphilippi.webserver.repository.SignatureRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class SignatureService implements BaseService<Signature> {

    private final SignatureRepository signatureRepository;

    @Override
    public List<Signature> getAll() {
        return signatureRepository.findAll();
    }

    @Override
    public Signature save(Signature entity) {
        return signatureRepository.save(entity);
    }

    @Override
    public void delete(Signature entity) {
        signatureRepository.delete(entity);
    }

    public int numberOfPrWaitingForSignature(Long id) {
        return signatureRepository.numberOfPrWaitingForSignature(id);
    }
}
