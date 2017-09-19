package com.dvt.HospitalService.commons.test.jdk8;

import java.util.function.Supplier;

public class Car {
	public Car(){
		System.out.println("Construct a car");
	}
	
	public static Car create( final Supplier< Car > supplier ) {
        return supplier.get();
    }              
         
    public static void collide( final Car car ) {
        System.out.println( "Collided " + car.toString() );
    }
         
    public void follow( final Car another ) {
        System.out.println( "Following the " + another.toString() );
    }
         
    public void repair() {   
        System.out.println( "Repaired " + this.toString() );
    }
}
