package com.crossover.trial.weather.domains;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * A collected point, including some information about the range of collected values
 *
 * @author code test administrator
 */
public class DataPoint {

    public double mean = 0.0;

    public int first = 0;

    public int median = 0;

    public int last = 0;

    public int count = 0;

    /**
     * private constructor, use the builder to create this object
     */
    private DataPoint() {
    }

    protected DataPoint(int first, int median, double mean, int last, int count) {
        this.setFirst(first);
        this.setMean(mean);
        this.setMedian(median);
        this.setLast(last);
        this.setCount(count);
    }

    /**
     * the mean of the observations
     */
    public double getMean() {
        return mean;
    }

    public void setMean(double mean) {
        this.mean = mean;
    }

    /**
     * 1st quartile -- useful as a lower bound
     */
    public int getFirst() {
        return first;
    }

    protected void setFirst(int first) {
        this.first = first;
    }

    /**
     * 2nd quartile -- median value
     */
    public int getMedian() {
        return median;
    }

    protected void setMedian(int median) {
        this.median = median;
    }

    /**
     * 3rd quartile value -- less noisy upper value
     */
    public int getLast() {
        return last;
    }

    protected void setLast(int last) {
        this.last = last;
    }

    /**
     * the total number of measurements
     */
    public int getCount() {
        return count;
    }

    protected void setCount(int count) {
        this.count = count;
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.NO_CLASS_NAME_STYLE);
    }

    public boolean equals(Object that) {
        return this.toString().equals(that.toString());
    }

    static public class Builder {
        int first;
        double mean;
        int median;
        int last;
        int count;

        public Builder() {
        }

        public Builder withFirst(int first) {
            this.first = first;
            return this;
        }

        public Builder withMean(double mean) {
            this.mean = mean;
            return this;
        }

        public Builder withMedian(int median) {
            this.median = median;
            return this;
        }

        public Builder withCount(int count) {
            this.count = count;
            return this;
        }

        public Builder withLast(int last) {
            this.last = last;
            return this;
        }

        public DataPoint build() {
            return new DataPoint(this.first, this.median, this.mean,this.last, this.count);
        }
    }
}
