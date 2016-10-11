package comet.thanhtikesoe.com.trafficmyanmar.Model;

public class PoliceStation {

    private String mStationName;
    private String mPhoneno;

    public PoliceStation(String mName, String mPhone) {

        this.mStationName = mName;
        this.mPhoneno = mPhone;
    }

    public String getmStationName() {
        return mStationName;
    }

    public void setmStationName(String mStationName) {
        this.mStationName = mStationName;
    }

    public String getmPhoneno() {
        return mPhoneno;
    }

    public void setmPhoneno(String mPhoneno) {
        this.mPhoneno = mPhoneno;
    }
}
