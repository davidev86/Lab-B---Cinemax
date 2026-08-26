/**
 *  @Authors: Francesca Pelizzoni, matricola 751550 (VA) e da Davide Villa, matricola 701105 (VA) 
 */
package cinemax.clientCM.callback;

import cinemax.contracts.dto.ui.ProjectionDetailsView;

/**
 * Interfaccia callback per gestire la selezione e la deselezione di una proiezione cinematografica nell'interfaccia utente.
 */
public interface SelezioneProjectionCallBack {


       void onSelezione(ProjectionDetailsView projectionDetails);
        void offSelezione(String errorMessage);
	
}


