package com.cmn.cmn.component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

public class KokodogEnvironmentStringPBEConfig extends EnvironmentStringPBEConfig {
  public KokodogEnvironmentStringPBEConfig() {
    BufferedReader br = null;
    String line = null;
    String data = "";
    boolean firstRead = true;
    try {
      br = new BufferedReader(new FileReader(this.getClass().getResource("/").getPath() + "conf/key.properties"));
    } catch (FileNotFoundException e) {
      try {
        br = new BufferedReader(new FileReader(this.getClass().getResource("/").getPath() + "../classes/conf/key.properties"));
      } catch (FileNotFoundException e2) {
        data = "KOKODOG_BRACE";
      }
    }
    if ("KOKODOG_BRACE".equals(data) == false) {
      try {
        while ((line = br.readLine()) != null) {
          if (firstRead == true) {
            data = data + line;
            firstRead = false;
          } else {
            data = data + "\n" + line;
          }
        }
      } catch (IOException e) {
        data = "KOKODOG_BRACE";      
      }
    }
    super.setPassword(data);
  }
}
