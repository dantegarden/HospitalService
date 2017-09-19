package com.dvt.HospitalService.commons.test;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class TestJdk8 {
	@Test
	public void method(){
		//lambda
		String separator = ",";
		Arrays.asList("a","b","c").forEach((String e) -> {
			String str = e + separator;
			System.out.print(str);
		});
		Arrays.asList( "a", "b", "d" ).sort( ( e1, e2 ) -> e1.compareTo( e2 ) );
		List<String> strList = Arrays.asList( "a", "d", "b" );
        strList.sort( ( e1, e2 ) -> e1.compareTo( e2 ) );
        System.out.println(strList);

        //下面的写法和上面一样
        strList.sort( ( e1, e2 ) -> {
            return e1.compareTo( e2 );
        } );
	}
}
