/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.clientCM.callback;

import cinemax.contracts.dto.BookingDetails;

/**
 * Callback utilizzata per notificare eventi di selezione/visualizzazione di una prenotazione.
 * Implementatori possono gestire scenario di selezione, deselezione o errori.
 */
public interface SelezioneBookingCallBack {

    /**
     * Invocato quando una prenotazione viene selezionata.
     * @param bookingDetails dettagli della prenotazione selezionata
     * @param idPrenotazione id numerico della prenotazione (se disponibile)
     */
    void onSelezione(BookingDetails bookingDetails, Integer idPrenotazione);

    /**
     * Invocato quando la selezione viene rimossa o si verifica un errore.
     * @param errorMessage messaggio di errore o motivazione della deselezione
     */
    void offSelezione(String errorMessage);

    /**
     * Variante semplice di onSelezione che fornisce solo i dettagli della prenotazione.
     * @param bookingDetails dettagli della prenotazione selezionata
     */
    void onSelezioneBooking(BookingDetails bookingDetails);
}


