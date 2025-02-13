package com.pullup.member.repository;

import com.pullup.member.domain.DeviceToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {

    @Query("SELECT COUNT(d) > 0 FROM DeviceToken d WHERE d.token = :token")
    Boolean existsDeviceTokenByToken(String token);


    Optional<DeviceToken> findByToken(String token);
}
