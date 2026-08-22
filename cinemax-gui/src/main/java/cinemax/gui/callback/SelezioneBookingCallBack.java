package cinemax.gui.callback;

import cinemax.contracts.dto.BookingDetails;

public interface SelezioneBookingCallBack {

       void onSelezione(BookingDetails bookingDetails);
        void offSelezione(String errorMessage);	
}
