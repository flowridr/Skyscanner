
package com.tilapia.skyscannerplus.models;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PricingOption {

    @SerializedName("Agents")
    @Expose
    private List<Integer> agents = null;
    @SerializedName("QuoteAgeInMinutes")
    @Expose
    private Integer quoteAgeInMinutes;
    @SerializedName("Price")
    @Expose
    private Double price;
    @SerializedName("DeeplinkUrl")
    @Expose
    private String deeplinkUrl;

    public List<Integer> getAgents() {
        return agents;
    }

    public void setAgents(List<Integer> agents) {
        this.agents = agents;
    }

    public Integer getQuoteAgeInMinutes() {
        return quoteAgeInMinutes;
    }

    public void setQuoteAgeInMinutes(Integer quoteAgeInMinutes) {
        this.quoteAgeInMinutes = quoteAgeInMinutes;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDeeplinkUrl() {
        return deeplinkUrl;
    }

    public void setDeeplinkUrl(String deeplinkUrl) {
        this.deeplinkUrl = deeplinkUrl;
    }

}
