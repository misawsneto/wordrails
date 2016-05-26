package co.xarx.trix.learning;

import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.javers.core.metamodel.object.CdoSnapshot;
import org.javers.repository.jql.QueryBuilder;
import org.junit.Test;

import javax.persistence.Id;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class JaversLearning {
	class Person {
		@Id
		private String login;
		private String name;
		private Set<Integer> things;
		private Integer index = 0;

		public Person(String login, String name) {
			this.login = login;
			this.name = name;
			this.things = new TreeSet<>();
			this.add();
		}

		public void add(){
			this.things.add(++index);
		}

		public String getLogin() {
			return login;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString(){
			String s = this.name + " " + this.login;
			for(Integer i: things){
				s += " " + i;
			}
			return s;
		}
	}

	//Not really a test though
	@Test
	public void main() throws NoSuchFieldException, IllegalAccessException {
		// it's useful for testing -- in-memory persisting
		Javers javers = JaversBuilder.javers().build();

		// init your data
		Person robert = new Person("bob", "Robert Martin");
		// and persist initial commit
		javers.commit("user", robert);

		// do some changes
		robert.setName("Robert C.");
		// and persist another commit
		javers.commit("user", robert);

		// do some changes
		robert.setName("João");
		robert.add();
		// and persist another commit
		javers.commit("user", robert);

		// Creating object for different changes
		List<CdoSnapshot> snapshots = javers.findSnapshots(QueryBuilder.byInstanceId("bob", Person.class).build());
		for(CdoSnapshot snapshot: snapshots){
			Person p = new Person("bob", "Robert Martin");
			Class<?> c = p.getClass();

			for(String attrName: snapshot.getChanged()){
				Object value = snapshot.getPropertyValue(attrName);

				Field f = null;
				f = c.getDeclaredField(attrName);
				f.setAccessible(true);
				f.set(p, value);
			}
			System.out.println(p.toString() + " " + snapshot.getType().name() + " " + snapshot.getCommitMetadata().getCommitDate().toString());
		}
	}
}
