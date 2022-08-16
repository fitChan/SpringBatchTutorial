package com.example.springbatchtutorial.job.TrMigrationConfig.accounts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Accounts, Integer> {
}
