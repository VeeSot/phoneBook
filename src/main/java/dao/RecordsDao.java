package dao;

import dbService.DBService;
import org.hibernate.cfg.Configuration;
import dataSets.Record;

import java.io.Serializable;
import java.util.List;

public class RecordsDao implements Serializable {
    private DBService dbService;

    public void setConfiguration(Configuration configuration) {
        dbService = new DBService(configuration);

    }


    public Record get(long id) {
        Record dataSet = dbService.get(id);
        if (dataSet == null) {
            throw new NullPointerException("Record not found");
        }
        return dataSet;
    }


    public List getAll() {
        return dbService.getAll();
    }


    public long save(Record dataSet) {
        return dbService.save(dataSet);
    }

    public void update(Record dataSet) {
        dbService.update(dataSet);
    }


    public List findByName(String name) {
        //Поиск абонента по подстроке
        return dbService.findByName(name);
    }

    public void delete(long id) {
        Record dataSet = this.get(id);
        dbService.delete(dataSet);
    }


}
