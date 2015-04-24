package testjdbc;

import java.util.List;

public class Demo {

	public static void main(String[] args) {
		DbBean bean = new DbBean();
		List li = bean.SelectRS("select * from tablename");
	}

}
