package hills.Anton;

import lombok.Getter;

/**
 * Created by gustav on 2017-03-21.
 */
public class Init {
	
	@Getter int age = 5;
	
	public Init(){
		System.out.println(getAge());
	}
	
}
