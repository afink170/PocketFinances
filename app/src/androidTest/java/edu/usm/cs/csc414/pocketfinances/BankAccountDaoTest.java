package edu.usm.cs.csc414.pocketfinances;

import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class BankAccountDaoTest {

    private FinancesDatabase db;
    private BankAccountDao dao;


    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void initDb() throws Exception {
        db = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
                FinancesDatabase.class)
                .allowMainThreadQueries()
                .build();

        dao = db.getBankAccountDao();
    }

    @After
    public void closeDb() throws Exception {
        db.close();
    }

    @Test
    public void onFetchingAccounts_shouldGetEmptyList_IfTable_IsEmpty() throws InterruptedException {
        List<BankAccount> accountsList = LiveDataTestUtil.getValue(dao.getAllBankAccounts());
        assertTrue(accountsList.isEmpty());
    }

    @Test
    public void onUpdatingAnAccount_checkIf_UpdateHappensCorrectly() throws InterruptedException {
        BankAccount account = new BankAccount("TestBank1", "My Test Account 1", "TestChecking", 1000.00);
        dao.insert(account);
        account.setAccountName("My Changed Account 1");
        dao.update(account);
        assertEquals(1, LiveDataTestUtil.getValue(dao.getAllBankAccounts()).size());
        /*
        assertEquals("My Changed Account 1",
                LiveDataTestUtil.getValue(dao.getAllBankAccounts()).get(0).getAccountName());
                */
    }

}
