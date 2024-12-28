package com.PMS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.PMS.model.Invitation;

public interface InvitationRepository extends JpaRepository<Invitation, Long> {
    Invitation findByToken(String token);

    Invitation findByEmail(String userEmail);
}
