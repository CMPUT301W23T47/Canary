package com.cmput301w23t47.canary.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.GeoPoint;

/**
 * Class for Qrcodem
 * this is a model for how to store the qrcodes for the map
 */
public class Qrcodem  implements Parcelable {
    private String qrHash;
    private Location location;
    private String name;
    private int score;
    
    /**
     * Constructor for Qrcodem
     * allows location to be passed in
     * @param qrHash the hash of the QR code
     * @param location the location of the QR code
     * @param name the name of the QR code
     * @param score the score of the QR code
     */
    public Qrcodem(String qrHash, Location location, String name, int score){
        this.qrHash = qrHash;
        this.location = location;
        this.name = name;
        this.score = score;
    }
    
    /**
     * Constructor for Qrcodem
     * allows a geopoint to be passed in
     * this makes it a little easier to use this class
     * @param qrHash the hash of the QR code
     * @param geo the location of the QR code
     * @param name the name of the QR code
     * @param score the score of the QR code
     */
    public Qrcodem(String qrHash, GeoPoint geo, String name, int score){
        this.qrHash = qrHash;
        this.location = new Location("");
        this.location.setLatitude(geo.getLatitude());
        this.location.setLongitude(geo.getLongitude());
        this.name = name;
        this.score = score;
    }
    
    /**
     * Construct an empty Qrcodem
     */
    public Qrcodem() {
    }
    
    
    /**
     * Get the hash of the QR code
     * @return qrHash - the hash of this QR code
     */
    public String getQrHash() {
        return qrHash;
    }
    
    /**
     * Set the hash of the QR code
     * @param qrHash the hash of the QR code
     */
    public void setQrHash(String qrHash) {
        this.qrHash = qrHash;
    }

    
    //location with location
    /**
     * Get the location of the QR code
     * @return location - the location of the QR code in Location format
     */
    public Location getLocation() {
        return location;
    }
    
    /**
     * Set the location of the QR code
     * @param location the location of the QR code in Location format
     */
    public void setLocation(Location location) {
        this.location = location;
    }
    
    //location with geopoint
    
    /**
     * Get the location of the QR code
     * @return geopoint - the location of the QR code in GeoPoint format
     */
    public GeoPoint getGeoPoint(){
        return new GeoPoint(location.getLatitude(), location.getLongitude());
    }
    /**
     * set the location of the QR code
     * @param geo the location of the QR code in GeoPoint format
     */
    public void setLocation(GeoPoint geo){
        this.location = new Location("");
        this.location.setLatitude(geo.getLatitude());
        this.location.setLongitude(geo.getLongitude());
    }
    
    
    
    /**
     * Get the name of the QR code
     * @return name - the name of the QR code
     */
    public String getName() {
        return name;
    }

    /**
     * Set the name of the QR code
     * @param name the name of the QR code
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the score of the QR code
     * @return score - the score of the QR code
     */
    public int getScore() {
        return score;
    }

      /**
      * Set the score of the QR code
      * @param score the score of the QR code
      */
    public void setScore(int score) {
        this.score = score;
    }
    
    /**
     * Get the score of the QR code as a string
     * @return score - the score of the QR code as a string
     */
    public String getScoreString(){
        return String.valueOf(score);
    }

    //added for tthe implementation of parcelable

    /**
     * Constructor for Qrcodem
     * allows a parcel to be passed in
     * @param in the parcel to be passed in
     *           qrHash the hash of the QR code
     *           location the location of the QR code
     *           name the name of the QR code
     *           score the score of the QR code
     *
     */
    protected Qrcodem(Parcel in) {
        qrHash = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
        name = in.readString();
        score = in.readInt();
    }
    
    /**
     * Describe the kinds of special objects contained in this Parcelable's marshalled representation.
     * @return 0 - we don't have any special objects
     * @see Parcelable
     */
    @Override
    public int describeContents() {
        return 0;
    }
    
    /**
     * Flatten this object in to a Parcel.
     * @param dest The Parcel in which the object should be written.
     * @param flags Additional flags about how the object should be written.
     * May be 0 or {@link #PARCELABLE_WRITE_RETURN_VALUE}.
     */
    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(qrHash);
        dest.writeParcelable(location, flags);
        dest.writeString(name);
        dest.writeInt(score);
    }

    /**
     * The creator for Qrcodem
     */
    public static final Parcelable.Creator<Qrcodem> CREATOR = new Parcelable.Creator<Qrcodem>() {
        @Override
        public Qrcodem createFromParcel(Parcel in) {
            return new Qrcodem(in);
        }

        @Override
        public Qrcodem[] newArray(int size) {
            return new Qrcodem[size];
        }
    };
    
    public QrCode toQrCode(){
        return new QrCode(qrHash, score,location,name);
    }

}
