package com.kzk.study.jaxb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class TestRandom {

	public static void main(String[] args) {
		List<Integer> list = new ArrayList<Integer>();
		for(int i=0;i<100;i++){
			list.add(i);
		}
		printList(list);
		Collections.shuffle(list);
		System.out.println("==========================");
		printList(list);
		
	}
	
	private static void printList(List<Integer> list){
		for(int i=0;i<100;i++){
			System.out.println(list.get(i));
		}
		System.out.println(Arrays.deepToString(list.toArray()));
	}

}
