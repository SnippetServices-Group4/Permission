package com.services.group4.permission.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserDtoTests {
@Test
void testUserDto() {
  String userId = "user123";
  String username = "user_name";

  UserDto userDto = new UserDto(userId, username);

  assertNotNull(userDto);
  assertEquals(userId, userDto.userId());
  assertEquals(username, userDto.username());
}}

