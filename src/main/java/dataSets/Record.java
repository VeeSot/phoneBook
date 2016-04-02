package dataSets;

import javax.persistence.*;

/**
 * veesot on 4/2/16.
 */
@Entity
@Table(name = "records")
public class Record {
    private long recordId;
    private String people;
    private String number;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "record_id")
    public long getRecordId() {
        return recordId;
    }


    private void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    @Basic
    @Column(name = "people")
    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    @Basic
    @Column(name = "number")
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Record record = (Record) o;

        if (getPeople() != null ? !getPeople().equals(record.getPeople()) : record.getPeople() != null) return false;
        return getNumber() != null ? getNumber().equals(record.getNumber()) : record.getNumber() == null;

    }

    @Override
    public int hashCode() {
        int result = getPeople() != null ? getPeople().hashCode() : 0;
        result = 31 * result + (getNumber() != null ? getNumber().hashCode() : 0);
        return result;
    }
}
