package io.AcquirerBank.AquirerBankService.resource;

import io.AcquirerBank.AquirerBankService.model.AcquirerRepository;
import io.AcquirerBank.AquirerBankService.model.IINRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AcquirerService
{
    @Autowired
    private static AcquirerRepository acquirerRepository;

    @Autowired
    private IINRepository iinRepository;

    public AcquirerService() {
    }

    public AcquirerService(AcquirerRepository acquirerRepository) {

        this.acquirerRepository = acquirerRepository;
    }

    public void getIINNumber(int id){

        System.out.println("IIN numbers::"+iinRepository.findAll());
    }



}
