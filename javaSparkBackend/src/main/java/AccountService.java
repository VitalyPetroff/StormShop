import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class AccountService {

    final private String accountsFilePath = "accounts.json";
    private static final Logger LOGGER = LoggerFactory.getLogger(Good.class);
    private ObjectMapper mapper = new ObjectMapper();
    private AccountDao dao = new AccountDao(accountsFilePath);
    private List<String> listTokens = new ArrayList<>();

    public String authorization(Account client) {
        List<Account> listAccounts = new ArrayList<>();
        listAccounts = dao.getAccounts();
        String result;
        for (Account account : listAccounts) {
            if ((account.login).equals(client.login)) {
                Sha1Creator creator = new Sha1Creator();
                String hashFromClient = creator.createSha1(client.password);
                if (account.password.equals(hashFromClient)) {
                    String key = "hkas4123jdfhl";
                    result = creator.createSha1(client.password + key);
                    listTokens.add(result);
                    return result;
                }
            }
        }
        return "FAILED";
    }

    public boolean verification(String clientToken) {
        boolean result = false;
        for (String accessToken : listTokens) {
            if (clientToken.equals(accessToken)) {
                result = true;
            }
        }
        return result;
    }
}
