package com.bohype.repository;

import com.bohype.model.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account,Long> {

    Optional<Account> findByEmail(String email);
}
