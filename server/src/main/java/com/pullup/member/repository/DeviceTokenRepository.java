package com.pullup.member.repository;

import com.pullup.member.domain.DeviceToken;
import org.springframework.data.repository.CrudRepository;

public interface DeviceTokenRepository extends CrudRepository<DeviceToken, Long> {
}
