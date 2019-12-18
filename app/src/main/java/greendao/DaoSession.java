package greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.nader.intelligent.entity.vo.DeviceVo;

import greendao.DeviceVoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig deviceVoDaoConfig;

    private final DeviceVoDao deviceVoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        deviceVoDaoConfig = daoConfigMap.get(DeviceVoDao.class).clone();
        deviceVoDaoConfig.initIdentityScope(type);

        deviceVoDao = new DeviceVoDao(deviceVoDaoConfig, this);

        registerDao(DeviceVo.class, deviceVoDao);
    }
    
    public void clear() {
        deviceVoDaoConfig.clearIdentityScope();
    }

    public DeviceVoDao getDeviceVoDao() {
        return deviceVoDao;
    }

}
