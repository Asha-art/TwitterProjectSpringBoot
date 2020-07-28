package com.win.twitter.repository;

public class RoleRepository {

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByRole(String role);
}