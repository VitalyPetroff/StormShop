import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AccountDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(Good.class);
    private String accountsFilePath;
    private ObjectMapper mapper = new ObjectMapper();

    AccountDao(String accountsFilePath) {
        this.accountsFilePath = accountsFilePath;
    }

    synchronized public ArrayList getAccounts() {
        ArrayList<Account> listOfAccounts = new ArrayList<>();
        try {
            listOfAccounts = mapper.readValue(new File(accountsFilePath),
                    new TypeReference<ArrayList<Account>>() {});
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
        return listOfAccounts;
    }
}
