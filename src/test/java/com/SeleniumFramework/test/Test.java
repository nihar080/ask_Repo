package com.SeleniumFramework.test;

import java.io.IOException;

public class Test {
public static void main(String[] args) throws IOException {
	Runtime.getRuntime().exec("taskkill /F /IM WINWORD.exe");
	System.out.println("Done");
}
}
