package com.nwuking.ytalk.db;

import com.nwuking.ytalk.ChatSession;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

public class DaoSession extends AbstractDaoSession {

    private final DaoConfig chatSessionDaoConfig;
    private final DaoConfig contactsDaoConfig;

    private final ChatSessionDao chatSessionDao;
    private final ContactsDao contactsDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        chatSessionDaoConfig = daoConfigMap.get(ChatSessionDao.class).clone();
        chatSessionDaoConfig.initIdentityScope(type);

        contactsDaoConfig = daoConfigMap.get(ContactsDao.class).clone();
        contactsDaoConfig.initIdentityScope(type);

        chatSessionDao = new ChatSessionDao(chatSessionDaoConfig, this);
        contactsDao = new ContactsDao(contactsDaoConfig, this);

        registerDao(ChatSession.class, chatSessionDao);
        registerDao(Contacts.class, contactsDao);
    }

    public void clear() {
        chatSessionDaoConfig.clearIdentityScope();
        contactsDaoConfig.clearIdentityScope();
    }

    public ChatSessionDao getChatSessionDao() {
        return chatSessionDao;
    }

    public ContactsDao getContactsDao() {
        return contactsDao;
    }

}