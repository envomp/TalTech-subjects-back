package ee.taltech.agonaudid.pojo.api.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserSubscriptionDto {

	private Long subjectId;
	private String token;

}
