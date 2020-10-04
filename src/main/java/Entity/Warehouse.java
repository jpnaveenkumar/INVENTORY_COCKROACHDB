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

   public Integer getW_ID() {
      return W_ID;
   }

   public void setW_ID(Integer w_ID) {
      W_ID = w_ID;
   }

   public String getW_NAME() {
      return W_NAME;
   }

   public void setW_NAME(String w_NAME) {
      W_NAME = w_NAME;
   }

   public String getW_STREET_1() {
      return W_STREET_1;
   }

   public void setW_STREET_1(String w_STREET_1) {
      W_STREET_1 = w_STREET_1;
   }

   public String getW_STREET2() {
      return W_STREET2;
   }

   public void setW_STREET2(String w_STREET2) {
      W_STREET2 = w_STREET2;
   }

   public String getW_CITY() {
      return W_CITY;
   }

   public void setW_CITY(String w_CITY) {
      W_CITY = w_CITY;
   }

   public String getW_STATE() {
      return W_STATE;
   }

   public void setW_STATE(String w_STATE) {
      W_STATE = w_STATE;
   }

   public String getW_ZIP() {
      return W_ZIP;
   }

   public void setW_ZIP(String w_ZIP) {
      W_ZIP = w_ZIP;
   }

   public Double getW_TAX() {
      return W_TAX;
   }

   public void setW_TAX(Double w_TAX) {
      W_TAX = w_TAX;
   }

   public Double getW_YTD() {
      return W_YTD;
   }

   public void setW_YTD(Double w_YTD) {
      W_YTD = w_YTD;
   }
}
