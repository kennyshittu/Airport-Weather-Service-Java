package com.crossover.trial.weather.domains;

/**
 * encapsulates sensor information for a particular location
 */
public final class AtmosphericInformation {

    /**
     * mTemperature in degrees celsius
     */
    private DataPoint mTemperature;

    /**
     * mWind speed in km/h
     */
    private DataPoint mWind;

    /**
     * mHumidity in percent
     */
    private DataPoint mHumidity;

    /**
     * mPrecipitation in cm
     */
    private DataPoint mPrecipitation;

    /**
     * mPressure in mmHg
     */
    private DataPoint mPressure;

    /**
     * cloud cover percent from 0 - 100 (integer)
     */
    private DataPoint mCloudCover;

    /**
     * the mLast time this data was updated, in milliseconds since UTC epoch
     */
    private long mLastUpdateTime;

    public AtmosphericInformation() {

    }

    public DataPoint getTemperature() {
        return mTemperature;
    }

    public void setTemperature(DataPoint temperature) {
        this.mTemperature = temperature;
    }

    public DataPoint getWind() {
        return mWind;
    }

    public void setWind(DataPoint wind) {
        this.mWind = wind;
    }

    public DataPoint getHumidity() {
        return mHumidity;
    }

    public void setHumidity(DataPoint humidity) {
        this.mHumidity = humidity;
    }

    public DataPoint getPrecipitation() {
        return mPrecipitation;
    }

    public void setPrecipitation(DataPoint precipitation) {
        this.mPrecipitation = precipitation;
    }

    public DataPoint getPressure() {
        return mPressure;
    }

    public void setPressure(DataPoint pressure) {
        this.mPressure = pressure;
    }

    public DataPoint getCloudCover() {
        return mCloudCover;
    }

    public void setCloudCover(DataPoint cloudCover) {
        this.mCloudCover = cloudCover;
    }

    public long getLastUpdateTime() {
        return this.mLastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.mLastUpdateTime = lastUpdateTime;
    }

    public boolean hasDataPoint() {
        return this.getCloudCover() != null || this.getHumidity() != null
            || this.getPrecipitation() != null || this.getPressure() != null
            || this.getTemperature() != null || this.getWind() != null;
    }
}
