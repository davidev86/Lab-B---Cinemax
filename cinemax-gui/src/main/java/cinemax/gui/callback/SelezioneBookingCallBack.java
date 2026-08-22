package cinemax.gui.callback;

import cinemax.contracts.dto.BookingDetails;

public interface SelezioneBookingCallBack {

       void onSelezione(BookingDetails bookingDetails, Integer idPrenotazione);
        void offSelezione(String errorMessage);	
}
