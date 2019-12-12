package io.geekmind.budgie.repository;

import io.geekmind.budgie.model.entity.AccountParameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountParameterRepository extends JpaRepository<AccountParameter, Integer> {

    List<AccountParameter> findByAccount_Id(Integer id);

}
