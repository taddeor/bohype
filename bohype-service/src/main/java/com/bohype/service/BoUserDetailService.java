package com.bohype.service;

import com.bohype.model.Account;
import com.bohype.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoUserDetailService implements UserDetailsService {
    private final AccountRepository accountRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final var byEmail = accountRepository.findByEmail(username);
        if(byEmail.isPresent()){
            final var account = byEmail.get();
            return new User(account.getEmail(), account.getPassword(), List.of());
        }
        return null;
    }


    @PostConstruct
    public void createAccount(){
        final var byEmail = accountRepository.findByEmail("test@test.it");
        if(byEmail.isEmpty()) {
            Account account = new Account();
            account.setEmail("test@test.it");
            account.setName("test");
            account.setSurname("test");
            account.setPassword(new BCryptPasswordEncoder().encode("test"));
            accountRepository.save(account);
        }

    }
}
