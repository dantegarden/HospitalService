package com.dvt.HospitalService.commons.test.jdk8;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Jdk8Test {
	
	public String aaa;
	
	public static void main(String[] args) {
		Supplier < Jdk8Test > supplier  = Jdk8Test::new;
		supplier.get();
		
		//Function function = String::toUpperCase;
		//Function function = supplier.get()::addOne;
		
		Car car = Car.create(Car::new);
		List< Car > cars = Arrays.asList( car );
		cars.forEach(Car::collide);
		
//		System.out.println(FunctionalInterfaceTest.minus(3, 2));
		//FunctionalInterfaceTest a = i -> { System.out.println(i); };
		List<String> list = Arrays.asList("aa","bb","cc").stream().map(String::toUpperCase).collect(Collectors.toList());//.forEach(i-> {System.out.print(i);});
	}

	public int addOne(Jdk8Test i){
		return 1+1;
	}
	
	
	
}
