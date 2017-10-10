package com.cmn.cmn.component;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;

import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

public class KokodogEnvironmentStringPBEConfig extends EnvironmentStringPBEConfig {
  public KokodogEnvironmentStringPBEConfig() {
    BufferedReader br = null;
    String line = null;
    String data = "";
    boolean firstRead = true;
    String realPath = null;
    try {
      realPath =  this.getClass().getResource("/").toURI().getPath();
    } catch (URISyntaxException e) {
      realPath = "";
    }
    try {
      if (realPath.length() != 0 && realPath.charAt(realPath.length() - 1) != '/') {
        realPath = realPath + "/";
      }
      realPath = realPath + "../classes";
      br = new BufferedReader(new FileReader(realPath + "/conf/key.properties"));
      while ((line = br.readLine()) != null) {
        if (firstRead == true) {
          data = data + line;
          firstRead = false;
        } else {
          data = data + "\n" + line;
        }

      }
    } catch (FileNotFoundException e) {
      data = "KOKODOG_BRACE";
    } catch (IOException e) {
      data = "KOKODOG_BRACE";      
    }
    super.setPassword(data);
  }
}
