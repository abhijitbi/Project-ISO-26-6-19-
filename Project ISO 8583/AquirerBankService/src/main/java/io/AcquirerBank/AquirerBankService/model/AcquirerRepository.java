package io.AcquirerBank.AquirerBankService.model;

import io.AcquirerBank.AquirerBankService.model.BankDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AcquirerRepository extends MongoRepository<BankDetails,String>
{

}
