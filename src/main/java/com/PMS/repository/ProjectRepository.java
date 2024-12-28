package com.PMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.PMS.model.Project;
import com.PMS.model.User;

import java.util.List;


public interface ProjectRepository extends JpaRepository<Project, Long> {
    // List<Project> findByOwner(User owner);

    List<Project> findByNameContainingAndTeamContains(String partialName, User user);

    // @Query("SELECT p FROM Project p JOIN p.team t WHERE t=:user")
    // List<Project> findProjectByTeam(@Param("user") User user);

    List<Project> findByTeamContainingOrOwner(User user, User owner);
}
