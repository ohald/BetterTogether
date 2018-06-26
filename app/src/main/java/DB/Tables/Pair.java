package DB.Tables;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import java.util.Date;

import DB.DataConverter;
import static android.arch.persistence.room.ForeignKey.NO_ACTION;

@Entity(tableName = "pair_table", foreignKeys = {
        @ForeignKey(entity = Person.class, parentColumns = "username", childColumns = "person1", onDelete = NO_ACTION),
        @ForeignKey(entity = Person.class, parentColumns = "username", childColumns = "person2", onDelete = NO_ACTION),

},  indices = {@Index("person1"),
               @Index("person2")}
)
public class Pair {

    private String person1;
    private String person2;

    @PrimaryKey
    @TypeConverters(DataConverter.class)
    private Date date;

    public Pair (Date date){
        this.date = date;
    }

    public String getPerson1() {
        return person1;
    }

    public void setPerson1(String person1) {
        this.person1 = person1;
    }

    public String getPerson2() {
        return person2;
    }

    public void setPerson2(String person2) {
        this.person2 = person2;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
