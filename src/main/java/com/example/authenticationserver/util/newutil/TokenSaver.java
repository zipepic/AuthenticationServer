package com.example.authenticationserver.util.newutil;

import java.io.IOException;
import java.text.ParseException;

interface TokenSaver {
  void save(String userId) throws IOException, ParseException;
}
