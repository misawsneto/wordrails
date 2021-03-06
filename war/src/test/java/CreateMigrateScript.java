import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateMigrateScript {

//	--script.sql
//	--constraints.sql
//	--duplicate_post.sql
//	--multitenant.sql
//	--images.sql
//	--passwordReset.sql
//	--refactoring_notification.sql
//	--insert_mobiledevice.sql
// 	  bookmarks-fix.sql
//	  new_invitation.sql

	private static final String SCRIPT_NAME = "new_invitation";

	public static void main(String[] args) throws IOException {
		CreateMigrateScript script = new CreateMigrateScript();
		script.createScript();
	}

	public void createScript() throws IOException {
		SimpleDateFormat sm = new SimpleDateFormat("yyyyMMddHHmmss");
		String timestamp = sm.format(new Date());
		System.out.println(timestamp);

		File file = new File("war/src/main/resources/db/migration/V" + timestamp + "__" + SCRIPT_NAME + ".sql");
		System.out.println(file.getAbsolutePath());

		file.createNewFile();

		System.out.println("Script created sucessfully");
	}
}