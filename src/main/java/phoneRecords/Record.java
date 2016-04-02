package phoneRecords;

/**
 * veesot on 4/2/16.
 */
public class Record {
    private String people;
    private String number;

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

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
