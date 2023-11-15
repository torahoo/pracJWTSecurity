package com.pracjwtsecurity.repository;

import com.pracjwtsecurity.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
======================================================================
CRUD 함수를 JpaRepository가 들고 있음.
@Repository라는 어노테이션이 없어도 IoC됨. 이유는 JpaRepository를 상속했기 때문에
======================================================================
 */

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /*
    ======================================================================
    findBy 규칙 ==> Username 문법
    select * from user where username = ?
    ==> public User findByUsername
    Jpa Query Method
    ======================================================================
     */
    public User findByUsername(String username);
    public User findByEmail(String email);
}
