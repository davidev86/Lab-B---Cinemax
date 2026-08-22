package cinemax.gui.callback;

import cinemax.application.services.BookingService;
import cinemax.application.services.TcpClient;
import cinemax.contracts.dto.BookingDetails;


public interface SelezioneBookingCallBack {


       void onSelezione(BookingDetails bookingDetails);
        void offSelezione(String errorMessage);




	
	
}
