package Entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "warehouse")
public class Warehouse {
   @Id
   Integer W_ID;
   String W_NAME;
   String W_STREET_1;
   String W_STREET2;
   String W_CITY;
   String W_STATE;
   String W_ZIP;
   Double W_TAX;
   Double W_YTD;
}
