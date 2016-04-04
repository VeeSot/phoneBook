package dao;

import common.Config;
import dataSets.Record;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static common.Utils.getRandomName;
import static common.Utils.getRandomNumber;


/**
 * veesot on 4/2/16.
 */
public class RecordsDaoTest {
    RecordsDao dao;


    @Before
    public void setUp() throws Exception {
        dao = new RecordsDao();
        dao.setConfiguration(Config.getTestDbConfiguration());
    }

    @Test
    public void testGet() throws Exception {
        String phoneNumber = getRandomNumber();
        String people = getRandomName();

        Record record = new Record();
        record.setNumber(phoneNumber);
        record.setPeople(people);

        long recordSaveId = dao.save(record);
        Record createdRecord = dao.get(recordSaveId);
        Assert.assertEquals(phoneNumber, createdRecord.getNumber());
        Assert.assertEquals(people, createdRecord.getPeople());
        Assert.assertEquals(recordSaveId, createdRecord.getRecordId());


    }

    @Test
    public void testGetAll() throws Exception {
        int amountOfRecords = 10;
        List<Record> expectedRecordList = new ArrayList<>();
        //Наплодим множество записей
        for (int i = 0; i < amountOfRecords; i++) {
            String phoneNumber = getRandomNumber();
            String people = getRandomName();

            Record record = new Record();
            record.setNumber(phoneNumber);
            record.setPeople(people);

            long newRecordId = dao.save(record);
            Record newRecord = dao.get(newRecordId);
            expectedRecordList.add(newRecord);//Пригодится для сравнения
        }

        List returnedRecordList = dao.getAll(); // Вытащим всё из БД
        Assert.assertEquals(expectedRecordList.size(), returnedRecordList.size());
        Assert.assertEquals(expectedRecordList, returnedRecordList);

    }

    @Test
    public void testSave() throws Exception {
        String phoneNumber = getRandomNumber();
        String people = getRandomName();

        Record record = new Record();
        record.setNumber(phoneNumber);
        record.setPeople(people);

        long recordSaveId = dao.save(record);
        Assert.assertNotEquals(0, recordSaveId);
    }

    @Test
    public void testRepeatSave() throws Exception {
        String phoneNumber = getRandomNumber();
        String people = getRandomName();

        Record record = new Record();
        record.setNumber(phoneNumber);
        record.setPeople(people);

        dao.save(record);
        long recordId = dao.save(record);
        Assert.assertEquals(0, recordId);//Дубликат не создался

    }

    @Test
    public void testUpdate() throws Exception {
        String phoneNumber = getRandomNumber();
        String people = getRandomName();

        Record record = new Record();
        record.setNumber(phoneNumber);
        record.setPeople(people);

        long recordSaveId = dao.save(record);
        Record createdRecord = dao.get(recordSaveId);

        String newPeople = getRandomName();
        String newNumber = getRandomNumber();
        createdRecord.setPeople(newPeople);
        createdRecord.setNumber(newNumber);
        dao.update(createdRecord);

        Record updatedRecord = dao.get(createdRecord.getRecordId());
        Assert.assertEquals(newPeople, updatedRecord.getPeople());
        Assert.assertEquals(newNumber, updatedRecord.getNumber());


    }

    @Test
    public void testFindByName() throws Exception {
        Record record = new Record();

        String firstPhoneNumber = getRandomNumber();
        String firstPeople = getRandomName();


        record.setNumber(firstPhoneNumber);
        record.setPeople(firstPeople);
        long firstRecordId = dao.save(record);
        Record firstRecord = dao.get(firstRecordId);

        //Обновим параметры и пересохраним как ещё одну модель
        String secondPhoneNumber = getRandomNumber();
        String secondPeople = getRandomName();
        record.setNumber(secondPhoneNumber);
        record.setPeople(secondPeople);
        long secondRecordId = dao.save(record);
        Record secondRecord = dao.get(secondRecordId);

        //Возьмем часть информации о первой записи для поиска по ней

        String partOfName = firstPeople.substring(1, firstPeople.length() - 1);//Строка у нас точно более 2 симолов.Можно извлекать от первого до предпоследнего.
        if (secondPeople.contains(partOfName)) {//Маловероятно, но всё таки стоит проверить возможное совпадение с двумя образцами сразу
            partOfName = secondPeople.substring(1, secondPeople.length() - 1);//Подменим на альтернативный вариант
        }

        List recordList = dao.findByName(partOfName);
        Assert.assertTrue(recordList.contains(firstRecord));
        Assert.assertFalse(recordList.contains(secondRecord));


    }

    @Test(expected = NullPointerException.class)
    public void testDelete() throws Exception {
        String phoneNumber = getRandomNumber();
        String people = getRandomName();

        Record record = new Record();
        record.setNumber(phoneNumber);
        record.setPeople(people);

        long recordSaveId = dao.save(record);
        dao.delete(recordSaveId);
        dao.get(recordSaveId);// Расчитываем на Exception

    }


}