package VLmongobaeldung;

import VLmongobaeldung.config.MongoConfig;
import VLmongobaeldung.repository.UserRepository;
import VLmongobaeldung.model.User;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@SpringBootApplication
public class VlMongoApplication {
//		https://www.baeldung.com/spring-data-mongodb-tutorial

	public static void main(String[] args) {
//		SpringApplication.run(VlMongoApplication.class, args);

		ApplicationContext ctx = new AnnotationConfigApplicationContext(MongoConfig.class);
		MongoTemplate mongoTemplate = (MongoTemplate) ctx.getBean("mongoTemplate");
		UserRepository userRepository = (UserRepository) ctx.getBean(UserRepository.class);

		System.out.println("\n\n===========VL start... =========");
		User user = new User();
		user.setName("jon");
		user.setAge(1);
		String sDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"));
		String sTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("-HH-mm"));
		user.setEmailAddress("jon"+sTime +"@lala.exe");
		user.setCreated(sDate);

/** curd by mongoTemplate (work ok)
		System.out.println("\n\n====================== insert =====================");
		mongoTemplate.insert(user, "bae-user");
		System.out.println("\n\n======================  save =======================");
		user = new User("jon", 2, "albert"+ sTime + "@albert.com");
		user.setCreated(sDate);
		mongoTemplate.save(user, "bae-user");
		user = new User("jon", 3, "albert"+ sTime + "@albert.com");
		user.setCreated(sDate);
		mongoTemplate.save(user, "bae-user");
		user = new User("jon", 4, "albert"+ sTime + "@albert.com");
		user.setCreated(sDate);
		mongoTemplate.save(user, "bae-user");
		user = new User("jon", 5, "albert"+ sTime + "@albert.com");
		user.setCreated(sDate);
		mongoTemplate.save(user, "bae-user");
  		System.out.println("\n\n====================== Find one  ===============");
		user = mongoTemplate.findOne(Query.query(Criteria.where("name").is("Jon")), User.class);
		System.out.println("Found user: " + user==null? "not found":user.getName());

  		System.out.println("\n\n====================== user: save-update  ===============");
		user = mongoTemplate.findOne(Query.query(Criteria.where("name").is("Jon")), User.class);
		if(user != null){
			user.setName("Jim");
			mongoTemplate.save(user, "bae-user");
		}
		System.out.println("\n\n====================== user: update very first  =========");
		Query query = new Query();
		query.addCriteria(Criteria.where("name").is("jon"));
		Update update = new Update();
		update.set("name", "Jon very first upd");
		mongoTemplate.updateFirst(query, update, User.class);
		System.out.println("\n\n=================== user: update multi (all docs)=========");
		Query query1 = new Query();
		query1.addCriteria(Criteria.where("name").is("jon"));
		Update update1 = new Update();
		update1.set("name", "Albert");
		mongoTemplate.updateMulti(query1, update1, User.class);
		System.out.println("\n\n=================== user: FindAndModify ==========\nreturns the object before it was modified");
		Query query2 = new Query();
		query2.addCriteria(Criteria.where("name").is("Albert"));
		Update update2 = new Update();
		update2.set("name", "Albert find n modify");
		User user2 = mongoTemplate.findAndModify(query2, update2, User.class);
		System.out.println("user2 = " + user2.getName());

		System.out.println("\n\n=================== user: upsert ==========\n");
//		The upsert works on the find and modify else create semantics: if the document is matched, update it, or else
//		create a new document by combining the query and update object.

		Query query3 = new Query();
		query3.addCriteria(Criteria.where("name").is("Albert"));
		Update update3 = new Update();
		update3.set("name", "albert upsert");
		mongoTemplate.upsert(query3, update3, User.class);

		System.out.println("\n\n=================== user: Remove ==========\n");
		User usr = mongoTemplate.findOne(Query.query(Criteria.where("name").is("Albert find n modify")), User.class);

		mongoTemplate.remove(usr, "bae-user");
*/

/////////////////////// crud by MongoRepository //////////////////////////////
		System.out.println("\n\n================================= INSERT =================================");
		userRepository.insert(user);
		System.out.println("\n\n================================= SAVE-INSERT =================================");
		user = new User("jon", 2, "albert"+ sTime + "@albert.com");
		user.setCreated(sDate);
		userRepository.save(user);
		user = new User("jon", 3, "albert"+ sTime + "@albert.com");
		user.setCreated(sDate);
		userRepository.save(user);
		user = new User("jon", 4, "albert"+ sTime + "@albert.com");
		user.setCreated(sDate);
		userRepository.save(user);
		user = new User("jon", 5, "albert"+ sTime + "@albert.com");
		user.setCreated(sDate);
		userRepository.save(user);

		System.out.println("\n\n================================= SAVE-UPDATE =================================");
		user = mongoTemplate.findOne(Query.query(Criteria.where("name").is("jon")), User.class);
		user.setName("Jim");
		userRepository.save(user);

		System.out.println("\n\n================================= DELETE =================================");
		userRepository.delete(user);

		System.out.println("\n\n================================= FIND ONE =================================");
		//https://www.baeldung.com/spring-data-query-by-example
		Example<User> uExample = Example.of(new User("jon"));
//		User usrTemlate = mongoTemplate.findOne(Query.query(Criteria.where("name").is("jon")), User.class);

		Optional<User> oUser = userRepository.findOne(uExample);
		System.out.println("user found by id: " + oUser.get().getName() + ", age: " + oUser.get().getAge());
		System.out.println("\n\n================================= EXISTS =================================");
		uExample = Example.of(new User("jon", 3));
		System.out.println("exixts: " + userRepository.exists(uExample));
		System.out.println("\n\n=========================== FindAll With Sort ==============================");
		List<User> users = userRepository.findAll(Sort.by(Sort.Direction.ASC, "name"));
		users.forEach(System.out::println);
		System.out.println("\n\n=========================FindAll With Pageable ==============================");
		Pageable pageableRequest = PageRequest.of(0, 1);
		Page<User> pages = userRepository.findAll(pageableRequest);
		List<User> usersPg = pages.getContent();
		usersPg.forEach(System.out::println);

	} // public static void main(String[] args) {

} // public class VlMongoApplication {
