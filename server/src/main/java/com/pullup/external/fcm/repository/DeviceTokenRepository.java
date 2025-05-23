package com.pullup.external.fcm.repository;

import com.pullup.external.fcm.domain.DeviceToken;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DeviceTokenRepository extends JpaRepository<DeviceToken, Long> {

    @Query("SELECT COUNT(d) FROM DeviceToken d WHERE d.token = :token")
    Optional<Long> countDeviceTokenByToken(String token);

    Optional<DeviceToken> findByToken(String token);

    @Modifying
    @Query("DELETE FROM DeviceToken d WHERE d.token IN :tokens")
    int deleteByToken(@Param("tokens") List<String> invalidPushTokens);
}
