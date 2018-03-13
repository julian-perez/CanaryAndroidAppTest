package is.yranac.canary.model.google;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Schroeder on 9/17/14.
 */
public class GooglePlaceDetails {

    @SerializedName("address_components")
    public List<AddressComponent> addressComponents;

    @SerializedName("formatted_address")
    public String formattedAddress;

    @SerializedName("international_phone_number")
    public String internationalPhoneNumber;

    @SerializedName("geometry")
    public Geometry geometry;

    @SerializedName("id")
    public String id;

    @SerializedName("place_id")
    public String placeId;

    @SerializedName("reference")
    public String reference;

    @SerializedName("types")
    public List<String> types;

}
