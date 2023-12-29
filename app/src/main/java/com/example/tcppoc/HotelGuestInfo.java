package com.example.tcppoc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HotelGuestInfo {

    private String id;
    private String company;
    private String hotelId;
    private String guestName;
    private List<String> channels;
    private String roomNumber;
    private String guestLanguage;
    private String reservationNumber;
    private String guestShare;
    private String guestVipStatus;
    private String guestGroupNumber;
    private String checkInTime;
    private String checkoutTime;
    private String active;
    private String metadata;
    private String className;
    private String contactNumber;
    private String guestEmailId;

    public HotelGuestInfo() {

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getGuestName() {
        return guestName;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getGuestLanguage() {
        return guestLanguage;
    }

    public void setGuestLanguage(String guestLanguage) {
        this.guestLanguage = guestLanguage;
    }

    public String getReservationNumber() {
        return reservationNumber;
    }

    public void setReservationNumber(String reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public String getGuestShare() {
        return guestShare;
    }

    public void setGuestShare(String guestShare) {
        this.guestShare = guestShare;
    }

    public String getGuestVipStatus() {
        return guestVipStatus;
    }

    public void setGuestVipStatus(String guestVipStatus) {
        this.guestVipStatus = guestVipStatus;
    }

    public String getGuestGroupNumber() {
        return guestGroupNumber;
    }

    public void setGuestGroupNumber(String guestGroupNumber) {
        this.guestGroupNumber = guestGroupNumber;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckoutTime() {
        return checkoutTime;
    }

    public void setCheckoutTime(String checkoutTime) {
        this.checkoutTime = checkoutTime;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getGuestEmailId() {
        return guestEmailId;
    }

    public void setGuestEmailId(String guestEmailId) {
        this.guestEmailId = guestEmailId;
    }



    public Map<String, Object> toMap() {
        Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("documentId", this.getId());
        properties.put("company", this.getCompany());
        properties.put("hotelId", this.getHotelId());
        properties.put("guestName", this.getGuestName());
        ArrayList<String> channels = new ArrayList<>();
        channels.add(getHotelId());
        properties.put("roomNumber", this.getRoomNumber());
        properties.put("guestLanguage", this.getGuestLanguage());
        properties.put("reservationNumber", this.getReservationNumber());
        properties.put("guestShare", this.getGuestShare());
        properties.put("guestVipStatus", this.getGuestVipStatus());
        properties.put("guestGroupNumber", this.getGuestGroupNumber());
        properties.put("checkInTime", this.getCheckInTime());
        properties.put("checkoutTime", this.getCheckoutTime());
        properties.put("active", this.getActive());
        properties.put("metadata", this.getMetadata());
        properties.put("className", "hotelguestinfo");
        properties.put("contactNumber", this.getContactNumber());
        properties.put("guestEmailId", this.getGuestEmailId());

        return properties;
    }
}

