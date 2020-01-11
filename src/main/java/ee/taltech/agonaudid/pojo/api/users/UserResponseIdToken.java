package ee.taltech.agonaudid.pojo.api.users;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserResponseIdToken {
	private long id;
	private boolean isAdmin;
	private String token;
}
