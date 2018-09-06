package org.xyh.quartz;

/**
* @ClassName: JobTest
* @author xueyh
* @date 2018年4月16日 上午11:10:12
* 
*/
public class JobTest {
	
	int id;
	String name;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	* @param id 要设置的 id
	*/
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	* @param name 要设置的 name
	*/
	public void setName(String name) {
		this.name = name;
	}
	/**
	* <p>Title: </p>
	* <p>Description: </p>
	* @param id
	* @param name
	*/
	public JobTest(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	/**
	* <p>Title: </p>
	* <p>Description: </p>
	*/
	public JobTest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
