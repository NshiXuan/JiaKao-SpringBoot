package com.sx.jk.test;

public class SqlTest {
  public static void main(String[] args) {
    for (int i = 100; i > 0; i--) {
      String sql = "INSERT INTO dict_type(name,value,intro) VALUES ('哈哈%d','hh%d','随便写写%d');\n";
      System.out.format(sql, i, i, i);
    }
  }
}
