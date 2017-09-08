

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;





public class StudentGroup implements GroupOperationService {

	private Student[] students;
	

	public StudentGroup() {
		super();
		students = new Student[0];
	}

	public StudentGroup(int length) {
		this();
	}

	@Override
	public Student[] getStudents() {		
		return students;
	}

	@Override
	public void setStudents(Student[] students) {
		if (students == null) {
			throw new IllegalArgumentException();
		}		
		this.students = students;
	}

	@Override
	public Student getStudent(int index) {
		if (index >= students.length || index < 0) {
			throw new IllegalArgumentException();
		}
		return students[index];
	}

	@Override
	public void setStudent(Student student, int index) {
		if (student == null || index < 0 || index >= students.length) {
			throw new IllegalArgumentException();
		}	
		students[index] = student;		
	}
	
	@Override
	public void addFirst(Student student) {
		if (student == null) {
			throw new IllegalArgumentException();
		}	
		Student[] buf = new Student[students.length + 1];
		int i = 0;
		buf[0] = student;
		for(i = 1; i < buf.length; i++){
			buf[i] = students[i - 1];
		}		
		students = buf;	
	}

	@Override
	public void addLast(Student student) {
		if (student == null) {
			throw new IllegalArgumentException();
		}	
		Student[] buf = new Student[students.length + 1];
		int i = 0;
		for(; i < students.length; i++){
			buf[i] = students[i];
		}
		buf[i] = student;
		students = buf;		
	}

	@Override
	public void remove(int index) {
		if (index < 0 || index >= students.length) {
			throw new IllegalArgumentException();
		}		
		Student[] buf = new Student[students.length - 1];			
		for(int i = 0, j = 0; i < buf.length; i++, j++) {
			if (i == index) {
				j++;
			}
			buf[i] = students[j];			
		}
		students = buf;
	}

	@Override
	public void remove(Student student) {
		if(student == null) {
			throw new IllegalArgumentException();
		}
		for (int i = 0; i < students.length; i++) {
			if(students[i].equals(student)) {
				remove(i--);				
			}
		}
	}

	@Override
	public void removeFromIndex(int index) {
		if (index < 0 || index >= students.length) {
			throw new IllegalArgumentException();
		}
		Student[] buf = new Student[index];		
		for(int i = 0; i < buf.length; i++){
			buf[i] = students[i];
		}			
		students = buf;			
	}

	@Override
	public void removeFromElement(Student student) {
		if(student == null) {
			throw new IllegalArgumentException();
		}
		int i = 0; 
		boolean flag = false;
		for (i = 0; i < students.length; i++) {
			if(students[i].equals(student)) {
				flag = true;
				break;
			}
		}
		if(flag) {
			removeFromIndex(i);
		}		
	}

	@Override
	public void removeToIndex(int index) {
		if (index < 0 || index >= students.length) {
			throw new IllegalArgumentException();
		}
		Student[] buf = new Student[students.length - index];		
		for(int i = 0; i < buf.length; i++){
			buf[i] = students[index + i];
		}			
		students = buf;				
	}

	@Override
	public void removeToElement(Student student) {
		if(student == null) {
			throw new IllegalArgumentException();
		}
		int i = 0; 
		boolean flag = false;
		for (i = 0; i < students.length; i++) {
			if(students[i].equals(student)) {
				flag = true;
				break;
			}
		}
		if(flag) {
			removeToIndex(i);
		}
	}

	@Override
	public void bubbleSort() {
		Student buf = null;
		for (int i = 0; i < students.length - 1; i++) {
			for (int j = students.length - 1; j > i; j--) {
				if(students[j].compareTo(students[j - 1]) < 0) {
					buf = students[j];
					students[j] = students[j - 1];
					students[j - 1] = buf;					
				}
			}
		}
	}

	@Override
	public Student[] getByBirthDate(Date date) {
		if(date == null) {
			throw new IllegalArgumentException();
		}
		StudentGroup st = new StudentGroup();
		for (int i = 0; i < students.length; i++) {
			if (students[i].getBirthDate().equals(date)) {
				st.addLast(students[i]);
			}				
		}
		return st.getStudents();
	}

	@Override
	public Student[] getBetweenBirthDates(Date firstDate, Date lastDate) {
		if(firstDate == null || lastDate == null) {
			throw new IllegalArgumentException();
		}
		StudentGroup st = new StudentGroup();
		/*
		if(firstDate.compareTo(lastDate) > 0) {
			Date buf = firstDate;
			firstDate = lastDate;
			lastDate = buf;
		}
		*/
		for (int i = 0; i < students.length; i++) {
			if (students[i].getBirthDate().compareTo(firstDate) >= 0 && 
					students[i].getBirthDate().compareTo(lastDate) <= 0) {
				st.addLast(students[i]);
			}				
		}
		return students;
		//return st.getStudents();	
	}

	@Override
	public Student[] getNearBirthDate(Date date, int days) {
		if(date == null) {
			throw new IllegalArgumentException();
		}
		Date lastDate = new Date(date.getTime() + (days * 24 * 60 * 60 * 1000l));		
		return getBetweenBirthDates(date, lastDate);
	}

	@Override
	public int getCurrentAgeByDate(int indexOfStudent) {
		if (indexOfStudent < 0 || indexOfStudent >= students.length) {
			throw new IllegalArgumentException();
		}
		return new Date().getYear() - students[indexOfStudent].getBirthDate().getYear();
	}

	@Override
	public Student[] getStudentsByAge(int age) {
		StudentGroup st = new StudentGroup();
		int currentAge = 0;
		for(Student k: students) {
			currentAge = new Date().getYear() - k.getBirthDate().getYear();
			if(currentAge == age) {
				st.addLast(k);
			}
		}
		return st.getStudents();
	}

	@Override
	public Student[] getStudentsWithMaxAvgMark() {
		if(students.length == 0) {
			return students;
		}
		StudentGroup st = new StudentGroup();
		double max = students[0].getAvgMark();
		for(Student k: students) {
			if(k.getAvgMark() > max) {
				max = k.getAvgMark();
			}
		}
		for(Student k: students) {
			if(k.getAvgMark() == max) {
				st.addLast(k);
			}
		}
		return st.getStudents();
	}

	@Override
	public Student getNextStudent(Student student) {
		if(student == null) {
			throw new IllegalArgumentException();
		}
		for(int i = 0; i < students.length - 1; i++) {
			if(student.equals(students[i])) {
				return students[i + 1];
			}
		}
		return student;
	}

	@Override
	public void add(Student student, int index) {
		if (student == null || index < 0 || index >= students.length) {
			throw new IllegalArgumentException();
		}		
		Student[] buf = new Student[students.length + 1];			
		for(int i = 0; i < buf.length; i++) {
			if (i < index) {
				buf[i] = students[i];
			} else if (i == index) {
				buf[i] = student;
			} else {
				buf[i] = students[i-1];
			}			
		}
		students = buf;
	}

	private int getStudentIndex(Student student) {
		return 0;

	}

	private int getDiffYears(Date first, Date last) {		
		return 0;

	}

	private Calendar getCalendar(Date date) {
		return null;

	}
}
