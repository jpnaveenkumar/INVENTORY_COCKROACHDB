package Entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "district")
public class District {
    @EmbeddedId
    DistrictId districtId;
    String D_NAME;
    String D_STREET_1;
    String D_STREET_2;
    String D_CITY;
    String D_STATE;
    String D_ZIP;
    Double D_TAX;
    Double D_YTD;
    Integer D_NEXT_O_ID;

    public DistrictId getDistrictId() {
        return districtId;
    }

    public void setDistrictId(DistrictId districtId) {
        this.districtId = districtId;
    }

    public String getD_NAME() {
        return D_NAME;
    }

    public void setD_NAME(String d_NAME) {
        D_NAME = d_NAME;
    }

    public String getD_STREET_1() {
        return D_STREET_1;
    }

    public void setD_STREET_1(String d_STREET_1) {
        D_STREET_1 = d_STREET_1;
    }

    public String getD_STREET_2() {
        return D_STREET_2;
    }

    public void setD_STREET_2(String d_STREET_2) {
        D_STREET_2 = d_STREET_2;
    }

    public String getD_CITY() {
        return D_CITY;
    }

    public void setD_CITY(String d_CITY) {
        D_CITY = d_CITY;
    }

    public String getD_STATE() {
        return D_STATE;
    }

    public void setD_STATE(String d_STATE) {
        D_STATE = d_STATE;
    }

    public String getD_ZIP() {
        return D_ZIP;
    }

    public void setD_ZIP(String d_ZIP) {
        D_ZIP = d_ZIP;
    }

    public Double getD_TAX() {
        return D_TAX;
    }

    public void setD_TAX(Double d_TAX) {
        D_TAX = d_TAX;
    }

    public Double getD_YTD() {
        return D_YTD;
    }

    public void setD_YTD(Double d_YTD) {
        D_YTD = d_YTD;
    }

    public Integer getD_NEXT_O_ID() {
        return D_NEXT_O_ID;
    }

    public void setD_NEXT_O_ID(Integer d_NEXT_O_ID) {
        D_NEXT_O_ID = d_NEXT_O_ID;
    }
}
