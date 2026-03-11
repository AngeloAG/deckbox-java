package com.molardev.deckbox.infrastructure.persistence.translations;

import com.molardev.deckbox.application.common.models.UserSecurityDetails;
import com.molardev.deckbox.domain.entity.User;
import com.molardev.deckbox.infrastructure.persistence.entity.UserEntity;

public class UserTranslator {
  public static UserEntity toEntity(User user, String hash) {
    return new UserEntity(user.getId(), user.getEmail(), hash);
  }

  public static User rehydrate(UserEntity userEntity) {
    return User.create(userEntity.getId(), userEntity.getEmail(), io.vavr.collection.List.empty()).get();
  }

  public static UserSecurityDetails rehydrateSecurityDetails(UserEntity userEntity) {
    return new UserSecurityDetails(userEntity.getId(), userEntity.getEmail(), userEntity.getHash());
  }
}
