

import java.util.ArrayList;

public class dumbMain {
	public static void main(String [] args) {
		Heap heapTest = new Heap();
		
		Student zero = new Student(0);
        zero.setminCost(0);
        Student one = new Student(1);
        one.setminCost(1);
        Student two = new Student(2);
        two.setminCost(2);
        Student three=new Student(3);
        three.setminCost(3);
        Student four=new Student(4);
        four.setminCost(4);
        Student five=new Student(6);
        five.setminCost(6);
        Student six=new Student(8);
        six.setminCost(8);

        ArrayList<Student> tester = new ArrayList<>();
        tester.add(zero);
        tester.add(one);
        tester.add(two);
        tester.add(three);
        tester.add(four);     
        tester.add(five);
        tester.add(six);
       
        heapTest.buildHeap(tester);
        System.out.println(heapTest.toString());
        heapTest.changeKey(zero,9);
        System.out.println(heapTest.toString());
        
	}

	
	
}
