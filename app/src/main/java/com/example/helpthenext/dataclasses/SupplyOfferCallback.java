package com.example.helpthenext.dataclasses;

import java.util.ArrayList;

public interface SupplyOfferCallback {
        void loadSupplyInfo(ArrayList<SupplyOffer> supplyOffers);
        void loadHomeInfo(ArrayList<HomeOffer> homeOffers);
}
