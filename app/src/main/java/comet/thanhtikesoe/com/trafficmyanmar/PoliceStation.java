package comet.thanhtikesoe.com.trafficmyanmar;

public class PoliceStation {


    private String mStationName;
    private String mPhoneno;

    public static final PoliceStation[] policeStations = {
            new PoliceStation("Changi Contact Centre", "6595-6868"),
            new PoliceStation("Flight Information", "1800-542-4422"),
            new PoliceStation("Inti Directory Assistance", "104"),
            new PoliceStation("Left Baggage", "6214-0448"),
            new PoliceStation("Left Baggage(Termainal 1)", "6214-0318"),
            new PoliceStation("Left Baggage(Termainal 2", "6214-0448"),
            new PoliceStation("Weather", "4642-7788")

    };

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
